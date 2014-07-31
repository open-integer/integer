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

import org.junit.Test;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.snmp.CommunityAuth;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.IpDiscoverySeed;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;

/**
 * @author dchan
 */
public class SubnetRangeTest {

	
	@Test
	public void testSubnetRangeIp() {
		
		String outTestip = "192.168.0.188";
		String inTestip = "192.168.0.203";
		
		String netIp = "192.168.0.200";
		String mask = "255.255.255.248";   // Expect 8 address and usable address is 6.
		
		DiscoverNet net = new DiscoverNet(netIp, mask);
		List<Credential> creds = new ArrayList<>();
		
		IpDiscoverySeed seed = new IpDiscoverySeed(net, creds);
		
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("public");
		CommunityAuth commAuth = new CommunityAuth(snmpV2c);
		
		seed.getAuths().add(commAuth);
		
		try {
			NetworkDiscovery netDisc = null;
			DiscoverSubnetAsyncTask<ElementAccess> task = new DiscoverSubnetAsyncTask<>(netDisc, seed, false);
		    if ( task.isInRange(outTestip) ) {
		    	fail("It should be out of range " + outTestip + " with given ip " + netIp + " mask " + mask );
		    }
		    
		    if ( !task.isInRange(inTestip) ) {
		    	fail("It should be in of range " + inTestip + " with given ip " + netIp + " mask " + mask);
		    }
		} 
		catch (IntegerException e) {
			
			fail(e.toString());
		}
	}
}
