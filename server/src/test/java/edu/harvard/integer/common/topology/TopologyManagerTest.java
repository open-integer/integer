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

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.topology.TopologyManagerInterface;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class TopologyManagerTest {

	@Inject
	TopologyManagerInterface topologyManager;

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
	
	@Test
	public void addInterDeviceLink() {
		InterDeviceLink link = new InterDeviceLink();
		link.setCreated(new Date());
		link.setDestinationAddress(new Address("1.2.3.4"));
		link.setSourceAddress(new Address("2.3.4.5"));
		link.setLayer("2.5");
		
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
	
	public void addNetwork() {
		Network network = new Network();
		
		network.setCreated(new Date());
		network.setDescription("My Network");
		network.setLayer("2.33");
	}
	
}
