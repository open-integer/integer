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

import java.util.ArrayList;
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

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.FCAPSEnum;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerLocalInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class ManagementObjectCapbilityManagerTest {

	@Inject
	ManagementObjectCapabilityManagerLocalInterface managementObjectManager;
	
	@Inject
	SnmpManagerInterface snmpManager;
	
	@Inject
	PersistenceManagerInterface persistencManager;
	
	@Inject
	Logger logger;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		WebArchive archive = ShrinkWrap
				.create(WebArchive.class, "ManagementObjectCapbilityManagerTest.war")
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addPackages(true, "org.apache.commons")
				.addPackages(true, "org.snmp4j")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-ds.xml");
		
		
		return archive;
	}

	@Before
	public void initializeLogger() {
		//BasicConfigurator.configure();
	}
	
	@Test
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
	public void getAllCapabilites() {
		
		Capability[] capabilities = findAllCapabilities();
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
		SNMP oid = new SNMP();
		oid.setName("ifName");
		oid.setOid("1.3.6.1.2.1.1");
		systemOids.add(oid);
		
		 oid = new SNMP();
		 oid.setName("ifDescr");
		 oid.setOid("1.3.6.1.2.1.2");
		 systemOids.add(oid);
			
		assert(systemOids != null);
		
		for (ServiceElementManagementObject snmp : systemOids) {
			logger.info("Found OID:  " + snmp.getName());
		}
		
	//	List<ServiceElementManagementObject> managementObjects = new ArrayList<ServiceElementManagementObject>();
		
		try {
			managementObjectManager.addManagementObjectsToCapability(capability, systemOids);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			
			fail("Failed to add oid to capability!" + e.toString());
		}
	}
	
	@Test
	public void loadManagementObjectsForCapability() {
		Capability[] capabilites = findAllCapabilities();
		
		for (Capability capability : capabilites) {
			try {
				List<ServiceElementManagementObject> managementObjects = managementObjectManager.getManagemntObjectsForCapability(capability.getID());
				
				logger.info("Found " + managementObjects.size() + " ManagementObjects for capabiliyt " + capability.getID());
				for (ServiceElementManagementObject managementObject : managementObjects) {
					logger.info("Found " + managementObject.getID());
				}
			} catch (IntegerException e) {
				
				e.printStackTrace();
				fail("Error getging ManagementObjects for capablity: " + e.toString());
			}
			
			logger.info("Capability " + capability.getID() + " has " );
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
			logger.info("Found " + capabilities.size() + " Capabilities in the database");
		
		assert(capabilities != null);
		
		for (Capability capability : capabilities) {
			logger.info("Found capablity " + capability.getIdentifier() + " " + capability.getName());
		}
		
		return capabilities.toArray(new Capability[0]);
	}
}
