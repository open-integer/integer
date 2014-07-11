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

package edu.harvard.integer.service.technology;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.technology.Mechanism;
import edu.harvard.integer.common.technology.Service;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.technology.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.technology.ServiceDAO;
import edu.harvard.integer.service.persistance.dao.technology.TechnologyDAO;

/**
 * @see TechnologyManagerInterface
 * 
 * @author David Taylor
 *
 */
@Stateless
public class TechnologyManager extends BaseManager implements TechnologyManagerLocalInterface, TechnologyManagerRemoteInterface {
	
	@Inject
	PersistenceManagerInterface persistenceManager;
	

	/**
	 * @param managerType
	 */
	public TechnologyManager() {
		super(ManagerTypeEnum.TechnologyManager);

	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#updateService(edu.harvard.integer.common.technology.Service)
	 */
	@Override
	public Service updateService(Service service) throws IntegerException {
		ServiceDAO dao = persistenceManager.getServiceDAO();
		
		return dao.update(service);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getServiceById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Service getServiceById(ID service) throws IntegerException {
		ServiceDAO dao = persistenceManager.getServiceDAO();
		
		return dao.findById(service);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getAllServices()
	 */
	@Override
	public Service[] getAllServices() throws IntegerException {
		ServiceDAO dao = persistenceManager.getServiceDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#updateTechnology(edu.harvard.integer.common.technology.Technology)
	 */
	@Override
	public Technology updateTechnology(Technology service) throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		
		return dao.update(service);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getTechnologyById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Technology getTechnologyById(ID service) throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		
		return dao.findById(service);
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getTechnologyByName(java.lang.String)
	 */
	@Override
	public Technology getTechnologyByName(String name) throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		Technology technology = dao.findByName(name);
		return technology;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getAllTechnologies()
	 */
	@Override
	public Technology[] getAllTechnologies() throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getTopLevelTechnology()
	 */
	@Override
	public Technology[] getTopLevelTechnology() throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		
		return dao.findTopLevel();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#updateMechanism(edu.harvard.integer.common.technology.Mechanism)
	 */
	@Override
	public Mechanism updateMechanism(Mechanism mechanism) throws IntegerException {
		MechanismDAO dao = persistenceManager.getMechanismDAO();
		
		mechanism = dao.update(mechanism);
		
		return mechanism;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getAllMechanisms()
	 */
	@Override
	public Mechanism[] getAllMechanisms() throws IntegerException {
		MechanismDAO dao = persistenceManager.getMechanismDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getMechanismById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Mechanism getMechanismById(ID mechanismId) throws IntegerException {
		MechanismDAO dao = persistenceManager.getMechanismDAO();
		
		return dao.findById(mechanismId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.technology.TechnologyManagerInterface#getMechanismByName(edu.harvard.integer.common.ID, java.lang.String)
	 */
	public Mechanism getMechanismByName(String name) throws IntegerException {
		MechanismDAO dao = persistenceManager.getMechanismDAO();
		
		return dao.findByName(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerInterface
	 * #getMechanisms(java.util.List)
	 */
	@Override
	public List<Mechanism> getMechanisms(List<Capability> capabilites) {

		MechanismDAO dao = persistenceManager.getMechanismDAO();
		
		dao.findByCapabilites(capabilites);
		
		return null;
	}

	
}
