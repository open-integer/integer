Jenkins archive location: github on branch master in directory jenkinsArchives
	jenkins						 . thinslice backup of the developers/Production Jenkins
									Just edit jenkins configuration xml and turn authentication off
									this jenkins was authenticated via github
	jenkinsadmin				 . thinslice backup of the ADMINISTRATIVE jenkins which did
									things like delete cloudformation stacks. This jenkins 
									only had an admin login.
									Just edit jenkins configuration xml and turn authentication off
	jenkinses					 . the jenkins war files that were running and all the plugins.

Integer directories in github: ( on branch development )
	SourceLicense.xml			 . xml representation of source license (for Eclipse?)
	agent						 . agent which does snmp queries
	build.gradle				 . gradle build file
	client						 . front end gui source
	common						 . code for common classes between server and ( client, agent )
	config						 . YAML configuration files for Integer
	database					 . dir for database installation scripts, pre-loads
	docs						 . All integer documentation
	gradle						 . gradle dir used by the bootstrap files to locally install gradle if it is missing
	gradle-app.setting			 . gradle support file
	gradle.properties			 . gradle support file
	gradlew						 . unix gradle bootstrap file
	gradlew.bat					 . msdos gradle bootstrap file
	jiraAndConfluenceBackups	 . backups of Confluence and JIRA data
	lastBuildArtifactsAndJavaDoc . snapshot of out last build. This is everything needed to install 
								 .  JAVA DOC is in this hierarchy.
	modules.gradle				 . gradle support file
	server						 . Source code for the server (DT)
	settings.gradle				 . gradle support file



	To build:
	1. clone source from github
		url: git@github.com:open-integer/integer.git
	2. get and install dependencies:
		wildfly-8.1.0.Final.tar.gz
		mariaDB
		java 1.7
	3. Create test databases The tests use TestConfig AND Config
	
		mysql -h localhost -u ${DBUSER} -p"${DBPASS}" <<EOF
		drop database if exists TestConfig;
		create database TestConfig;

		EOF
	4. Add user and create grants. You will have to do grants for both databases added in #3
		in your git clone see: 
			docs/MarksDocs/Erics\ Sheets/Integer\ DB\ User\ creation\ and\ GRANT\ instructions\ -\ Eric\ Remington\ -\ Confluence.pdf 
	5. cd to top of git clone:
		execute gradlew clean deployWildFly gwtc build ear createSchema javadoc
	
	6. set JBOSS_HOME to an empty directory 	
		Below is the MakeTGzPackages script from Jenkins kept in ManagedFiles. Note that a gradle deployServer 
		and deployWildfly is used with an empty JBOSS _HOME to deploy all of the build and wildfly artifacts to a directory I can 
		tar it up from.  

		gradle knows all of the locations for the build artifacts.  the targets for this are deployServer and deployWildfly
		When you are done just look into wat was your empty JBOSS_HOME



		#!/bin/bash
		export PRIVPRELOADS=${WORKSPACE}/IntegerPrivate
		export INTARCHIVE=${WORKSPACE}/Integer.archive
		export OLDARCHIVE=${WORKSPACE}/junk.$$
		export INTEGERWS=${WORKSPACE}/Integer
		export INSTALLSRC=${PRIVPRELOADS}/simTestInstance/root/bin
		export INSTALLTMP=${WORKSPACE}/install.$$

		rm -rf ${INSTALLTMP}
		mkdir -p ${INSTALLTMP}

		cd ${WORKSPACE}
		if [ -d ${INTARCHIVE} ]; then
		   mv ${INTARCHIVE} ${OLDARCHIVE}
		   rm -rf ${OLDARCHIVE} &
		fi
		cp -pr ${INTEGERWS} ${INTARCHIVE} &

		rm -rf ${PRIVPRELOADS} 
		git clone ssh://git@build.integer.harvard.edu/home/git/IntegerPrivate.git ${PRIVPRELOADS}
		if [ $? -ne 0 ]; then
		   echo "ERROR: git clone of private failed"
		   wait
		   exit 1
		fi
		export DEPLOYABLE=${WORKSPACE}/deployable
		export PRELOADS=${WORKSPACE}/preloads
 
		rm -rf ${DEPLOYABLE}
		rm -rf ${PRELOADS}

		export JBOSS_HOME=${DEPLOYABLE}/${WILDFLY}

		mkdir -p ${DEPLOYABLE}
		mkdir -p ${PRELOADS}
		mkdir -p ${JBOSS_HOME}
		mkdir -p ${DEPLOYABLE}/dynamic-preloads

		if [ ! -e ${WORKSPACE}/Integer/build/create_table.sql  ]; then
		   echo "ERROR: ${WORKSPACE}/Integer/build/create_table.sql does not exist "
		   exit 1
		fi

		echo "WILDFLY=${WILDFLY}" > ${DEPLOYABLE}/Integer.settings

		cd ${INTEGERWS}
		cp -p build/create_table.sql ${DEPLOYABLE}/dynamic-preloads
		#
		mkdir -p ${PRELOADS}/public
		cp -pr database ${PRELOADS}/public

		cd ${PRELOADS}
		tar cvzf ${WORKSPACE}/preloadsPublic.tgz public

		cd ${INTEGERWS}
		./gradlew --info deployServer deployWildFly
		cd ${DEPLOYABLE}
		tar cvzf ${WORKSPACE}/deployables.tgz * > ${WORKSPACE}/deployablestar.log 2>&1
		result=$?
		if [ ${result} -eq 0 ]; then
		   cd ${PRIVPRELOADS}
		   if [ ! -d database ]; then
			  echo "ERROR: ${PRIVPRELOADS}/database does not exist, exiting."
			  exit 1
		   fi
		   tar cvzf ${WORKSPACE}/PrivatePreloads.tgz database
		   result=$?
		fi
		if [ ${result} -eq 0 ]; then
		   if [ ! -d ${INSTALLSRC} ] || [ ! -d ${INSTALLTMP} ];  then
			  echo "ERROR: ${INSTALLSRC} or ${INSTALLTMP} does not exist, exiting."
			  ls -ld ${INSTALLSRC} ${INSTALLTMP}
			  exit 1
		   fi
		   cd ${INSTALLSRC}
		   cp Integer.answers ${INSTALLTMP}/Integer.answers.editme
		   cp installAnd* abort* ${INSTALLTMP}
		   cd ${INSTALLTMP}
		   tar cvzf ${WORKSPACE}/installer.tgz *
		   result=$?
		   cd ${WORKSPACE}
		   rm -rf ${INSTALLTMP}
		fi
		wait
		exit ${result}
		
		
