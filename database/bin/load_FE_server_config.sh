#!/bin/sh

BASEDIR=$(dirname $0)
options=${2-""}

if [ -z "${1}" ]; then
   mysqlCmd='mysql -u root Config'
else
   mysqlCmd="mysql ${options} -u root Config -p$1"
fi



$mysqlCmd < $BASEDIR/../preload/insertFEManager.sql
$mysqlCmd < $BASEDIR/../preload/insertIntegerServer.sql
