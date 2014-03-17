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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;
import edu.harvard.integer.service.discovery.subnet.DiscoverNetInclusive;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;


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
public class NetworkDiscovery <T extends ElementAccess>implements NetworkDiscoveryBase {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
 	
	
	/** The l3 nodes. */
	private ConcurrentHashMap<String, DiscoverNode>  l3Nodes = new ConcurrentHashMap<>();
	
	/** The l2 nodes. */
	private ConcurrentHashMap<String, DiscoverNode> l2Nodes = new ConcurrentHashMap<>();
	
	/** The end nodes. */
	private ConcurrentHashMap<String, DiscoverNode> endNodes = new ConcurrentHashMap<>();
	
	/** The callback for notify the progress of discovery. */
	
	private ElementDiscoverCB<ServiceElement> cb;
	
	/** The discover seed. */
	private final IpDiscoverySeed discoverSeed;
	
	/** The stop discovery. */
	private volatile boolean stopDiscovery;
	
	
	/** The integer Inteface used by discovery to retrieve object model information.. */
	private IntegerInterface integerIf;
	

	/**
	 * Instantiates a new network discovery.
	 *
	 * @param discoverSeed the discover seed
	 * @param callback the callback
	 * @param integerIf the integer if
	 */
	public NetworkDiscovery( final IpDiscoverySeed discoverSeed, 
			                 ElementDiscoverCB<ServiceElement> callback,
			                 IntegerInterface integerIf ) 
	{
		this.cb = callback;
		this.discoverSeed = discoverSeed;
		this.integerIf = integerIf;
		
		discoverNetwork();
	}
	
	
	
	
	/**
	 * Discover network. Each subnet has its own thread for discovery.
	 */
	@SuppressWarnings("rawtypes")
	private void discoverNetwork() {
		
		logger.debug("In discoverNetwork ");
		
		/**
		 * Create subnet tasks based on discover configuration subnet.
		 */
		List<DiscoverNetInclusive> nets =  discoverSeed.getDiscoverNets();
		if ( nets != null ) {
			
			ExecutorService exService =  DiscoveryManager.getInstance().getSubPool();
			for ( DiscoverNet net : nets) {
			
				try {
					@SuppressWarnings("unchecked")
					DiscoverSubnetAsyncTask<T> subTask = new DiscoverSubnetAsyncTask(this, net.getNetwork(), 
							                                        net.getNetmask(), discoverSeed.getAuths());
					exService.submit(subTask);
					
				} catch (IntegerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		

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
	 * Gets the cb.
	 *
	 * @return the cb
	 */
	public ElementDiscoverCB<ServiceElement> getCb() {
		return cb;
	}



	/**
	 * Checks if is stop discovery.
	 *
	 * @return true, if is stop discovery
	 */
	public boolean isStopDiscovery() {
		return stopDiscovery;
	}




	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.NetworkDiscoveryBase#stopDiscovery()
	 */
	@Override
	public void stopDiscovery() {		
		stopDiscovery = true;
		
	}



	/**
	 * Gets the integer Integer Interface.
	 *
	 * @return the integer if
	 */
	public IntegerInterface getIntegerIf() {
		return integerIf;
	}


	
}
