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


import edu.harvard.integer.access.AccessUtil;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.service.discovery.snmp.DevicePhisicalPattern;

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
	

	/**  The ip address of the node. */
	final private String ipAddress;
	
	
	/**  The access port of the node. */
	private int port = -1;
	
	/** The authentication to access the node. */
	private Authentication auth;
	
	
	/** The device physical pattern is used to discover the physical layout of the device. 
	 *  One example is the physical entity mib pattern.  
	 */
	private DevicePhisicalPattern phyPattern;
	
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}



	/**
	 * Sets the auth.
	 *
	 * @param auth the new auth
	 */
	public void setAuth(Authentication auth) {
		this.auth = auth;
	}



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
		
		if ( auth != null ) {
			
			if ( port == -1 ) {
				port = AccessUtil.getDefaultPort(auth.getAccessType());
				if ( port == -1 ) {
					return null;
				}				
			}
			ElementEndPoint ept = new ElementEndPoint(ipAddress, port, auth);
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



	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets the auth.
	 *
	 * @return the auth
	 */
	public Authentication getAuth() {
		return auth;
	}
	
	

	
	
	/**
	 * Gets the device physical pattern.
	 *
	 * @return the phy pattern
	 */
	public DevicePhisicalPattern getPhyPattern() {
		return phyPattern;
	}



	/**
	 * Sets the device physical pattern.
	 *
	 * @param phyPattern the new phy pattern
	 */
	public void setPhyPattern(DevicePhisicalPattern phyPattern) {
		this.phyPattern = phyPattern;
	}

	
}
