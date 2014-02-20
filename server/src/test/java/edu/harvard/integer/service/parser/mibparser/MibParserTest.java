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

package edu.harvard.integer.service.parser.mibparser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModuleHistory;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.server.parser.mibparser.MibParser;
import edu.harvard.integer.server.parser.mibparser.MibParserFactory;
import edu.harvard.integer.server.parser.mibparser.moduleloader.MibbleParser;

/**
 * @author David Taylor
 *
 */
public class MibParserTest {

	private Logger logger = LoggerFactory.getLogger(MibParserTest.class);
	
	@Test
	public void importRFC1065_SMI() {
		importIETFMIB("RFC1065-SMI");
	}

	@Test
	public void importRFC1155_SMI() {
		importIETFMIB("RFC1155-SMI");
	}

	@Before
	public void setup() {
		
	}
	
	/**
	 * Test the importing of RFC1213. This will read in the MIB and create the
	 * entries in the database for SNMPModule, SNMP (for scalors) and SNMPTable
	 * (for table oids)
	 * 
	 */
	@Test
	public void importRFC1213() {
		importIETFMIB("RFC1213-MIB");
	}

	private void importIETFMIB(String mibName) {
		importMib("ietf/" + mibName);
	}
	
	private void importMib(String mibName) {

			logger.info("Start test import of " + mibName);

			System.setProperty(MibbleParser.MIBFILELOCATON,
					"../server/mibs");

			File mibFile = new File("../server/mibs/" + mibName);
			if (mibFile.exists())
				System.out.println("Found rfc");
			else
				System.out.println("rfc not found! PATH: "
						+ mibFile.getAbsolutePath());

			String content = null;
			try {
				content = new String(Files.readAllBytes(mibFile.toPath()));

			} catch (IOException e) {

				e.printStackTrace();
				fail("Error loading MIB: " + e.getMessage());
			}

			MIBImportInfo importInfo = new MIBImportInfo();
			importInfo.setFileName(mibFile.getName());
			importInfo.setMib(content);
			importInfo.setStandardMib(true);

			MibParser mibParser = null;
			MIBImportResult[] importMIBs = null;
			
			try {
				mibParser = MibParserFactory.getParserSource(MibParserFactory.ParserProvider.MIBBLE);
				importMIBs = mibParser.importMIB(new MIBImportInfo[] { importInfo }, true);
				
			} catch (IntegerException e1) {
				e1.printStackTrace();
				assert(e1 == null);
			}
			
			try {
				
				assert(importMIBs != null);
				
				System.out.println("Got " + importMIBs.length + " import result(s)");
				
				for (MIBImportResult mibImportResult : importMIBs) {
					System.out.println("MIB filename:   " + mibImportResult.getFileName());
					if (mibImportResult.getModule() != null) {
						System.out.println("SNMPModlue  :   "
								+ mibImportResult.getModule().getOid());
						System.out.println("Description :   "
								+ mibImportResult.getModule().getDescription());
					} else
						System.out.println("SNMModule   :   MODULE IS NULL!!!!");

					System.out.println("Errors      :   "
							+ Arrays.toString(mibImportResult.getErrors()));

					if (mibImportResult.getHistory() == null || mibImportResult.getHistory().size() == 0)
						System.out.println("No history!");
					else {
						System.out.println("Number of history records: " + mibImportResult.getHistory().size());
						
						for (SNMPModuleHistory history : mibImportResult.getHistory()) {
							System.out.println("Date:        " + history.getDate());
							System.out.println("Discription: " + history.getDescription());
							System.out.println("Name:        " + history.getName());
						}
					}
					if (mibImportResult.getSnmpTable() != null) {
						System.out.println("Num of Tables:  "
								+ mibImportResult.getSnmpTable().size());
						for (SNMPTable snmpTable : mibImportResult.getSnmpTable()) {
							System.out.println("Table: " + snmpTable.getIdentifier() + " "
									+ snmpTable.getName() + " "
									+ snmpTable.getOid());

							if (snmpTable.getTableOids() != null) {
								System.out.println("Num of table oids: "
										+ snmpTable.getTableOids().size());
								for (SNMP snmpOid : snmpTable.getTableOids()) {
									System.out.println("Oid: " + snmpOid.getIdentifier()
											+ " " + snmpOid.getDisplayName() + " "
											+ snmpOid.getOid());
								}
							}
						}
					} else {
						System.out.println("NO TABLES for "
								+ mibImportResult.getFileName());
					}

					if (mibImportResult.getSnmpScalars() != null) {
						System.out.println("Num of Scalors: "
								+ mibImportResult.getSnmpScalars().size());
						for (SNMP snmpOid : mibImportResult.getSnmpScalars()) {
							System.out.println("Oid: " + snmpOid.getIdentifier() + " "
									+ snmpOid.getDisplayName() + " "
									+ snmpOid.getOid());
						}
					} else {
						System.out.println("NO SCALORS for "
								+ mibImportResult.getFileName());
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
				System.out.println("Error loading mibs! " + e.toString());
				assert(e.getMessage() == null);
			}


	}
}
