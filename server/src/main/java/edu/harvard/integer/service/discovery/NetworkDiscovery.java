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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;
import edu.harvard.integer.service.discovery.subnet.Ipv4Range;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;


/**
 * The Class NetworkDiscovery provides method for topology discovery. 
 * The first stage of the discovery is the service element discovery.
 * The second stage of the discovery is the topology discovery.
 * 
 *  The topology discovery is based L2, L3 and end host found in the first stage.
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
public class NetworkDiscovery <T extends ServiceElement> implements NetworkDiscoveryBase {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
 	
	
	/** The l3 nodes. */
	private ConcurrentHashMap<String, DiscoverNode>  l3Nodes = new ConcurrentHashMap<>();
	
	/** The l2 nodes. */
	private ConcurrentHashMap<String, DiscoverNode> l2Nodes = new ConcurrentHashMap<>();
	
	/** The end nodes. */
	private ConcurrentHashMap<String, DiscoverNode> endNodes = new ConcurrentHashMap<>();
	
	private ConcurrentHashMap<String, DiscoverSubnetAsyncTask<ElementAccess, T>>  subnetTasks = new ConcurrentHashMap<>();
	
	
	/** The callback for notify the progress of discovery. */
	private ElementDiscoverCB<ServiceElement> cb;
	
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
	private final String discoverId;
	

	/**
	 * Instantiates a new network discovery.
	 *
	 * @param discoverSeed the discover seed
	 * @param callback the callback
	 * @param integerIf the integer if
	 */
	public NetworkDiscovery( final List<IpDiscoverySeed> discoverSeed, 
			                 ElementDiscoverCB<ServiceElement> callback,
			                 String discoveryId ) 
	{
		this.discoverId = discoveryId;
		this.cb = callback;
		this.discoverSeeds = discoverSeed;
		
		List<VariableBinding> vbs = new ArrayList<>();
		ServiceElementDiscoveryManagerInterface manager = null;
		try {
			manager = (ServiceElementDiscoveryManagerInterface) DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		} catch (IntegerException e) {
			// throw new IntegerException(e, SystemErrorCodes.ManagerNotFound);
			logger.error("Unable to get ServiceElementDiscoveryManager " + e.toString());
			return;
		}
		
		List<ServiceElementManagementObject> mgrObjects = manager.getTopLevelPolls();
		for ( ServiceElementManagementObject se : mgrObjects ) {
			
			if ( se instanceof SNMP ) {
		
				SNMP sn = (SNMP) se;
				VariableBinding vb = new VariableBinding(new OID(sn.getOid()));
				vbs.add(vb);
			}
		}
		if ( vbs.size() > 0 ) {
			topLevelVBs = vbs;
		}
	}
	
	
	
	
	/**
	 * Discover network. Each subnet has its own thread for discovery.
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
			
			ExecutorService exService =  DiscoveryManager.getInstance().getSubPool();
			for ( IpDiscoverySeed discoverSeed : discoverSeeds ) {
			
				
				try {
					@SuppressWarnings("unchecked")
					DiscoverSubnetAsyncTask<ElementAccess, T> subTask = new DiscoverSubnetAsyncTask(this, discoverSeed);
	                subnetTasks.put(subTask.getSeed().getSeedId(), subTask);
					
					Future<Ipv4Range> v = exService.submit(subTask);
					discFuture.add(v);
					
				} catch (IntegerException e) {
					// TODO Auto-generated catch block
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
	 * Discovered element. -- Be called after discovered each service element.
	 *
	 * @param elm the discoverd element.
	 */
	public void discoveredElement(DiscoverNode discoverNode, String subnetId ) {
		
		cb.discoveredElement(discoverNode.getAccessElement());
		removeIpaddressFromSubnet(discoverNode.getIpAddress(), subnetId);
	}
	
	/**
	 * Use to notify for progress.
	 * @param msg
	 */
	public void progressNotification( String msg ) {
		cb.progressNotification(msg);
	}
	
	/**
	 * Error occur -- Call when errors occurs during discovering.
	 *
	 * @param errorCode the error code.
	 * @param msg the associated message.
	 */
	public void errorOccur( NetworkErrorCodes errorCode, String msg ) {
		cb.errorOccur(errorCode, msg);
	}
	
	
	public void ipAddressNoResponse( String ipAddress, String subnetId ) {
		
		removeIpaddressFromSubnet(ipAddress, subnetId);
	}
	
	
	

	/**
	 * Checks if is stop discovery.
	 *
	 * @return true, if is stop discovery
	 */
	public boolean isStopDiscovery() {
		return stopDiscovery;
	}



	/**
	 * 
	 * @param ip
	 * @param subnetid
	 */
	private void removeIpaddressFromSubnet( String ip, String subnetid ) {
		
		if ( subnetid != null ) {
			DiscoverSubnetAsyncTask<ElementAccess, T> subTask = subnetTasks.get(subnetid);
			if ( subTask != null ) {
				
				subTask.removeDiscoverNode(ip);
				if ( subTask.discoveryNodeCount() == 0 ) {
					
					subnetTasks.remove(subnetid);
					cb.discoveredSubnet(subnetid);
					
					logger.debug("Discovered subnet " + subnetid);
				}
			}
			if ( subnetTasks.size() == 0 ) {
				cb.discoveredNetwork(discoverId);
			}
		}
	}
	
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.NetworkDiscoveryBase#stopDiscovery()
	 */
	@Override
	public void stopDiscovery() {		
		stopDiscovery = true;
		
	}



	public List<VariableBinding> getTopLevelVBs() {
		return topLevelVBs;
	}



	public String getDiscoverId() {
		return discoverId;
	}





	
}
