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

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * A business service is the highest level of abstraction in this hierarchy and
 * is the think that is most visible to customers. Examples include Payroll,
 * Personnel, Learning Management, and student information. These services will
 * be comprised of potentially many technologies required to realize the
 * service.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Service extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The date this service instance was created.
	 */
	private Date created = null;

	/**
	 * Date modified. This is the date this instance of this service class was
	 * modified.
	 */
	private Date lastModified = null;

	/**
	 * Services on whose functionin this service depends. This is a list of the
	 * ids of those services.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> dependsOn = null;

	/**
	 * A brief tesctual description of the service instance. The system will use
	 * 'template' objects from which additional service instances may be
	 * created.
	 */
	private String description = null;

	/**
	 * Technology's that make up this business service
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> technologies = null;

	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> userServices = null;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<ID> providerServices = null;

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
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the dependsOn
	 */
	public List<ID> getDependsOn() {
		return dependsOn;
	}

	/**
	 * @param dependsOn
	 *            the dependsOn to set
	 */
	public void setDependsOn(List<ID> dependsOn) {
		this.dependsOn = dependsOn;
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
	 * @return the userServices
	 */
	public List<ID> getUserServices() {
		return userServices;
	}

	/**
	 * @param userServices the userServices to set
	 */
	public void setUserServices(List<ID> userServices) {
		this.userServices = userServices;
	}

	/**
	 * @return the providerServices
	 */
	public List<ID> getProviderServices() {
		return providerServices;
	}

	/**
	 * @param providerServices the providerServices to set
	 */
	public void setProviderServices(List<ID> providerServices) {
		this.providerServices = providerServices;
	}

}
