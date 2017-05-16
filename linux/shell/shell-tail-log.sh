#!/bin/bash

function getProjectName()
{
	case $1 in
	"1")
		echo "datacollect-api"
		;;
	"2")
		echo "datacollect-mainjob"
		;;
	"3")
		echo "datacollect-workjob"
		;;
	*)
		echo $1
		;;
	esac
}

function getLogLevel()
{
	case $1 in
	"1")
		echo "info"
		;;
	"2")
		echo "error"
		;;
	"3")
		echo "warn"
		;;
	*)
		echo $1
		;;
	esac
}

function getFilePath()
{
	path=`find /*/$1/ -name "log-$2.log" | head -n 1`
	echo $path
}

function tailLog()
{
	if [  "$1" != "" ]
		then
		echo -e "\033[40;34;1mtail -500f $1\033[0m"
		tail -500f $1
	else
		echo -e "\033[40;34;1minvalid!\033[0m"
	fi
}

function run()
{
	echo -e  "\033[40;31;1mNote!!!\033[0m \033[40;34;1m Select 1 for datacollect-api, Select 2 for datacollect-mainjob, Select 3 for datacollect-workjob!\033[0m"
	echo -e  "\033[40;35;1mStep 1: Please select the follows or enter your project name!\033[0m"
	read -p  "select the follows or enter your project name: "  project
	projectName=$(getProjectName $project)
	
	echo -e  "\033[40;31;1mNote!!!\033[0m \033[40;34;1m Select 1 for info, Select 2 for error, Select 3 for warn!\033[0m"
	echo -e  "\033[40;35;1mStep 1: Please select the follows or enter your log level!\033[0m"
	read -p  "select the follows or enter your level name: "  level
	logLevel=$(getLogLevel $level)
	
	path=$(getFilePath $projectName $logLevel)
	
	tailLog $path
}

run
exit
