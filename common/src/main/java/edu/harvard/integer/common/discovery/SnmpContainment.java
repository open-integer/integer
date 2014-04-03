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

import java.util.List;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 *         The SnmpContainment object tells the system the SNMP model to follow
 *         to discover the service elements that are contained within an
 *         instance of the device identified by the
 *         SnmpVendorContainmentSelector that pointed to the SnmpContainment
 *         instance.
 * 
 *         The reason for having many of these (potentially) for an
 *         SnmpContainment instance is that some vendors may have discontinuous
 *         tables that represent the containment hierarchy.
 * 
 */
public class SnmpContainment extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The containment type defines how the discovery program will use the data
	 * in instances of this class. If the containment type is Entity MIB, then
	 * it uses the OID structure of the Entitiy MIB to determine the containment
	 * of the ServiceElementTypes from the vendor(s) in the associated
	 * SnmpVendorDiscoveryTemplate
	 * 
	 * If the containment type is hierarchal then there will be a list of top
	 * level SnmpLevelOID instances in the snmpLevelOidList attribute.
	 */
	private SnmpContainmentType containmentType = null;

	private List<ID> snmpLevels = null;

	/**
	 * @return the containmentType
	 */
	public SnmpContainmentType getContainmentType() {
		return containmentType;
	}

	/**
	 * @param containmentType
	 *            the containmentType to set
	 */
	public void setContainmentType(SnmpContainmentType containmentType) {
		this.containmentType = containmentType;
	}

	/**
	 * @return the snmpLevels
	 */
	public List<ID> getSnmpLevels() {
		return snmpLevels;
	}

	/**
	 * @param snmpLevels
	 *            the snmpLevels to set
	 */
	public void setSnmpLevels(List<ID> snmpLevels) {
		this.snmpLevels = snmpLevels;
	}
}
