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
package edu.harvard.integer.service.discovery.snmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.LayerTypeEnum;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.topology.TopologyManagerInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;

/**
 * The Class DiscoverSubnetTopologyTask.  The topology discovery is assumed all nodes have been discovered
 * on subnet discover stage.  The discover algorithm is level based discover starting from a root node.
 * The root node can be any node from a discovered node list. The root is considered as level 1 and nodes
 * that are connected to it are level 2 and so on.
 * 
 * When a node being processed from the node list it will be removed from the list. The process is repeating 
 * for disconnected group of devices from a node list until the node list is empty.
 * 
 * When a CDP remove connection is a untouchable node(SNMP requests are being blocked on that node),  it will
 * create a generic service element for the remove node. 
 *
 * @author dchan
 */
public class DiscoverCdpTopologyTask implements Callable<Void> {

	/** The disc nodes. */
	private List<DiscoverNode>  discNodes;
	
	/** The net disc. */
	private NetworkDiscovery netDiscover;
	
	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(DiscoverCdpTopologyTask.class);
	
	/**
	 * Instantiates a new discover subnet topology task.
	 *
	 * @param discNodes the disc nodes
	 * @param netDisc the net disc
	 */
	public DiscoverCdpTopologyTask( List<DiscoverNode> discNodes,
			                           NetworkDiscovery netDisc ) {
		
		this.discNodes = discNodes;
		this.netDiscover = netDisc;		
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call()  {
		
		try {
			try {
				discoverNodeLink();
			} 
			catch (IntegerException e) {
				
				e.printStackTrace();
				logger.error("Cdp discovery error on network discover id " + netDiscover.getDiscoverId().toString() + " " + e.getLocalizedMessage());
			}
		}
		finally {
			netDiscover.discoverTopologyComplete();
		}
		return null;
	}
	
	
	
	/**
	 * This method will pick the first node from the list as a root node and discover
	 * neighboring of the node and build links.  
	 * 
	 * @throws IntegerException
	 */
	private void discoverNodeLink() throws IntegerException {
		
		/**
		 *  Pick the first node as the root node to discover the topology on the subnet.
		 */
		DiscoverNode dn = discNodes.remove(0);
		DeviceTopologyInfo deviceInfo =  dn.getTopologyInfo();
		List<NetworkConnection> conns = deviceInfo.getNetConnections();
	
		List<CdpConnectionNode> nextLevelNodes = new ArrayList<>();		
		for ( NetworkConnection conn : conns ) {
			
			if ( conn instanceof CdpConnection ) {
				
				CdpConnection cdpConn = (CdpConnection) conn;
				discoverLink(dn, cdpConn, deviceInfo, nextLevelNodes);
			}
		}
		
		/**
		 * If the next level nodes are not empty, find the next level links.
		 */
		if ( nextLevelNodes.size() > 0 ) {
			findNextLevelLinks(nextLevelNodes);
		}
		
		/**
		 * If discover nodes are not empty, call discprverNodesLink recursively.   
		 */
		if ( discNodes.size() > 0 ) {
			discoverNodeLink();
		}
		
	}
	
	
	
	
	
	/**
	 * Find next level links of a list of CdpConnectionNodes.  
	 *
	 * @param connNodes the conn nodes
	 * @param topologyMgr the topology mgr
	 * @throws IntegerException the integer exception
	 */
	public void findNextLevelLinks( List<CdpConnectionNode> connNodes ) throws IntegerException {
		
		List<CdpConnectionNode> nextLevelNodes = new ArrayList<>();	
		for ( CdpConnectionNode connNode : connNodes ) {
			
			DiscoverNode dn = connNode.associatedNode;
			DeviceTopologyInfo deviceInfo =  dn.getTopologyInfo();
			List<NetworkConnection> conns = deviceInfo.getNetConnections();
			
			for ( NetworkConnection conn : conns ) {
				
				if ( conn instanceof CdpConnection ) {
					
					CdpConnection cdpConn = (CdpConnection) conn;
					discoverLink(dn, cdpConn, deviceInfo, nextLevelNodes);				
				}
			}
		}
		
		/**
		 * If the next level nodes are not empty, continue next level discover.
		 */
		if ( nextLevelNodes.size() > 0 ) {
			findNextLevelLinks(nextLevelNodes);
		}
	}
	
	
	
	/**
	 * Discover a link between two nodes.
	 *
	 * @param dn the dn
	 * @param cdpConn the cdp conn
	 * @param deviceInfo the device info
	 * @param nextLevelNodes the next level nodes
	 * @throws IntegerException the integer exception
	 */
	private void discoverLink( DiscoverNode dn,
			                   CdpConnection cdpConn, 
			                   DeviceTopologyInfo deviceInfo,
			                   List<CdpConnectionNode> nextLevelNodes ) throws IntegerException {
		
		TopologyManagerInterface topologyMgr = DistributionManager.getManager(ManagerTypeEnum.TopologyManager);
		ServiceElementDiscoveryManagerInterface discMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		ServiceElementAccessManagerInterface accessMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
		
		TopologyNode foundTn = null;
		for ( TopologyNode tn : deviceInfo.getTopoNodes() ) {
			
			if ( tn.getIfIndex() == cdpConn.getIfIndex() ) {
				foundTn = tn;
				break;
			}
		}
		if ( foundTn.isFoundConnection() ) {
			return;
		}
		
		foundTn.setFoundConnection(true);
		CdpConnectionNode connNode = findAssociatedConnection(cdpConn);
		if ( connNode == null ) {
			connNode = findAssociatedConnection(nextLevelNodes, cdpConn);
		}
		else {
			nextLevelNodes.add(connNode);
		}
		
		TopologyElement sourceTe = foundTn.getTopologyElm();
		if ( sourceTe.getInterDeviceLinks() == null ) {
			sourceTe.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
		}
		if ( connNode != null ) {
			
			connNode.associatedTn.setFoundConnection(true);
			InterDeviceLink upLink = new InterDeviceLink();
			
			upLink.setCreated(new Date());
			upLink.setSourceAddress(foundTn.getTopologyElm().getAddress().get(0));
			upLink.setDestinationAddress(connNode.destAddress);
			upLink.setSourceServiceElementId(dn.getAccessElement().getID());
			upLink.setDestinationServiceElementId(connNode.associatedNode.getAccessElement().getID());
			upLink.setLayer(LayerTypeEnum.Two);
			topologyMgr.updateInterDeviceLink(upLink);
			
			sourceTe.getInterDeviceLinks().add(upLink);
			topologyMgr.updateTopologyElement(sourceTe);
			
			
            InterDeviceLink downLink = new InterDeviceLink();
			downLink.setCreated(new Date());
			downLink.setDestinationAddress(foundTn.getTopologyElm().getAddress().get(0));
			downLink.setSourceAddress(connNode.destAddress);
			downLink.setDestinationServiceElementId(dn.getAccessElement().getID());
			downLink.setSourceServiceElementId(connNode.associatedNode.getAccessElement().getID());
			downLink.setLayer(LayerTypeEnum.Two);
			topologyMgr.updateInterDeviceLink(downLink);
			
			TopologyElement destTe = connNode.associatedTn.getTopologyElm();			
			if ( destTe.getInterDeviceLinks() == null ) {
				destTe.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
			}
			destTe.getInterDeviceLinks().add(downLink);
			topologyMgr.updateTopologyElement(destTe);
		}
		else {
			
			ServiceElementType set = discMgr.getServiceElementTypeByName("unknownSystem");
			ServiceElement nodeSe = netDiscover.getUnknownServiceElement(cdpConn.getRemoteDeviceId());
			
			if ( nodeSe == null ) {
				nodeSe = new ServiceElement();		
				nodeSe.setUpdated(new Date());
				nodeSe.setServiceElementTypeId(set.getID());
				
				nodeSe.setName(cdpConn.getRemoteDeviceId());
				StringBuffer sb = new StringBuffer();
				
				sb.append("System Platform: " + cdpConn.getRemotePlatform());
				sb.append("System version: " + cdpConn.getRemoteVersion());			
				nodeSe.setDescription(sb.toString());
				
				nodeSe = accessMgr.updateServiceElement(nodeSe);
				netDiscover.addUnknownServiceElement(cdpConn.getRemoteDeviceId(), nodeSe);
			}
            
			set = discMgr.getServiceElementTypeByName("connectionEndPort");
			ServiceElement se = new ServiceElement();		
			se.setUpdated(new Date());
			se.setServiceElementTypeId(set.getID());			
			se.setName(cdpConn.getRemotePort());
			se.setDescription("IPAddress: " + cdpConn.getRemoteIpAddress());				
			se = accessMgr.updateServiceElement(se);
			
            InterDeviceLink upLink = new InterDeviceLink();
			upLink.setCreated(new Date());
			upLink.setSourceAddress(foundTn.getTopologyElm().getAddress().get(0));
			Address addr = new Address();
			addr.setAddress(cdpConn.getRemoteIpAddress());	
			upLink.setDestinationAddress(addr);
			upLink.setSourceServiceElementId(dn.getAccessElement().getID());
			upLink.setDestinationServiceElementId(nodeSe.getID());
			upLink.setLayer(LayerTypeEnum.Two);
			
			upLink = topologyMgr.updateInterDeviceLink(upLink);
			
			sourceTe.getInterDeviceLinks().add(upLink);
			sourceTe = topologyMgr.updateTopologyElement(sourceTe);
			
			TopologyElement destTe = new TopologyElement();
			destTe.setAddress(new ArrayList<Address>());
			destTe.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
			
			Address remoteAddr = new Address();
			remoteAddr.setAddress(cdpConn.getRemoteIpAddress());
			destTe.getAddress().add(remoteAddr);
			destTe.setServiceElementId(se.getID());
			
            InterDeviceLink downLink = new InterDeviceLink();
			downLink.setCreated(new Date());
			downLink.setDestinationAddress(foundTn.getTopologyElm().getAddress().get(0));
			
			downLink.setSourceAddress(addr);
			downLink.setDestinationServiceElementId(dn.getAccessElement().getID());
			downLink.setSourceServiceElementId(nodeSe.getID());
			downLink.setLayer(LayerTypeEnum.Two);
			downLink = topologyMgr.updateInterDeviceLink(downLink);
			
			destTe.getInterDeviceLinks().add(downLink);
			destTe = topologyMgr.updateTopologyElement(destTe);
			
			logger.info("Create link for remote ip address " + cdpConn.getRemoteIpAddress());
		}				
		
	}
	
	
	
	
	/**
	 * Find associated connection.
	 *
	 * @param connNodes the conn nodes
	 * @param cdpConn the cdp conn
	 * @return the cdp connection node
	 */
	private CdpConnectionNode  findAssociatedConnection( List<CdpConnectionNode> connNodes, 
			                                             CdpConnection cdpConn ) {
		
		for ( CdpConnectionNode connNode : connNodes ) {
			
			DiscoverNode dn = connNode.associatedNode;
			for ( TopologyNode tn : dn.getTopologyInfo().getTopoNodes() ) {
				
				List<Address> addrs = tn.getTopologyElm().getAddress();
				for ( Address addr : addrs ) {					
					if ( cdpConn.getRemoteIpAddress().equals(addr) ) {
						
						CdpConnectionNode cdpConnNode = new CdpConnectionNode();
						cdpConnNode.associatedNode = dn;
						cdpConnNode.associatedTn = tn;
						cdpConnNode.destAddress = addr;
						return cdpConnNode;
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Find associated connection.
	 *
	 * @param cdpConn the cdp conn
	 * @return the cdp connection node
	 */
	private CdpConnectionNode findAssociatedConnection( CdpConnection cdpConn ) {
		
		for ( int i=0;i<discNodes.size(); i++ ) {
			
			DiscoverNode dn = discNodes.get(i);
			for ( TopologyNode tn : dn.getTopologyInfo().getTopoNodes() ) {
		
				List<Address> addrs = tn.getTopologyElm().getAddress();
				for ( Address addr : addrs ) {					
					if ( cdpConn.getRemoteIpAddress().equals(addr) ) {
						CdpConnectionNode cdpConnNode = new CdpConnectionNode();
						cdpConnNode.associatedNode = dn;
						cdpConnNode.associatedTn = tn;
						cdpConnNode.destAddress = addr;
						
						discNodes.remove(i);
						return cdpConnNode;
					}
				}
			}
		}		
		return null;
	}
	
	
	/**
	 * The Class CdpConnectionNode.
	 */
	public class CdpConnectionNode {
		
		/** The associated node. */
		DiscoverNode associatedNode;
		
		/** The associated tn. */
		TopologyNode associatedTn;
		
		/** The dest address. */
		Address destAddress;
	}

}
