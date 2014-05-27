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

package edu.harvard.integer.common.technology;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 *         This class which can contain parents and children is used to organize
 *         details of a particular technology. For example in the routing
 *         technology area, BGP might be a mail element followed by information
 *         about neighbors, and then more detailed information about them. Users
 *         can define as many levels/refinements as they find useful. Some
 *         technologies will have very short hierarchies. For example specific
 *         technologies for redundancy of one type or another (e.g., RAID or
 *         VRRP).
 */
@Entity
public class Technology extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A short description of the ServiceTechnology
	 */
	private String description = null;

	/**
	 * Child technologies
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name="idx")
	private List<ID> technologies = null;

	/**
	 * Mechanisms for this technology. The technology can only have mechanisms
	 * for the lowest technology. So the technologies list must be empty to have
	 * mechanisms
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name="idx")
	private List<ID> mechanisims = null;

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
	 * @return the technologies
	 */
	public List<ID> getTechnologies() {
		return technologies;
	}

	/**
	 * @param technologies
	 *            the technologies to set
	 */
	public void setTechnologies(List<ID> technologies) {
		this.technologies = technologies;
	}

	/**
	 * @return the mechanisims
	 */
	public List<ID> getMechanisims() {
		return mechanisims;
	}

	/**
	 * @param mechanisims
	 *            the mechanisims to set
	 */
	public void setMechanisims(List<ID> mechanisims) {
		this.mechanisims = mechanisims;
	}

}
