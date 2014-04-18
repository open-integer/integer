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

package edu.harvard.integer.service.persistance.dao.topology.vendortemplate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 */
public class VendorContainmentSelectorDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public VendorContainmentSelectorDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, VendorContainmentSelector.class);
	
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
				
		super.preSave(entity);
	}

	/**
	 * @param selector
	 * @return
	 */
	public VendorContainmentSelector[] findBySelector(VendorContainmentSelector selector) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<VendorContainmentSelector> query = criteriaBuilder.createQuery(VendorContainmentSelector.class);

		Root<VendorContainmentSelector> from = query.from(VendorContainmentSelector.class);
		query.select(from);
		
		ParameterExpression<String> vendor = criteriaBuilder.parameter(String.class);
		ParameterExpression<String> model = criteriaBuilder.parameter(String.class);
		ParameterExpression<String> firmware = criteriaBuilder.parameter(String.class);
		ParameterExpression<String> softwareVersion = criteriaBuilder.parameter(String.class);
		
		query.select(from).where(criteriaBuilder.and(
				criteriaBuilder.equal(from.get("vendor"), vendor),
				criteriaBuilder.equal(from.get("model"), model)),
				criteriaBuilder.equal(from.get("firmware"), firmware),
				criteriaBuilder.equal(from.get("softwareVersion"), softwareVersion));

		
		TypedQuery<VendorContainmentSelector> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(vendor, selector.getVendor());
		typeQuery.setParameter(model, selector.getModel());
		typeQuery.setParameter(firmware, selector.getFirmware());
		typeQuery.setParameter(softwareVersion, selector.getSoftwareVersion());

		List<VendorContainmentSelector> resultList = typeQuery.getResultList();

		
		return (VendorContainmentSelector[]) resultList
				.toArray(new VendorContainmentSelector[resultList.size()]);
	}

}
