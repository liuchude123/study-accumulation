1.创建shell
touch /opt/soft/bin/auto-del-30-days-ago-log.sh
chmod +x auto-del-30-days-ago-log.sh
新建一个可执行文件auto-del-30-days-ago-log.sh，并分配可运行权限

2.编辑shell脚本
vi auto-del-30-days-ago-log.sh
编辑auto-del-30-days-ago-log.sh文件如下：
#!/bin/sh
find /opt/soft/log/ -mtime +30 -name "*.log" -exec rm -rf {} \;
ok，保存退出(:wq)。

3.计划任务
#crontab -e
将auto-del-30-days-ago-log.sh执行脚本加入到系统计划任务，到点自动执行
输入：
10 0 * * * /opt/soft/log/auto-del-7-days-ago-log.sh >/dev/null 2>&1
这里的设置是每天凌晨0点10分执行auto-del-7-days-ago-log.sh文件进行数据清理任务了
