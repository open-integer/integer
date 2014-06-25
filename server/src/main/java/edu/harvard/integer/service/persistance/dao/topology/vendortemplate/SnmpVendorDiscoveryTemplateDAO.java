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

import javax.persistence.EntityManager;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * The DAO is responsible for persisting the SnmpVendorDiscoveryTemplate. All
 * queries will be done in this class. 
 *
 * @author David Taylor
 *
 */
public class SnmpVendorDiscoveryTemplateDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public SnmpVendorDiscoveryTemplateDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, SnmpVendorDiscoveryTemplate.class);
		
	}

	/**
	 * @param vendor
	 */
	public SnmpVendorDiscoveryTemplate findByVendor(Long vendor) throws IntegerException{
		SnmpVendorDiscoveryTemplate[] templates = findByLongField(vendor, "vendor", SnmpVendorDiscoveryTemplate.class);
	
		SnmpVendorDiscoveryTemplate snmpVendorDiscoveryTemplate = null;
		
		if (templates.length > 0)
			snmpVendorDiscoveryTemplate = templates[0];
		
		return snmpVendorDiscoveryTemplate;
	}

	/**
	 * @param vendor
	 * @throws IntegerException 
	 */
	public SnmpVendorDiscoveryTemplate findByVendor(ID vendor) throws IntegerException {
		SnmpVendorDiscoveryTemplate[] snmpVendorDiscoveryTemplates = findByIDField(vendor, "vendorId", SnmpVendorDiscoveryTemplate.class);
		
		if (snmpVendorDiscoveryTemplates != null && snmpVendorDiscoveryTemplates.length > 0)
			return snmpVendorDiscoveryTemplates[0];
		else 
			return null;
	}

	
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
		
		SnmpVendorDiscoveryTemplate template = (SnmpVendorDiscoveryTemplate) entity;
		
		PersistenceManagerInterface persistenceManager = DistributionManager.getManager(ManagerTypeEnum.PersistenceManager);
		DiscoveryParseStringDAO discoveryParseStringDAO = persistenceManager.getDiscoveryParseStringDAO();
		template.setParseString(discoveryParseStringDAO.update(template.getParseString()));
		
		super.preSave(entity);
	}

	
}
