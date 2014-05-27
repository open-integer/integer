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

package edu.harvard.integer.service.distribution;

import edu.harvard.integer.common.distribution.DistributedServiceInterface;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.BaseServiceInterface;
import edu.harvard.integer.service.discovery.DiscoveryService;
import edu.harvard.integer.service.discovery.DiscoveryServiceInterface;
import edu.harvard.integer.service.persistance.PersistenceService;
import edu.harvard.integer.service.persistance.PersistenceServiceInterface;
import edu.harvard.integer.service.selection.SelectionService;
import edu.harvard.integer.service.selection.SelectionServiceInterface;
import edu.harvard.integer.service.topology.TopologyService;
import edu.harvard.integer.service.topology.TopologyServiceInterface;


/**
 * @author David Taylor
 *
 */
public enum ServiceTypeEnum implements DistributedServiceInterface {
	DiscoveryService(DiscoveryService.class, DiscoveryServiceInterface.class),
	TopologyService(TopologyService.class, TopologyServiceInterface.class),
	PersistenceService(PersistenceService.class, PersistenceServiceInterface.class),
	DistributionService(DistributionService.class, DistributionServiceInterface.class),
	SelectionService(SelectionService.class, SelectionServiceInterface.class);
	
	Class<? extends BaseService> mgrClazz;
	Class<? extends BaseServiceInterface> intfClazz;
	
	private ServiceTypeEnum(Class<? extends BaseService> mgrClazz,
			Class<? extends BaseServiceInterface> intfClazz) {
	
		this.intfClazz = intfClazz;
		this.mgrClazz = mgrClazz;
	}

	/**
	 * @return
	 */
	public Class<? extends BaseService> getServiceClass() {
		
		return mgrClazz;
	}

	/**
	 * @return
	 */
	public Class<? extends BaseServiceInterface> getBeanLocalInterfaceClass() {
		
		return intfClazz;
	}
	
}
