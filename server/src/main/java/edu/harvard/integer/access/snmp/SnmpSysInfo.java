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
 * @author dchan
 *
 */
public class SnmpSysInfo {

	private String sysDescr;
	private String sysObjectID;
	private String sysContact;
	private String sysName;
	private String sysLocation;
	
	private PDU pdu;
	

	public SnmpSysInfo( PDU sysPdu ) {
		
		for ( int i=0; i<sysPdu.size(); i++ ) {
			VariableBinding vb = sysPdu.get(i);
			if ( vb.getOid().toString().equals(CommonSnmpOids.sysContact) ) {
				sysContact = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().equals(CommonSnmpOids.sysObjectID) ) {
				sysObjectID = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().equals(CommonSnmpOids.sysLocation) ) {
				sysLocation = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().equals(CommonSnmpOids.sysName) ) {
				sysName = vb.getVariable().toString();
			}
			else if ( vb.getOid().toString().equals(CommonSnmpOids.sysDescr) ) {
				sysDescr = vb.getVariable().toString();
			}
		}		
		this.pdu = sysPdu;
	}
	
	public String getSysDescr() {
		return sysDescr;
	}
	public String getSysObjectID() {
		return sysObjectID;
	}
	public String getSysContact() {
		return sysContact;
	}
	public String getSysName() {
		return sysName;
	}
	public String getSysLocation() {
		return sysLocation;
	}
	
	
	public PDU getPdu() {
		return pdu;
	}
}
