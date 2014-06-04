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

import java.util.concurrent.Future;

import javax.ejb.Local;
import javax.ejb.Remote;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.event.DiscoveryCompleteEvent;
import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.util.DisplayableInterface;
import edu.harvard.integer.service.BaseServiceInterface;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;
import edu.harvard.integer.service.discovery.subnet.Ipv4Range;


/**
 * The Interface DiscoveryServiceInterface.
 *
 * @author David Taylor
 */
@Local
public interface DiscoveryServiceInterface extends BaseServiceInterface {

	/**
	 * Start a discovery with the given DiscoveryRule.
	 * 
	 * @param rule
	 * @return
	 * @throws IntegerException
	 */
	public DiscoveryId startDiscovery(DiscoveryRule rule) throws IntegerException;
	
	/**
	 * @param dicoveryId
	 * @throws IntegerException
	 */
	void discoveryComplete(DiscoveryId dicoveryId) throws IntegerException;

	/**
	 * @param id
	 * @param errorCode
	 * @param args
	 */
	void discoveryError(DiscoveryId id, ErrorCodeInterface errorCode,
			DisplayableInterface[] args);
	
	
	/**
	 * 
	 * @param se
	 * @param ipAddress
	 */
	public void discoveryServiceElementNoResponse( ServiceElement se, String ipAddress );
	
	

	/**
	 * Save ServiceElement
	 * @param accessElement
	 */
	void discoveredServiceElement(ServiceElement accessElement);

	/**
	 * Stop the running discovery specified by the DiscoveryId
	 * 
	 * @param id
	 */
	void stopDiscovery(DiscoveryId id);


	/**
	 * Submit a ServiceElement Discovery task for execution.
	 * 
	 * @param discoveryTask
	 */
	void submitElementDiscoveryTask(ElementDiscoverTask discoveryTask);

	/**
	 * Submit a SubnetDiscovery task for execution.
	 *  
	 * @param task
	 */
	Future<Ipv4Range> submitSubnetDiscovery(DiscoverSubnetAsyncTask task);

	/**
	 * @param serviceElementId
	 * @return
	 * @throws IntegerException
	 */
	DiscoveryCompleteEvent[] getDiscoveryStatus(ID serviceElementId)
			throws IntegerException;
}
