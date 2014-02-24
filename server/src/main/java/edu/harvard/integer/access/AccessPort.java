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
package edu.harvard.integer.agent.serviceelement.access;

import java.util.ArrayList;
import java.util.List;

import edu.harvard.integer.agent.serviceelement.AccessTypeEnum;

/**
 * The Class AccessPort contains port to access ip node and the associated access type.
 * For example port 161 can apply to SNMPv1, SNMPv2s and SNMPv3.
 *
 * @author dchan
 */
public class AccessPort {

	/** The port. */
	final private int port;
	
	/** The access types. */
	final private List<AccessTypeEnum>  accessTypes = new ArrayList<>();
	
	/**
	 * Instantiates a new access port.
	 *
	 * @param port the port
	 * @param access the access
	 */
	public AccessPort( int port, AccessTypeEnum access ) {
		this.port = port;
		accessTypes.add(access);
	}
	
	
	/**
	 * Adds the access type for that port.
	 *
	 * @param access the access
	 */
	public void addAccess( AccessTypeEnum access ) {
		
		for ( AccessTypeEnum a : accessTypes ) {
			if ( access == a ) {
				return;
			}
		}
		accessTypes.add(access);
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
	 * Checks if is access type support.
	 *
	 * @param access the access
	 * @return true, if is access support
	 */
	public boolean isAccessSupport( AccessTypeEnum access ) {
		
		for ( AccessTypeEnum a : accessTypes ) {
			if ( access == a ) {
				return true;
			}
		}
		return false;
	}
	
}
