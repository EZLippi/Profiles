## 性能测试Gatling入门教程
###　gatling安装
从Gatling[官网](https://gatling.io/)下载后解压即可,执行bin目录下的gatling.bat或者gatling.sh即可运行,要先安装好jdk配置好环境变量

### 快速教程

推荐使用Intelj idea + maven archetype插件来搭建开发环境,Gatling采用了Scala DSL语言,首先要配置好Scala环境，在Idea的插件库搜索Scala并安装，然后从
Scala[官网](https://www.scala-lang.org/download/)下载scala SDK,打开Idea的project structure，找到Global Library那一栏，点加号添加下载的Scala SDK.

执行Maven命令创建项目骨架:

 mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=io.gatling.highcharts -DarchetypeArtifactId=gatling-highcharts-maven-archetype -DgroupId=com.ezlippi  -DartifactId=gatling.test  -Dversion=1.0
 生成如下项目结构：在src/test/scala目录下新建一个继承Simulation类的测试类,在里面写测试方法:
 
 ``` scala
 class OpenApiSimulation extends Simulation {

  val httpConf = http
    .baseURL("https://someUrl")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

 //forever表示一直执行,具体写法可以参考：https://gatling.io/docs/2.3/general/simulation_setup/
  val scn = scenario("com.ezlippi.OpenApiSimulation")
      .forever(exec(http("request_1")
        .get("")))

//只执行一次
//  val scn = scenario("com.ezlippi.OpenApiSimulation")
//      .exec(http("request_1").get("/646df17616081148d3e679"))

  //立刻注入100个用户
  val injectStrategy = atOnceUsers(100)


  
  setUp(
    scn.inject(injectStrategy)
  ).protocols(httpConf).maxDuration(Duration.apply(10, TimeUnit.MINUTES))
 ```
 ## 执行测试脚本
 
 如果通过maven命令执行测试脚本，需要在pom文件中配置gatling-maven-plugin，simulationClass表示要执行的测试类,resultsFolder是测试报告存放的路径，
 disableCompiler执行的时候是否禁止编译,因为我写完测试类后就右键编译把scala文件编译好了，执行maven命令时没必要再次编译,插件配置如下:
 ``` XML
 <plugin>
				<groupId>io.gatling</groupId>
				<artifactId>gatling-maven-plugin</artifactId>
				<version>2.2.4</version>
				<configuration>
					<!-- 测试脚本 -->
					<simulationClass>com.ezlippi.OpenApiSimulation</simulationClass>
					<!-- 结果输出地址 -->
					<resultsFolder>gatling</resultsFolder>
					<disableCompiler>true</disableCompiler>
				</configuration>
			</plugin>
 ```
 然后执行mvn gatling:execute就可以执行用例了,输出结果如下:
 ``` Bash
 ================================================================================
2018-01-22 21:34:27                                           0s elapsed
---- com.ezlippi.OpenApiSimulation ---------------------------------------------
[                                                                          ]  0%
          waiting: 100    / active: 0      / done:0     
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=0      KO=0     )

================================================================================


================================================================================
2018-01-22 21:34:32                                           4s elapsed
---- com.ezlippi.OpenApiSimulation ---------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 100    / done:0     
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=4789   KO=0     )
> request_1                                                (OK=4789   KO=0     )
================================================================================
 ```
 ## 查看测试报告
 
 打开gatling目录已时间戳结尾的文件夹下的index.html文件就可以查看报告了，结果非常明显。
 
 ## 直接通过maven配置
 
 如果通过archetype插件创建项目骨架失败了，那可以直接新建maven工程，然后添加如下内容到pom文件中:
 ``` XML
 <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.ezlippi</groupId>
	<artifactId>gatling</artifactId>
	<version>1.0-SNAPSHOT</version>

	<properties>
		<maven.compiler.source>1.7</maven.compiler.source>
		<maven.compiler.target>1.7</maven.compiler.target>
		<scala.version>2.11.7</scala.version>
		<encoding>UTF-8</encoding>
		<gatling.version>2.1.7</gatling.version>
		<scala-maven-plugin.version>3.2.2</scala-maven-plugin.version>
	</properties>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>io.gatling</groupId>
				<artifactId>gatling-app</artifactId>
				<version>${gatling.version}</version>
			</dependency>
			<dependency>
				<groupId>io.gatling</groupId>
				<artifactId>gatling-recorder</artifactId>
				<version>${gatling.version}</version>
			</dependency>
			<dependency>
				<groupId>io.gatling.highcharts</groupId>
				<artifactId>gatling-charts-highcharts</artifactId>
				<version>${gatling.version}</version>
			</dependency>
			<dependency>
				<groupId>org.scala-lang</groupId>
				<artifactId>scala-library</artifactId>
				<version>${scala.version}</version>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<dependencies>
		<dependency>
			<groupId>io.gatling.highcharts</groupId>
			<artifactId>gatling-charts-highcharts</artifactId>
		</dependency>
		<dependency>
			<groupId>io.gatling</groupId>
			<artifactId>gatling-app</artifactId>
		</dependency>
		<dependency>
			<groupId>io.gatling</groupId>
			<artifactId>gatling-recorder</artifactId>
		</dependency>
		<dependency>
			<groupId>org.scala-lang</groupId>
			<artifactId>scala-library</artifactId>
		</dependency>
		<dependency>
			<groupId>commons-codec</groupId>
			<artifactId>commons-codec</artifactId>
			<version>1.10</version>
		</dependency>
	</dependencies>

	<build>
		<testSourceDirectory>src/test/scala</testSourceDirectory>
		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>net.alchim31.maven</groupId>
					<artifactId>scala-maven-plugin</artifactId>
					<version>${scala-maven-plugin.version}</version>
				</plugin>
			</plugins>
		</pluginManagement>
		<plugins>
			<plugin>
				<groupId>net.alchim31.maven</groupId>
				<artifactId>scala-maven-plugin</artifactId>
				<executions>
					<execution>
						<goals>
							<goal>testCompile</goal>
						</goals>
						<configuration>
							<args>
								<arg>-target:jvm-1.7</arg>
								<arg>-deprecation</arg>
								<arg>-feature</arg>
								<arg>-unchecked</arg>
								<arg>-language:implicitConversions</arg>
								<arg>-language:postfixOps</arg>
							</args>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>io.gatling</groupId>
				<artifactId>gatling-maven-plugin</artifactId>
				<version>2.2.4</version>
				<configuration>
					<!-- 测试脚本 -->
					<simulationClass>com.ezlippi.OpenApiSimulation</simulationClass>
					<!-- 结果输出地址 -->
					<resultsFolder>gatling</resultsFolder>
					<disableCompiler>true</disableCompiler>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-dependency-plugin</artifactId>
				<version>2.10</version>
				<executions>
					<execution>
						<id>copy-dependencies</id>
						<phase>package</phase>
						<goals>
							<goal>copy-dependencies</goal>
						</goals>
						<configuration>
							<outputDirectory>${project.basedir}/dependencies</outputDirectory>
							<overWriteReleases>false</overWriteReleases>
							<overWriteSnapshots>false</overWriteSnapshots>
							<overWriteIfNewer>true</overWriteIfNewer>
							<includeScope>compile</includeScope>
							<includeScope>runtime</includeScope>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
 ```
 在src/test/resources目录下添加logback的配置文件logback.xml,内容如下:
 ``` XML
 <?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%-5level] %logger{15} - %msg%n%rEx</pattern>
			<immediateFlush>false</immediateFlush>
		</encoder>
	</appender>

	<root level="WARN">
		<appender-ref ref="CONSOLE" />
	</root>

</configuration>

 ```
 
 然后执行mvn gatling:execute就大功告成了。
