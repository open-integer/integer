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

import javax.persistence.ElementCollection;
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

	private String description = null;
	
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
	 * List of the parent organizational unit.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> parentOrganizations = null;

	/**
	 * List of the sub-organization units of this element of an organization.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> childOrginizations = null;

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
	 * @return the parentOrganizations
	 */
	public List<ID> getParentOrganizations() {
		return parentOrganizations;
	}

	/**
	 * @param parentOrganizations
	 *            the parentOrganizations to set
	 */
	public void setParentOrganizations(List<ID> parentOrganizations) {
		this.parentOrganizations = parentOrganizations;
	}

	/**
	 * @return the childOrginizations
	 */
	public List<ID> getChildOrginizations() {
		return childOrginizations;
	}

	/**
	 * @param childOrginizations
	 *            the childOrginizations to set
	 */
	public void setChildOrginizations(List<ID> childOrginizations) {
		this.childOrginizations = childOrginizations;
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

}
