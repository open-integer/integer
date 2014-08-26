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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.ChangedField;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.EnvironmentLevel;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementFields;
import edu.harvard.integer.common.topology.ServiceElementHistory;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.selection.SelectionDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SnmpV2CredentialDAO;
import edu.harvard.integer.service.persistance.dao.topology.EnvironmentLevelDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementHistoryDAO;
import edu.harvard.integer.service.persistance.dao.topology.TopologyElementDAO;

/**
 * @see ServiceElementAccessManagerInterface
 * 
 * @author David Taylor
 * 
 */
@Stateless
public class ServiceElememtAccessManager extends BaseManager implements
		ServiceElementAccessManagerLocalInterface,
		ServiceElementAccessManagerRemoteInterface {

	

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface dbm;

	/**
	 * @param managerType
	 */
	public ServiceElememtAccessManager() {
		super(ManagerTypeEnum.ServiceElementAccessManager);

	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#updateServiceElement(edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public ServiceElement updateServiceElement(ServiceElement serviceElement)
			throws IntegerException {

		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		ServiceElement dbServiceElement = getDatabaseServiceElement(serviceElement);
		if (dbServiceElement != null) {
			logger.info("Found existing service element: " + dbServiceElement.getID().toDebugString() + " Will update instead of create new ");
			
			List<ChangedField> changedFields = serviceElementDAO.copyFieldsGetChanges(dbServiceElement, serviceElement);
			serviceElement = dbServiceElement;
			
			if (changedFields != null && changedFields.size() > 0) {
				
					
				StringBuffer b = new StringBuffer();
				b.append("Changes for ").append(serviceElement.getID().toDebugString());
				for (Iterator<ChangedField> itr = changedFields.iterator(); itr.hasNext(); ) {
					ChangedField change = itr.next();
				
					if (change.getFieldName().equals("getIdentifier") || change.getFieldName().equals("getUpdated")) {
						itr.remove();
						
					} else {
						b.append(" ").append(change.getFieldName());
						b.append(" new ").append(change.getNewValue());
						b.append(" old ").append(change.getOldValue());
					
					}
				}
				
				if (changedFields.size() > 0) {
					logger.info(b.toString());

					ServiceElementHistory serviceElementHistory = new ServiceElementHistory();
					ServiceElementHistoryDAO serviceElementHistoryDAO = dbm.getServiceElementHistoryDAO();
					serviceElementHistoryDAO.copyFields(serviceElementHistory, serviceElement);
					serviceElementHistory.setServiceElementId(serviceElement.getID());
					serviceElementHistory.setChangedFields(changedFields);

					serviceElementHistoryDAO.update(serviceElementHistory);
				}
			}
		}
		
		serviceElement = serviceElementDAO.update(serviceElement);

		if (logger.isDebugEnabled())
			logger.debug("Save ServiceElement " + serviceElement);

		return serviceElement;
	}

	
	private ServiceElement getDatabaseServiceElement(ServiceElement serviceElement) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		if (serviceElement.getParentIds() != null && serviceElement.getParentIds().size() > 0) {
			for (ID parentId : serviceElement.getParentIds()) {
				ServiceElementFields[] dbServiceElement = (ServiceElementFields[]) serviceElementDAO.findByParentIdAndName(parentId, serviceElement.getName());
				if (dbServiceElement != null && dbServiceElement.length > 0)
					return (ServiceElement) dbServiceElement[0];
			}
			return null;
			
		} else
			return (ServiceElement) serviceElementDAO.findByName(serviceElement.getName());
	
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface#getAllServiceElements()
	 */
	@Override
	public ServiceElement[] getAllServiceElements() throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		ServiceElement[] serviceElements = serviceElementDAO.findAll();

		return serviceElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface#getTopLevelServiceElements()
	 */
	@Override
	public ServiceElement[] getTopLevelServiceElements()
			throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		ServiceElement[] topLevel = (ServiceElement[]) serviceElementDAO
				.findTopLevelServiceElements();

		for (int i = 0; i < topLevel.length; i++) {
			logger.info("Top level Service element " + topLevel[i].getID());

			topLevel[i] = serviceElementDAO.createCleanCopy(topLevel[i]);

		}

		return topLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #getServiceElementByParentId(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElement[] getServiceElementByParentId(ID parentId)
			throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		logger.info("Get child service elements for parent " + parentId.toDebugString());
		
		ServiceElement[] topLevel = (ServiceElement[]) serviceElementDAO.findByParentId(parentId);
		for (int i = 0; i < topLevel.length; i++) {

			topLevel[i] = serviceElementDAO.createCleanCopy(topLevel[i]);
			if (logger.isDebugEnabled())
				logger.info("Service element " + topLevel[i].getID() + " Parent "
						+ parentId);
		}
		return topLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #getServiceElementBySelection(edu.harvard
	 * .integer.common.selection.Selection)
	 */
	@Override
	public ServiceElement[] getTopLevelServiceElementBySelection(
			Selection selection) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		return (ServiceElement[]) serviceElementDAO.findBySelection(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #getServiceElementBySelection(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElement[] getTopLevelServiceElementBySelection(ID selectionId)
			throws IntegerException {
		SelectionDAO dao = dbm.getSelectionDAO();
		Selection selection = dao.findById(selectionId);

		return getTopLevelServiceElementBySelection(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #deleteServiceElememts(edu.harvard.integer.common.ID[])
	 */
	@Override
	public void deleteServiceElememts(ID[] ids) throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		serviceElementDAO.delete(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #getDeviceDetails(edu.harvard.integer.common.ID)
	 */
	@Override
	public DeviceDetails getDeviceDetails(ID serviceElementId)
			throws IntegerException {
		ServiceElementDAO serviceElementDAO = dbm.getServiceElementDAO();

		ServiceElement serviceElement = serviceElementDAO
				.findById(serviceElementId);

		DeviceDetails deviceDetails = new DeviceDetails();
		deviceDetails.setServiceElementId(serviceElementId);
		deviceDetails.setComment(serviceElement.getComment());
		deviceDetails.setCreated(serviceElement.getCreated());
		deviceDetails.setDescription(serviceElement.getDescription());
		deviceDetails.setOperationalControlId(serviceElement
				.getOperationalControlId());
		deviceDetails.setPrimaryLocation(serviceElement.getPrimaryLocation());
		deviceDetails.setServiceElementCriticality(serviceElement
				.getServiceElementCriticality());
		deviceDetails.setUpdated(serviceElement.getUpdated());

		return deviceDetails;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getServiceElementByName(java.lang.String)
	 */
	@Override
	public ServiceElement getServiceElementByName(String name) throws IntegerException {
		ServiceElementDAO dao = dbm.getServiceElementDAO();
		
		return (ServiceElement) dao.findByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.topology.device.
	 * ServiceElementAccessManagerInterface
	 * #getServiceElementByIpAddress(java.lang.String)
	 */
	@Override
	public ServiceElement getServiceElementByIpAddress(String ipAddress)
			throws IntegerException {
		
		ServiceElementDAO dao = dbm.getServiceElementDAO();
		TopologyElementDAO topologyElementDao = dbm.getTopologyElementDAO();
		
		TopologyElement[] findByAddress = topologyElementDao.findByAddress(ipAddress);
		for (TopologyElement topologyElement : findByAddress) {
			ServiceElement serviceElement = dao.findById(topologyElement.getServiceElementId());
			if (serviceElement != null)
				return serviceElement;
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getTopLevelServiceElementByIpAddress(java.lang.String)
	 */
	@Override
	public ServiceElement getTopLevelServiceElementByIpAddress(String ipAddress) throws IntegerException {
		ServiceElementDAO dao = dbm.getServiceElementDAO();
		return getTopLevel(getServiceElementByIpAddress(ipAddress), dao);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getServiceElementsBySelection(edu.harvard.integer.common.selection.Selection)
	 */
	@Override
	public ServiceElement[] getServiceElementsBySelection(Selection selection) throws IntegerException {
		ServiceElementDAO dao = dbm.getServiceElementDAO();
		
		logger.info("Get ServiceElementsBySeletion " + selection.getID().toDebugString());
		
		return (ServiceElement[]) dao.findBySelection(selection);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getTopLevelServicesElementsBySelection(edu.harvard.integer.common.selection.Selection)
	 */
	@Override
	public ServiceElement[] getTopLevelServicesElementsBySelection(Selection selection) throws IntegerException {
		ServiceElement[] serviceElements = getServiceElementsBySelection(selection);
		
		HashMap<ID, ServiceElement> topLevelServiceElements = new HashMap<ID, ServiceElement>();
		
		ServiceElementDAO dao = dbm.getServiceElementDAO();
		
		for (ServiceElement serviceElement : serviceElements) {
			if (serviceElement.getParentIds() == null || serviceElement.getParentIds().size() == 0)
				topLevelServiceElements.put(serviceElement.getID(), serviceElement);
			else {
				ServiceElement topLevel = getTopLevel(serviceElement, dao);
				if (topLevel != null && topLevelServiceElements.get(topLevel.getID()) == null)
					topLevelServiceElements.put(topLevel.getID(), topLevel);
			}
		}
		
		return serviceElements;
	}

	/**
	 * @param serviceElement
	 * @return
	 * @throws IntegerException 
	 */
	private ServiceElement getTopLevel(ServiceElement serviceElement, ServiceElementDAO dao) throws IntegerException {
		
		if ( serviceElement == null ) {
			return null;
		}
		if (serviceElement.getParentIds() != null && serviceElement.getParentIds().size() > 0) {
			ServiceElement parent = dao.findById(serviceElement.getParentIds().get(0));
			return getTopLevel(parent, dao);
		} else
			return serviceElement;
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getAllEnvironmentLevels()
	 */
	@Override
	public EnvironmentLevel[] getAllEnvironmentLevels() throws IntegerException {
		EnvironmentLevelDAO dao = dbm.getEnvironmentLevelDAO();
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#updateEnvironmentLevel(edu.harvard.integer.common.topology.EnvironmentLevel)
	 */
	@Override
	public EnvironmentLevel updateEnvironmentLevel(EnvironmentLevel environmentLevel) throws IntegerException {
		EnvironmentLevelDAO dao = dbm.getEnvironmentLevelDAO();
		
		return dao.update(environmentLevel);
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface#getCredentialById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Credential getCredentialById(ID id) throws IntegerException {
		
		SnmpV2CredentialDAO dao =  dbm.getSnmpV2cCredentailDAO();
		return dao.findById(id);
	}
}
