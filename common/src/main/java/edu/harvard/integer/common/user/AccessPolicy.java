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

package edu.harvard.integer.common.user;

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
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * 
 * An access policy is associated with one or more roles. It is used to identify
 * which ServiceElement instances are accessible to a user and what operations
 * on those elements are permitted.
 * 
 * <p>Each of the attributes allows narrower and narrower control. The most
 * restrictive of which would be the identification of specific ServiceElement
 * instance(s).
 * 
 * <p>The way to think about how AccessPolicy instances work in association with
 * roles is that roles have a deny all basic function until an AccessPolicy is
 * associated with them.
 * 
 * <p>Permit policies grant access according to the rules they contain. In some
 * cases it is more convenient to grant the policies and create a deny policy
 * that restricts the permission granted in the first access policy.
 * 
 * <p>If an AccessPolicy is a Deny policy then the permitParentId policy will be
 * filled in. This is to ensure that the deny filter is associated with the
 * correct permit policy since a role may have many AccessPolicies associated
 * with it.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class AccessPolicy extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of roles associated with this AccessPolicy.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> roles = null;

	/**
	 * For the serviceElements that are allowed to be accessed by users under
	 * the control of this AccessPolicy, this determines which functions they
	 * are allowed. Functions are: read, write and create
	 * 
	 * They may be assigned in any combination.
	 */
	@ElementCollection(targetClass = PermissionTypeEnum.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "AccessPolicy_Permissions")
	private List<PermissionTypeEnum> permissions = null;

	/**
	 * Restricts this AccessPolicy scope to only those locations identified in
	 * this attribute which contains a list of organization IDs.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> orginizations = null;

	/**
	 * This list is filled in to either restrict locations for a specific
	 * organization or in the case of the need to specify multiple organization
	 * locations that may span locations. In this case the organizationIdList
	 * would be null.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> locations = null;

	/**
	 * This is provided to control access to systems based on their importance
	 * to the overall function of the environment. See the definition of this
	 * attribute in the ServiceElement Class.
	 */
	private int serviceElementCreitcality = 0;

	/**
	 * This 'restriction' allows users to be limited within an environment to
	 * one or more types of service elements. For example routers of a
	 * particular type, load balancers, DNS servers etc.
	 * 
	 * If this attribute is null, there are not restrictions which is a very
	 * unlikely, but possible condition if the permissions list is read-write or
	 * read-write-create.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> serviceElementTypes = null;

	/**
	 * This 'restriction' allows users to be limited within an environment to
	 * one or more types of service elements. For example routers of a
	 * particular type, load balancers, DNS servers etc.
	 * 
	 * If this attribute is null, there are not restrictions which is a very
	 * unlikely, but possible condition if the permissions list is read-write or
	 * read-write-create.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> domains = null;

	/**
	 * If additional finer-grained restriciton is needed beyond organization,
	 * location, criticality, service element type and domain, this attribute
	 * allows the specification of specific serviceElement instances. If the
	 * other attributes are filled in they will further restrict access. When
	 * this attribute is used, all listed service elements must be of the same
	 * type.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> instances = null;

	/**
	 * Because of the sensitivity of some systems, a policy requiring a
	 * particular form of authentication (e.g., two factor) may be required for
	 * some functions. For this reason, a list can be created/maintained that is
	 * used to enforce the type of authentication required for certain
	 * AccessPolicy instances.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> requiredAuths = null;

	/**
	 * AccessPolicy instances are always one of two types, permit or deny. If it
	 * is a permit, then all of the elements it describes are included for
	 * access. If it is a deny, then all of the serviceElements it describes are
	 * denied access to those uses associated with the roles to which this
	 * AccessPolicy instance is associated.
	 */
	private PermitDenyEnum permitDeny = null;

	/**
	 * If an AccessPolicy is a Deny policy then the permitParentId policy will
	 * be filled in. This is to ensure that the deny filter is associated with
	 * the correct permit policy since a role may have many AccessPolicies
	 * associated with it.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "parentId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "parentType")),
			@AttributeOverride(name = "name", column = @Column(name = "parentName")) })
	private ID parentId = null;

	/**
	 * @return the roles
	 */
	public List<ID> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<ID> roles) {
		this.roles = roles;
	}

	/**
	 * @return the permissions
	 */
	public List<PermissionTypeEnum> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(List<PermissionTypeEnum> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the orginizations
	 */
	public List<ID> getOrginizations() {
		return orginizations;
	}

	/**
	 * @param orginizations
	 *            the orginizations to set
	 */
	public void setOrginizations(List<ID> orginizations) {
		this.orginizations = orginizations;
	}

	/**
	 * @return the locations
	 */
	public List<ID> getLocations() {
		return locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(List<ID> locations) {
		this.locations = locations;
	}

	/**
	 * @return the serviceElementCreitcality
	 */
	public int getServiceElementCreitcality() {
		return serviceElementCreitcality;
	}

	/**
	 * @param serviceElementCreitcality
	 *            the serviceElementCreitcality to set
	 */
	public void setServiceElementCreitcality(int serviceElementCreitcality) {
		this.serviceElementCreitcality = serviceElementCreitcality;
	}

	/**
	 * @return the serviceElementTypes
	 */
	public List<ID> getServiceElementTypes() {
		return serviceElementTypes;
	}

	/**
	 * @param serviceElementTypes
	 *            the serviceElementTypes to set
	 */
	public void setServiceElementTypes(List<ID> serviceElementTypes) {
		this.serviceElementTypes = serviceElementTypes;
	}

	/**
	 * @return the domains
	 */
	public List<ID> getDomains() {
		return domains;
	}

	/**
	 * @param domains
	 *            the domains to set
	 */
	public void setDomains(List<ID> domains) {
		this.domains = domains;
	}

	/**
	 * @return the instances
	 */
	public List<ID> getInstances() {
		return instances;
	}

	/**
	 * @param instances
	 *            the instances to set
	 */
	public void setInstances(List<ID> instances) {
		this.instances = instances;
	}

	/**
	 * @return the requiredAuths
	 */
	public List<ID> getRequiredAuths() {
		return requiredAuths;
	}

	/**
	 * @param requiredAuths
	 *            the requiredAuths to set
	 */
	public void setRequiredAuths(List<ID> requiredAuths) {
		this.requiredAuths = requiredAuths;
	}

	/**
	 * @return the permitDeny
	 */
	public PermitDenyEnum getPermitDeny() {
		return permitDeny;
	}

	/**
	 * @param permitDeny
	 *            the permitDeny to set
	 */
	public void setPermitDeny(PermitDenyEnum permitDeny) {
		this.permitDeny = permitDeny;
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

}
