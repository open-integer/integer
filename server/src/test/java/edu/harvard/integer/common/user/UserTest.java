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

import static org.junit.Assert.*;

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

import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.util.Resource;
import edu.harvard.integer.manager.user.UserManager;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class UserTest {

//	@Inject
//	private UserManager userManager;
//	
//	@Inject
//	private Logger logger;
//	
//	 @Deployment
//	    public static Archive<?> createTestArchive() {
//	        return ShrinkWrap.create(WebArchive.class, "test.war")
//	                .addClasses(Resource.class)
//	                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
//	                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
//	                // Deploy our test datasource
//	                .addAsWebInfResource("test-ds.xml");
//	    }
//
//	@Test
//	public void addOneUser() {
//		logger.info("Create user dtaylor");
//		
//		User u = new User();
//		u.setName("dtaylor");
//		
//		IDType userType = new IDType();
//		userType.setClassType(User.class);
//		u.setIdType(userType);
//		u.setFirstName("Dave");
//		u.setLastName("Taylor");
//		u.setUuid("123456789");
//		
//		try {
//			userManager.addUser(u);
//			
//		} catch (IntegerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail("Error saveing user " + e);
//		}
//		
//		
//	}

	@Test
	public void addOneUser() {
		
	}
}
