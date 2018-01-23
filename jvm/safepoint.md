## 基本概念

学习JVM到一定程度后肯定会接触到safepoint(安全点)的概念,特别是在处理GC问题的时候,这篇文章主要介绍下
安全点的概念及如何在JVM环境中开启安全点日志来分析系统的性能.

<!-- more -->

安全点就是指代码中一些特定的位置,单线程运行到这些位置时它的状态是确定的,这样JVM就可以安全的进行一些操作,比如GC.
这些特定的位置主要有几下几种:
1. 方法返回之前
2. 调用某个方法之后
3. 抛出异常的位置
4. 循环的末尾

为什么把这些位置设置为jvm的安全点呢,主要目的就是避免程序长时间无法进入safepoint,比如JVM在做GC之前要等所有的应用
线程进入到安全点后VM线程才能分派GC任务 ,如果有线程一直没有进入到安全点,就会导致GC时JVM停顿时间延长,比如R大之前
回复的这个[例子](http://hllvm.group.iteye.com/group/topic/38232),这里面就是写了一个超大的循环导致线程
一直没有进入到安全点,GC前停顿了8秒.

## safepoint的使用场景

1. 垃圾回收(这是最常见的场景)
2. 取消偏向锁(JVM会使用偏向锁来优化锁的获取过程)
3. Class重定义(比如常见的hotswap和instrumentation)
4. Code Cache Flushing(JDK1.8在CodeCache满的情况下就可能出现)
5. 线程堆栈转储

既然这样,线程怎么知道什么时候要进入到saftpoint呢,常见的做法就是设置一个状态位,让所有线程去检查这个状态,当检测到
处于saftpoint时就停下来,可以看下OpenJdk里对safePoint的描述:
``` java
// Begin the process of bringing the system to a safepoint.
  // Java threads can be in several different states and are
  // stopped by different mechanisms:
  //
  //  1. Running interpreted
  //     The interpeter dispatch table is changed to force it to
  //     check for a safepoint condition between bytecodes.
  //  2. Running in native code
  //     When returning from the native code, a Java thread must check
  //     the safepoint _state to see if we must block.  If the
  //     VM thread sees a Java thread in native, it does
  //     not wait for this thread to block.  The order of the memory
  //     writes and reads of both the safepoint state and the Java
  //     threads state is critical.  In order to guarantee that the
  //     memory writes are serialized with respect to each other,
  //     the VM thread issues a memory barrier instruction
  //     (on MP systems).  In order to avoid the overhead of issuing
  //     a memory barrier for each Java thread making native calls, each Java
  //     thread performs a write to a single memory page after changing
  //     the thread state.  The VM thread performs a sequence of
  //     mprotect OS calls which forces all previous writes from all
  //     Java threads to be serialized.  This is done in the
  //     os::serialize_thread_states() call.  This has proven to be
  //     much more efficient than executing a membar instruction
  //     on every call to native code.
  //  3. Running compiled Code
  //     Compiled code reads a global (Safepoint Polling) page that
  //     is set to fault if we are trying to get to a safepoint.
  //  4. Blocked
  //     A thread which is blocked will not be allowed to return from the
  //     block condition until the safepoint operation is complete.
  //  5. In VM or Transitioning between states
  //     If a Java thread is currently running in the VM or transitioning
  //     between states, the safepointing code will wait for the thread to
  //     block itself when it attempts transitions to a new state.
```

大概意思就是当线程处于解释执行时,当有safepoint请求的时候，解释器就会把指令跳转到去进行safepoint状态检查;
 当Java线程正在执行native code的时候, 当VM thread看到一个Java线程在执行native code,
 它不需要等待这个Java线程进入阻塞状态，因为当Java线程从执行native code返回的时候,
 Java线程会去检查safepoint看是否要block,但是检查这个sync_state状态的操作不是原子的,有可能线程1读到的
 状态是_not_synchronized这时候线程CPU被抢占,VM线程把sync_state状态改成了_synchronizing,然后读取所有
 线程的状态是是否处于block或者in native状态,是的话就可以开始GC,否则就要等待.如果线程1在读sync_state状态
 和写线程状态这两个操作不是原子的就会出问题,线程1以为不是saftpoint状态而VM Thread以为所有线程都Ok了,这时候
 开启GC就会出问题,一旦线程1获取CPU时间片就会开始执行java代码,这样子GC就出错了.
 
 如何解决上面出现的状态不一致的问题呢,常见的方法就是加内存屏障,比如volatile语义就是通过内存屏障来实现,但是内存
 屏障的实现是一个重量级的操作,需要锁住总线或者CPU的Cache Line,而JVM不是采用的内存屏障来解决这个问题,而是采用
 了一个叫做serialization page的轻量级同步方法,所有线程都必须顺序的在serialization page内存页上更新自己的
 状态,当VM线程要执行GC时就把这个内存页设置为只读,其他线程就无法操作,关于serialization page可以参考这篇文章:
 [关于memory_serialize_page的一些疑问](http://hllvm.group.iteye.com/group/topic/38904)
 
 
## safepoint实战

在生产环境推荐使用-XX:+PrintGCApplicationStoppedTime这个参数来打印JVM暂停的时间,如果在GC日志前面出现较大
的停顿,那要考虑是不是代码里有大的循环操作,如下所示:
    Total time for which application threads were stopped: 8.8328410 seconds 
    2018-01-23T21:07:21.277+0800: 24021.914: [GC (Allocation Failure)
同时在测试环境可以开启以下几个参数来打印安全点统计日志:

    -XX:+UnlockDiagnosticVMOptions
    -XX:+LogVMOutput
    -XX:LogFile=/dev/shm/vm.log
    -XX:+PrintSafepointStatistics
    -XX:PrintSafepointStatisticsCount=1    
     
-XX:LogFile用户把jvm的输出到某个日志中,推荐写入到/dev/shm这个内存映射文件目录下,输出的结果类似这样:

    RevokeBias                           [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
    24201.686: GenCollectForAllocation          [    1710          0              0    ]      [     0     0     0     5   900    ]  0
上面的输出参数解释如下:
第一段是时间戳，VM Operation的类型，以及线程概况
* total: 安全点里的总线程数
* initially_running: 安全点时开始时正在运行状态的线程数
* wait_to_block: 在VM Operation开始前需要等待其暂停的线程数
第二段是到达安全点时的各个阶段以及执行操作所花的时间，其中最重要的是vmop
* spin: 等待线程响应safepoint号召的时间
* block: 暂停所有线程所用的时间
* sync: 等于 spin+block，这是从开始到进入安全点所耗的时间，可用于判断进入安全点耗时
* cleanup: 清理所用时间
* vmop: 真正执行VM Operation的时间

由于没有深入研究jvm源码,上面的研究只是停留在表明,希望后面继续学习和思考.
参考文档:
1. [safepoints-in-hotspot-jvm.](http://blog.ragozin.info/2012/10/safepoints-in-hotspot-jvm.html)
2. [理解JVM的safepoint](http://blog.csdn.net/iter_zc/article/details/41847887)
3. [jvm迟迟进入不到安全点的例子](http://hllvm.group.iteye.com/group/topic/38232)
4. [关于memory_serialize_page的一些疑问](http://hllvm.group.iteye.com/group/topic/38904)
