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
package edu.harvard.integer.database;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.user.User;
import edu.harvard.integer.server.IntegerApplication;
/**
 * @author David Taylor
 *
 */
@Singleton
@Startup
@Path("/Database")
public class DatabaseService implements DatabaseServiceEJB {

	@PersistenceContext
	private EntityManager em;
		
	@Inject
	private Logger logger;
	
	@PostConstruct
	public void init() {
		
		IntegerApplication.register(this);

		logger.info("Create user dtaylor");
		
		User u = new User();
		u.setName("dtaylor");
		
		IDType userType = new IDType();
		userType.setClassType(User.class);
		u.setIdType(userType);
		u.setFirstName("Dave");
		u.setLastName("Taylor");
		u.setUuid("123456789");
		
		em.persist(u);
		
	}
	

	@GET
	@Path("/Users")
	@Produces(value=MediaType.TEXT_HTML)
	public String showUsers() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
		Root<User> from = query.from(User.class);
		query.select(from);
		List<User> resultList = em.createQuery(query).getResultList();
		
		StringBuffer b = new StringBuffer();
		b.append("<ul>");
		for (User obj : resultList) {
			b.append("<li>User " + obj.getIdentifier() + " " + obj.getFirstName() + " " + obj.getLastName() + " UUID: " + obj.getUuid() + "</li>");
			
		}
		b.append("</ul>");	
		return b.toString();
	}
}
