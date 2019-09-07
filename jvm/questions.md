## Java基础
1. HashMap 1.7,1.8,ConcurrentHashMap size()方法实现，返回值是否准确
2. ArrayList和LinkedList区别
3. 线程池参数，核心池的设置依据
4. Lock和Synchronized区别，AQS,公平锁和非公平锁
5. 线程有哪些状态，以及是怎么转换的
6. Object.finialize()方法原理
7. 列举3个以上的RuntimeException
8. ni耦合bio,JDK epoll的bug，netty是怎么解决的，怎么解决TCP粘报和拆包的问题
9. JVM内存布局，为什么要分代收集,object对象内存布局，一个空Object对象的占多大空间？
10. 多线程通信有哪些方式
11. 常用的GC算法，哪些对象可以作为GC ROOT
12. 线上jdk版本，用了哪些jvm参数，什么场景下会触发一次GC
13. 哪些场景会出现内存泄漏,HashMap，ThreadLocal,WeakHashMap,Finalze
14. Tomcat ClassLoader机制
15. 泛型extends和super

## Spring框架

1. Spring AOP是怎么实现的，jdk动态代理和CGLib实现性能差别
2. 谈谈你对IOC理解，Spring BeanFactory和FactoryBean
3. Spring事务A调用B的问题

## Mysql

1. mysql锁加在什么位置,myisam和innodb的区别
2. 结合InnoDB的实现阐述事务的隔离级别,分别怎么解决脏读取，幻读和不可重复读
3. 对Mysql最新版本的同步机制有没有了解，比如一个主节点多个slave节点怎么保证可用性和性能

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

## 问题定位

1. 如何定位系统响应慢的问题
2. 有个节点一直在Full GC，怎么快速定位并恢复环境

## 协议栈

1. http协议格式，get和post的区别
2. https握手机制
3. TCP backlog参数的作用

