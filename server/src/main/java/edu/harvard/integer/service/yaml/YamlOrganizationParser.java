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
import java.util.List;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.technology.Service;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.common.user.Organization;
import edu.harvard.integer.common.yaml.YamlOrganization;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.technology.TechnologyManagerInterface;
import edu.harvard.integer.service.user.UserManagerInterface;

/**
 * @author David Taylor
 * 
 */
public class YamlOrganizationParser implements YamlParserInterface<YamlOrganization> {
	
	private Organization[] allOrginazations = null;

	private UserManagerInterface userManager = null;

	/**
	 * @param yamlOrganization
	 */
	public YamlOrganizationParser() {
		
	}

	/**
	 * @return
	 * @throws IntegerException
	 */
	@Override
	public String parse(YamlOrganization yamlOrganization) throws IntegerException {
	
		if (yamlOrganization == null)
			return "NoData";

		userManager = DistributionManager
				.getManager(ManagerTypeEnum.UserManager);
		allOrginazations = userManager.getAllOrganizations();

		parseChildOrganizations(yamlOrganization.getOrganizations());
		return "success";
	}

	private List<ID> parseChildOrganizations(
			List<YamlOrganization> organizations) throws IntegerException {
		List<ID> ids = new ArrayList<ID>();

		if (organizations != null) {
			for (YamlOrganization yamlOrganization : organizations) {
				if (yamlOrganization.getOrganizations() != null)
					System.out.println("Add Organization: " + yamlOrganization.getName() + " With "
							+ yamlOrganization.getOrganizations().size() + " children");
				else
					System.out.println("Add Organization: " + yamlOrganization.getName() + " With no children");
				
				Organization organization = getOrganizationByName(yamlOrganization
						.getName());
				organization.setDescription(yamlOrganization.getDescription());

				if (yamlOrganization.getBusinessServices() != null) 
					organization.setBusinessServices(createBusinessServiceIDs(yamlOrganization.getBusinessServices()));
				
				if (yamlOrganization.getTechnologyServices() != null)
					organization.setTechnologies(createTechnologyIDs(yamlOrganization.getTechnologyServices()));
				
				if (yamlOrganization.getOrganizations() != null)
					organization
					.setChildOrginizations(parseChildOrganizations(yamlOrganization
							.getOrganizations()));

				organization = userManager.updateOrganization(organization);
				ids.add(organization.getID());
			}
		}

		return ids;
	}

	/**
	 * @param businessServices
	 * @return
	 * @throws IntegerException 
	 */
	private List<ID> createBusinessServiceIDs(List<String> businessServices) throws IntegerException {
		List<ID> ids = new ArrayList<ID>();
		
		TechnologyManagerInterface technologyManager = DistributionManager.getManager(ManagerTypeEnum.TechnologyManager);
		
		for (String name : businessServices) {
			Service service = technologyManager.getServiceByName(name);
			
			if (service != null)
				ids.add(service.getID());
		}
		
		return ids;
	}

	/**
	 * @param businessServices
	 * @return
	 * @throws IntegerException 
	 */
	private List<ID> createTechnologyIDs(List<String> technologies) throws IntegerException {
		List<ID> ids = new ArrayList<ID>();
		
		TechnologyManagerInterface technologyManager = DistributionManager.getManager(ManagerTypeEnum.TechnologyManager);
		
		for (String name : technologies) {
			Technology technology = technologyManager.getTechnologyByName(name);
			
			if (technology != null)
				ids.add(technology.getID());
		}
		
		return ids;
	}

	private Organization getOrganizationByName(String name) {
		
		for (Organization organization : allOrginazations) {
			if (organization.getName().equals(name))
				return organization;
		}

		Organization organization = new Organization();
		organization.setName(name);
		
		return organization;
	}

}
