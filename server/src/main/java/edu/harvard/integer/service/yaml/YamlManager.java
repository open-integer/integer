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
import java.util.LinkedHashMap;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.technology.TechnologyManagerInterface;

/**
 * @author David Taylor
 *
 */
@Stateless
public class YamlManager extends BaseManager implements YamlManagerLocalInterface, YamlManagerRemoteInterface {

	@Inject
	private Logger logger;

	@Inject
	private TechnologyManagerInterface technologyManager;
	
	/**
	 * @param managerType
	 */
	public YamlManager() {
		super(ManagerTypeEnum.YamlManager);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.yaml.YamlManagerInterface#loadTechnologyTree(java.lang.String)
	 */
	@Override
	public String loadTechnologyTree(String content) {
		
		Yaml yaml = new Yaml();
		
		Object load = yaml.load(content);
		logger.info("YAML Object is " + load.getClass().getName());
		
		try {
			Technology[] root = technologyManager.getTopLevelTechnology();
			
			ID rootId = null;
			
			if (root == null || root.length == 0) {
				Technology rootTech = new Technology();
				rootTech.setName("root");
				rootTech = technologyManager.updateTechnology(rootTech);
				rootId = rootTech.getID();
			} else // Take first root. There should be only one!
				rootId = root[0].getID();
				
			parseObject(rootId, "", load);
			
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "Success";
	}
	

	private void parseArrayList(ID parentId, String indent, ArrayList<Object> list) throws IntegerException {
		for (Object value : list) {
			//logger.info("Item " + value + " class " + value.getClass().getName());
			parseObject(parentId, indent, value);
		}	
	}
		
	private void parseHashMap(ID parentId, String indent,LinkedHashMap<String, Object> map) throws IntegerException {
		for (String key : map.keySet()) {
			Object value = map.get(key);
			
			logger.info(indent + "Key " + key);
			
			if (key.contains("placeholder"))
				continue;
			
			Technology technology = technologyManager.getTechnologyByName(key);
			if (technology == null) {
				technology = createTechnology(key, parentId);
			}
			parentId = technology.getID();
			
			parseObject(parentId, indent, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseObject(ID parentId, String indent, Object value) throws IntegerException {
		if (value instanceof ArrayList)
			parseArrayList(parentId, indent + "  ", (ArrayList<Object>) value);
		else if (value instanceof LinkedHashMap) 
			parseHashMap(parentId, indent + "  ", (LinkedHashMap<String, Object>) value);
		else if (value instanceof String) {
			logger.info(indent + "String " + value);
			createTechnology((String) value, parentId);
		} else
			logger.error("Unknown Value type " + value.getClass().getName() + " " + value);
	}
	
	private Technology createTechnology(String name, ID parentId) throws IntegerException {

		if (name.contains("placeholder"))
			return null;
		
		Technology technology = new Technology();
		int indexOf = name.indexOf('(');
		if (indexOf > 0) {
			technology.setName(name.substring(0, name.indexOf('(') - 1));
			
			int endIndex = name.lastIndexOf(')');
			if (endIndex < indexOf)
				endIndex = name.length();
			
			technology.setDescription(name.substring(name.indexOf('(') + 1, endIndex));
		} else {
			technology.setDescription(name);
			technology.setName(name);
		}
		
		logger.info("Create Technology " + technology.getName() + "(" + technology.getName().length() + ")"
				+ "== " + technology.getDescription() + "(" + technology.getDescription().length() + ")");
		
		technology.setParentId(parentId);
		
		technology = technologyManager.updateTechnology(technology);
		
		return technology;
	}
	
	
}
