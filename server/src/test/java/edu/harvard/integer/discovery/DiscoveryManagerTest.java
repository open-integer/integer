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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SnmpGlobalReadCredential;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.DiscoveryManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class DiscoveryManagerTest {
	
	@Inject
	private DiscoveryManagerInterface discoveryManager;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("DiscoveryManagerTest.war");
	}
	
	@Test
	public void getAllDiscoveryRules() {
		
		try {
			DiscoveryRule[] discoveryRules = discoveryManager.getAllDiscoveryRules();
			if (discoveryRules == null || discoveryRules.length == 0)
				addDiscoveryRule();
			
			 discoveryRules = discoveryManager.getAllDiscoveryRules();
			 
			 assert (discoveryRules != null);
			 assert (discoveryRules.length > 0);
			 
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

	/**
	 * 
	 */
	@Test
	public void addDiscoveryRule() {

		try {
			DiscoveryRule rule = createDiscoveryRuleRule();
			assert(rule.getIdentifier() != null);
			
		} catch (IntegerException e) {
		
			e.printStackTrace();
			fail(e.toString());
		} catch (Throwable e ) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	private DiscoveryRule createDiscoveryRuleRule() throws IntegerException {
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		subnet.setAddress(new Address("1.2.3.0", "255.255.255.0" ));
		
		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));
		
		List<Credential> credentials = new ArrayList<Credential>();
		
		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integer");
		credential.setWriteCommunity("integerrw");
		
		credentials.add(credential);
		
		credential = new SnmpV2cCredentail();
		credential.setReadCommunity("recorded/solaris-system");
		credential.setWriteCommunity("integerrw");
		credentials.add(credential);
		
		seed.setCredentials(credentials);
		
		List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
		topologySeeds.add(seed);
		DiscoveryRule rule = new DiscoveryRule();
		
		rule.setName("SubnetDiscovery: " + subnet.getAddress().getAddress() +"::" + subnet.getAddress().getMask());
		rule.setTopologySeeds(topologySeeds);
		rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
		rule.setCreated(new Date());
		
		try {
			rule = discoveryManager.updateDiscoveryRule(rule);
			assert(rule.getIdentifier() != null);
			
		} catch (IntegerException e) {
		
			e.printStackTrace();
			fail(e.toString());
		}

		return rule;
	}
	
	@Test
	public void startDiscoveryRule() {
		
		DiscoveryRule rule = null;
		try {
			rule = createDiscoveryRuleRule();
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			DiscoveryId startDiscovery = discoveryManager.startDiscovery(rule);
			DiscoveryId[] runningDiscoveries = discoveryManager.getRunningDiscoveries();
			assert (runningDiscoveries != null);
			
			boolean foundIt = false;
			for (DiscoveryId discoveryId : runningDiscoveries) {
				if (discoveryId.equals(startDiscovery))
					foundIt = true;
			}
			
			assert(foundIt == true);
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void stopDiscoveryRule() {
		DiscoveryRule rule = null;
		try {
			rule = createDiscoveryRuleRule();
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			DiscoveryId startDiscovery = discoveryManager.startDiscovery(rule);
			assert(startDiscovery != null);
			
			DiscoveryRule[] runningDiscoveries = discoveryManager.getRunningDiscoveryRules();
			
			assert (runningDiscoveries != null);
			
			boolean foundIt = false;
			for (DiscoveryRule discovery : runningDiscoveries) {
				if (discovery.getID().equals(rule.getID()))
					foundIt = true;
			}
			
			assert(foundIt == true);
			
			discoveryManager.stopDiscovery(rule);
			
			runningDiscoveries = discoveryManager.getRunningDiscoveryRules();
			
			if (runningDiscoveries != null) {
				foundIt = false;
				for (DiscoveryRule discovery : runningDiscoveries) {
					if (discovery.getID().equals(rule.getID()))
						foundIt = true;
				}
				
				assert(foundIt == false);
			}
			
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void getRunningDiscoveryRules() {
		DiscoveryRule rule = null;
		try {
			rule = createDiscoveryRuleRule();
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			discoveryManager.startDiscovery(rule);
		
			DiscoveryRule[] runningDiscoveries = discoveryManager.getRunningDiscoveryRules();
			assert(runningDiscoveries != null);
			
			for (DiscoveryRule discoveryRule : runningDiscoveries) {
				if (discoveryRule.getID().equals(rule.getID()))
					return;
			}
			
			fail("Discovery rule " + rule.getID().toDebugString() + " Not found in running disoverie rules "
					+ Arrays.toString(runningDiscoveries));
			
		} catch( Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void addGlobalCredentails() {
		List<Credential> credentials = new ArrayList<Credential>();
		
		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integer");
		credential.setWriteCommunity("integerrw");;
		
		credentials.add(credential);
		
		credential = new SnmpV2cCredentail();
		credential.setReadCommunity("recorded/solaris-system");
		credential.setWriteCommunity("integerrw");;
		credentials.add(credential);
		
		SnmpGlobalReadCredential gloablCredentails = new SnmpGlobalReadCredential();
		gloablCredentails.setCredentials(credentials);
	
		try {
			discoveryManager.updateSnmpGlobalReadCredentail(gloablCredentails);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Test
	public void getAllGlobalCredentails() {
		
		try {
			SnmpGlobalReadCredential[] credentials = discoveryManager.getAllGlobalCredentails();
			if (credentials == null || credentials.length == 0) {
				addGlobalCredentails();
				credentials = discoveryManager.getAllGlobalCredentails();
			}
			
			assert(credentials != null);
			assert(credentials.length > 0);
			
			for (SnmpGlobalReadCredential snmpGlobalReadCredential : credentials) {
				assert (snmpGlobalReadCredential.getCredentials() != null);
				assert (snmpGlobalReadCredential.getCredentials().size() > 0);
			}
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getAllIpTopologySeeds() {
		try {
			IpTopologySeed[] topologySeeds = discoveryManager.getAllIpTopologySeeds();
			if (topologySeeds == null || topologySeeds.length == 0) {
				addDiscoveryRule();
				topologySeeds = discoveryManager.getAllIpTopologySeeds();
			}
			
			assert(topologySeeds != null);
			assert(topologySeeds.length > 0);
				
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
