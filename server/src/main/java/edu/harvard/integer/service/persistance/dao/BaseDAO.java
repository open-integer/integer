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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.DatabaseErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.type.displayable.DisplayableInterface;
import edu.harvard.integer.common.type.displayable.SQLStatement;
import edu.harvard.integer.util.LoggerUtil;

/**
 * 
 * Base object for all DAO classes. The common update, modify, delete, findByXXX
 * methods will be defined here.
 * 
 * @author David Taylor
 * 
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

		if (entity == null)
			return null;

		try {

			preSave(entity);

			if (entity.getIdentifier() == null)
				getEntityManager().persist(entity);
			else if (!getEntityManager().contains(entity))
				getEntityManager().merge(entity);

			if (getLogger().isDebugEnabled())
				getLogger().debug(
						"Added " + entity.getName() + " ID: "
								+ entity.getIdentifier());

		} catch (EntityExistsException ee) {
			throw new IntegerException(ee,
					DatabaseErrorCodes.EntityAlreadyExists);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new IntegerException(e, DatabaseErrorCodes.ErrorSavingData,
					new DisplayableInterface[] { new SQLStatement("update "
							+ entity.getID().toDebugString()) });
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
	protected <T extends BaseEntity> T findByStringFieldIngnoreCase(
			String fieldValue, String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<String> oid = criteriaBuilder
				.parameter(String.class);

		query.select(from).where(
				criteriaBuilder.equal(from.get(fieldName.toLowerCase()),
						criteriaBuilder.lower(oid)));

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
	protected <T extends BaseEntity> T findByAddressField(Address fieldValue,
			String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<Address> oid = criteriaBuilder
				.parameter(Address.class);
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
	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> T[] findByLongField(Long fieldValue,
			String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<Long> oid = criteriaBuilder.parameter(Long.class);
		query.select(from).where(
				criteriaBuilder.equal(from.get(fieldName), oid));

		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(oid, fieldValue);

		List<T> resultList = typeQuery.getResultList();

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

				if (logger.isDebugEnabled()) {
					if (t instanceof BaseEntity)
						logger.debug("Found " + ((BaseEntity) t).getName() + " ID "
								+ ((BaseEntity) t).getIdentifier());
					else
						logger.debug("Found Non-BaseEntity " + t);
				}
				
				objs[i] = t;
			}

			return objs;
		}
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
	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> T[] findByIDField(ID fieldValue,
			String fieldName, Class<T> clazz) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		ParameterExpression<ID> idParam = criteriaBuilder.parameter(ID.class);
		query.select(from).where(
				criteriaBuilder.equal(from.get(fieldName), idParam));

		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(idParam, fieldValue);

		List<T> resultList = typeQuery.getResultList();

		return resultList.toArray((T[]) Array.newInstance(clazz, 0));
	}

	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> T[] findByNullField(String fieldName,
			Class<T> clazz) throws IntegerException {

		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);
		query.select(from);

		query.select(from).where(criteriaBuilder.isNull(from.get(fieldName)));

		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);

		List<T> resultList = typeQuery.getResultList();

		return (T[]) resultList.toArray((T[]) Array.newInstance(clazz, 0));
	}

	/**
	 * Find the given entity by the specified ID.
	 * 
	 * @param id
	 * @return
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T findById(ID id) throws IntegerException {

		Class<? extends BaseEntity> clazz = null;
		try {
			clazz = (Class<? extends BaseEntity>) Class.forName(id.getIdType()
					.getClassType());
		} catch (ClassNotFoundException e) {
			logger.error("Error creating class from "
					+ id.getIdType().getClassType());
			e.printStackTrace();
		}

		T entity = (T) entityManger.find(clazz, id.getIdentifier());

		return entity;
	}

	/**
	 * Find a list of objects by the given list of identifiers.
	 * 
	 * @param ids
	 * @param clazz
	 * @return
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseEntity> T[] findByIds(ID[] ids, Class<T> clazz)
			throws IntegerException {

		List<Long> identifers = new ArrayList<Long>();
		for (ID id : ids) {
			identifers.add(id.getIdentifier());
		}

		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);

		Root<T> from = query.from(clazz);

		Predicate[] predicates = new Predicate[ids.length];
		List<ParameterExpression<Long>> paramExpressions = new ArrayList<ParameterExpression<Long>>();

		for (int i = 0; i < ids.length; i++) {
			ParameterExpression<Long> idParam = criteriaBuilder
					.parameter(Long.class);
			paramExpressions.add(idParam);

			criteriaBuilder.equal(from.get("identifier"), idParam);

			predicates[i] = criteriaBuilder.equal(from.get("identifier"),
					idParam);
		}

		query.select(from).where(criteriaBuilder.or(predicates));

		TypedQuery<T> typeQuery = getEntityManager().createQuery(query);
		for (int i = 0; i < ids.length; i++)
			typeQuery.setParameter(paramExpressions.get(i),
					ids[i].getIdentifier());

		List<T> resultList = typeQuery.getResultList();

		return resultList.toArray((T[]) Array.newInstance(clazz, 0));
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

		if (entities == null)
			return null;

		List<T> dbEntries = new ArrayList<T>();
		
		for (T baseEntity : entities) {
			dbEntries.add(update(baseEntity));
		}

		return dbEntries;
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
	public <T extends Object> T createCleanCopy(T originialInstance)
			throws IntegerException {

		T cleanCopy = null;
		try {

			if (originialInstance instanceof BaseEntity) {
				cleanCopy = (T) originialInstance.getClass().newInstance();
			} else if (originialInstance != null && originialInstance.getClass().isArray()) {
				cleanCopy = (T) createArrayFrom(originialInstance);
				for (int i = 0; i < ((T[]) originialInstance).length; i++) {
					((T[]) cleanCopy)[i] = copyFields(((T[]) cleanCopy)[i], (((T[])originialInstance)[i]));
				}
				
			} else if (originialInstance.getClass().isEnum())
				cleanCopy = (T) originialInstance;
			else
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

		Long identifier = null;

		if (originialInstance instanceof BaseEntity)
			identifier = ((BaseEntity) originialInstance).getIdentifier();

		T copy = copyFields(cleanCopy, originialInstance);

		if (originialInstance instanceof BaseEntity)
			((BaseEntity) cleanCopy).setIdentifier(identifier);

		return copy;
	}

	public Object createArrayFrom(Object value) {

		if (value == null || value.getClass().isArray() == false)
			return value;

		if (Array.getLength(value) == 0)
			return value;

		// Doing byte[] this way because primative arrays choke in
		// the other code.
		if (value instanceof byte[]) {
			Object newInstance = new byte[Array.getLength(value)];
			System.arraycopy(value, 0, newInstance, 0, Array.getLength(value));
			return newInstance;
		}

		String arrayName = value.getClass().getName();
		String arrayElement = arrayName.substring(2, arrayName.indexOf(';'));
		Class<?> arrayClass = null;
		try {

			arrayClass = Class.forName(arrayElement);

		} catch (ClassNotFoundException e1) {
			System.out
					.println("Failed to create array of type " + arrayElement);
			e1.printStackTrace();
		}
		Object newInstance = Array.newInstance(arrayClass,
				Array.getLength(value));

		return newInstance;

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
	public <T extends Object> T copyFields(T toInstance, T fromInstance)
			throws IntegerException {

		// Don't want to overwrite the identifier.
		Long identifier = null;

		if (toInstance == null && fromInstance != null) {
			try {
				toInstance = (T) fromInstance.getClass().newInstance();
			} catch (InstantiationException e) {
				
				e.printStackTrace();
				return fromInstance;
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
				return fromInstance;
			}
		}
		
		if (toInstance instanceof BaseEntity)
			identifier = ((BaseEntity) toInstance).getIdentifier();

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

					if (logger.isDebugEnabled())
						logger.debug(LoggerUtil.filterLog(toInstance.getClass()
								.getSimpleName()
								+ " "
								+ ((BaseEntity) toInstance).getID()
								+ " "
								+ f.getName() + "(" + value + ")"));

				} catch (NoSuchMethodException e) {
					if (logger.isDebugEnabled())
						logger.debug("No getter on " + fromInstance.getClass().getName() + " method " + f.getName());
//					throw new IntegerException(
//							e,
//							DatabaseErrorCodes.UnableToCreateCleanCopyNoSuchMethod);
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

		if (toInstance instanceof BaseEntity)
			((BaseEntity) toInstance).setIdentifier(identifier);

		return toInstance;
	}

	public <T extends Object> T[] copyArray(T[] values) throws IntegerException {

		if (values == null)
			return values;

		@SuppressWarnings("unchecked")
		T[] newList = (T[]) createArrayFrom(values);
		
		for (int i = 0; i < values.length; i++) {
			newList[i] = createCleanCopy(values[i]);
		}

		return newList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List copyList(List<Object> list) throws IntegerException {

		List copy = new ArrayList();

		if (logger.isDebugEnabled())
			logger.debug("Copy list " + copy.getClass() + " "
					+ Arrays.toString(list.toArray()));

		for (Object object : list) {
			copy.add(createCleanCopy(object));
		}

		return copy;

	}

	protected int executeUpdate(String sqlCmd) throws IntegerException {

		try {
			return getEntityManager().createNativeQuery(sqlCmd).executeUpdate();
		} catch (Exception e) {
			throw new IntegerException(e, DatabaseErrorCodes.ErrorExecutingSQL,
					new DisplayableInterface[] { new SQLStatement(sqlCmd) });
		}
	}

	public void createIndex() throws IntegerException {

	}
}
