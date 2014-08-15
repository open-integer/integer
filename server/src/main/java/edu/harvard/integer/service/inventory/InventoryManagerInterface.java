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

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.inventory.InventoryRule;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * 
 */
public interface InventoryManagerInterface extends BaseManagerInterface {

	/**
	 * Update/Save the inventory rule.
	 * 
	 * @param rule
	 * @return The updated InventoryRule. This will have the identifier set when
	 *         the object is created.
	 * @throws IntegerException
	 */
	InventoryRule updateInventoryRule(InventoryRule rule)
			throws IntegerException;

	/**
	 * Get the InventoryRule for the given ID.
	 * 
	 * @param ruleId
	 * @return InventoryRule with the given ID.
	 * @throws IntegerException
	 */
	InventoryRule getInventoryRuleById(ID ruleId) throws IntegerException;

	/**
	 * Check inventory rule for this service element.
	 * 
	 * @param dbServiceElment
	 *            . ServiceElement that was found previously.
	 * @param newServiceElement
	 *            . The new ServiceElement discovered.
	 * @return The updated dbServiceElement to store in the database for the
	 *         current state of the ServiceElement
	 * @throws IntegerException
	 */
	ServiceElement checkServiceElement(ServiceElement dbServiceElment,
			ServiceElement newServiceElement) throws IntegerException;

}
