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

package edu.harvard.integer.service.security;

import javax.ejb.Local;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.filter.IntegerFilter;
import edu.harvard.integer.common.security.DirectUserLogin;
import edu.harvard.integer.common.security.IntegerSession;
import edu.harvard.integer.common.security.UserLogin;
import edu.harvard.integer.common.user.AccessPolicy;

/**
 * The security and audit manager is responsible for managing the users and
 * access policy's in the system.
 * 
 * @author David Taylor
 * 
 */
@Local
public interface SecurityAndAuditManagerInterface {

	public IntegerSession loginUser(UserLogin login) throws IntegerException;

	/**
	 * Update or save the AccessPolicy.
	 * 
	 * @param accessPolicy
	 *            that is to be updated or saved
	 * @return AccessPolicy that has been saved in the database.
	 * @throws IntegerException
	 */
	AccessPolicy updateAccessPolicy(AccessPolicy accessPolicy)
			throws IntegerException;

	/**
	 * Delete the access policy.
	 * 
	 * @param accessPolicy
	 * @throws IntegerException
	 */
	void deleteAccessPolicy(AccessPolicy accessPolicy) throws IntegerException;

	/**
	 * Add a user that can login directly bypassing the CAS authentication.
	 * 
	 * @param user
	 *            . To be added.
	 * @return The direct user.s
	 * @throws IntegerException
	 */
	DirectUserLogin addDirectUser(DirectUserLogin user) throws IntegerException;

	/**
	 * Delete the direct user. After this call the user will no longer be able
	 * to login to the server
	 * 
	 * @param user do delete/remove from the ability to login directly.
	 * @throws IntegerException
	 */
	void deleteDirectUser(DirectUserLogin user) throws IntegerException;

	/**
	 * Get a list of all direct login users.
	 * @return List of DirectUserLogins.
	 * 
	 * @throws IntegerException
	 */
	DirectUserLogin[] getAllDirectUsers() throws IntegerException;

}
