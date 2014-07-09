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

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.server.parser.mibparser.MibParser;
import edu.harvard.integer.server.parser.mibparser.MibParserFactory;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;

/**
 * @see SnmpManagerInterface
 * 
 * @author David Taylor
 * 
 */
@Stateless
public class SnmpManager extends BaseManager implements SnmpManagerLocalInterface, SnmpManagerRemoteInterface {

	@Inject
	private MibLoaderLocalInterface mibLoader;

	@Inject
	private PersistenceManagerInterface persistenceManager;

	@Inject
	private Logger logger;

	@Resource
	private EJBContext context;


	/**
	 * @param managerType
	 */
	public SnmpManager() {
		super(ManagerTypeEnum.SnmpManager);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface
	 * #importMib(java.lang.String)
	 */
	@Override
	public MIBImportResult[] importMib(MIBImportInfo[] mibFile)
			throws IntegerException {

		logger.info("Caller: " + context.getCallerPrincipal());

		try {
			logger.info("Importing " + mibFile.length + " MIB's");
			for (MIBImportInfo mibImportInfo : mibFile) {
				String fileName = mibImportInfo.getFileName();
				int fileNameIdx = fileName.lastIndexOf('/');
				if (fileNameIdx > 0)
					fileName = fileName.substring(fileNameIdx + 1);

				fileNameIdx = fileName.lastIndexOf('\\');
				if (fileNameIdx > 0)
					fileName = fileName.substring(fileNameIdx + 1);

				mibImportInfo.setFileName(fileName);

				logger.info("MIB " + mibImportInfo.getFileName() + " Size "
						+ mibImportInfo.getMib().length());
			}

			MibParser mibParser = MibParserFactory
					.getParserSource(MibParserFactory.ParserProvider.MIBBLE);
			MIBImportResult[] results = mibParser.importMIB(mibFile, true);
			for (MIBImportResult result : results) {

				// Only save mib if the load was a success!
				if (result.getErrors() == null
						|| result.getErrors().length == 0)
					mibLoader.load(result);
				else {
					logger.error("MIB " + result.getFileName()
							+ " Not loaded!! "
							+ Arrays.toString(result.getErrors()));
				}

			}

			logger.info("Load of mibs complete! Got " + results.length
					+ " results");

			return results;
		} catch (IntegerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface#importProductMib(java.lang.String, edu.harvard.integer.common.snmp.MIBImportInfo)
	 */
	@Override
	public MIBImportResult importProductMib(String vendor, MIBImportInfo mibImport)
			throws IntegerException {
		logger.info("Caller: " + context.getCallerPrincipal());

		try {

			String fileName = mibImport.getFileName();
			int fileNameIdx = fileName.lastIndexOf('/');
			if (fileNameIdx > 0)
				fileName = fileName.substring(fileNameIdx + 1);

			fileNameIdx = fileName.lastIndexOf('\\');
			if (fileNameIdx > 0)
				fileName = fileName.substring(fileNameIdx + 1);

			mibImport.setFileName(fileName);

			logger.info("Load Product MIB " + mibImport.getFileName() + " Size "
					+ mibImport.getMib().length());

			MibParser mibParser = MibParserFactory
					.getParserSource(MibParserFactory.ParserProvider.MIBBLE);
			MIBImportResult[] results = mibParser.importMIB(
					new MIBImportInfo[] { mibImport }, true);
			
			for (MIBImportResult result : results) {

				logger.info("Number of product OIDs " + result.getObjectIdentifiers().size());

				
				// Only save mib if the load was a success!
				if (result.getErrors() == null
						|| result.getErrors().length == 0)
					mibLoader.loadProductMib(vendor, result);
				else {
					logger.error("MIB " + result.getFileName()
							+ " Not loaded!! "
							+ Arrays.toString(result.getErrors()));
				}

			}

			logger.info("Load of mibs complete! Got " + results.length
					+ " results");

			return results[0];
		}

		catch (IntegerException e) {
			e.printStackTrace();
			throw e;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface
	 * #getImportedMibs()
	 */
	@Override
	public MIBInfo[] getImportedMibs() throws IntegerException {

		logger.info("Caller: " + context.getCallerPrincipal().getName() + " "
				+ context.getCallerPrincipal().getClass());

		MIBInfoDAO mibInfoDAO = persistenceManager.getMIBInfoDAO();

		MIBInfo[] mibInfos = mibInfoDAO.findAll();
		MIBInfo[] mibs = new MIBInfo[mibInfos.length];
		for (int i = 0; i < mibInfos.length; i++) {

			mibs[i] = mibInfoDAO.createCleanCopy(mibInfos[i]);

		}

		return mibs;
	}

	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface#getImportedMibByName(java.lang.String)
	 */
	@Override
	public MIBInfo getMIBInfoByName(String string)
			throws IntegerException {
		MIBInfoDAO mibInfoDAO = persistenceManager.getMIBInfoDAO();

		MIBInfo mibInfo = mibInfoDAO.findByName(string);
		return mibInfo;
	}

	@Override
	public MIBInfo getMIBInfoByID(ID id) throws IntegerException {

		MIBInfoDAO mibInfoDAO = persistenceManager.getMIBInfoDAO();
		MIBInfo mibInfo = mibInfoDAO.findById(id);

		return mibInfo;
	}

	@Override
	public List<SNMP> findByNameStartsWith(String name) throws IntegerException {
		SNMPDAO dao = persistenceManager.getSNMPDAO();
		List<SNMP> oids = dao.findByOidSubtree(name);
		return oids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface
	 * #getSNMPByOid(java.lang.String)
	 */
	@Override
	public SNMP getSNMPByOid(String oid) throws IntegerException {
		SNMPDAO snmpdao = persistenceManager.getSNMPDAO();

		return snmpdao.findByOid(oid);
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface#getSNMPByName(java.lang.String)
	 */
	@Override
	public SNMP getSNMPByName(String name) throws IntegerException {
		SNMPDAO snmpdao = persistenceManager.getSNMPDAO();
		
		return snmpdao.findByName(name);
	}

	/**
	 * Update a single SNMP OID.
	 * 
	 * @param oid
	 * @return the updated OID.
	 * @throws IntegerException
	 */
	@Override
	public SNMP updateSNMP(SNMP oid) throws IntegerException {
		SNMPDAO snmpdao = persistenceManager.getSNMPDAO();
		oid = snmpdao.update(oid);
		return oid;
	}

}
