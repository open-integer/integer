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
package edu.harvard.integer.service.discovery.snmp.containment;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentType;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorStringValue;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;

/**
 * @author dchan
 *
 */
public class ContainmentGenerator {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(ContainmentGenerator.class);
	
	public static SnmpContainment generator( ServiceElementType serviceElementType, SnmpContainmentType type ) throws IntegerException {
		
		switch (type) {
		
		    case HostResourcesMib:
			
			   return hostMibGenerator(serviceElementType);

		    default: {
		    
		    	SnmpContainment sc = new SnmpContainment();
				sc.setContainmentType(type);
				sc.setServiceElementTypeId(serviceElementType.getID());
				sc.setName("AutoDiscoverContainment");
				
				return sc;
		    }
		}
	}
	
	
	/**
	 * 
	 * 
	 * @param serviceElmType
	 * @return
	 * @throws IntegerException
	 */
	public static SnmpContainment hostMibGenerator( ServiceElementType serviceElmType ) throws IntegerException {
		
		logger.info("in HostMibGenerator ......... ");
		
		SnmpManagerInterface snmpMgr = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		ServiceElementDiscoveryManagerInterface discMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		ManagementObjectCapabilityManagerInterface  capMgr = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
		
		SnmpContainment sc = new SnmpContainment();
		sc.setContainmentType(SnmpContainmentType.HostResourcesMib);		
		sc.setServiceElementTypeId(serviceElmType.getID());
		sc.setName("HostResourcesMIbContainment");
		
		List<SnmpLevelOID> levelOids = new ArrayList<>();
		sc.setSnmpLevels(levelOids);
		
		SnmpLevelOID levelOid = new SnmpLevelOID();
		levelOids.add(levelOid);
		
		levelOid.setName("HostMibTopLevel");
		
		SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceEntry);
		levelOid.setContextOID(snmp);
		
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceType);
		levelOid.setDescriminatorOID(snmp);
		
		List<SnmpServiceElementTypeDiscriminator>  discriminators = new ArrayList<>();
		levelOid.setDisriminators(discriminators);
				
		ServiceElementType setCpu = null;
		ServiceElementType[] sets =  discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.cpu, serviceElmType.getVendor());
		if ( sets == null || sets.length == 0 ) {
			
			setCpu = new ServiceElementType();
			setCpu.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			setCpu.setCategory(CategoryTypeEnum.cpu);
			setCpu.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			setDeviceTblComponentIdentify(setCpu, snmpMgr);
			
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceDescr);
			setCpu.setDefaultNameCababilityId(defName.getID());
			
			List<ID> attributeIds = new ArrayList<>();
			addDeviceTblAttributes(attributeIds, snmpMgr);
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrProcessorFrwID);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrProcessorLoad);
			attributeIds.add(snmp.getID());
			
			setCpu.setAttributeIds(attributeIds);			
			setCpu = capMgr.updateServiceElementType(setCpu);
		}
		else {
			
			setCpu = sets[0];
		}
		
		SnmpServiceElementTypeDiscriminator sstd = new SnmpServiceElementTypeDiscriminator();
		SnmpServiceElementTypeDiscriminatorStringValue discriminatorValue = new SnmpServiceElementTypeDiscriminatorStringValue();
		
		discriminatorValue.setValue("1.3.6.1.2.1.25.3.1.3");
		sstd.setDiscriminatorValue(discriminatorValue);
		sstd.setServiceElementTypeId(setCpu.getID());
		
		discriminators.add(sstd);
		
		ServiceElementType setPrinter = null;
		sets =  discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.printer, serviceElmType.getVendor());
		if ( sets == null || sets.length == 0 ) {
			
			setPrinter = new ServiceElementType();
			setPrinter.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			setPrinter.setCategory(CategoryTypeEnum.printer);
			setPrinter.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			setDeviceTblComponentIdentify(setPrinter, snmpMgr);
			
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceDescr);
			setPrinter.setDefaultNameCababilityId(defName.getID());
			
			List<ID> attributeIds = new ArrayList<>();
			addDeviceTblAttributes(attributeIds, snmpMgr);
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrPrinterDetectedErrorState);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrProcessorLoad);
			attributeIds.add(snmp.getID());
			
			setPrinter.setAttributeIds(attributeIds);			
			setPrinter = capMgr.updateServiceElementType(setPrinter);
		}
		else {
			setPrinter = sets[0];
		}
		
		sstd = new SnmpServiceElementTypeDiscriminator();
		discriminatorValue = new SnmpServiceElementTypeDiscriminatorStringValue();
		discriminatorValue.setValue("1.3.6.1.2.1.25.3.1.5");
		sstd.setDiscriminatorValue(discriminatorValue);
		sstd.setServiceElementTypeId(setPrinter.getID());
		
		discriminators.add(sstd);
		
		
		ServiceElementType setIf = null;
		sets =  discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.portIf, serviceElmType.getVendor());
		if ( sets == null || sets.length == 0 ) {
			
			setIf = new ServiceElementType();
			setIf.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			setIf.setCategory(CategoryTypeEnum.portIf);
			setIf.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceDescr);
			setIf.setDefaultNameCababilityId(defName.getID());
			
			List<ID> attributeIds = new ArrayList<>();
			addDeviceTblAttributes(attributeIds, snmpMgr);
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrNetworkIfIndex);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrNetworkIfIndex);
			List<ID> ids = new ArrayList<>();
			
			ids.add(snmp.getID());
			setIf.setUniqueIdentifierCapabilities(ids);
			
			setIf.setAttributeIds(attributeIds);			
			setIf = capMgr.updateServiceElementType(setIf);
		}
		else {
			setIf = sets[0];
		}
		
		sstd = new SnmpServiceElementTypeDiscriminator();
		discriminatorValue = new SnmpServiceElementTypeDiscriminatorStringValue();
		discriminatorValue.setValue("1.3.6.1.2.1.25.3.1.4");
		sstd.setDiscriminatorValue(discriminatorValue);
		sstd.setServiceElementTypeId(setIf.getID());
		discriminators.add(sstd);
		
		
		ServiceElementType diskType = null;
		sets = discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.disk, serviceElmType.getVendor());
		
		if ( sets == null || sets.length == 0 ) {
			
            diskType = new ServiceElementType();
			diskType.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			diskType.setCategory(CategoryTypeEnum.disk);
			diskType.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			setDeviceTblComponentIdentify(diskType, snmpMgr);
			
			List<ID> attributeIds = new ArrayList<>();
			addDeviceTblAttributes(attributeIds, snmpMgr);
			
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceDescr);
			diskType.setDefaultNameCababilityId(defName.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDiskStorageAccess);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDiskStorageMedia);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDiskStorageRemoveble);
			attributeIds.add(snmp.getID());
			
			diskType.setAttributeIds(attributeIds);			
			diskType = capMgr.updateServiceElementType(diskType);
		}
		else {
			diskType = sets[0];
		}
		
		sstd = new SnmpServiceElementTypeDiscriminator();
		discriminatorValue = new SnmpServiceElementTypeDiscriminatorStringValue();
		discriminatorValue.setValue("1.3.6.1.2.1.25.3.1.6");
		sstd.setDiscriminatorValue(discriminatorValue);
		sstd.setServiceElementTypeId(diskType.getID());		
		discriminators.add(sstd);
		
		/**
		 * Snmp SW installed.
		 */
		levelOid = new SnmpLevelOID();
		levelOids.add(levelOid);		
		levelOid.setName("HostMibSWInstalled");
		
		discriminators = new ArrayList<>();
		levelOid.setDisriminators(discriminators);
			
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledEntry);
		levelOid.setContextOID(snmp);
		
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledName);
		levelOid.setDescriminatorOID(snmp);
		
		sets = discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.software, serviceElmType.getVendor());
		ServiceElementType swType = null;
		if ( sets == null || sets.length == 0 ) {
			
			swType = new ServiceElementType();
			swType.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			swType.setCategory(CategoryTypeEnum.software);
			swType.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			List<ID> attributeIds = new ArrayList<>();
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledName);
			swType.setDefaultNameCababilityId(defName.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledID);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledType);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledDate);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSWInstalledIndex);
			List<ID> ids = new ArrayList<>();
			
			ids.add(snmp.getID());
			swType.setUniqueIdentifierCapabilities(ids);
			
			swType.setAttributeIds(attributeIds);			
			swType = capMgr.updateServiceElementType(swType);
			
		}
		else {
			swType = sets[0];
		}
		sstd = new SnmpServiceElementTypeDiscriminator();
		sstd.setServiceElementTypeId(swType.getID());		
		discriminators.add(sstd);
		
		
		/**
		 * Snmp Storage installed.
		 */
		levelOid = new SnmpLevelOID();
		levelOids.add(levelOid);		
		levelOid.setName("HostMibStorage");
		
		discriminators = new ArrayList<>();
		levelOid.setDisriminators(discriminators);
			
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageEntry);
		levelOid.setContextOID(snmp);
		
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageDescr);
		levelOid.setDescriminatorOID(snmp);
		
		sets = discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.storage, serviceElmType.getVendor());
		ServiceElementType storageType = null;
		if ( sets == null || sets.length == 0 ) {
			
			storageType = new ServiceElementType();
			storageType.addSignatureValue(SignatureTypeEnum.Vendor, serviceElmType.getVendor());
			
			storageType.setCategory(CategoryTypeEnum.storage);
			storageType.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
			
			List<ID> attributeIds = new ArrayList<>();
			SNMP defName = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageDescr);
			storageType.setDefaultNameCababilityId(defName.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageAllocationUnits);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageSize);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageType);
			attributeIds.add(snmp.getID());
		
			List<ID> ids = new ArrayList<>();
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrStorageDescr);
			ids.add(snmp.getID());
			storageType.setUniqueIdentifierCapabilities(ids);
			
			storageType.setAttributeIds(attributeIds);			
			storageType = capMgr.updateServiceElementType(storageType);
			
		}
		else {
			storageType = sets[0];
		}
		sstd = new SnmpServiceElementTypeDiscriminator();
		sstd.setServiceElementTypeId(storageType.getID());		
		
		discriminators.add(sstd);
		
		logger.info("out HostMibGenerator ......... ");
		return sc;
	}
	
	
	/**
	 * 
	 * @param attributeIds
	 * @param snmpMgr
	 * @throws IntegerException
	 */
	public static void addDeviceTblAttributes( List<ID> attributeIds, SnmpManagerInterface snmpMgr ) throws IntegerException {
		
		SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceDescr);
		attributeIds.add(snmp.getID());
	    
		snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceID);
		attributeIds.add(snmp.getID());
		
	}
	
	/**
	 * 
	 * @param set
	 * @param snmpMgr
	 * @throws IntegerException
	 */
	public static void setDeviceTblComponentIdentify( ServiceElementType set, SnmpManagerInterface snmpMgr ) throws IntegerException {
		
		SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrDeviceIndex);
		List<ID> ids = new ArrayList<>();
		
		ids.add(snmp.getID());
		set.setUniqueIdentifierCapabilities(ids);
	}
	
	/**
	 * 
	 * 
	 * @param ept
	 * @param snmpType
	 * @throws IntegerException 
	 */
	public static void setUpTopServiceElementProperty( ElementEndPoint ept,
			                                    ServiceElementType set,
			                                    SnmpContainmentType snmpType ) throws IntegerException {
		
		SnmpManagerInterface snmpMgr = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		if ( snmpType == SnmpContainmentType.HostResourcesMib ) {
			List<ID> ids = new ArrayList<>();
			
			SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.sysName);
			ids.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifPhysAddress);
			ids.add(snmp.getID());
			
			set.setUniqueIdentifierCapabilities(ids);
			
			List<ID> attributeIds = new ArrayList<>();
			set.setAttributeIds(attributeIds);
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSystemUptime);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSystemDate);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSystemInitialLoadDevice);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSystemMaxProcesses);
			attributeIds.add(snmp.getID());
			
			snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrSystemNumUsers);
			attributeIds.add(snmp.getID());
			
		//	snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.hrMemorySize);
		//	attributeIds.add(snmp.getID());
		}
	}
		
}
