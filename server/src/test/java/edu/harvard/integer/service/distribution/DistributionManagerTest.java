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

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.BaseServiceInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class DistributionManagerTest {
	

	//@Inject
	private Logger logger= LoggerFactory.getLogger(DistributionManagerTest.class);
//
//	  @Resource(name="&entityManagerFactory")
//	  private LocalContainerEntityManagerFactoryBean entityManagerFactory;
//	
	  @PersistenceContext
	  private EntityManager em;

	  
	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("DistributionManagerTest.war");
	}
	
	@Before
	public void initializeLogger() {
		//BasicConfigurator.configure();
	//	org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
	}


	@Test
	public void getDiscoveryService() {
		try {
			BaseServiceInterface service = DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
			
			assert(service != null);
			
			logger.info("Found DiscoveryService " + service);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getPersistenceService() {
		try {
			BaseServiceInterface service = DistributionManager.getService(ServiceTypeEnum.PersistenceService);
			
			assert(service != null);
			
			logger.info("Found PersistenceService " + service);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void TopologyService() {
		try {
			BaseServiceInterface service = DistributionManager.getService(ServiceTypeEnum.TopologyService);
			
			assert(service != null);
			
			logger.info("Found TopologyService " + service);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getLocalManagementObjectCapabilityManager() {
		try {
			BaseManagerInterface service = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
			
			assert(service != null);
			
			logger.info("Found ManagementObjectCapabilityManager " + service);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void getLocalSnmpManager() {
		try {
			BaseManagerInterface service = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
			
			assert(service != null);
			
			logger.info("Found SnmpManager " + service);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void getLocalAllManager() {
		try {
			
			for (ManagerTypeEnum managerType : ManagerTypeEnum.values()) {
				BaseManagerInterface service = DistributionManager.getManager(managerType);
				
				logger.info("Lookup manager " + managerType);
				
				assert(service != null);
				
			}
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void getLocalAllServices() {
		try {
			
			for (ServiceTypeEnum serviceType : ServiceTypeEnum.values()) {
				BaseServiceInterface service = DistributionManager.getService(serviceType);
				
				assert(service != null);
				
				logger.info("Found " + serviceType + " Instance " + service);
				
			}
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	
	
	
//	@Test
//	public void getWebServerPort() {
//		try {
//			Integer webServerPort = JMXConsole.getWebServerPort();
//			logger.info("Got web server port " + webServerPort);
//		} catch (IntegerException e) {
//			
//			e.printStackTrace();
//			fail(e.toString());
//		}
//	}
}
