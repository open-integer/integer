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

import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.snmp.CommonSnmpOids;

/**
 * @author dchan
 *
 */
public class PhysEntityRow {

	private String entPhysicalDescr;
	private String entPhysicalVendorType;
	private int entPhysicalContainedIn;
    private EntityClassEnum entityClass;
    private int entPhysicalParentRelPos;
    private String entPhysicalName;
    private String entPhysicalHardwareRev;
    private String entPhysicalFirmwareRev;
    private String entPhysicalSoftwareRev;
    private String entPhysicalSerialNum;
 //   private String entPhysicalMfgName;
    private String entPhysicalModelName;
//    private String entPhysicalAlias;
    private boolean entPhysicalIsFRU;
    
    private String index;
    

	public PhysEntityRow( TableEvent et ) {
    	
    	VariableBinding[] vbs =  et.getColumns();
    	for ( VariableBinding vb : vbs ) {
    		
    		if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalClass) ) ) {
    			entityClass = EntityClassEnum.valueOf(vb.getVariable().toInt());
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalContainedIn))) {
    			entPhysicalContainedIn = vb.getVariable().toInt();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalDescr)) ) {
    			entPhysicalDescr = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalFirmwareRev)) ) {
    			entPhysicalFirmwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalHardwareRev)) ) {
    			entPhysicalHardwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalModelName)) ) {
    			entPhysicalModelName = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalName)) ) {
    			entPhysicalName = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalParentRelPos)) ) {
    			entPhysicalParentRelPos = vb.getVariable().toInt();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalSoftwareRev)) ) {
    			entPhysicalSoftwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalVendorType)) ) {
    			entPhysicalVendorType = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalSerialNum)) ) {
    			entPhysicalSerialNum = vb.getVariable().toString();
    		}
    	}
    	index = et.getIndex().toString();
    	
    }
	
	
   public PhysEntityRow( PDU pdu, String index ) {
    	
    	VariableBinding[] vbs =  pdu.toArray();
    	for ( VariableBinding vb : vbs ) {
    		
    		if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalClass) ) ) {
    			entityClass = EntityClassEnum.valueOf(vb.getVariable().toInt());
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalContainedIn))) {
    			entPhysicalContainedIn = vb.getVariable().toInt();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalDescr)) ) {
    			entPhysicalDescr = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalFirmwareRev)) ) {
    			entPhysicalFirmwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalHardwareRev)) ) {
    			entPhysicalHardwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalModelName)) ) {
    			entPhysicalModelName = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalName)) ) {
    			entPhysicalName = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalParentRelPos)) ) {
    			entPhysicalParentRelPos = vb.getVariable().toInt();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalSoftwareRev)) ) {
    			entPhysicalSoftwareRev = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalVendorType)) ) {
    			entPhysicalVendorType = vb.getVariable().toString();
    		}
    		else if ( vb.getOid().startsWith(new OID(CommonSnmpOids.entPhysicalSerialNum)) ) {
    			entPhysicalSerialNum = vb.getVariable().toString();
    		}
    	}
    	this.index = index;
    	
    }
	 
	
	/**
	 * Return value on a given oid.
	 * 
	 * @param oid
	 * @return
	 */
	public Object getValueByOid( String oid ) {
		
		if ( oid.indexOf( CommonSnmpOids.entPhysicalClass) >= 0 ) {
			 return Integer.valueOf(entityClass.getValue());
		}
		else if ( oid.indexOf(CommonSnmpOids.entPhysicalContainedIn) >= 0) {
			return Integer.valueOf(entPhysicalContainedIn);
		}
		else if ( oid.indexOf(CommonSnmpOids.entPhysicalDescr) >= 0 ) {
			return entPhysicalDescr;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalFirmwareRev) >= 0) {
			return entPhysicalFirmwareRev;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalHardwareRev) >= 0 ) {
			return entPhysicalHardwareRev;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalModelName) >= 0 ) {
			return entPhysicalModelName;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalName) >= 0 ) {
			return entPhysicalName;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalParentRelPos) >= 0 ) {
			return entPhysicalParentRelPos;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalSoftwareRev) >= 0 ) {
			return entPhysicalSoftwareRev;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalVendorType) >= 0 ) {
			return entPhysicalVendorType;
		}
		else if ( oid.indexOf( CommonSnmpOids.entPhysicalSerialNum) >= 0 ) {
			return entPhysicalSerialNum;
		}
		return null;
	}
	
    
    
	public String getEntPhysicalDescr() {
		return entPhysicalDescr;
	}
	public String getEntPhysicalVendorType() {
		return entPhysicalVendorType;
	}
	public int getEntPhysicalContainedIn() {
		return entPhysicalContainedIn;
	}
	public EntityClassEnum getEntityClass() {
		return entityClass;
	}
	public int getEntPhysicalParentRelPos() {
		return entPhysicalParentRelPos;
	}
	public String getEntPhysicalName() {
		return entPhysicalName;
	}
	public String getEntPhysicalHardwareRev() {
		return entPhysicalHardwareRev;
	}
	public String getEntPhysicalFirmwareRev() {
		return entPhysicalFirmwareRev;
	}
	public String getEntPhysicalSoftwareRev() {
		return entPhysicalSoftwareRev;
	}
	public String getEntPhysicalSerialNum() {
		return entPhysicalSerialNum;
	}
//	public String getEntPhysicalMfgName() {
//		return entPhysicalMfgName;
//	}
	public String getEntPhysicalModelName() {
		return entPhysicalModelName;
	}
//	public String getEntPhysicalAlias() {
//		return entPhysicalAlias;
//	}
	public boolean isEntPhysicalIsFRU() {
		return entPhysicalIsFRU;
	}
    
    
    public String getIndex() {
		return index;
	}

    
}
