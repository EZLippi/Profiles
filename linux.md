#linux高级命令

##VIM高级命令
翻页:<C-B> <C-F>
0y$	复制这一行
<C-V>块操作,<>左右缩进，J把所有行连成一行

##窗口管理
显示桌面	ctrl+alt+D
显示右键菜单　shift+F10
在应用程序间切换	alt+ESC
close window : alt + F4
min   window : alt + F9
move  window : alt + F7
resize window : alt ＋F8
启动应用	alt + M
log out  	ctrl+alt+delete
Shift+Ctrl+N – 新建文件夹
Alt + Enter – 查看选择文件/文件夹的属性

##实用应用
下拉式终端 yakuake
自动调节屏幕色彩	redshift
应用启动器	gnome-do
多窗口终端tmux
<C-B> + C 创建新窗口
<C-B> + & KILL当前窗口
<C-B> + , Rename当前窗口
<C-B> + ? 显示所有按键绑定
<C-B> + SPACE/数字 切换窗口
<C-B> + !　关闭当前面板
配置文件tmux.conf
ctrl + b + [进入选择模式，v/空格进入vi选择模式，y/Enter 复制，<C-B> + ]粘贴
插件tmuxinator,会话管理,mux new a新建项目，mux start a启动项目a

**终极shell	zsh**
插件：
1. git 处于一个git目录时，shell明确显示git和branch
2. textmate :textmate 打开指定文件
3. osx:tab增强，quick-look 预览文件，man-preview grep生成grep手册pdf版本
4. autojump: 使用j+目录名跳转，j --stat 显示最近保存的路径 
##常用命令
ps -ef			已完整格式显示所有进程
netstat -rn     查看路由表
ln -s file1 lnk1 	创建一个指向文件或目录的软链接
ln file1 lnk1 		创建一个指向文件或目录的物理链接
tar -xvfz archive.tar.gz 解压一个gzip格式的压缩包
tar -cvfz archive.tar.gz dir1 创建一个gzip格式的压缩包
cat file1 		从第一个字节开始正向查看文件的内容
find / -mtime +2 	从 '/' 开始进入根文件系统搜索文件和目录
file	a.c		文件类型
grep -n - -color=auto ‘abc’　test.c 在test.c中搜索abc,显示行号
;			使用多个命令
LANG=C 7za x your_zip_file.zip	**解决压缩文件乱码 **
convmv -f GBK -t utf-8 --notest -r .
关闭触摸板	sudo modprobe-r psmouse
打开触摸板　	sudo modprobe psmouse

**test命令**
if [ $test -gt 2 ] then echo "$test";fi	数字比较-eq,-lt,-gt,-nq
= != \> \< 　		字符串比较

**处理用户输入**
$0是程序名，$1是第一个参数，以此类推
**算术表达式**
a=$(( 10 / 5 )) a=$[ 10 / 5 ]
**修改最大文件描述符**
用户级vim /etc/security/limits.conf
* hard nofile 102400
* soft nofile 102400
内核/etc/sysctl.conf添加fs.file-max=102400
使用sysctl -p 命令使其生效
##高级shell命令

ctrl + a/e    移动到命令行首/行尾
ctrl + u/y	剪切光标到行首/粘贴
alt + B/F 	光标左移/右移
sudo !! 	以管理员权限执行上一条命令
ctrl + z 	暂停前台应用，`fg`重新将程序映射到前台
htop 		显示进程列表
ranger	文件浏览器
at		定时执行>输入命令　ctrl+D返回终端
stat 		获取文件详细信息tr
mtr 		ping+traceroute
!!			上一条命令
sudo !!		以root权限执行上一条命令
!$		上一条命令的最后一个参数
!^		上一条命令第一个参数
!?his	搜索包含his的命令
!#		引用当前行
^is^e	替换上一条命令的is为e,相当于!:s/is/e


#SED命令
##参数
-i 插入或替换	-a 添加行	－d 删除行	-c 替换行
－n 不打印默认输出	p	打印行	－f指定sed脚本 -e 多条sed命令
##正则表达式

[^]匹配不在组内字符　\(..\)保存已匹配的字符可以用\1引用它

##例子
sed -n -e "1,5p" -e "s/my/your/gp" 替换１－５行中的my为your


##MySql命令
SOURCE + SQL路径	执行SQL语句
SHOW PROFILES	显示每条命令耗费时间统计（需要set profiling = 1）
SHOW PROFILE FOR QUERY 1;//显示查询1具体时间消耗

