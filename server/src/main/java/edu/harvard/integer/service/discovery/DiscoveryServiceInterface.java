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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import edu.harvard.integer.common.discovery.VendorDiscoveryTemplate;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;


/**
 * The Interface DiscoveryServiceInterface.
 *
 * @author David Taylor
 */
public interface DiscoveryServiceInterface {

	/**
	 * Gets the subnet discovery pool.
	 *
	 * @return the sub pool
	 */
	public ExecutorService getSubPool(); 

	/**
	 * Gets the element discovery pool.
	 *
	 * @return the element pool
	 */
	public ExecutorService getElementPool(); 

		
	/**
	 * Sutmit element discovery task.
	 *
	 * @param elmTask the elm task
	 * @return the future
	 */
	public Future<DiscoverNode> sutmitElementTask( @SuppressWarnings("rawtypes") ElementDiscoverTask elmTask ); 
	


	/**
	 * Get network discover provider.  It is considering IP based network discover.
	 *
	 * @param discoverSeed the discover seed
	 * @param callback the callback
	 * @param integer the integer
	 * @return -- A discovery id.
	 */
	public String discoverNetwork( final List<IpDiscoverySeed> discoverSeed, 
			                             ElementDiscoverCB<ServiceElement> callback,
			                             IntegerInterface integer ); 
	
	
	/**
	 * Remove a discovery based on a given discovery id.
	 *
	 * @param id the id
	 * @return the network discovery base
	 */
	public NetworkDiscoveryBase removeDiscovery( String id ); 
	
	/**
	 * Stop discovery based on id.
	 *
	 * @param id the id
	 */
	public void stopDiscovery( String id );
	
	
	/**
	 * Get Network Discovery based on id.
	 *
	 * @param id the id
	 * @return the discovery
	 */
	public NetworkDiscoveryBase getDiscovery( String id ); 
	
	
	/**
	 * Gets the top level polls.  On IP network topology discovery, the list should
	 * be a list of SNMP objects in the system group.
	 *
	 * @return the top level polls for discovery. 
	 * 
	 */
	public List<ServiceElementManagementObject>  getTopLevelPolls();
	
	/**
	 * Gets the discovery template based on the list of top level poll results.
	 *
	 * @param pollResult the poll result
	 * @return the discovery template
	 */
	public VendorDiscoveryTemplate<ServiceElementManagementObject>  getDiscoveryTemplate( List<PollResult> pollResult );
}
