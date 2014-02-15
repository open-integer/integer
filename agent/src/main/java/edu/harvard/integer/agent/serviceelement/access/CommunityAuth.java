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

import edu.harvard.integer.agent.serviceelement.AccessTypeEnum;
import edu.harvard.integer.agent.serviceelement.Authentication;

/**
 * The Class CommunityAccess contains access information for SNMPv2 and SNMPv1 access.
 *
 * @author dchan
 */
public class CommunityAuth implements Authentication {

	/** The community string. */
	private String community;
	
	/** Check if it is a V2c version or not.  If not, it is v1. */
	private boolean isVersionV2c;
	
	/** If it is true, it is a read community string.  Else it is write community string. */
	private boolean isRead;
	
	/**
	 * Gets the community string.
	 *
	 * @return the community
	 */
	public String getCommunity() {
		return community;
	}
	
	/**
	 * Sets the community string.
	 *
	 * @param community the new community
	 */
	public void setCommunity(String community) {
		this.community = community;
	}
	
	/**
	 *
	 * @return true, if is read community string, else it is write community string.
	 */
	public boolean isRead() {
		
		return isRead;
	}
	
	/**
	 * Sets if it is a read community string.
	 *
	 * @param isRead -- Indication if it is a read or write community string.
	 */
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
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
}
