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

package edu.harvard.integer.service.persistance.dao.snmp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPModuleHistory;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 * All add, delete, modify, findXXX methods for the SNMPModuleHistory object are done
 * in this class. 
 */
public class SNMPModuleHistoryDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public SNMPModuleHistoryDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, SNMPModule.class);
		
	}

	/**
	 * @param history
	 * @return 
	 */
	public SNMPModuleHistory[] findByHistories(List<ID> history) {
		

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<SNMPModuleHistory> query = criteriaBuilder.createQuery(SNMPModuleHistory.class);

		Root<SNMPModuleHistory> from = query.from(SNMPModuleHistory.class);
		query.select(from);
		
		List<ParameterExpression<Long>> parameters = new ArrayList<ParameterExpression<Long>>();
		
		for (int i = 0; i < history.size(); i++) {
			ParameterExpression<Long> historyId = criteriaBuilder.parameter(Long.class);
			parameters.add(historyId);
			query.select(from).where(criteriaBuilder.or(criteriaBuilder.equal(from.get("identifier"), historyId)));
		}
		
		TypedQuery<SNMPModuleHistory> createQuery = getEntityManager().createQuery(query);
		
		for (int i = 0; i < parameters.size(); i++) {
			ParameterExpression<Long> historyId = parameters.get(i);
			createQuery.setParameter(historyId, history.get(i).getIdentifier());
		}
		
		
		List<SNMPModuleHistory> resultList = createQuery.getResultList();
		return resultList.toArray(new SNMPModuleHistory[0]);
	}

}
