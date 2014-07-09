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

package edu.harvard.integer.service.topology;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.topology.InterDeviceLinkDAO;
import edu.harvard.integer.service.persistance.dao.topology.NetworkDAO;
import edu.harvard.integer.service.persistance.dao.topology.TopologyElementDAO;

/**
 * 
 * @author David Taylor
 *
 */
@Stateless
public class TopologyManager extends BaseManager implements TopologyManagerLocalInterface, TopologyManagerRemoteInterface {

	
	@Inject
	private PersistenceManagerInterface persistenceManager;
	
	/**
	 * @param managerType
	 */
	public TopologyManager() {
		super(ManagerTypeEnum.TopologyManager);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getAllNetworks()
	 */
	@Override
	public Network[] getAllNetworks() throws IntegerException {
		NetworkDAO dao = persistenceManager.getNetworkDAO();
		
		return dao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#updateNetwork(edu.harvard.integer.common.topology.Network)
	 */
	@Override
	public Network updateNetwork(Network network) throws IntegerException {
		NetworkDAO dao = persistenceManager.getNetworkDAO();
		
		return dao.update(network);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getAllInterDeviceLinks()
	 */
	@Override
	public InterDeviceLink[] getAllInterDeviceLinks() throws IntegerException {
		InterDeviceLinkDAO dao = persistenceManager.getInterDeviceLinkDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#updateInterDeviceLink(edu.harvard.integer.common.topology.InterDeviceLink)
	 */
	@Override
	public InterDeviceLink updateInterDeviceLink(InterDeviceLink interDeviceLink) throws IntegerException {
		InterDeviceLinkDAO dao = persistenceManager.getInterDeviceLinkDAO();
		
		return dao.update(interDeviceLink);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getAllTopologyElements()
	 */
	@Override
	public TopologyElement[] getAllTopologyElements() throws IntegerException {
		TopologyElementDAO dao = persistenceManager.getTopologyElementDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#updateTopologyElement(edu.harvard.integer.common.topology.TopologyElement)
	 */
	@Override
	public TopologyElement updateTopologyElement(TopologyElement topologyElement) throws IntegerException {
		TopologyElementDAO dao = persistenceManager.getTopologyElementDAO();
		return dao.update(topologyElement);
	}
	
}
