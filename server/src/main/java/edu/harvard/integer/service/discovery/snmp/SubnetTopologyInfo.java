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

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SubnetTopologyInfo is data object holding layer2 topology information on each subnet.
 *
 * @author dchan
 */
public class SubnetTopologyInfo {

	/** The subnet id. */
	private final String subnetId;
	
	/** The topology map.  The key is the IP Address of a discovered node. */
	private Map<String, DeviceTopologyInfo>  topologyMap = new HashMap<String, DeviceTopologyInfo>();
	
	/**
	 * Instantiates a new subnet topology info.
	 *
	 * @param subnetId the subnet id
	 */
	public SubnetTopologyInfo( String subnetId ) {
		this.subnetId = subnetId;
	}

	/**
	 * Gets the subnet id.
	 *
	 * @return the subnet id
	 */
	public String getSubnetId() {
		return subnetId;
	}

	/**
	 * Gets the topology map.
	 *
	 * @return the topology map
	 */
	public Map<String, DeviceTopologyInfo> getTopologyMap() {
		return topologyMap;
	}
	
	
	/**
	 * Adds the device topology info.
	 *
	 * @param topoInfo the topo info
	 */
	public void addDeviceTopologyInfo( String discoverNodeIp, DeviceTopologyInfo topoInfo ) {
	
		topologyMap.put(discoverNodeIp, topoInfo);
	}
}
