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

import java.util.List;

import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * @author David Taylor
 * 
 */
public class RunningDiscovery {

	public DiscoveryId id = null;

	public List<NetworkDiscovery<ServiceElement>> runningDiscoveries = null;

	public List<NetworkDiscovery<ServiceElement>> completeDiscoveries = null;

	/**
	 * @return the id
	 */
	public DiscoveryId getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(DiscoveryId id) {
		this.id = id;
	}

	/**
	 * @return the runningDiscoveries
	 */
	public List<NetworkDiscovery<ServiceElement>> getRunningDiscoveries() {
		return runningDiscoveries;
	}

	/**
	 * @param runningDiscoveries
	 *            the runningDiscoveries to set
	 */
	public void setRunningDiscoveries(
			List<NetworkDiscovery<ServiceElement>> runningDiscoveries) {
		this.runningDiscoveries = runningDiscoveries;
	}

	/**
	 * @return the completeDiscoveries
	 */
	public List<NetworkDiscovery<ServiceElement>> getCompleteDiscoveries() {
		return completeDiscoveries;
	}

	/**
	 * @param completeDiscoveries
	 *            the completeDiscoveries to set
	 */
	public void setCompleteDiscoveries(
			List<NetworkDiscovery<ServiceElement>> completeDiscoveries) {
		this.completeDiscoveries = completeDiscoveries;
	}

	/**
	 * 
	 */
	public void stopDiscovery() {
		
		for (NetworkDiscovery<ServiceElement> runningDiscovery : runningDiscoveries) {
			runningDiscovery.stopDiscovery();
		}
		
	}

}