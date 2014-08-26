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

package edu.harvard.integer.common.topology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Size;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;

/**
 * @author David Taylor
 * 
 */
@MappedSuperclass
public class ServiceElementFields extends BaseEntity {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private NetworkLayer networkLayer = null;

	@Size(max = 2000)
	private String description = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementTypeId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElementTypeType")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementTypeName")) })
	private ID serviceElementTypeId = null;

	private String iconName = null;

	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> parentIds = null;

	private Boolean hasChildren = false;

	// Since some ServiceElements will have many capabilities this attribute
	// will list all the capabilities the serviceElement has. This does not mean
	// that the service element has been configured, that would be determined by
	// whether or not there were ServiceElementManagementObjects that were
	// configured and deployed.
	// In some cases, the order of configuration of capabilities is important.
	// For this reason, this is an ordered list that represents the required
	// order the Capabilities must be deployed/sent to the ServiceElement. How
	// this is accomplished is protocol/communication method specific.
	// This is an ordered listing of the capabilities for this service element.
	// It is found here instead of the ServiceElementType because order may be
	// inpacted by local considerations such as hardware and software
	// configuration.
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> capabilites = null;

	// The id of the id object instance that identifies the Location instance to
	// which this ServiceElement belongs.
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "primaryLocationId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "primaryLocationType")),
			@AttributeOverride(name = "name", column = @Column(name = "primaryLocationName")) })
	private ID primaryLocation = null;

	// The id of the id object assocaited with the Organization instance
	// responsible for the operational control of this service element. Ideally,
	// this would be hierarchical a follow a simple containment model; however
	// this is not always the case. For example a router at an interchange that
	// has many interfaces. Only some of which may be under the operational
	// control of one customer, while others may be under the control of others.
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "operationalControlId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "operationalControlType")),
			@AttributeOverride(name = "name", column = @Column(name = "operationalControlName")) })
	private ID operationalControlId = null;

	/*
	 * It is not always easy to algorithmically determine the criticality of
	 * every serviceElement in the environment. For example, a server providing
	 * functions to many systems versus one providing support to only a few, or
	 * a network element that is the main gateway to the Internet versus one
	 * routing traffic to a small building.
	 * 
	 * Where this value is populated via software or manually, this attribute
	 * allows the system to make decisions about access to the device,
	 * processing events and alarms, reporting, etc.
	 * 
	 * The higher the value, the more critical the ServiceElement is.
	 */
	private int serviceElementCriticality = 0;

	/*
	 * Listing of the credentialIds that have been created for this service
	 * element by the ServiceElementAccess Manager. Note that this is not the
	 * actual credentials, but the reference to them.
	 * 
	 * Some service elements have nested levels of access (as in the case of the
	 * configuration of certain router elements. In this case, the credential
	 * set up and later used by the ServiceElementAccessManager will have this
	 * information.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> credentials = null;

	/**
	 * A listing of all the ServiceElementProtocolInstanceIdentifier instances
	 * for this service element.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
//	@CollectionTable(name = "ServiceElement_Values")
	private List<ServiceElementProtocolInstanceIdentifier> values = null;

	/**
	 * The serviceElementCriticalityOverride has the same possible values as the
	 * serviceElementCriticality attribute but is manually entered by the
	 * operator. When not null, this value will take precedence over the
	 * serviceElementCriticality attribute.
	 */
	private int serviceElementCriticalityOverride = 0;

	/**
	 * Unique identifier of the Service Element. ID's of the management object
	 * that is used to uniquely identify this object. ex. (IpAddress or serial
	 * number)
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> uniqueIdentifierIds = null;

	/**
	 * If this value is not null, then the value it contains will be used in the
	 * name attribute.
	 */
	private String nameOverride = null;

	/**
	 * Date and time the service element was created, either by the discovery
	 * system or by hand.
	 */
	private Long created = null;

	/**
	 * Date and time this object was last modified, either by the discovery
	 * system or by hand.
	 */
	private Long updated = null;

	/**
	 * A listing of the values of the attributes collected by the discovery
	 * program.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
//	@CollectionTable(name = "ServiceElement_AttributeValues")
	private List<ManagementObjectValue> attributeValues = null;

	/**
	 * This attribute describes the level of information confidentiality
	 * held/transfered by the service element. For example confidential
	 * information. It is found in both the ServiceElement and Adjacency
	 * Objects.
	 */
	// TODO: Get the correct type for this object
	private int securityLevel = 0;

	/**
	 * The environmentLevel attribute indicates whether a system is in
	 * production, test, etc. It is an enumerated integer where 1 is the
	 * production level and there can be a number of configured levels such as
	 * test as level 2, development as level 3 etc.
	 */
	@Enumerated(EnumType.STRING)
	private EnvironmentLevelEnum environmentLevel = null;

	/**
	 * Any arbitrary comment the user would like.
	 */
	private String comment = null;

	/**
	 * A listing of all the ServiceElementProtocolInstanceIdentifier instances
	 * for this service element.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> serviceElementProtocolInstanceIdentifiers = null;

	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ServiceElementAssociation> associations = null;

	/**
	 * Category that this service element is in. Category is a general type of
	 * serviceElement like a disk or Ethernet card without any vendor
	 * specificities.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private Category category = null;


	/**
	 * Configured state includes whether the service element is in compliance
	 * with current policy or not. If it is not, then other methods would be
	 * able to retrieve information about policy overrides, etc.
	 */
	public String getConfiguredState() {
		return null;
	}

	/**
	 * @return the networkLayer
	 */
	public NetworkLayer getNetworkLayer() {
		return networkLayer;
	}

	/**
	 * @param networkLayer
	 *            the networkLayer to set
	 */
	public void setNetworkLayer(NetworkLayer networkLayer) {
		this.networkLayer = networkLayer;
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
	 * Set the parent ID for this service element. This will replace any parents
	 * that are currently set for this service element.
	 */
	public void setParentId(ID id) {
		parentIds = new ArrayList<ID>();
		parentIds.add(id);
	}

	/**
	 * Add a parent to this service element.
	 * 
	 * @param id
	 */
	public void addParentId(ID id) {
		if (parentIds == null)
			parentIds = new ArrayList<ID>();

		if (!parentIds.contains(id))
			parentIds.add(id);

	}

	/**
	 * @return the parentId
	 */
	public List<ID> getParentIds() {
		return parentIds;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(List<ID> parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * @return the capabilites
	 */
	public List<ID> getCapabilites() {
		return capabilites;
	}

	/**
	 * @param capabilites
	 *            the capabilites to set
	 */
	public void setCapabilites(List<ID> capabilites) {
		this.capabilites = capabilites;
	}

	/**
	 * @return the primaryLocation
	 */
	public ID getPrimaryLocation() {
		return primaryLocation;
	}

	/**
	 * @param primaryLocation
	 *            the primaryLocation to set
	 */
	public void setPrimaryLocation(ID primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	/**
	 * @return the operationalControlId
	 */
	public ID getOperationalControlId() {
		return operationalControlId;
	}

	/**
	 * @param operationalControlId
	 *            the operationalControlId to set
	 */
	public void setOperationalControlId(ID operationalControlId) {
		this.operationalControlId = operationalControlId;
	}

	/**
	 * @return the serviceElementCriticality
	 */
	public int getServiceElementCriticality() {
		return serviceElementCriticality;
	}

	/**
	 * @param serviceElementCriticality
	 *            the serviceElementCriticality to set
	 */
	public void setServiceElementCriticality(int serviceElementCriticality) {
		this.serviceElementCriticality = serviceElementCriticality;
	}

	/**
	 * @return the values
	 */
	public List<ServiceElementProtocolInstanceIdentifier> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<ServiceElementProtocolInstanceIdentifier> values) {
		this.values = values;
	}

	/**
	 * @return the serviceElementTypeId
	 */
	public ID getServiceElementTypeId() {
		return serviceElementTypeId;
	}

	/**
	 * @param serviceElementTypeId
	 *            the serviceElementTypeId to set
	 */
	public void setServiceElementTypeId(ID serviceElementTypeId) {
		this.serviceElementTypeId = serviceElementTypeId;
	}

	/**
	 * @return the iconID
	 */
	public String getIconName() {
		return iconName;
	}

	/**
	 * @param iconID
	 *            the iconID to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @return the credentials
	 */
	public List<ID> getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(List<ID> credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the serviceElementCriticalityOverride
	 */
	public int getServiceElementCriticalityOverride() {
		return serviceElementCriticalityOverride;
	}

	/**
	 * @param serviceElementCriticalityOverride
	 *            the serviceElementCriticalityOverride to set
	 */
	public void setServiceElementCriticalityOverride(
			int serviceElementCriticalityOverride) {
		this.serviceElementCriticalityOverride = serviceElementCriticalityOverride;
	}

	/**
	 * @return the uniqueIdentifier
	 */
	public List<ID> getUniqueIdentifierIds() {
		return uniqueIdentifierIds;
	}

	/**
	 * @param uniqueIdentifier
	 *            the uniqueIdentifier to set
	 */
	public void setUniqueIdentifierIds(List<ID> uniqueIdentifierIds) {
		this.uniqueIdentifierIds = uniqueIdentifierIds;
	}

	/**
	 * @return the nameOverride
	 */
	public String getNameOverride() {
		return nameOverride;
	}

	/**
	 * @param nameOverride
	 *            the nameOverride to set
	 */
	public void setNameOverride(String nameOverride) {
		this.nameOverride = nameOverride;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		if (created != null)
			return new Date(created.longValue());
		else
			return null;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		if (created != null)
			this.created = Long.valueOf(created.getTime());
		else
			this.created = null;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		if (updated != null)
			return new Date(updated.longValue());
		else
			return null;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		if (updated != null)
			this.updated = Long.valueOf(updated.getTime());
		else
			this.updated = null;
	}

	/**
	 * @return the attributeValues
	 */
	public List<ManagementObjectValue> getAttributeValues() {
		return attributeValues;
	}

	/**
	 * @param attributeValues
	 *            the attributeValues to set
	 */
	public void setAttributeValues(List<ManagementObjectValue> attributeValues) {
		this.attributeValues = attributeValues;
	}

	/**
	 * @return the securityLevel
	 */
	public int getSecurityLevel() {
		return securityLevel;
	}

	/**
	 * @param securityLevel
	 *            the securityLevel to set
	 */
	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	/**
	 * @return the environmentLevel
	 */
	public EnvironmentLevelEnum getEnvironmentLevel() {
		return environmentLevel;
	}

	/**
	 * @param environmentLevel
	 *            the environmentLevel to set
	 */
	public void setEnvironmentLevel(EnvironmentLevelEnum environmentLevel) {
		this.environmentLevel = environmentLevel;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the serviceElementProtocolInstanceIdentifiers
	 */
	public List<ID> getServiceElementProtocolInstanceIdentifiers() {
		return serviceElementProtocolInstanceIdentifiers;
	}

	/**
	 * @param serviceElementProtocolInstanceIdentifiers
	 *            the serviceElementProtocolInstanceIdentifiers to set
	 */
	public void setServiceElementProtocolInstanceIdentifiers(
			List<ID> serviceElementProtocolInstanceIdentifiers) {
		this.serviceElementProtocolInstanceIdentifiers = serviceElementProtocolInstanceIdentifiers;
	}

	/**
	 * @return the hasChildren
	 */
	public Boolean getHasChildren() {
		return hasChildren;
	}

	/**
	 * @param hasChildren
	 *            the hasChildren to set
	 */
	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public List<ServiceElementAssociation> getAssociations() {
		return associations;
	}

	public void setAssociations(List<ServiceElementAssociation> associations) {
		this.associations = associations;
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
}
