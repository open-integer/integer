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

import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

/**
 * Specify common OIDs which are used very often.
 * 
 * The Class CommonSnmpOids.
 *
 * @author dchan
 */
public class CommonSnmpOids {
 
	/**
	 * System Group OIDs. They are most likely used in the first stage of
	 * discovery.
	 */
	
	/** The Constant sysDescr. */
	public static final String sysDescr = "1.3.6.1.2.1.1.1";
    
    /** The Constant sysObjectID. */
    public static final String sysObjectID = "1.3.6.1.2.1.1.2";
    
    /** The Constant sysUpTime. */
    public static final String sysUpTime = "1.3.6.1.2.1.1.3";
    
    /** The Constant sysContact. */
    public static final String sysContact = "1.3.6.1.2.1.1.4";
    
    /** The Constant sysName. */
    public static final String sysName = "1.3.6.1.2.1.1.5";
    
    /** The Constant sysLocation. */
    public static final String sysLocation = "1.3.6.1.2.1.1.6";
    
    public static final int vendorSysIdIndex = 6;
    
   
    
    /**
     * Entity Group OIDs.  They are most likely used in physical topology on a device.
     */
    
    public static final String entPhysicalDescr = "1.3.6.1.2.1.47.1.1.1.1.2";
    
    public static final String entPhysicalVendorType = "1.3.6.1.2.1.47.1.1.1.1.3";
    
    public static final String entPhysicalContainedIn = "1.3.6.1.2.1.47.1.1.1.1.4";
    
    public static final String entPhysicalClass = "1.3.6.1.2.1.47.1.1.1.1.5";
    
    public static final String entPhysicalParentRelPos = "1.3.6.1.2.1.47.1.1.1.1.6";
    
    public static final String entPhysicalName = "1.3.6.1.2.1.47.1.1.1.1.7";
    
    public static final String entPhysicalHardwareRev = "1.3.6.1.2.1.47.1.1.1.1.8";
    
    public static final String entPhysicalFirmwareRev = "1.3.6.1.2.1.47.1.1.1.1.9";
    
    public static final String entPhysicalSoftwareRev = "1.3.6.1.2.1.47.1.1.1.1.10";
    
    public static final String entPhysicalModelName = "1.3.6.1.2.1.47.1.1.1.1.13";
    
    public static final String entPhysicalSerialNum = "1.3.6.1.2.1.47.1.1.1.1.11";
     
    public static final String entAliasMappingIdentifier = "1.3.6.1.2.1.47.1.3.2.1.2";
    
    
    public static final String hrDeviceType = "1.3.6.1.2.1.25.3.2.1.2";
    
    public static final String hrDeviceDescr = "1.3.6.1.2.1.25.3.2.1.3";
    
    public static final String hrDeviceID = "1.3.6.1.2.1.25.3.2.1.4";
    
    public static final String hrDeviceStatus = "1.3.6.1.2.1.25.3.2.1.5";
    
    public static final String hrDeviceErrors = "1.3.6.1.2.1.25.3.2.1.6";
    
    public static final String hrProcessorFrwID = "1.3.6.1.2.1.25.3.3.1.1";
    
    public static final String hrProcessorLoad = "1.3.6.1.2.1.25.3.3.1.2";
    
    public static final String hrNetworkIfIndex = "1.3.6.1.2.1.25.3.4.1.1";
    
    public static final String hrDiskStorageAccess = "1.3.6.1.2.1.25.3.6.1.1";
    
    public static final String hrDiskStorageMedia = "1.3.6.1.2.1.25.3.6.1.2";
    
    public static final String  hrDiskStorageRemoveble = "1.3.6.1.2.1.25.3.6.1.3";
    
    public static final String hrPrinterStatus = "1.3.6.1.2.1.25.3.5.1.1";
    
    public static final String hrPrinterDetectedErrorState = "1.3.6.1.2.1.25.3.5.1.2";
    
    public static final String hrNetworkEntry = "1.3.6.1.2.1.25.3.4.1";
    
    public static final String hrDeviceEntry = "1.3.6.1.2.1.25.3.2.1";
    
    public static final String hrProcessorEntry = "1.3.6.1.2.1.25.3.3.1";
    
    public static final String hrDiskStorageEntry = "1.3.6.1.2.1.25.3.6.1";
    
    public static final String hrPartitionEntry = "1.3.6.1.2.1.25.3.7.1";
    
    public static final String hrSWInstalledEntry = "1.3.6.1.2.1.25.6.3.1";
    
    public static final String ifEntry = "1.3.6.1.2.1.2.2.1";
    
   
    
    /**
     * If table oid
     */
    public static final String ifDescr = "1.3.6.1.2.1.2.2.1.2";    // String
	
    public static final String ifType = "1.3.6.1.2.1.2.2.1.3";     // Integer
    
    public static String ifMtu = "1.3.6.1.2.1.2.2.1.4";  // Integer
    
    public static String ifSpeed = "1.3.6.1.2.1.2.2.1.5";  // Long
    
    public static String ifPhysAddress = "1.3.6.1.2.1.2.2.1.6";  // String
    
    
	public static VariableBinding[] sysVB = {
		
		new VariableBinding(new OID(CommonSnmpOids.sysContact)),
		new VariableBinding(new OID(CommonSnmpOids.sysDescr)),
		new VariableBinding(new OID(CommonSnmpOids.sysLocation)),
		new VariableBinding(new OID(CommonSnmpOids.sysName)),
		new VariableBinding(new OID(CommonSnmpOids.sysObjectID))
	};
    
   
}
