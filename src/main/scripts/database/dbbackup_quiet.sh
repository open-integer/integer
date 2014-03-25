#!/bin/sh
# 
# dbbackup42.sh
# 
MYSQL_USER=backup
MYSQL_PASS=backupOnly

BE_DATABASES="AmbientNms ReportData"
FE_DATABASES="AmbientNms AmbientNmsFE quartz ReportData"

MYSQL_PARMS="--add-drop-database --add-drop-table --add-locks --single-transaction --quick --disable-keys --extended-insert --create-options --compact"

BACKUP_DIR=$HOME/db_backup

DATE_CMD=/bin/date
MKDIR_CMD=/bin/mkdir
ECHO_CMD=/bin/echo
GZIP_CMD=/bin/gzip
MYSQLDUMP_CMD=/usr/bin/mysqldump
RM_CMD=/bin/rm

if [ ! -x $ECHO_CMD ]; then
    ECHO_CMD="echo"
fi

if [ ! -x $RM_CMD ]; then
    RM_CMD="rm"
fi
if [ ! -x $MYSQLDUMP_CMD ]; then    
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
  esac
done

shift $(($OPTIND - 1))

if [ -z "$SERVER_TYPE" ]; then
	exit 1
fi

case $SERVER_TYPE in
	BE|be) DATABASES="--databases ${BE_DATABASES}";;		
	FE|fe) DATABASES="--databases ${FE_DATABASES}";;		
	BEFE|befe) DATABASES="--databases ${FE_DATABASES}";;		
esac

if [ -z "$BACKUP_DIR" ]; then
    BACKUP_DIR=$HOME/db_backup
fi

if [ ! -d $BACKUP_DIR ]; then
  if [ ! -x $MKDIR_CMD ]; then
      $MKDIR_CMD=mkdir
  fi
  $MKDIR_CMD -p $BACKUP_DIR
  if [ $? -ne 0 ]; then
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

if [ -x $GZIP_CMD ]; then
    DB_BACKUP_FILE_NAME="$DB_BACKUP_FILE_NAME.gz"
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 0;" | $GZIP_CMD > $DB_BACKUP_FILE_NAME
    $MYSQLDUMP_CMD --user=$MYSQL_USER --password=$MYSQL_PASS $MYSQL_PARMS $DATABASES | $GZIP_CMD -9 >> $DB_BACKUP_FILE_NAME
    if [ $? -ne 0 ]; then        
        $RM -f $DB_BACKUP_FILE_NAME
        exit 1;
    fi
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 1;" | $GZIP_CMD >> $DB_BACKUP_FILE_NAME
else
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 0;" > $DB_BACKUP_FILE_NAME
    $MYSQLDUMP_CMD --user=$MYSQL_USER --password=$MYSQL_PASS $MYSQL_PARMS $DATABASES >> $DB_BACKUP_FILE_NAME
    $ECHO_CMD "SET FOREIGN_KEY_CHECKS = 1;" >> $DB_BACKUP_FILE_NAME    
fi

exit 0
