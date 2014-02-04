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
package edu.harvard.integer.agent.serviceelement.access;

import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.SecureTarget;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

import edu.harvard.integer.agent.serviceelement.ElementEndPoint;


/**
 * The Class SnmpCollectionUtil provides some static methods used for SNMP collections.
 *
 * @author dchan
 */
public class SnmpCollectionUtil {
 
	/**
	 * Creates the SNMP target used for SNMP4j
	 *
	 * @param endPoint the end point
	 * @return the abstract target
	 */
	public static AbstractTarget createTarget( ElementEndPoint endPoint ) {
		
		if ( endPoint.getAccess() instanceof CommunityAccess ) {
			return createCommunityTarget((CommunityAccess) endPoint.getAccess(), endPoint.getIpAddress(), endPoint.getAccessPort());
		}
		else if ( endPoint.getAccess() instanceof SnmpSecureAccess ) {
			return createSecureTarget((SnmpSecureAccess) endPoint.getAccess(), endPoint.getIpAddress(), endPoint.getAccessPort());
		}
		return null;
	}
	
	/** 
	 * Creates the community target.
	 *
	 * @param access the access object containing community string information.
	 * @param ip the ip of the element.
	 * @param port the port is the managment port of the element.
	 * @return the SNMP4j community target
	 */
	public static CommunityTarget createCommunityTarget( CommunityAccess access, String ip, int port ) {
		
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(access.getCommunity()));
	
		Address targetAddress = GenericAddress.parse("udp:" + ip + "/" + port);
        
		if ( access.isVersionV2c() ) {
			target.setVersion(SnmpConstants.version2c);
		}
		else {
			target.setVersion(SnmpConstants.version1);
		}	
		
		target.setAddress(targetAddress);
		return target;
	}
	
	/**
	 * Creates the secure target for SNMPv3 access.
	 *
	 * @param access the access containing SNMPv3 secure information.
	 * @param ip the ip of the element
	 * @param port the managerment port of the element.
	 * @return the secure target
	 */
	public static SecureTarget createSecureTarget( SnmpSecureAccess access, String ip, int port ) 
	{
		UserTarget ut = new UserTarget();
		Address targetAddress = GenericAddress.parse("udp:" + ip + "/" + port);
		ut.setAddress(targetAddress);
		ut.setVersion(SnmpConstants.version3);
		
		ut.setSecurityLevel(access.getSecurityLevel());
		ut.setSecurityModel(access.getSecurityModel());
		ut.setSecurityName(new OctetString(access.getSecurityName()));
		return ut;
	}
}
