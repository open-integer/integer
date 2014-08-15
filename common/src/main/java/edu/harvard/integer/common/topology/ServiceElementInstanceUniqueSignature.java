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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 */
@Entity
public class ServiceElementInstanceUniqueSignature extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Description of this signature for determininging a unique instance of an
	 * instance of a service element instance.
	 */
	private String description = null;

	/**
	 * The id of he ServiceElementType instance to which an instance of this
	 * ServiceElementInstanceUniqueSignature is identified.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElementType")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementName")) })
	private ID serviceElementId = null;

	/**
	 * List of the SEMOs the system will use to uniquely identify an instance of
	 * a service element of the type to which this class is associated.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name="idx")
	@JoinTable(name="ServiceElementInstanceUniqueSignature_SEMO")
	private List<ServiceElementManagementObject> uniqueSemos = null;

	/**
	 * A change rule is used the the system to identify when a service element
	 * instance is considered to be the same serviceElement but is
	 * different/changed in a meaningful way. This is a list of the
	 * serviceElementManagementObjects that are used for comparison from one
	 * discovery run to another.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name="idx")
	@JoinTable(name="ServiceElementInstanceUniqueSignature_ChangeSEMO")
	private List<ServiceElementManagementObject> changeSemos = null;

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
	 * @return the serviceElementId
	 */
	public ID getServiceElementId() {
		return serviceElementId;
	}

	/**
	 * @param serviceElementId
	 *            the serviceElementId to set
	 */
	public void setServiceElementId(ID serviceElementId) {
		this.serviceElementId = serviceElementId;
	}

	/**
	 * @return the uniqueSemos
	 */
	public List<ServiceElementManagementObject> getUniqueSemos() {
		return uniqueSemos;
	}

	/**
	 * @param uniqueSemos
	 *            the uniqueSemos to set
	 */
	public void setUniqueSemos(List<ServiceElementManagementObject> uniqueSemos) {
		this.uniqueSemos = uniqueSemos;
	}

	/**
	 * @return the changeSemos
	 */
	public List<ServiceElementManagementObject> getChangeSemos() {
		return changeSemos;
	}

	/**
	 * @param changeSemos
	 *            the changeSemos to set
	 */
	public void setChangeSemos(List<ServiceElementManagementObject> changeSemos) {
		this.changeSemos = changeSemos;
	}

}
