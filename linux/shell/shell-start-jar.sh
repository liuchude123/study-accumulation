#!/bin/bash

function getJarName()
{
	case $1 in 
	"1") 
		echo "datacollect-api.jar"	
		;; 
  	"2") 
		echo "datacollect-mainjob.jar"
		;; 
  	"3") 
		echo "datacollect-workjob.jar"
		;; 
	 *) 
		aaa=$(contains $1 "datacollect")
		if [ $aaa == 1 ]
        		then
		        echo "$1"
		else
			echo "invalid"
		fi
  		;; 
	esac 
}

function contains()
{
        contain=$(echo $1 | grep "${2}")
        if [ "$contain" != "" ]
                then
                echo 1
        else
                echo 0
        fi
}

function toUpper()
{
	str=`echo $1 | tr 'a-z' 'A-Z'`
	echo $str
}

function toLower()
{
	str=`echo $1 | tr 'A-Z' 'a-z'`
	echo $str
}

function getPid()
{	
	pid=`ps -ef|grep -E "$1"|grep -v grep|awk '{print $2}'` 
	echo $pid
}

function getFilePath()
{
	path=`find / -name "$1" | head -n 1`
	echo $path
}

function runJar()
{
	nohup java -XX:ErrorFile=./runlog-$1%p.log -XX:+HeapDumpOnOutOfMemoryError -jar -Xms128m -Xmx1024m $2 > /dev/null 2>&1 &			
}

function run()
{
	echo -e  "\033[40;31;1mNote!!!\033[0m \033[40;34;1m Select 1 for datacollect-api.jar, Select 2 for datacollect-mainjob.jar, Select 3 for datacollect-workjob.jar!\033[0m"

	echo -e  "\033[40;35;1mStep 1: Please select the follows or enter your jar name!\033[0m"
	read -p  "select the follows or enter your jar name: "  jar

	# get jar
	jarname=$(getJarName $jar)

	# get pid
	if [ "$jarname" == "" ]
		then
		echo -e "\033[40;31;1mempty input!\033[0m"
	elif [ "$jarname" == "invalid" ]
		then 
		echo -e "\033[40;31;1minvalid input!\033[0m"
	else
		echo -e "\033[40;35;1mStep 2: Determine whether the $jarname process is running?\033[0m"
		
		# get pid
		pid=$(getPid $jarname)
		if [ "$pid" != "" ]
			then
			echo -e "\033[40;34;1mthe $jarname process is runing\033[0m"
			read -p "now kill it, yes or no? " yesorno
			# tollower case
			yn=$(toLower $yesorno)
			if [ "$yn" == "yes" ] || [ "$yn" == "y" ]
				then
				kill -9 $pid
				echo -e "\033[40;32;1mkill -9 $pid success ...\033[0m"
			else
				echo -e "\033[40;34;1mexit!\033[0m"
				exit
			fi
		else
			echo -e "\033[40;32;1mthe $jarname process does not run!\033[0m"
		fi

		echo -e "\033[40;35;1mStep 3: Start running your jar?\033[0m"
		read -p "yes or no? " yesorno
		# tollower case
		start=$(toLower $yesorno)
		if [ "$start" == "yes" ] || [ "$start" == "y" ]
			then	
			echo -e "\033[40;34;1mstart running $jarname ...\033[0m "
			
			# get file path
			path=$(getFilePath $jarname)
			# run jar
			runJar $jarname $path

			# get pid
			pid=$(getPid $jarname)
			if [ "$pid" != "" ]
				then
				echo -e "\033[40;32;1mrunning $jarname success $pid ...\033[0m"
				
				# view memory?
				echo -e "\033[40;35;1mStep 4: View memory usage information?\033[0m"
				read -p "yes or no? " yesorno
				# tollower case
				yn=$(toLower $yesorno)
				if [ "$yn" == "yes" ] || [ "$yn" == "y" ]
					then
						top -p $pid
				else
					echo -e "\033[40;34;1mexit!\033[0m"
					exit
				fi
			else
				echo -e "\033[40;31;1mrunning $pname failure ...\033[0m"
			fi
		else
			echo -e "\033[40;34;1mexit!\033[0m"
			exit
		fi
	fi
}

run

exit




#lcd.sh
#lcd.sh调用helper.sh的run方法
##!/bin/bash
#source /helper.sh
#run
