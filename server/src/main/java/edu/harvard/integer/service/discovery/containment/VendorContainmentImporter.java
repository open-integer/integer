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

package edu.harvard.integer.service.discovery.containment;


/**
 * @author David Taylor
 * 
 */
public class VendorContainmentImporter {
//
//	private VendorContainmentSelectorYaml yaml = null;
//
//	private VendorContainmentSelector selector = null;
//
//	private SnmpManagerInterface snmpMgr = null;
//
//	private ManagementObjectCapabilityManagerInterface capMgr = null;
//
//	private List<ServiceElementType> allServiceElementTypes = null;
//	
//	public void importYaml(String yamlContent) throws IntegerException {
//		Yaml yaml = new Yaml(new Constructor(
//				VendorContainmentSelectorYaml.class));
//
//		VendorContainmentSelectorYaml containmentSelectorYaml = (VendorContainmentSelectorYaml) yaml
//				.load(yamlContent);
//
//		createContainmentSelector(containmentSelectorYaml);
//
//	}
//
//	/**
//	 * @param containmentSelectorYaml
//	 * @throws IntegerException
//	 */
//	private void createContainmentSelector(
//			VendorContainmentSelectorYaml containmentSelectorYaml)
//			throws IntegerException {
//
//		SnmpContainment sc = new SnmpContainment();
//
//		allServiceElementTypes = new ArrayList<ServiceElementType>();
//
//		for (ServiceElementTypeYaml serviceElementType : containmentSelectorYaml
//				.getServiceElementTypes()) {
//			ServiceElementType setType = createServiceElementType(serviceElementType);
//
//			allServiceElementTypes.add(setType);
//
//			if (setType.getName().equals(
//					containmentSelectorYaml.getTopLevelServiceElementType()))
//				sc.setServiceElementTypeId(setType.getID());
//		}
//
//		// TODO: Do we need the containment type?
//		sc.setContainmentType(SnmpContainmentType.HostResourcesMib);
//
//		sc.setName(containmentSelectorYaml.getName());
//
//		// Find the ServiceElementTypes that are mapped by a mapper and conenct
//		// to the top level (Should be generic) service element type.
//		// Then create descriminators for all of them. 
//		// Then add the child service element types to the list.
//
//		List<SnmpLevelOID> levelOids = new ArrayList<>();
//		sc.setSnmpLevels(levelOids);
//
//		for (ServiceElementTypeYaml serviceElementType : containmentSelectorYaml
//				.getServiceElementTypes()) {
//			SnmpLevelOID levelOid = new SnmpLevelOID();
//
//			levelOid.setName(serviceElementType.getName());
//
//				String oidName = serviceElementType.getContextObject();
//				SNMP snmp = snmpMgr.getSNMPByName(oidName);
//				if (snmp != null)
//					levelOid.setContextOID(snmp);
//
//				// TODO: Add DescriminatorOID
//				// levelOid.setDescriminatorOID(snmp);
//			
//
//			levelOids.add(levelOid);
//		}
//
//		// TODO: Add Descriminator
//		// List<SnmpServiceElementTypeDiscriminator> discriminators = new
//		// ArrayList<>();
//		// levelOid.setDisriminators(discriminators);
//
//	}
//
//	private SnmpLevelOID[] createChildLevels(ServiceElementTypeYaml serviceElementType, 
//			List<ServiceElementTypeYaml> serviceElementTypes) {
//		
//		for (ServiceElementTypeYaml serviceElementTypeYaml : serviceElementTypes) {
//			
//		}
//		
//		return null;
//	}
//	
//	private ServiceElementType createServiceElementType(
//			ServiceElementTypeYaml serviceElemntTypeYaml)
//			throws IntegerException {
//
//		ServiceElementType setCpu = new ServiceElementType();
//		setCpu.setVendor(yaml.getVendor());
//
//		if (serviceElemntTypeYaml.getFieldReplaceableUnit() != null
//				&& "true".equals(serviceElemntTypeYaml
//						.getFieldReplaceableUnit().toLowerCase()))
//			setCpu.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);
//		else
//			setCpu.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
//
//		if (serviceElemntTypeYaml.getContextObject() != null) {
//			
//			SNMP snmp = snmpMgr.getSNMPByName(serviceElemntTypeYaml.getContextObject());
//			
//			List<ID> uniqeIds = new ArrayList<ID>();
//			
//			if (snmp instanceof SNMPTable) {
//				SNMPTable snmpTable = (SNMPTable) snmp;
//				
//				for (SNMP indexOid : snmpTable.getIndex()) {
//					
//				}
//			}
//
//			// TODO: get unique Ids
//			setCpu.setUniqueIdentifierCapabilities(uniqeIds);
//		}
//
//		SNMP defName = snmpMgr.getSNMPByName(serviceElemntTypeYaml
//				.getNameMangementObject());
//		setCpu.setDefaultNameCababilityId(defName.getID());
//
//		if (serviceElemntTypeYaml.getManagementObjects() != null) {
//			List<ID> attributeIds = new ArrayList<>();
//
//			for (ManagementObjectYaml mo : serviceElemntTypeYaml
//					.getManagementObjects()) {
//
//				SNMP snmp = snmpMgr.getSNMPByName(mo.getOidName());
//				attributeIds.add(snmp.getID());
//			}
//
//			setCpu.setAttributeIds(attributeIds);
//		}
//
//		setCpu = capMgr.updateServiceElementType(setCpu);
//
//		return setCpu;
//	}
}
