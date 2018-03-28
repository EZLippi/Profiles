
# 慎用java的finalize方法

我们通常用构造器来创建对象,而Finalize正好相反,构造方法执行对象的初始化操作,finalize方法执行对象的销毁操作.
那我们什么时候需要使用finalize方法呢,我们都知道Java里垃圾回收器可以回收对象使用的内存空间,但是对象可能会
持有很多资源比如Socket、文件句柄等,垃圾收集器无法回收这些资源,因此你需要使用finalize方法帮助GC回收这些
资源,比如关闭打开的文件或者网元资源,删除临时文件等.

Object类是所有类的父类,如果你去查看java.lang.Object类的源码,你会发现里面有个finalize方法,这个方法
没有默认实现,需要子类根据实际情况重写这个方法,但是如果不恰当使用finalize方法可能会造成很大的负面影响,比如下面的
例子:
``` java
public class finalizer {
    @Override
    protected void finalize() throws Throwable {
    while (true) {
           Thread.yield();
      }
  }

public static void main(String str[]) {
  while (true) {
        for (int i = 0; i < 100000; i++) {
            finalizer force = new finalizer();
        }
   }
 }
}
```
当我们运行上述代码时,可以看到创建大量的Finalizer对象,运行一段时间后一般出现以下两种结果:
1. JVM异常退出并且生成了内存镜像Dump
2. JVM抛出了一个异常:Out of Memory:GC OverHead limit exceeded.

不管上述两种情况,JVM都崩溃了,那到底执行finalize方法时发生了什么.Jvm会给每个实现了finalize方法的实例创建一个监听,
这个称为Finalizer,每次调用对象的finalize方法时,JVM会创建一个java.lang.ref.Finalizer对象,这个Finalizer对象会持有
这个对象的引用,由于这些对象被Finilizer对象引用了,当对象数量较多时,就会导致Eden区空间满了,经历多次youngGC后可能对象就
进入到老年代了.
java.lang.ref.Finalizer类继承自java.lang.ref.FinalReference,也是Refence的一种,因此Finalizer类里也有一个引用队列,
这个引用队列是JVM和垃圾回收器打交道的唯一途径,当垃圾回收器需要回收该对象时,会把该对象放到引用队列中,这样java.lang.ref.Finalizer类
就可以从队列中取出该对象,执行对象的finalize方法,并清除和该对象的引用关系.需要注意的是只有finalize方法实现不为空时JVM才会执行上述操作,
JVM在类的加载过程中会标记该类是否为finalize类.

## GC怎么处理这些对象呢

当老年代空间达到了OldGC条件时,JVM执行一次OldGC,当OldGC执行后JVM检测到这些对象只被Finalizer对象引用,这些对象会被标记成要
被清除的对象,GC会把所有的Finalizer对象放入到一个引用队列:java.lang.ref.Finalizer.ReferenceQueue.

## Finalizer对象怎么被清理的呢

JVM默认会创建一个finalizer线程来处理Finalizer对象,如果你去抓取线程堆栈的话可以看到这个线程的堆栈,如下所示:
```bash
"Finalizer" daemon prio=10 tid=0x0962d000 nid=0x4836 runnable [0xafaa8000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.Thread.yield(Native Method)
        at finalizer.finalize(finalizer.java:5)
        at java.lang.ref.Finalizer.invokeFinalizeMethod(Native Method)
        at java.lang.ref.Finalizer.runFinalizer(Finalizer.java:83)
        at java.lang.ref.Finalizer.access$100(Finalizer.java:14)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:160)
```
这个线程唯一的职责就是不断的从java.lang.ref.Finalizer.ReferenceQueue队列中取对象,当一个对象进入到队列中,finalizer线程就执行对象
的finalize方法并且把对象从队列中删除,因此在下一次GC周期中可以看到这个对象和Finalizer对象都被清除了.

大部分场景finalizer线程清理finalizer队列是比较快的,但是一旦你在finalize方法里执行一些耗时的操作,可能导致内存无法及时释放进而导致
内存溢出的错误,在实际场景还是推荐尽量少用finalize方法.

## 实战案例

``` java
    public class Finalizer {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize");
        }

        public static void main(String str[]) throws IOException {
                for (int i = 0; i < 10000; i++) {
                    Finalizer force = new Finalizer();
                }
                //让线程阻塞住,方便分析内存使用情况
            System.in.read();
        }
    }
```
执行main方法后使用jmap命令查看内存使用情况,可以看到java.lang.ref.Finalizer和Finalizer的实例都创建了10000个,
```bash
$ jmap -histo 8700|head -n 10

 num     #instances         #bytes  class name
----------------------------------------------
   1:           646        3398408  [I
   2:          1851        1511144  [B
   3:          6081         808864  [C
   4:         10175         407000  java.lang.ref.Finalizer
   5:         10000         160000  Finalizer
   6:          4328         103872  java.lang.String
   7:           601          64208  java.lang.Class
   8:           683          40952  [Ljava.lang.Object;
   9:           785          31400  java.util.TreeMap$Entry
  10:           248          14144  [Ljava.lang.String;
```

接下来使用```jmap -histo:live 8700|head -n 10```强制触发一次GC,结果和前面的分析一致,Finalizer对象都放到引用队列中,
并依次调用了对象的finalize方法,内存中java.lang.ref.Finalizer和Finalizer对象依然存在,不过这一次java.lang.ref.Finalizer
不再引用Finalizer对象,下一次GC周期时两者都属于垃圾对象:
```bash
$ jmap -histo:live 8700|head -n 10

 num     #instances         #bytes  class name
----------------------------------------------
   1:         10175         407000  java.lang.ref.Finalizer
   2:          3043         372608  [C
   3:           605         273624  [B
   4:         10000         160000  Finalizer
   5:          2883          69192  java.lang.String
   6:           601          64208  java.lang.Class
   7:           631          37008  [Ljava.lang.Object;
```

再触发一次```jmap -histo:live 8700|head -n 10```，可以看到两者都被回收了:
```bash
$ jmap -histo:live 8700|head -n 10

 num     #instances         #bytes  class name
----------------------------------------------
   1:          3059         373224  [C
   2:           498         138064  [B
   3:          2899          69576  java.lang.String
   4:           602          64312  java.lang.Class
   5:           631          37008  [Ljava.lang.Object;
   6:           785          31400  java.util.TreeMap$Entry
   7:           227          11256  [Ljava.lang.String;
```

最后我们来总结一下:
1. finalize对象至少经历两次GC才能被回收,因为只有在FinalizerThread执行完了finalize对象的finalize方法的情况下才有可能被
下次GC回收，而有可能期间已经经历过多次GC了，但是一直还没执行finalize对象的finalize方法;
2. CPU资源不足的场景FinalizerThread线程可能因为优先级较低而一直没有执行对象的finalize方法,可能导致大部分对象进入到老年代,
进而触发老年代GC,设置触发Full GC.
