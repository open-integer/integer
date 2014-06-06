#!/bin/sh

BASEDIR=$(dirname $0)
serverType=${1-""}
password=${2-""}
options=${3-""}

if [ -z "${serverType}" ]; then
   echo "ERROR: serverType Must be provided. Core or FE"
   exit 1
fi
echo "INFO: This script is server type neutral" 


if [ -z "${password}" ]; then
   mysqlCmd='mysql -u root Config'
else
   mysqlCmd="mysql ${options} -u root Config -p${password}"
fi


$mysqlCmd < $BASEDIR/../preload/vendor_identifier.sql

#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersaa.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersab.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersac.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersad.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersae.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersaf.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersag.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersah.sql
#$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersai.sql
