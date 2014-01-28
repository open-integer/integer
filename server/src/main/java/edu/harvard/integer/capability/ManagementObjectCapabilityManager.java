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

package edu.harvard.integer.capability;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Mechanism;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.database.DatabaseManager;

/**
 * @author David Taylor
 * 
 */
@Stateless
public class ManagementObjectCapabilityManager implements
		ManagementObjectCapabilityManagerLocalInterface {

	@Inject
	private Logger log;

	@Inject
	DatabaseManager dbm;

	public ServiceElementType addServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
		log.debug("Add ServiceElementType " + serviceElementType);
		
		try {
			dbm.update(serviceElementType);
		}catch (EntityExistsException e) {
			log.error("ServiceElementType already exists! " + e.getMessage());
		} catch (IntegerException e) {
			log.error("Unexpected Error saveing ServiceElementType " + serviceElementType.getName() + ". " + e, e);
			e.printStackTrace();
		}
		
		return serviceElementType;
	}
	
	public void deleteServiceElementType(ServiceElementType serviceElementType) throws IntegerException {
	
		log.info("Delete service element " + serviceElementType.getName());
		
		dbm.delete(serviceElementType);
	}

	public void deleteServiceElementType(ID serviceElementTypeId) throws IntegerException {
		
		log.info("Delete service element " + serviceElementTypeId.getName());
		
		dbm.delete(serviceElementTypeId);
	}

	
	public ServiceElementType[] getAllServiceElementTypes() throws IntegerException {
		IDType type = new IDType();
		type.setClassType(ServiceElementType.class);
		
		return dbm.findAll(type);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.capability.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #addCapability(edu.harvard.integer.common.topology.Capability)
	 */
	@Override
	public Capability addCapability(Capability capability) {

		log.debug("Add capability " + capability.getName());

		try {
			dbm.update(capability);
		
		} catch (EntityExistsException e) {
			log.error("Capability already exists! " + e.getMessage());
		} catch (IntegerException e) {
			log.error("Unexpected Error saveing capability " + capability.getName() + ". " + e, e);
			e.printStackTrace();
		}
		return capability;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.capability.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #addManagementObjectsToCapability
	 * (edu.harvard.integer.common.topology.Capability, java.util.List)
	 */
	@Override
	public Capability addManagementObjectsToCapability(Capability capability,
			List<ServiceElementManagementObject> managementObjects) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
	 * ManagementObjectCapabilityManagerLocalInterface#getCapabilities()
	 */
	@Override
	public List<Capability> getCapabilities() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.capability.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getMechanisms(java.util.List)
	 */
	@Override
	public List<Mechanism> getMechanisms(List<Capability> capabilites) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
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
	 * @see edu.harvard.integer.capability.
	 * ManagementObjectCapabilityManagerLocalInterface
	 * #getUnassignedServiceElementManagementObjects()
	 */
	@Override
	public List<ServiceElementManagementObject> getUnassignedServiceElementManagementObjects() {

		return null;
	}
}
