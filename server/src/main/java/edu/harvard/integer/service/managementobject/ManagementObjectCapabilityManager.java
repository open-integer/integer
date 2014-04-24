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
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Applicability;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Mechanism;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SnmpServiceElementTypeOverride;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.managementobject.ApplicabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;
import edu.harvard.integer.service.persistance.dao.topology.SnmpServiceElementTypeOverrideDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;

/**
 * @author David Taylor
 * 
 * Manages the associations of Capabilities with specific management objects.
 * 
 */
@Stateless
public class ManagementObjectCapabilityManager extends BaseManager implements
		ManagementObjectCapabilityManagerInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface dbm;

	@Override
	public ServiceElementType updateServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
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
	
	@Override
	public void deleteServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
		logger.info("Delete service element " + serviceElementType.getName());
		
		dbm.getServiceElementTypeDAO().delete(serviceElementType);
	}

	@Override
	public void deleteServiceElementType(ID serviceElementTypeId) throws IntegerException {
		
		logger.info("Delete service element " + serviceElementTypeId.getName());
		
		dbm.getServiceElementTypeDAO().delete(serviceElementTypeId);
	}

	
	@Override
	public ServiceElementType[] getAllServiceElementTypes() throws IntegerException {
		
		return dbm.getServiceElementTypeDAO().findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.managementobject.
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface#getCapabilities()
	 */
	@Override
	public List<Capability> getCapabilities() throws IntegerException {
		CapabilityDAO dao = dbm.getCapabilityDAO();
		
		Capability[] findAll = dao.findAll();
		
		findAll = dao.copyArray(findAll);
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
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
	 * ManagementObjectCapabilityManagerInterface
	 * #getUnassignedServiceElementManagementObjects()
	 */
	@Override
	public List<ServiceElementManagementObject> getUnassignedServiceElementManagementObjects() {

		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#updateSnmpContainment(edu.harvard.integer.common.discovery.SnmpContainment)
	 */
	@Override
	public SnmpContainment updateSnmpContainment(SnmpContainment snmpContainment) throws IntegerException {
		
		SnmpContainmentDAO dao = dbm.getSnmpContainmentDAO();
		snmpContainment = dao.update(snmpContainment);
		
		return snmpContainment;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getAllSnmpContainments()
	 */
	@Override
	public SnmpContainment[] getAllSnmpContainments() throws IntegerException {
		
		SnmpContainmentDAO dao = dbm.getSnmpContainmentDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getSnmpContainmentById(edu.harvard.integer.common.ID)
	 */
	@Override
	public SnmpContainment getSnmpContainmentById(ID id) throws IntegerException {
		SnmpContainmentDAO dao = dbm.getSnmpContainmentDAO();
		
		return dao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#updateApplicability(edu.harvard.integer.common.topology.Applicability)
	 */
	@Override
	public Applicability updateApplicability(Applicability applicabilty) throws IntegerException {
		ApplicabilityDAO dao = dbm.getApplicabilityDAO();
		
		return dao.update(applicabilty);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getAllApplicabilities()
	 */
	@Override
	public Applicability[] getAllApplicabilities() throws IntegerException {
		ApplicabilityDAO dao = dbm.getApplicabilityDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getApplicabilityById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Applicability getApplicabilityById(ID id) throws IntegerException { 
		ApplicabilityDAO dao = dbm.getApplicabilityDAO();
		
		return dao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#deleteApplicability(edu.harvard.integer.common.ID)
	 */
	@Override
	public void deleteApplicability(ID id) throws IntegerException {
		ApplicabilityDAO dao = dbm.getApplicabilityDAO();
		
		dao.delete(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#updateSnmpServiceElementTypeOverride(edu.harvard.integer.common.topology.SnmpServiceElementTypeOverride)
	 */
	@Override
	public SnmpServiceElementTypeOverride updateSnmpServiceElementTypeOverride(SnmpServiceElementTypeOverride override) throws IntegerException {
		SnmpServiceElementTypeOverrideDAO dao = dbm.getSnmpServiceElementTypeOverrideDAO();
		
		return dao.update(override);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getAllSnmpServiceElementTypeOverride()
	 */
	@Override
	public SnmpServiceElementTypeOverride[] getAllSnmpServiceElementTypeOverride() throws IntegerException {
		SnmpServiceElementTypeOverrideDAO dao = dbm.getSnmpServiceElementTypeOverrideDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getSnmpServiceElementTypeOverrideById(edu.harvard.integer.common.ID)
	 */
	@Override
	public SnmpServiceElementTypeOverride getSnmpServiceElementTypeOverrideById(ID id) throws IntegerException {
		SnmpServiceElementTypeOverrideDAO dao = dbm.getSnmpServiceElementTypeOverrideDAO();
		
		return dao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#deleteSnmpServiceElementTypeOverride(edu.harvard.integer.common.ID)
	 */
	@Override
	public void deleteSnmpServiceElementTypeOverride(ID id) throws IntegerException {
		SnmpServiceElementTypeOverrideDAO dao = dbm.getSnmpServiceElementTypeOverrideDAO();
		
		dao.delete(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getManagementObjectById(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElementManagementObject getManagementObjectById(ID id) throws IntegerException {
		ServiceElementManagementObjectDAO dao = dbm.getServiceElementManagementObjectDAO();
		
		return dao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#getManagementObjectsByIds(edu.harvard.integer.common.ID[])
	 */
	@Override
	public ServiceElementManagementObject[] getManagementObjectsByIds(ID[] ids) throws IntegerException {
		ServiceElementManagementObjectDAO dao = dbm.getServiceElementManagementObjectDAO();
		
		return dao.findByIds(ids);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface#updateManagementObject(edu.harvard.integer.common.topology.ServiceElementManagementObject)
	 */
	@Override
	public ServiceElementManagementObject updateManagementObject(ServiceElementManagementObject managementObject) throws IntegerException {
		ServiceElementManagementObjectDAO dao = dbm.getServiceElementManagementObjectDAO();
		
		managementObject = dao.update(managementObject);
		
		return managementObject;
	}
	
	@Override
	public void deleteManagementObject(ID id) throws IntegerException {
		ServiceElementManagementObjectDAO dao = dbm.getServiceElementManagementObjectDAO();
		dao.delete(id);
	}
}
