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

package edu.harvard.integer.service.persistance.dao.topology;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectStringValue;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementProtocolInstanceIdentifier;
import edu.harvard.integer.service.persistance.dao.BaseDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ManagementObjectValueDAO;

/**
 * @author David Taylor
 * 
 */
public class ServiceElementDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public ServiceElementDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, ServiceElement.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard
	 * .integer.common.BaseEntity)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {

		ServiceElement serviceElement = (ServiceElement) entity;
		if (serviceElement.getValues() != null) {
			List<ServiceElementProtocolInstanceIdentifier> values = new ArrayList<ServiceElementProtocolInstanceIdentifier>();

			ServiceElementProtocolInstanceIdentifierDAO seiDAO = new ServiceElementProtocolInstanceIdentifierDAO(
					getEntityManager(), getLogger());
			for (int i = 0; i < serviceElement.getValues().size(); i++) {
				values.add(seiDAO.update(serviceElement.getValues().get(i)));
			}

			serviceElement.setValues(values);
		}

		ManagementObjectValueDAO valueDao = new ManagementObjectValueDAO(
				getEntityManager(), getLogger());
		List<ManagementObjectValue> dbValues = new ArrayList<ManagementObjectValue>();
		if (serviceElement.getAttributeValues() != null) {
			for (ManagementObjectValue value : serviceElement
					.getAttributeValues()) {
				dbValues.add(valueDao.update(value));
			}

			serviceElement.setAttributeValues(dbValues);
		}

		if (serviceElement.getParentId() != null) {
			ServiceElement parent = findById(serviceElement.getParentId());
			if (parent != null)
				parent.setHasChildren(true);
			update(parent);
		}

		super.preSave(entity);
	}

	/**
	 * @param object
	 * @return
	 */
	public ServiceElement[] findTopLevelServiceElements() {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<ServiceElement> query = criteriaBuilder
				.createQuery(ServiceElement.class);

		Root<ServiceElement> from = query.from(ServiceElement.class);
		query.select(from);

		query.select(from).where(criteriaBuilder.isNull(from.get("parentId")));

		TypedQuery<ServiceElement> typeQuery = getEntityManager().createQuery(
				query);

		List<ServiceElement> resultList = typeQuery.getResultList();

		return (ServiceElement[]) resultList
				.toArray(new ServiceElement[resultList.size()]);
	}

	/**
	 * @param object
	 * @return
	 */
	public ServiceElement[] findByParentId(ID parent) {

		return findByIDField(parent, "parentId", ServiceElement.class);

	}

	/**
	 * @param parentId
	 * @param value
	 * @return
	 */
	public ServiceElement findByIdAndValue(ID parentId,
			ID serviceElementTypeId, ManagementObjectValue value) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<ServiceElement> query = criteriaBuilder
				.createQuery(ServiceElement.class);

		Root<ServiceElement> from = query.from(ServiceElement.class);

		Join<ServiceElement, ManagementObjectStringValue> join = from
				.join("valueId");

		// if (value instanceof ManagementObjectStringValue)
		// from.join(ManagementObjectStringValue.class);

		ParameterExpression<Long> idParam = criteriaBuilder
				.parameter(Long.class);

		ParameterExpression<ID> serviceElementTypeParam = criteriaBuilder
				.parameter(ID.class);

		query.select(from).where(
				criteriaBuilder.and(criteriaBuilder.equal(
						from.get("identifier"), idParam), criteriaBuilder
						.equal(from.get("serviceElementTypeId"),
								serviceElementTypeParam)));

		TypedQuery<ServiceElement> typeQuery = getEntityManager().createQuery(
				query);
		typeQuery.setParameter(idParam, parentId.getIdentifier());
		typeQuery.setParameter(serviceElementTypeParam, serviceElementTypeId);

		List<ServiceElement> resultList = typeQuery.getResultList();

		return null;
	}

	/**
	 * @param selection
	 * @return
	 */
	public ServiceElement[] findBySelection(Selection selection) {

		if (selection.getFilters() == null)
			return new ServiceElement[0];
		
		StringBuffer b = new StringBuffer();
		
		for (Filter filter : selection.getFilters() ) {
			for (FilterNode filterNode : filter.getTechnologies()) {
				if (filterNode.getSelected()) {
					
				}
			}
		}
		return findTopLevelServiceElements();
	}

}
