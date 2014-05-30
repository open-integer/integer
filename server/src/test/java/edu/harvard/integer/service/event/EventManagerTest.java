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

package edu.harvard.integer.service.event;

import static org.junit.Assert.fail;

import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Level;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.discovery.DiscoveryStatusEnum;
import edu.harvard.integer.common.event.DiscoveryCompleteEvent;
import edu.harvard.integer.common.event.Event;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.selection.SelectionManagerTest;

/**
 * @author David Taylor
 *
 */
@RunWith(Arquillian.class)
public class EventManagerTest {

	@Inject
	private EventManagerInterface eventManager;

	private Logger logger = LoggerFactory.getLogger(SelectionManagerTest.class);

	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil
				.createTestArchive("EventManagerTest.war");
	}

	@Before
	public void setUpLogger() {
	//	org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
	}

	@Test
	public void updateEvent() {
		DiscoveryCompleteEvent discoveryComplete = new DiscoveryCompleteEvent();
		discoveryComplete.setDescription("My description");
		discoveryComplete.setDiscoveryStatus(DiscoveryStatusEnum.Complete);
		discoveryComplete.setEntityId(new ID(Long.valueOf(1), "Name", new IDType(DiscoveryCompleteEvent.class.getName())));
		discoveryComplete.setTime(new Date());
		
		try {
			eventManager.saveEvent(discoveryComplete);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Test
	public void getAllEvents() {
		
		try {
			Event[] allEvents = eventManager.getAllEvents();
			
			for (Event event : allEvents) {
				logger.info("Found event " + event.getID().toDebugString() + " " + event.getDescription());
			}
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAllDiscoveryEvents() {
		
		try {
			Event[] allEvents = eventManager.getAllDiscoveryEvents();
			
			for (Event event : allEvents) {
				logger.info("Found event " + event.getID().toDebugString() + " " + event.getDescription());
			}
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
