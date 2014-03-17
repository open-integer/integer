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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * @author dchan
 *
 */
public class DiscoveryManager {

	private static Logger logger = LoggerFactory.getLogger(DiscoveryManager.class);
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
	private ExecutorService elementPool = Executors.newFixedThreadPool(elementTaskLimit);
	
	
	
	public static int getDiscoveryTaskLimit() {
		return discoveryTaskLimit;
	}

	public static int getSubTaskLimit() {
		return subTaskLimit;
	}

	public static int getElementTaskLimit() {
		return elementTaskLimit;
	}

	public ExecutorService getPool() {
		return pool;
	}

	public ExecutorService getSubPool() {
		return subPool;
	}

	public ExecutorService getElementPool() {
		return elementPool;
	}

	public static DiscoveryManager getMgrInst() {
		return mgrInst;
	}

	private static DiscoveryManager mgrInst = new DiscoveryManager();
	
	public static DiscoveryManager getInstance() {
		
		return mgrInst;
	}
	
	public Future<DiscoverNode> sutmitElementTask( ElementDiscoverTask elmTask ) {
		return elementPool.submit(elmTask);
	}
}
