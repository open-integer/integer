#!/bin/sh

BASEDIR=$(dirname $0)
serverType=${1-""}
password=${2-""}
options=${3-""}
myType=Core

if [ -z "${serverType}" ]; then
   echo "ERROR: serverType Must be provided. Core or FE"
   exit 1
fi
if [ "${serverType}" != "${myType}" ]; then
   echo "INFO: Skipping $0 as this is server type ${serverType} NOT ${myType}"
   exit 0
fi
if [ -z "${password}" ]; then
   mysqlCmd='mysql -u root Config'
else
   mysqlCmd="mysql ${options} -u root Config -p${password}"
fi



$mysqlCmd < $BASEDIR/../preload/insertCoreManager.sql
$mysqlCmd < $BASEDIR/../preload/insertIntegerServer.sql
$mysqlCmd < $BASEDIR/../preload/DataFilesToLoad.sql
