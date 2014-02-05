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

package edu.harvard.integer.common.service.managementobjectcapability.snmp;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.service.managementobject.provider.ServiceProviderMain;
import edu.harvard.integer.service.managementobject.snmp.SnmpObjectManagerLocalInterface;
import edu.harvard.integer.service.persistance.PersistenceManager;

/**
 * @author David Taylor
 * 
 * Test program for importing of MIB's into the Integer system.
 * 
 */
@RunWith(Arquillian.class)
public class ImportMIBTest {

	@Inject
	private SnmpObjectManagerLocalInterface snmpObjectManager;

	@Inject
	private PersistenceManager persistenceManager;
	
	@Inject
	private Logger logger;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-ds.xml");
	}

	/**
	 * Test the importing of RFC1213. This will read in the MIB and create
	 * the entries in the database for SNMPModule, SNMP (for scalors) 
	 * and SNMPTable (for table oids)
	 * 
	 */
	@Test
	public void importRFC1213() {

		logger.info("Start test import of RFC1213");
		
		System.setProperty(ServiceProviderMain.MIBFILELOCATON, "../serviceprovider/mibs");
		
		File mibFile = new File("../serviceprovider/mibs/ietf/RFC1213-MIB");
		if (mibFile.exists())
			System.out.println("Found rfc");
		else
			System.out.println("rfc not found! PATH: " + mibFile.getAbsolutePath());
	
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));
			
		} catch (IOException e) {
			
			e.printStackTrace();
			fail("Error loading MIB: "+ e.getMessage());
		}
		
		MIBImportInfo importInfo = new MIBImportInfo();
		importInfo.setFileName("RFC1213-MIB");
		importInfo.setMib(content);
		importInfo.setStandardMib(true);
		
		MIBImportResult[] importMIBs = null;
		try {
			
			MIBImportResult[] importMib = snmpObjectManager.importMib(new MIBImportInfo[] { importInfo });
		
			if (importMib == null) {
				logger.error("Got null back from importMIB");
				return;
			}
			
			System.out.println("Got " + importMib.length + " import results");
			for (MIBImportResult mibImportResult : importMib) {
				logger.info("MIB filename:   " + mibImportResult.getFileName());
				logger.info("SNMPModlue  :   " + mibImportResult.getModule().getOid());
				logger.info("Description :   " + mibImportResult.getModule().getDescription());
				logger.info("Errors      :   " + Arrays.toString(mibImportResult.getErrors()));
				
				logger.info("Num of Tables:  " + mibImportResult.getSnmpTable().size());
				for (SNMPTable snmpTable : mibImportResult.getSnmpTable()) {
					logger.info("Table: " + snmpTable.getIdentifier() + " " + snmpTable.getName() + " " + snmpTable.getOid());
					
					if (snmpTable.getTableOids() != null) {
						logger.info("Num of table oids: " + snmpTable.getTableOids().size());
						for (SNMP snmpOid : snmpTable.getTableOids()) {
							logger.info("Oid: " + snmpOid.getIdentifier() + " " + snmpOid.getDisplayName() + " " + snmpOid.getOid());
						}
					}
				}
				
				logger.info("Num of Scalors: " + mibImportResult.getSnmpScalars().size());
				for (SNMP snmpOid : mibImportResult.getSnmpScalars()) {
					logger.info("Oid: " + snmpOid.getIdentifier() + " " + snmpOid.getDisplayName() + " " + snmpOid.getOid());
				}
			}
			
			IDType type = new IDType();
			type.setClassType(SNMPModule.class);
			
			Object[] findAll = persistenceManager.findAll(type);
			System.out.println("Found " + findAll.length + " type " + type.getClassType());
			
			for (Object object : findAll) {
				System.out.println("Found " + object);
			}
			
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail("Error getting mib parser");
		}
		
	}

}
