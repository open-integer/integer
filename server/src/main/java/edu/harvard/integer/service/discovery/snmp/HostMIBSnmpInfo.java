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

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;

/**
 * @author dchan
 *
 */
public class HostMIBSnmpInfo {

	private static volatile HostMIBSnmpInfo hostSnmpInfo;
	
	private SNMPTable deviceTbl;
	private SNMPTable processorTbl;
	private SNMPTable networkTbl;
	private SNMPTable diskTbl;
	private SNMPTable swInstalledTbl;
	private SNMPTable ifTbl;


	/**
	 * Gets the entity info instance.
	 *
	 * @return the entity info instance
	 * @throws IntegerException the integer exception
	 */
	public static HostMIBSnmpInfo getInstance() throws IntegerException {

		if (hostSnmpInfo == null) {
			synchronized (HostMIBSnmpInfo.class) {
				if (hostSnmpInfo == null) {
					hostSnmpInfo = new HostMIBSnmpInfo();
				}
			}
		}		
		return hostSnmpInfo;
	}
	
	
	private HostMIBSnmpInfo() throws IntegerException {
		
		SnmpManagerInterface snmpMgr =  DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		deviceTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceEntry);
		processorTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrProcessorEntry);
		networkTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrNetworkEntry);
		diskTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrDiskStorageEntry);
		swInstalledTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledEntry);
		
	}
	
	
	public SNMPTable getDeviceTbl() {
		return deviceTbl;
	}


	public SNMPTable getProcessorTbl() {
		return processorTbl;
	}


	public SNMPTable getNetworkTbl() {
		return networkTbl;
	}


	public SNMPTable getDiskTbl() {
		return diskTbl;
	}


	public SNMPTable getSwInstalledTbl() {
		return swInstalledTbl;
	}


	public SNMPAliasMapping getIfMapping() {
		
		SNMPAliasMapping mapping = new SNMPAliasMapping(deviceTbl, ifTbl, networkTbl);
		return mapping;
	}
}
