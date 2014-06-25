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

import java.io.Serializable;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;

/**
 * This class holds the information needed to do a service element discovery.
 * This is passed to the discovery process to define what to discover and how to
 * discover the devices.
 * 
 * @author David Taylor
 * 
 */
public class IpServiceElementSeed implements Serializable {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The subnet to discover.
	 */
	private DiscoverNet subnetToDiscover = null;

	/**
	 * A list of gateways to exclude.
	 */
	private ID[] gatewayExclusionList = null;

	/**
	 * This is the list of technologies used to discover elements in the net.
	 * The default is using SNMP first (trying v2 with a community string
	 * preference list first).
	 * 
	 * If that should fail a try with V3 and if that should fail try with v1.
	 * 
	 * These following options would have to be specifically enabled by the
	 * user.
	 * 
	 * After this try ICMP and if ICMP is successful then SSH/CLI.
	 * 
	 * 
	 * Puppet.
	 */
	private DiscoveryTechnology[] discoveryTechnologies = null;

	/**
	 * A listing of service element types to exclude in this discovery.
	 */
	private ServiceElementType[] serviceElementTypeExclusions = null;

	/**
	 * @return the subnetToDiscover
	 */
	public DiscoverNet getSubnetToDiscover() {
		return subnetToDiscover;
	}

	/**
	 * @param subnetToDiscover
	 *            the subnetToDiscover to set
	 */
	public void setSubnetToDiscover(DiscoverNet subnetToDiscover) {
		this.subnetToDiscover = subnetToDiscover;
	}

	/**
	 * @return the gatewayExclusionList
	 */
	public ID[] getGatewayExclusionList() {
		return gatewayExclusionList;
	}

	/**
	 * @param gatewayExclusionList
	 *            the gatewayExclusionList to set
	 */
	public void setGatewayExclusionList(ID[] gatewayExclusionList) {
		this.gatewayExclusionList = gatewayExclusionList;
	}

	/**
	 * @return the discoveryTechnologies
	 */
	public DiscoveryTechnology[] getDiscoveryTechnologies() {
		return discoveryTechnologies;
	}

	/**
	 * @param discoveryTechnologies
	 *            the discoveryTechnologies to set
	 */
	public void setDiscoveryTechnologies(
			DiscoveryTechnology[] discoveryTechnologies) {
		this.discoveryTechnologies = discoveryTechnologies;
	}

	/**
	 * @return the serviceElementTypeExclusions
	 */
	public ServiceElementType[] getServiceElementTypeExclusions() {
		return serviceElementTypeExclusions;
	}

	/**
	 * @param serviceElementTypeExclusions
	 *            the serviceElementTypeExclusions to set
	 */
	public void setServiceElementTypeExclusions(
			ServiceElementType[] serviceElementTypeExclusions) {
		this.serviceElementTypeExclusions = serviceElementTypeExclusions;
	}

}
