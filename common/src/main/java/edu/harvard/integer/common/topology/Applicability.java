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

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * 
 * There will be an instance of the applicability object for each unique
 * combination of vendor, model, software, firmware revision, feature set,
 * hardware configuration list that uniquely describe a valid management object.
 * This means that a ServiceElementManagementObject instance may potentially be
 * associated with many Applicability objects.
 * 
 * <p>Not that it is assumed that management objects do not apply across management
 * access methods since the data formats, encapsulation, etc. are all different.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Applicability extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Access Method used to convey management objects that are associated (or
	 * may be associated -- which is why this attribute exists in both places)
	 * with this version.
	 */
	@Enumerated(EnumType.STRING)
	private AccessMethod accessMethod = null;

	/**
	 * The list of serviceElementTypeId objects that support the access method
	 * contained in theServiceElementManagementObject associated with this
	 * instance.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> serviceElementTypes = null;

	/**
	 * The list of management objects that this Applicability object applies to.
	 * All management objects must be of the same access method type.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> managementObjects = null;

	/**
	 * 
	 */
	public Applicability() {

	}

	/**
	 * @return the accessMethod
	 */
	public AccessMethod getAccessMethod() {
		return accessMethod;
	}

	/**
	 * @param accessMethod
	 *            the accessMethod to set
	 */
	public void setAccessMethod(AccessMethod accessMethod) {
		this.accessMethod = accessMethod;
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
	 * @return the managementObjects
	 */
	public List<ID> getManagementObjects() {
		return managementObjects;
	}

	/**
	 * @param managementObjects
	 *            the managementObjects to set
	 */
	public void setManagementObjects(List<ID> managementObjects) {
		this.managementObjects = managementObjects;
	}

}
