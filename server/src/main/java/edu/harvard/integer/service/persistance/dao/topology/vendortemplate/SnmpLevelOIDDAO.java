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
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.persistance.dao.BaseDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;

/**
 * @author David Taylor
 *
 */
public class SnmpLevelOIDDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public SnmpLevelOIDDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, SnmpLevelOID.class);
		
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
		
		SnmpLevelOID levelOID = (SnmpLevelOID) entity;
		
		SnmpServiceElementTypeDescriminatorDAO dao = new SnmpServiceElementTypeDescriminatorDAO(getEntityManager(), getLogger());
		SNMPDAO snmpDao = new SNMPDAO(getEntityManager(), getLogger());
		levelOID.setContextOID(snmpDao.update(levelOID.getContextOID()));
		levelOID.setDescriminatorOID(snmpDao.update(levelOID.getDescriminatorOID()));
		
		levelOID.setDisriminators(dao.update(levelOID.getDisriminators()));
		levelOID.setChildren(update(levelOID.getChildren()));
		
		SnmpRelationshipDAO relationDao = new SnmpRelationshipDAO(getEntityManager(), getLogger());
		levelOID.setRelationToParent(relationDao.update(levelOID.getRelationToParent()));
		
		super.preSave(entity);
	}


	public SnmpLevelOID findByContextOid(ServiceElementManagementObject contextOid) throws IntegerException {
		
		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();

		CriteriaQuery<SnmpLevelOID> query = criteriaBuilder.createQuery(SnmpLevelOID.class);

		Root<SnmpLevelOID> from = query.from(SnmpLevelOID.class);
		query.select(from);

		ParameterExpression<ServiceElementManagementObject> oid = criteriaBuilder
				.parameter(ServiceElementManagementObject.class);
		
		query.select(from).where(
				criteriaBuilder.equal(from.get("contextOID"), oid));

		TypedQuery<SnmpLevelOID> typeQuery = getEntityManager().createQuery(query);
		typeQuery.setParameter(oid, contextOid);

		List<SnmpLevelOID> resultList = typeQuery.getResultList();

		if (resultList.size() > 0)
			return resultList.get(0);
		else
			return null;
	}
	
}
