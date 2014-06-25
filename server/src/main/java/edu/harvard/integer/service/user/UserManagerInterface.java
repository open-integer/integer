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

package edu.harvard.integer.service.user;

import javax.ejb.Local;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.user.AccessPolicy;
import edu.harvard.integer.common.user.Contact;
import edu.harvard.integer.common.user.Location;
import edu.harvard.integer.common.user.Organization;
import edu.harvard.integer.common.user.Role;
import edu.harvard.integer.common.user.User;
import edu.harvard.integer.common.user.authentication.AuthInfo;

/**
 * * The user contact and role manager controls addition, creation and
 * modification of users and contacts in the system.
 * <p>
 * It allows for automatic updates from external sources for many attributes
 * found in Contact object.
 * <p>
 * It also manages role definition and association with Access Policies.
 * <p>
 * 
 * First, it allows users and contacts to be created in the system with, or to
 * use and external IAM system for user authentication. A single user can be
 * authenticated by only one source.
 * <p>
 * Secondly, this system is used to manage credentials for each of the managed
 * ServiceElements in the environment. In some cases, external systems (as with
 * users) could be used like TACCAS+, though this is not a requirement.
 * <p>
 * This service is also responsible for the assignment of roles to users such as
 * administrator or user. Note that there is a facility to define different
 * roles for the integrated management system and different devices since not
 * all users will have the same access to different devices.
 * 
 * 
 * @author David Taylor
 * 
 */
@Local
public interface UserManagerInterface {
	
	/**
	 * Add one user to the system. The user will be saved into the database. All
	 * setup for the user will be done after the call completes.
	 * 
	 * TODO: add initialization steps once known what the initialization steps
	 * are.
	 * 
	 * @param user
	 * @return User. The newly created user.
	 * @throws IntegerException
	 */
	public User addUser(User user) throws IntegerException;

	/**
	 * 
	 * Modify the user.
	 * 
	 * @param user
	 * @return User. The modified user.
	 * @throws IntegerException
	 */
	public User modifyUser(User user) throws IntegerException;

	/**
	 * Delete the user. This will clean up any data associated with the user.
	 * 
	 * 
	 * @param user
	 * @throws IntegerException
	 */

	public void deleteUser(User user) throws IntegerException;

	/**
	 * Return a list of all users.
	 * 
	 * @return User[]. All users.
	 * @throws IntegerException
	 */
	public User[] getAllUsers() throws IntegerException;

	/**
	 * Get a HTML String that can be used for display. 
	 * @return HTML Sting of users in the system.s
	 * @throws IntegerException
	 */
	public String showUsers() throws IntegerException;

	/**
	 * Update/save the Contact in the database
	 * @param contact. Contact to save
	 * @return Contact that has been saved.
	 * @throws IntegerException
	 */
	Contact updateContact(Contact contact) throws IntegerException;

	/**
	 * Delete the Contact
	 * @param contact. Contact to deleted.
	 * @throws IntegerException
	 */
	void deleteContact(Contact contact) throws IntegerException;

	/**
	 * Update/save the Organization in the database.
	 * @param organization. Origanization to update.
	 * 
	 * @return Organization. The updated Organization.
	 * @throws IntegerException
	 */
	Organization updateOrganization(Organization organization)
			throws IntegerException;

	/**
	 * Delete the Organization with the given ID.
	 * @param id. ID of the Organization to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteOrganization(ID id) throws IntegerException;

	/**
	 * Delete the Organization.
	 * @param organization. Organization to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteOrganization(Organization organization) throws IntegerException;

	/**
	 * Update the AccessPolicy in the database.
	 * @param policy. AccessPolcy to be updated
	 * @return AccessPolicy. The updated AccessPolicy.
	 * @throws IntegerException
	 */
	AccessPolicy updateAccessPolicy(AccessPolicy policy)
			throws IntegerException;

	/**
	 * Delete the AccessPolicy with the given ID.
	 * @param id. ID of the AccessPolicy to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteAccessPolicy(ID id) throws IntegerException;

	/**
	 * Delete the AccessPolicy.
	 * @param policy. AccessPolicy to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteAccessPolicy(AccessPolicy policy) throws IntegerException;

	/**
	 * Update/save the AuthInfo in the database.
	 * 
	 * @param authInfo. AuthInfo to update.
	 * @return AuthInfo. The updated AuthInfo
	 * 
	 * @throws IntegerException
	 */
	AuthInfo updateAuthInfo(AuthInfo authInfo) throws IntegerException;

	/**
	 * Update/save the Location in the database.
	 * @param location. Location to be updated.
	 * @return Location. The update Location.
	 * 
	 * @throws IntegerException
	 */
	Location updateLocation(Location location) throws IntegerException;

	/**
	 * Delete the Location with the give ID.
	 * 
	 * @param id. ID of the Location to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteLocation(ID id) throws IntegerException;

	/**
	 * Update/save the Role in the database.
	 * @param role. Role to be updated.
	 * @return Role. The updated Role
	 * @throws IntegerException
	 */
	Role updateRole(Role role) throws IntegerException;

}
