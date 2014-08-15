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

package edu.harvard.integer.service.inventory;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.inventory.InventoryRule;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.inventory.InventoryRuleDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;

/**
 * @author David Taylor
 *
 */
@Stateless
public class InventoryManager extends BaseManager implements InventoryManagerLocalInterface, InventoryManagerRemoteInterface {

	@Inject
	private Logger logger;
	
	@Inject
	private PersistenceManagerInterface persistenceManager;
	
	/**
	 * @param managerType
	 */
	public InventoryManager() {
		super(ManagerTypeEnum.InventoryManager);
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.inventory.InventoryManagerInterface#updateInventoryRule(edu.harvard.integer.common.inventory.InventoryRule)
	 */
	@Override
	public InventoryRule updateInventoryRule(InventoryRule rule) throws IntegerException {
		InventoryRuleDAO dao = persistenceManager.getInventoryRuleDAO();
		
		return dao.update(rule);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.inventory.InventoryManagerInterface#getInventoryRuleById(edu.harvard.integer.common.ID)
	 */
	@Override
	public InventoryRule getInventoryRuleById(ID ruleId) throws IntegerException {
		InventoryRuleDAO dao = persistenceManager.getInventoryRuleDAO();
		
		return dao.findById(ruleId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.inventory.InventoryManagerInterface#checkServiceElement(edu.harvard.integer.common.topology.ServiceElement, edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public ServiceElement checkServiceElement(ServiceElement dbServiceElment, ServiceElement newServiceElement) throws IntegerException {
		ServiceElementDAO dao = persistenceManager.getServiceElementDAO();
		
		return dao.copyFields(dbServiceElment, newServiceElement);
	}
	
}
