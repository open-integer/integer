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

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 */
@Entity
public class InterDeviceLink extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "address", column = @Column(name = "sourceAddress")),
			@AttributeOverride(name = "mask", column = @Column(name = "sourceMask")) })
	private Address sourceAddress = null;
	
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "sourceServiceElementId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "sourceServiceElementType")),
			@AttributeOverride(name = "name", column = @Column(name = "sourceServiceElementName")) })
	private ID sourceServiceElementId = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "sourceNetworkId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "sourceNetworkType")),
			@AttributeOverride(name = "name", column = @Column(name = "sourceNetworkName")) })
	private ID sourceNetworkId = null;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "address", column = @Column(name = "destinationAddress")),
		@AttributeOverride(name = "mask", column = @Column(name = "destinationMask")) })
	private Address destinationAddress = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "destinationServiceElementId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "cdestinationServiceElementType")),
			@AttributeOverride(name = "name", column = @Column(name = "destinationServiceElementName")) })
	private ID destinationServiceElementId = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "destinationNetworkd")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "cdestinationNetworkType")),
			@AttributeOverride(name = "name", column = @Column(name = "destinationNetworkName")) })
	private ID destinationNetworkId = null;
	
	private Date created = null;

	private Date modified = null;

	@Enumerated(EnumType.STRING)
	private LayerTypeEnum layer = null;

	/**
	 * @return the sourceAddress
	 */
	public Address getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * @param sourceAddress
	 *            the sourceAddress to set
	 */
	public void setSourceAddress(Address sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * @return the destinationAddress
	 */
	public Address getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * @param destinationAddress
	 *            the destinationAddress to set
	 */
	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the layer
	 */
	public LayerTypeEnum getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(LayerTypeEnum layer) {
		this.layer = layer;
	}

	/**
	 * @return the sourceServiceElementId
	 */
	public ID getSourceServiceElementId() {
		return sourceServiceElementId;
	}

	/**
	 * @param sourceServiceElementId the sourceServiceElementId to set
	 */
	public void setSourceServiceElementId(ID sourceServiceElementId) {
		this.sourceServiceElementId = sourceServiceElementId;
	}

	/**
	 * @return the sourceNetworkId
	 */
	public ID getSourceNetworkId() {
		return sourceNetworkId;
	}

	/**
	 * @param sourceNetworkId the sourceNetworkId to set
	 */
	public void setSourceNetworkId(ID sourceNetworkId) {
		this.sourceNetworkId = sourceNetworkId;
	}

	/**
	 * @return the destinationServiceElementId
	 */
	public ID getDestinationServiceElementId() {
		return destinationServiceElementId;
	}

	/**
	 * @param destinationServiceElementId the destinationServiceElementId to set
	 */
	public void setDestinationServiceElementId(
			ID destinationServiceElementId) {
		this.destinationServiceElementId = destinationServiceElementId;
	}

	/**
	 * @return the destinationNetworkId
	 */
	public ID getDestinationNetworkId() {
		return destinationNetworkId;
	}

	/**
	 * @param destinationNetworkId the destinationNetworkId to set
	 */
	public void setDestinationNetworkId(ID destinationNetworkId) {
		this.destinationNetworkId = destinationNetworkId;
	}

}
