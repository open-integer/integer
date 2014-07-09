/*
 *  Copyright (c) 2013 Harvard University and the persons
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

/**
 * @author David Taylor
 *
 */
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * TopologyElement instances provide details for each ServiceElement about its
 * addresses, address types and layer of the network hierarchy in which they
 * exist.
 */
@Entity
public class TopologyElement extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private Date created = null;

	private Date modified = null;

	private String layer = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "serviceElementId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "serviceElementType")),
			@AttributeOverride(name = "name", column = @Column(name = "serviceElementName")) })
	private ID serviceElementId = null;
	
	@ManyToMany
	private List<InterDeviceLink> interDeviceLinks = null;
	
	/**
	 * Address is separate from layer since a layer like IP might have different
	 * address types, e.g., IPv4 or IPv6. A topology element may exist at only
	 * one layer. Multiple addresses are possible for an element at a given
	 * layer.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<Address> address = null;

	/**
	 * @return the address
	 */
	public List<Address> getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(List<Address> address) {
		this.address = address;
	}

	/**
	 * @return the layer
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
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
	 * @return the serviceElementId
	 */
	public ID getServiceElementId() {
		return serviceElementId;
	}

	/**
	 * @param serviceElementId the serviceElementId to set
	 */
	public void setServiceElementId(ID serviceElementId) {
		this.serviceElementId = serviceElementId;
	}

	/**
	 * @return the interDeviceLinks
	 */
	public List<InterDeviceLink> getInterDeviceLinks() {
		return interDeviceLinks;
	}

	/**
	 * @param interDeviceLinks the interDeviceLinks to set
	 */
	public void setInterDeviceLinks(List<InterDeviceLink> interDeviceLinks) {
		this.interDeviceLinks = interDeviceLinks;
	}
}
