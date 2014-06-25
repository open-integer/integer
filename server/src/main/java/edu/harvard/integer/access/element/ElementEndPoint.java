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
package edu.harvard.integer.access.element;

import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.snmp.CommunityAuth;

/**
 * The Class ElementEndPoint contains information to access IP nodes such as
 * port, user name and password.
 * 
 *
 * @author dchan
 */
public class ElementEndPoint {
	
	/**  The ip address of the element. */
	final private String ipAddress;
	
	/**  The access port to access the element. */
	final private int  accessPort;
	
	/** The auth objects containing information such as key, community string to
	 *  to communicate with the element. 
	 */
	final private Authentication  auth; 
	
	/**
	 * If "blocking" no further access to the device is allowed.
	 */
	private volatile boolean blocking = false;
	
	

	/**
	 * Instantiates a new element end point. The ip, port and authentication is required.
	 * For some of access with no require for authentication, NoAuthentication will be used.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param auth the auth 
	 */
	public ElementEndPoint( String ip, int port, Authentication auth ) {
		
		this.ipAddress = ip;
		this.accessPort = port;
		this.auth = auth;
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
	 * Gets the access port.
	 *
	 * @return the access port
	 */
	public int getAccessPort() {
		return accessPort;
	}
	

	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public Authentication getAuth() {
		return auth;
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		if ( auth instanceof CommunityAuth ) {
			sb.append("ipAddress " + ipAddress + " port " + accessPort + " " + ((CommunityAuth)auth).getCommunity(true) );
		}
		else {
			sb.append("ipAddress " + ipAddress + " port " + accessPort );
		}
		return sb.toString();		
	}
	
	

	public boolean isBlocking() {
		return blocking;
	}


	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}



}
