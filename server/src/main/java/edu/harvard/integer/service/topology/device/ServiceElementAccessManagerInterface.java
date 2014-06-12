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

package edu.harvard.integer.service.topology.device;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 *
 */
public interface ServiceElementAccessManagerInterface extends BaseManagerInterface {

	/**
	 * Add or update a service element. If the service element does not exist in the database. Then 
	 * a new service element will be created. If the service element exists in then the service element
	 * will be updated.
	 * 
	 * @param serviceElement
	 * @return Updated ServiceElement
	 * @throws IntegerException
	 */
	ServiceElement updateServiceElement(ServiceElement serviceElement)
			throws IntegerException;

	/**
	 * Get the list of all service elements.
	 * @return ServiceElement[] of all ServiceElements 
	 * @throws IntegerException
	 */
	ServiceElement[] getAllServiceElements() throws IntegerException;

	/**
	 * Get a list of the top level (Device) ServiceElements.
	 * 
	 *  @return ServiceElement[] of the top level service elements that have been
	 * discovered. An empty list will be returned when there is no data in the database.
	 * @throws IntegerException will be thrown if any error occurs while retrieving the data. 
	 */
	ServiceElement[] getTopLevelServiceElements() throws IntegerException;

	/**
	 * Find the ServiceElements that have the given parent ID.
	 * 
	 * @param parentId
	 * @return
	 * @throws IntegerException
	 */
	ServiceElement[] getServiceElementByParentId(ID parentId)
			throws IntegerException;
	
	
	/**
	 * Get a service element given by an IP Address which associates with the service element.
	 */
	ServiceElement getServiceElementByIpAddress( String ipAddress ) throws IntegerException;
	
	

	/**
	 * Delete the ServiceElements with the given ID's
	 * 
	 * @param ids
	 * @throws IntegerException
	 */
	void deleteServiceElememts(ID[] ids) throws IntegerException;

	public DeviceDetails getDeviceDetails(ID serviceElementId) throws IntegerException;

	/**
	 * Return all top level service elements that match the given selection. 
	 * @param selection
	 * @return
	 * @throws IntegerException
	 */
	ServiceElement[] getTopLevelServiceElementBySelection(Selection selection)
			throws IntegerException;

	/**
	 * Return all top level service elements that match the given selection id. 
	 * @param selection
	 * @return
	 * @throws IntegerException
	 */
	ServiceElement[] getTopLevelServiceElementBySelection(ID selectionId)
			throws IntegerException;
}
