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

package edu.harvard.integer.common.discovery;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * When the contextOid in the SnmpLevelOID instance that is associated with the
 * SnmpDiscriminatorOID to which this SnmpServiceElementTypeDiscriminator is
 * attached is a table that has an OID that can get a number of return values
 * that indicated different service element types, then we will need as many of
 * these as there are possible return types for that oid.
 * 
 * @author David Taylor
 * 
 * 
 */
@Entity
public class SnmpServiceElementTypeDiscriminator extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The service element type and is uniquely identified by the specific value
	 * that is returned on a get of the contextOid object.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementTypeId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElementTypeType")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementTypeName")) })
	private ID serviceElementTypeId = null;

	/**
	 * The specific value in the returned contextOid that matches the
	 * serviceElementTypeId in an instance of this object
	 */
	@OneToOne
	private SnmpServiceElementTypeDiscriminatorValue<?> discriminatorValue = null;

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
	 * @return the discriminatorValue
	 */
	public SnmpServiceElementTypeDiscriminatorValue<?> getDiscriminatorValue() {
		return discriminatorValue;
	}

	/**
	 * @param discriminatorValue
	 *            the discriminatorValue to set
	 */
	public void setDiscriminatorValue(
			SnmpServiceElementTypeDiscriminatorValue<?> discriminatorValue) {
		this.discriminatorValue = discriminatorValue;
	}

}
