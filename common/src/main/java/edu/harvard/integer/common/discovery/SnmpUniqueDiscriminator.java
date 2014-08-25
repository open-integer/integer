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

import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.snmp.SNMP;


/**
 * The Class SnmpUniqueDriscriminator is used to specify how the top Service Element 
 * unique identify attributes being retrieves.  The uniqueIdentifierSemos should be matched 
 * to subset of uniqueIdentifierCapabilities defined on service element type.
 *
 * @author dchan
 */
@Entity
@DiscriminatorColumn(columnDefinition="varchar(50)")
public class SnmpUniqueDiscriminator extends BaseEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The context oid. */
	@ManyToOne
	private SNMP contextOID = null;
	
	/**
	 * A list of the Capabilities used to uniquely identify the service element
	 * type.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> uniqueIdentifierSemos = null;
	
	/** The Descriminator value. */
	@OneToOne
	private SnmpServiceElementTypeDiscriminatorValue<?> descriminatorValue = null;


	/** The discriminator oid. */
	@ManyToOne
	private SNMP descriminatorOID = null;
	
	

	public SNMP getDescriminatorOID() {
		return descriminatorOID;
	}

	public void setDescriminatorOID(SNMP descriminatorOID) {
		this.descriminatorOID = descriminatorOID;
	}

	/**
	 * Gets the context oid.
	 *
	 * @return the context oid
	 */
	public SNMP getContextOID() {
		return contextOID;
	}

	/**
	 * Sets the context oid.
	 *
	 * @param contextOID the new context oid
	 */
	public void setContextOID(SNMP contextOID) {
		this.contextOID = contextOID;
	}

	/**
	 * Gets the unique identifier semos.
	 *
	 * @return the unique identifier semos
	 */
	public List<ID> getUniqueIdentifierSemos() {
		return uniqueIdentifierSemos;
	}

	/**
	 * Sets the unique identifier semos.
	 *
	 * @param uniqueIdentifierSemos the new unique identifier semos
	 */
	public void setUniqueIdentifierSemos(List<ID> uniqueIdentifierSemos) {
		this.uniqueIdentifierSemos = uniqueIdentifierSemos;
	}
	
	
	public SnmpServiceElementTypeDiscriminatorValue<?> getDescriminatorValue() {
		return descriminatorValue;
	}

	public void setDescriminatorValue(
			SnmpServiceElementTypeDiscriminatorValue<?> descriminatorValue) {
		this.descriminatorValue = descriminatorValue;
	}

}
