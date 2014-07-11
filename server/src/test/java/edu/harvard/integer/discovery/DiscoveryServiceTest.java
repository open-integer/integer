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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.DiscoveryServiceInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class DiscoveryServiceTest {
	

	@Inject
	private DiscoveryServiceInterface discoverService;
	
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("DiscoveryServiceTest.war");
	}
	
	@Test
	public void startDiscovery() {
		
		System.out.println("Start discovery test.");
		
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		Address address = new Address();
		address.setAddress("10.240.127.0");
		subnet.setAddress(new Address( "10.240.127.0", "255.255.255.0" ));
		
		
		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));
		
		List<Credential> credentials = new ArrayList<Credential>();
		
		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integerrw");
		credential.setWriteCommunity("integerrw");;
		
		credentials.add(credential);
		seed.setCredentials(credentials);
		
		List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
		topologySeeds.add(seed);
		DiscoveryRule rule = new DiscoveryRule();
		
		rule.setTopologySeeds(topologySeeds);
		rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
		rule.setCreated(new Date());
		
		try {
			discoverService.startDiscovery(rule);
		} 
		catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void startAndStopDiscovery() throws InterruptedException {
		
		System.out.println("Start and stop discovery test.");
		
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		Address address = new Address();
		address.setAddress("10.240.127.0");
		subnet.setAddress(new Address( "10.240.127.0", "255.255.255.0" ));
		
		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));
		
		List<Credential> credentials = new ArrayList<Credential>();
		
		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integerrw");
		credential.setWriteCommunity("integerrw");;
		
		credentials.add(credential);
		seed.setCredentials(credentials);
		
		List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
		topologySeeds.add(seed);
		DiscoveryRule rule = new DiscoveryRule();
		
		rule.setTopologySeeds(topologySeeds);
		rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
		rule.setCreated(new Date());
		
		try {
			DiscoveryId did =  discoverService.startDiscovery(rule);
			discoverService.stopDiscovery(did);
			TimeUnit.MILLISECONDS.sleep(2000);
			
		} 
		catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}


}
