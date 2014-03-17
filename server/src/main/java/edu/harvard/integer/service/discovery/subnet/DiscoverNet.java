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
package edu.harvard.integer.service.discovery.subnet;

/**
 * The Class DiscoverNet is used to specify which net or node for discovery
 * or need to be excluded.  There are two lists for discovery.  One contains the nodes and
 * another one contains net. The discover engine will start the discover on nodes
 * first.
 *
 * @author dchan
 */
public abstract class DiscoverNet {

	/**
	 * Specify the network to discovery.  
	 */
	private String network;
	
	/** Network Mask. */
	private String netmask;
	

	/**
	 * Gets the network.
	 *
	 * @return the network
	 */
	public String getNetwork() {
		return network;
	}
	
	/**
	 * Sets the network.
	 *
	 * @param network Specify which network to be discover.
	 */
	public void setNetwork(String network) {
		this.network = network;
	}
	
	/**
	 * Gets the netmask.
	 *
	 * @return the netmask Specify the netmask on the network. 
	 */
	public String getNetmask() {
		return netmask;
	}
	
	/**
	 * Sets the netmask.
	 *
	 * @param netmask The netmask
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	
	/**
	 * Checks if include is true or false.  True the current net will be discover.
	 *
	 * @return true or false
	 */
	public abstract boolean isInclude(); 
	
	
	
}
