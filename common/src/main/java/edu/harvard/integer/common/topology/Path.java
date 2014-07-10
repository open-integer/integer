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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;

/**
 * A path is an ordered list of InterDeviceLink instances. For this reason paths
 * will frequently come in pairs for certain types of InterDeviceLinks such as
 * at Layer 3 between end systems. In these cases there will be one from one
 * device to the other and another in the opposite direction.
 * 
 * @author David Taylor
 * 
 */
public class Path extends BaseEntity {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is the ordered list of InterDeviceLink objects that define a
	 * specific path between any source and destination service elements.
	 */
	@Embedded
	@AttributeOverride(name = "address", column = @Column(name = "sourceAddress"))
	private Address sourceAddress = null;

	/**
	 * The destination/end point of the path.
	 */
	@Embedded
	@AttributeOverride(name = "address", column = @Column(name = "destinationAddress"))
	private Address destinationAddress = null;

	private Date created = null;

	private Date modified = null;

	/**
	 * This is the ordered list of InterDeviceLink objects that define a
	 * specific path between any source and destination service elements.
	 */
	private List<InterDeviceLink> interDeviceLinks = null;

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
	 * @return the interDeviceLinks
	 */
	public List<InterDeviceLink> getInterDeviceLinks() {
		return interDeviceLinks;
	}

	/**
	 * @param interDeviceLinks
	 *            the interDeviceLinks to set
	 */
	public void setInterDeviceLinks(List<InterDeviceLink> interDeviceLinks) {
		this.interDeviceLinks = interDeviceLinks;
	}

}
