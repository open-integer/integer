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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.server.parser.mibparser.MibParser;
import edu.harvard.integer.server.parser.mibparser.MibParserFactory;
import edu.harvard.integer.server.parser.mibparser.MibParserFactory.ParserProvider;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;

/**
 * @author David Taylor
 * 
 *         Test program for importing of MIB's into the Integer system.
 * 
 */
@RunWith(Arquillian.class)
public class ImportMIBTest {

	@Inject
	private SnmpManagerInterface snmpObjectManager;

	@Inject
	private PersistenceManagerInterface persistenceManager;

	@Inject
	private Logger logger;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "ImportMIBTest.war")
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addPackages(true, "org.snmp4j")
				.addPackages(true, "uk.co.westhawk")
				.addPackages(true, "com.fasterxml.jackson")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-ds.xml");
	}

	@Before
	public void initializeLogger() {

		// System.setProperty(MibbleParser.MIBFILELOCATON,
		// "../server/build/mibs");
		// importDir();
	}

	@Test
	public void importRFC1065_SMI() {
		importIETFMIB("RFC1065-SMI");
	}

	@Test
	public void importRFC1155_SMI() {
		importIETFMIB("RFC1155-SMI");
	}

	public void importDir() {

		List<MIBImportInfo> importMibs = new ArrayList<>();
		String commonDir = "../server/mibs";
		File dirf = new File(commonDir);
		File[] fs = dirf.listFiles();

		importFiles(importMibs, fs, false);

		try {
			MibParser mibParser = MibParserFactory
					.getParserSource(ParserProvider.MIBBLE);
			mibParser.importMIB(
					importMibs.toArray(new MIBImportInfo[importMibs.size()]),
					true);
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		importMib(mibName);
	}

	private void importVendorMIB(String mibName) {
		importMib(mibName);
	}

	private void importMib(String mibName) {

		logger.warn("Start test import of ***************************************************************************** "
				+ mibName);

		File mibFile = null;
		mibFile = new File("../server/mibs/" + mibName);

		if (mibFile.exists())
			System.out.println("Found rfc");
		else {
			System.out.println("rfc not found! PATH: "
					+ mibFile.getAbsolutePath());

			fail("rfc not found! PATH: " + mibFile.getAbsolutePath());
		}

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

		MIBImportResult[] importMIBs = null;
		try {

			MIBImportResult[] importMib = snmpObjectManager
					.importMib(new MIBImportInfo[] { importInfo });

			if (importMib == null) {
				logger.error("Got null back from importMIB");
				return;
			}

			System.out.println("Got " + importMib.length + " import results");
			for (MIBImportResult mibImportResult : importMib) {
				logger.info("MIB filename:   " + mibImportResult.getFileName());
				if (mibImportResult.getModule() != null) {
					logger.info("SNMPModlue  :   "
							+ mibImportResult.getModule().getOid());
					logger.info("Description :   "
							+ mibImportResult.getModule().getDescription());
				} else
					logger.info("SNMModule   :   MODULE IS NULL!!!!");

				logger.info("Errors      :   "
						+ Arrays.toString(mibImportResult.getErrors()));

				if (mibImportResult.getSnmpTable() != null) {
					logger.info("Num of Tables:  "
							+ mibImportResult.getSnmpTable().size());
					for (SNMPTable snmpTable : mibImportResult.getSnmpTable()) {
						logger.info("Table: " + snmpTable.getIdentifier() + " "
								+ snmpTable.getName() + " "
								+ snmpTable.getOid());

						if (snmpTable.getTableOids() != null) {
							logger.info("Num of table oids: "
									+ snmpTable.getTableOids().size());
							for (SNMP snmpOid : snmpTable.getTableOids()) {
								logger.info("Oid: " + snmpOid.getIdentifier()
										+ " " + snmpOid.getDisplayName() + " "
										+ snmpOid.getOid());
							}
						}
					}
				} else {
					logger.error("NO TABLES for "
							+ mibImportResult.getFileName());
				}

				if (mibImportResult.getSnmpScalars() != null) {
					logger.info("Num of Scalors: "
							+ mibImportResult.getSnmpScalars().size());
					for (SNMP snmpOid : mibImportResult.getSnmpScalars()) {
						logger.info("Oid: " + snmpOid.getIdentifier() + " "
								+ snmpOid.getDisplayName() + " "
								+ snmpOid.getOid());
					}
				} else {
					logger.error("NO SCALORS for "
							+ mibImportResult.getFileName());
				}
			}

			Object[] findAll = persistenceManager.getSNMPModuleDAO().findAll();
			System.out.println("Found " + findAll.length + " SNMPModule");

			for (Object object : findAll) {
				System.out.println("Found " + object);
			}

		} catch (IntegerException e) {

			e.printStackTrace();
			fail("Error getting mib parser");
		}

	}

	@Test
	public void importSNMPv2_SMI() {
		importIETFMIB("SNMPv2-SMI");
	}

	@Test
	public void importSNMPv2_TC() {
		importIETFMIB("SNMPv2-TC");
	}

	@Test
	public void importSNMPv2_CONF() {
		importIETFMIB("SNMPv2-CONF");
	}

	@Test
	public void importSNMPv2_MIB() {
		importIETFMIB("SNMPv2-MIB");
	}

	@Test
	public void importIANAifType_MIB() {
		importIETFMIB("IANAifType-MIB");
	}

	@Test
	public void importEntityMIB() {
		importIETFMIB("ENTITY-MIB");
	}

	@Test
	public void importIfMIB() {
		importIETFMIB("IF-MIB");
	}

	@Test
	public void importCiscoSMI() {

		importVendorMIB("CISCO-SMI.my");
	}

	@Test
	public void importCiscoVendorEntity() {
		importVendorMIB("CISCO-ENTITY-VENDORTYPE-OID-MIB.my");
	}

	@Test
	public void importCiscoTC() {
		importVendorMIB("CISCO-TC.my");
	}

	@Test
	public void importCiscoProducts() {
		importVendorMIB("CISCO-PRODUCTS-MIB.my");
	}

	/**
	 * Import files into importMibs for fs.
	 * 
	 * @param importMibs
	 *            the import mibs
	 * @param fs
	 *            the fs
	 * @param isCommon
	 *            if
	 */
	private void importFiles(List<MIBImportInfo> importMibs, File[] fs,
			boolean isCommon) {

		for (File f : fs) {

			if (f.isFile()) {
				try {

					String content = getFileContents(f);
					MIBImportInfo minfo = new MIBImportInfo();
					minfo.setMib(content);

					minfo.setStandardMib(isCommon);
					minfo.setFileName(f.getName());
					importMibs.add(minfo);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Gets the file contents.
	 * 
	 * @param file
	 *            the file
	 * @return the file contents
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	static String getFileContents(File file) throws IOException {

		String p = file.getAbsolutePath();
		String content = new String(Files.readAllBytes(Paths.get(p)));
		return content;
	}

	public void findSysName() {
		SNMPDAO snmpdao = persistenceManager.getSNMPDAO();

		SNMP snmp = snmpdao.findByOid(CommonSnmpOids.sysName);

		assert (snmp != null);

	}

	@Test
	public void exportSNMPObjects() {
		JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
														// org.codehaus.jackson.mapper.MappingJsonFactory
		JsonGenerator jg = null;
		try {

			jg = jsonFactory.createGenerator(new FileOutputStream(
					"managementObject.json"), JsonEncoding.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		MIBInfo[] importedMibs = null;
		try {
			importedMibs = snmpObjectManager.getImportedMibs();
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		logger.info("Have " + importedMibs.length + " MIBs to export");
		try {
			mapper.writeValue(jg, importedMibs);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		}

		try {
			jg.close();
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.toString());
		}

	}
}
