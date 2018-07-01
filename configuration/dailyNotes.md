1. Dump JVM内存数据: gdb -pid=pid ;gcore /opt/huawei/jvm.core;detach;quit;jmap -dump:format=b,file=/tmp/jvm.hprof /tmp/jvm.core
2. git
    * 从远程分支checkout:git checkout -b c51-branch origin/c51_bracnch.
    * 从指定分支checkout指定文件:git checkout master -- myplugin.js
    * 合并其他分支的提交到当前分支:git cherry-pick commitId
    * 手动建立本地分支和远程分支的追踪关系:git branch --set-upstream c51 origin/c51_bracnch,pull命令可以省略远程分支名字
    * git submodule foreach --recursive git checkout -b LBtags/LB
    
3.     
    * 生成自签名证书:keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass Changeme_123 -validity 3660 -keysize 2048
            keytool -genkey -keyalg RSA -alias selfsigned -keystoretype pkcs12 -keystore keystore.p12 -storepass Changeme_123 -validity 360 -keysize 2048
    * 导出keystore里的证书:keytool -export -keystore keystore.p12 -storetype pkcs12 -alias tomcat -file car.crt
    * 导入证书为自签名格式:keytool -import -file E:\svnrepo\I2000\outserver.crt -alias tomcat -keystore truststore
    * 查看证书信息: keytool -v -list -keystore iwsKeyStore
    * curl -v https://10.75.157.216:29952 --tlsv1.2 --insecure
    * 导出pfx证书到pem格式: openssl pkcs12 -in mypfxfile.pfx -out frompfx.pem -nodes
                           openssl pkcs12 -export -out eneCert.pkcs12 -in eneCert.pem
                           
4. CGLib将字节码保存到文件: -Dcglib.debugLocation=E:\somewhere
5. 搜索Jar包里的关键字: find . -name "com.huawei.oms*.jar"|xargs -I{} sh -c 'echo searhing in "{}"; zipgrep "setListeningAddress" {}'
6. ES
* 查询全部: curl 'localhost:9200/testdb/_search?pretty=true' -d '{"query":{"match_all":{}}}'
* 根据Id查询: curl 'localhost:9200/testdb/_search?pretty=true' -d '{"query":{"match":{"id":e16"}}}'
* 条件查询: curl 'http://localhost:9200/testdb_search?pretty=true' -d '{"query":{"match":{"type":"vm"}}}'
7. SS
202.5.20.48:2448
103.29.70.110:2448
