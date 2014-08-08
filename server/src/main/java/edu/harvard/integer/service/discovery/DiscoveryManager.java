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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SnmpGlobalReadCredential;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.discovery.DiscoveryRuleDAO;
import edu.harvard.integer.service.persistance.dao.discovery.IpTopologySeedDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SnmpGlobalReadCredentialDAO;

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
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#startDiscovery(edu.harvard.integer.common.ID)
	 */
	@Override
	public DiscoveryId startDiscovery(ID ruleId) throws IntegerException {
		
		return startDiscovery(getDiscoveryRuleById(ruleId));
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#startDiscovery(java.util.List)
	 */
	@Override
	public List<DiscoveryId> startDiscovery(List<ID> ruleIds) throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		List<DiscoveryId> discoveryIds = new ArrayList<DiscoveryId>();
		
		for (ID id : ruleIds) {
			DiscoveryRule rule = dao.findById(id);
			if (rule != null)
				discoveryIds.add(startDiscovery(rule));
		}
		
		return discoveryIds;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#stopDiscovery(edu.harvard.integer.common.discovery.DiscoveryId)
	 */
	@Override
	public void stopDiscovery(DiscoveryId discoverId) throws IntegerException {
		discoveryService.stopDiscovery(discoverId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#stopDiscovery(edu.harvard.integer.common.topology.DiscoveryRule)
	 */
	@Override
	public void stopDiscovery(DiscoveryRule rule) throws IntegerException {
		discoveryService.stopDiscovery(rule);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getRunningDiscoveries()
	 */
	@Override
	public DiscoveryId[] getRunningDiscoveries() throws IntegerException {
		return discoveryService.getRunningDiscoveries();
	}
	
	@Override
	public DiscoveryRule[] getRunningDiscoveryRules() throws IntegerException {
		return discoveryService.getRunningDiscoverieRules();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getAllDiscoveryRules()
	 */
	@Override
	public DiscoveryRule[] getAllDiscoveryRules() throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		return (DiscoveryRule[]) dao.copyArray(dao.findAll());
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
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getDiscoveryRuleByName(java.lang.String)
	 */
	@Override
	public DiscoveryRule getDiscoveryRuleByName(String name) throws IntegerException {
		DiscoveryRuleDAO dao = persistenceManager.getDiscoveryRuleDAO();
		
		return dao.findByName(name);
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
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getAllIpTopologySeeds()
	 */
	@Override
	public IpTopologySeed[] getAllIpTopologySeeds() throws IntegerException {
		IpTopologySeedDAO dao = persistenceManager.getIpTopologySeedDAO();
		
		return (IpTopologySeed[]) dao.copyArray(dao.findAll());
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#updateIpTopologySeed(edu.harvard.integer.common.topology.IpTopologySeed)
	 */
	@Override
	public IpTopologySeed updateIpTopologySeed(IpTopologySeed seed) throws IntegerException {
		IpTopologySeedDAO dao = persistenceManager.getIpTopologySeedDAO();
		
		return dao.update(seed);
	}
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getAllGlobalCredentails()
	 */
	@Override
	public SnmpGlobalReadCredential[] getAllGlobalCredentails() throws IntegerException {
		SnmpGlobalReadCredentialDAO dao = persistenceManager.getSnmpGlobalReadCredentialDAO();
		return (SnmpGlobalReadCredential[]) dao.copyArray(dao.findAll());
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#updateSnmpGlobalReadCredentail(edu.harvard.integer.common.snmp.SnmpGlobalReadCredential)
	 */
	@Override
	public SnmpGlobalReadCredential updateSnmpGlobalReadCredentail(SnmpGlobalReadCredential globalCredentail) throws IntegerException {
		SnmpGlobalReadCredentialDAO dao = persistenceManager.getSnmpGlobalReadCredentialDAO();
		
		return dao.update(globalCredentail);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.DiscoveryManagerInterface#getSnmpGlobalReadCredentialById(edu.harvard.integer.common.ID)
	 */
	@Override
	public SnmpGlobalReadCredential getSnmpGlobalReadCredentialById(ID id) throws IntegerException {
		SnmpGlobalReadCredentialDAO dao = persistenceManager.getSnmpGlobalReadCredentialDAO();
		
		return dao.findById(id);
	}
}
