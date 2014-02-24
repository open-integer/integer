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
package edu.harvard.integer.service.discovery.element;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.snmp.DevicePhisicalPattern;

/**
 * This interface layout the methods for discover IP based service element
 * which including SNMP elements and NON-SNMP elements.  For now discovery
 * for each ip element is synchronized calls.  It may changed in future.
 * 
 * @author dchan
 *
 */
public interface ElementDiscoveryBase {

	/**
	 * Discover element based on elementEndPoint and DevicePhisicalPattern.   
	 * The outcome of this method should return the full physical layout of the node.
	 *
	 * @param endEpt the element endpoint which including IPAddress, port and access information.
	 * @param topoPattern the topoPattern for discovery physical layout of the node.
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public ServiceElement discoverElementNode( ElementEndPoint endEpt, DevicePhisicalPattern topoPattern  ) throws IntegerException;
	
	/**
	 * Scan node element with a given node service element. It is considering a full scan which means during 
	 * the scan, some sub-component of given "element" may be added and some of them may missing.
	 *  
	 * @param endEpt
	 * @param elementNode
	 * @param topoPattern
	 * @throws IntegerException
	 */
	public void scanElementNode( ElementEndPoint endEpt, ServiceElement elementNode, DevicePhisicalPattern topoPattern ) throws IntegerException;
	
	
	/**
	 * Scan element with a given service element. It is considering a full scan. However it should
	 * not have any new sub-component being added or deleted any sub-component during scan.  
	 *
	 * @param endEpt the end ept
	 * @param element the element
	 * @throws IntegerException the integer exception
	 */
	public void scanElement( ElementEndPoint endEpt, ServiceElement element ) throws IntegerException;
	
	
	/**
	 * Check alive for that element.  If no exception occurs, the element is reachable.
	 *
	 * @param endEpt the element endPoint.
	 * @throws IntegerException the integer exception
	 * @return -- The identify of the element node such as sysObjectID
	 */
	public String checkReachable( ElementEndPoint endEpt ) throws IntegerException;
}
