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
package edu.harvard.integer.agent.serviceelement.discovery;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class NetworkDiscovery provides method for topology discovery.
 * 
 * @author dchan
 */
public class NetworkDiscovery implements NetworkDiscoveryBase {

	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
    /**
     * Use to limit the number of discovery tasks.  The number should be small since
     * we are not suggest too many tasks for discovery.
     */
	private static int discoveryTaskLimit = 5;
	
	/**
	 * Use to limit the number of element discovery task.
	 */
	private static int elementTaskLimit = 20;
	
	private ExecutorService pool = Executors.newFixedThreadPool(discoveryTaskLimit);
	

	/**
	 * Used to manager the task pool for element discovery.  We can use ExecutorCompletionService
	 * over this for better manage for the task completely.  However the ExecutorCompletionService
	 * does not have a way manage the size of the pool and we do not care which tasks are came up first.
	 * Use ExecutorService is a better choice.
	 */
	private ExecutorService elmPool = Executors.newFixedThreadPool(NetworkDiscovery.getElementTaskLimit());
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.NetworkDiscoveryBase#discoverNetwork(edu.harvard.integer.agent.serviceelement.discovery.DiscoveryConfiguration, edu.harvard.integer.agent.serviceelement.discovery.DiscoverCB)
	 */
	@Override
	public void discoverNetwork(final DiscoveryConfiguration discoverConfig,
			final DiscoverCB<ServiceElement> cb) {
		
		pool.submit(new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				
				List<DiscoverSubnetTask>  subnetTasks = new ArrayList<>();
				
				List<Future<Void>> subnetFutures = new ArrayList<>(subnetTasks.size());
				
				for ( DiscoverSubnetTask task : subnetTasks ) {
					Future<Void> f = elmPool.submit(task);			
					subnetFutures.add(f);
				}
				
				for ( Future<Void> f : subnetFutures ) {
					
					try {
						f.get();
					} 
					catch (InterruptedException e) {
						logger.info(e.getMessage(), e);
					} 
					catch (ExecutionException e) {
						logger.info(e.getMessage(), e);
					}
				}
				cb.doneDiscover();
				return null;
			}
		});
	}

	
	public static int getElementTaskLimit() {
		return elementTaskLimit;
	}

}
