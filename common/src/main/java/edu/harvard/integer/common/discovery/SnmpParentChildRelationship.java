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

package edu.harvard.integer.common.discovery;

import edu.harvard.integer.common.snmp.SNMP;

/**
 * This class holds the definition of a parent child relationship. This is used
 * to map the child back to the parent.
 * 
 * @author David Taylor
 * 
 */
public class SnmpParentChildRelationship extends SnmpRelationship {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the OID to use match parent instance OID.
	 */
	private SNMP containmentOid = null;

	private SNMP siblingOid = null;

	/**
	 * @return the containmentOid
	 */
	public SNMP getContainmentOid() {
		return containmentOid;
	}

	/**
	 * @param containmentOid
	 *            the containmentOid to set
	 */
	public void setContainmentOid(SNMP containmentOid) {
		this.containmentOid = containmentOid;
	}

	/**
	 * @return the siblingOid
	 */
	public SNMP getSiblingOid() {
		return siblingOid;
	}

	/**
	 * @param siblingOid
	 *            the siblingOid to set
	 */
	public void setSiblingOid(SNMP siblingOid) {
		this.siblingOid = siblingOid;
	}

}