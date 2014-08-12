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
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
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

	private static Address sourceAddress = new Address("1.2.3.4", "255.255.255.0");
	private static Address destAddress = new Address("2.3.4.5", "255.255.255.0");
	
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
		
		topologyElement.setLayer(LayerTypeEnum.Two);

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
	
	@Test
	public void updateMapItemPostition() {
		
		addMapItemPosition();
	}
	
	private MapItemPosition addMapItemPosition() {
		MapItemPosition position = new MapItemPosition();
		position.setIconName("MyIcon");
		position.setItemId(new ID(Long.valueOf(1), "MyItem", new IDType("IDType")));
		position.setMapId(new ID(Long.valueOf(2), "MyMap", new IDType("IDType")));
		position.setUserId(new ID(Long.valueOf(3), "MyUser", new IDType("IDType")));
		position.setXposition(3);
		position.setYposition(5);
		
		try {
			position = topologyManager.updateMapItemPosition(position);
			
			assert (position != null);
			assert (position.getIdentifier() != null);
			return position;
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		return null;
	}
	
	@Test
	public void getAllNetworkPositions() {
		
		try {
			MapItemPosition[] allNetworkPositions = topologyManager.getAllNetworkPositions();
			if (allNetworkPositions == null || allNetworkPositions.length == 0)
				addMapItemPosition();
			
			allNetworkPositions = topologyManager.getAllNetworkPositions();
			assert(allNetworkPositions != null);
			assert(allNetworkPositions.length > 0);
			
		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	private InterDeviceLink createInterDeviceLink() {
		
		ServiceElement sourceServiceElement = null;
		ServiceElement destServiceElement = null;
		try {
			sourceServiceElement = serviceElementManger.updateServiceElement(createServiceElement(sourceAddress.getAddress()));
			destServiceElement = serviceElementManger.updateServiceElement(createServiceElement(destAddress.getAddress()));
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}

		InterDeviceLink link = new InterDeviceLink();
		link.setCreated(new Date());
		link.setSourceAddress(sourceAddress);
		link.setSourceServiceElementId(sourceServiceElement.getID());
		
		link.setDestinationAddress(destAddress);
		link.setDestinationServiceElementId(destServiceElement.getID());
		
		link.setLayer(LayerTypeEnum.TwoAndHalf);
		
		return link;
	}
	
	@Test
	public void addInterDeviceLink() {

		insertInterDeviceLink();
	}
	
	private InterDeviceLink insertInterDeviceLink() {

		InterDeviceLink link = createInterDeviceLink();
		
		try {
			link = topologyManager.updateInterDeviceLink(link);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
		return link;
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
	public void getInterDeviceLinksBetweenDevices() {
		InterDeviceLink srcLink = insertInterDeviceLink();
	
		InterDeviceLink[] links = null;
		try {
			links = topologyManager.getInterDeviceLinksBySourceDestServiceElementIDs(srcLink.getSourceServiceElementId(), srcLink.getDestinationServiceElementId());
			
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
		assert (links != null);
		assert (links.length > 0);
			
	}
	
	
	
	@Test
	public void addNetwork() {
		Network network = new Network();
		
		network.setCreated(new Date());
		network.setDescription("My Network");
		network.setLayer(LayerTypeEnum.TwoAndHalf);
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
		lowerNetwork.setLayer(LayerTypeEnum.TwoAndHalf);
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
	public void getNetworkInformation() {
		try {
			NetworkInformation networkInformation = topologyManager.getNetworkInformation();
		
			assert(networkInformation != null);
			
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getInterNetworkLinks() {
		
		Network network = null;
		Network destnetwork = null;
		try {
			network = topologyManager.getNetworkByAddress(sourceAddress);
			if (network == null) {
				insertInterDeviceLink();
				
				network = topologyManager.getNetworkByAddress(sourceAddress);
			}
		
			destnetwork = topologyManager.getNetworkByAddress(destAddress);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
		assert (network != null);
		assert (destnetwork != null);
		
		InterNetworkLink[] links = null;
		
		try {
			links = topologyManager.getInterNetworkLinksBySourceDestNetworkIDs(network.getID(), destnetwork.getID());
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
		assert(links != null);
		assert(links.length > 0);
	}
	
	@Test
	public void createFakeData() {

		File deviceFile = new File("/Users/dtaylor/topology");
		
		BufferedReader br = null;
		String line = null;
	 
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
					
					ServiceElement sourceServiceElement = serviceElementManger.getServiceElementByName(device[0]);
					if (sourceServiceElement == null)
						sourceServiceElement = serviceElementManger.updateServiceElement(createServiceElement(device[0]));
					
					serviceElements.add(sourceServiceElement);
					ServiceElement itfServiceElement = createInterfaceServiceElement("If " + device[1]);
					itfServiceElement.setParentId(sourceServiceElement.getID());
					serviceElementManger.updateServiceElement(itfServiceElement);
					
					ServiceElement destServiceElement = serviceElementManger.getServiceElementByName(device[1]);
					if (destServiceElement == null)
						destServiceElement = serviceElementManger.updateServiceElement(createServiceElement(device[1]));
					
					serviceElements.add(destServiceElement);
					
					InterDeviceLink link = createInterDeviceLink(device[0], device[1]);
					InterDeviceLink[] dbLinks = topologyManager.getInterDeviceLinksBySourceDestAddress(link.getSourceAddress(), link.getDestinationAddress());
					if (dbLinks != null && dbLinks.length > 0)
						link = dbLinks[0];
					
					link.setSourceServiceElementId(sourceServiceElement.getID());
					link.setDestinationServiceElementId(destServiceElement.getID());
					
					links.add(link);
					topologyManager.updateInterDeviceLink(link);
					
				} else
					logger.info("Split into " + device.length + " columns " + line);
			}
	 
		} catch (FileNotFoundException e) {
			logger.warn("topology file not found!! No fake data will be created");
			
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (IntegerException e) {
		
			e.printStackTrace();
			fail(e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	
		logger.info("Found " + networks.size() + " networks");
		logger.info("Found " + serviceElements.size() + " Service Elements");
		logger.info("Found " + links.size() + " InterDeviceLinks");
		

		for (Network network : networks) {
			try {
				Network dbNetwork = topologyManager.updateNetwork(network);
				logger.info("Created network " + dbNetwork.getName() +
						" with " + dbNetwork.getServiceElements().size() + " Service Elements" +
						" and " + dbNetwork.getInterDeviceLinks().size() + " Links");
				
				for (ServiceElement servieElement : network.getServiceElements()) {
					logger.info("Added ServieElement " + servieElement.getID().toDebugString());
				}
				
			} catch (IntegerException e) {
			
				e.printStackTrace();
			}
		}
		
	}

	

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	private InterDeviceLink createInterDeviceLink(String string, String string2) {
		
		InterDeviceLink link = new InterDeviceLink();
		
		link.setCreated(new Date());
		link.setSourceAddress(new Address(string, "255.255.255.0"));
		link.setDestinationAddress(new Address(string2, "255.255.255.0"));
		link.setName(string + " - " + string2);
		link.setModified(new Date());
		
		return link;
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

	private ServiceElement createInterfaceServiceElement(String name) {
		ServiceElement serviceElement = new ServiceElement();
		serviceElement.setName(name);
		serviceElement.setCreated(new Date());
		serviceElement.setDescription("Interface " + name);
		
		return serviceElement;
	}

	@Test
	public void getLinksForSourceDestAddress() {
		InterDeviceLink[] links = null;
		try {
			links = topologyManager.getInterDeviceLinksBySourceDestAddress(sourceAddress, destAddress);
			if (links == null || links.length == 0)
				topologyManager.updateInterDeviceLink(createInterDeviceLink());
			
			links = topologyManager.getInterDeviceLinksBySourceDestAddress(sourceAddress, destAddress);
			
			assert (links != null);
			assert (links.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			
			fail(e.toString());
		}
	}
	
	@Test
	public void getPathsForSourceDestAddress() {
		
		try {
			Path path = topologyManager.getPathBySourceDestAddress(sourceAddress, destAddress);
			
			if (path == null) {
				topologyManager.updatePath(createPath());
				
				path = topologyManager.getPathBySourceDestAddress(sourceAddress, destAddress);
			}
			
			assert (path != null);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}

	/**
	 * @return
	 */
	private Path createPath() {
		Path path = new Path();
		path.setCreated(new Date());
		path.setModified(new Date());
		path.setName(sourceAddress.getAddress() + " - " + destAddress.getAddress());
		path.setSourceAddress(sourceAddress);
		path.setDestinationAddress(destAddress);
		
		return path;
	}
	
	@Test
	public void getAllPaths() {
		
		try {
			Path[] paths = topologyManager.getAllPaths();
			if (paths == null || paths.length == 0) 
				topologyManager.updatePath(createPath());
			
			paths = topologyManager.getAllPaths();
			
			assert (paths != null);
			assert (paths.length > 0);
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
	}
}

