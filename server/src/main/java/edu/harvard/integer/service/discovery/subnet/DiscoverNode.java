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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.snmp.AssociationInfo;
import edu.harvard.integer.service.discovery.snmp.DeviceTopologyInfo;
import edu.harvard.integer.service.discovery.snmp.NetworkConnection;
import edu.harvard.integer.service.discovery.snmp.TopologyNode;

/**
 * The Class DiscoverNode is a data object used for IP node discovery.
 * It contains the basic IP, port, authentication, stage information on the IP node.
 * It also contains physical 
 *
 * @author dchan
 */
public class DiscoverNode extends ElementAccess {

	/**
	 * Define the stage during discover.  
	 *
	 */
	public enum DiscoverStageE {
		
		/**
		 * This stage is indicating the node is in reachable scan stage.
		 * This is considering the first stage during discovery.
		 */
		ReachableScan, 
		
		/**
		 * This stage is used to indicate the device is being in detail discover.
		 * It is processed after the ReachableScan.
		 */
		DetailScan, 
		
		/**
		 * This stage is used to indicate that the device is in topology scan stage.
		 * Not all IP nodes need to be go through this stage.
		 */
		TopoScan, 
		
		/**
		 * This stage is used to indicate the node is done with discover.
		 * Any other stages can jump to this stage
		 */
		DoneScan,
		
		/**
		 * This stage is used to indicate the node is stop by request.  It is a final stage
		 * which means it cannot change to other stage after it reach Stop stage.
		 * 
		 * Upon receiving the stop request, it should be no more new request to devices.
		 */
		Stop
	}
	
	
    /**
     * The subnet that belong to this discovered node.
     */
	private DiscoverNet discoverNet;
	
	private List<DiscoverNet> otherSubnet = new ArrayList<>();


	/**
	 * Used to indicate what discover stage on this node.
	 */
	private volatile DiscoverStageE stage = DiscoverStageE.ReachableScan;

	private boolean reachable = false;

	/**
	 * Subnet id associated the subnet which discovers this node.
	 */
	private String subnetId;


	/**
	 * Top Service Element associated with this node.
	 */
	private ServiceElementType topServiceElementType;
	
	/**
	 * Existing service element which contains same unique Identifiers as the current
	 * discover service element.
	 */
	private ServiceElement existingSE;
	

	/**  The ip address of the node. */
	final private String ipAddress;
	
	/**
	 * The access currently uses to make request to device.
	 */
	private Access access;
	

	private DeviceTopologyInfo topologyInfo = new DeviceTopologyInfo();

	/**
	 * Used for identify the top level of service element.
	 */
	private List<Identify>  identifies;
	
	/**
	 * Map to store discovered service elements, it is used for final stage to link the association
	 * between service elements.
	 * The key for the MAP is service element type plus the uniqueKeyValue of the service element.
	 */
	private Map<String, ID>  instSeMap = new HashMap<String, ID>();

	/**
	 * Map to store association information, it is used to final stage to link the association
	 * between service elements.  The key for the MAP is service element type plus the uniqueKeyValue of the service element.
	 */
	private Map<String, AssociationInfo> associationInfos = new HashMap<>();
	

	/**
	 * Boolean used to indicate whether the current discovered node can forward packages
	 * or not.
	 */
	private boolean isFwdNode = true;

	/**
	 * The system name of the discovered node.
	 */
	private String sysName;
	
	
	/**
	 * This list holds remote nodes found from CDP discovery which subnet mask is unknown.  
	 * It can not find locally or remotely.
	 */
	private List<DiscoverNode>  unknownMaskNodes;
	
	
	/**
	 * Instantiates a new discover node.
	 *
	 * @param ipAddress the ip address
	 */
	public DiscoverNode( String ipAddress ) {
		
		this.ipAddress = ipAddress;
		DiscoverNet dnet = new DiscoverNet(ipAddress, "255.255.255.0", 0);
		this.discoverNet = dnet;
	}
	
	
	/**
	 * Instantiates a new discover node.
	 *
	 * @param ipAddress the ip address
	 */
	public DiscoverNode( String ipAddress,  DiscoverNet discoverNet ) {
		
		this.ipAddress = ipAddress;
		this.discoverNet = discoverNet;
	}
	
	
	/**
	 * Gets the element end point of the discover node if available.
	 * It may not be available during the starting subnet discovery.  In that case 
	 * the subnet discover task should assign the port and auth to this node during discovery.
	 *
	 * @return the element end point
	 */
	public ElementEndPoint getElementEndPoint() {
		
		if ( access != null ) {
			ElementEndPoint ept = new ElementEndPoint(ipAddress, access.getPort(), access.getAuth());
			return ept;
		}
		return null;
	}

	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	

	/**
	 * Gets the stage.
	 *
	 * @return the stage
	 */
	public DiscoverStageE getStage() {
		return stage;
	}

	
	/**
	 * Sets the stage.
	 *
	 * @param stage the new stage
	 */
	public void setStage(DiscoverStageE stage) {
		
		if ( stage == DiscoverStageE.Stop ) {
			return;
		}
		this.stage = stage;
	}


	
	public boolean isReachable() {
		return reachable;
	}



	public void setReachable(boolean reachable) {
		this.reachable = reachable;
	}

	
	
	public Access getAccess() {
		return access;
	}


	public void setAccess(Access access) {
		this.access = access;
	}



	public String getSubnetId() {
		return subnetId;
	}


	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	
	
	


