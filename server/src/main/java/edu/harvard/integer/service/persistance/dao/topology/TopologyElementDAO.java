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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Signature;
import edu.harvard.integer.common.topology.SignatureValueOperator;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 */
public class TopologyElementDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public TopologyElementDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, TopologyElement.class);

	}

	/**
	 * Get the list of TopologyElements for the given service element ID.
	 * 
	 * @param serviceElementId
	 * @return TopologyElement[] found for the service element.
	 */
	public TopologyElement[] findByServiceElementID(ID serviceElementId) {

		return findByIDField(serviceElementId, "serviceElementId", TopologyElement.class);
	}

	/**
	 * @param ipAddress
	 */
	public TopologyElement[] findByAddress(String ipAddress) {
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();
		
		CriteriaQuery<TopologyElement> query = criteriaBuilder.createQuery(TopologyElement.class);

		Root<TopologyElement> from = query.from(TopologyElement.class);
		query.select(from);

		Join<TopologyElement, Address> addresses = from.join("address");
			
		ParameterExpression<String> addressParam = criteriaBuilder
				.parameter(String.class);
		
		
		query.select(from).where(
				criteriaBuilder.equal(addresses.get("address"), addressParam));

		TypedQuery<TopologyElement> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(addressParam, ipAddress);

		List<TopologyElement> resultList = typeQuery.getResultList();

		return (TopologyElement[]) resultList
				.toArray(new TopologyElement[resultList.size()]);
		
	}

}
