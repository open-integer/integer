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

import edu.harvard.integer.agent.serviceelement.Access;

/**
 * 
 * The Class DiscoveryPolicy contains discovery configuration such as seed element
 * filters based on network etc.
 *
 * @author dchan
 */
public class DiscoveryConfiguration {

	private int snmpTimeout;
	private int snmpRetries;
	
	private boolean useIcmp = true;
	private int icmpTimeout;
	private int icmpRetries;
	
	/**
	 * Discovered node which contains node ip.  Those nodes need to be discovered first.
	 */
	private List<DiscoverNode> discoverNodes;
	
	/**
	 * Discovered subnet for auto discovered.  
	 */
	private List<DiscoverNet>  discoverNets;
	
	/**
	 * The access used for discovery.  Right now we assume access is shared on all the subnet.
	 * However it may not be the case.
	 */
	private List<Access> access;
	
	
	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(List<Access> access) {
		this.access = access;
	}

	public int getSnmpTimeout() {
		return snmpTimeout;
	}
	
	public void setSnmpTimeout(int snmpTimeout) {
		this.snmpTimeout = snmpTimeout;
	}
	
	public int getSnmpRetries() {
		return snmpRetries;
	}
	
	public void setSnmpRetries(int snmpRetries) {
		this.snmpRetries = snmpRetries;
	}
	
	public boolean isUseIcmp() {
		return useIcmp;
	}
	
	public void setUseIcmp(boolean useIcmp) {
		this.useIcmp = useIcmp;
	}
	
	public int getIcmpTimeout() {
		return icmpTimeout;
	}
	
	public void setIcmpTimeout(int icmpTimeout) {
		this.icmpTimeout = icmpTimeout;
	}
	
	public int getIcmpRetries() {
		return icmpRetries;
	}
	
	public void setIcmpRetries(int icmpRetries) {
		this.icmpRetries = icmpRetries;
	}
	
	public List<DiscoverNode> getDiscoverNodes() {
		return discoverNodes;
	}
	
	public void setDiscoverNodes(List<DiscoverNode> discoverNodes) {
		this.discoverNodes = discoverNodes;
	}
	
	public List<DiscoverNet> getDiscoverNets() {
		return discoverNets;
	}
	
	public void setDiscoverNets(List<DiscoverNet> discoverNets) {
		this.discoverNets = discoverNets;
	}
    
}
