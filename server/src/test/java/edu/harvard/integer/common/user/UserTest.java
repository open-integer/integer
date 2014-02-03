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

package edu.harvard.integer.common.user;

import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.EmailAddress;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDInterface;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.Orginization;
import edu.harvard.integer.common.PhoneNumber;
import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.util.DisplayableInterface;
import edu.harvard.integer.common.util.Resource;
import edu.harvard.integer.service.persistance.PersistenceManager;
import edu.harvard.integer.service.user.UserManager;
import edu.harvard.integer.service.user.UserManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class UserTest {
	
	@Inject 
	private UserManagerInterface userManager;
	
	@Inject
	private Logger logger;
	
	 @Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(Resource.class)
				.addClasses(User.class, BaseEntity.class, IDInterface.class, ID.class, Orginization.class, IDType.class)
				.addClasses(UserManager.class, PersistenceManager.class, ErrorCodeInterface.class, IntegerException.class)
				.addClasses(DisplayableInterface.class, UserManagerInterface.class)
				.addClasses(Contact.class, ContactType.class, EmailAddress.class, PhoneNumber.class)
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml");
	}
	

	@Test
	public void addOneUser() {
		logger.info("Create user dtaylor");
		
		User u = new User();
		u.setName("dtaylor");
		
		IDType userType = new IDType();
		userType.setClassType(User.class);
		u.setIdType(userType);
		u.setFirstName("Dave");
		u.setLastName("Taylor");
		u.setUuid("123456789");
		
		try {
			userManager.addUser(u);
			
		} catch (IntegerException e) {

			e.printStackTrace();
			fail("Error saveing user " + e);
		}
		
	}
	
	@Test
	public void findUser() {
		logger.info("Find users");
		
		addOneUser();
		
		try {
			User[] users = userManager.getAllUsers();

			logger.info("Found " + users.length + " users");
			
			for (User user : users) {
				logger.info("Found user " + user.getFirstName() + " " + user.getLastName());
			}
		} catch (IntegerException e) {
			
			e.printStackTrace();
			
			fail("Error finding all users " + e.getMessage());
		}
	}
	
	
}
