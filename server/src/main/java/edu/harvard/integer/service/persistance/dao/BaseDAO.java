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
 *         Base object for all DAO classes. The common update, modify, delete,
 *         findByXXX methods will be defined here.
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
	protected BaseDAO(EntityManager entityManger, Logger logger,
			Class<? extends BaseEntity> clazz) {
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
	 * Create or update the entity in the database. If the identifier is set and
	 * this object is not currently managed by the database then this object
	 * will update the entity in the database with the same identifier.
	 * 
	 * @param entity
	 * @return
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> T update(T entity) throws IntegerException {

		try {

			preSave(entity);

			if (entity.getIdentifier() == null)
				getEntityManager().persist(entity);
			else if (!getEntityManager().contains(entity))
				getEntityManager().merge(entity);

			getLogger().info(
					"Added " + entity.getName() + " ID: "
							+ entity.getIdentifier());

		} catch (EntityExistsException ee) {
			throw new IntegerException(ee,
					DatabaseErrorCodes.EntityAlreadyExists);
		}
		return entity;
	}

	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {

	}

	/**
	 * Find the entity in the database by the specified field.
	 * 
	 * @param fieldValue
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	protected <T extends BaseEntity> T findByStringField(String fieldValue,
			String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<String> oid = criteriaBuilder
				.parameter(String.class);
		query.select(from).where(
				criteriaBuilder.equal(from.get(fieldName), oid));

		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(oid, fieldValue);

		List<T> resultList = typeQuery.getResultList();

		if (resultList.size() > 0)
			return resultList.get(0);
		else
			return null;
	}
	
	/**
	 * Find the entity in the database by the specified field.
	 * 
	 * @param fieldValue
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	protected <T extends BaseEntity> T findByLongField(Long fieldValue,
			String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<Long> oid = criteriaBuilder
				.parameter(Long.class);
		query.select(from).where(
				criteriaBuilder.equal(from.get(fieldName), oid));

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

		CriteriaQuery<T> query = (CriteriaQuery<T>) criteriaBuilder
				.createQuery(clazz);
		Root<T> from = (Root<T>) query.from(clazz);

		query.select(from);
		List<T> resultList = entityManger.createQuery(query).getResultList();

		if (resultList != null) {
			T[] objs = (T[]) Array.newInstance(clazz, resultList.size());

			for (int i = 0; i < resultList.size(); i++) {
				T t = resultList.get(i);

				if (t instanceof BaseEntity)
					logger.info("Found " + ((BaseEntity) t).getName() + " ID "
							+ ((BaseEntity) t).getIdentifier());
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

		Class<? extends BaseEntity> clazz = id.getIdType().getClassType();

		@SuppressWarnings("unchecked")
		T entity = (T) entityManger.find(clazz, id.getIdentifier());

		return entity;
	}

	/**
	 * Update a list of entities of the type of this DAO. ex User
	 * 
	 * @param entities
	 * @return
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> List<T> update(List<T> entities)
			throws IntegerException {

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
	 * Delete the list of entities.
	 * 
	 * @param entity
	 * @throws IntegerException
	 */
	public <T extends BaseEntity> void delete(T[] entity)
			throws IntegerException {

		for (T t : entity) {

			if (!entityManger.contains(t))
				entityManger.merge(t);

			entityManger.remove(t);
		}

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

	/**
	 * Delete the entity specified by the ID.
	 * 
	 * @param entityId
	 * @throws IntegerException
	 */
	public void delete(ID[] entityIds) throws IntegerException {
		for (ID id : entityIds) {

			BaseEntity entity = findById(id);

			delete(entity);
		}
	}

	/**
	 * Create a "clean" copy of this persistent object. All references to
	 * hibernate will be removed.
	 * 
	 * @param originialInstance
	 * @return A new copy of the persistent object. All references to hibernate
	 *         will have been removed. This object is detached from the
	 *         persistent entity manager.
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T createCleanCopy(T originialInstance)
			throws IntegerException {

		T cleanCopy = null;
		try {

			cleanCopy = (T) originialInstance.getClass().newInstance();

			if (logger.isDebugEnabled())
				logger.debug("Created new instance " + cleanCopy.getClass());

		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IntegerException(e,
					DatabaseErrorCodes.UnableToCreateCleanCopy);
		} catch (IllegalAccessException e) {
			throw new IntegerException(e,
					DatabaseErrorCodes.UnableToCreateCleanCopyIllegalAccess);
		}

		Long identifier = originialInstance.getIdentifier();

		T copy = copyFields(cleanCopy, originialInstance);

		cleanCopy.setIdentifier(identifier);

		return copy;
	}

	/**
	 * Copy the fields from the "fromInstance" to the "toInstance". The
	 * identifier for the toInstnace will remain. Only data fields will be
	 * copied from the "fromInstnace".
	 * 
	 * @param cleanCopy
	 * @param tInstance
	 * @return T toInstnace. This has the data from the fromInstnace.
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IntegerException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends BaseEntity> T copyFields(T toInstance, T fromInstance)
			throws IntegerException {

		// Don't want to overwrite the identifier.
		Long identifier = toInstance.getIdentifier();

		for (Method f : fromInstance.getClass().getMethods()) {

			if (f.getName().startsWith("set")
					&& Character.isUpperCase(f.getName().charAt(3))) {

				f.setAccessible(true);

				Method getter;
				try {
					getter = fromInstance.getClass().getMethod(
							"get" + f.getName().substring(3));

					getter.setAccessible(true);

					Object value = getter.invoke(fromInstance);
					if (value instanceof BaseEntity) {
						value = createCleanCopy((BaseEntity) value);
					}
					if (value instanceof List) {
						value = copyList((List) value);
					}
					if (value != null && value.getClass().isArray())
						value = copyArray((T[]) value);

					f.invoke(toInstance, value);

					logger.info(toInstance.getClass().getSimpleName() + " "
							+ ((BaseEntity) toInstance).getID() + " "
							+ f.getName() + "(" + value + ")");

				} catch (NoSuchMethodException e) {
					throw new IntegerException(
							e,
							DatabaseErrorCodes.UnableToCreateCleanCopyNoSuchMethod);
				} catch (SecurityException e) {
					throw new IntegerException(
							e,
							DatabaseErrorCodes.UnableToCreateCleanCopySecurityException);
				} catch (IllegalAccessException e) {
					throw new IntegerException(
							e,
							DatabaseErrorCodes.UnableToCreateCleanCopyIllegalArgument);
				} catch (IllegalArgumentException e) {
					throw new IntegerException(
							e,
							DatabaseErrorCodes.UnableToCreateCleanCopyIllegalAccess);
				} catch (InvocationTargetException e) {
					throw new IntegerException(
							e,
							DatabaseErrorCodes.UnableToCreateCleanCopyNoSuchMethod);
				}

			}

		}

		toInstance.setIdentifier(identifier);

		return toInstance;
	}

	private <T extends BaseEntity> T[] copyArray(T[] values)
			throws IntegerException {

		if (values == null)
			return values;

		for (int i = 0; i < values.length; i++) {
			values[i] = createCleanCopy(values[i]);
		}

		return values;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List copyList(List<BaseEntity> list) throws IntegerException {

		List copy = new ArrayList();

		if (logger.isDebugEnabled())
			logger.debug("Copy list " + copy.getClass() + " "
					+ Arrays.toString(list.toArray()));

		for (BaseEntity object : list) {
			copy.add(createCleanCopy(object));
		}

		return copy;

	}
}
