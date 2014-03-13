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

import java.util.UUID;

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

import edu.harvard.integer.common.exception.IntegerException;
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
				.addPackages(true, "edu.harvard.integer")
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
		
		u.setFirstName("Dave");
		u.setLastName("Taylor");
		u.setUuid(UUID.randomUUID());
		
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
	
	@Test
	public void addOrginization() {
		Organization organization = new Organization();
		organization.setName("Organization name");
		organization.setOrginizationType("Harvard");
		
		try {
			userManager.updateOrganization(organization);
		} catch (IntegerException e) {
		
			e.printStackTrace();
			
			fail(e.toString());
		}
	}
	
	@Test
	public void addAccessPolicy() {
		AccessPolicy policy = new AccessPolicy();
		policy.setName("Policy");
		policy.setPermitDeny(PermitDenyEnum.Pemit);
		
		try {
			userManager.updateAccessPolicy(policy);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Test
	public void addRole() {
		Role role = new Role();
		role.setName("MyRole");
		role.setRoleType(RoleType.Primary);
		
		try {
			userManager.updateRole(role);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			
			fail(e.toString());
		}
	}
	
}