	/**
	 * Set the identify for this discover object.
	 * 
	 * @param identifyDefs
	 */
	public void setIdentifyDefs(List<String> identifyDefs) {
		
		identifies = new ArrayList<>();
		for ( String s : identifyDefs ) {
		
			Identify i = new Identify();
			i.identifyDef = s;
		}	
		Collections.sort(identifies);
	}

	
	/**
	 * Check if there are enough data for identify a discover node not or.
	 * If all values in "identifies" being set, it will return true.
	 * @return
	 */
	public boolean isEnoughIdentifyValue() {
	
		if ( identifies == null ) {
			return false;
		}
		for ( Identify i : identifies ) {
			if ( i.value == null ) {
		        return true;		
			}
		}		
		return true;
	}
	
	/**
	 * 
	 * @param identifyName
	 * @param value
	 * @return
	 */
	public boolean setIdentifyValue( String identifyName, String value ) {
		
		if ( identifies == null ) {
			return false;
		}
		
		for ( Identify i : identifies ) {
			
			if ( i.identifyDef.equals(identifyName) ) {
				i.value = value;
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Return the identify value of the discover node.
	 * @return
	 */
	public String getDiscoverNodeIdentify() {
		
		if ( !isEnoughIdentifyValue() ) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for ( Identify i : identifies ) {
			
			sb.append(i.value + ":");
		}	
		return sb.toString();
	}
	

	/**
	 * Us to store the 
	 * @author dchan
	 *
	 */
	public class Identify implements Comparable<Identify> {
		
		String identifyDef;
		String value;
		
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Identify o) {
			
			return o.identifyDef.compareTo(identifyDef);
		}
	}


	public ServiceElementType getTopServiceElementType() {
		return topServiceElementType;
	}


	public void setTopServiceElementType(ServiceElementType topServiceElementType) {
		this.topServiceElementType = topServiceElementType;
	}
	

	public ServiceElement getExistingSE() {
		return existingSE;
	}


	public void setExistingSE(ServiceElement existingSE) {
		this.existingSE = existingSE;
	}


	/**
	 * Store the stop stage and set element end point to blocking.
	 */
	public void stopDiscover() {
		
		if ( getElementEndPoint() != null ) {
			getElementEndPoint().setBlocking(true);
		}
		stage = DiscoverStageE.Stop;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasProtocolConnection() {
		
		if ( topologyInfo.getNetConnections().size() > 0 ) {
			return true;
		}
		return false;
	}
	

	/**
	 * Adds the topology node.
	 *
	 * @param tn the tn
	 */
	public void addTopologyNode( TopologyNode tn ) {		
		topologyInfo.addTopologyNode(tn);
	}
	
	/**
	 * Adds the net connection.
	 *
	 * @param netConnection the net connection
	 */
	public void addNetConnection( NetworkConnection netConnection ) {
	    topologyInfo.addNetConnection(netConnection);	
	}
	
	public DeviceTopologyInfo getTopologyInfo() {
		return topologyInfo;
	}

	

	public boolean isFwdNode() {
		return isFwdNode;
	}


	public void setFwdNode(boolean isFwdNode) {
		this.isFwdNode = isFwdNode;
	}

	

	public DiscoverNet getDiscoverNet() {
		return discoverNet;
	}



	/**
	 * 
	 * @return
	 */
	public List<DiscoverNet> getOtherSubnet() {
		return otherSubnet;
	}


	/**
	 * 
	 * @return
	 */
	public String getSysName() {
		return sysName;
	}


	/**
	 * 
	 * @param sysNamn
	 */
	public void setSysNamn(String sysNamn) {
		this.sysName = sysNamn;
	}

	/**
	 * 
	 * @param instKey
	 * @param se
	 */
	public void bookDiscoveredSE( String instKey, ID seId ) {		
		instSeMap.put(instKey, seId);
	}
	
	/**
	 * 
	 * @param instKey
	 * @return
	 */
	public ID getDiscoveredSE( String instKey ) {		
		return instSeMap.get(instKey);
	}
	
	/**
	 * 
	 * @param asInfo
	 */
	public void addAssociationInfo( String uniqueKeyValue, AssociationInfo asInfo ) {
		
		if ( uniqueKeyValue == null ) {
			return;
		}
		associationInfos.put( uniqueKeyValue, asInfo);
	}
	
	/**
	 * Update the association service element if 
	 * @param instKey
	 * @param se
	 */
	public void updateAssociationSe( String uniqueKeyValue, ServiceElement se ) {
		
		if ( uniqueKeyValue == null ) {
			return;
		}
		
		AssociationInfo asInfo = associationInfos.get(uniqueKeyValue);
		if ( asInfo != null ) {
			asInfo.setAssociationSe(se);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Map<String, AssociationInfo> getAssociationInfos() {
		return associationInfos;
	}
	

	
	public List<DiscoverNode> getUnknownMaskNodes() {
		return unknownMaskNodes;
	}


	public void addUnknownMaskNodes( String ipAddr ) {
		
		DiscoverNet dnet = new DiscoverNet(ipAddr, "255.255.255.255", discoverNet.getRadiusCountDown() - 1);
		DiscoverNode dnode = new DiscoverNode(ipAddr, dnet);
		
		if ( unknownMaskNodes == null ) {
			
			unknownMaskNodes = new ArrayList<>();
		}
		unknownMaskNodes.add(dnode);
	}
	
}
