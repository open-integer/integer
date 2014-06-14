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
package edu.harvard.integer.service.discovery.snmp;

import org.snmp4j.smi.OID;

import edu.harvard.integer.common.discovery.SnmpLevelOID;

/**
 * 
 * The Class ParentIndexInstance is used for storing entry index information
 * along with instance oid.
 *
 * @author dchan
 */
public class ParentIndexInstance {

	/** The index position. */
	final private int indexPosition;
	
	/** The index oid. */
	final private String indexOid;
	
	final private SnmpLevelOID snmpLevel;
	

	/** The instance oid. */
	private String instanceOid;
	
	
	/**
	 * Instantiates a new parent index instance.
	 *
	 * @param indexP the index p
	 * @param indexOid the index oid
	 */
	public ParentIndexInstance( int indexP, final String indexOid, SnmpLevelOID snmpLevel ) {
		
		this.snmpLevel = snmpLevel;
		this.indexPosition = indexP;
		this.indexOid = indexOid;
	}
	
	/**
	 * Gets the instance oid.
	 *
	 * @return the instance oid
	 */
	public String getInstanceOid() {
		return instanceOid;
	}

	/**
	 * Sets the instance oid.
	 *
	 * @param instanceOid the new instance oid
	 */
	public void setInstanceOid(String instanceOid) {
		this.instanceOid = instanceOid;
	}

	/**
	 * Gets the index position.
	 *
	 * @return the index position
	 */
	public int getIndexPosition() {
		return indexPosition;
	}

	/**
	 * Gets the index oid.
	 *
	 * @return the index oid
	 */
	public String getIndexOid() {
		return indexOid;
	}

	
	/**
	 * Return the Table oid based on entry attribute oid.
	 *
	 * @return the table oid
	 */
	public String getTableOid() {
		
		OID o = new OID(indexOid);
		
		if ( o.get(o.size() - 2) == 1 ) {
			
			o.trim(2);
			return o.toString();
		}
		return null;
	}
	

	public SnmpLevelOID getChildSnmpLevel() {
		return snmpLevel;
	}

}
