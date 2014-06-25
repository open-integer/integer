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
package edu.harvard.integer.service.discovery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;
import edu.harvard.integer.service.discovery.subnet.Ipv4Range;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.distribution.ServiceTypeEnum;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;


/**
 * The Class NetworkDiscovery provides method for topology discovery. 
 * The first stage of the discovery is the scanning subnet to find out any IP devices on subnet.
 * The second stage of the discovery is the service element discovery.
 * The third stage of the discovery is the topology discovery.
 * 
 *  The topology discovery is based Layer2, Layer3 and end host found in the first stage.
 *  When the topology discovery is done, it should have connection information between IP nodes.
 * 
 * It manages 3 different pools for discovery.
 * 
 * Discovery request pool which controls how many concurrent request we should have.
 * Subnet discovery pool which controls how many concurrent subnet discovery we can have.
 * Element discovery pool which controls how many concurrent element discovery.we can have.
 *
 * @author dchan
 * @param <T> the generic type
 */
public class NetworkDiscovery  implements NetworkDiscoveryBase {

	public static String IPIDENTIFY = "IpIdentify";
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
 	
	
	/** The layer3 nodes. */
	private ConcurrentHashMap<String, DiscoverNode>  l3Nodes = new ConcurrentHashMap<>();
	
	/** The layer2 nodes. */
	private ConcurrentHashMap<String, DiscoverNode> l2Nodes = new ConcurrentHashMap<>();
	
	/** The discover nodes. The key is the IPAddress on the discover node during discvoery.  */
	private ConcurrentHashMap<String, DiscoverNode> endNodes = new ConcurrentHashMap<>();
	
	/**
	 * Map to keep track of each subnet tasks
	 */
	private ConcurrentHashMap<String, DiscoverSubnetAsyncTask>  subnetTasks = new ConcurrentHashMap<>();
    

	/** The discover seed. */
	private final List<IpDiscoverySeed> discoverSeeds;
	
	/** The stop discovery. */
	private volatile boolean stopDiscovery;
		
	/** Top level polling variable bindings.  It is getting from IntegerInterface and instantiate in the beginning
	 *  for performance reason.
	 * 
	 */
	private List<VariableBinding> topLevelVBs;
	
	/**
	 * Discovery id to keep track of discovery.
	 */
	private final DiscoveryId discoverId;
	
	
	
