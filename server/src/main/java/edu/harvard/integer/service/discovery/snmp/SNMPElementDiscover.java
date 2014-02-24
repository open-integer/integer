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


import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.element.ElementDiscoveryBase;

/**
 * @author dchan
 *
 */
public class SNMPElementDiscover implements ElementDiscoveryBase {

	
	public SNMPElementDiscover() {}
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#discoverElementNode(edu.harvard.integer.agent.serviceelement.ElementEndPoint, edu.harvard.integer.access.snmp.DevicePhisicalPattern)
	 */
	@Override
	public ServiceElement discoverElementNode(ElementEndPoint endEpt,
			DevicePhisicalPattern topoPattern) throws IntegerException {
		
		
		
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#scanElementNode(edu.harvard.integer.agent.serviceelement.ElementEndPoint, edu.harvard.integer.common.topology.ServiceElement, edu.harvard.integer.access.snmp.DevicePhisicalPattern)
	 */
	@Override
	public void scanElementNode(ElementEndPoint endEpt,
			ServiceElement elementNode, DevicePhisicalPattern topoPattern)
			throws IntegerException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#scanElement(edu.harvard.integer.agent.serviceelement.ElementEndPoint, edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public void scanElement(ElementEndPoint endEpt, ServiceElement element)
			throws IntegerException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#checkReachable(edu.harvard.integer.agent.serviceelement.ElementEndPoint)
	 */
	@Override
	public String checkReachable(ElementEndPoint endEpt)
			throws IntegerException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
