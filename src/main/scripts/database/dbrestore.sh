#!/bin/sh
##
# Copyright (C) Ambient Corporation, 2009-2013,  All Rights Reserved 
#
# NMS database restore script
##

DIRNAME=`dirname $0`
mysql_opts="--column-names=false -u root -Bse"

LOCAL_IP=`ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'`
LOCALE=$(mysql $mysql_opts "SELECT systemLocale FROM AmbtAppCfg.NmsServer WHERE address = '$LOCAL_IP';" 2>/dev/null)
# if locale is null, default to english
if test -z "$LOCALE"; then
	$LOCALE=en_US
fi

MSG_FILE=$DIRNAME/locale/$LOCALE/messages
if [ -f "$MSG_FILE" ]; then
	source $MSG_FILE
else
	echo "ERROR: $MSG_FILE Not Found!"
	exit 1
fi

MYSQL_USER=root

BACKUP_DIR=$HOME/db_backup

GUNZIP_CMD=/bin/gunzip
GZIP_CMD=/bin/gzip
MYSQL_CMD=/usr/bin/mysql
ECHO_CMD=/bin/echo
CAT_CMD=/bin/cat
FILE_CMD=/usr/bin/file
AWK_CMD=/bin/awk

if [ ! -z $1 ]; then
    PARM=$1;
    FILE_NAME=$PARM
    if [ ! -r $FILE_NAME ]; then
	FILE_NAME=$BACKUP_DIR/$PARM
	if [ ! -r $FILE_NAME ]; then
	    FILE_NAME=$BACKUP_DIR/$PARM.gz
	    if [ ! -r $FILE_NAME ]; then
		FILE_NAME=$BACKUP_DIR/$PARM.sql
		if [ ! -r $FILE_NAME ]; then
		    FILE_NAME=$BACKUP_DIR/nmsdb_$PARM.sql.gz
		    if [ ! -r $FILE_NAME ]; then
			FILE_NAME=$BACKUP_DIR/nmsdb_$PARM.sql
		    fi
		fi
	    fi
	fi
    fi
else
    FILE_NAME=$BACKUP_DIR/nmsdb_backup.sql.gz
    if [ ! -r $FILE_NAME ]; then
	FILE_NAME=$BACKUP_DIR/nmsdb_backup.sql
    fi
fi

if [ ! -x $ECHO_CMD ]; then
    ECHO_CMD="echo"
fi

if [ ! -x $CAT_CMD ]; then
    CAT_CMD="cat"
fi

if [ ! -x $FILE_CMD ]; then
    FILE_CMD="file"
fi

if [ ! -x $AWK_CMD ]; then
    AWK_CMD="awk"
fi

if [ ! -r $FILE_NAME ]; then
    $ECHO_CMD ${RESTORE_FILE_NOT_FOUND//0/$PARM}
    $ECHO_CMD $ABORT
    exit 1
fi

if [ ! -x $MYSQL_CMD ]; then
    $ECHO_CMD ${MYSQL_CMD_NOT_FOUND//0/$MYSQL_CMD}
    $ECHO_CMD $ABORT
    exit 1
fi

# determine command to prep file based on type (may or may not be gzipped)
FILE_TYPE=`$FILE_CMD $FILE_NAME | $AWK_CMD '{ print $2 }'`
if [ "$FILE_TYPE" == "gzip" ]; then
    if [ -x $GUNZIP_CMD ]; then
        UNZIP_CMD="$GUNZIP_CMD"
    else
        if [ -x $GZIP_CMD ]; then
            UNZIP_CMD="$GZIP_CMD -d"
        else
            ERR_P1=${GZIP_NOT_FOUND/0/$GUNZIP_CMD}
            ERR_MSG=${ERR_P1/1/$GZIP_CMD}
	    $ECHO_CMD $ERR_MSG
	    $ECHO_CMD $ABORT
	    exit 1
        fi
    fi
    CATORZIP_CMD="$UNZIP_CMD -c"
else
    CATORZIP_CMD=$CAT_CMD
fi

$ECHO_CMD $RESTORE_DB_FROM_FILE
$ECHO_CMD "   $FILE_NAME"

# here's were we modify the SQL database
$CATORZIP_CMD $FILE_NAME | $MYSQL_CMD -u $MYSQL_USER

if [ $? -eq 0 ]; then
    $ECHO_CMD $RESTORE_DB_SUCCESS
    $ECHO_CMD "   $FILE_NAME"
else
    $ECHO_CMD $RESTORE_DB_FAILURE
fi
