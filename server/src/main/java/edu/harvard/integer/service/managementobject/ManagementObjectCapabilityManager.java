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

package edu.harvard.integer.service.managementobject;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Mechanism;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.persistance.PersistenceManagerLocalInterface;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;

/**
 * @author David Taylor
 * 
 * Manages the associations of Capabilities with specific management objects.
 * 
 */
@Stateless
public class ManagementObjectCapabilityManager implements
		ManagementObjectCapabilityManagerLocalInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerLocalInterface dbm;

	public ServiceElementType addServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
		logger.debug("Add ServiceElementType " + serviceElementType);
		
		try {
			dbm.getServiceElementTypeDAO().update(serviceElementType);
			
		}catch (EntityExistsException e) {
			logger.error("ServiceElementType already exists! " + e.getMessage());
		} catch (IntegerException e) {
			logger.error("Unexpected Error saveing ServiceElementType " + serviceElementType.getName() + ". " + e, e);
			e.printStackTrace();
		}
		
		return serviceElementType;
	}
	
	public void deleteServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
		logger.info("Delete service element " + serviceElementType.getName());
		
		dbm.getServiceElementTypeDAO().delete(serviceElementType);
	}

	public void deleteServiceElementType(ID serviceElementTypeId) throws IntegerException {
		
		logger.info("Delete service element " + serviceElementTypeId.getName());
		
		dbm.getServiceElementTypeDAO().delete(serviceElementTypeId);
	}

	
	public ServiceElementType[] getAllServiceElementTypes() throws IntegerException {
		
		return dbm.getServiceElementTypeDAO().findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #addCapability(edu.harvard.integer.common.topology.Capability)
	 */
	@Override
	public Capability addCapability(Capability capability) {

		logger.debug("Add capability " + capability.getName());

		try {
			dbm.getCapabilityDAO().update(capability);
		
		} catch (EntityExistsException e) {
			logger.error("Capability already exists! " + e.getMessage());
		} catch (IntegerException e) {
			logger.error("Unexpected Error saveing capability " + capability.getName() + ". " + e, e);
			e.printStackTrace();
		}
		return capability;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #addManagementObjectsToCapability
	 * (edu.harvard.integer.common.topology.Capability, java.util.List)
	 */
	@Override
	public Capability addManagementObjectsToCapability(Capability capability,
			List<ServiceElementManagementObject> managementObjects) throws IntegerException {

		CapabilityDAO dao = dbm.getCapabilityDAO();
		
		logger.info("Lookup capablity by ID: " + capability.getID());
		
		Capability dbCapability = dao.findById(capability.getID());
		if (dbCapability != null) {
			try {
				dao.copyFields(dbCapability, capability);
			} catch (IntegerException e) {
				
				e.printStackTrace();
				logger.error("Error coping fields to db capability!! " + e.toString());
			}
		} else
			dbCapability = dao.update(capability);
		
		ServiceElementManagementObjectDAO serviceElementDao = dbm.getServiceElementManagementObjectDAO();
		
		for (ServiceElementManagementObject serviceElementManagementObject : managementObjects) {
			serviceElementManagementObject = serviceElementDao.update(serviceElementManagementObject);
			
			serviceElementManagementObject.setCapabilityId(dbCapability.getID());
		}
		
		
		return dbCapability;
	}

	/**
	 * @param ID. The ID of capability that the management objects are for.
	 */
	public List<ServiceElementManagementObject> getManagemntObjectsForCapability(ID id) throws IntegerException {
		
		ServiceElementManagementObjectDAO dao = dbm.getServiceElementManagementObjectDAO();
		List<ServiceElementManagementObject> managementObjects = dao.findByCapabilityId(id);
		
		return managementObjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getAccessMethods(edu.harvard.integer.common.topology.Capability)
	 */
	@Override
	public List<AccessMethod> getAccessMethods(Capability capability) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getAccessMethods(edu.harvard.integer.common.ID)
	 */
	@Override
	public List<AccessMethod> getAccessMethods(ID capabilityId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getAssociatedServiceElementManagementObjects
	 * (edu.harvard.integer.common.topology.Capability)
	 */
	@Override
	public List<ServiceElementManagementObject> getAssociatedServiceElementManagementObjects(
			Capability capability) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface#getCapabilities()
	 */
	@Override
	public List<Capability> getCapabilities() throws IntegerException {
		CapabilityDAO dao = dbm.getCapabilityDAO();
		
		Capability[] findAll = dao.findAll();
		
		return Arrays.asList(findAll);
	}

	public Mechanism updateMechanism(Mechanism mechanism) throws IntegerException {
		MechanismDAO dao = dbm.getMechanismDAO();
		
		mechanism = dao.update(mechanism);
		
		return mechanism;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getMechanisms(java.util.List)
	 */
	@Override
	public List<Mechanism> getMechanisms(List<Capability> capabilites) {

		MechanismDAO dao = dbm.getMechanismDAO();
		
		dao.findByCapabilites(capabilites);
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #setMechamism(edu.harvard.integer.common.topology.Capability,
	 * edu.harvard.integer.common.topology.Mechanism)
	 */
	@Override
	public Mechanism setMechamism(Capability capability, Mechanism mechanism) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #setServiceElementManagementObjects
	 * (edu.harvard.integer.common.topology.Capability, java.util.List)
	 */
	@Override
	public Capability setServiceElementManagementObjects(Capability capability,
			List<ServiceElementManagementObject> managementObjects) {
		return capability;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getAllServiceElementManagementObjects()
	 */
	@Override
	public List<ServiceElementManagementObject> getAllServiceElementManagementObjects() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getUnassignedServiceElementManagementObjects()
	 */
	@Override
	public List<ServiceElementManagementObject> getUnassignedServiceElementManagementObjects() {

		return null;
	}
	
	
}
