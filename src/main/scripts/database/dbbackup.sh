#!/bin/sh
##
# Copyright (C) Ambient Corporation, 2009-2013,  All Rights Reserved 
#
# NMS database backup script
##

DIRNAME=`dirname $0`
mysql_opts="--column-names=false -u root -Bse"

MYSQL_USER=backup
MYSQL_PASS=backupOnly

DATABASES="Config"

MYSQL_PARMS="--add-drop-database --add-drop-table --add-locks --single-transaction --quick --disable-keys --extended-insert --create-options --compact --routines"

BACKUP_DIR=$HOME/db_backup

DATE_CMD=/bin/date
MKDIR_CMD=/bin/mkdir
ECHO_CMD=/bin/echo
GZIP_CMD=/bin/gzip
MYSQLDUMP_CMD=/usr/bin/mysqldump
RM_CMD=/bin/rm

USAGE=$BACKUP_DB_USAGE

if [ ! -x $ECHO_CMD ]; then
    ECHO_CMD="echo"
fi

if [ ! -x $RM_CMD ]; then
    RM_CMD="rm"
fi
if [ ! -x $MYSQLDUMP_CMD ]; then
    $ECHO_CMD ${MYSQL_DUMP_CMD_NOT_FOUND//0/$MYSQLDUMP_CMD}
    $ECHO_CMD $ABORT
    exit 1
fi

while getopts t:d:ahu:p:f: option
do
  case $option in
    d) BACKUP_DIR=$OPTARG;;
    u) MYSQL_USER=$OPTARG;;
    p) MYSQL_PASS=$OPTARG;;
    f) FILE_NAME=$OPTARG;;
    t) SERVER_TYPE=$OPTARG;;
    h) echo $USAGE
         exit;;
    ?) echo $USAGE
       exit;;
  esac
done

shift $(($OPTIND - 1))

if [ -z "$SERVER_TYPE" ]; then
	$ECHO_CMD $BACKUP_SVR_CMD_REQUIRED
	$ECHO_CMD $USAGE	
	$ECHO_CMD $ABORT
	exit 1
fi

case $SERVER_TYPE in
	BE|be) DATABASES="--databases ${BE_DATABASES}";;		
	FE|fe) DATABASES="--databases ${FE_DATABASES}";;		
	BEFE|befe) DATABASES="--databases ${BEFE_DATABASES}";;		
	*) $ECHO_CMD ${BACKUP_SVR_TYPE_INVALID//0/$SERVER_TYPE}
		$ECHO_CMD $USAGE
		$ECHO_CMD $ABORT
		exit;;
esac

$ECHO_CMD ${BACKUP_START_BACKUP_MSG//0/$SERVER_TYPE}

if [ -z "$BACKUP_DIR" ]; then
    BACKUP_DIR=$HOME/db_backup
fi

if [ ! -d $BACKUP_DIR ]; then
  if [ ! -x $MKDIR_CMD ]; then
      $MKDIR_CMD=mkdir
  fi
  $MKDIR_CMD -p $BACKUP_DIR
  if [ $? -ne 0 ]; then
    ERR_P1=${BACKUP_DIR_NOT_FOUND//0/$BACKUP_DIR}
    ER_MSG=${ERR_P1//1/$MKDIR_CMD}
    $ECHO_CMD $ABORT
    exit 1
  fi
fi

if [ -z "$FILE_NAME" ]; then
   if [ -x $DATE_CMD ]; then
      FILE_NAME=nmsdb_`$DATE_CMD +"%Y%m%d%H%M%S"`.sql
    else
      FILE_NAME=nmsdb_backup.sql
    fi
fi

DB_BACKUP_FILE_NAME=$BACKUP_DIR/$FILE_NAME

$ECHO_CMD $BACKUP_DB_TO_FILE
$ECHO_CMD "   $FILE_NAME"

if [ -x $GZIP_CMD ]; then
    DB_BACKUP_FILE_NAME="$DB_BACKUP_FILE_NAME.gz"
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 0;" | $GZIP_CMD > $DB_BACKUP_FILE_NAME
    $MYSQLDUMP_CMD --user=$MYSQL_USER --password=$MYSQL_PASS $MYSQL_PARMS $DATABASES | $GZIP_CMD -9 >> $DB_BACKUP_FILE_NAME
    if [ $? -ne 0 ]; then
        $ECHO_CMD $BACKUP_DB_FAILURE
        $RM -f $DB_BACKUP_FILE_NAME
        exit 1;
    fi
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 1;" | $GZIP_CMD >> $DB_BACKUP_FILE_NAME
else
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 0;" > $DB_BACKUP_FILE_NAME
    $MYSQLDUMP_CMD --user=$MYSQL_USER --password=$MYSQL_PASS $MYSQL_PARMS $DATABASES >> $DB_BACKUP_FILE_NAME
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 1;" >> $DB_BACKUP_FILE_NAME
    $ECHO_CMD $BACKUP_DB_FILE_NOT_COMPRESSED
fi

$ECHO_CMD $BACKUP_DB_SUCCESS
$ECHO_CMD "   $FILE_NAME"

exit 0
