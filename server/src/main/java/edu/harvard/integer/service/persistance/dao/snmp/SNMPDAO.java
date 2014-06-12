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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.service.persistance.dao.BaseDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.SnmpSyntaxDAO;

/**
 * @author David Taylor All add, delete, modify, findXXX methods for the SNMP
 *         object are done in this class.
 */
public class SNMPDAO extends BaseDAO {

	private Logger logger = LoggerFactory.getLogger(SNMPDAO.class);

	/**
	 * @param entityManger
	 * @param logger
	 */
	public SNMPDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, SNMP.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard
	 * .integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {

		SNMP snmp = (SNMP) entity;
		if (snmp.getTextualConvetion() != null) {
			SnmpSyntaxDAO dao = new SnmpSyntaxDAO(getEntityManager(),
					getLogger());
			snmp.setSyntax(dao.update(snmp.getSyntax()));
		}

		super.preSave(entity);
	}

	/**
	 * Find the SNMPModule that has the given OID.
	 * 
	 * @return
	 */
	public SNMP findByOid(String oidString) {
		return findByStringField(oidString, "oid", SNMP.class);

	}

	/**
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public List<SNMP> findByOidSubtree(String name) {

		Query createQuery = getEntityManager().createQuery("select snmp from ServiceElementManagementObject snmp where oid like :name");
		createQuery.setParameter("name", name + ".%");
		
		List<SNMP> resultList = createQuery.getResultList();

		if (resultList.size() > 0) {
			if (getLogger().isDebugEnabled())
				getLogger().debug(
						"Found OID " + resultList.get(0).getIdentifier() + " "
								+ resultList.get(0).getOid() + " "
								+ resultList.get(0).getName());

			return resultList;
		} else
			return null;

	}

	/**
	 * Find the SNMP object with the given name. This should only match ONE SNMP
	 * object.
	 * 
	 * @param name
	 * @return
	 */
	public SNMP findByName(String name) {

		SNMP snmp = findByStringField(name, "name", SNMP.class);
		return snmp;
	}
}
