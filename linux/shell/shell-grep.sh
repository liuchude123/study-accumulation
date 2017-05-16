#!/bin/bash

read -p  "please enter your key word: " kw
echo -e  "\033[40;35;1mps -ef|grep $kw\033[0m"

ps -ef|grep $kw
