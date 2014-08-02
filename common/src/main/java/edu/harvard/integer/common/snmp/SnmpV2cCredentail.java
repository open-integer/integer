/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.common.snmp;

import java.io.Serializable;

import javax.persistence.Entity;

import edu.harvard.integer.common.topology.Credential;

/**
 * Holder for SNMP V2 credentials (community strings) needed to talk to an SNMP
 * device.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class SnmpV2cCredentail extends Credential implements Serializable {
	/**
	 * Serialization version
	 */
	private static final long serialVersionUID = 1L;

	private String readCommunity = null;
	private String writeCommunity = null;

	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.common.BaseEntity#toString()
	 */
	@Override
	public String toString() {
		
		return readCommunity;
	}

	/**
	 * @return the readCommunity
	 */
	public String getReadCommunity() {
		return readCommunity;
	}

	/**
	 * @param readCommunity
	 *            the readCommunity to set
	 */
	public void setReadCommunity(String readCommunity) {
		this.readCommunity = readCommunity;
	}

	/**
	 * @return the writeCommunity
	 */
	public String getWriteCommunity() {
		return writeCommunity;
	}

	/**
	 * @param writeCommunity
	 *            the writeCommunity to set
	 */
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}

}
