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

package edu.harvard.integer.common.service.managementobjectcapability;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SnmpDisplayStringTC;
import edu.harvard.integer.common.snmp.SnmpEnumList;
import edu.harvard.integer.common.snmp.SnmpEnumValue;
import edu.harvard.integer.common.snmp.SnmpIntegerTC;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Applicability;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.FCAPSEnum;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.common.topology.SnmpServiceElementTypeOverride;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.persistance.PersistenceServiceInterface;

/**
 * @author David Taylor
 * 
 */
@RunWith(Arquillian.class)
public class ManagementObjectCapbilityManagerTest {

	@Inject
	PersistenceServiceInterface persistenceService;
	
	@Inject
	ManagementObjectCapabilityManagerInterface managementObjectManager;

	@Inject
	SnmpManagerInterface snmpManager;

	Logger logger = LoggerFactory.getLogger(ManagementObjectCapbilityManagerTest.class);

	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("ManagementObjectCapbilityManagerTest.war");	}

	@Before
	public void initializeLogger() {
		// BasicConfigurator.configure();
	}

//	@Test(timeout=10000)
//	public void loadPreLoadDataFiles() {
//		persistenceService.loadPreLoadFiles();
//	}

	public void addCapability() {
		Capability capability = new Capability();
		capability.setName("SystemGroup");
		capability.setDescription("SNMP System Group");
		List<FCAPSEnum> fcaps = new ArrayList<FCAPSEnum>();
		fcaps.add(FCAPSEnum.Configuration);

		capability.setFcaps(fcaps);

		managementObjectManager.addCapability(capability);
	}

	@Test
	public void addCapabilitysysName() {
		Capability capability = new Capability();
		capability.setName("SysName");
		capability.setDescription("System name");
		List<FCAPSEnum> fcaps = new ArrayList<FCAPSEnum>();
		fcaps.add(FCAPSEnum.Configuration);

		capability.setFcaps(fcaps);

		managementObjectManager.addCapability(capability);
	}

	@Test
	public void getAllCapabilites() {

		Capability[] capabilities = findAllCapabilities();
		logger.info("Found " + capabilities.length
				+ " Capabilites in the database.");

	}

	@Test
	public void getCapabilityByName() {
		
		Capability capability = null;
		
		try {
			capability = managementObjectManager.getCapabilityByName("SysName");
			
			if (capability == null)
				addCapabilitysysName();
			
			capability = managementObjectManager.getCapabilityByName("SysName");
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		assert (capability != null);
	}
	
	@Test
	public void addManagementObjectToCapability() {

		Capability capability = new Capability();
		capability.setName("Interface table");
		capability.setDescription("SNMP Interface table");
		List<FCAPSEnum> fcaps = new ArrayList<FCAPSEnum>();
		fcaps.add(FCAPSEnum.Performance);

		capability = managementObjectManager.addCapability(capability);

		List<ServiceElementManagementObject> systemOids = new ArrayList<ServiceElementManagementObject>();
		SNMP oid = getSNMP("1.3.6.1.2.1.2.2.1.3", "ifType");
		
		systemOids.add(oid);
	
		oid = getSNMP("1.3.6.1.2.1.2.2.1.2", "ifDescr");
		
		systemOids.add(oid);

		assert (systemOids != null);

		for (ServiceElementManagementObject snmp : systemOids) {
			logger.info("Found OID:  " + snmp.getName());
		}

		try {
			managementObjectManager.addManagementObjectsToCapability(
					capability, systemOids);
		} catch (IntegerException e) {

			e.printStackTrace();

			fail("Failed to add oid to capability!" + e.toString());
		}
	}

	private SNMP getSNMP(String oidString, String name) {
		SNMP oid = null;
		try {
			oid = snmpManager.getSNMPByOid(oidString);
		} catch (IntegerException e1) {

			e1.printStackTrace();

			fail("Error gettting " + name + " OID! " + e1.toString());
		}

		if (oid == null) {
			oid = new SNMP();

			oid.setName(name);
			oid.setOid(oidString);

			try {
				oid = snmpManager.updateSNMP(oid);
			} catch (IntegerException e) {
				e.printStackTrace();
				fail(e.toString());
			}
		}

		return oid;
	}
	

	@Test
	public void findSNMPLike() {
		String rootOid = "1.3.6.1.2.1.1";
		
		try {
			List<SNMP> findByNameStartsWith = snmpManager.findByNameStartsWith(rootOid);
			
			if (findByNameStartsWith == null) {
				SNMP oid = getSNMP(rootOid + ".1", "sysName");
				logger.info("Created OID " + oid.getID().toDebugString() + " " + oid.getOid());
				
				oid = getSNMP(rootOid + ".2", "sysDescr");
				logger.info("Created OID " + oid.getID().toDebugString() + " " + oid.getOid());
				
				oid = getSNMP(rootOid + ".3", "sysUptime");
				logger.info("Created OID " + oid.getID().toDebugString() + " " + oid.getOid());
				findByNameStartsWith = snmpManager.findByNameStartsWith(rootOid);
				
			}
			
			assert(findByNameStartsWith != null);
			
			logger.info("Found " + findByNameStartsWith.size() + " oids in subtree " + rootOid);
			for (SNMP snmp : findByNameStartsWith) {
				logger.info("SubTreeOid " + snmp.getName() + " - " + snmp.getOid());
			}
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void loadManagementObjectsForCapability() {
		Capability[] capabilites = findAllCapabilities();

		for (Capability capability : capabilites) {
			try {
				List<ServiceElementManagementObject> managementObjects = managementObjectManager
						.getManagemntObjectsForCapability(capability.getID());

				logger.info("Found " + managementObjects.size()
						+ " ManagementObjects for capabiliyt "
						+ capability.getID());
				for (ServiceElementManagementObject managementObject : managementObjects) {
					logger.info("Found " + managementObject.getID());
				}
			} catch (IntegerException e) {

				e.printStackTrace();
				fail("Error getging ManagementObjects for capablity: "
						+ e.toString());
			}

			logger.info("Capability " + capability.getID() + " has ");
		}
	}

	private Capability[] findAllCapabilities() {

		List<Capability> capabilities = null;
		try {
			capabilities = managementObjectManager.getCapabilities();
		} catch (IntegerException e) {

			e.printStackTrace();

			fail("Error loading Capabilites! " + e.toString());
		}

		if (capabilities == null)
			logger.error("NO Capabilities found in the database");
		else
			logger.info("Found " + capabilities.size()
					+ " Capabilities in the database");

		assert (capabilities != null);

		for (Capability capability : capabilities) {
			logger.info("Found capablity " + capability.getIdentifier() + " "
					+ capability.getName());
		}

		return capabilities.toArray(new Capability[0]);
	}

	@Test
	public void exportJasonCapabilityManagementObjects() {
		String jsonFile = "capabilites.json";
		
		JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
														// org.codehaus.jackson.mapper.MappingJsonFactory
		JsonGenerator jsongenerator = null;
		try {

			jsongenerator = jsonFactory.createGenerator(new FileOutputStream(
					jsonFile), JsonEncoding.UTF8);
		} catch (IOException e) {

			e.printStackTrace();
		} // or Stream, Reader

		List<Capability> capabilities = null;
		try {
			capabilities = managementObjectManager.getCapabilities();

			logger.info("Loaded " + capabilities.size()
					+ " Capabilites to export to JSON");
		} catch (IntegerException e) {

			e.printStackTrace();

			fail("Error loading Capabilites! " + e.toString());
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			mapper.writeValue(jsongenerator, capabilities);

		} catch (JsonProcessingException je) {
			je.printStackTrace();
			fail(je.toString());
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.toString());
		}

		try {
			if (jsongenerator != null)
				jsongenerator.close();
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
		File jsonDataFile = new File(jsonFile);
		assert(jsonDataFile.exists());
		assert(jsonDataFile.length() > 0);
		
		assert(jsonDataFile.delete());
		
	}

	@Test
	public void createServiceElementType() {
		ServiceElementType type = new ServiceElementType();
		type.setFeatureSet("CoolFeature");
		type.addSignatureValue(null, SignatureTypeEnum.Vendor, "Cisco");
		type.addSignatureValue(null, SignatureTypeEnum.Model, "7604");
		type.addSignatureValue(null, SignatureTypeEnum.Firmware, "Firmware");
		type.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);

		type.setApplicabilities(TestUtil.createIdList(10, Applicability.class,
				"Applicablity"));
		type.setAttributeIds(TestUtil.createIdList(5, SNMP.class, "Attribute"));

		try {
			managementObjectManager.updateServiceElementType(type);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void addApplicability() {
		Applicability app = createApplicability();

		try {
			managementObjectManager.updateApplicability(app);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void findAllApplicablities() {
		addApplicability();

		try {
			Applicability[] overrides = managementObjectManager
					.getAllApplicabilities();

			assert (overrides != null);

			logger.info("Found " + overrides.length + " Appliciablities");

			assert (overrides.length != 0);

		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	public Applicability createApplicability() {
		Applicability applicability = new Applicability();
		applicability.setAccessMethod(AccessMethod.SNMP);

		List<ID> ids = TestUtil.createIdList(10, SNMP.class, "Oid");

		applicability.setManagementObjects(ids);

		return applicability;
	}

	@Test
	public void addSnmpServiceElementTypeOverride() {
		SnmpServiceElementTypeOverride override = new SnmpServiceElementTypeOverride();

		override.setMaxOutstanding(Integer.valueOf(4));
		override.setMaxRate(Integer.valueOf(5));
		override.setRetries(Integer.valueOf(6));
		override.setTimeout(Integer.valueOf(88));
		override.setName("SnmpOverride");

		try {
			managementObjectManager
					.updateSnmpServiceElementTypeOverride(override);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void findAllServiceElementTypeOverrides() {
		addSnmpServiceElementTypeOverride();

		try {
			SnmpServiceElementTypeOverride[] overrides = managementObjectManager
					.getAllSnmpServiceElementTypeOverride();

			assert (overrides != null);
			assert (overrides.length != 0);

			logger.info("Found " + Arrays.toString(overrides) + " SnmpOverides ");

		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void getManagementObjectsByIDs() {
		
		ID[] ids = {
			getSNMP(CommonSnmpOids.sysName, "sysName").getID(),
			getSNMP(CommonSnmpOids.sysDescr, "sysDescr").getID(),
			getSNMP(CommonSnmpOids.sysObjectID, "sysObjectID").getID()
		};
		
		
		ServiceElementManagementObject[] managementObjects = null;
		try {
			managementObjects = managementObjectManager
					.getManagementObjectsByIds(ids);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}

		assert(managementObjects != null);
		
		for (ServiceElementManagementObject serviceElementManagementObject : managementObjects) {
			logger.info("Found by ID: " + serviceElementManagementObject.getID());
		}

		assert(managementObjects.length == 3);
	}

	@Test
	public void addEnumSnmpSyntax() {
		SnmpEnumList enumTc = new SnmpEnumList();
		List<SnmpEnumValue> values = new ArrayList<SnmpEnumValue>();
		
		for (int i = 0; i < 10; i++) {
			SnmpEnumValue eValue1 = new SnmpEnumValue("Value" + i, Integer.valueOf(i));
			eValue1.setName("MyEnum" + i);
			values.add(eValue1);
		}
		
		enumTc.setValues(values);
		enumTc.setName("MyEnumTC");
		
		SNMP snmp = new SNMP();
		snmp.setCapabilityId(new ID(Long.valueOf(1), "Name", new IDType(Capability.class.getName())));
		snmp.setDescription("A good description");
		snmp.setDisplayName("Hi");
		snmp.setTextualConvetion("EnumList");
		snmp.setSyntax(enumTc);
		
		try {
			snmpManager.updateSNMP(snmp);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void addStringSnmpSyntax() {
		
		SnmpDisplayStringTC stringTC = new SnmpDisplayStringTC();
		stringTC.setName("String Syntax");
 		stringTC.setMinimumValue(0);
		stringTC.setMaximumValue(255);
		
		SNMP snmp = new SNMP();
		snmp.setCapabilityId(new ID(Long.valueOf(1), "Name", new IDType(Capability.class.getName())));
		snmp.setDescription("A good description");
		snmp.setDisplayName("Hi");
		snmp.setTextualConvetion("String");
		snmp.setSyntax(stringTC);
		
		try {
			snmpManager.updateSNMP(snmp);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void addIntegerSnmpSyntax() {
		
		SnmpIntegerTC stringTC = new SnmpIntegerTC();
		stringTC.setName("Integer Syntax");
		
		SNMP snmp = new SNMP();
		snmp.setCapabilityId(new ID(Long.valueOf(2), "Name", new IDType(Capability.class.getName())));
		snmp.setDescription("A good description");
		snmp.setDisplayName("Hi");
		snmp.setTextualConvetion("Integer");
		snmp.setSyntax(stringTC);
 		stringTC.setMinimumValue(0);
		stringTC.setMaximumValue(255);
		stringTC.setDefaultValue(333);
		
		try {
			snmpManager.updateSNMP(snmp);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
