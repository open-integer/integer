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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.snmp4j.smi.OID;

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;


/**
 * The Class EntityInfo defines a singleton static object for Physical Entity MIB for
 *
 * @author dchan
 */
public class EntitySNMPInfo {
	
	/** The entity info. */
	private static volatile EntitySNMPInfo entityInfo;
	
	/** The column oids. */
	private OID[] columnOids;
	
	private OID entAliasMappingIdentifier = new OID(CommonSnmpOids.entAliasMappingIdentifier);


	/** The entity columns. */
	private List<SNMP> entityColumns;

	/**
	 * Gets the entity info instance.
	 *
	 * @return the entity info instance
	 * @throws IntegerException the integer exception
	 */
	public static EntitySNMPInfo getEntityInfoInstance() throws IntegerException {

		if (entityInfo == null) {
			synchronized (EntitySNMPInfo.class) {
				if (entityInfo == null) {
					entityInfo = new EntitySNMPInfo();
				}
			}
		}		
		return entityInfo;
	}

	/**
	 * Instantiates a new entity info.
	 *
	 * @throws IntegerException the integer exception
	 */
	private EntitySNMPInfo() throws IntegerException {

		ServiceElementDiscoveryManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		entityColumns = manager.getEntityMIBInfo();
		
		columnOids = new OID[entityColumns.size()];
		for ( int i=0; i<entityColumns.size(); i++ ) {
			
			SNMP snmp = (SNMP) entityColumns.get(i);
			columnOids[i] = new OID(snmp.getOid());
		}		
	}

	/**
	 * Gets the entity columns.
	 *
	 * @return the entity columns
	 */
	public List<SNMP> getEntityColumns() {
		return entityColumns;
	}
	

	public OID[] getColumnOids() {
		return columnOids;
	}
	


	public OID getEntAliasMappingIdentifier() {
		return entAliasMappingIdentifier;
	}
	
	
    public List<SNMP> getEntityMIBInfo() {
			
		List<SNMP> snmps = new ArrayList<>();
		
		SNMP snmp = new SNMP();		
		snmp.setOid(CommonSnmpOids.entPhysicalClass);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalContainedIn);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalDescr);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalFirmwareRev);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalHardwareRev);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalModelName);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalName);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalParentRelPos);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalSerialNum);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalSoftwareRev);
		snmps.add(snmp);
		
		snmp = new SNMP();
		snmp.setOid(CommonSnmpOids.entPhysicalVendorType);
		snmps.add(snmp);
		

		return snmps;
	}


}
