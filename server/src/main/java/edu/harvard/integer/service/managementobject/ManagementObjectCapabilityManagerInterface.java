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

package edu.harvard.integer.service.managementobject;

import java.util.List;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Applicability;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SnmpServiceElementTypeOverride;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 *
 */
public interface ManagementObjectCapabilityManagerInterface extends BaseManagerInterface {

	/**
	 * Add/define a new capability for the system.
	 * 
	 * @param capability
	 *            . New capability to add.
	 * @return Capability. The newly created Capability. This will have the
	 *         identifier filled in.
	 */
	public abstract Capability addCapability(Capability capability);

	/**
	 * Associates a capability with one or more management objects.
	 * 
	 * @param capability
	 * @param managementObjects
	 * @return capability. The updated capability.
	 * @throws IntegerException 
	 */
	public abstract Capability addManagementObjectsToCapability(
			Capability capability,
			List<ServiceElementManagementObject> managementObjects) throws IntegerException;

	/**
	 * Retrieves access methods that have been configured to support a specific
	 * capability.
	 * 
	 * @return
	 */
	public abstract List<AccessMethod> getAccessMethods(Capability capability);

	/**
	 * Retrieves access methods that have been configured to support a specific
	 * capability.
	 * 
	 * @return
	 */
	public abstract List<AccessMethod> getAccessMethods(ID capabilityId);

	/**
	 * Retrieves the specific management objects that have been associated with
	 * a specific capability. The parameter for access method allows retrieval
	 * for a particular method.
	 * 
	 * @param Capability
	 *            . The capability to get the ServiceElementManagementObjects
	 *            for.
	 * @return List<ServiceElementManagementObject>. Management Objects for the
	 *         given capability.
	 */
	public abstract List<ServiceElementManagementObject> getAssociatedServiceElementManagementObjects(
			Capability capability);

	/**
	 * Capabilities may be returned for each mechanism.
	 * 
	 * @return List<Capability>. Capabilities for this mechanism.
	 * @throws IntegerException 
	 */
	public abstract List<Capability> getCapabilities() throws IntegerException;


	/**
	 * @param ID. The ID of capability that the management objects are for.
	 */
	public List<ServiceElementManagementObject> getManagemntObjectsForCapability(ID id) throws IntegerException;

	/**
	 * @param serviceElementType
	 * @return
	 * @throws IntegerException
	 */
	ServiceElementType updateServiceElementType(
			ServiceElementType serviceElementType) throws IntegerException;

	/**
	 * @param serviceElementType
	 * @throws IntegerException
	 */
	void deleteServiceElementType(ServiceElementType serviceElementType)
			throws IntegerException;

	/**
	 * @return
	 * @throws IntegerException
	 */
	ServiceElementType[] getAllServiceElementTypes() throws IntegerException;

	/**
	 * @param serviceElementTypeId
	 * @throws IntegerException
	 */
	void deleteServiceElementType(ID serviceElementTypeId)
			throws IntegerException;

	/**
	 * Update or save the SnmpContainment. The returned SnmpContainment
	 * will have the identifier filled in.
	 * 
	 * @param snmpContainment
	 * @return
	 * @throws IntegerException
	 */
	SnmpContainment updateSnmpContainment(SnmpContainment snmpContainment)
			throws IntegerException;

	/**
	 * Get all the SnmpContainment objects in the database.
	 * 
	 * @return
	 * @throws IntegerException
	 */
	SnmpContainment[] getAllSnmpContainments() throws IntegerException;

	/**
	 * find the SnmpContainment with the given ID
	 * 
	 * @param id
	 * @return SnmpContainment 
	 * @throws IntegerException
	 */
	SnmpContainment getSnmpContainmentById(ID id) throws IntegerException;

	/**
	 * @param applicabilty
	 * @return
	 * @throws IntegerException
	 */
	Applicability updateApplicability(Applicability applicabilty)
			throws IntegerException;

	/**
	 * @return
	 * @throws IntegerException
	 */
	Applicability[] getAllApplicabilities() throws IntegerException;

	/**
	 * @param id
	 * @return
	 * @throws IntegerException
	 */
	Applicability getApplicabilityById(ID id) throws IntegerException;

	/**
	 * @param id
	 * @throws IntegerException
	 */
	void deleteApplicability(ID id) throws IntegerException;

	/**
	 * @param override
	 * @return
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride updateSnmpServiceElementTypeOverride(
			SnmpServiceElementTypeOverride override) throws IntegerException;

	/**
	 * @return
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride[] getAllSnmpServiceElementTypeOverride()
			throws IntegerException;

	/**
	 * @param id
	 * @return
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride getSnmpServiceElementTypeOverrideById(ID id)
			throws IntegerException;

	/**
	 * @param id
	 * @throws IntegerException
	 */
	void deleteSnmpServiceElementTypeOverride(ID id) throws IntegerException;

	/**
	 * Get the ServiceElementManagementObject for the given ID.
	 * 
	 * @param id
	 * @return ServiceElementManagementObject identified by the ID.
	 * @throws IntegerException
	 */
	ServiceElementManagementObject getManagementObjectById(ID id)
			throws IntegerException;

	/**
	 * Update the management object. The identifier will be set on the returned object
	 * @param managementObject
	 * @return ManagementObject with the identifier set if this is a new instance.
	 * @throws IntegerException
	 */
	ServiceElementManagementObject updateManagementObject(
			ServiceElementManagementObject managementObject)
			throws IntegerException;

	/**
	 * Delete the ServiceElementManagementObject that is identified by the ID.
	 * @param id
	 * @throws IntegerException
	 */
	void deleteManagementObject(ID id) throws IntegerException;

	/**
	 * Get the List of ManagementObejcts by the ID's
	 * 
	 * @param ids
	 * @return
	 * @throws IntegerException
	 */
	ServiceElementManagementObject[] getManagementObjectsByIds(ID[] ids)
			throws IntegerException;

	/**
	 * Update the management object value specified. The Identifier will be valid after this call.
	 * @param managementObjectValue
	 * @return
	 * @throws IntegerException
	 */
	ManagementObjectValue<?> updateManagementObjectValue(
			ManagementObjectValue<?> managementObjectValue)
			throws IntegerException;

	/**
	 * Load the management object value specified by the given ID.
	 * @param ids
	 * @return
	 * @throws IntegerException
	 */
	ManagementObjectValue<?> getManagementObjectValuesById(ID ids)
			throws IntegerException;

}