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
package edu.harvard.integer.service.discovery.snmp;

import org.snmp4j.PDU;

import edu.harvard.integer.access.snmp.SnmpAyncListener;
import edu.harvard.integer.common.exception.ErrorCodeInterface;

/**
 * The listener interface for receiving discoverAync events.
 * The class that is interested in processing a discoverAync
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDiscoverAyncListener<code> method. When
 * the discoverAync event occurs, that object's appropriate
 * method is invoked.
 *
 * @author dchan
 */
public class DiscoverAyncListener extends SnmpAyncListener {

	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.access.SnmpAyncListener#applicationResponse(org.snmp4j.PDU, edu.harvard.integer.common.exception.ErrorCodeInterface, java.lang.String)
	 */
	@Override
	public void applicationResponse(PDU pdu, ErrorCodeInterface error,
			String errMsg) {
		// TODO Auto-generated method stub

	}

}
