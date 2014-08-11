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

package edu.harvard.integer.service.yaml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.technology.Service;
import edu.harvard.integer.common.yaml.YamlService;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.technology.TechnologyManagerInterface;

/**
 * @author David Taylor
 *
 */
public class YamlServiceParser implements YamlParserInterface<YamlService>{
	
	private Service[] allServices = null;
	
	private TechnologyManagerInterface technologyManager = null;
	
	public YamlServiceParser() {
		
	}
	

	public String parse(YamlService yamlService) throws IntegerException {
		
		if (yamlService == null)
			return "NoData";
		
		if (yamlService.getBusinessServices() == null)
			return "NoData";
		
		technologyManager = DistributionManager.getManager(ManagerTypeEnum.TechnologyManager);
		allServices = technologyManager.getAllServices();
		
		parseChildren(yamlService.getBusinessServices());
		
		return "success";
	}
	
	/**
	 * @param businessServices
	 * @return
	 * @throws IntegerException 
	 */
	private List<ID> parseChildren(List<YamlService> businessServices) throws IntegerException {
		
		List<ID> serviceIds = new ArrayList<ID>();
		
		for (YamlService service : businessServices) {
			Service dbService = getServiceByName(service.getName());
			
			if (dbService.getIdentifier() == null) {
				dbService.setName(service.getName());
				dbService.setCreated(new Date());
			}
			
			if (dbService.getDescription() == null || !dbService.getDescription().equals(service)) {
				dbService.setDescription(service.getDescription());
				dbService.setLastModified(new Date());
				
				Service businessService = technologyManager.updateService(dbService);
				serviceIds.add(businessService.getID());
			}
			
			if (service.getProviderServices() != null)
				dbService.setProviderServices(parseChildren(service.getProviderServices()));
			
			if (service.getUserServices() != null)
				dbService.setUserServices(parseChildren(service.getUserServices()));
		}
		
		return serviceIds;
	}

	private Service getServiceByName(String name) {
		for (Service service : allServices) {
			if (service.getName().equals(name))
				return service;
		}
		
		return new Service();
	}
}
