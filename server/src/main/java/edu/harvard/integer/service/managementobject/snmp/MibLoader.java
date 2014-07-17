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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPModuleHistory;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.discovery.VendorIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleHistoryDAO;
import edu.harvard.integer.util.LoggerUtil;

/**
 * 
 * Helper class to save the parsed MIB. If the MIB has already been imported
 * this will update the MIB with any new OID's.
 * 
 * @author David Taylor
 * 
 */
@Stateless
public class MibLoader implements MibLoaderLocalInterface {

	@Inject
	private PersistenceManagerInterface persistenceManager;

	@Inject
	private Logger logger;

	private SNMPDAO snmpDao = null;
	private SNMPModuleDAO snmpModuleDAO = null;

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

		result.setModule(saveSNMPModule(result));

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
			logger.info("Table: " + snmpTable.getIdentifier() + " "
					+ snmpTable.getName() + " " + snmpTable.getOid());

			if (snmpTable.getTableOids() != null) {
				logger.info("Num of table oids: "
						+ snmpTable.getTableOids().size());
				for (SNMP snmpOid : snmpTable.getTableOids()) {
					logger.info("Oid: " + snmpOid.getIdentifier() + " "
							+ snmpOid.getDisplayName() + " " + snmpOid.getOid());
				}
			}
		}

		logger.info("Num of Scalors: " + result.getSnmpScalars().size());
		for (SNMP snmpOid : result.getSnmpScalars()) {
			logger.info("Oid: " + snmpOid.getIdentifier() + " "
					+ snmpOid.getDisplayName() + " " + snmpOid.getOid());
		}
	}

	/**
	 * Create the MIBInfo and save in the database. 
	 * @param result. The MIBInfo for this imported mib.
	 * 
	 * @throws IntegerException
	 */
	private MIBInfo createMibInfo(MIBImportResult result)
			throws IntegerException {
		MIBInfo mibInfo = null;

		MIBInfoDAO infoDAO = persistenceManager.getMIBInfoDAO();

		mibInfo = infoDAO.findByName(result.getModule().getName());
		if (mibInfo == null)
			mibInfo = new MIBInfo();

		mibInfo.setModule(result.getModule());
		mibInfo.setName(result.getModule().getName());
		mibInfo.setScalors(result.getSnmpScalars());
		mibInfo.setTables(result.getSnmpTable());

		mibInfo = infoDAO.update(mibInfo);

		return mibInfo;
	}

	/**
	 * Save a list of SNMP objects. This will check to 
	 * see if the SNMP object already exists in the database
	 * before createing a new instance.
	 * 
	 * @param oids. SNMP oids to be saved in the database
	 * @return the saved list of SNMP oids.
	 * 
	 * @throws IntegerException
	 */
	private List<SNMP> saveOids(List<SNMP> oids) throws IntegerException {

		if (oids == null) {
			logger.error("No OID's to save!");
			return null;
		}

		List<SNMP> savedOids = new ArrayList<SNMP>();
		for (SNMP snmpOid : oids) {

			savedOids.add(saveSNMPOid(snmpOid));

		}

		return savedOids;
	}

	/**
	 * Save a list of the SNMPTable oids in the database.
	 * 
	 * @param oids. SNMPTable oids to be saved.
	 * @return The saved SNMPTable oids. 
	 * 
	 * @throws IntegerException
	 */
	private List<SNMPTable> saveTableOids(List<SNMPTable> oids)
			throws IntegerException {

		List<SNMPTable> savedOids = new ArrayList<SNMPTable>();

		for (SNMPTable snmpOid : oids) {

			snmpOid.setTableOids(saveOids(snmpOid.getTableOids()));
			snmpOid.setIndex(saveOids(snmpOid.getIndex()));

			SNMPTable dbTable = null;
			SNMP dbOid = snmpDao.findByOid(snmpOid.getOid());

			if (dbOid instanceof SNMPTable) {
				dbTable = (SNMPTable) dbOid;

				logger.info("Found table " + dbTable.getID() + "::"
						+ snmpOid.getName() + "::" + snmpOid.getOid()
						+ " In the database. Will update it.");

				snmpDao.copyFields(dbTable, snmpOid);

			} else
				dbTable = snmpOid;

			dbTable = snmpDao.update(dbTable);

			savedOids.add(dbTable);

		}

		return savedOids;
	}

	/**
	 * Save the MIB in the database.
	 * 
	 * @param result. The result of the MIB parsing. 
	 * @return SNMPModule created from the MIB.
	 * 
	 * @throws IntegerException
	 */
	private SNMPModule saveSNMPModule(MIBImportResult result)
			throws IntegerException {

		if (logger == null) {
			System.err.println("Logger is null!!!");

		}
		if (persistenceManager == null) {
			System.err.println("PersistenceManage is null!!!");

		}

		if (logger != null)
			logger.info("Save SNMPModule " + result.getModule().getName()
					+ " OID: " + result.getModule().getOid());
		try {

			SNMPModule dbModule = snmpModuleDAO.findByOid(result.getModule()
					.getOid());
			if (dbModule != null) {
				dbModule.setDescription(result.getModule().getDescription());
				dbModule.setLastUpdated(result.getModule().getLastUpdated());

				if (dbModule.getHistory() != null
						&& result.getModule().getHistory() != null) {
					if (dbModule.getHistory().size() < result.getModule()
							.getHistory().size()) {
						for (int i = dbModule.getHistory().size(); i < result
								.getModule().getHistory().size(); i++)
							dbModule.getHistory().add(
									result.getModule().getHistory().get(i));
					}
				} else
					dbModule.setHistory(result.getModule().getHistory());

			} else
				dbModule = snmpModuleDAO.update(result.getModule());

			if (dbModule.getHistory() != null) {

				SNMPModuleDAO snmpModuleDAO = persistenceManager
						.getSNMPModuleDAO();
				SNMPModuleHistoryDAO historyDAO = persistenceManager
						.getSNMPModuleHistoryDAO();
				SNMPModuleHistory[] dbHistories = historyDAO
						.findByHistories(result.getModule().getHistory());
				if (dbHistories != null) {

					for (int i = 0; i < dbHistories.length; i++) {
						SNMPModuleHistory history = dbHistories[i];

						logger.info("Save module history " + history.getDate()
								+ " " + history.getDescription());
						SNMPModuleHistory moduleHistory = snmpModuleDAO
								.update(history);

						dbModule.getHistory().set(i, moduleHistory.getID());
					}
				}
			}

			if (result.getModule().getHistory() != null) {
				for (int i = dbModule.getHistory().size(); i < result
						.getModule().getHistory().size(); i++) {

					SNMPModuleHistory moduleHistory = snmpModuleDAO
							.update(result.getHistory().get(i));

					dbModule.getHistory().set(i, moduleHistory.getID());
				}
			}

			return dbModule;
		} catch (IntegerException e) {
			logger.error(LoggerUtil.filterLog("Error saveing SNMPModule "
					+ result.getModule()));

			throw e;
		}
	}

	/**
	 * Save the SNMP OID in the database. If the OID already exists 
	 * in the database the it will be updated.
	 * 
	 * @param snmpOid
	 * @return SNMP oid saved in the database.
	 * @throws IntegerException
	 */
	private SNMP saveSNMPOid(SNMP snmpOid) throws IntegerException {

		if (logger == null) {
			System.err.println("Logger is null!!!");
		}

		if (persistenceManager == null) {
			logger.error("PersistenceManage is null!!!");
		}

		if (snmpOid == null) {
			logger.error("SNMP oid is null!! Can not save");
			return null;
		}

		if (logger != null)
			logger.info("Save SNMP Oid: " + snmpOid.getName() + " OID: "
					+ snmpOid.getOid() + " "
					+ snmpOid.getClass().getSimpleName());
		try {

			SNMP dbOid = snmpDao.findByOid(snmpOid.getOid());
			if (dbOid != null) {
				logger.info("Update OID " + dbOid.getID());

				dbOid.setDescription(snmpOid.getDescription());
				dbOid.setMaxAccess(snmpOid.getMaxAccess());
				dbOid.setName(snmpOid.getName());
				dbOid.setNamespace(snmpOid.getNamespace());
				dbOid.setServiceElementTypes(snmpOid.getServiceElementTypes());
				dbOid.setSnmpModuleId(snmpOid.getSnmpModuleId());
				dbOid.setComment(snmpOid.getComment());

				snmpOid = snmpDao.update(dbOid);
			} else
				snmpOid = snmpDao.update(snmpOid);

		} catch (IntegerException e) {
			logger.error("Error saveing SNMPModule " + snmpOid + " Error "
					+ e.toString());

			throw e;
		}

		return snmpOid;
	}

	/**
	 * Save the result of a product MIB. The product MIB is used
	 * to identifier the type of device or component in a device.
	 */
	@Override
	public void loadProductMib(String vendor, MIBImportResult result)
			throws IntegerException {
		snmpDao = persistenceManager.getSNMPDAO();
		snmpModuleDAO = persistenceManager.getSNMPModuleDAO();
		VendorIdentifierDAO vendorIdentifierDAO = persistenceManager
				.getVendorIdentifierDAO();

		List<SNMP> scalars = result.getObjectIdentifiers();

		HashMap<String, VendorIdentifier> vendorIdentifierMap = new HashMap<String, VendorIdentifier>();
		VendorIdentifier[] allVendorsIds = vendorIdentifierDAO.findAll();
		for (VendorIdentifier vendorIdentifier : allVendorsIds) {
			vendorIdentifierMap.put(vendorIdentifier.getVendorOid(), vendorIdentifier);
		}
		
		for (SNMP snmp : scalars) {

			VendorIdentifier dbVendorSubtype = vendorIdentifierMap.get(snmp.getOid());
			
			if (dbVendorSubtype == null) {
				dbVendorSubtype = new VendorIdentifier();
			}

			if (logger.isDebugEnabled())
				logger.debug("Load " + vendor + " SubType " + snmp.getOid() + " :: "
						+ snmp.getName() + " VendorId "
						+ getVendorId(snmp.getOid()) + " Comment "
						+ snmp.getComment());

			dbVendorSubtype.setName(vendor.trim());
			dbVendorSubtype.setVendorOid(getVendorId(snmp.getOid()));
			dbVendorSubtype.setVendorSubtypeId(snmp.getOid());
			dbVendorSubtype.setVendorSubtypeName(snmp.getName().trim());
			dbVendorSubtype.setComment(snmp.getComment());

			vendorIdentifierDAO.update(dbVendorSubtype);
		}
	}

	/**
	 * Parse the vendor identifier portion of the spacific oid.
	 * 
	 * <p>
	 * Ex:
	 * <p>
	 * spacific oid: 1.3.6.1.4.1.9.1.100 = cisco 763
	 * <p>
	 * vendor oid:   1.3.6.1.4.1.9.1 = cisco
	 * 
	 * @param oid
	 * @return
	 */
	private String getVendorId(String oid) {
		String parts[] = oid.split("\\.");
		StringBuffer b = new StringBuffer();

		b.append(parts[0]);
		for (int i = 1; i < 7; i++)
			b.append('.').append(parts[i]);

		return b.toString();
	}
}
