/*
 *  Copyright (c) 2014 Harvard University and the persons
 *  identified as authors of the code.  All rights reserved. 
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 * 	.    Redistributions of source code must retain the above copyright
 * 		 notice, this list of conditions and the following disclaimer.
 * 
 * 	.    Redistributions in binary form must reproduce the above copyright
 * 		 notice, this list of conditions and the following disclaimer in the
 * 		 documentation and/or other materials provided with the distribution.
 * 
 * 	.    Neither the name of Harvard University, nor the names of specific
 * 		 contributors, may be used to endorse or promote products derived from
 * 		 this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *      
 */

package edu.harvard.integer.service.managementobject.snmp;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.StringPropertyNames;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.util.FileUtil;
import edu.harvard.integer.util.Resource;


/**
 * @author David Taylor
 * 
 * 
 *         Used to load the default set of MIB's into the database on startup.
 *         The list of MIB's is set in StringPropertyNames.MibList.
 */
@Singleton
@Startup
public class MibService extends BaseService {

	@Inject
	Logger logger;
	
	@Inject
	SnmpManagerInterface snmpManager;
	
	/**
	 * 
	 */
	public MibService() {
		
	}

	/**
	 * Load the default set of MIB's. The list of MIB's can be changed by updating the StringPropertyNames.BaseMibList property.
	 */
	@PostConstruct
	public void initialize() {
		
		IntegerProperties props = null;
		try {
			props = IntegerProperties.getInstance();
		} catch (IntegerException e) {
			
			e.printStackTrace();
			logger.error("Error loading MIBS! Could not get MIB dir path " + e.toString());
			return;	
		}
		
		
		try {
			loadDefaultProductsMib();
		} catch (IntegerException e) {
			logger.info("Error loading product mibs! " + e.toString());
			e.printStackTrace();
			
		} catch (Throwable e) {
			logger.info("Error loading product mibs! " + e.toString());
			e.printStackTrace();
		}
		
	}
	
	private void loadDefaultProductsMib() throws IntegerException {
		
		IntegerProperties props = IntegerProperties.getInstance();
		
		String mibNameProp = props.getProperty(StringPropertyNames.ProductsMib);
		
		String mibNames[] = mibNameProp.split(",");
		
		String mibDirPath = null;
		
		mibDirPath =  Resource.getWildflyHome() +  props.getProperty(StringPropertyNames.MIBDir) + "/";
		
		for (String string : mibNames) {
			MIBInfo mibInfo = snmpManager.getMIBInfoByName(string);
			if (mibInfo == null) {
				logger.info("Product mib " + string + " already loaded");
				continue;
			}
			
			File file = new File(mibDirPath + string);
			if (!file.exists()) {
				logger.error("Unable to load " + file.getAbsolutePath() + " File not found!");
				continue;
			}
			
			MIBImportInfo importInfo = new MIBImportInfo();
			importInfo.setFileName(string);
			importInfo.setMib(FileUtil.readInMIB(file));

			snmpManager.importProductMib(string, importInfo);
		}
				
	}
	
}
