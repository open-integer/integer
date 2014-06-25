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

import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.snmp.CommunityAuth;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;

/**
 * 
 * The Class DiscoveryPolicy contains discovery configuration for each subnet.
 *
 * @author dchan
 */
public class IpDiscoverySeed {

	/** The snmp timeout in million second. */
	private int snmpTimeout = 3000;
	
	/** The snmp retries. */
	private int snmpRetries = 1;
	
	/** The use icmp. */
	private boolean useIcmp = true;
	
	/** The icmp timeout. */
	private int icmpTimeout;
	
	/** The icmp retries. */
	private int icmpRetries;
	
	private int radius;
	
	private DiscoverNet discoverNet;
	
	private List<DiscoverNet>  exclusiveNet = new ArrayList<>();
	
	/**
	 * Optional. If specify it will start to discovery from this IP address 
	 */
	private String startIp;
	
	/** The end IP address for discovery */
	private String endIp;
	


	/**
	 * The access used for discovery.  Right now we assume access is shared on all the subnet.
	 * However it may not be the case.
	 */
	private List<Authentication> auths;
	
	/** The ports contain ip node access ports for discover.  It can be null.  In that case
	 * the default ports will be used based on access type.
	 */
	private List<AccessPort>  ports;
	
	
	/**
	 * 
	 * @param net
	 * @param credentials
	 */
	public IpDiscoverySeed( final DiscoverNet net, final List<Credential> credentials ) {
		
		this.discoverNet = net;
		setCredential(credentials);
	}
	
	
	
	
	
	
	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public List<Authentication> getAuths() {
		return auths;
	}

	/**
	 * Sets the access.
	 *
	 * @param access the new access
	 */
	public void setCredential(List<Credential> credentials ) {
		
		auths = new ArrayList<>();
		for ( Credential c : credentials ) {
			
			if ( c instanceof SnmpV2cCredentail ) {
				CommunityAuth auth = new CommunityAuth((SnmpV2cCredentail) c);
				auths.add(auth);
			}
		}
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
	

	public DiscoverNet getDiscoverNet() {
		return discoverNet;
	}

  
	
	public int getRadius() {
		return radius;
	}

	/**
	 * 
	 */
	private List<DiscoverNet> notDiscoverNet;
	


	public String getStartIp() {
		return startIp;
	}







	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}




	public String getEndIp() {
		return endIp;
	}



	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}



	public List<DiscoverNet> getNotDiscoverNet() {
		return notDiscoverNet;
	}



	public void setNotDiscoverNet(List<DiscoverNet> notDiscoverNet) {
		this.notDiscoverNet = notDiscoverNet;
	}



	public void setRadius(int radius) {
		this.radius = radius;
	}


	public void setPorts(List<AccessPort> ports) {
		this.ports = ports;
	}


	
	public List<DiscoverNet> getExclusiveNet() {
		return exclusiveNet;
	}

	
	public String getSeedId() {
		return discoverNet.getNetworkAddress() + "/" + discoverNet.getNetmask();
	}
}
