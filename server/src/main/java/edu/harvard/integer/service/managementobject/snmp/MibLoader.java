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

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPModuleHistory;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.service.persistance.PersistenceManager;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleDAO;

/**
 * @author David Taylor
 * 
 *         Helper class to save the parsed MIB. If the MIB has already been
 *         imported this will update the MIB with any new OID's.
 * 
 */
@Stateless
public class MibLoader implements MibLoaderLocalInterface {

	@Inject
	private PersistenceManager persistenceManager;

	@Inject
	private Logger logger;
	
	private SNMPDAO snmpDao = null;
	private SNMPModuleDAO snmpModuleDAO = null;
	private MIBInfoDAO mibInfoDAO = null;
	
	public MibLoader() {
	
	}

	/**
	 * Load a MIB into the system. This imported MIB must be associated with one
	 * or more capabilities before it can be use.
	 * 
	 * @param result
	 * @throws IntegerException
	 */
	public void load(MIBImportResult result) throws IntegerException {
		snmpDao = persistenceManager.getSNMPDAO();
		snmpModuleDAO = persistenceManager.getSNMPModuleDAO();
		mibInfoDAO = persistenceManager.getMIBInfoDAO();
		
		result.setModule(saveSNMPModule(result.getModule()));

		logger.info("Loaded module " + result.getModule().getDescription());

		result.setSnmpScalars(saveOids(result.getSnmpScalars()));

		logger.info("Loaded " + result.getSnmpScalars().size() + " scalors");

		result.setSnmpTable(saveTableOids(result.getSnmpTable()));

		logger.info("Loaded " + result.getSnmpTable().size() + " table oids");
		
		createMibInfo(result);
		
		logger.info("MIB filename:   " + result.getFileName());
		logger.info("SNMPModlue  :   " + result.getModule().getOid());
		logger.info("Description :   " + result.getModule().getDescription());
		logger.info("Errors      :   " + Arrays.toString(result.getErrors()));
		
		logger.info("Num of Tables:  " + result.getSnmpTable().size());
		for (SNMPTable snmpTable : result.getSnmpTable()) {
			logger.info("Table: " + snmpTable.getIdentifier() + " " + snmpTable.getName() + " " + snmpTable.getOid());
			
			if (snmpTable.getTableOids() != null) {
				logger.info("Num of table oids: " + snmpTable.getTableOids().size());
				for (SNMP snmpOid : snmpTable.getTableOids()) {
					logger.info("Oid: " + snmpOid.getIdentifier() + " " + snmpOid.getDisplayName() + " " + snmpOid.getOid());
				}
			}
		}
		
		logger.info("Num of Scalors: " + result.getSnmpScalars().size());
		for (SNMP snmpOid : result.getSnmpScalars()) {
			logger.info("Oid: " + snmpOid.getIdentifier() + " " + snmpOid.getDisplayName() + " " + snmpOid.getOid());
		}
	}

	/**
	 * @param result
	 * @throws IntegerException 
	 */
	private MIBInfo createMibInfo(MIBImportResult result) throws IntegerException {
		MIBInfo mibInfo = new MIBInfo();
		
		mibInfo.setModule(result.getModule());
		mibInfo.setName(result.getModule().getName());
		mibInfo.setScalors(result.getSnmpScalars());
		mibInfo.setTables(result.getSnmpTable());
		
		MIBInfoDAO infoDAO = persistenceManager.getMIBInfoDAO();
		mibInfo = infoDAO.update(mibInfo);
		
		return mibInfo;
	}

	private List<SNMP> saveOids(List<SNMP> oids) throws IntegerException {

		for (int i = 0; i < oids.size(); i++) {
			SNMP snmpOid = oids.get(i);
			oids.set(i, saveSNMPOid(snmpOid));
		}

		return oids;
	}

