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
package edu.harvard.integer.access.snmp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.SecureTarget;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.exception.CommonErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.type.displayable.NonLocaleErrorMessage;
import edu.harvard.integer.common.util.DisplayableInterface;



/**
 * The Class SnmpCollectionUtil provides some static methods used for SNMP collections.
 *
 * @author dchan
 */
public class SnmpCollectionUtil {
 
	/**
	 * Creates the SNMP target used for SNMP4j.
	 *
	 * @param endPoint the end point
	 * @return the abstract target
	 */
	public static AbstractTarget createTarget( ElementEndPoint endPoint, boolean isRead ) {
		
		if ( endPoint.getAuth() instanceof CommunityAuth ) {
			
			CommunityTarget ct = createCommunityTarget((CommunityAuth) endPoint.getAuth(), 
                                         endPoint.getIpAddress(), endPoint.getAccessPort(), isRead );
			ct.setTimeout(((SnmpAuthentication)endPoint.getAuth()).getTimeOut());
			ct.setRetries(((SnmpAuthentication)endPoint.getAuth()).getTryCount());
			
			return ct;
		}
		else if ( endPoint.getAuth() instanceof SnmpSecureAuth ) {
			
			SecureTarget st = createSecureTarget((SnmpSecureAuth) endPoint.getAuth(), endPoint.getIpAddress(), endPoint.getAccessPort());
			st.setTimeout(((SnmpAuthentication)endPoint.getAuth()).getTimeOut());
			st.setRetries(((SnmpAuthentication)endPoint.getAuth()).getTryCount());
			
			return st;
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
	public static CommunityTarget createCommunityTarget( CommunityAuth access, 
			                                             String ip, 
			                                             int port,
			                                             boolean isRead ) {
		
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(access.getCommunity(isRead)));
	
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
	public static SecureTarget createSecureTarget( SnmpSecureAuth access, String ip, int port ) 
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
	
	
    /**
 	 * <p>Community ping which include SNMP ping and ICMP ping.</p>
 	 * <p>setSnmpVersion determines and sets the highest SNMP version supported by 
     * the SNMP end point. The SysObjectID MIB entry is used to test the different 
     * SNMP versions.</p>
 	 * 
 	 *
 	 * @param snmpEndPt ElementEndPoint for reaching the device
 	 * @return SysOid
 	 * @throws IntegerException the integer exception
 	 */
 	public static PDU snmpPing( ElementEndPoint snmpEndPt ) 
    	                                   throws IntegerException
    {
    	/*
    	 * Try a single SNMP read to check if SNMP access is available.
    	 * If not, or if the endPoint's SNMP version is not set, then
    	 * call set the snmpAccessAvailable flag to false, which will cause
    	 * the setSnmpVersion method to determine and set the SNMP version.
    	 */
    	/*
    	 * Try to verify SNMP access via a single PDU read.
    	 */
    	PDU sysPdu = null;
    	PDU pdu = new PDU();
    	pdu.addAll(CommonSnmpOids.sysVB);
    	
    	try 
    	{
    		sysPdu = SnmpService.instance().getPdu(snmpEndPt, pdu);
    	}
    	catch (IntegerException e) 
    	{
    		NetworkErrorCodes ecode = (NetworkErrorCodes) e.getErrorCode();
    		
    		if ( ecode == NetworkErrorCodes.CannotReach ) {
    			
    			try {
					if ( !InetAddress.getByName(snmpEndPt.getIpAddress()).isReachable(5000) )
					{
						throw new IntegerException( null, NetworkErrorCodes.CannotReach, 
									new DisplayableInterface[] { new NonLocaleErrorMessage("Can not ping on " + snmpEndPt.getIpAddress()) });
					}
					else {
						
						if ( snmpEndPt.getAuth() instanceof CommunityAuth ) {
							/*
					    	 * Try the passing in SNMP version first.  If it is not matched agent version
					    	 * try another one.
					    	 */
					    	CommunityAuth auth = (CommunityAuth) snmpEndPt.getAuth();  
					    	auth.setVersionV2c(!auth.isVersionV2c());
						    sysPdu = SnmpService.instance().getPdu(snmpEndPt, pdu);
						}						
						throw e;
					}
				} 
    			catch (UnknownHostException e1  ) {
    				throw new IntegerException(e1, CommonErrorCodes.IOError);
				} catch (IOException e1) {
					throw new IntegerException(e1, CommonErrorCodes.IOError);
				}
    		}
    		else {
    			throw e;
    		}
    		
    	};
    	return sysPdu;
    	
    }
    
}
