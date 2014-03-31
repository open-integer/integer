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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import edu.harvard.integer.common.discovery.VendorDiscoveryTemplate;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.discovery.DiscoveryService;
import edu.harvard.integer.service.discovery.DiscoveryServiceInterface;
import edu.harvard.integer.service.discovery.IntegerInterface;
import edu.harvard.integer.service.discovery.IpDiscoverySeed;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.PollResult;
import edu.harvard.integer.service.discovery.TopoNetwork;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;

/**
 * @author dchan
 *
 */
public class DiscoverAuthOrderTest implements IntegerInterface, ElementDiscoverCB<ServiceElement> {

	
	private NetworkDiscovery netDisc;
	
	@Inject
	private DiscoveryServiceInterface discoverIf;
	
	
	@Before
	public void setUp() {
		
		List<IpDiscoverySeed> discoverSeed = new ArrayList<>();
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("public");
		
		String netIp = "10.251.41.172";
		String mask = "255.255.255.248";   // Expect 8 address and usable address is 6.
		
		DiscoverNet dNet = new DiscoverNet(netIp, mask);
		List<Credential> creds = new ArrayList<>();
		creds.add(snmpV2c);
		
		IpDiscoverySeed seed = new IpDiscoverySeed(dNet, creds);
		discoverSeed.add(seed);
		
		if ( discoverIf == null )  {
			
			discoverIf = new DiscoveryService();
		}
		netDisc = new NetworkDiscovery(discoverSeed, this, discoverIf);		
	}
	
	
	@Test
	public void testAuthOrder() {
		
		netDisc.discoverNetwork();
	}
	
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoverCB#discoveredTopoNet(edu.harvard.integer.service.discovery.TopoNetwork)
	 */
	@Override
	public void discoveredTopoNet(TopoNetwork tb) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoverCB#discoveredElement(edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public void discoveredElement(ServiceElement elm) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoverCB#progressNotification(java.lang.String)
	 */
	@Override
	public void progressNotification(String msg) {
		
		System.out.println("In progress "  + msg );
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoverCB#errorOccur(edu.harvard.integer.common.exception.NetworkErrorCodes, java.lang.String)
	 */
	@Override
	public void errorOccur(NetworkErrorCodes errorCode, String msg) {
		
		// TODO Auto-generated method stub	
		
		System.out.println("Error " + errorCode + " error message " + msg );
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoverCB#doneDiscover()
	 */
	@Override
	public void doneDiscover() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.IntegerInterface#getTopLevelPolls()
	 */
	@Override
	public List<ServiceElementManagementObject> getTopLevelPolls() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.IntegerInterface#getDiscoveryTemplate(java.util.List)
	 */
	@Override
	public VendorDiscoveryTemplate<ServiceElementManagementObject> getDiscoveryTemplate(
			List<PollResult> pollResult) {
		// TODO Auto-generated method stub
		return null;
	}
}
