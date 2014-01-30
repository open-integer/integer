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

package edu.harvard.integer.manager.user;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.user.Contact;
import edu.harvard.integer.common.user.User;
import edu.harvard.integer.database.DatabaseManager;

/**
 * The user contact and role manager controls addition, creation and modification of users and contacts in the system.
 *
 * It allows for automatic updates from external sources for many attributes found in Contact object.
 *
 * It also manages role definition and association with Access Policies. 
 *
 *
 * First, it allows users and contacts to be created in the system with, or to use and external IAM system for 
 * user authentication. A single user can be authenticated by only one source.
 *
 * Secondly, this system is used to manage credentials for each of the managed ServiceElements in the environment. 
 * In some cases, external systems (as with users) could be used like TACCAS+, though this is not a requirement.
 *
 * This service is also responsible for the assignment of roles to users such as administrator or user. Note that 
 * there is a facility to define different roles for the integrated management system and different devices since
 * not all users will have the same access to different devices. 
 */
/**
 * @author David Taylor
 * 
 */
@Stateless
public class UserManager {
	@Inject
	private Logger logger;
	
	@Inject
	private DatabaseManager dbm;
	
	public User addUser(User user) throws IntegerException {
		logger.info("Add User (" + user.getAlias() + ") " + user.getFirstName() + " " + user.getLastName());
		dbm.update(user);
		return user;
	}
	
	public User modifyUser(User user)  throws IntegerException {
		logger.info("Modify User (" + user.getAlias() + ") " + user.getFirstName() + " " + user.getLastName());
		return user;
	}
	
	public void deleteUser(User user)  throws IntegerException {
		logger.info("Delete User (" + user.getAlias() + ") " + user.getFirstName() + " " + user.getLastName());
	}

	public User[] getAllUsers() throws IntegerException {
		IDType type = new IDType();
		type.setClassType(User.class);
	
		return dbm.findAll(type);
	}
	
	public Contact addContact(Contact contact)  throws IntegerException {
		return contact;
	}
	
	public Contact modifyContact(Contact contact)  throws IntegerException {
		return contact;
	}
	
	public void deleteContact(Contact contact)  throws IntegerException {
		
	}
	
}
