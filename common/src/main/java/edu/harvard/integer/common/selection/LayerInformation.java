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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.selection.operator.CapabilityOperator;

/**
 * @author David Taylor
 * 
 */
@Entity
public abstract class LayerInformation<T> extends BaseEntity {
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

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "capabilityId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "capabilityIdType")),
			@AttributeOverride(name = "name", column = @Column(name = "capabilityIdName")) })
	private ID capabilityId = null;

	@ManyToOne
	private CapabilityOperator operator = null;

	@Enumerated(EnumType.STRING)
	private FilterControlEnum filterControl = null;

	/**
	 * @return the value
	 */
	public abstract T getValue();

	/**
	 * @param value
	 *            the value to set
	 */
	public abstract void setValue(T value);

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
	 * @return the lastModifyed
	 */
	public Date getLastModifyed() {
		return lastModifyed;
	}

	/**
	 * @param lastModifyed
	 *            the lastModifyed to set
	 */
	public void setLastModifyed(Date lastModifyed) {
		this.lastModifyed = lastModifyed;
	}

	/**
	 * @return the capabilityId
	 */
	public ID getCapabilityId() {
		return capabilityId;
	}

	/**
	 * @param capabilityId
	 *            the capabilityId to set
	 */
	public void setCapabilityId(ID capabilityId) {
		this.capabilityId = capabilityId;
	}

	/**
	 * @return the operator
	 */
	public CapabilityOperator getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(CapabilityOperator operator) {
		this.operator = operator;
	}

	/**
	 * @return the filterControl
	 */
	public FilterControlEnum getFilterControl() {
		return filterControl;
	}

	/**
	 * @param filterControl
	 *            the filterControl to set
	 */
	public void setFilterControl(FilterControlEnum filterControl) {
		this.filterControl = filterControl;
	}

}
