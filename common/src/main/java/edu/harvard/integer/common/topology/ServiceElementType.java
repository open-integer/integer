/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.common.topology;

/**
 * @author David Taylor
 *
 * Every service element can be only one type.
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

@Entity
public class ServiceElementType extends BaseEntity {
	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Category can be hardware as in the case of an interface card, a disk, or
	 * something else that you can touch. It could also be a software element
	 * such as an operating system, web server like Apache or something else. A
	 * system that is said to be virtual is one where there is a software system
	 * emulating a physcial thing as in the case of a Linux of Centos VM.
	 * Currents types that are supported are: Virtual Hardware Software Element
	 * Software Application See RFCS 2287 and 2564
	 */
	@ManyToOne
	private Category category = null;

	/**
	 * A signature is used to identify a specific service element type. It may
	 * take several Signature instances to accurately identify an SET.
	 * 
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OneToMany
	private List<Signature> signatures = null;

	/**
	 * A specific set of options installed.
	 */
	private String featureSet = null;

	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> capabilityIds = null;

	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> childServiceElementTypes;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "iconId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "iconType")),
			@AttributeOverride(name = "name", column = @Column(name = "iconName")) })
	private ID iconId = null;

	private String description = null;

	/**
	 * Is this a field replaceable unit? Values are yes, no, unknown
	 */
	@Enumerated(EnumType.STRING)
	private FieldReplaceableUnitEnum fieldReplaceableUnit = null;

	/**
	 * This points to the capability that is used as the default for the human
	 * readable display name for service elements of this type.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "defaultNameCababilityId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "defaultNameCababilityType")),
			@AttributeOverride(name = "name", column = @Column(name = "defaultNameCababilityName")) })
	private ID defaultNameCababilityId = null;

	/**
	 * A list of the Capabilities used to uniquely identify the service element
	 * type.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> uniqueIdentifierCapabilities = null;

	/**
	 * A listing of default attributes that the discovery system will collect.
	 * This list can be modified by the system administrator.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> attributeIds = null;

	/**
	 * This attribute may not be present for many ServiceElements. When present,
	 * it will usually be associated with ServiceElementTypes that are at the
	 * top of their hierarchy like a router, server, etc. This attribute points
	 * to the capability that is able to retrieve the current boot image name
	 * used by the ServiceElement.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "bootImageCapabilityId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "bootImageCapabilityType")),
			@AttributeOverride(name = "name", column = @Column(name = "bootImageCapabilityName")) })
	private ID bootImageCapability = null;

	/**
	 * Indicates whether the service element is a physical or logical type. If
	 * empty it is unknown.
	 */
	@Enumerated(EnumType.STRING)
	private PhysicalLogicalEnum physicalLocal = null;

	/**
	 * Some vendors assign specific sub types to the broad categories of things
	 * found in the category attribute. The vendorSpecificSubType points to the
	 * capability for an instance of a ServiceElementType that can retrieve this
	 * information. When this is available, the Discovery Process will attempt
	 * to retrieve this information.
	 */
	private String vendorSpecificSubType = null;

	/**
	 * List of Applicability objects associated with this service element type.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> applicabilities = null;

	/**
	 * SNMP override used to limit the requests or rate at which messages are
	 * sent to a device.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "snmpOverrideId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "snmpOverrideType")),
			@AttributeOverride(name = "name", column = @Column(name = "snmpOverrideName")) })
	private ID snmpOverride = null;

	/**
	 * @return the featureSet
	 */
	public String getFeatureSet() {
		return featureSet;
	}

	/**
	 * @param featureSet
	 *            the featureSet to set
	 */
	public void setFeatureSet(String featureSet) {
		this.featureSet = featureSet;
	}

	/**
	 * @return the capabilityIds
	 */
	public List<ID> getCapabilityIds() {
		return capabilityIds;
	}

	/**
	 * @param capabilityIds
	 *            the capabilityIds to set
	 */
	public void setCapabilityIds(List<ID> capabilityIds) {
		this.capabilityIds = capabilityIds;
	}

	/**
	 * @return the signatures
	 */
	public List<Signature> getSignatures() {
		return signatures;
	}

	/**
	 * @param signatures
	 *            the signatures to set
	 */
	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures;
	}

	/**
	 * Find the value for Vendor in the Signature list. If the vendor is not
	 * found then return null.
	 * 
	 * @return vendor for this signature.
	 */
	public String getVendor() {
		return getSignatureForType(SignatureTypeEnum.Vendor);
	}

	public String getModel() {
		return getSignatureForType(SignatureTypeEnum.Model);
	}

	public String getSoftware() {
		return getSignatureForType(SignatureTypeEnum.SoftwareVersion);
	}

	public String getFirmware() {
		return getSignatureForType(SignatureTypeEnum.Firmware);
	}

	private String getSignatureForType(SignatureTypeEnum type) {
		for (Signature signature : signatures) {
			if (type.equals(signature.getSignatureType())) {
				if (signature.getValueOperators() != null
						&& signature.getValueOperators().size() > 0)
					return signature.getValueOperators().get(0).getValue();
			}

		}

		return null;
	}


	public void addSignatureValue(ID semoId, SignatureTypeEnum signatureType, String value) {
		if (signatures == null)
			signatures = new ArrayList<Signature>();

		boolean addedIt = false;
		for (Signature signature : signatures) {
		
			if (signatureType.equals(signature.getSignatureType())) {

				addedIt = true;
				if (signature.getValueOperators() != null) {
					boolean foundValue = false;
					for (SignatureValueOperator valueOperator : signature.getValueOperators()) {
						if (valueOperator.getValue().equals(value)) {
							foundValue = true;
							break;
						}
					}
					
					if (!foundValue) {
						signature.getValueOperators().add(createEuqualValueOperator(value));
					}

				} else {

					List<SignatureValueOperator> values = new ArrayList<SignatureValueOperator>();
					values.add(createEuqualValueOperator(value));
					signature.setValueOperators(values);
				}
			}
		}
		
		if (!addedIt ) {
			Signature signature = new Signature();
			signature.setSignatureType(signatureType);
			signature.setSemoId(semoId);
			
			List<SignatureValueOperator> values = new ArrayList<SignatureValueOperator>();
			values.add(createEuqualValueOperator(value));
			signature.setValueOperators(values);
			
			signature.setValueOperators(values);
			
			signatures.add(signature);
		}
	}

	private SignatureValueOperator createEuqualValueOperator(String value) {
		SignatureValueOperator valueOperator = new SignatureValueOperator();
		valueOperator.setValue(value);
		valueOperator.setOperator(ValueOpertorEnum.Equal);
		
		return valueOperator;
	}
	
	/**
	 * @return the childServiceElementTypes
	 */
	public List<ID> getChildServiceElementTypes() {
		return childServiceElementTypes;
	}

	/**
	 * @param childServiceElementTypes
	 *            the childServiceElementTypes to set
	 */
	public void setChildServiceElementTypes(List<ID> childServiceElementTypes) {
		this.childServiceElementTypes = childServiceElementTypes;
	}

	/**
	 * @return the iconId
	 */
	public ID getIconId() {
		return iconId;
	}

	/**
	 * @param iconId
	 *            the iconId to set
	 */
	public void setIconId(ID iconId) {
		this.iconId = iconId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the fieldReplaceableUnit
	 */
	public FieldReplaceableUnitEnum getFieldReplaceableUnit() {
		return fieldReplaceableUnit;
	}

	/**
	 * @param fieldReplaceableUnit
	 *            the fieldReplaceableUnit to set
	 */
	public void setFieldReplaceableUnit(
			FieldReplaceableUnitEnum fieldReplaceableUnit) {
		this.fieldReplaceableUnit = fieldReplaceableUnit;
	}

	/**
	 * @return the defaultNameCababilityId
	 */
	public ID getDefaultNameCababilityId() {
		return defaultNameCababilityId;
	}

	/**
	 * @param defaultNameCababilityId
	 *            the defaultNameCababilityId to set
	 */
	public void setDefaultNameCababilityId(ID defaultNameCababilityId) {
		this.defaultNameCababilityId = defaultNameCababilityId;
	}

	/**
	 * @return the uniqueIdentifierCapabilities
	 */
	public List<ID> getUniqueIdentifierCapabilities() {
		return uniqueIdentifierCapabilities;
	}

	/**
	 * @param uniqueIdentifierCapabilities
	 *            the uniqueIdentifierCapabilities to set
	 */
	public void setUniqueIdentifierCapabilities(
			List<ID> uniqueIdentifierCapabilities) {
		this.uniqueIdentifierCapabilities = uniqueIdentifierCapabilities;
	}

	/**
	 * @return the attributeIds
	 */
	public List<ID> getAttributeIds() {
		return attributeIds;
	}

	/**
	 * @param attributeIds
	 *            the attributeIds to set
	 */
	public void setAttributeIds(List<ID> attributeIds) {
		this.attributeIds = attributeIds;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the applicabilities
	 */
	public List<ID> getApplicabilities() {
		return applicabilities;
	}

	/**
	 * @param applicabilities
	 *            the applicabilities to set
	 */
	public void setApplicabilities(List<ID> applicabilities) {
		this.applicabilities = applicabilities;
	}

	/**
	 * @return the bootImageCapability
	 */
	public ID getBootImageCapability() {
		return bootImageCapability;
	}

	/**
	 * @param bootImageCapability
	 *            the bootImageCapability to set
	 */
	public void setBootImageCapability(ID bootImageCapability) {
		this.bootImageCapability = bootImageCapability;
	}

	/**
	 * @return the physicalLocal
	 */
	public PhysicalLogicalEnum getPhysicalLocal() {
		return physicalLocal;
	}

	/**
	 * @param physicalLocal
	 *            the physicalLocal to set
	 */
	public void setPhysicalLocal(PhysicalLogicalEnum physicalLocal) {
		this.physicalLocal = physicalLocal;
	}

	/**
	 * @return the vendorSpecificSubType
	 */
	public String getVendorSpecificSubType() {
		return vendorSpecificSubType;
	}

	/**
	 * @param vendorSpecificSubType
	 *            the vendorSpecificSubType to set
	 */
	public void setVendorSpecificSubType(String vendorSpecificSubType) {
		this.vendorSpecificSubType = vendorSpecificSubType;
	}

	/**
	 * @return the snmpOverride
	 */
	public ID getSnmpOverride() {
		return snmpOverride;
	}

	/**
	 * @param snmpOverride
	 *            the snmpOverride to set
	 */
	public void setSnmpOverride(ID snmpOverride) {
		this.snmpOverride = snmpOverride;
	}

}
