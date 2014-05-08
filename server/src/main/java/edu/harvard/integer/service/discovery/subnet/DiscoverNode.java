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
import java.util.List;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.topology.ServiceElementType;

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
		DoneScan
	}
	
	

	/**
	 * Used to indicate what discover stage on this node.
	 */
	private DiscoverStageE stage = DiscoverStageE.ReachableScan;

	private boolean reachable = false;

	private String subnetId;

	private ServiceElementType topServiceElementType;
	

	/**  The ip address of the node. */
	final private String ipAddress;
	
	private Access access;
	


	/**
	 * 
	 */
	private List<Identify>  identifies;


	/**
	 * Instantiates a new discover node.
	 *
	 * @param ipAddress the ip address
	 */
	public DiscoverNode( String ipAddress ) {
		this.ipAddress = ipAddress;
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

}