	private List<SNMPTable> saveTableOids(List<SNMPTable> oids)
			throws IntegerException {

		for (int i = 0; i < oids.size(); i++) {
			SNMP snmpOid = oids.get(i);
			((SNMPTable) snmpOid).setTableOids( saveOids(((SNMPTable) snmpOid).getTableOids()));
			
			snmpOid = saveSNMPOid(snmpOid);
			
			if (snmpOid instanceof SNMPTable)
				oids.set(i, (SNMPTable) snmpOid);
			else
				logger.error("OID: " + snmpOid.getName() + " " + snmpOid.getOid() + " Is NOT an SNMPTable!! "
						+ " Passed in " +oids.get(i).getClass().getSimpleName());
			
		}

		return oids;
	}

	private SNMPModule saveSNMPModule(SNMPModule module)
			throws IntegerException {

		if (logger == null) {
			System.err.println("Logger is null!!!");

		}
		if (persistenceManager == null) {
			System.err.println("PersistenceManage is null!!!");

		}
		logger.info("Save SNMPModule " + module.getName() + " OID: "
				+ module.getOid());
		try {
			
			SNMPModule dbModule = snmpModuleDAO.findByOid(module.getOid());
			if (dbModule != null) {
				dbModule.setDescription(module.getDescription());
				dbModule.setLastUpdated(module.getLastUpdated());
				
				if (dbModule.getHistory() != null && module.getHistory() != null) {
					if (dbModule.getHistory().size() < module.getHistory().size()) {
						for (int i = dbModule.getHistory().size(); i < module.getHistory().size(); i++)
							dbModule.getHistory().add(module.getHistory().get(i));
					}
				} else
					dbModule.setHistory(module.getHistory());
				
			} else
				dbModule = module;
				 
			
			if (dbModule.getHistory() != null) {
				
				SNMPModuleDAO snmpModuleDAO = persistenceManager.getSNMPModuleDAO();
				
				for (int i = 0; i < dbModule.getHistory().size(); i++) {
					SNMPModuleHistory history = dbModule.getHistory().get(i);

					logger.info("Save module history " + history.getDate()
							+ " " + history.getDescription());
					dbModule.getHistory().set(
							i, snmpModuleDAO.update(history));
				}
			}
			
			module = snmpModuleDAO.update(dbModule);
			
		} catch (IntegerException e) {
			logger.error("Error saveing SNMPModule " + module);

			throw e;
		}

		return module;
	}

	private SNMP saveSNMPOid(SNMP snmpOid) throws IntegerException {

		if (logger == null) {
			System.err.println("Logger is null!!!");

		}
		if (persistenceManager == null) {
			System.err.println("PersistenceManage is null!!!");

		}
		logger.info("Save SNMP Oid: " + snmpOid.getDisplayName() + " OID: "
				+ snmpOid.getOid() + " " + snmpOid.getClass().getSimpleName());
		try {
			
			SNMP dbOid = snmpDao.findByOid(snmpOid.getOid());
			if (dbOid != null) {
				dbOid.setAccessMethod(snmpOid.getAccessMethod());
				dbOid.setDescription(snmpOid.getDescription());
				dbOid.setMaxAccess(snmpOid.getMaxAccess());
				dbOid.setName(snmpOid.getName());
				dbOid.setNamespace(snmpOid.getNamespace());
				dbOid.setServiceElementTypes(snmpOid.getServiceElementTypes());
				dbOid.setSnmpModuleId(snmpOid.getSnmpModuleId());
				
				if (dbOid instanceof SNMPTable) {
					SNMPTable dbTable = (SNMPTable) dbOid;
					SNMPTable table = (SNMPTable) snmpOid;
					
					dbTable.setIndex(table.getIndex());
					dbTable.setTableOids(table.getTableOids());
					
					if (((SNMPTable) snmpOid).getIndex() != null) {
						logger.info("Save Index "
								+ Arrays.toString(((SNMPTable) snmpOid).getIndex()
										.toArray(new SNMP[0])) + " for OID "
										+ snmpOid.getOid());

						((SNMPTable) snmpOid).setIndex(saveOids(((SNMPTable) snmpOid)
								.getIndex()));
					}
				}
				
				snmpOid = snmpDao.update(dbOid);
			} else
				snmpOid = snmpDao.update(snmpOid);
			
		} catch (IntegerException e) {
			logger.error("Error saveing SNMPModule " + snmpOid);

			throw e;
		}

		return snmpOid;
	}
}
