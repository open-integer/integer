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

import java.util.ArrayList;
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
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.service.managementobjectcapability.snmp.ImportMIBTest;
import edu.harvard.integer.common.snmp.SNMP;
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
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;

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
	
	@Inject
	private PersistenceManagerInterface persistenceManager;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("ServiceElementDiscoveryManagerTest.war");
	}
	
	@Before
	public void initializeLogger() {
		
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
		
		/**
		 * Make sure the MIB's have been loaded.
		 */
		ImportMIBTest importMibsTest = new ImportMIBTest();
		importMibsTest.setLogger(logger);
		importMibsTest.setPersistenceManger(persistenceManager);
		importMibsTest.setSnmpObjectManager(snmpManager);
		importMibsTest.importMIBs();
		

	}
		
	
	@Test
	public void hostMibTest() {
		
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("public");
		snmpV2c.setWriteCommunity("public");
		CommunityAuth ca = new CommunityAuth(snmpV2c);
		
		String deviceAddress = "127.0.0.1";
		DiscoverNode discNode = new DiscoverNode(deviceAddress);
		Access ac = new Access(161, ca);
		
		discNode.setAccessElement(new ServiceElement());
		discNode.setAccess(ac);;
		
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
		List<SNMP> mgrObjects = serviceMgr.getToplLevelOIDs();
		for ( SNMP snmp : mgrObjects ) {

			VariableBinding vb = new VariableBinding(new OID(snmp.getOid() + ".0"));
			vbs.add(vb);
		}
		
		NetworkDiscovery discovery = new NetworkDiscovery(null, vbs, new DiscoveryId(Long.valueOf(1), Long.valueOf(1)));
		
		System.out.println("After creation element discover task for host MIB test ");
		try {
			
			ElementDiscoverTask<ElementAccess> discTask = new ElementDiscoverTask<>(discovery, discNode);
			discTask.call();
		} catch (IntegerException e) {
			if (NetworkErrorCodes.CannotReach.equals(e.getErrorCode())) 
				logger.info("Unable to reace " + deviceAddress);
			else {
				e.printStackTrace();
				fail(e.toString());
			}
				
		} catch (Exception e) {
			
			//e.printStackTrace();
			//fail(e.toString());
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
		
		DiscoverNet dNet = new DiscoverNet(netIp, mask, 0);
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
		
		try {
			ElementDiscoverTask<ElementAccess> discTask = new ElementDiscoverTask<>(discovery, discNode);
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
		
		discNode.setAccess(ac);		
		System.out.println("After creation element discover task *********************************************************** ");
		
		try {
			ElementDiscoverTask<ElementAccess> discTask2 = new ElementDiscoverTask<>(discovery, discNode);
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
