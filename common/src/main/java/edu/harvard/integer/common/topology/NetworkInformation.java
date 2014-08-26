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

import java.io.Serializable;
import java.util.HashMap;

import edu.harvard.integer.common.ID;

/**
 * Helper class to get the Network's and InterDeviceLink's between the Network's in one
 * call.
 * 
 * @author David Taylor
 * 
 */
public class NetworkInformation implements Serializable {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 1L;

	private Network[] networks = null;
	
	private InterNetworkLink[] links = null;
	
	private HashMap<ID, MapItemPosition> positions = null;

	/**
	 * @return the networks
	 */
	public Network[] getNetworks() {
		return networks;
	}

	/**
	 * @param networks the networks to set
	 */
	public void setNetworks(Network[] networks) {
		this.networks = networks;
	}

	/**
	 * @return the links
	 */
	public InterNetworkLink[] getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(InterNetworkLink[] links) {
		this.links = links;
	}

	/**
	 * @return the positions
	 */
	public HashMap<ID, MapItemPosition> getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(HashMap<ID, MapItemPosition> positions) {
		this.positions = positions;
	}
	
}
