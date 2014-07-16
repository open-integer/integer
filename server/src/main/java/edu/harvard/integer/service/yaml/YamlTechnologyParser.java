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
import edu.harvard.integer.common.technology.Mechanism;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.LayerTypeEnum;
import edu.harvard.integer.common.yaml.YamlCapability;
import edu.harvard.integer.common.yaml.YamlMechanismType;
import edu.harvard.integer.common.yaml.YamlTechnology;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.technology.TechnologyDAO;
import edu.harvard.integer.service.technology.TechnologyManagerInterface;

/**
 * @author David Taylor
 *
 */
public class YamlTechnologyParser {

	private List<Technology> technologies = null;
	
	private TechnologyManagerInterface technologyManager = null;
	
	private PersistenceManagerInterface persistenceManager = null;
	
	
	public YamlTechnologyParser(TechnologyManagerInterface technologyManager, 
			PersistenceManagerInterface persistenceManager) {
		this.technologyManager = technologyManager;
		this.persistenceManager = persistenceManager;
		
		this.technologies = new ArrayList<Technology>();
		
	}
	
	public void init() throws IntegerException {
		
		TechnologyDAO technologyDAO = persistenceManager.getTechnologyDAO();
		Technology[] alltechnologies = technologyDAO.findAll();
		
		for (Technology technology : alltechnologies) {
			technologies.add(technology);
		}
	}
	
	/**
	 * Parse the Technology tree. This method is called recursively to process
	 * all sub "TechnologyTree" elements. The current Technology is passed in so
	 * that the child technology elements can be linked to the parent.
	 * 
	 * @param technology
	 *            . The current Root of the Technology tree to parse.
	 * @param list
	 *            . YamlTechnology of the child technologies to be parsed.
	 * 
	 * @throws IntegerException
	 */
	public void parseTechnologyTree(Technology parentTechnology,
			List<YamlTechnology> list) throws IntegerException {

		for (YamlTechnology node : list) {
			Technology technology = technologyManager.getTechnologyByName(node
					.getName());
			if (technology == null) {
				technology = new Technology();
				technology.setName(node.getName());
			}

			Technology parent = getTechnology(node.getParent());
			if (parent != null)
				technology.setParentId(parent.getID());
			else
				technology.setParentId(parentTechnology.getID());
			
			technology.setDescription(node.getDescription());
			technology.setMechanisims(saveMechanisms(technology,
					node.getMechanisms()));
			
			// Set layer on sub technologies if the parent has a layer and the child does not.
			LayerTypeEnum layer = LayerTypeEnum.getLayerTypeEnum(node.getLayer());
			
			if (LayerTypeEnum.Unknown.equals(layer) && parentTechnology.getLayer() != null
					&& !LayerTypeEnum.Unknown.equals(parentTechnology.getLayer()))
				
				technology.setLayer(parentTechnology.getLayer());
			else
				technology.setLayer(layer);
			
			technology.setDiscoveryTypes(createDiscoveryTypeEnumList(node.getDiscovery()));
			
			technology = technologyManager.updateTechnology(technology);

			technologies.add(technology);
			
			if (node.getTechnologies() != null)
				parseTechnologyTree(technology, node.getTechnologies());

		}

	}
	
	private Technology getTechnology(String name) {
		if (name == null)
			return null;
		
		for (Technology technology : technologies) {
			
			if (technology.getName() != null && technology.getName().toLowerCase().equals(name.toLowerCase()))
				return technology;
		}
		
		return null;
	}

	/**
	 * @param discovery
	 * @return
	 */
	private List<DiscoveryTypeEnum> createDiscoveryTypeEnumList(
			List<String> discovery) {
		
		if (discovery == null)
			return null;
		
		List<DiscoveryTypeEnum> discoveryTypes = new ArrayList<DiscoveryTypeEnum>();
		
		for (String string : discovery) {
			discoveryTypes.add(DiscoveryTypeEnum.valueOf(string));
		}
		
		return discoveryTypes;
	}
	/**
	 * Parse the Mechanisms for a technology. The mechanism is the lowest level
	 * of the technology tree that does not have capabilities directly attached
	 * to it.
	 * 
	 * @param mechanismTypes
	 * @throws IntegerException
	 */
	private List<ID> saveMechanisms(Technology technology,
			List<YamlMechanismType> mechanismTypes) throws IntegerException {

		List<ID> ids = new ArrayList<ID>();

		if (mechanismTypes == null)
			return ids;

		for (YamlMechanismType mechanism : mechanismTypes) {
			Mechanism dbMechanism = technologyManager
					.getMechanismByName(mechanism.getName());

			if (dbMechanism == null) {
				dbMechanism = new Mechanism();
				dbMechanism.setName(mechanism.getName());
			}

			dbMechanism.setDescription(mechanism.getDescription());

			List<ID> capabilitiIds = saveCapabilites(mechanism
					.getCapabilities());

			dbMechanism.setCapabilities(capabilitiIds);
			dbMechanism = technologyManager.updateMechanism(dbMechanism);

			ids.add(dbMechanism.getID());
		}

		return ids;
	}
	
	/**
	 * Save the Capabilities found.
	 * 
	 * @param capabilities
	 *            . YamlCapability list of capabilities to import.
	 * @return List<ID>. IDs' of the imported capabilities.
	 * @throws IntegerException
	 */
	private List<ID> saveCapabilites(List<YamlCapability> capabilities)
			throws IntegerException {

		CapabilityDAO dao = persistenceManager.getCapabilityDAO();

		List<ID> ids = new ArrayList<ID>();

		if (capabilities == null)
			return ids;

		for (YamlCapability yamlCapability : capabilities) {
			Capability dbCapablity = dao.findByName(yamlCapability.getName());
			if (dbCapablity == null) {
				dbCapablity = new Capability();
				dbCapablity.setName(yamlCapability.getName());
			}

			dbCapablity.setDescription(yamlCapability.getDescription());

			dbCapablity = dao.update(dbCapablity);

			ids.add(dbCapablity.getID());
		}

		return ids;
	}

}
