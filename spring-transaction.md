## Spring事务源码分析

事务管理能保证一系列操作以原子的方式执行,Spring框架提供了非常完善的事务支持,Spring事务管理主要围绕着TransactionDefinition,
AbstractPlatformTransactionManager,TransactionSynchronizationManager,DataSourceTransactionManager这几个类.

<!-- more -->

### 事务属性

#### 事务的传播行为

事务的传播行为是指如果在开启当前事务之前,当前线程已经存在一个事务上下文,那么用户可以指定当前事务的执行行为,Spring里的
传播行为有以下几种:

* PROPAGATION_REQUIRED	如果当前没有事务,就新建一个事务,如果已经存在一个事务中,加入到这个事务中.
* PROPAGATION_SUPPORTS	如果当前存在一个事务,则加入这个事务,如果当前没有事务,就以非事务方式执行
* PROPAGATION_MANDATOR	使用当前的事务,如果当前没有事务,就抛出异常
* PROPAGATION_REQUIRES_NEW	新建事务,如果当前存在事务,把当前事务挂起
* PROPAGATION_NOT_SUPPORTED	以非事务方式执行操作,如果当前存在事务,就把当前事务挂起
* PROPAGATION_NEVER	以非事务方式执行,如果当前存在事务,则抛出异常
* PROPAGATION_NESTED	如果当前存在事务,则在嵌套事务内执行.如果当前没有事务,则执行与PROPAGATION_REQUIRED类似的操作

Spring框架里在事务初始化时对隔离级别的处理如下所示:
``` java
public final TransactionStatus getTransaction(TransactionDefinition definition){
            if (isExistingTransaction(transaction)) {
                // 如果当前上下文存在事务 -> 检查用户设置的隔离级别来确定当前事务的行为
                return handleExistingTransaction(definition, transaction, debugEnabled);
            }
            // PROPAGATION_MANDATORY级别不存在当前事务,抛异常.
    		if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
    			throw new IllegalTransactionStateException(
    					"No existing transaction found for transaction marked with propagation 'mandatory'");
    		}
    		//需要支持事务
    		else if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
    				definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
    				definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
    			//把当前事务挂起并保存当前事务的事务属性
    			SuspendedResourcesHolder suspendedResources = suspend(null);
    			try {
    				boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
    				DefaultTransactionStatus status = newTransactionStatus(
    						definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
    				//开启事务
    				doBegin(transaction, definition);
    				prepareSynchronization(status, definition);
    				return status;
    			}
    			catch (RuntimeException | Error ex) {
    				resume(null, suspendedResources);
    				throw ex;
    			}
    		}
    		else {
    			// 其他事务级别以非事务方式执行
    			if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
    				logger.warn("Custom isolation level specified but no actual transaction initiated; " +
    						"isolation level will effectively be ignored: " + definition);
    			}
    			boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
    			return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
    		}
}
```

suspend挂起一个事务时需要判断是否开启事务同步,用户可以给事务注册事务同步器,如果检查到开启事务同步则需要保存事务的名称、事务同步列表、只读属性、超时时间、事务隔离级别、是否开启同步这些属性,
如果当前事务不为空,则由TransactionManager将对应事务挂起,通常的挂起行为是将事务对象和资源进行解绑,suspend的代码如下:

``` java
protected final SuspendedResourcesHolder suspend(Object transaction) throws TransactionException {
        //当前事务开启了事务同步
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
		    //把当前事务的所有事务同步器挂起
			List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
			try {
				Object suspendedResources = null;
				if (transaction != null) {
					suspendedResources = doSuspend(transaction);
				}
				//保存事务属性
				String name = TransactionSynchronizationManager.getCurrentTransactionName();
				TransactionSynchronizationManager.setCurrentTransactionName(null);
				boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
				TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
				Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
				TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(null);
				boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
				TransactionSynchronizationManager.setActualTransactionActive(false);
				return new SuspendedResourcesHolder(
						suspendedResources, suspendedSynchronizations, name, readOnly, isolationLevel, wasActive);
			}
			catch (RuntimeException | Error ex) {
				// doSuspend failed - original transaction is still active...
				doResumeSynchronization(suspendedSynchronizations);
				throw ex;
			}
		}
		//没有开启同步但存在事务,则调用对应TransactionManager的doSuspend()方法挂起事务
		else if (transaction != null) {
			// Transaction active but no synchronization active.
			Object suspendedResources = doSuspend(transaction);
			return new SuspendedResourcesHolder(suspendedResources);
		}
		else {
			// 不存在事务也没有开启事务同步
			return null;
		}
	}
```

上面提到了事务同步的概念,Spring里有一个事务同步管理器(TransactionSynchronizationManager)管理着事务的生命周期,用户可以给事务注册事务同步器,监听指定的事务事件,
事务同步管理器在指定事务事件(比如事务提交成功、事务执行完成时)回调你事务同步器里的方法;用户只需要实现TransactionSynchronization接口并实现里面的方法.
TransactionSynchronizationManager的属性都设置为ThreadLocal类型的,因为事务是和线程进行绑定的,几个属性如下所示:

