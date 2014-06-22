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
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.common.discovery.SnmpParentChildRelationship;

/**
 * @author dchan
 *
 */
public class ParentChildRelationNode {

	private String containmentValue;
	private int siblingValue;
	private String subTypeValue;
	private String categoryValue;	
	private String index;
	
	private String contextOid;
	

	/**
	 * 
	 * @param et
	 * @param relation
	 */
	public ParentChildRelationNode( TableEvent et, 
			                        SnmpParentChildRelationship relation,
			                        String contexOid ) {
		
		this.contextOid = contexOid;
		VariableBinding[] vbs =  et.getColumns();
    	for ( VariableBinding vb : vbs ) {
    		
    		if ( vb.getOid().startsWith(new OID(relation.getContainmentOid().getOid()) ) ) {
    			
    			containmentValue = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(relation.getSiblingOid().getOid()) ) ) {
    			
    			siblingValue = vb.getVariable().toInt();
    		}
    		else if ( vb.getOid().startsWith(new OID(relation.getSubTypeOid().getOid()) ) ) {
    			
    			subTypeValue = vb.getVariable().toString();
    		}
    		else if ( relation.getCategoryOid() != null && vb.getOid().startsWith(new OID(relation.getCategoryOid().getOid()) ) ) {
    			categoryValue = vb.getVariable().toString();
    		}    		
    	}
    	index = et.getIndex().toString();
	}
	

	
	public String getContainmentValue() {
		return containmentValue;
	}



	public int getSiblingValue() {
		return siblingValue;
	}



	public String getSubTypeValue() {
		return subTypeValue;
	}



	public String getCategoryValue() {
		return categoryValue;
	}



	public String getIndex() {
		return index;
	}
	


	public String getContextOid() {
		return contextOid;
	}




}
