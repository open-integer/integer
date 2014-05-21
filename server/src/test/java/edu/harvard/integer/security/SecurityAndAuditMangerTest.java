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

package edu.harvard.integer.security;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.apache.log4j.Level;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.security.DirectUserLogin;
import edu.harvard.integer.common.security.IntegerSession;
import edu.harvard.integer.service.security.SecurityAndAuditManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class SecurityAndAuditMangerTest {

	@Inject
	private Logger logger;
	
	@Inject
	private SecurityAndAuditManagerInterface securityManager;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("SecurityAndAuditMangerTest.war");
	}

	@Before
	public void initializeLogger() {
		//BasicConfigurator.configure();
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
	}

	@Test
	public void loginUser() {
		
		DirectUserLogin login = new DirectUserLogin();
		login.setName("UserBob");
		login.setPlainTextPassword("BobsPassword");
		
		boolean validatePassword = login.validatePassword("BadPassword");
		assert(validatePassword == false);
		
		validatePassword = login.validatePassword("BobsPassword");
		assert(validatePassword == true);
		
		try {
			login.setAddress(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1}));
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			IntegerSession integerSession = securityManager.loginUser(login);
			logger.info("User " + login.getName() + " logged in! ID " + integerSession.getSessionId());
		} catch (IntegerException e) {
			
			e.printStackTrace();
			//fail(e.toString());
		}
	
	}

	@Test
	public void addDirectUser() {
		DirectUserLogin login = new DirectUserLogin();
		login.setName("admin");
		login.setPlainTextPassword("public");
		
		try {
			securityManager.addDirectUser(login);
		} catch (IntegerException e) {
		
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void getAllDirectUsers() {
		try {
			DirectUserLogin[] directUsers = securityManager.getAllDirectUsers();
			if (directUsers == null || directUsers.length == 0) {
				addDirectUser();
				directUsers = securityManager.getAllDirectUsers();
			}
				
			logger.info("Found " + directUsers.length + " Direct user logins");
			
			assert(directUsers.length > 0);
			
			for (DirectUserLogin directUserLogin : directUsers) {
				logger.info("Found direct login " + directUserLogin.getID().toString());
			}
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
