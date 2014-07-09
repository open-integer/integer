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

package edu.harvard.integer.service.persistance.dao.topology;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 */
public class NetworkDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public NetworkDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, Network.class);

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
		
		if (!(entity instanceof Network))
			return;
		
		Network network = (Network) entity;
		if (network.getLowerNetworks() != null) {
			List<Network> dbLowerNetworks = new ArrayList<Network>();
			for (Network lowerNetwork : network.getLowerNetworks()) {
				dbLowerNetworks.add(update(lowerNetwork));
			}
			
			network.setLowerNetworks(dbLowerNetworks);
		}
		
		if (network.getInterDeviceLinks() != null) {
			InterDeviceLinkDAO interDeviceLinkDao = new InterDeviceLinkDAO(getEntityManager(), getLogger());
			
			List<InterDeviceLink> dbLinks = new ArrayList<InterDeviceLink>();
			
			for (InterDeviceLink interDeviceLink : network.getInterDeviceLinks()) {
				dbLinks.add(interDeviceLinkDao.update(interDeviceLink));
			}
			
			network.setInterDeviceLinks(dbLinks);
		}
		
		
		if (network.getServiceElements() != null) {
			ServiceElementDAO dao = new ServiceElementDAO(getEntityManager(), getLogger());
			List<ServiceElement> dbServiceElements = new ArrayList<ServiceElement>();
			
			for(ServiceElement serviceElement : network.getServiceElements()) {
				dbServiceElements.add(dao.update(serviceElement));
			}
			
			network.setServiceElements(dbServiceElements);
		}
		
		super.preSave(entity);
	}

	
}
