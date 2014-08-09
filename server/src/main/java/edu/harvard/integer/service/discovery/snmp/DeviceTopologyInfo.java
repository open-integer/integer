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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;

/**
 * The Class DeviceTopologyInfo.
 *
 * @author dchan
 */
public class DeviceTopologyInfo {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(DeviceTopologyInfo.class);
	
	/** The topo nodes. */
	private List<TopologyNode>  topoNodes = new ArrayList<>();
	
	/** The net connections. */
	private List<NetworkConnection>  netConnections = new ArrayList<>();
	
	private boolean topoDiscoverDone = false;
	
	

	public void setTopoDiscoverDone() {
		this.topoDiscoverDone = true;
	}



	public boolean isTopoDiscoverDone() {
		return topoDiscoverDone;
	}



	/**
	 * Gets the topo nodes.
	 *
	 * @return the topo nodes
	 */
	public List<TopologyNode> getTopoNodes() {
		return topoNodes;
	}
	
	

	/**
	 * Gets the net connections.
	 *
	 * @return the net connections
	 */
	public List<NetworkConnection> getNetConnections() {
		return netConnections;
	}
	
	/**
	 * Adds the topology node.
	 *
	 * @param tn the tn
	 */
	public void addTopologyNode( TopologyNode tn ) {	
		
		/**
		 * If the topology node interface is already in the list, skip it.
		 */
		for ( TopologyNode t : topoNodes ) {
			if ( t.getIfIndex() == tn.getIfIndex() ) {
				return;
			}
		}
		topoNodes.add(tn);
	}
	
	/**
	 * Adds the net connection.
	 *
	 * @param netConnection the net connection
	 */
	public void addNetConnection( NetworkConnection netConnection ) {
		
		for ( NetworkConnection nc : netConnections ) {
			if ( nc.getIfIndex() == netConnection.getIfIndex() ) {
				return;
			}
		}
		logger.info("Add connection in the list ifIndex " + netConnection.getIfIndex() );
	    netConnections.add(netConnection);	
	}
}
