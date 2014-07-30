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

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * The discovery manager is used to discover the network devices. This will be
 * used to start an instance of discovery with a DiscoveryRule that descibes the
 * parameters of the discovery.
 * 
 * @author David Taylor
 * 
 */
public interface DiscoveryManagerInterface extends BaseManagerInterface {

	/**
	 * Start a discovery with the given discovery rule.
	 * 
	 * @param rule
	 *            . This Discovery Rule defines the scope and device types to
	 *            discover
	 * 
	 * @return DiscoveryId that identifies this instance of a discovery.
	 * 
	 * @throws IntegerException
	 */
	public DiscoveryId startDiscovery(DiscoveryRule rule)
			throws IntegerException;

	/**
	 * Get all DiscoveryRule's in the database.
	 * 
	 * @return DiscoveryRule[] of all disovery rules.
	 * 
	 * @throws IntegerException
	 */
	DiscoveryRule[] getAllDiscoveryRules() throws IntegerException;

	/**
	 * Find the discovery rule with the give ID. If the rule is not found then
	 * null will be returned.
	 * 
	 * @param discoveryRuleId. ID of the discovery rule to find in the database.
	 * @return DiscoveryRule for the given ID. If not found then return null.
	 * @throws IntegerException
	 */
	DiscoveryRule getDiscoveryRuleById(ID discoveryRuleId)
			throws IntegerException;

	/**
	 * Update or save the Discovery Rule in the database. The Identifier for the 
	 * discovery rule will be valid after this call. 
	 * 
	 * @param rule. DiscoveryRule to save.
	 * @return DiscoveryRule that has been saved in the database.
	 * @throws IntegerException
	 */
	DiscoveryRule updateDiscoveryRule(DiscoveryRule rule)
			throws IntegerException;
}
