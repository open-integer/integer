#!/bin/sh

BASEDIR=$(dirname $0)
serverType=${1-""}
password=${2-""}
options=${3-""}

if [ ! -z "${serverType}" ]; then
   exit 0
fi


if [ -z "${password}" ]; then
   mysqlConfigCmd='mysql -u root Config'
   mysqlCmd='mysql -u root'
else
   mysqlConfigCmd="mysql ${options} -u root Config -p${password}"
   mysqlCmd="mysql ${options} -u root -p${password}"
fi

echo "drop database Config"
echo "drop database if exists Config" | $mysqlCmd

echo "create databse Config"
echo "create database Config" |  $mysqlCmd

$mysqlConfigCmd < $BASEDIR/../preload/DataFilesToLoad.sql

$BASEDIR/load_default_data.sh Core ${password}

