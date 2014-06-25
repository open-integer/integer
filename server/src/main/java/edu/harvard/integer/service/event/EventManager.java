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

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.event.DiscoveryCompleteEvent;
import edu.harvard.integer.common.event.Event;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.event.DiscoveryCompleteEventDAO;
import edu.harvard.integer.service.persistance.dao.event.EventDAO;

/**
 * @see EventManagerInterface
 * @author David Taylor
 * 
 */
@Stateless
public class EventManager extends BaseManager implements EventManagerLocalInterface, EventManagerRemoteInterface {
	
	@Inject
	private Logger logger;
	
	@Inject
	private PersistenceManagerInterface persistenceManager;

	/**
	 * @param managerType
	 */
	public EventManager() {
		super(ManagerTypeEnum.EventManager);
		
	}


	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.event.EventManagerInterface#getAllEvents()
	 */
	@Override
	public Event[] getAllEvents() throws IntegerException {
		EventDAO dao = persistenceManager.getEventDAO();
		
		Event[] allEvents = dao.findAll();
		logger.info("Get all events! found " + allEvents.length);
		
		return allEvents;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.event.EventManagerInterface#getAllDiscoveryEvents()
	 */
	@Override
	public DiscoveryCompleteEvent[] getAllDiscoveryEvents() throws IntegerException {
		DiscoveryCompleteEventDAO dao = persistenceManager.getDiscoveryCompleteEventDAO();
		
		DiscoveryCompleteEvent[] discoveryCompleteEvents = dao.findAll();
		logger.info("Found " + discoveryCompleteEvents.length + " Discovery Complete Events");
		
		return discoveryCompleteEvents;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.event.EventManagerInterface#saveEvent(edu.harvard.integer.common.event.Event)
	 */
	@Override
	public Event saveEvent(Event event) throws IntegerException {
		EventDAO dao = persistenceManager.getEventDAO();
		
		return dao.update(event);
	}
}
