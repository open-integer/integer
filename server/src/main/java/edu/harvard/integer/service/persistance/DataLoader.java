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

package edu.harvard.integer.service.persistance;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.persistence.DataPreLoadFile;
import edu.harvard.integer.common.persistence.PersistenceStepStatusEnum;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.StringPropertyNames;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.persistance.dao.persistance.DataPreLoadFileDAO;
import edu.harvard.integer.service.yaml.YamlManagerInterface;
import edu.harvard.integer.util.FileUtil;
import edu.harvard.integer.util.Resource;

/**
 * This is a helper class to load the YAML data files. This is called from the
 * PersistenceService on startup to load the YAML, SQL and MIBs
 * 
 * @author David Taylor
 * 
 */
@Stateless
public class DataLoader implements DataLoaderInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface persistanceManager;

	/**
	 * Load the data file. The data files loaded are YAML, MIB or SQL 
	 * data types.
	 * 
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	public void loadDataFile(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		switch (dataPreLoadFile.getFileType()) {

		case Service:
			loadService(dataPreLoadFile);
			break;
			
		case TechnologyTreeYaml:
		case TechnologyYaml:
			loadTechnologyTreeYaml(dataPreLoadFile);
			break;

		case CategoryYaml:
			loadCategoryYaml(dataPreLoadFile);
			break;
			
		case ServiceElementTypeYaml:
			loadServiceElementTypeYaml(dataPreLoadFile);
			break;

		case MIB:
			loadMib(dataPreLoadFile);
			break;

		case ProductMIB:
			loadProductMib(dataPreLoadFile);
			break;

		case VendorContainmentYaml:
			loadVendorContainmentYaml(dataPreLoadFile);
			break;

		case SQL:
			loadSQLData(dataPreLoadFile);
			break;

		case Location:
			loadLocationData(dataPreLoadFile);
			break;
			
		default:
			logger.error("Unknown data file type "
					+ dataPreLoadFile.getFileType() + " Can not load!!");
		}

	}


	/**
	 * @param dataPreLoadFile
	 */
	private void loadLocationData(DataPreLoadFile dataPreLoadFile) throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);
		
		if (manager != null) {
			try {
				manager.importLocation(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}
		
	}


	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadService(DataPreLoadFile dataPreLoadFile) throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);
		
		if (manager != null) {
			try {
				manager.importService(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}
	
	}


	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadSQLData(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		DataPreLoadFileDAO dao = persistanceManager.getDataPreLoadFileDAO();

		String data = FileUtil.readInMIB(file);

		String[] sqlCommands = data.split(";");

		for (String sqlCmd : sqlCommands) {

			if (sqlCmd.trim().length() > 1) {
				logger.info("Execute SQL command " + sqlCmd);
				dao.loadSQL(sqlCmd);
			} else
				logger.info("Skip blank line");
		}
		
		dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

	}

	private File getFile(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		IntegerProperties props = IntegerProperties.getInstance();

		String dataDirPath = Resource.getWildflyHome()
				+ props.getProperty(StringPropertyNames.DATADir) + "/"
				+ dataPreLoadFile.getFileType().getDataSubDir();
		File file = new File(dataDirPath + "/" + dataPreLoadFile.getDataFile());

		if (!file.exists()) {
			logger.error(dataPreLoadFile.getFileType() + " file "
					+ file.getAbsolutePath() + " NOT found ");
			dataPreLoadFile.setErrorMessage("File not found ");
			persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
			return null;
		}

		return file;
	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadProductMib(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.SnmpManager))
			return;

		MIBImportInfo mibFile = new MIBImportInfo();

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		mibFile.setFileName(dataPreLoadFile.getDataFile());
		mibFile.setName(dataPreLoadFile.getDataFile());
		mibFile.setMib(FileUtil.readInMIB(file));

		SnmpManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.SnmpManager);
		if (manager != null) {

			try {
				MIBImportResult importMib = manager.importProductMib(
						dataPreLoadFile.getName(), mibFile);

				if (importMib.getErrors() == null) {
					dataPreLoadFile.setTimeLoaded(new Date());
					dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);
				} else {
					dataPreLoadFile.setErrorMessage(Arrays.toString(importMib
							.getErrors()));

					dataPreLoadFile
							.setStatus(PersistenceStepStatusEnum.NotLoaded);
				}

			} catch (Throwable e) {
				logger.error("Error loading product MIB "
						+ dataPreLoadFile.getName());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
				dataPreLoadFile.setErrorMessage(e.getMessage());
			}

			persistanceManager.getDataPreLoadFileDAO().update(dataPreLoadFile);
		}

	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadMib(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.SnmpManager))
			return;

		MIBImportInfo mibFile = new MIBImportInfo();

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		mibFile.setFileName(dataPreLoadFile.getDataFile());
		mibFile.setName(dataPreLoadFile.getDataFile());
		mibFile.setMib(FileUtil.readInMIB(file));

		SnmpManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.SnmpManager);
		if (manager != null) {

			try {
				MIBImportResult[] importMib = manager
						.importMib(new MIBImportInfo[] { mibFile });

				if (importMib.length > 0) {

					dataPreLoadFile.setErrorMessage(null);

					if (importMib[0].getErrors() != null) {
						for (String errorMessage : importMib[0].getErrors()) {
							if (dataPreLoadFile.getErrorMessage() != null)
								dataPreLoadFile.setErrorMessage(dataPreLoadFile
										.getErrorMessage() + errorMessage);
							else
								dataPreLoadFile.setErrorMessage(errorMessage);

							dataPreLoadFile
									.setStatus(PersistenceStepStatusEnum.NotLoaded);
						}
					} else {
						dataPreLoadFile
								.setStatus(PersistenceStepStatusEnum.Loaded);
						dataPreLoadFile.setTimeLoaded(new Date());
					}
				}

			} catch (Throwable e) {
				logger.error("Error loading MIB " + dataPreLoadFile.getName()
						+ e.toString());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
				dataPreLoadFile.setErrorMessage(e.getMessage());
			}

		}

	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadTechnologyTreeYaml(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);

		if (manager != null) {
			try {
				manager.loadTechnologyTree(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}

	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadServiceElementTypeYaml(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);
		if (manager != null) {
			try {
				manager.loadServiceElementType(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}

	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException
	 */
	private void loadVendorContainmentYaml(DataPreLoadFile dataPreLoadFile)
			throws IntegerException {

		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);
		if (manager != null) {
			try {
				manager.loadVendorContainment(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}

	}

	/**
	 * @param dataPreLoadFile
	 * @throws IntegerException 
	 */
	private void loadCategoryYaml(DataPreLoadFile dataPreLoadFile) throws IntegerException {
		if (!DistributionManager.isLocalManager(ManagerTypeEnum.YamlManager))
			return;

		File file = getFile(dataPreLoadFile);
		if (file == null) {
			logger.error("Unable to get data file "
					+ dataPreLoadFile.getDataFile());
			return;
		}

		String data = FileUtil.readInMIB(file);

		YamlManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.YamlManager);
		
		if (manager != null) {
			try {
				manager.loadCategory(data);

				dataPreLoadFile.setTimeLoaded(new Date());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.Loaded);

			} catch (IntegerException e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			} catch (Throwable e) {
				dataPreLoadFile.setErrorMessage(e.getLocalizedMessage());
				dataPreLoadFile.setStatus(PersistenceStepStatusEnum.NotLoaded);
			}
		}

		
	}

}
