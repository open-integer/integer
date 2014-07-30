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
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SnmpServiceElementTypeOverride;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * The ManagementObjectCapablityManager is used to add/delete/modify the
 * Capabilities and management objects.
 * 
 * @author David Taylor
 * 
 */
public interface ManagementObjectCapabilityManagerInterface extends
		BaseManagerInterface {

	/**
	 * Add/define a new capability for the system. After the capability has been
	 * saved the capability can be added to a mechanism.
	 * 
	 * @param capability
	 *            . New capability to add.
	 * @return Capability. The newly created Capability. This will have the
	 *         identifier filled in.
	 */
	public Capability addCapability(Capability capability);

	/**
	 * Associates a capability with one or more management objects. The given
	 * list of management objects will be attached to the capability. Then the
	 * capability will be saved into the database.
	 * 
	 * @param capability
	 * @param managementObjects
	 * @return capability. The updated capability with the given list of
	 *         management objects associated.
	 * @throws IntegerException
	 */
	public Capability addManagementObjectsToCapability(Capability capability,
			List<ServiceElementManagementObject> managementObjects)
			throws IntegerException;

	/**
	 * Retrieves access methods that have been configured to support a specific
	 * capability.
	 * 
	 * @return List<AccessMethod> that are valid for the given capability.
	 */
	public List<AccessMethod> getAccessMethods(Capability capability);

	/**
	 * Retrieves access methods that have been configured to support a specific
	 * capability. List<AccessMethod> that are valid for the given capability.
	 * This returns the same values as the public List<AccessMethod>
	 * getAccessMethods(Capability capability); The difference is the ID that
	 * specifies a capability instead of the object.
	 * 
	 * @return List<AccessMethod> that are valid for the given capability.
	 */
	public List<AccessMethod> getAccessMethods(ID capabilityId);

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
	public List<ServiceElementManagementObject> getAssociatedServiceElementManagementObjects(
			Capability capability);

	/**
	 * Capabilities may be returned for each mechanism.
	 * 
	 * @return List<Capability>. Capabilities for this mechanism.
	 * @throws IntegerException
	 */
	public List<Capability> getCapabilities() throws IntegerException;

	/**
	 * Get the list of ServiceElementManagementObjects that implement the
	 * capability identified by the capability id.
	 * 
	 * @param ID
	 *            . The ID of capability that the management objects are for.
	 */
	public List<ServiceElementManagementObject> getManagemntObjectsForCapability(
			ID id) throws IntegerException;

	/**
	 * Update the serviceElementType. The identifier will be set on the
	 * instance returned
	 * 
	 * @param serviceElementType. The service element type to be saved
	 * @return the saved service elemeent type that has been saved in
	 * the database. The identifier is set on the object returned.
	 * @throws IntegerException
	 */
	ServiceElementType updateServiceElementType(
			ServiceElementType serviceElementType) throws IntegerException;

	/**
	 * Delete the service element type.
	 * @param serviceElementType The service element type to be deleted
	 * 
	 * @throws IntegerException
	 */
	void deleteServiceElementType(ServiceElementType serviceElementType)
			throws IntegerException;

	/**
	 * Get all the service element types that are in the database.
	 * @return List of all service element types in the database.
	 * @throws IntegerException
	 */
	ServiceElementType[] getAllServiceElementTypes() throws IntegerException;

	/**
	 * Delete the service element type.
	 * @param ID of the serviceElementType to be deleted.
	 * @throws IntegerException
	 */
	void deleteServiceElementType(ID serviceElementTypeId)
			throws IntegerException;

	/**
	 * Update or save the SnmpContainment. The returned SnmpContainment will
	 * have the identifier filled in.
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
	 * Update/save the applicability object
	 * 
	 * @param applicabilty. The applicability object to be saved/updated
	 * @return The updated applicability
	 * @throws IntegerException
	 */
	Applicability updateApplicability(Applicability applicabilty)
			throws IntegerException;

	/**
	 * Get the list of all applicability objects in the database.
	 * @return List of applicatablity objects found in the database.
	 * @throws IntegerException
	 */
	Applicability[] getAllApplicabilities() throws IntegerException;

	/**
	 * Get the Applicability object identified by the ID.
	 * @param id of the applicability object to be retrieved from the database.
	 * @return the Applicability object for the given ID.
	 * @throws IntegerException
	 */
	Applicability getApplicabilityById(ID id) throws IntegerException;

	/**
	 * Delete the Applicability object identified by the ID.
	 * @param id of the Applicability object to delete.
	 * @throws IntegerException
	 */
	void deleteApplicability(ID id) throws IntegerException;

	/**
	 * update the service element type overide object in the database.
	 * @param override. SnmpServiceElementTypeOverride to be updated.
	 * @return updated SnmpServiceElementTypeOverride
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride updateSnmpServiceElementTypeOverride(
			SnmpServiceElementTypeOverride override) throws IntegerException;

	/**
	 * Get all the SnmpServiceElementTypeOverride in the database.
	 * @return List of SnmpServiceElementTypeOverride found in the database.
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride[] getAllSnmpServiceElementTypeOverride()
			throws IntegerException;

	/**
	 * Get SnmpServiceElementTypeOverride identified by the ID
	 * @param id. ID of the SnmpServiceElementTypeOverride to get.
	 * @return SnmpServiceElementTypeOverride 
	 * @throws IntegerException
	 */
	SnmpServiceElementTypeOverride getSnmpServiceElementTypeOverrideById(ID id)
			throws IntegerException;

	/**
	 * delete the SnmpServiceElementTypeOverride identified by the ID.
	 * 
	 * @param id of the SnmpServiceElementTypeOverride to delete
	 * @throws IntegerException
	 */
	void deleteSnmpServiceElementTypeOverride(ID id) throws IntegerException;

	/**
	 * Delete the ServiceElementManagementObject that is identified by the ID.
	 * 
	 * @param id
	 * @return ServiceElementManagementObject identified by the ID.
	 * @throws IntegerException
	 */
	ServiceElementManagementObject getManagementObjectById(ID id)
			throws IntegerException;

	/**
	 * Update the management object. The identifier will be set on the returned
	 * object
	 * 
	 * @param managementObject
	 * @return ManagementObject with the identifier set if this is a new
	 *         instance.
	 * @throws IntegerException
	 */
	ServiceElementManagementObject updateManagementObject(
			ServiceElementManagementObject managementObject)
			throws IntegerException;

	/**
	 * Delete the ServiceElementManagementObject that is identified by the ID.
	 * 
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
	 * Update the management object value specified. The Identifier will be
	 * valid after this call.
	 * 
	 * @param managementObjectValue
	 * @return
	 * @throws IntegerException
	 */
	ManagementObjectValue<?> updateManagementObjectValue(
			ManagementObjectValue<?> managementObjectValue)
			throws IntegerException;

	/**
	 * Load the management object value specified by the given ID.
	 * 
	 * @param ids
	 * @return
	 * @throws IntegerException
	 */
	ManagementObjectValue<?> getManagementObjectValuesById(ID ids)
			throws IntegerException;

	/**
	 * Find the category for the given category name.
	 * 
	 * @param name. Name of the category to get from the datbase.
	 * @return Category for the given name.
	 * @throws IntegerException
	 */
	Category getCategoryByName(String name) throws IntegerException;

	/**
	 * Update the category. 
	 * @param category
	 * @return
	 * @throws IntegerException
	 */
	Category updateCategory(Category category) throws IntegerException;

	/**
	 * Get the capability with the given name. 
	 * 
	 * @param name. Name of capability to get from the database.
	 * @return Capability found for the given name.
	 * @throws IntegerException
	 */
	Capability getCapabilityByName(String name) throws IntegerException;

}