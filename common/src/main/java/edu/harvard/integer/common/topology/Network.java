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
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;

/**
 * A network object is the sum of all the NetworkInfo, InterDeviceLink, and Path
 * information within its domain.
 * <p>
 * It describes any layer of the network hierarchy down to the physical level.
 * At the physical level, 'discovery' will be limited.
 * <p>
 * It will contain, potentially many adjacencies.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Network extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private String description = null;
	
	private Date created = null;
	
	private Date modified = null;

	@Enumerated(EnumType.STRING)
	private LayerTypeEnum layer = null;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<InterDeviceLink> interDeviceLinks = null;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<ServiceElement> serviceElements = null;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Network> lowerNetworks = null;
	
	private Boolean reachable = null;

	@Embedded
	private Address address = null;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
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
	 * @param modified the modified to set
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
	 * @param layer the layer to set
	 */
	public void setLayer(LayerTypeEnum layer) {
		this.layer = layer;
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

	/**
	 * @return the serviceElements
	 */
	public List<ServiceElement> getServiceElements() {
		return serviceElements;
	}

	/**
	 * @param serviceElements the serviceElements to set
	 */
	public void setServiceElements(List<ServiceElement> serviceElements) {
		this.serviceElements = serviceElements;
	}

	/**
	 * @return the lowerNetworks
	 */
	public List<Network> getLowerNetworks() {
		return lowerNetworks;
	}

	/**
	 * @param lowerNetworks the lowerNetworks to set
	 */
	public void setLowerNetworks(List<Network> lowerNetworks) {
		this.lowerNetworks = lowerNetworks;
	}

	/**
	 * @return the reachable
	 */
	public Boolean getReachable() {
		return reachable;
	}

	/**
	 * @param reachable the reachable to set
	 */
	public void setReachable(Boolean reachable) {
		this.reachable = reachable;
	}

	/**
	 * @param sourceAddress
	 * @return
	 */
	public static String createName(Address sourceAddress) {
		return "0.0.0.0";
		
//		if (sourceAddress != null)
//			return Address.getSubNet(sourceAddress.getAddress(), sourceAddress.getMask());
//		else
//			return "N/A";
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

}
