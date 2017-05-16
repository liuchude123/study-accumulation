#!/bin/sh
log_dir=/var/log/tomcat
monitor_file=$1 #tomcat目录下的catalina.out文件的绝对路径
file_size=`du  -m $monitor_file | awk '{print $1}'`
if [ $file_size -ge  100 ]
then
        if [ ! -d $log_dir ]
        then
                mkdir /var/log/tomcat   #创建保存切割文件目录,这个路径可以自行修改，保存到你想要的目录
        fi
        cp $1 /var/log/tomcat/log-`date +%Y-%m-%d-%H%M%S`.log   #保存日志文件
        echo `date +%Y-%m-%d-%H:%M:%S`":文件切割"  >>/var/log/tomcat/split.log  #记录切割日志
        echo "" > $1    #清空tomcat的log/catalina.out文件内容
fi

#crontab -e   #进入计划任务编辑模式
#*/1 * * * *  sh /monitor_script.sh  /opt/apache-tomcat-7.0.67/logs/catalina.out #第一个路径是脚本的路径，第二个参数是tomcat下catalina.out文件的绝对路径
#若没有crontab命令
#yum install vixie-cron
#yum install crontabs
