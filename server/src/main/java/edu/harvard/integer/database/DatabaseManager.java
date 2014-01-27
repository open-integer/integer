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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.DatabaseErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;
/**
 * @author David Taylor
 * 
 */
@Stateless
public class DatabaseManager {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	Logger logger;
	
	public BaseEntity update(BaseEntity entity) throws IntegerException {
		
		try {
		if (entity.getIdentifier() == null)
			em.persist(entity);
		else if (!em.contains(entity))
			em.merge(entity);
	
		logger.info("Added " + entity.getName() + " ID: " + entity.getIdentifier());
		
		} catch (EntityExistsException ee) {
			throw new IntegerException(ee, DatabaseErrorCodes.EntityAlreadyExists);
		} 
		return entity;
	}
	
	public List<BaseEntity> update(List<BaseEntity> entities) throws IntegerException {
		
		for (BaseEntity baseEntity : entities) {
			update(baseEntity);
		}
		
		return entities;
	}
}
