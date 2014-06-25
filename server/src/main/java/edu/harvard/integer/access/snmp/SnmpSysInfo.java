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

import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

/**
 * The Class SnmpSysInfo contains the content of a SNMP system group information.
 *
 * @author dchan
 */
public class SnmpSysInfo {

	/** The system description. */
	private String sysDescr;
	
	/** The system object id. */
	private String sysObjectID;
	
	/** The system contact. */
	private String sysContact;
	
	/** The system name. */
	private String sysName;
	
	/** The system location. */
	private String sysLocation;
	
	/** The pdu which contains System group OID. */
	private PDU pdu;
	

	/**
	 * Instantiates a new snmp sys info.
	 *
	 * @param sysPdu the sys pdu
	 */
	public SnmpSysInfo( PDU sysPdu ) {
		
		for ( int i=0; i<sysPdu.size(); i++ ) {
			
			VariableBinding vb = sysPdu.get(i);
			if ( vb.getOid().toString().indexOf(CommonSnmpOids.sysContact) >= 0 ) {
				sysContact = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().indexOf(CommonSnmpOids.sysObjectID) >= 0) {
				sysObjectID = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().indexOf(CommonSnmpOids.sysLocation) >= 0 ) {
				sysLocation = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().indexOf(CommonSnmpOids.sysName) >= 0 ) {
				sysName = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().indexOf(CommonSnmpOids.sysDescr) >= 0 ) {
				sysDescr = vb.getVariable().toString();
			}
		}		
		this.pdu = sysPdu;
	}
	
	/**
	 * Gets the sys descr.
	 *
	 * @return the sys descr
	 */
	public String getSysDescr() {
		return sysDescr;
	}
	
	/**
	 * Gets the sys object id.
	 *
	 * @return the sys object id
	 */
	public String getSysObjectID() {
		return sysObjectID;
	}
	
	/**
	 * Gets the sys contact.
	 *
	 * @return the sys contact
	 */
	public String getSysContact() {
		return sysContact;
	}
	
	/**
	 * Gets the sys name.
	 *
	 * @return the sys name
	 */
	public String getSysName() {
		return sysName;
	}
	
	/**
	 * Gets the sys location.
	 *
	 * @return the sys location
	 */
	public String getSysLocation() {
		return sysLocation;
	}
	
	
	/**
	 * Gets the pdu.
	 *
	 * @return the pdu
	 */
	public PDU getPdu() {
		return pdu;
	}
}
