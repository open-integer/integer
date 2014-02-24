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
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.agent.serviceelement.Authentication;
import edu.harvard.integer.agent.serviceelement.ElementAccessTask;
import edu.harvard.integer.agent.serviceelement.ElementEndPoint;
import edu.harvard.integer.agent.serviceelement.access.AccessPort;
import edu.harvard.integer.agent.serviceelement.access.AccessUtil;
import edu.harvard.integer.agent.serviceelement.access.ElementAccess;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 *
 * The Class NetworkDiscovery provides method for topology discovery. It is defined as 
 * singleton class.
 * 
 * It manages 3 different pools for discovery.
 * 
 * Discovery request pool which controls how many concurrent request we should have.
 * Subnet discovery pool which controls how many concurrent subnet discovery we can have.
 * Element discovery pool which controls how many concurrent element discovery.we can have.
 * 
 * 
 * @author dchan
 */
public class NetworkDiscovery <T extends ElementAccess>implements NetworkDiscoveryBase {

	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
    /**
     * Use to limit the number of discovery tasks.  The number should be small since
     * we are not suggest too many tasks for discovery.
     */
	private static int discoveryTaskLimit = 5;
	
	private static int subTaskLimit = 10;
	
	/**
	 * Use to limit the number of element discovery task.
	 */
	private static int elementTaskLimit = 20;
	

	private ExecutorService pool = Executors.newFixedThreadPool(discoveryTaskLimit);
	
	private ExecutorService subPool = Executors.newFixedThreadPool(subTaskLimit);
	
	/**
	 * Used to manager the task pool for element discovery.  
	 */
	private static ExecutorService elementPool = Executors.newFixedThreadPool(NetworkDiscovery.getElementTaskLimit());
	
	private static NetworkDiscovery instance = new NetworkDiscovery();
	
	public static NetworkDiscovery  getInstance() {
	
		return instance;
	}
	
	
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.NetworkDiscoveryBase#discoverNetwork(edu.harvard.integer.agent.serviceelement.discovery.DiscoveryConfiguration, edu.harvard.integer.agent.serviceelement.discovery.DiscoverCB)
	 */
	@Override
	public void discoverNetwork(final DiscoveryConfiguration discoverConfig,
			                         final ElementDiscoverCB<ServiceElement> cb) {
		
		pool.submit(new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				
				List<DiscoverSubnetTask>  subnetTasks = new ArrayList<>();
				
				/**
				 * Create subnet tasks based on discover config.
				 */
				List<DiscoverNet> nets =  discoverConfig.getDiscoverNets();
				if ( nets != null ) {
					for ( DiscoverNet net : nets) {
					
						DiscoverSubnetTask subTask = new DiscoverSubnetTask(net.getNetwork(), net.getNetmask(), 
								                                            discoverConfig.getAccess(), cb );
						subTask.setAccessPorts(discoverConfig.getPorts());
						subnetTasks.add(subTask);
					}
				}
				
				/**
				 * Assign seed nodes to each subnet.  If cannot found subnet it belongs, discover it independently.  
				 */
				List<DiscoverNode> seedNodes =  discoverConfig.getDiscoverNodes();
				List<DiscoverNode> outOfSubnetNodes = null;
							
				if ( seedNodes != null ) {
					
					outOfSubnetNodes = new ArrayList<>();										
					for ( DiscoverNode dn : seedNodes ) {
						
						boolean outOfSubnet = true;
						for ( DiscoverSubnetTask subTask : subnetTasks ) {
							if ( subTask.isInRange(dn.getIpAddress()) ) {
								
								subTask.addDiscoverNode(dn);
								outOfSubnet = false;
								break;
							}
						}				        	
						if ( outOfSubnet ) {
							outOfSubnetNodes.add(dn);
						}
					}										
				}
				
				try {
					
					ExecutorCompletionService<DiscoveredNet>  subService = null;
					if ( subnetTasks.size() > 0 ) {
						
						subService = new ExecutorCompletionService<>(subPool);
						for ( DiscoverSubnetTask task : subnetTasks ) {
							subService.submit(task);
						}						
					}

					/**
					 * Discover IP nodes which are out of any given subnet.
					 */
					if ( outOfSubnetNodes != null ) {
					
						for ( DiscoverNode dn : outOfSubnetNodes ) {
							
							int extraAuthIndex = 0;
							boolean missingEpt = false;
							ElementEndPoint ept = dn.getElementEndPoint();
							int defaultPort = -1;
							if ( ept == null ) {
							
								missingEpt = true;
								
								List<Authentication> access = discoverConfig.getAccess();
								if ( access == null || access.size() == 0) {
									throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
								}
								defaultPort = AccessUtil.getDefaultPort(access.get(0).getAccessType());
								ept = new ElementEndPoint(dn.getIpAddress(), defaultPort, access.get(0));
								extraAuthIndex++;
							}	
							ElementDiscoverTask elmTask = new ElementDiscoverTask(cb, ept);
							if ( missingEpt ) {
								if ( discoverConfig.getAccess() != null ) {
									
									for ( AccessPort p : discoverConfig.getPorts() ) {
										if ( p.getPort() == defaultPort ) {
											continue;
										}
										elmTask.addOtherPort(p);
									}
								}
								for ( int i=extraAuthIndex;i<discoverConfig.getAccess().size(); i++ ) {
									elmTask.addOtherAuth(discoverConfig.getAccess().get(i));
								}
							}
							elementPool.submit(elmTask);
						}													
					}
					for ( int i=0; i<subnetTasks.size(); i++ ) {
						
						final Future<DiscoveredNet> subFuture = subService.take();
						try {
							
							final DiscoveredNet subnet = subFuture.get();
							
							String progressMsg = "Complete subnet discovery: " + subnet.getNetwork() + " mask:" + subnet.getNetmask();
							cb.progressNotification(progressMsg);
							
							logger.trace(progressMsg);
						}
						catch ( ExecutionException e ) {
							logger.warn("Subnet discovery exception. " + e.getMessage());
						}
					}
					
				}
				finally {
					cb.doneDiscover();
				}				
				return null;
			}
		});
	}

	
	
	
	public static int getElementTaskLimit() {
		return elementTaskLimit;
	}


	public synchronized Future<T> sutmitElementTask( ElementAccessTask<T> task ) {
		return elementPool.submit(task);
	}
	
}
