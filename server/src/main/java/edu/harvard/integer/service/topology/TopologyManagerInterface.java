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

package edu.harvard.integer.service.topology;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.Path;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * 
 */
public interface TopologyManagerInterface extends BaseManagerInterface {

	/**
	 * Find all the Network objects in the database. Return the list.
	 * 
	 * @return
	 * @throws IntegerException
	 */
	Network[] getAllNetworks() throws IntegerException;

	/**
	 * Update a Network in the database.
	 * 
	 * @param network
	 * @return
	 * @throws IntegerException
	 */
	Network updateNetwork(Network network) throws IntegerException;

	/**
	 * Get all InterDeviceLinks that are in the database.
	 * 
	 * @return InterDeviceLink[] of all InterDeviceLinks
	 * @throws IntegerException
	 */
	InterDeviceLink[] getAllInterDeviceLinks() throws IntegerException;

	/**
	 * Update the InterDeviceLink in the database.
	 * 
	 * @param interDeviceLink
	 * @return InterDeviceLink that has been updated. If this is a new
	 *         InterDeviceLink then the identifier will be set on the returned
	 *         object.
	 * @throws IntegerException
	 */
	InterDeviceLink updateInterDeviceLink(InterDeviceLink interDeviceLink)
			throws IntegerException;

	/**
	 * Get the list of all topology elements in the database.
	 * 
	 * @return TopologyElement[] of all TopologyElements
	 * @throws IntegerException
	 */
	TopologyElement[] getAllTopologyElements() throws IntegerException;

	/**
	 * Update the toplogy element in the database.
	 * 
	 * @param topologyElement
	 * @return TopologyElement that has been updated in the database. The new
	 *         object will have the identifier set.
	 * @throws IntegerException
	 */
	TopologyElement updateTopologyElement(TopologyElement topologyElement)
			throws IntegerException;

	/**
	 * Get the TopologyElements for the given service element ID. 
	 * 
	 * @param serviceElementId
	 * @return TopologyElement[] for the ServiceElement ID.
	 * @throws IntegerException
	 */
	public TopologyElement[] getTopologyElementsByServiceElement(ID serviceElementId) throws IntegerException;
	
	/**
	 * Update the path in the database. If this is a new object then the
	 * identifier will be set on the returned Path object.
	 * 
	 * @param path
	 * @return Path with the identifier set.
	 * @throws IntegerException
	 */
	Path updatePath(Path path) throws IntegerException;

	/**
	 * Get a list of all paths found in the database.
	 * 
	 * @return Path[] of all paths in the database.
	 * @throws IntegerException
	 */
	Path[] getAllPaths() throws IntegerException;

	/**
	 * Get the Path object for the given source and destination address.
	 * 
	 * @param sourceAddress
	 * @param destAddress
	 * @return Path for source and destination address. If not found then return
	 *         null.
	 * @throws IntegerException
	 */
	Path getPathBySourceDestAddress(Address sourceAddress, Address destAddress)
			throws IntegerException;

	/**
	 * @param sourceAddress
	 * @param destAddress
	 * @return
	 * @throws IntegerException
	 */
	InterDeviceLink[] getInterDeviceLinksBySourceDestAddress(Address sourceAddress,
			Address destAddress) throws IntegerException;

}