Build and test systems:
	The build and test systems were generic RedHat Amazon images with 
		wget
		rhn-client-tools
		amazon aws and ec2 tools. ( google installation of these amazon has good instructions)
		git
		python
		java 1.7
		jenkins
		cloud-init
		curl
		
		
	The builds were performed completely in Jenkins, so be sure to set up from the 
	"thinslice backup of the developers/Production Jenkins" above if you wish to perform 
	a "production build"  The task is BuildInteger

	see the 2 screen shots in docs/Jenkins for a list of the jobs that are available in the 
	2 jenkins'.


Special AWS Cloud considerations: None, the build and deployment Hardware/VM environment should be 
	completely transparent to the build (obviously not the post build JOBS which created Test instances)
	and the running of project Integer.

The Jenkins installation created the testing instances in their own cloudformation stack, containing
a VPC and 2 instances.  Creating a new cloudformation stack eased cleanup of unneeded test instances

The build ( and mail ) servers were in a separate RelEng VPC but this was only to segregate 
them from the various developers VPCs.


The install was kicked off by a @reboot in the crontab of the EC2 instance
[root@ec2-54-86-77-252 ~]# crontab -l ; ls -l bin
	@reboot /root/bin/startup
	total 8
	lrwxrwxrwx 1 root root 58 Jun 11 10:15 abortInteger -> /root/IntegerPrivate/simTestInstance/root/bin/abortInteger
	lrwxrwxrwx 1 root root 68 Jul  2 15:11 installAndStartInteger -> /root/IntegerPrivate/simTestInstance/root/bin/installAndStartInteger
	-rw-r--r-- 1 root root 68 Jul  2 09:43 installDepends
	lrwxrwxrwx 1 root root 53 Jun 11 10:15 startup -> /root/IntegerPrivate/simTestInstance/root/bin/startup
	
	abortInteger is a convenience script which kills a running integer, wipes the DB and if it exists
	executes ~/bin/startup
	
	All of these are links so that a git pull of the IntegerPrivate repo would result in 
	an update of these scripts more conveniently located in /root/bin
	
	installDepends contained:
		yum install python MariaDB-server MariaDB-client java httpd mod_ssl
	 
