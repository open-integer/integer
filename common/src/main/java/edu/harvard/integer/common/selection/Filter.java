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

package edu.harvard.integer.common.selection;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.topology.CriticalityEnum;

/**
 * A filter is the way users tell the system the range of ServiceElements they
 * want to select for some purpose.
 * 
 * <p>Note that the selections attributes (e.g., technology, provider, etc.) are
 * AND. For example if the technology is routing AND a provider is acme service
 * company, then only service elements from the acme service company will be the
 * output of the filter. If one of the attributes like location or service is
 * null, then it is as if the user had selected all possibilities to which they
 * have access.
 * 
 * <P.Also not that within a selector such as technology, users are able to select
 * multiples. This means that they could select routing and DNS. In this event,
 * these are treated as OR.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class Filter extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Description of the filter.
	 */
	private String description = null;

	/**
	 * User that created the filter.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "userId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "userType")),
			@AttributeOverride(name = "name", column = @Column(name = "userName")) })
	private ID userId = null;

	/**
	 * Date and time the filter was created.
	 */
	private Date created = null;

	/**
	 * User that made the last modification.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "modifiedById")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "modifiedByType")),
			@AttributeOverride(name = "name", column = @Column(name = "modifiedByName")) })
	private ID modifiedBy = null;

	/**
	 * Date of last modification to the filter.
	 */
	private Date lastModifyed = null;

	/**
	 * List of service elements providing (business services). This should not
	 * be confused with a TechnologyService. Those are specified in the
	 * technologyList. This attribute restricts the filter to those
	 * serviceElements that have been tagged as supporting a specific business
	 * service like payroll.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> services = null;

	/**
	 * List of technologies included in this filter.
	 */
	@ElementCollection
	@JoinTable(name="FilterTechnologies")
	@OrderColumn(name = "idx")
	private List<FilterNode> technologies = null;

	/**
	 * The list of Categories to include in this filter.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")	
	private List<FilterNode> categories = null;
	
	/**
	 * Providers selects for the filter.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> providers = null;

	/**
	 * Only systems that have criticality values specified will be included.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	@Enumerated(EnumType.STRING)
	private List<CriticalityEnum> criticalities = null;

	/**
	 * List of locations that are included.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> locations = null;

	/**
	 * Restricts the filter to service elements associated with a specific
	 * organization
	 */
	@ElementCollection
	@JoinTable(name="FilterOrginizations")
	@OrderColumn(name = "idx")
	private List<FilterNode> orginizations = null;

	/**
	 * If this is null, the user does not want to include technology links in
	 * the selection. In not, it contains the list of technology links that they
	 * do want included, for example CDP, or OSPF.
	 */
	@ElementCollection
	@JoinTable(name="FilterLinkTechnologies")
	@OrderColumn(name = "idx")
	private List<FilterNode> linkTechnologies = null;

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
	 * @return the userId
	 */
	public ID getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(ID userId) {
		this.userId = userId;
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
	 * @return the modifiedBy
	 */
	public ID getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(ID modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifyed
	 */
	public Date getLastModifyed() {
		return lastModifyed;
	}

	/**
	 * @param modifyed
	 *            the modifyed to set
	 */
	public void setLastModifyed(Date modifyed) {
		this.lastModifyed = modifyed;
	}

	/**
	 * @return the technologies
	 */
	public List<FilterNode> getTechnologies() {
		return technologies;
	}

	/**
	 * @param technologies
	 *            the technologies to set
	 */
	public void setTechnologies(List<FilterNode> technologies) {
		this.technologies = technologies;
	}

	/**
	 * @return the providers
	 */
	public List<ID> getProviders() {
		return providers;
	}

	/**
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(List<ID> providers) {
		this.providers = providers;
	}

	/**
	 * @return the criticalities
	 */
	public List<CriticalityEnum> getCriticalities() {
		return criticalities;
	}

	/**
	 * @param criticalities
	 *            the criticalities to set
	 */
	public void setCriticalities(List<CriticalityEnum> criticalities) {
		this.criticalities = criticalities;
	}

	/**
	 * @return the locations
	 */
	public List<ID> getLocations() {
		return locations;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(List<ID> locations) {
		this.locations = locations;
	}

	/**
	 * @return the services
	 */
	public List<ID> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<ID> services) {
		this.services = services;
	}

	/**
	 * @return the orginizations
	 */
	public List<FilterNode> getOrginizations() {
		return orginizations;
	}

	/**
	 * @param orginizations
	 *            the orginizations to set
	 */
	public void setOrginizations(List<FilterNode> orginizations) {
		this.orginizations = orginizations;
	}

	/**
	 * @return the linkTechnologies
	 */
	public List<FilterNode> getLinkTechnologies() {
		return linkTechnologies;
	}

	/**
	 * @param linkTechnologies
	 *            the linkTechnologies to set
	 */
	public void setLinkTechnologies(List<FilterNode> linkTechnologies) {
		this.linkTechnologies = linkTechnologies;
	}

	/**
	 * @return the categories
	 */
	public List<FilterNode> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<FilterNode> categories) {
		this.categories = categories;
	}

}
