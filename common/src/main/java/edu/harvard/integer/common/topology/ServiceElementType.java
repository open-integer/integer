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
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

@Entity
public class ServiceElementType extends BaseEntity {
	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = 1L;

	private String firmware = null;

	private String model = null;

	private String vendor = null;

	private String elementType = null;

	/**
	 * A specific set of options installed.
	 */
	private String featureSet = null;

	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> capabilityIds = null;

	@ElementCollection
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
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> uniqueIdentifierCapabilities = null;

	/**
	 * A listing of default attributes that the discovery system will collect.
	 * This list can be modified by the system administrator.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> attributeIds = null;

	/**
	 * @return the firmware
	 */
	public String getFirmware() {
		return firmware;
	}

	/**
	 * @param firmware
	 *            the firmware to set
	 */
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the elementType
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * @param elementType
	 *            the elementType to set
	 */
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

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
	 * @return the childServiceElementTypes
	 */
	public List<ID> getChildServiceElementTypes() {
		return childServiceElementTypes;
	}

	/**
	 * @param childServiceElementTypes the childServiceElementTypes to set
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
	 * @param iconId the iconId to set
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
	 * @param description the description to set
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
	 * @param fieldReplaceableUnit the fieldReplaceableUnit to set
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
	 * @param defaultNameCababilityId the defaultNameCababilityId to set
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
	 * @param uniqueIdentifierCapabilities the uniqueIdentifierCapabilities to set
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
	 * @param attributeIds the attributeIds to set
	 */
	public void setAttributeIds(List<ID> attributeIds) {
		this.attributeIds = attributeIds;
	}

	
}
