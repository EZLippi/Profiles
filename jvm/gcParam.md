1. 基本参数,设置新生代、老年代、Metaspace、codeCache大小:
    -Xms6g -Xmx6g -Xmn2g -XX:SurvivorRatio=6 -XX:MetaspaceSize=800m -XX:MaxMetaspaceSize=800m -XX:ReservedCodeCacheSize=128m

2. 设置GC算法:
    -XX:+UseConcMarkSweepGC -XX:+UseParNewGC 

3. 优化CMS GC的一些参数:

3.1 当NIO框架显式调用system.gc()时使用CMS GC而不是触发一次Full GC
    -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
3.2 当使用CMS GC时会有一个线程去检查老年代的大小来决定是否触发一次CMS GC：    
    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 
3.3 防止GC线程占用虚拟机太多CPU的参数
    -XX:ParallelGCThreads=6 设置CMS阈值及线程数 -XX:CICompilerCount=2设置JIT编译线程数

4. 辅助分析的一些参数:
    -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/dev/shm/gc.log -XX:+PrintClassHistogram -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=5M
    -XX:+PrintTenuringDistribution 在每次新生代GC时，输出幸存区中对象的年龄分布,观察分析后再通过参数设置进入老年代的阈值: -XX:MaxTenuringThreshold=2
    参考:http://ifeve.com/useful-jvm-flags-part-5-young-generation-garbage-collection/
    -XX:+PrintTLAB  打印线程局部分配区域信息

5. 其他有用的参数:
-XX:AutoBoxCacheMax=20000 自动装箱默认缓存-128~127
-XX:+AlwaysPreTouch 启动时访问并置零内存页面
-Djava.security.egd=file:/dev/./urandom 原用于Tomcat显式使用SHA1PRNG算法时，初始因子从/dev/random读取导致堵塞。而使用此设置后，额外效果是默认的SecureRandom算法也变成SHA1了
-XX:MaxTenuringThreshold=2  对象在Survivor区熬过多少次Young GC后晋升到年老代，JDK7里看起来默认是6，跑起来好像变成了15

6. JIT编译: 
    -XX:+PrintCompilation  -XX:ReservedCodeCacheSize= XX:-UseCodeCacheFlushing


7. 打印安全点信息,用于检查系统停顿的原因: -XX:+PrintGCApplicationStoppedTime 会在GC日志中输出应用停止时间,不仅仅是GC停顿时间
    -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 会在stdout输出安全点统计信息
    如果有很多并且很短的安全点,很有可能是取消偏向锁导致的,建议添加参数: -XX:-UseBiasedLocking
    如果要在生产环境中使用, -XX:+UnlockDiagnosticVMOptions -XX: -DisplayVMOutput -XX:+LogVMOutput -XX:LogFile=/dev/shm/vm.log
    打开Diagnostic,关掉输出VM日志到stdout，输出到/dev/shm目录（内存文件系统)
8. 查看当前JVM打开的所有参数:   -XX:+PrintFlagsFinal

9. JVM经常会默默的在/tmp/hperf目录写上一点statistics数据，如果刚好遇到PageCache刷盘，把文件阻塞了就不能结束这个STW的安全点了。用此参数可以禁止JVM写statistics数据，代价是jps, jstat 用不了，只能用JMX取数据
    -XX:+PerfDisableSharedMem
    
10. JMX相关:
    -Dcom.sun.management.jmxremote.port=${MY_JMX_PORT} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1   
    
11.  -XX:AutoBoxCacheMax=20000 增加了IntegerCache池的大小，吞吐量提高3%

12. 如果在代码里某个特定位置被抛出过多次的话，HotSpot Server Compiler（C2）会透明的决定用fast throw来优化抛出异常的地方,异常message和stack trace都被清空，可以开启这个参数禁用这个优化:-XX:-OmitStackTraceInFastThrow。
 
http://www.techpaste.com/2012/02/java-command-line-options-jvm-performance-improvement/
http://hllvm.group.iteye.com/group/topic/27945
http://calvin1978.blogcn.com/articles/jvmoption-2.html
https://blogs.oracle.com/poonam/why-do-i-get-message-codecache-is-full-compiler-has-been-disabled
