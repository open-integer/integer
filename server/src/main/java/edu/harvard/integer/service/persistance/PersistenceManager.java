/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.service.persistance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPIndexDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleHistoryDAO;
import edu.harvard.integer.service.persistance.dao.user.UserDAO;
/**
 * @author David Taylor
 * 
 */
@Stateless
public class PersistenceManager implements PersistenceManagerLocalInterface {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	Logger logger;
		
	
	/**
	 * Get the SNMPModuleDAO. 
	 * 
	 * @return SNMPModuleDAO. 
	 */
	@Override
	public SNMPModuleDAO getSNMPModuleDAO() {
		return new SNMPModuleDAO(em, logger);
	}
	
	/**
	 * Get the SNMPDAO.
	 * 
	 * @return
	 */
	@Override
	public SNMPDAO getSNMPDAO() {
		return new SNMPDAO(em, logger);
	}
	
	/**
	 * Get the SNMPIndexDAO.
	 * 
	 * @return
	 */
	@Override
	public SNMPIndexDAO getSNMPIndexDAO() {
		return new SNMPIndexDAO(em, logger);
	}
	
	/**
	 * Get the MIBInfoDAO.
	 * 
	 * @return
	 */
	@Override
	public MIBInfoDAO getMIBInfoDAO() {
		return new MIBInfoDAO(em, logger);
	}
	
	/**
	 * Get the UserDAO
	 * 
	 * @return
	 */
	@Override
	public UserDAO getUserDAO() {
		return new UserDAO(em, logger);
	}
	
	/**
	 * Get the ServiceElementTypeDAO
	 * 
	 * @return
	 */
	@Override
	public ServiceElementTypeDAO getServiceElementTypeDAO() {
		return new ServiceElementTypeDAO(em, logger);
	}
	
	/**
	 * Get the CapabilityDAO
	 * 
	 * @return
	 */
	@Override
	public CapabilityDAO getCapabilityDAO() {
		return new CapabilityDAO(em, logger);
	}

	/**
	 * Get the SNMPHistoryDAO()
	 * 
	 * @return SNMPHistoryDAO
	 */
	@Override
	public SNMPModuleHistoryDAO getSNMPModuleHistoryDAO() {
		
		return new SNMPModuleHistoryDAO(em, logger);
	}
	
	/**
	 * Get the ServiceElementManagementObjectDAO
	 * 
	 * @return ServiceElementManagementObjectDAO
	 */
	@Override
	public ServiceElementManagementObjectDAO getServiceElementManagementObjectDAO() {
		return new ServiceElementManagementObjectDAO(em, logger);
	}
}
