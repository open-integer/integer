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

import edu.harvard.integer.access.AccessTypeEnum;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;

import org.snmp4j.mp.SnmpConstants;

/**
 * The Class CommunityAccess contains access information for SNMPv2 and SNMPv1 access.
 *
 * @author dchan
 */
public class CommunityAuth extends SnmpAuthentication {

	
	private SnmpV2cCredentail credentail;
	
	public CommunityAuth( SnmpV2cCredentail credentail ) {
		this.credentail = credentail;
	}
	
	
	public SnmpV2cCredentail getCredentail() {
		return credentail;
	}


	/** Check if it is a V2c version or not.  If not, it is v1. */
	private boolean isVersionV2c;

	
	/**
	 * Checks if is version v2c.
	 *
	 * @return true, if is version v2c
	 */
	public boolean isVersionV2c() {
		return isVersionV2c;
	}
	
	
	/**
	 * Sets SNMP v1 or v2c version indication.
	 *
	 * @param isVersionV2c 
	 */
	public void setVersionV2c(boolean isVersionV2c) {
		this.isVersionV2c = isVersionV2c;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.Access#getAccessType()
	 */
	@Override
	public AccessTypeEnum getAccessType() {
		
		if ( isVersionV2c ) {
			return AccessTypeEnum.SNMPv2c;
		}
		return AccessTypeEnum.SNMPv1;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.access.snmp.SnmpAuthentication#getSnmpVersion()
	 */
	@Override
	public int getSnmpVersion() {
		
		if ( isVersionV2c ) {
			return SnmpConstants.version2c;
		}
		return SnmpConstants.version1;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.access.Authentication#isSame(edu.harvard.integer.access.Authentication)
	 */
	@Override
	public boolean isSame(Authentication auth) {
		
		if ( auth instanceof CommunityAuth ) {
		
			CommunityAuth cauth = (CommunityAuth) auth;
			
			if ( getCommunity(true).equals(cauth.getCommunity(true)) &&
					getCommunity(false).equals(cauth.getCommunity(false)) )
			{
				return true;
			}					
			
		}
		return false;
	}
	

	public String getCommunity( boolean isRead ) {
		
		String community = null;
		if ( isRead ) {			
			community = credentail.getReadCommunity();
		}
		else {
			community = credentail.getWriteCommunity();
		}
		
		return community != null ? community : "public";
	}
	
}

