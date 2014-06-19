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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Signature;
import edu.harvard.integer.common.topology.SignatureValueOperator;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 * All add, delete, modify, findXXX methods for the ServiceElementType object are done
 * in this class. 
 */
public class ServiceElementTypeDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public ServiceElementTypeDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, ServiceElementType.class);
		
	}

	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
		
		if (!(entity instanceof ServiceElementType)) {
			getLogger().error("Entity should be a ServiceElementType! Is " + entity.getClass());
			return;
		}
		
		ServiceElementType serviceElementType = (ServiceElementType) entity;
		if (serviceElementType.getSignatures() != null) {
			SignatureDAO dao = new SignatureDAO(getEntityManager(), getLogger());
			
			List<Signature> dbSignatures = new ArrayList<Signature>();
			for (Signature signature : serviceElementType.getSignatures()) {
				dbSignatures.add(dao.update(signature));
			}
			
			serviceElementType.setSignatures(dbSignatures);
		}
				
		super.preSave(entity);
	}


	/**
	 * @return
	 */
	public ServiceElementType[] findByCategoryAndVendor(CategoryTypeEnum category, String vendor) {

		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();
		
		CriteriaQuery<ServiceElementType> query = criteriaBuilder.createQuery(ServiceElementType.class);

		Root<ServiceElementType> from = query.from(ServiceElementType.class);
		query.select(from);

		Join<ServiceElementType, Signature> signatures = from.join("signatures");
		Join<Selection, SignatureValueOperator> values = signatures.join("valueOperators");
		
		ParameterExpression<CategoryTypeEnum> categoryParam = criteriaBuilder
				.parameter(CategoryTypeEnum.class);
		
		ParameterExpression<String> vendorParam = criteriaBuilder
				.parameter(String.class);
		
		
		query.select(from).where(criteriaBuilder.and(
				criteriaBuilder.equal(from.get("category"), categoryParam),
				criteriaBuilder.equal(values.get("value"), vendorParam)));

		TypedQuery<ServiceElementType> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(categoryParam, category);
		typeQuery.setParameter(vendorParam, vendor);

		List<ServiceElementType> resultList = typeQuery.getResultList();

		return (ServiceElementType[]) resultList
				.toArray(new ServiceElementType[resultList.size()]);
		
	}

	/**
	 * @param name
	 * @return
	 */
	public ServiceElementType findByName(String name) {
		return findByStringField(name, "name", ServiceElementType.class);
	}

}
