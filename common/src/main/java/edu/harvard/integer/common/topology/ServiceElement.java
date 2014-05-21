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
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;

/*
 * A service element can be any function from an os manager in a vm environment to a high-level 
 * web service.  The idea is that services are defined independently of the physical systems on 
 * which they run.  For the system to work there must be a connection between the service and 
 * the physical world. Certainly not all services will run on all hardware.  For exmple you would 
 * not run Jboss on an ethernet switch. Note that this service element is that, not a service.  
 * This means that a service may have many service elements of the same kind working coooperatively
 * for load balancing, redundancy or other purposes on different pieces of hardware.  
 * In other cases as in the case of an OS instance/VM on a particular piece of hardware.
 * An example of a service element that exists on potentially many different systems is a 
 * postfix type system for mail routing. 
 */
@Entity
public class ServiceElement extends BaseEntity implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private NetworkLayer networkLayer = null;

	private String description = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementTypeId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElementTypeType")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementTypeName")) })
	private ID serviceElementTypeId = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "iconId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "iconType")),
			@AttributeOverride(name = "name", column = @Column(name = "iconName")) })
	private ID iconID = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "parentId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "parentType")),
			@AttributeOverride(name = "name", column = @Column(name = "parentName")) })
	private ID parentId = null;

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
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> credentials = null;

	/*
	 * Listing of the id object instances that reference domains supported by
	 * this service element. This list helps with scoping of configuration
	 * sequencing and control.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> domainIds = null;

	/**
	 * Listing of the id object instances that reference mechanisms supported by
	 * this service element. This list helps with scoping of configuration
	 * sequencing and control.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> mechanismIds = null;

	/**
	 * A listing of all the ServiceElementProtocolInstanceIdentifier instances
	 * for this service element.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	@CollectionTable(name = "ServiceElement_Values")
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
	private Date created = null;

	/**
	 * Date and time this object was last modified, either by the discovery
	 * system or by hand.
	 */
	private Date updated = null;

	/**
	 * A listing of the values of the attributes collected by the discovery
	 * program.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	@CollectionTable(name = "ServiceElement_AttributeValues")
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

	public ServiceElement() {
		super();
	}
	
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
	 * @return the parentId
	 */
	public ID getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(ID parentId) {
		this.parentId = parentId;
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
	public ID getIconID() {
		return iconID;
	}

	/**
	 * @param iconID
	 *            the iconID to set
	 */
	public void setIconID(ID iconID) {
		this.iconID = iconID;
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
	 * @return the domainIds
	 */
	public List<ID> getDomainIds() {
		return domainIds;
	}

	/**
	 * @param domainIds
	 *            the domainIds to set
	 */
	public void setDomainIds(List<ID> domainIds) {
		this.domainIds = domainIds;
	}

	/**
	 * @return the mechanismIds
	 */
	public List<ID> getMechanismIds() {
		return mechanismIds;
	}

	/**
	 * @param mechanismIds
	 *            the mechanismIds to set
	 */
	public void setMechanismIds(List<ID> mechanismIds) {
		this.mechanismIds = mechanismIds;
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
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
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
	 * @param hasChildren the hasChildren to set
	 */
	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
