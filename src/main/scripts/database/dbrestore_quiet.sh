#!/bin/sh

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
    exit 1
fi

if [ ! -x $MYSQL_CMD ]; then    
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
			exit 1
        fi
    fi
    CATORZIP_CMD="$UNZIP_CMD -c"
else
    CATORZIP_CMD=$CAT_CMD
fi

# here's were we modify the SQL database
$CATORZIP_CMD $FILE_NAME | $MYSQL_CMD -u $MYSQL_USER

if [ $? -eq 0 ]; then
    $ECHO_CMD $RESTORE_DB_SUCCESS
    $ECHO_CMD "   $FILE_NAME"
else
    $ECHO_CMD $RESTORE_DB_FAILURE
fi