	/**
	 * Create a discovery  
	 * 
	 * @param seed
	 * @param toplevelVarBinds
	 * @param id
	 */
	public NetworkDiscovery(IpDiscoverySeed seed, List<VariableBinding> toplevelVarBinds, DiscoveryId id)  {
		this.discoverId = id;
		List<IpDiscoverySeed> seeds = new ArrayList<IpDiscoverySeed>();
		seeds.add(seed);
		this.discoverSeeds = seeds;
		this.topLevelVBs = toplevelVarBinds;
	}
	
	
	/**
	 * Discover network. Each subnet it will spawn a thread for discovery.
	 * The subnet information is contained in each IpDiscoverySeed
	 * @throws  
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Future<Ipv4Range>> discoverNetwork()  {
		
		logger.debug("In discoverNetwork ");
		
		List<Future<Ipv4Range>> discFuture = new ArrayList<>();
		/**
		 * Create subnet tasks based on discover configuration subnet.
		 */
		if ( discoverSeeds != null ) {
			
			DiscoveryServiceInterface discoveryService = null;
			try {
				discoveryService = DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
			} catch (IntegerException e1) {
				
				logger.error("Error getting DiscoveryService " + e1.toString(), e1);
			}
		
			for ( IpDiscoverySeed discoverSeed : discoverSeeds ) {
				
				try {
					@SuppressWarnings("unchecked")
					DiscoverSubnetAsyncTask<ElementAccess> subTask = new DiscoverSubnetAsyncTask(this, discoverSeed);
	                subnetTasks.put(subTask.getSeed().getSeedId(), subTask);
					
					Future<Ipv4Range> v = discoveryService.submitSubnetDiscovery(subTask);
					discFuture.add(v);
					
				} catch (IntegerException e) {
					
					logger.equals("Error on submit subnet discover...  " + e.toString() );
					e.printStackTrace();
				} 
			}
		}
		return discFuture;

	}


	
	/**
	 * Gets the l3 nodes.
	 *
	 * @return the l3 nodes
	 */
	public ConcurrentHashMap<String, DiscoverNode> getL3Nodes() {
		return l3Nodes;
	}



	/**
	 * Gets the l2 nodes.
	 *
	 * @return the l2 nodes
	 */
	public ConcurrentHashMap<String, DiscoverNode> getL2Nodes() {
		return l2Nodes;
	}



	/**
	 * Gets the end nodes.
	 *
	 * @return the end nodes
	 */
	public ConcurrentHashMap<String, DiscoverNode> getEndNodes() {
		return endNodes;
	}



	/**
	 * Discovered element. -- Be called after discovered each service element. It is used to 
	 * indicate that discovery being finished.
	 *
	 * @param elm the discoverd element.
	 */
	public void discoveredElement(DiscoverNode discoverNode, String subnetId ) {
		
		try {
			((DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService)).discoveredServiceElement(discoverNode.getAccessElement());
		} catch (IntegerException e) {
			
			logger.error("Error saveing ServiceElement " + discoverNode.getAccessElement());
		}
		
		removeIpAddressFromSubnet(discoverNode.getIpAddress(), subnetId, true);
	}
	
	
	/**
	 * Error occur -- Call when errors occurs during discovering.
	 *
	 * @param errorCode the error code.
	 * @param msg the associated message.
	 */
	public void discoverErrorOccur( ErrorCodeInterface errorCode, String msg ) {
		try {
			((DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService)).discoveryError(discoverId, errorCode, null);
		} catch (IntegerException e) {
			
			logger.error("Error sending error " + errorCode + " args " + msg);
		}
		
	}
	
	
	
	/**
	 * This method being called when there is no response for a IP address during discover.
	 * If on the database there is a service element is associated with this IP address.  It should issue
	 * a no response message.
	 * 
	 * @param ipAddress
	 * @param subnetId
	 */
	public void ipAddressNoResponse( String ipAddress, String subnetId ) {
		
		logger.debug("No response for this IP " + ipAddress + " for this subnet " + subnetId );
		try {
			ServiceElementAccessManagerInterface access = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			ServiceElement se =  access.getServiceElementByIpAddress(ipAddress);
			if ( se != null ) {
				DiscoveryServiceInterface dsif = (DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
				dsif.discoveryServiceElementNoResponse(se, ipAddress);
			}
						
		} catch (IntegerException e) {
		
			e.printStackTrace();
			logger.error("Unable to call Service Manager to mark no response on a service element !! " + e.toString());
		}
		
		removeIpAddressFromSubnet(ipAddress, subnetId, false);
	}
	
	
	

	/**
	 * Checks if the discovery being stopped.
	 *
	 * @return true, if is stop discovery
	 */
	public boolean isStopDiscovery() {
		return stopDiscovery;
	}



	/**
	 * Remove IP Address discovery from subnet map. This method being called a IP device being done 
	 * with discovery.  
	 * 
	 * @param ip
	 * @param subnetid
	 */
	private void removeIpAddressFromSubnet( String ip, String subnetid, boolean elmComplete ) {
		
		if ( subnetid != null ) {
			@SuppressWarnings("unchecked")
			DiscoverSubnetAsyncTask<ElementAccess> subTask = subnetTasks.get(subnetid);
			if ( subTask != null ) {
				
				subTask.removeDiscoverNode(ip);
				if ( subTask.discoveryNodeCount() == 0 ) {
					
					subnetTasks.remove(subnetid);
					
					if ( elmComplete ) {
						try {
							DiscoveryServiceInterface dsif = (DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
							dsif.discoveryComplete(discoverId);
						} catch (IntegerException e) {
						
							e.printStackTrace();
							logger.error("Unable to call DiscoveryService to mark discovery complete!! " + e.toString());
						}
					}
					logger.debug("Discovered subnet **** " + subnetid);
				}
			}
			if ( subnetTasks.size() == 0 ) {
				try {
					DiscoveryServiceInterface dsif = (DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
					dsif.discoveryComplete(discoverId);
				} catch (IntegerException e) {
				
					e.printStackTrace();
					logger.error("Unable to call DiscoveryService to mark discovery complete!! " + e.toString());
				}
				
			}
		}
	}
	
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.NetworkDiscoveryBase#stopDiscovery()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stopDiscovery() {
		
		for ( DiscoverSubnetAsyncTask<ElementAccess> subTask : subnetTasks.values() ) {
			
			subTask.stopDiscover();
		}
		stopDiscovery = true;
		
	}


	/**
	 * Retrieve the top level of VB (Variable Bindings in System Group)
	 * @return
	 */
	public List<VariableBinding> getTopLevelVBs() {
		return topLevelVBs;
	}



	/*
	 * Return the discovery id.
	 */
	public DiscoveryId getDiscoverId() {
		return discoverId;
	}


}
