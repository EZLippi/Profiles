## Java基础
1. HashMap 1.7,1.8,ConcurrentHashMap size()方法实现，返回值是否准确
2. ArrayList和LinkedList区别
3. 线程池参数，核心池的设置依据, 如果Runnable里抛出RuntimeException,线程还在吗
4. Lock和Synchronized区别，AQS,公平锁和非公平锁, Synchronized 锁的升降级机制
5. 线程有哪些状态，以及是怎么转换的
6. Object.finialize()方法原理
7. 列举3个以上的RuntimeException
8. ni耦合bio,JDK epoll的bug，netty是怎么解决的，怎么解决TCP粘报和拆包的问题
9. JVM内存布局，为什么要分代收集,object对象内存布局，一个空Object对象的占多大空间？
10. 多线程通信有哪些方式
11. 常用的GC算法，哪些对象可以作为GC ROOT, 有没有分析过GC日志里有很多小的停顿, 哪些场景会进入安全点
12. 线上jdk版本，用了哪些jvm参数，什么场景下会触发一次GC
13. 哪些场景会出现内存泄漏,HashMap，ThreadLocal,WeakHashMap,Finalze
14. Tomcat ClassLoader机制, servlet-api 的scope为什么要设置为provided
15. 泛型extends和super
16. 调用System.gc()会立马进行垃圾回收吗
17. HashSet 是如何保证没有重复元素的
18. JUC包下面主要的类
19. 使用 ThreadLocal 需要注意些什么
20. 哪些场景用到了spi机制
21. 怎么中断一个线程


## Spring框架

1. Spring AOP是怎么实现的，jdk动态代理和CGLib实现性能差别
2. spring框架有哪些模块
3. 谈谈你对IOC理解，Spring BeanFactory和FactoryBean
4. Spring事务A调用B的问题
5. spring bean生命周期方法,initializingBean,@PostConstruct,init-method顺序
6. @Transactional注解不生效的场景
7. springboot 的profile怎么管理
8. 执行某操作，前50次成功，第51次失败
a 全部回滚(父类方法加事务PROPAGATION_REQUIRED )
b 前50次提交第51次抛异常，ab场景分别如何设置Spring（传播性）(父类方法加事务PROPAGATION_REQUIRED ,子类方法加事务PROPAGATION_REQUIRES_NEW)

## Netty
 1. bossGroup和workerGroup,NIOEventLoop线程池实现机制(一个线程一个队列)
 2. headContext和tailContext
 3. 

## Mysql

1. mysql锁加在什么位置,myisam和innodb的区别
2. 结合InnoDB的实现阐述事务的隔离级别,分别怎么解决脏读取，幻读和不可重复读
3. 对Mysql最新版本的同步机制有没有了解，比如一个主节点多个slave节点怎么保证可用性和性能
4. 如果发现mysql进程的CPU使用率500%，你要怎么处理(show processlist)
5. 你们数据库是否支持emoji表情，如果不支持，如何操作？

## MyBatis

1. 如何获取自动生成的(主)键值
2. mybatis如何实现多数据源读写分离
3. mybatis缓存实现

## Redis

1. 常用的数据结构，skipList和zipList实现
2. AOF和RDB
3. 如何获取某个前缀的所有key


## 分布式

1. 如何实现分布式锁,redis实现方式中如果超时了还没执行完怎么办
2. Dubbo实现原理，服务发现机制
3. 分布式ID生成
4. 分布式事务实现方式
6. zookeeper选举算法
5. zookeeper对节点的watch监听通知是永久的吗


## 问题定位

1. 如何定位系统响应慢的问题
2. 有个节点一直在Full GC，怎么快速定位并恢复环境
3. 本地Tomcat部署时发现启动非常慢,怎么定位

## 协议栈

1. http协议格式，get和post的区别
2. https握手机制
3. TCP backlog参数的作用

## 工具

1. git的分支怎么管理的

A B C
A［］array ＝ new B［2］;
A［0］＝ new C()
用泛型优化
