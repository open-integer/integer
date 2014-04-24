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


import javax.inject.Inject;

import org.junit.Test;
import org.snmp4j.smi.OID;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.access.snmp.CommunityAuth;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.IpDiscoverySeed;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.snmp.EntityMibServiceElementDiscovery;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * @author dchan
 *
 */
public class EntityMibTest {
	
	@Test
	public void retrieveTable() {
		
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("integerrw");
		snmpV2c.setWriteCommunity("integerrw");
		
		OID[] eOid = new OID[5];
		eOid[0] = new OID(CommonSnmpOids.entPhysicalClass);
		eOid[1] = new OID(CommonSnmpOids.entPhysicalContainedIn);
		eOid[2] = new OID(CommonSnmpOids.entPhysicalDescr);
		eOid[3] = new OID(CommonSnmpOids.entPhysicalModelName);
		eOid[4] = new OID(CommonSnmpOids.entPhysicalVendorType);
		
		CommunityAuth ca = new CommunityAuth(snmpV2c);
		ElementEndPoint ePoint = new ElementEndPoint( "10.240.127.121", 161, ca);
		
		try {
			SnmpService.instance().getTablePdu(ePoint, eOid);
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Done with snmp table get ........ ");
	}
	
	
	@Test
	public void contaimentTest() {
		
		EntityMibServiceElementDiscovery entityDisc = new EntityMibServiceElementDiscovery();
		SnmpV2cCredentail snmpV2c = new SnmpV2cCredentail();
		snmpV2c.setReadCommunity("integerrw");
		snmpV2c.setWriteCommunity("integerrw");
		CommunityAuth ca = new CommunityAuth(snmpV2c);
		
		DiscoverNode discNode = new DiscoverNode("10.240.127.121");
		Access ac = new Access(161, ca);
		
		discNode.setAccessElement(new ServiceElement());
		
		discNode.setAccess(ac);;
		
		try {
			entityDisc.discover(null, discNode);
		} 
		catch (IntegerException e) {
			
			e.printStackTrace();
		}
	}
	
}
