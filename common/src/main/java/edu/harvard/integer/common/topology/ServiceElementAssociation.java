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
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;

/**
 * @author dchan
 *  
 * Service Element Association type between two different service element.
 */
@Entity
public class ServiceElementAssociation extends BaseEntity {

	/**
	 * Serialization version.
	 */
	private static final long serialVersionUID = 1L;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElement")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementName")) })
	private ID serviceElementId = null;
	

	/**
	 * A listing of the values of the attributes collected by the discovery
	 * program.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ManagementObjectValue<?>> attributeValues = null;
	

	/**
	 * A listing of all the ServiceElementProtocolInstanceIdentifier instances
	 * for this service element.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name = "idx")
	@CollectionTable(name = "ServiceElementAssociation_Values")
	private List<ServiceElementProtocolInstanceIdentifier> values = null;



	public List<ManagementObjectValue<?>> getAttributeValues() {
		return attributeValues;
	}


	public void setAttributeValues(List<ManagementObjectValue<?>> attributeValues) {
		this.attributeValues = attributeValues;
	}


	public List<ServiceElementProtocolInstanceIdentifier> getValues() {
		return values;
	}


	public void setValues(List<ServiceElementProtocolInstanceIdentifier> values) {
		this.values = values;
	}

	
	public ID getServiceElementId() {
		return serviceElementId;
	}


	public void setServiceElementId(ID serviceElementId) {
		this.serviceElementId = serviceElementId;
	}

	
}
