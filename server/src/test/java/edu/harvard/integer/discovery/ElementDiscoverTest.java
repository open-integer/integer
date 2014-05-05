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
package edu.harvard.integer.discovery;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Level;
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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.AccessTypeEnum;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.snmp.CommunityAuth;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.IpDiscoverySeed;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;

/**
 * @author dchan
 *
 */
@RunWith(Arquillian.class)
public class ElementDiscoverTest {

	@Inject
	SnmpManagerInterface snmpManager;
	
	@Inject
	ServiceElementDiscoveryManagerInterface serviceMgr;
	
	@Inject
	private Logger logger;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "ServiceElementDiscoveryManagerTest.war")
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addPackages(true, "org.apache.commons")
				.addPackages(true, "org.snmp4j")
				.addPackages(true, "uk.co.westhawk.snmp")
				//.addPackages(true, "org.jboss")
				//.addPackages(true, "org.wildfly")
				.addPackages(true, "org.xnio")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-ds.xml");
	}
	
	@Before
	public void initializeLogger() {
		
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
		
		importMib("RFC1065-SMI");
		importMib("RFC1155-SMI");
		importMib("RFC-1212");
		importMib("RFC1213-MIB");
		importMib("SNMPv2-SMI");
		importMib("SNMPv2-TC");
		importMib("SNMPv2-CONF");
		importMib("SNMPv2-MIB");
		importMib("IANAifType-MIB");
		importMib("IF-MIB");
		importMib("SNMP-FRAMEWORK-MIB");
		importMib("ENTITY-MIB.my");
		importMib("HOST-RESOURCES-MIB.my");
		importMib("CISCO-SMI.my");
		importMib("CISCO-ENTITY-VENDORTYPE-OID-MIB.my");
		importMib("CISCO-TC.my");
		importMib("CISCO-PRODUCTS-MIB.my");
	}
		
	
	private void importMib(String mibName) {

		logger.warn("Start test import of ******************************** "
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

		try {

			MIBImportResult[] importMib = snmpManager
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

				assert (mibImportResult.getErrors() == null || mibImportResult
						.getErrors().length == 0);

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


		} catch (IntegerException e) {

			e.printStackTrace();
			fail("Error getting mib parser");
		}
	}
	
	@Test
	public void serviceElementTask() {
		
		System.out.println("Start service element task ******************************************** .");
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
		
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("public");
		
		String netIp = "10.251.41.172";
		String mask = "255.255.255.248";   // Expect 8 address and usable address is 6.
		
		DiscoverNet dNet = new DiscoverNet(netIp, mask);
		List<Credential> creds = new ArrayList<>();
		creds.add(snmpV2c);
		
		snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("private");
		creds.add(snmpV2c);
		
		IpDiscoverySeed seed = new IpDiscoverySeed(dNet, creds);
		AccessPort ap = new AccessPort(161, AccessTypeEnum.SNMPv1);
		ap.addAccess(AccessTypeEnum.SNMPv2c);
		ap.addAccess(AccessTypeEnum.SNMPv3);
	
		seed.addAccessPort(ap);
		
		ap = new AccessPort(163, AccessTypeEnum.SNMPv1);
		ap.addAccess(AccessTypeEnum.SNMPv2c);
		ap.addAccess(AccessTypeEnum.SNMPv3);
		
		seed.addAccessPort(ap);
		
		DiscoveryId id = new DiscoveryId(Long.valueOf(1), Long.valueOf(1));
		
		List<SNMP> mgrObjects = serviceMgr.getToplLevelOIDs();
		for ( SNMP snmp : mgrObjects ) {

			VariableBinding vb = new VariableBinding(new OID(snmp.getOid() + ".0"));
			vbs.add(vb);
		}
		
		snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("integerrw");
		snmpV2c.setWriteCommunity("integerrw");
		CommunityAuth ca = new CommunityAuth(snmpV2c);
		
		String deviceAddress = "10.240.127.121";
		DiscoverNode discNode = new DiscoverNode(deviceAddress);
		Access ac = new Access(161, ca);
		
		discNode.setAccessElement(new ServiceElement());
		
		discNode.setAccess(ac);;
		
		NetworkDiscovery discovery = new NetworkDiscovery(seed, vbs, id);
		ElementDiscoverTask<ElementAccess> discTask = new ElementDiscoverTask<>(discovery, discNode);
		
		System.out.println("After creation element discover task *********************************************************** ");
		
		try {
			discTask.call();
		} catch (IntegerException e) {
			if (NetworkErrorCodes.CannotReach.equals(e.getErrorCode())) 
				logger.info("Unable to reace " + deviceAddress);
			else {
				e.printStackTrace();
				fail(e.toString());
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

	@Test
	public void discoverTestRouter() {
		
		System.out.println("Start service element task ******************************************** .");
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
		
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("public");
		
		String netIp = "10.251.41.172";
		String mask = "255.255.255.248";   // Expect 8 address and usable address is 6.
		
		DiscoverNet dNet = new DiscoverNet(netIp, mask);
		List<Credential> creds = new ArrayList<>();
		creds.add(snmpV2c);
		
		snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("private");
		creds.add(snmpV2c);
		
		IpDiscoverySeed seed = new IpDiscoverySeed(dNet, creds);
		AccessPort ap = new AccessPort(161, AccessTypeEnum.SNMPv1);
		ap.addAccess(AccessTypeEnum.SNMPv2c);
		ap.addAccess(AccessTypeEnum.SNMPv3);
	
		seed.addAccessPort(ap);
		
		ap = new AccessPort(163, AccessTypeEnum.SNMPv1);
		ap.addAccess(AccessTypeEnum.SNMPv2c);
		ap.addAccess(AccessTypeEnum.SNMPv3);
		
		seed.addAccessPort(ap);
		
		DiscoveryId id = new DiscoveryId(Long.valueOf(1), Long.valueOf(1));
		
		List<SNMP> mgrObjects = serviceMgr.getToplLevelOIDs();
		for ( SNMP snmp : mgrObjects ) {

			VariableBinding vb = new VariableBinding(new OID(snmp.getOid() + ".0"));
			vbs.add(vb);
		}
		
		snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("integerrw");
		snmpV2c.setWriteCommunity("integerrw");
		CommunityAuth ca = new CommunityAuth(snmpV2c);
		
		String deviceAddress = "10.240.127.121";
		DiscoverNode discNode = new DiscoverNode(deviceAddress);
		Access ac = new Access(161, ca);
		
		discNode.setAccessElement(new ServiceElement());
		
		discNode.setAccess(ac);;
		
		NetworkDiscovery discovery = new NetworkDiscovery(seed, vbs, id);
		ElementDiscoverTask<ElementAccess> discTask = new ElementDiscoverTask<>(discovery, discNode);
		
		System.out.println("After creation element discover task *********************************************************** ");
		
		try {
			discTask.call();
		} catch (IntegerException e) {
			if (NetworkErrorCodes.CannotReach.equals(e.getErrorCode())) 
				logger.info("Unable to reace " + deviceAddress);
			else {
				e.printStackTrace();
				fail(e.toString());
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
		deviceAddress = "10.240.127.144";
		discNode = new DiscoverNode(deviceAddress);
		ac = new Access(161, ca);
		
		discNode.setAccessElement(new ServiceElement());
		
		discNode.setAccess(ac);;
		

		ElementDiscoverTask<ElementAccess> discTask2 = new ElementDiscoverTask<>(discovery, discNode);
		
		System.out.println("After creation element discover task *********************************************************** ");
		
		try {
			discTask2.call();
		} catch (IntegerException e) {
			if (NetworkErrorCodes.CannotReach.equals(e.getErrorCode())) 
				logger.info("Unable to reace " + deviceAddress);
			else {
				e.printStackTrace();
				fail(e.toString());
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
}
