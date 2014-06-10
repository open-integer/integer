#!/bin/sh

BASEDIR=$(dirname $0)
serverType=${1-""}
password=${2-""}
options=${3-""}

if [ ! -z "${serverType}" ]; then
   exit 0
fi


if [ -z "${password}" ]; then
   mysqlCmd='mysql -u root Config'
else
   mysqlCmd="mysql ${options} -u root Config -p${password}"
fi

$mysqlCmd < $BASEDIR/../preload/DataFilesToLoad.sql

$BASEDIR/load_default_data.sh Core

