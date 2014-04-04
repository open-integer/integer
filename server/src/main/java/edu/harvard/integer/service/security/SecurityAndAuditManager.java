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

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.UserErrorCodes;
import edu.harvard.integer.common.filter.IntegerFilter;
import edu.harvard.integer.common.security.DirectUserLogin;
import edu.harvard.integer.common.security.IntegerSession;
import edu.harvard.integer.common.security.UserLogin;
import edu.harvard.integer.common.user.AccessPolicy;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.security.DirectUserLoginDAO;
import edu.harvard.integer.service.persistance.dao.user.AccessPolicyDAO;

/**
 * @author David Taylor
 * 
 *         The security and audit manager has a number of functions:
 * 
 *         1. It is used to 'connect' the IMS to external IAM systems that may
 *         be used for user authentication, for this system or managed
 *         ServiceElements.
 * 
 *         2. It controls access to the data and various services in the system.
 * 
 *         3. Provides audit and log functions related to access/security. The
 *         system will use general log facilities for normal log functions (the
 *         low level implementation of this service will also use secured
 *         general logging approaches).
 * 
 *         4. It creates AccessPolicy instances and associates them with roles
 *         and also manages credentials for ServiceElements.
 */
@Stateless
public class SecurityAndAuditManager implements
		SecurityAndAuditManagerInterface {

	@Inject
	private Logger logger;
	
	@Inject 
	private PersistenceManagerInterface persistenceManager;
	
	@Inject
	private SecurityServiceInterface securityService;
	
	@Override
	public AccessPolicy updateAccessPolicy(AccessPolicy accessPolicy) throws IntegerException {
		AccessPolicyDAO dao = persistenceManager.getAccessPolicyDAO();
		
		accessPolicy = dao.update(accessPolicy);
		
		if (logger.isDebugEnabled())
			logger.debug("Update AccessPolicy " + accessPolicy);
		
		return accessPolicy;
	}
	
	@Override
	public void deleteAccessPolicy(AccessPolicy accessPolicy) throws IntegerException {
		 AccessPolicyDAO dao = persistenceManager.getAccessPolicyDAO();
		
		if (logger.isDebugEnabled())
			logger.debug("Delete AccessPolicy " + accessPolicy);
		
		dao.delete(accessPolicy);
	}
	
	@Override
	public AccessPolicy[] findAccessPolicies(IntegerFilter filter) throws IntegerException {
		AccessPolicyDAO dao = persistenceManager.getAccessPolicyDAO();
		
		return dao.findAll();
	}
	
	@Override
	public IntegerSession loginUser(UserLogin login) throws IntegerException {
		
		if (login instanceof DirectUserLogin) {
			DirectUserLoginDAO dao = persistenceManager.getDirectUserLoginDAO();
			DirectUserLogin directLogin = dao.findByName(login.getName());
			if (directLogin == null)
				throw new IntegerException(null, UserErrorCodes.UserNamePasswordNotValid);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Login User " + login.getName() + " from " + login.getAddress());
		
		IntegerSession session = new IntegerSession();
		session.setSessionId(securityService.getNextLoginId(login));
		
		return session;
	}
	
	@Override
	public DirectUserLogin addDirectUser(DirectUserLogin user) throws IntegerException {
		DirectUserLoginDAO dao = persistenceManager.getDirectUserLoginDAO();
		
		user = dao.update(user);
		
		return user;
	}
	
	@Override
	public void deleteDirectUser(DirectUserLogin user) throws IntegerException {
		DirectUserLoginDAO dao = persistenceManager.getDirectUserLoginDAO();
		
		dao.delete(user);
	}
	
	@Override
	public DirectUserLogin[] getAllDirectUsers() throws IntegerException {
		DirectUserLoginDAO dao = persistenceManager.getDirectUserLoginDAO();
		
		return dao.findAll();
	}
}
