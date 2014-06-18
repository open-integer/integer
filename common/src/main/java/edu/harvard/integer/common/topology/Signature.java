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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * A signature is used to identify a specific service element type. It may take
 * several Signature instances to accurately identify an SET.
 * 
 * Some common instances will have the name of vendor, model, firmware,
 * softwareVersion, featureSet, hardware revision.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Signature extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "semoId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "semoType")),
			@AttributeOverride(name = "name", column = @Column(name = "semoName")) })
	private ID semoId = null;

	@OneToMany
	private List<SignatureValueOperator> valueOperators = null;

	@Enumerated(EnumType.STRING)
	private SignatureTypeEnum signatureType = null;
	
	/**
	 * @return the semoId
	 */
	public ID getSemoId() {
		return semoId;
	}

	/**
	 * @param semoId
	 *            the semoId to set
	 */
	public void setSemoId(ID semoId) {
		this.semoId = semoId;
	}

	/**
	 * @return the valueOperators
	 */
	public List<SignatureValueOperator> getValueOperators() {
		return valueOperators;
	}

	/**
	 * @param valueOperators
	 *            the valueOperators to set
	 */
	public void setValueOperators(List<SignatureValueOperator> valueOperators) {
		this.valueOperators = valueOperators;
	}

	/**
	 * @return the signatureType
	 */
	public SignatureTypeEnum getSignatureType() {
		return signatureType;
	}

	/**
	 * @param signatureType the signatureType to set
	 */
	public void setSignatureType(SignatureTypeEnum signatureType) {
		this.signatureType = signatureType;
	}

}
