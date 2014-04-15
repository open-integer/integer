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

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.schedule.CalendarPolicy;

/**
 * @author David Taylor
 * 
 *         A discovery rule identifies the type of discovery to be performed
 *         along with the calendar frequency.
 */
@Entity
public class DiscoveryRule extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A list of the calendar policies associated with this discovery rule.
	 */
	@ManyToMany
	private List<CalendarPolicy> calendars = null;

	/**
	 * Discovery type is the type(s) of discovery to perform. For example
	 * service element only, topology only or both. Topology will always include
	 * topology modifiers such as a load balancer, or NAT or global site
	 * selector.
	 */
	@Enumerated(EnumType.STRING)
	private DiscoveryTypeEnum discoveryType = null;

	/**
	 * List of the Topology Seeds used for an instance of this rule.
	 */
	@ManyToMany
	private List<IpTopologySeed> topologySeeds = null;

	/**
	 * Date and time the DiscoveryRule was created.
	 */
	private Date created = null;

	/**
	 * Date and time the DiscoveryRule was last modified.
	 */
	private Date modified = null;

	/**
	 * 
	 */
	public DiscoveryRule() {

	}

	/**
	 * @return the calendars
	 */
	public List<CalendarPolicy> getCalendars() {
		return calendars;
	}

	/**
	 * @param calendars
	 *            the calendars to set
	 */
	public void setCalendars(List<CalendarPolicy> calendars) {
		this.calendars = calendars;
	}

	/**
	 * @return the discoveryType
	 */
	public DiscoveryTypeEnum getDiscoveryType() {
		return discoveryType;
	}

	/**
	 * @param discoveryType
	 *            the discoveryType to set
	 */
	public void setDiscoveryType(DiscoveryTypeEnum discoveryType) {
		this.discoveryType = discoveryType;
	}

	/**
	 * @return the topologySeeds
	 */
	public List<IpTopologySeed> getTopologySeeds() {
		return topologySeeds;
	}

	/**
	 * @param topologySeeds
	 *            the topologySeeds to set
	 */
	public void setTopologySeeds(List<IpTopologySeed> topologySeeds) {
		this.topologySeeds = topologySeeds;
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

}
