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
 * @author David Taylor
 *
 */
@Local
public interface UserManagerInterface {
	public User addUser(User user) throws IntegerException ;
	public User modifyUser(User user) throws IntegerException;
	public void deleteUser(User user) throws IntegerException;
	public User[] getAllUsers() throws IntegerException;
	
	public String showUsers() throws IntegerException;

	
	/**
	 * @param contact
	 * @return
	 * @throws IntegerException
	 */
	Contact updateContact(Contact contact) throws IntegerException;
	/**
	 * @param contact
	 * @throws IntegerException
	 */
	void deleteContact(Contact contact) throws IntegerException;
	/**
	 * @param organization
	 * @return
	 * @throws IntegerException
	 */
	Organization updateOrganization(Organization organization)
			throws IntegerException;
	/**
	 * @param id
	 * @throws IntegerException
	 */
	void deleteOrganization(ID id) throws IntegerException;
	/**
	 * @param organization
	 * @throws IntegerException
	 */
	void deleteOrganization(Organization organization) throws IntegerException;
	/**
	 * @param policy
	 * @return
	 * @throws IntegerException
	 */
	AccessPolicy updateAccessPolicy(AccessPolicy policy)
			throws IntegerException;
	/**
	 * @param id
	 * @throws IntegerException
	 */
	void deleteAccessPolicy(ID id) throws IntegerException;
	/**
	 * @param policy
	 * @throws IntegerException
	 */
	void deleteAccessPolicy(AccessPolicy policy) throws IntegerException;
	/**
	 * @param authInfo
	 * @return
	 * @throws IntegerException
	 */
	AuthInfo updateAuthInfo(AuthInfo authInfo) throws IntegerException;
	/**
	 * @param location
	 * @return
	 * @throws IntegerException
	 */
	Location updateLocation(Location location) throws IntegerException;
	/**
	 * @param id
	 * @throws IntegerException
	 */
	void deleteLocation(ID id) throws IntegerException;
	/**
	 * @param role
	 * @return
	 * @throws IntegerException
	 */
	Role updateRole(Role role) throws IntegerException;
	
}
