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
package edu.harvard.integer.common.user;

import java.util.List;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * @author David Taylor
 * 
 *         Every system user will have a User instance. Additionally, locations
 *         and many other objects in the system may also have users associated
 *         with them.
 */
@Entity
public class User extends BaseEntity {
	/**
	 * Serializtion ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The contact ID
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "contactId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "contactType")),
			@AttributeOverride(name = "name", column = @Column(name = "contactName")) })
	private ID contactId = null;

	/**
	 * Users can be members of multiple organizations. This is a listing of the
	 * ids of the id objects for each organization they belong to.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> orginizations = null;

	/**
	 * This should not be confused wtih the internal id or idId attribes. This
	 * is a uuid.
	 */
	private UUID uuid = null;

	/**
	 * Organizations may differ in the format of unique identifiers used for
	 * individuals. This attribute may be used in those cases where UUID is not
	 * avialable.
	 */
	private String otherId = null;

	private String firstName = null;

	private String lastName = null;

	private String middleName = null;

	/**
	 * The alias is the 'screen' name like, Bob S. or Barbara H.
	 */
	private String alias = null;

	/**
	 * List of roles associated with this user.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> roles = null;

	/**
	 * Users can be members of multiple organizations. This is a listing of the
	 * IDs of the id objects for each organization they belong to.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> organizations = null;

	/**
	 * @return the identifier
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the contactId
	 */
	public ID getContactId() {
		return contactId;
	}

	/**
	 * @param contactId
	 *            the contactId to set
	 */
	public void setContactId(ID contactId) {
		this.contactId = contactId;
	}

	/**
	 * @return the orginizationList
	 */
	public List<ID> getOrginizations() {
		return orginizations;
	}

	/**
	 * @param orginizationList
	 *            the orginizationList to set
	 */
	public void setOrginizations(List<ID> orginizations) {
		this.orginizations = orginizations;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the otherId
	 */
	public String getOtherId() {
		return otherId;
	}

	/**
	 * @param otherId
	 *            the otherId to set
	 */
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	/**
	 * @return the roles
	 */
	public List<ID> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<ID> roles) {
		this.roles = roles;
	}
}
