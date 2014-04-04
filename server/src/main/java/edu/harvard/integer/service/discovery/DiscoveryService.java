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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * @author David Taylor
 * 
 */
@Singleton
@Startup
public class DiscoveryService extends BaseService implements
		DiscoveryServiceInterface {
	
	@Inject
	private Logger logger;

    /**
     * Use to limit the number of discovery tasks.  The number should be small since
     * we are not suggest too many tasks for discovery.
     */
	private int discoveryTaskLimit = 5;
	
	private int subTaskLimit = 10;
	
	/**
	 * Use to limit the number of element discovery task.
	 */
	private int elementTaskLimit = 20;
	
	private ExecutorService pool = Executors.newFixedThreadPool(discoveryTaskLimit);
	
	private ExecutorService subPool = Executors.newFixedThreadPool(subTaskLimit);
		
	/**
	 * Discovery sequence id used for network discovery.  This id only valid within an integer server.
	 */
	private long discoverySeqId = 0;
		
	private Map<String, NetworkDiscovery> discoverMap = new ConcurrentHashMap<>();
	

	/**
	 * Called after service has been created. Initialize of the discovery
	 * service is done here.
	 */
	@PostConstruct
	private void init() {
		logger.info("Discovery service starting....");

	}
	
	public ExecutorService getPool() {
		return pool;
	}

	public ExecutorService getSubPool() {
		return subPool;
	}
	
	/**
	 * Discovery id.  The format of the id is Integer Server IP + sequence id.  It is considering valid cross different 
	 * Integer server.
	 */
	private synchronized String getNextDiscoveryId()  {
		
		try {
			return InetAddress.getLocalHost().getHostAddress() + ":" + (++discoverySeqId);
		} catch (UnknownHostException e) {
			return "unknownHost:" + (++discoverySeqId);
		}
	}
	

	/**
	 * 
	 * Get network discover provider.  It is considering IP based network discover.
	 * 
	 * @return -- A discovery id.
	 */
	public String discoverNetwork( final List<IpDiscoverySeed> discoverSeed, 
			                                            ElementDiscoverCB<ServiceElement> callback,
			                                            IntegerInterface integer ) {
			
		 String id = getNextDiscoveryId();
		 NetworkDiscovery netDisc = new NetworkDiscovery( discoverSeed, callback, id );
		 discoverMap.put(id, netDisc);
		 netDisc.discoverNetwork();
		 
		 return id;
	}
	
	
	
	/**
	 * Remove a discovery based on a given discovery id.
	 * 
	 * @param id
	 * @return
	 */
	public NetworkDiscoveryBase removeDiscovery( String id ) {
		
		return discoverMap.remove(id);
	}
	
	
	/**
	 * Stop discovery based on id.
	 * 
	 * @param id
	 */
	public void stopDiscovery( String id ) {
		
		NetworkDiscovery netDisc = discoverMap.get(id);
		if ( netDisc != null ) {
		    netDisc.stopDiscovery();	
		}
	}
	
	
	/**
	 * Get Network Discovery based on id.
	 * 
	 * @param id
	 * @return
	 */
	public NetworkDiscoveryBase getDiscovery( String id ) {
		
		return discoverMap.get(id);
		
	}

	
}
