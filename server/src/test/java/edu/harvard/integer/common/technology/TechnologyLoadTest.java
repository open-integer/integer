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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import edu.harvard.integer.common.device.containment.ServiceElementTypeYaml;
import edu.harvard.integer.common.device.containment.VendorContainmentSelectorYaml;

/**
 * @author David Taylor
 *
 */
public class TechnologyLoadTest {


	@Test
	public void readCDPTechnology() {
		File mibFile = new File("../config/technology/cdp.yaml");
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
		
		Object load = yaml.load(content);
		System.out.println("YAML Object is " + load.getClass().getName());
//		System.out.println("YAML: " + load.toString());
		
		System.out.println("Technology read in: " + yaml.dump(load));
	}
	
	
	@Test
	public void readInterfaceTechnology() {
		File mibFile = new File("../config/technology/interfaces.yaml");
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
		
		Object load = yaml.load(content);
		System.out.println("YAML Object is " + load.getClass().getName());
//		System.out.println("YAML: " + load.toString());
		
		System.out.println("Technology read in: " + yaml.dump(load));
	}
	
	@Test
	public void readIv4Technology() {
		File mibFile = new File("../config/technology/ipv4.yaml");
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml(new Constructor(YamlTechnology.class));
		
		Object load = yaml.load(content);
		System.out.println("YAML Object is " + load.getClass().getName());
//		System.out.println("YAML: " + load.toString());
		
		System.out.println("Technology read in: " + yaml.dump(load));
	}
	
//	@Test
	public void readDeviceContainment() {
		File mibFile = new File("../config/technology/juniper-containment.yaml");
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml(new Constructor(VendorContainmentSelectorYaml.class));
		
		Object load = yaml.load(content);
		System.out.println("YAML Object is " + load.getClass().getName());
//		System.out.println("YAML: " + load.toString());
		
		System.out.println("Device Containment read in: " + yaml.dump(load));
	}
	
//	@Test
	public void readHostContainment() {
		File mibFile = new File("../config/technology/host-resources.yaml");
		
		String content = null;
		try {
			content = new String(Files.readAllBytes(mibFile.toPath()));

		} catch (IOException e) {

			e.printStackTrace();
			fail("Error loading MIB: " + e.getMessage());
		}
		
		Yaml yaml = new Yaml(new Constructor(VendorContainmentSelectorYaml.class));
		
		Object load = yaml.load(content);
		System.out.println("YAML Object is " + load.getClass().getName());
//		System.out.println("YAML: " + load.toString());
		
		System.out.println("Device Containment read in: " + yaml.dump(load));
		if (load instanceof VendorContainmentSelectorYaml) {
			VendorContainmentSelectorYaml vendorContainment = (VendorContainmentSelectorYaml) load;
			System.out.println("Found " + vendorContainment.getMappings().size()
					+ " Service Element mappings");
			for (ServiceElementTypeYaml set : vendorContainment.getServiceElementTypes()) {
				System.out.println("Service Element Type " + set.getName()
						+ " Children " + set.getChildServiceElementTypes());
			}
			System.out.println("Found " + vendorContainment.getServiceElementTypes().size()
					+ " Service Elements");
		}
	}
}
