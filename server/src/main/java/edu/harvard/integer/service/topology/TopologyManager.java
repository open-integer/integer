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

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.InterNetworkLink;
import edu.harvard.integer.common.topology.LayerTypeEnum;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
import edu.harvard.integer.common.topology.Path;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.topology.InterDeviceLinkDAO;
import edu.harvard.integer.service.persistance.dao.topology.InterNetworkLinkDAO;
import edu.harvard.integer.service.persistance.dao.topology.NetworkDAO;
import edu.harvard.integer.service.persistance.dao.topology.PathDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.TopologyElementDAO;

/**
 * 
 * @author David Taylor
 *
 */
@Stateless
public class TopologyManager extends BaseManager implements TopologyManagerLocalInterface, TopologyManagerRemoteInterface {

	@Inject
	private Logger logger;
	
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
		
		Network[] networks = dao.findAll();
		
		networks = dao.copyArray(networks);
		
		return networks;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getNetworkInformation()
	 */
	@Override
	public NetworkInformation getNetworkInformation() throws IntegerException {
		NetworkInformation networkInfo = new NetworkInformation();
		
		networkInfo.setNetworks(getAllNetworks());
		
		InterNetworkLinkDAO linkDao = persistenceManager.getInterNetworkLinkDAO();
		networkInfo.setLinks(linkDao.copyArray((InterNetworkLink[]) linkDao.findAll()));
		
		logger.info("Found " + networkInfo.getNetworks().length + " networks");
		for (Network network : networkInfo.getNetworks()) {
			logger.info("Network: " + network.getName());
			
			
			for (InterNetworkLink link : networkInfo.getLinks()) {
				if (link.getSourceNetworkId() != null &&
						link.getSourceNetworkId().equals(network.getID()))
					logger.info("    Link: " + link.getName());
			}
		}
		
		
		return networkInfo;
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
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getLinksBySourceDestAddress(edu.harvard.integer.common.Address, edu.harvard.integer.common.Address)
	 */
	@Override
	public InterDeviceLink[] getInterDeviceLinksBySourceDestAddress(Address sourceAddress, Address destAddress) throws IntegerException {
		InterDeviceLinkDAO dao = persistenceManager.getInterDeviceLinkDAO();
	
		return dao.findBySourceDestAddress(sourceAddress, destAddress);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#updateInterDeviceLink(edu.harvard.integer.common.topology.InterDeviceLink)
	 */
	@Override
	public InterDeviceLink updateInterDeviceLink(InterDeviceLink interDeviceLink) throws IntegerException {
		InterDeviceLinkDAO dao = persistenceManager.getInterDeviceLinkDAO();
		
		interDeviceLink = dao.update(interDeviceLink);
		
//		checkNetworks(interDeviceLink);
		
		return interDeviceLink;
	}
	
	/**
	 * @param interDeviceLink
	 * @throws IntegerException 
	 */
	private void checkNetworks(InterDeviceLink interDeviceLink) throws IntegerException {

		InterNetworkLinkDAO linkDao = persistenceManager.getInterNetworkLinkDAO();
		NetworkDAO networkDao = persistenceManager.getNetworkDAO();
		
		logger.info("Create network name from " + interDeviceLink.getSourceAddress().getAddress()
				 + " and " + interDeviceLink.getSourceAddress().getMask());
		
		String sourceNetworkName = Network.createName(interDeviceLink.getSourceAddress());
		String destNetworkName = Network.createName(interDeviceLink.getDestinationAddress());
		Network sourceNetwork = networkDao.findByName(sourceNetworkName);
		if (sourceNetwork == null)
			sourceNetwork = networkDao.update(createNetwork(sourceNetworkName));
		
		addInterDeviceLinkToNetwork(interDeviceLink, sourceNetwork);
		addServiceElementToNetwork(interDeviceLink.getSourceServiceElementId(), sourceNetwork);
		
		Network destNetwork = networkDao.findByName(destNetworkName);
		if (destNetwork == null)
			destNetwork = networkDao.update(createNetwork(destNetworkName));
		
		InterNetworkLink[] networkLinks = linkDao.findBySourceDestID(sourceNetwork.getID(), destNetwork.getID());
		if (networkLinks == null || networkLinks.length == 0) {
			InterNetworkLink link = new InterNetworkLink();
			link.setCreated(new Date());
			link.setModified(new Date());
			
			link.setSourceAddress(new Address(Address.getSubNet(interDeviceLink.getSourceAddress()),
						interDeviceLink.getSourceAddress().getMask()));
			link.setSourceNetworkId(sourceNetwork.getID());
			
			link.setDestinationAddress(new Address(Address.getSubNet(interDeviceLink.getDestinationAddress()),
					interDeviceLink.getDestinationAddress().getMask()));
		
			link.setDestinationNetworkId(destNetwork.getID());
			
			link.setLayer(LayerTypeEnum.Two);
			link.setName(sourceNetworkName + " - " + destNetworkName);
			
			linkDao.update(link);
		}
		
	}

	/**
	 * @param sourceServiceElementId
	 * @param sourceNetwork
	 * @throws IntegerException 
	 */
	private void addServiceElementToNetwork(ID sourceServiceElementId,
			Network sourceNetwork) throws IntegerException {
		
		if (sourceServiceElementId == null)
			return;
		
		if (sourceNetwork.getServiceElements() == null)
			sourceNetwork.setServiceElements(new ArrayList<ServiceElement>());
		
		ServiceElementDAO dao = persistenceManager.getServiceElementDAO();
		ServiceElement serviceElement = dao.findById(sourceServiceElementId);
		sourceNetwork.getServiceElements().add(serviceElement);
		
	}

	/**
	 * @param interDeviceLink
	 * @param sourceNetwork
	 */
	private void addInterDeviceLinkToNetwork(InterDeviceLink interDeviceLink,
			Network sourceNetwork) {
		
		if (sourceNetwork.getInterDeviceLinks() == null)
			sourceNetwork.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
		
		boolean foundLink = false;
		for (InterDeviceLink link : sourceNetwork.getInterDeviceLinks()) {
		
			if (link.getSourceServiceElementId() != null &&
					link.getSourceServiceElementId().equals(interDeviceLink.getSourceServiceElementId()) &&
					link.getDestinationServiceElementId().equals(interDeviceLink.getDestinationServiceElementId()))
				foundLink = true;
		}
		
		if (!foundLink)
			sourceNetwork.getInterDeviceLinks().add(interDeviceLink);
		
	}

	private Network createNetwork(String address) {
		Network network = new Network();
		network.setCreated(new Date());
		network.setModified(new Date());
		network.setName(address);
		network.setServiceElements(new ArrayList<ServiceElement>());
		network.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
		
		return network;
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
	 * @see edu.harvard.integer.service.topology.TopologyManagerInterface#getTopologyElementsByServiceElement(edu.harvard.integer.common.ID)
	 */
	@Override
	public TopologyElement[] getTopologyElementsByServiceElement(ID serviceElementId) throws IntegerException {
		TopologyElementDAO dao = persistenceManager.getTopologyElementDAO();
		
		return dao.findByServiceElementID(serviceElementId);
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
	
	@Override
	public Path updatePath(Path path) throws IntegerException {
		PathDAO dao = persistenceManager.getPathDAO();
		
		return dao.update(path);
	}
	
	@Override
	public Path[] getAllPaths() throws IntegerException {
		PathDAO dao = persistenceManager.getPathDAO();
		
		return dao.findAll();
	}
	
	@Override
	public Path getPathBySourceDestAddress(Address sourceAddress, Address destAddress) throws IntegerException {
		PathDAO dao = persistenceManager.getPathDAO();
		
		return dao.findBySourceDestAddress(sourceAddress, destAddress);
	}
}
