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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.SignatureValueOperator;
import edu.harvard.integer.common.topology.ValueOpertorEnum;

/**
 * 
 * 
 * This object is created by the user. It tells the system which SnmpContainment
 * instance (in the case of SNMP) to use based on a combination of vendor, model
 * firmware and software revision. Not all of these attributes need be filled
 * in.
 * 
 * <p>
 * The SnmpVendorDiscoveryTemplate tells the system where to look on the systems
 * to get the model, firmware and softwareVersion data.
 * 
 * <p>
 * The containment ID will contain the ID of the specific containment object to
 * use for this specific type of system. Other containment types could use SSH
 * or other protocols.
 * 
 * @author David Taylor
 * 
 * 
 */
@Entity
public class VendorContainmentSelector extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A list of signatures that tell the system the range of systems that are
	 * covered by an instance of the VendorContainmentSelector.
	 */
	@ElementCollection
	private List<VendorSignature> signatures = null;

	/**
	 * The idId of the instances of the hardware containment structure class
	 * that tells the discovery system how to learn the structure of
	 * serviceElements from a particular vendors.
	 * 
	 * These values can be retrieved from the database based on the vendor,
	 * model, firmware and software revision information.
	 * 
	 * There are two ways to collect this information. If model, firmware and
	 * software revision OIDs have been populated in an instance of this class,
	 * then the discovery system retrieves that information.
	 * 
	 * If the firmware, software and model information are not populated in an
	 * instance of this class, then the parseStringOid must be present. The
	 * discovery system will use that information to retrieve an SNMP Object
	 * with structured information. It will use the parseSting attribute that
	 * has been configured to parse out the available model, firmware and
	 * software information. Then it can query the database for the correct
	 * containment object instances.
	 * 
	 * It is possible to create an instance of the VendorContainmentSelector
	 * without all of the fields complete, for example without the firmware or
	 * softwareVersion attributed filled.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "containmentId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "containmentType")),
			@AttributeOverride(name = "name", column = @Column(name = "containmentName")) })
	private ID containmentId = null;

	/**
	 * @return the containmentId
	 */
	public ID getContainmentId() {
		return containmentId;
	}

	/**
	 * @param containmentId
	 *            the containmentId to set
	 */
	public void setContainmentId(ID containmentId) {
		this.containmentId = containmentId;
	}

	/**
	 * @return the signatures
	 */
	public List<VendorSignature> getSignatures() {
		return signatures;
	}

	/**
	 * @param signatures
	 *            the signatures to set
	 */
	public void setSignatures(List<VendorSignature> signatures) {
		this.signatures = signatures;
	}

	public void addEqualSignature(VendorSignatureTypeEnum type, String value) {
		if (signatures == null)
			signatures = new ArrayList<VendorSignature>();

		VendorSignature signature = new VendorSignature();
		signature.setName(value);
		signature.setSignatureType(type);

		SignatureValueOperator operator = new SignatureValueOperator();
		operator.setOperator(ValueOpertorEnum.Equal);
		operator.setValue(value);

		signature.setValueOperator(operator);

		signatures.add(signature);
	}
}