``` java
public abstract class TransactionSynchronizationManager {
    //线程绑定的资源,比如DataSourceTransactionManager绑定是的某个数据源的一个Connection,在整个事务执行过程中
    //都使用同一个Jdbc Connection
    private static final ThreadLocal<Map<Object, Object>> resources =
			new NamedThreadLocal<>("Transactional resources");
    //事务注册的事务同步器
	private static final ThreadLocal<Set<TransactionSynchronization>> synchronizations =
			new NamedThreadLocal<>("Transaction synchronizations");
    //事务名称
	private static final ThreadLocal<String> currentTransactionName =
			new NamedThreadLocal<>("Current transaction name");
    //事务只读属性
	private static final ThreadLocal<Boolean> currentTransactionReadOnly =
			new NamedThreadLocal<>("Current transaction read-only status");
    //事务隔离级别
	private static final ThreadLocal<Integer> currentTransactionIsolationLevel =
			new NamedThreadLocal<>("Current transaction isolation level");
    //事务同步开启
	private static final ThreadLocal<Boolean> actualTransactionActive =
			new NamedThreadLocal<>("Actual transaction active");
}
```

#### 事务的隔离级别

JDBC事务的隔离级别通常是由数据库的锁来实现的,MySql数据库通过行锁、Gap锁和next-key锁来解决事务的脏读取、幻读取和
不可重复读的问题.事务的隔离级别通常是指同一个事务内相同的一条查询语句前后执行多次的行为差别,下面介绍常见的几种事务
隔离级别并简单介绍怎么用锁去实现:

* TransactionDefinition.ISOLATION_DEFAULT：这是默认值,表示使用底层数据库的默认隔离级别.对大部分数据库而言,通常这值就是TransactionDefinition.ISOLATION_READ_COMMITTED.
* TransactionDefinition.ISOLATION_READ_UNCOMMITTED：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据.该级别没有用到锁,多个事务可以并发修改同一行数据,因此不能防止脏读和不可重复读.
* TransactionDefinition.ISOLATION_READ_COMMITTED：该隔离级别表示一个事务只能读取另一个事务已经提交的数据.该级别用到了行锁,查询的时候读取的是快照的数据,每次查询读取的是最新的快照,
,可以防止脏读,但是存在幻读取,因为行锁只能锁住修改,不能锁住插入和删除.
* TransactionDefinition.ISOLATION_REPEATABLE_READ：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询,并且每次返回的记录都相同,同个事务内多次查询读取到的是同一个快照(第一次查询时的快照),因此可以防止脏读和不可重复读,
MySql数据库InnoDb存储引擎使用使用的是Gap锁和next-key锁,执行查询时锁住的是一个范围,因此可以防止插入和删除.
* TransactionDefinition.ISOLATION_SERIALIZABLE：所有的事务依次顺序执行,该级别可以防止脏读、不可重复读以及幻读,通常情况下不会用到该级别.

#### 事务超时

所谓事务超时,就是指一个事务的最长执行时间,如果超过该时间限制但事务还没有完成,则自动回滚事务.在TransactionDefinition中以int的值来表示超时时间,其单位是秒.

#### 事务的只读属性

事务的只读属性是指,对事务性资源进行只读操作或者是读写操作.所谓事务性资源就是指那些被事务管理的资源,比如数据源、JMS 资源,以及自定义的事务性资源等等.如果确定只对事务性资源进行只读操作,那么我们可以将事务标志为只读的,以提高事务处理的性能.在 TransactionDefinition 中以 boolean 类型来表示该事务是否只读.

#### 事务的回滚规则

Spring的默认回滚规则是,如果在事务中抛出了非检查异常(继承自RuntimeException 的异常),则默认将回滚事务.如果没有抛出任何异常,或者抛出了已检查异常,则仍然提交事务.这通常也是大多数开发者希望的处理方式,也是 EJB 中的默认处理方式.但是,我们可以根据需要人为控制事务在抛出某些未检查异常时任然提交事务,或者在抛出某些已检查异常时回滚事务.


下面就以DataSourceTransactionManager为例介绍下开启一个事务时的准备工作,设置事务的隔离级别、关闭Jdbc Connection的自动提交、根据事务的只读属性来设置事务是否只读、
设置事务的超时时间、获取数据库连接并绑定资源到事务同步管理器,代码如下:

``` java
protected void doBegin(Object transaction, TransactionDefinition definition) {
		DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
		Connection con = null;

		try {
			if (!txObject.hasConnectionHolder() ||
					txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
				//从数据源中获取一个连接
				Connection newCon = obtainDataSource().getConnection();
				txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
			}

			txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
			con = txObject.getConnectionHolder().getConnection();

			Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
			txObject.setPreviousIsolationLevel(previousIsolationLevel);

			// 关闭Connection的自动提交,由Spring框架来控制事务的提交和回滚,
			 //修改自动提交操作代价非常大,先做判断是否开启了自动提交然后再修改
			if (con.getAutoCommit()) {
				txObject.setMustRestoreAutoCommit(true);
				con.setAutoCommit(false);
			}

			prepareTransactionalConnection(con, definition);
			txObject.getConnectionHolder().setTransactionActive(true);
            //根据事务定义的超时时间来设置事务的超时时间
			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
			}

			//将ConnectionHolder对象绑定到事务同步管理器中
			if (txObject.isNewConnectionHolder()) {
				TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());
			}
		}

		catch (Throwable ex) {
			if (txObject.isNewConnectionHolder()) {
				DataSourceUtils.releaseConnection(con, this.dataSource);
				txObject.setConnectionHolder(null, false);
			}
			throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
		}
	}
```

通过以上步骤就完成了事务的准备过程,最关键的是通过事务同步管理器绑定了一个Jdbc Connection,如果你要使用Spring事务来管理数据库操作,那就必须保证你用来执行数据库操作的
Connection和Spring事务里的Connection是同一个,Spring提供了DataSourceUtils.getConnection(dataSource)来获取事务连接,你也可以用JdbcTemplate来执行SQL,JdbcTemplate
内部就是调用的DataSourceUtils来获取Connection.

