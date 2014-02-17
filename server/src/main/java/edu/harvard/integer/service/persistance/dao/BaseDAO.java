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

package edu.harvard.integer.service.persistance.dao;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.DatabaseErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;

/**
 * @author David Taylor
 *
 * Base object for all DAO classes. The common update, modify, delete, findByXXX methods will
 * be defined here.
 * 
 */
public class BaseDAO {
	
	private EntityManager entityManger = null;
	
	private Logger logger = null;
	
	private Class<? extends BaseEntity> clazz = null;
	
	/**
	 * Create the DAO for this class type. All entities that are stored in the
	 * database must extend BaseEntity.
	 * 
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	protected BaseDAO(EntityManager entityManger, Logger logger, Class<? extends BaseEntity> clazz) {
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
	protected Logger getLogger() {
		return logger;
	}
	
	protected Class<? extends BaseEntity> getPersistentClass() {
		return clazz;
	}
	
	/**
	 * Create or update the entity in the database. If the identifier is set and this object
	 * is not currently managed by the database then this object will update the entity
	 * in the database with the same identifier.
	 *  
	 * @param entity
	 * @return
	 * @throws IntegerException
	 */
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


	/**
	 * Find the entity in the database by the specified field.
	 * @param fieldValue
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	protected <T extends BaseEntity> T findByStringField(String fieldValue, String fieldName, Class<T> clazz) {
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

	/**
	 * Find all the entities in the database of the type of this DAO. ex. User
	 * 
	 * @return
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T[] findAll() throws IntegerException {
		
		CriteriaBuilder criteriaBuilder = entityManger.getCriteriaBuilder();
		
		CriteriaQuery<T> query = (CriteriaQuery<T>) criteriaBuilder.createQuery(clazz);
		Root<T> from = (Root<T>) query.from(clazz);
	
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
	
	/**
	 * Find the given entity by the specified ID.
	 * 
	 * @param id
	 * @return
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> T findById(ID id) throws IntegerException {

		Class clazz = null;
		try {
			clazz = Class.forName(id.getIdType().getClassType());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		@SuppressWarnings("unchecked")
		T entity = (T) entityManger.find(clazz, id.getIdentifier());
		
		return entity;
	}

	/**
	 * Update a list of entities of the type of this DAO. ex User
	 * @param entities
	 * @return
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> List<T> update(List<T> entities) throws IntegerException {
		
		for (T baseEntity : entities) {
			update(baseEntity);
		}
		
		return entities;
	}
	
	
	/**
	 * Delete the given entity.
	 * 
	 * @param entity
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> void delete(T entity) throws IntegerException {
		if (!entityManger.contains(entity))
			entityManger.merge(entity);
		
		entityManger.remove(entity);
	}
	
	/**
	 * Delete the entity specified by the ID.
	 * 
	 * @param entityId
	 * @throws IntegerException
	 */
	public void delete(ID entityId) throws IntegerException {
		BaseEntity entity = findById(entityId);
		
		delete(entity);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createCleanCopy(T tInstance)
			throws IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, IntegerException {

		T cleanCopy = null;
		try {
			cleanCopy = (T) tInstance.getClass().newInstance();
			
			if (logger.isDebugEnabled())
				logger.debug("Created new instance " + cleanCopy.getClass());
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IntegerException(e, DatabaseErrorCodes.UnableToCreateCleanCopy);
		}

		return copyFields(cleanCopy, tInstance);
	}
	

	/**
	 * Copy the fields from the "fromInstance" to the "toInstance"
	 * 
	 * @param cleanCopy
	 * @param tInstance
	 * @return T toInstnace. This has the data from the fromInstnace. 
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IntegerException
	 */
	public <T> T copyFields(T toInstance, T fromInstance)
			throws IllegalAccessException, NoSuchMethodException,
			InvocationTargetException, IntegerException {

		
		for (Method f : fromInstance.getClass().getMethods()) {

			if (f.getName().startsWith("set") && Character
					.isUpperCase(f.getName().charAt(3))) {

				f.setAccessible(true);

				Method getter = fromInstance.getClass()
						.getMethod("get" + f.getName().substring(3));

				getter.setAccessible(true);

				Object value = getter.invoke(fromInstance);
				if (value instanceof BaseEntity) {
					value = createCleanCopy(value);
				} if (value instanceof List) {
					value = copyList((List) value);
				}
				
				f.invoke(toInstance, value);
			}

		}

		return toInstance;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List copyList(List list) {
		try {
			List copy = new ArrayList();
			
			if (logger.isDebugEnabled())
				logger.debug("Copy list " + copy.getClass() + " " + Arrays.toString(list.toArray()));
			
			for (Object object : list) {
				copy.add(createCleanCopy(object));
			}
	
			return copy;
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
