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
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 *         A role is associated with every individual known to the system. In
 *         some cases an individual may have many roles, however, they are only
 *         permitted to operate in the system as one role at a time.
 * 
 *         Note that role is distinct from one or more access policies that a
 *         user may be associated with. The reason is that role can influence
 *         how the system interacts with users (e.g., menus, messages, prompts).
 */
@Entity
public class Role extends BaseEntity {

	/**
	 * The Serial Version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This allows people to act as backups in different organizations for other
	 * organizations.
	 */
	private RoleType roleType = null;

	/**
	 * This is the id of the id object for the organization this role is used
	 * in. Different organizations may have different rules for similar roles -
	 * that is why this is not a list.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "organizationId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "organizationType")),
			@AttributeOverride(name = "name", column = @Column(name = "organizationName")) })
	private ID organizationId = null;

	/**
	 * This attribute contains a list of IDs of the Calendar Policy Objects that
	 * contains the date and time information that is used to determine when the
	 * role and associated AccessPolicy objects are active. If this is null, it
	 * is active when created and there policy remains in effect indefinitely.
	 * 
	 * The reason for placing this attribute here as opposed in the AccessPolicy
	 * object is that roles may be temporary or users may be backup. In these
	 * events (or when extra access is given to a development engineer to track
	 * down and production environment problem) it is the role that will come
	 * and go, not the access policy.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> calendarPolicies = null;

	/**
	 * @return the roleType
	 */
	public RoleType getRoleType() {
		return roleType;
	}

	/**
	 * @param roleType
	 *            the roleType to set
	 */
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the organizationId
	 */
	public ID getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	public void setOrganizationId(ID organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the calendarPolicies
	 */
	public List<ID> getCalendarPolicies() {
		return calendarPolicies;
	}

	/**
	 * @param calendarPolicies
	 *            the calendarPolicies to set
	 */
	public void setCalendarPolicies(List<ID> calendarPolicies) {
		this.calendarPolicies = calendarPolicies;
	}

}
