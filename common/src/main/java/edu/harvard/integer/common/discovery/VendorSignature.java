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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.common.topology.SignatureValueOperator;

/**
 * This class can be used to identify a range of systems whose containment may
 * be learned with the associated VendorContainmentSelector.
 * <p>
 * Note that it may take several of these VendorAndSNMPSignature instances to
 * fully express the range of systems the VendorContainmentSelector.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class VendorSignature extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This carries the type of restriction in this signature. Values are:
	 * Vendor, Model Firmware Revision Software Revision Feature Set Others may
	 * be added as needed.
	 */
	@Enumerated(EnumType.STRING)
	private VendorSignatureTypeEnum signatureType = null;

	/**
	 * Since it may be necessary to represent a range of ranges e.g., a list of
	 * OSs, this is a list. For example 12.2. - 12.4 and greater than 11.a. but
	 * less than 12.2. Equals Is not equal Is less than is greater than Is less
	 * than or equal Is greater than or equal SNMP OID Range - this is specified
	 * by indicating the starting base OID of the range.
	 */
	@ManyToOne
	private SignatureValueOperator valueOperator = null;

	/**
	 * @return the signatureType
	 */
	public VendorSignatureTypeEnum getSignatureType() {
		return signatureType;
	}

	/**
	 * @param signatureType
	 *            the signatureType to set
	 */
	public void setSignatureType(VendorSignatureTypeEnum signatureType) {
		this.signatureType = signatureType;
	}

	/**
	 * @return the valueOperators
	 */
	public SignatureValueOperator getValueOperator() {
		return valueOperator;
	}

	/**
	 * @param valueOperators
	 *            the valueOperators to set
	 */
	public void setValueOperator(SignatureValueOperator valueOperators) {
		this.valueOperator = valueOperators;
	}

}
