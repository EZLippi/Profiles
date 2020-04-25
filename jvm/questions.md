## Java基础

* ArrayList和LinkedList区别
* HashMap 1.7,1.8,ConcurrentHashMap size()方法实现，返回值是否准确
* 1.7 ConcurrentHashMap的Segment继承自ReentrantLock,1.8 通过synchronized锁住的是head节点(减少内存占用,synchronized锁能够运行时优化)
* 没有有顺序的Map实现类，如果有，他们是怎么保证有序的
* 线程池参数，核心池的设置依据, 如果Runnable里抛出RuntimeException,线程还在吗
* Lock和Synchronized区别，AQS,公平锁和非公平锁, Synchronized 锁的升降级机制
* 线程有哪些状态，以及是怎么转换的
* sleep和wait的区别
* Object.finialize()方法原理
* 列举3个以上的RuntimeException
* ni耦合bio,JDK epoll的bug，netty是怎么解决的，怎么解决TCP粘报和拆包的问题
* JVM内存布局，为什么要分代收集,object对象内存布局，一个空Object对象的占多大空间？
* 多线程通信有哪些方式
* 常用的GC算法，哪些对象可以作为GC ROOT, 有没有分析过GC日志里有很多小的停顿, 哪些场景会进入安全点
* 线上jdk版本，用了哪些jvm参数，什么场景下会触发一次GC
* 哪些场景会出现内存泄漏,HashMap，ThreadLocal,WeakHashMap,Finalze
* Tomcat ClassLoader机制, servlet-api 的scope为什么要设置为provided
* 泛型extends和super
* 调用System.gc()会立马进行垃圾回收吗
* HashSet 是如何保证没有重复元素的
* JUC包下面主要的类
* 使用 ThreadLocal 需要注意些什么
* 哪些场景用到了spi机制
* 怎么中断一个线程
* final关键字的用途
* 什么情况下会发生栈内存溢出



## Spring框架

* Spring AOP是怎么实现的，jdk动态代理和CGLib实现性能差别
* spring框架有哪些模块
* 谈谈你对IOC理解，Spring BeanFactory和FactoryBean
* Spring事务A调用B的问题
* spring bean生命周期方法,initializingBean,@PostConstruct,init-method顺序
* @Transactional注解不生效的场景
* springboot 的profile怎么管理
* 讲讲Spring事务的传播属性
* 执行某操作，前50次成功，第51次失败
a 全部回滚(父类方法加事务PROPAGATION_REQUIRED )
b 前50次提交第51次抛异常，ab场景分别如何设置Spring（传播性）(父类方法加事务PROPAGATION_REQUIRED ,子类方法加事务PROPAGATION_REQUIRES_NEW)

## Netty

* bossGroup和workerGroup,NIOEventLoop线程池实现机制(一个线程一个队列)
* headContext和tailContext 

## Mysql

* mysql锁加在什么位置,myisam和innodb的区别
* 结合InnoDB的实现阐述事务的隔离级别,分别怎么解决脏读取，幻读和不可重复读
* 对Mysql最新版本的同步机制有没有了解，比如一个主节点多个slave节点怎么保证可用性和性能
* 如果发现mysql进程的CPU使用率500%，你要怎么处理(show processlist)
* 你们数据库是否支持emoji表情，如果不支持，如何操作？
* SQL优化的一般步骤是什么，怎么看执行计划，如何理解其中各个字段的含

## MyBatis

* 如何获取自动生成的(主)键值
* mybatis如何实现多数据源读写分离
* mybatis缓存实现

## Redis

* 常用的数据结构，skipList和zipList实现
* AOF和RDB
* 如何获取某个前缀的所有key


## 分布式

* 如何实现分布式锁,redis实现方式中如果超时了还没执行完怎么办
* Dubbo实现原理，服务发现机制
* 分布式ID生成
* 分布式事务实现方式
* zookeeper选举算法
* zookeeper对节点的watch监听通知是永久的吗


## 问题定位

* 如何定位系统响应慢的问题
* 有个节点一直在Full GC，怎么快速定位并恢复环境
* 本地Tomcat部署时发现启动非常慢,怎么定位

## 协议栈

* http协议格式，get和post的区别
* https握手机制
* TCP backlog参数的作用
* Linux系统下你关注过哪些内核参数，说说你知道的
* 平时用到哪些Linux命令

## 工具

* git的分支怎么管理的

## 场景分析

* 微博的阅读数以及用户的关注数

## 做题

* 用2个线程按顺序循环打印ab 2个字母

A B C
A［］array ＝ new B［2］;
A［0］＝ new C()
用泛型优化
