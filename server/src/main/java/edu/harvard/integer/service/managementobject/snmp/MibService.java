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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
		
		String mibNameProp = props.getProperty(StringPropertyNames.BaseMibList);
		if (mibNameProp == null) {
			logger.error("Unable to load mibs. " + StringPropertyNames.BaseMibList.getFieldName() + " Is not set!!");
			return;
		}
		
		String mibNames[] = mibNameProp.split(",");
		
		String mibDirPath = null;
		
		mibDirPath = props.getProperty(StringPropertyNames.MIBDir) + "/";
		
		MIBInfo[] loadedMibs = null;
		try {
			loadedMibs = snmpManager.getImportedMibs();
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String string : mibNames) {
			
			MIBImportInfo importInfo = new MIBImportInfo();
			File file = new File(mibDirPath + string);
			if (!file.exists()) {
				logger.error("Unable to load " + file.getAbsolutePath() + " File not found!");
				continue;
			}
			
			importInfo.setFileName(string);
			importInfo.setMib(readInMIB(file));

			boolean mibLoaded = false;
			if (loadedMibs != null) {
				for (MIBInfo mibInfo : loadedMibs) {
					if (mibInfo.getName().equals(string)) {
						logger.info("MIB " + string + " already loaded!");
						mibLoaded = true;
						break;
					}

				}
			}
			
			if (!mibLoaded) {
				try {
					snmpManager.importMib( new MIBImportInfo[] { importInfo });
					logger.info("Loaded " + string);
					
				} catch (IntegerException e) {

					e.printStackTrace();
					logger.error("Error loading MIB " + string + " Error " + e.toString());
				}
			}
			
		}
		
		try {
			loadDefaultProductsMib();
		} catch (IntegerException e) {
			logger.info("Error loading product mibs! " + e.toString());
			e.printStackTrace();
			
		}
		
	}
	
	private void loadDefaultProductsMib() throws IntegerException {
		
		IntegerProperties props = IntegerProperties.getInstance();
		
		String mibNameProp = props.getProperty(StringPropertyNames.ProductsMib);
		if (mibNameProp == null) {
			logger.error("Unable to load mibs. " + StringPropertyNames.BaseMibList.getFieldName() + " Is not set!!");
			return;
		}
		
		String mibNames[] = mibNameProp.split(",");
		
		String mibDirPath = null;
		
		mibDirPath = props.getProperty(StringPropertyNames.MIBDir) + "/";
		
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
			importInfo.setMib(readInMIB(file));

			snmpManager.importProductMib(string, importInfo);
		}
				
	}
	
	private String readInMIB(File file) {
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(file.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		return content;
	}
	
}
