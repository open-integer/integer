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

package edu.harvard.integer.service.topology.device;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.print.attribute.standard.Severity;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;

/**
 * @author David Taylor
 *
 */
@Stateless
public class ServiceElememtAccessManager extends BaseManager implements ServiceElementAccessManagerInterface {
	@Inject
	private Logger logger;
		
	@Inject
	private PersistenceManagerInterface dbm;

	/**
	 * Add or update a service element. If the service element does not exist in the database. Then 
	 * a new service element will be created. If the service element exists in then the service element
	 * will be updated.
	 * 
	 */
	@Override
	public ServiceElement updateServiceElement(ServiceElement serviceElement) throws IntegerException {
		
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		serviceElement = serviceElementDAO.update(serviceElement);
		
		if (logger.isDebugEnabled())
			logger.debug("Save ServiceElement " + serviceElement);
		
		return serviceElement;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getAllServiceElements()
	 */
	@Override
	public ServiceElement[] getAllServiceElements() throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		ServiceElement[] serviceElements = serviceElementDAO.findAll();
		
		return serviceElements;
	}
	
	public ServiceElement getServiceElementByUninque(ID parentId, ManagementObjectValue value) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		return null;
		//return serviceElementDAO.findByIdAndValue(parentId, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getTopLevelServiceElements()
	 */
	@Override
	public ServiceElement[] getTopLevelServiceElements() throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		ServiceElement[] topLevel = serviceElementDAO.findTopLevelServiceElements();
		
		for (int i = 0; i < topLevel.length; i++) {
			logger.info("Top level Service element " + topLevel[i].getID());
			
			topLevel[i] = serviceElementDAO.createCleanCopy(topLevel[i]);
				
		}
		return topLevel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getServiceElementByParentId(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElement[] getServiceElementByParentId(ID parentId) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		ServiceElement[] topLevel = serviceElementDAO.findByParentId(parentId);
		for (int i = 0; i < topLevel.length; i++) {
			
			topLevel[i] = serviceElementDAO.createCleanCopy(topLevel[i]);
			logger.info("Service element " + topLevel[i].getID() + " Parent " + parentId);	
		}
		return topLevel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#deleteServiceElememts(edu.harvard.integer.common.ID[])
	 */
	@Override
	public void deleteServiceElememts(ID[] ids) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		serviceElementDAO.delete(ids);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getDeviceDetails(edu.harvard.integer.common.ID)
	 */
	@Override
	public DeviceDetails getDeviceDetails(ID serviceElementId) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();
		
		ServiceElement serviceElement = serviceElementDAO.findById(serviceElementId);
		
		DeviceDetails deviceDetails = new DeviceDetails();
		deviceDetails.setServiceElementId(serviceElementId);
		deviceDetails.setComment(serviceElement.getComment());
		deviceDetails.setCreated(serviceElement.getCreated());
		deviceDetails.setDescription(serviceElement.getDescription());
		deviceDetails.setOperationalControlId(serviceElement.getOperationalControlId());
		deviceDetails.setPrimaryLocation(serviceElement.getPrimaryLocation());
		deviceDetails.setServiceElementCriticality(serviceElement.getServiceElementCriticality());
		deviceDetails.setUpdated(serviceElement.getUpdated());
		
		return deviceDetails;
	}
}
