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
 * 
 * Identifies an organization. Organizations have hierarchies/containment. That
 * is why this element can contain more Organizations.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Organization extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is a list of the IDs instances associated with location objects for
	 * this organization. Generally an organization will have several locations
	 * each with a different 'type'. For example, support center, development
	 * center, business office, etc.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> locations = null;

	/**
	 * Organizations can be either providers or sub units of provider
	 * organizations or they can be users/consumers.
	 */
	private String orginizationType = null;

	/**
	 * Authentication methods to use for this organization.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> authenticationMethods = null;

	/**
	 * ID of the parent organization or this organization.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "parentOrganizationId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "parentOrganizationType")),
			@AttributeOverride(name = "name", column = @Column(name = "parentOrganizationName")) })
	private ID parentOrganizationId = null;

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
	 * @return the orginizationType
	 */
	public String getOrginizationType() {
		return orginizationType;
	}

	/**
	 * @param orginizationType
	 *            the orginizationType to set
	 */
	public void setOrginizationType(String orginizationType) {
		this.orginizationType = orginizationType;
	}

	/**
	 * @return the authenticationMethods
	 */
	public List<ID> getAuthenticationMethods() {
		return authenticationMethods;
	}

	/**
	 * @param authenticationMethods
	 *            the authenticationMethods to set
	 */
	public void setAuthenticationMethods(List<ID> authenticationMethods) {
		this.authenticationMethods = authenticationMethods;
	}

	/**
	 * @return the parentOrganizationId
	 */
	public ID getParentOrganizationId() {
		return parentOrganizationId;
	}

	/**
	 * @param parentOrganizationId
	 *            the parentOrganizationId to set
	 */
	public void setParentOrganizationId(ID parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

}
