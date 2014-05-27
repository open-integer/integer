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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;

import edu.harvard.integer.common.ID;

/**
 * Hold device details. This is used to pass the device details up to the GUI.
 * 
 * 
 * @author David Taylor
 * 
 */
public class DeviceDetails implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private ID serviceElementId = null;

	private String description = null;

	// The id of the id object instance that identifies the Location instance to
	// which this ServiceElement belongs.
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "primaryLocationId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "primaryLocationType")),
			@AttributeOverride(name = "name", column = @Column(name = "primaryLocationName")) })
	private ID primaryLocation = null;

	// The id of the id object associated with the Organization instance
	// responsible for the operational control of this service element. Ideally,
	// this would be hierarchical a follow a simple containment model; however
	// this is not always the case. For example a router at an interchange that
	// has many interfaces. Only some of which may be under the operational
	// control of one customer, while others may be under the control of others.
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "operationalControlId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "operationalControlType")),
			@AttributeOverride(name = "name", column = @Column(name = "operationalControlName")) })
	private ID operationalControlId = null;

	/*
	 * It is not always easy to algorithmically determine the criticality of
	 * every serviceElement in the environment. For example, a server providing
	 * functions to many systems versus one providing support to only a few, or
	 * a network element that is the main gateway to the Internet versus one
	 * routing traffic to a small building.
	 * 
	 * Where this value is populated via software or manually, this attribute
	 * allows the system to make decisions about access to the device,
	 * processing events and alarms, reporting, etc.
	 * 
	 * The higher the value, the more critical the ServiceElement is.
	 */
	private int serviceElementCriticality = 0;

	/**
	 * Date and time the service element was created, either by the discovery
	 * system or by hand.
	 */
	private Date created = null;

	/**
	 * Date and time this object was last modified, either by the discovery
	 * system or by hand.
	 */
	private Date updated = null;

	/**
	 * Any arbitrary comment the user would like.
	 */
	private String comment = null;

	/**
	 * @return the serviceElementId
	 */
	public ID getServiceElementId() {
		return serviceElementId;
	}

	/**
	 * @param serviceElementId
	 *            the serviceElementId to set
	 */
	public void setServiceElementId(ID serviceElementId) {
		this.serviceElementId = serviceElementId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the primaryLocation
	 */
	public ID getPrimaryLocation() {
		return primaryLocation;
	}

	/**
	 * @param primaryLocation
	 *            the primaryLocation to set
	 */
	public void setPrimaryLocation(ID primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	/**
	 * @return the operationalControlId
	 */
	public ID getOperationalControlId() {
		return operationalControlId;
	}

	/**
	 * @param operationalControlId
	 *            the operationalControlId to set
	 */
	public void setOperationalControlId(ID operationalControlId) {
		this.operationalControlId = operationalControlId;
	}

	/**
	 * @return the serviceElementCriticality
	 */
	public int getServiceElementCriticality() {
		return serviceElementCriticality;
	}

	/**
	 * @param serviceElementCriticality
	 *            the serviceElementCriticality to set
	 */
	public void setServiceElementCriticality(int serviceElementCriticality) {
		this.serviceElementCriticality = serviceElementCriticality;
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
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
