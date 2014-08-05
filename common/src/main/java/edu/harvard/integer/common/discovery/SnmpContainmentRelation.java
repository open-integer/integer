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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;

/**
 * 
 * SnmpContainmentRelation definition to map the ServiceElement from this level to a different
 * ServiceElement. Ex. entAliasMappingTable. Maps the logical components and
 * physical components to and extrernal to the entity MIB object.
 * entAliasMappingIdentifier.33.0 = ifIndex.6
 * 
 * 
 * @author David Taylor
 * 
 */
@Entity
public class SnmpContainmentRelation extends SnmpRelationship {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private SnmpLevelOID childTable = null;

	@ManyToOne
	private SNMPTable mappingTable = null;

	@ManyToOne
	private SNMP mappingOid = null;

	private Boolean valueIsParent = false;


	/**
	 * @return the childTable
	 */
	public SnmpLevelOID getChildTable() {
		return childTable;
	}

	/**
	 * @param childTable
	 *            the childTable to set
	 */
	public void setChildTable(SnmpLevelOID childTable) {
		this.childTable = childTable;
	}

	/**
	 * @return the mappingTable
	 */
	public SNMPTable getMappingTable() {
		return mappingTable;
	}

	/**
	 * @param mappingTable
	 *            the mappingTable to set
	 */
	public void setMappingTable(SNMPTable mappingTable) {
		this.mappingTable = mappingTable;
	}

	/**
	 * @return the mappingOid
	 */
	public SNMP getMappingOid() {
		return mappingOid;
	}

	/**
	 * @param mappingOid
	 *            the mappingOid to set
	 */
	public void setMappingOid(SNMP mappingOid) {
		this.mappingOid = mappingOid;
	}
	


	public Boolean getValueIsParent() {
		return valueIsParent;
	}

	public void setValueIsParent(Boolean valueIsParent) {
		this.valueIsParent = valueIsParent;
	}

}
