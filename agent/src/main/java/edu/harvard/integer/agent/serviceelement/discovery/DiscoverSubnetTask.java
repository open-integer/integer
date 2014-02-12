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
package edu.harvard.integer.agent.serviceelement.discovery;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The Class DiscoverSubnet is used to do a subnet discover.
 * During discover, if "seed" nodes contains on discoverNodes will be discovered first.
 *
 * @author dchan
 */
public class DiscoverSubnetTask implements Callable<Void> {
	
	/**
	 * Specify the network of the subnet 
	 */
	final private String network;
	
	/** Network Mask of the Subnet. */
	final private String netmask;

	/** If it is true, done with discovery. */
	private boolean doneDiscovery;
	
	/** The topo element map. */
	private final Map<String, DiscoveredNode> elmMap = new ConcurrentHashMap<String, DiscoveredNode>();
	
	/**
	 * Discover nodes to be discovered before auto-discovered on the subnet.  
	 * In general, they are considering the seed nodes.
	 */
	private List<DiscoverNode> discoverNodes;
	
	
	/**
	 * Instantiates a new discover subnet.
	 *
	 * @param network the network
	 * @param netmask the netmask
	 */
	public DiscoverSubnetTask( String network, String netmask, List<DiscoverNode> discoverNodes ) {
		
		this.network = network;
		this.netmask = netmask;
		
		this.discoverNodes = discoverNodes;
	}
	
	/**
	 * Instantiates a new discover subnet.
	 *
	 * @param network the network
	 * @param netmask the netmask
	 */
	public DiscoverSubnetTask( String network, String netmask ) {
		
		this.network = network;
		this.netmask = netmask;
	}
	
	/**
	 * Gets the network of the subnet
	 *
	 * @return the network
	 */
	public String getNetwork() {
		return network;
	}

	/**
	 * Gets the netmask of the subnet.
	 *
	 * @return the netmask
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * Gets the elm map.
	 *
	 * @return the elm map
	 */
	public Map<String, DiscoveredNode> getElmMap() {
		return elmMap;
	}

	/**
	 * Adds the discovered node.
	 *
	 * @param node the node
	 */
	public void addDiscoveredNode(DiscoveredNode node) {
		
		elmMap.put(node.getIpAddress(), node);
	}
	
	/**
	 * Gets the discovered node.
	 *
	 * @param ip the ip
	 * @return the discovered node
	 */
	public DiscoveredNode getDiscoveredNode( String ip ) {
		
		return elmMap.get(ip);
	}
	

	/**
	 * Removes the discovered node.
	 *
	 * @param ip the ip
	 * @return the discovered node
	 */
	public DiscoveredNode removeDiscoveredNode( String ip ) {
		
		return elmMap.remove(ip);
	}
	

	
	/**
	 * Checks if is done discovery.
	 *
	 * @return true, if is done discovery
	 */
	public boolean isDoneDiscovery() {
		return doneDiscovery;
	}

	/**
	 * Sets the done discovery.
	 *
	 * @param doneDiscovery the new done discovery
	 */
	public void setDoneDiscovery(boolean doneDiscovery) {
		this.doneDiscovery = doneDiscovery;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
