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

package edu.harvard.integer.common.technology;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.service.yaml.YamlManagerInterface;


/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class TechnologyLoadTest {
	
	private Logger logger = LoggerFactory.getLogger(TechnologyLoadTest.class);

	@Inject
	private YamlManagerInterface yamlManager;
	
	@Before
	public void initializeLogger() {
	
		//org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
	}
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil
				.createTestArchive("SelectionManagerTest.war");
	}

	@Test
	public void readTechnologyTree() {
		File techTree = new File("../config/technology/technology-tree.yaml");
		String content = null;
		try {
			content = new String(Files.readAllBytes(techTree.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml();
		
		Object load = yaml.load(content);
		logger.info("YAML Object is " + load.getClass().getName());
		
		parseObject("", load);
		
		logger.info("Technology read in: " + yaml.dump(load));
		
		yamlManager.loadTechnologyTree(content);
	}
	
	
	private void parseArrayList(String indent,ArrayList<Object> list) {
		for (Object value : list) {
			//logger.info("Item " + value + " class " + value.getClass().getName());
			parseObject(indent, value);
		}	
	}
		
	private void parseHashMap(String indent,LinkedHashMap<String, Object> map) {
		for (String key : map.keySet()) {
			Object value = map.get(key);
			
			logger.info(indent + "Key " + key);
			parseObject(indent, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseObject(String indent, Object value) {
		if (value instanceof ArrayList)
			parseArrayList(indent + "  ", (ArrayList<Object>) value);
		else if (value instanceof LinkedHashMap) 
			parseHashMap(indent + "  ", (LinkedHashMap<String, Object>) value);
		else if (value instanceof String)
			logger.info(indent + "String " + value);
		else
			logger.error("Unknown Value type " + value.getClass().getName() + " " + value);
	}
	
	
//
//
//	@Test
//	public void readCDPTechnology() {
//		File mibFile = new File("../config/technology/cdp.yaml");
//		
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(mibFile.toPath()));
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			fail("Error loading MIB: " + e.getMessage());
//		}
//		
//		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
//		
//		Object load = yaml.load(content);
//		System.out.println("YAML Object is " + load.getClass().getName());
////		System.out.println("YAML: " + load.toString());
//		
//		System.out.println("Technology read in: " + yaml.dump(load));
//	}
//	
//	
//	@Test
//	public void readInterfaceTechnology() {
//		File mibFile = new File("../config/technology/interfaces.yaml");
//		
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(mibFile.toPath()));
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			fail("Error loading MIB: " + e.getMessage());
//		}
//		
//		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
//		
//		Object load = yaml.load(content);
//		System.out.println("YAML Object is " + load.getClass().getName());
////		System.out.println("YAML: " + load.toString());
//		
//		System.out.println("Technology read in: " + yaml.dump(load));
//	}
//	
//	@Test
//	public void readIv4Technology() {
//		File mibFile = new File("../config/technology/ipv4.yaml");
//		
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(mibFile.toPath()));
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			fail("Error loading MIB: " + e.getMessage());
//		}
//		
//		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
//		
//		Object load = yaml.load(content);
//		System.out.println("YAML Object is " + load.getClass().getName());
////		System.out.println("YAML: " + load.toString());
//		
//		System.out.println("Technology read in: " + yaml.dump(load));
//	}
//	
////	@Test
//	public void readDeviceContainment() {
//		File mibFile = new File("../config/technology/juniper-containment.yaml");
//		
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(mibFile.toPath()));
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			fail("Error loading MIB: " + e.getMessage());
//		}
//		
//		Yaml yaml = new Yaml(new Constructor(VendorContainmentSelectorYaml.class));
//		
//		Object load = yaml.load(content);
//		System.out.println("YAML Object is " + load.getClass().getName());
////		System.out.println("YAML: " + load.toString());
//		
//		System.out.println("Device Containment read in: " + yaml.dump(load));
//	}
//	
////	@Test
//	public void readHostContainment() {
//		File mibFile = new File("../config/technology/host-resources.yaml");
//		
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(mibFile.toPath()));
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			fail("Error loading MIB: " + e.getMessage());
//		}
//		
//		Yaml yaml = new Yaml(new Constructor(VendorContainmentSelectorYaml.class));
//		
//		Object load = yaml.load(content);
//		System.out.println("YAML Object is " + load.getClass().getName());
////		System.out.println("YAML: " + load.toString());
//		
//		System.out.println("Device Containment read in: " + yaml.dump(load));
//		if (load instanceof VendorContainmentSelectorYaml) {
//			VendorContainmentSelectorYaml vendorContainment = (VendorContainmentSelectorYaml) load;
//			System.out.println("Found " + vendorContainment.getMappings().size()
//					+ " Service Element mappings");
//			for (ServiceElementTypeYaml set : vendorContainment.getServiceElementTypes()) {
//				System.out.println("Service Element Type " + set.getName()
//						+ " Children " + set.getChildServiceElementTypes());
//			}
//			System.out.println("Found " + vendorContainment.getServiceElementTypes().size()
//					+ " Service Elements");
//		}
//	}
}
