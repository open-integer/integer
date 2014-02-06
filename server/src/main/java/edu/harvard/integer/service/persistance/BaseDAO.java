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

package edu.harvard.integer.service.persistance;

import java.lang.reflect.Array;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.DatabaseErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;

/**
 * @author David Taylor
 *
 * Base object for all DAO classes. 
 * 
 */
public class BaseDAO {
	
	private EntityManager entityManger = null;
	
	private Logger logger = null;
	
	private Class clazz = null;
	
	public BaseDAO(EntityManager entityManger, Logger logger, Class<? extends BaseEntity> clazz) {
		this.entityManger = entityManger;
		this.logger = logger;
		this.clazz = clazz;
	}
	
	protected EntityManager getEntityManager() {
		return entityManger;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	
	public <T extends BaseEntity> T update(T entity) throws IntegerException {
		
		try {
			if (entity.getIdentifier() == null)
				getEntityManager().persist(entity);
			else if (!getEntityManager().contains(entity))
				getEntityManager().merge(entity);

			getLogger().info("Added " + entity.getName() + " ID: " + entity.getIdentifier());

		} catch (EntityExistsException ee) {
			throw new IntegerException(ee, DatabaseErrorCodes.EntityAlreadyExists);
		} 
		return entity;
	}


	public <T> T findByStringField(String fieldValue, String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);
		
		ParameterExpression<String> oid = criteriaBuilder.parameter(String.class);
		query.select(from).where(criteriaBuilder.equal(from.get(fieldName), oid));
		
		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(oid, fieldValue);
		
		List<T> resultList = typeQuery.getResultList();
		
		if (resultList.size() > 0)
			return resultList.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> T[] findAll() throws IntegerException {
		
		CriteriaBuilder criteriaBuilder = entityManger.getCriteriaBuilder();
		
		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
		Root<T> from = query.from(clazz);
	
		query.select(from);
		List<T> resultList = entityManger.createQuery(query).getResultList();
		
		if (resultList != null) {
			T[] objs = (T[]) Array.newInstance(clazz, resultList.size());
		
			for (int i = 0; i < resultList.size(); i++) {
				T t = resultList.get(i);
				
				if (t instanceof BaseEntity)
					logger.info("Found " + ((BaseEntity) t).getName() + " ID " + ((BaseEntity)t).getIdentifier());
				else
					logger.info("Found Non-BaseEntity " + t);
				
				objs[i] = t;
			}
			
			return objs;
		}
		return null;
	}
}
