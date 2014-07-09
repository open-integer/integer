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

package edu.harvard.integer.common.topology;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.topology.TopologyManagerInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class TopologyManagerTest {

	@Inject
	private Logger logger;
	
	@Inject
	private TopologyManagerInterface topologyManager;
	
	@Inject
	private ServiceElementAccessManagerInterface serviceElementManger;

	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil.createTestArchive("TopologyManagerTest.war");
	}


	@Test
	public void addTopologyElement() {
		TopologyElement topologyElement = new TopologyElement();
		topologyElement.setName("New TopologyElement");
		topologyElement.setCreated(new Date());
		topologyElement.setModified(new Date());
		
		topologyElement.setLayer("2");

		try {
			topologyManager.updateTopologyElement(topologyElement);
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	
	@Test
	public void getAllTopologyElements() {
	
		TopologyElement[] topologyElements = null;
		try {
			topologyElements = topologyManager.getAllTopologyElements();
			if (topologyElements == null || topologyElements.length == 0) {
				addTopologyElement();
				topologyElements = topologyManager.getAllTopologyElements();
			}
			
			assert (topologyElements != null);
			assert (topologyElements.length > 0);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	private InterDeviceLink createInterDeviceLink() {
		InterDeviceLink link = new InterDeviceLink();
		link.setCreated(new Date());
		link.setDestinationAddress(new Address("1.2.3.4"));
		link.setSourceAddress(new Address("2.3.4.5"));
		link.setLayer("2.5");
		
		return link;
	}
	@Test
	public void addInterDeviceLink() {
		InterDeviceLink link = createInterDeviceLink();
		
		try {
			topologyManager.updateInterDeviceLink(link);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void getAllInterDeviceLinks() {
		
		try {
			InterDeviceLink[] allInterDeviceLinks = topologyManager.getAllInterDeviceLinks();
			if (allInterDeviceLinks == null || allInterDeviceLinks.length == 0) {
				addInterDeviceLink();
				allInterDeviceLinks = topologyManager.getAllInterDeviceLinks();
			}
				
			assert(allInterDeviceLinks != null);
			assert(allInterDeviceLinks.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void addNetwork() {
		Network network = new Network();
		
		network.setCreated(new Date());
		network.setDescription("My Network");
		network.setLayer("2.33");
		network.setReachable(Boolean.TRUE);
		
		List<InterDeviceLink> links = new ArrayList<InterDeviceLink>();
		links.add(createInterDeviceLink());
		network.setInterDeviceLinks(links);
		
		List<ServiceElement> serviceElements = new ArrayList<ServiceElement>();
		serviceElements.add(ServiceElementTest.createServiceElement());
		
		network.setServiceElements(serviceElements);
		
		List<Network> lowerNetworks = new ArrayList<Network>();
		Network lowerNetwork = new Network();
		
		lowerNetwork.setCreated(new Date());
		lowerNetwork.setDescription("My Network");
		lowerNetwork.setLayer("2.33");
		lowerNetwork.setReachable(Boolean.TRUE);
		lowerNetworks.add(lowerNetwork);
		
		network.setLowerNetworks(lowerNetworks);
		
		try {
			topologyManager.updateNetwork(network);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void createFakeData() {

		File deviceFile = new File("src/test/resources/topology");
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = " ";
	 
		List<Network> networks = new ArrayList<Network>();
		List<ServiceElement> serviceElements = new ArrayList<ServiceElement>();
		List<InterDeviceLink> links = new ArrayList<InterDeviceLink>();
		
		HashMap<String, List<String>> deviceLinks = new HashMap<String, List<String>>();
		
		try {
	 
			br = new BufferedReader(new FileReader(deviceFile));
			logger.info("Read in file " + deviceFile);
			
			while ((line = br.readLine()) != null) {
	 
				String[] device = line.split("\t");
	 
				
				if (device.length >= 2) {
					System.out.println("Address: " + device[0]
									+ " Link: " + device[1]);
					
					List<String> devLinks = deviceLinks.get(device[0]);
					if (devLinks == null) {
						devLinks = new ArrayList<String>();
						deviceLinks.put(device[0], devLinks);
					}
					
					devLinks.add(device[1]);
				
					links.add(createInterDeviceLink(device[0], device[1]));
					
				} else
					logger.info("Split into " + device.length + " columns " + line);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		for (String key : deviceLinks.keySet()) {
			ServiceElement serviceElement = getServiceElementByAddress(key, serviceElements);
			if (serviceElement == null) {
				serviceElements.add(createServiceElement(key));
			}
			
			String subnet = key.substring(0, key.lastIndexOf("."));
			Network network = findNetwork(subnet, networks);
			if (network == null) {
				network = createNetwork(subnet);
				networks.add(network);
			}
			
			network.getServiceElements().add(serviceElement);
			
			for (InterDeviceLink interDeviceLink : links) {
				String sourceSubnnet = interDeviceLink.getSourceAddress().getAddress().substring(0, interDeviceLink.getSourceAddress().getAddress().lastIndexOf("."));
				
				if (network.getName().equals(sourceSubnnet)) {
					boolean foundIt = false;
					for (InterDeviceLink networklink : network.getInterDeviceLinks()) {
						if (networklink.getSourceAddress().equals(interDeviceLink.getSourceAddress())) 
							foundIt = true;
					}
					if (!foundIt)
						network.getInterDeviceLinks().add(interDeviceLink);
				}
			}
			
		}
		
		logger.info("Found " + networks.size() + " networks");
		logger.info("Found " + serviceElements.size() + " Service Elements");
		logger.info("Found " + links.size() + " InterDeviceLinks");
		

//		for (ServiceElement serviceElement : serviceElements) {
//			try {
//				serviceElementManger.updateServiceElement(serviceElement);
//			} catch (IntegerException e) {
//			
//				e.printStackTrace();
//			}
//		}
		
		for (Network network : networks) {
			try {
				Network dbNetwork = topologyManager.updateNetwork(network);
				logger.info("Created network " + dbNetwork.getName() +
						" with " + dbNetwork.getServiceElements().size() + " Service Elements" +
						" and " + dbNetwork.getInterDeviceLinks().size() + " Links");
				
			} catch (IntegerException e) {
			
				e.printStackTrace();
			}
		}
		
//		for (InterDeviceLink interDeviceLink : links) {
//			try {
//				topologyManager.updateInterDeviceLink(interDeviceLink);
//			} catch (IntegerException e) {
//				e.printStackTrace();
//			}
//		}
		
	}

	

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	private InterDeviceLink createInterDeviceLink(String string, String string2) {
		
		InterDeviceLink link = new InterDeviceLink();
		
		link.setCreated(new Date());
		link.setSourceAddress(new Address(string));
		link.setDestinationAddress(new Address(string2));
		link.setName(string + " - " + string2);
		link.setModified(new Date());
		
		return link;
	}

	private Network findNetwork(String address, List<Network> networks) {
		for (Network network : networks) {
			if (network.getName().equals(address))
				return network;
		}
		
		return null;
	}
	
	private Network createNetwork(String address) {
		Network network = new Network();
		network.setCreated(new Date());
		network.setModified(new Date());
		network.setName(address);
		network.setServiceElements(new ArrayList<ServiceElement>());
		network.setInterDeviceLinks(new ArrayList<InterDeviceLink>());
		
		return network;
	}

	/**
	 * @param string
	 * @return
	 */
	private ServiceElement createServiceElement(String string) {
		
		ServiceElement serviceElement = new ServiceElement();
		serviceElement.setName(string);
		serviceElement.setCreated(new Date());
		serviceElement.setDescription("Router " + string);
		
		return serviceElement;
	}


	/**
	 * @param string
	 * @param serviceElements
	 * @return
	 */
	private ServiceElement getServiceElementByAddress(String string,
			List<ServiceElement> serviceElements) {
		
		for (ServiceElement serviceElement : serviceElements) {
			if (serviceElement.getName().equals(string))
				return serviceElement;
		}
		
		return null;
	}
}
