/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.service.persistance;

import java.io.File;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.Path;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.persistence.DataPreLoadFile;
import edu.harvard.integer.common.persistence.PersistenceStepStatusEnum;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.StringPropertyNames;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.server.IntegerApplication;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.DistributionService;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.persistance.dao.persistance.DataPreLoadFileDAO;
import edu.harvard.integer.service.yaml.YamlManagerInterface;
import edu.harvard.integer.util.FileUtil;
/**
 * @author David Taylor
 *
 */
@Singleton
@Startup
@Path("/Database")
@DependsOn(value={ "DistributionService" } )
public class PersistenceService extends BaseService implements PersistenceServiceInterface {

	@Inject
	private Logger logger;
	
	@Inject
	private PersistenceManagerInterface persistanceManager;

	/**
	 * All PersistenceService initialization occurs here. 
	 */
	@PostConstruct
	public void init() {

		logger.warn("PersistenceServices is startint");
		
		logger.debug("PersistenceService starting");

		// Register the application for RESTfull interface
		IntegerApplication.register(this);

		loadPreloads();
		
	}

	
	private void loadPreloads() {
		logger.info("Loading preload data files");

		DataPreLoadFileDAO dao = persistanceManager.getDataPreLoadFileDAO();
		
		try {
			DataPreLoadFile[] perloads = dao.findAll();
			
			for (DataPreLoadFile dataPreLoadFile : perloads) {
				if (dataPreLoadFile.getStatus() == null || PersistenceStepStatusEnum.NotLoaded.equals(dataPreLoadFile.getStatus())) {
					loadDataFile(dataPreLoadFile);
					logger.info("Loaded " + dataPreLoadFile.getDataFile());
				} else
					logger.info("Preload already loaded!" + dataPreLoadFile);
			}
			
		} catch (IntegerException e) {
			logger.error("Error loading preload table! " + e.toString());
			
			e.printStackTrace();
		}
	}


	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadDataFile(DataPreLoadFile dataPreLoadFile) throws IntegerException {
		
		switch(dataPreLoadFile.getFileType()) {
		
			
		case TechnologyTreeYaml:
			loadTechnologyTreeYaml(dataPreLoadFile);
			break;
			
		case MIB:
			loadMib(dataPreLoadFile);
			break;
		}
		
	}


	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadMib(DataPreLoadFile dataPreLoadFile) throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.SnmpManager))
			return;
		
		MIBImportInfo mibFile = new MIBImportInfo();
		mibFile.setFileName(dataPreLoadFile.getDataFile());
		mibFile.setName(dataPreLoadFile.getDataFile());
		
		SnmpManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		if (manager != null) 
			manager.importMib(new MIBImportInfo[] { mibFile});
			
	}


	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadTechnologyTreeYaml(DataPreLoadFile dataPreLoadFile) throws IntegerException {
		
		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;
		
		IntegerProperties props = IntegerProperties.getInstance();
		
		String dataDirPath = props.getProperty(StringPropertyNames.DATADir) + "/yaml";
		File file = new File(dataDirPath + "/" + dataPreLoadFile.getDataFile());
		
		if (!file.exists()) {
			logger.error("YAML file " + file.getAbsolutePath() + " NOT found ");
			dataPreLoadFile.setErrorMessage("File not found ");
			persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
			return;
		}
		
		String data = FileUtil.readInMIB(file);
		
		YamlManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.YamlManager);
		if (manager != null) {
			try {
				manager.loadTechnologyTree(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);
				
			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}
		
		persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadTechnologyYaml(DataPreLoadFile dataPreLoadFile) throws IntegerException {
		
		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;
		
		IntegerProperties props = IntegerProperties.getInstance();
		
		String dataDirPath = props.getProperty(StringPropertyNames.DATADir) + "/";
		File file = new File(dataDirPath + "/" + dataPreLoadFile.getDataFile());
		
		if (!file.exists()) {
			logger.error("YAML file " + dataPreLoadFile.getDataFile() + " NOT found ");
			dataPreLoadFile.setErrorMessage("File not found ");
			persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
			return;
		}
		
		String data = FileUtil.readInMIB(file);
		
		YamlManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.YamlManager);
		if (manager != null) {
			manager.loadTechnology(data);
		}
		
		dataPreLoadFile.setTimeLoaded(new Date());
		dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);
		persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
	}


	/**
	 * @param dataPreLoadFile
	 */
	private void loadSQL(DataPreLoadFile dataPreLoadFile) {
		
		
	}
}
