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

import java.util.ArrayList;
import java.util.List;

import edu.harvard.integer.agent.serviceelement.Authentication;
import edu.harvard.integer.agent.serviceelement.access.AccessPort;

/**
 * 
 * The Class DiscoveryPolicy contains discovery configuration such as seed element
 * filters based on network etc.
 *
 * @author dchan
 */
public class DiscoveryConfiguration {

	/** The snmp timeout. */
	private int snmpTimeout;
	
	/** The snmp retries. */
	private int snmpRetries;
	
	/** The use icmp. */
	private boolean useIcmp = true;
	
	/** The icmp timeout. */
	private int icmpTimeout;
	
	/** The icmp retries. */
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
	private List<Authentication> access;
	
	/** The ports contain ip node access ports for discover.  It can be null.  In that case
	 * the default ports will be used based on access type.
	 */
	private List<AccessPort>  ports;
	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public List<Authentication> getAccess() {
		return access;
	}

	/**
	 * Sets the access.
	 *
	 * @param access the new access
	 */
	public void setAccess(List<Authentication> access) {
		this.access = access;
	}

	/**
	 * Gets the snmp timeout.
	 *
	 * @return the snmp timeout
	 */
	public int getSnmpTimeout() {
		return snmpTimeout;
	}
	
	/**
	 * Sets the snmp timeout.
	 *
	 * @param snmpTimeout the new snmp timeout
	 */
	public void setSnmpTimeout(int snmpTimeout) {
		this.snmpTimeout = snmpTimeout;
	}
	
	/**
	 * Gets the snmp retries.
	 *
	 * @return the snmp retries
	 */
	public int getSnmpRetries() {
		return snmpRetries;
	}
	
	/**
	 * Sets the snmp retries.
	 *
	 * @param snmpRetries the new snmp retries
	 */
	public void setSnmpRetries(int snmpRetries) {
		this.snmpRetries = snmpRetries;
	}
	
	/**
	 * Checks if is use icmp.
	 *
	 * @return true, if is use icmp
	 */
	public boolean isUseIcmp() {
		return useIcmp;
	}
	
	/**
	 * Sets the use icmp.
	 *
	 * @param useIcmp the new use icmp
	 */
	public void setUseIcmp(boolean useIcmp) {
		this.useIcmp = useIcmp;
	}
	
	/**
	 * Gets the icmp timeout.
	 *
	 * @return the icmp timeout
	 */
	public int getIcmpTimeout() {
		return icmpTimeout;
	}
	
	/**
	 * Sets the icmp timeout.
	 *
	 * @param icmpTimeout the new icmp timeout
	 */
	public void setIcmpTimeout(int icmpTimeout) {
		this.icmpTimeout = icmpTimeout;
	}
	
	/**
	 * Gets the icmp retries.
	 *
	 * @return the icmp retries
	 */
	public int getIcmpRetries() {
		return icmpRetries;
	}
	
	/**
	 * Sets the icmp retries.
	 *
	 * @param icmpRetries the new icmp retries
	 */
	public void setIcmpRetries(int icmpRetries) {
		this.icmpRetries = icmpRetries;
	}
	
	/**
	 * Gets the discover nodes.
	 *
	 * @return the discover nodes
	 */
	public List<DiscoverNode> getDiscoverNodes() {
		return discoverNodes;
	}
	
	/**
	 * Sets the discover nodes.
	 *
	 * @param discoverNodes the new discover nodes
	 */
	public void setDiscoverNodes(List<DiscoverNode> discoverNodes) {
		this.discoverNodes = discoverNodes;
	}
	
	/**
	 * Gets the discover nets.
	 *
	 * @return the discover nets
	 */
	public List<DiscoverNet> getDiscoverNets() {
		return discoverNets;
	}
	
	/**
	 * Sets the discover nets.
	 *
	 * @param discoverNets the new discover nets
	 */
	public void setDiscoverNets(List<DiscoverNet> discoverNets) {
		this.discoverNets = discoverNets;
	}
	
	/**
	 * Adds the access port for discover.
	 *
	 * @param port the port
	 */
	public void addAccessPort( AccessPort port ) {
		
		if ( ports == null ) {
			ports = new ArrayList<>();
		}
		
		ports.add(port);
	}
	
	
	/**
	 * Gets the discover ports.
	 *
	 * @return the ports
	 */
	public List<AccessPort> getPorts() {
		return ports;
	}
    
}
