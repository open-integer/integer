#!/bin/sh

BASEDIR=$(dirname $0)


if [ =z "${1}" ]; then
   mysqlCmd='mysql -u root Config'
else
   mysqlCmd="mysql -u root Config -p$1"
fi



$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersaa.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersab.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersac.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersad.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersae.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersaf.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersag.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersah.sql
$mysqlCmd < $BASEDIR/../preload/enterpriseNumbersai.sql
