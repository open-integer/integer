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

package edu.harvard.integer.common.topology;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryOrderEnum;

/**
 * 
 * TopologySeeds are used by the system to start and limit the scope of a
 * topology discovery.
 * 
 * <p>
 * Note that the way the include and exclude attributes work is that if none are
 * filled in then, the system would discovery everything. If the technology has
 * an Include or Exclude list, then the technologies listed determine whether
 * the discovery program will include those devices and the service elements
 * they contain or not.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class IpTopologySeed extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private String description = null;

	/**
	 * A set of networks not to be included in the discovery even if they are
	 * within the radius of the discovery.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<Subnet> netExclustions = null;

	/**
	 * A list of gateways to exclude.
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<Address> gatewayExclusuions = null;

	/**
	 * Initial gateway allows the specification of the starting gateway on a
	 * subnet in IpTopologySeeds that would be used as the starting point for a
	 * topology Discovery.
	 * <p/>
	 * If a subnet were specified it would do the same thing. That is find the
	 * routers on the network and go out as many hops as the radius attribute
	 * requires.
	 * <p/>
	 * In some subnets, there may be more than one router. In this case, whether
	 * a router or subnet wer specified, the hop count always begins at the
	 * first hop outside the subnet.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "address", column = @Column(name = "initialGatewayAddress")),
			@AttributeOverride(name = "mask", column = @Column(name = "initialGatewayMask")) })
	private Address initialGateway = null;

	/**
	 * This is the starting subnet for the discovery process. If radius is null,
	 * then just discovery this subnet. If it is not null, then go out the
	 * number of hops specified in the radius.
	 */
	@Embedded
	private Subnet subnet = null;

	/**
	 * Radius is the number of level-appropriate 'hops' from the starting subnet
	 * to go out. 0 Means discovery ONLY the subnet specified. 1 means discover
	 * the subnet specified and all connected subnets 1 hop away.
	 * 
	 * 99 means no limit and is not recommended.
	 */
	private Integer radius = null;

	/**
	 * This is the list of protocols/technologies used to discover elements in
	 * the net. The default is using SNMP first (trying v2 with a community
	 * string preference list first).
	 * 
	 * If that should fail a try with V3 and if that should fail try with v1.
	 * 
	 * These following options would have to be specifcially enabled by the
	 * user.
	 * 
	 * After this try ICMP and if ICMP is successful then SSH/CLI.
	 * 
	 * 
	 * Puppet.
	 */
	@ElementCollection(targetClass = DiscoveryOrderEnum.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "IpTopologySeed_DiscoveryOrder")
	private List<DiscoveryOrderEnum> discoveryOrder = null;

	/**
	 * A boolean indicating whether the list of technologies in the
	 * technologyList is included or excluded.
	 */
	private Boolean technologyIncludeExclude = null;

	/**
	 * List of technologies that are included or excluded based on the value in
	 * the technologyIncludeExclude attribute.
	 */
	@ElementCollection
	@OrderColumn(name = "idx")
	private List<ID> technologys = null;

	/**
	 * The timeout value that the snmp engine should use for discovery.
	 */
	private Long snmpTimeoutServiceElementDiscovery = null;

	/**
	 * The timeout value that the snmp engine should use for discovery.
	 */
	private Integer snmpRetriesServiceElementDiscovery = null;

	/**
	 * SNMP Timeout value for requests issued by the Topology manager while
	 * processing this IpTopologySeed.
	 */
	private Long snmpTimeoutTopologyDiscovery = null;

	/**
	 * Number of SNMP retries used by the Topology Manager when processing this
	 * IpTopologySeed.
	 */
	private Integer snmpRetriesTopologyDiscovery = null;

	/**
	 * Credentials to use for this discovery
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@OrderColumn(name = "idx")
	@CollectionTable(name = "IpTopologySeed_Credentials")
	private List<Credential> credentials = null;

	@ElementCollection
	@OrderColumn(name = "idx")
	private List<Integer> alternateSNMPports = null;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the netExclustions
	 */
	public List<Subnet> getNetExclustions() {
		return netExclustions;
	}

	/**
	 * @param netExclustions
	 *            the netExclustions to set
	 */
	public void setNetExclustions(List<Subnet> netExclustions) {
		this.netExclustions = netExclustions;
	}

	/**
	 * @return the gatewayExclusuions
	 */
	public List<Address> getGatewayExclusuions() {
		return gatewayExclusuions;
	}

	/**
	 * @param gatewayExclusuions
	 *            the gatewayExclusuions to set
	 */
	public void setGatewayExclusuions(List<Address> gatewayExclusuions) {
		this.gatewayExclusuions = gatewayExclusuions;
	}

	/**
	 * @return the initialGateway
	 */
	public Address getInitialGateway() {
		return initialGateway;
	}

	/**
	 * @param initialGateway
	 *            the initialGateway to set
	 */
	public void setInitialGateway(Address initialGateway) {
		this.initialGateway = initialGateway;
	}

	/**
	 * @return the subnet
	 */
	public Subnet getSubnet() {
		return subnet;
	}

	/**
	 * @param subnet
	 *            the subnet to set
	 */
	public void setSubnet(Subnet subnet) {
		this.subnet = subnet;
	}

	/**
	 * @return the radius
	 */
	public Integer getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	/**
	 * @return the discoveryOrder
	 */
	public List<DiscoveryOrderEnum> getDiscoveryOrder() {
		return discoveryOrder;
	}

	/**
	 * @param discoveryOrder
	 *            the discoveryOrder to set
	 */
	public void setDiscoveryOrder(List<DiscoveryOrderEnum> discoveryOrder) {
		this.discoveryOrder = discoveryOrder;
	}

	/**
	 * @return the technologyIncludeExclude
	 */
	public Boolean getTechnologyIncludeExclude() {
		return technologyIncludeExclude;
	}

	/**
	 * @param technologyIncludeExclude
	 *            the technologyIncludeExclude to set
	 */
	public void setTechnologyIncludeExclude(Boolean technologyIncludeExclude) {
		this.technologyIncludeExclude = technologyIncludeExclude;
	}

	/**
	 * @return the technologys
	 */
	public List<ID> getTechnologys() {
		return technologys;
	}

	/**
	 * @param technologys
	 *            the technologys to set
	 */
	public void setTechnologys(List<ID> technologys) {
		this.technologys = technologys;
	}

	/**
	 * @return the snmpTimeoutServiceElementDiscovery
	 */
	public Long getSnmpTimeoutServiceElementDiscovery() {
		return snmpTimeoutServiceElementDiscovery;
	}

	/**
	 * @param snmpTimeoutServiceElementDiscovery
	 *            the snmpTimeoutServiceElementDiscovery to set
	 */
	public void setSnmpTimeoutServiceElementDiscovery(
			Long snmpTimeoutServiceElementDiscovery) {
		this.snmpTimeoutServiceElementDiscovery = snmpTimeoutServiceElementDiscovery;
	}

	/**
	 * @return the snmpRetriesServiceElementDiscovery
	 */
	public Integer getSnmpRetriesServiceElementDiscovery() {
		return snmpRetriesServiceElementDiscovery;
	}

	/**
	 * @param snmpRetriesServiceElementDiscovery
	 *            the snmpRetriesServiceElementDiscovery to set
	 */
	public void setSnmpRetriesServiceElementDiscovery(
			Integer snmpRetriesServiceElementDiscovery) {
		this.snmpRetriesServiceElementDiscovery = snmpRetriesServiceElementDiscovery;
	}

	/**
	 * @return the snmpTimeoutTopologyDiscovery
	 */
	public Long getSnmpTimeoutTopologyDiscovery() {
		return snmpTimeoutTopologyDiscovery;
	}

	/**
	 * @param snmpTimeoutTopologyDiscovery
	 *            the snmpTimeoutTopologyDiscovery to set
	 */
	public void setSnmpTimeoutTopologyDiscovery(
			Long snmpTimeoutTopologyDiscovery) {
		this.snmpTimeoutTopologyDiscovery = snmpTimeoutTopologyDiscovery;
	}

	/**
	 * @return the snmpRetriesTopologyDiscovery
	 */
	public Integer getSnmpRetriesTopologyDiscovery() {
		return snmpRetriesTopologyDiscovery;
	}

	/**
	 * @param snmpRetriesTopologyDiscovery
	 *            the snmpRetriesTopologyDiscovery to set
	 */
	public void setSnmpRetriesTopologyDiscovery(
			Integer snmpRetriesTopologyDiscovery) {
		this.snmpRetriesTopologyDiscovery = snmpRetriesTopologyDiscovery;
	}

	/**
	 * @return the credentials
	 */
	public List<Credential> getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the alternateSNMPports
	 */
	public List<Integer> getAlternateSNMPports() {
		return alternateSNMPports;
	}

	/**
	 * @param alternateSNMPports the alternateSNMPports to set
	 */
	public void setAlternateSNMPports(List<Integer> alternateSNMPports) {
		this.alternateSNMPports = alternateSNMPports;
	}

}
