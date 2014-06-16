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

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.technology.TechnologyManagerLocalInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class TechnologyManagerTest {

	@Inject
	TechnologyManagerLocalInterface technologyManager;
	
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("ServiceElementDiscoveryManagerTest.war");
	}

	@Test
	public void saveService() {
		Service service = new Service();
		
		service.setCreated(new Date());
		service.setLastModified(new Date());
		service.setDescription("Description of service");
		service.setDependsOn(TestUtil.createIdList(5, Service.class, "dependentService"));
		service.setTechnologies(TestUtil.createIdList(5, Service.class, "Technology"));
		
		try {
			technologyManager.updateService(service);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail("Error saveing service! " + e.toString());
		}
	}
	
	@Test
	public void loadService() {
		saveService();
		
		try {
			Service[] allServices = technologyManager.getAllServices();
			
			assert (allServices != null);
			assert(allServices.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			
			fail("Error loading services! " + e.toString());
		}
	}
	
	@Test
	public void saveTechnology() {
		Technology technology = new Technology();
		
		technology.setDescription("Descrption of technolgoy");
		technology.setName("Technology");
	
		technology.setMechanisims(TestUtil.createIdList(5, Mechanism.class, "Mechanism"));
		
		try {
			technologyManager.updateTechnology(technology);
		} catch (IntegerException e) {
		
			e.printStackTrace();
			fail("Error saveing technology " + e.toString());
		}
	}
	
	@Test
	public void loadTechnology() {
		saveTechnology();
		
		
		try {
			Technology[] allTechnologies = technologyManager.getAllTechnologies();
			
			assert(allTechnologies != null);
			assert(allTechnologies.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail("Error loading technologies " + e.toString());
		}
		
	}
	
	@Test
	public void saveMechanism() {
		Mechanism mechanism = new Mechanism();
		
		mechanism.setName("Mechansim");
		mechanism.setCapabilities(TestUtil.createIdList(5, Mechanism.class, "Mechanism"));
		
		try {
			technologyManager.updateMechanism(mechanism);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail("Error saveing Mechanism " + e.toString());
		}
		
	}
	
	@Test
	public void loadMechanism() {
		saveMechanism();
		
		
		try {
			Mechanism[] allMechanisms = technologyManager.getAllMechanisms();
			
			assert(allMechanisms != null);
			assert(allMechanisms.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail("Error loading mechanisms " + e.toString());
		}
		
	}
}
