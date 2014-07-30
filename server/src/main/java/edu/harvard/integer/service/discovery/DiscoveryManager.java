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
package edu.harvard.integer.service.discovery;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.discovery.DiscoveryRuleDAO;

/**
 * @author dchan
 *
 */
@Stateless
public class DiscoveryManager  extends BaseManager implements DiscoveryManagerLocalInterface, DiscoveryManagerRemoteInterface {

	@Inject
	private Logger logger;
	
	@Inject
	private DiscoveryServiceInterface discoveryService;
	

	@Inject
	private PersistenceManagerInterface persistenceManager;
	
	/**
	 * @param managerType
	 */
	public DiscoveryManager() {
		super(ManagerTypeEnum.DiscoveryManager);
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#startDiscovery(edu.harvard.integer.common.topology.DiscoveryRule)
	 */
	@Override
	public DiscoveryId startDiscovery(DiscoveryRule rule) throws IntegerException {
		
		logger.info("Start discovery of " + rule);
		
		return discoveryService.startDiscovery(rule);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getAllDiscoveryRules()
	 */
	@Override
	public DiscoveryRule[] getAllDiscoveryRules() throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getDiscoveryRuleById(edu.harvard.integer.common.ID)
	 */
	@Override
	public DiscoveryRule getDiscoveryRuleById(ID discoveryRuleId) throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		return dao.findById(discoveryRuleId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#updateDiscoveryRule(edu.harvard.integer.common.topology.DiscoveryRule)
	 */
	@Override
	public DiscoveryRule updateDiscoveryRule(DiscoveryRule rule) throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		return dao.update(rule);
	}
}
