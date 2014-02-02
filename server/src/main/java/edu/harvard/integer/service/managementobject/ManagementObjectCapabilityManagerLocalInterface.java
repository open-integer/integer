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

import javax.ejb.Local;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.AccessMethod;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Mechanism;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

/**
 * @author David Taylor
 *
 */
@Local
public interface ManagementObjectCapabilityManagerLocalInterface {

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
	 */
	public abstract Capability addManagementObjectsToCapability(
			Capability capability,
			List<ServiceElementManagementObject> managementObjects);

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
	 */
	public abstract List<Capability> getCapabilities();

	/**
	 * Returns a list of mechanisms that are associated with specific
	 * capabilities.
	 * 
	 * @return List<Mechanisim>. The Mechanisms for the capabilities.
	 */
	public abstract List<Mechanism> getMechanisms(List<Capability> capabilites);

	/**
	 * Associates a mechanism with a specific capability.
	 * 
	 * @param capability
	 * @param mechanism
	 * @return Mechanism. The updated mechanism.
	 */
	public abstract Mechanism setMechamism(Capability capability,
			Mechanism mechanism);

	/**
	 * Associates a specific ServiceElementManagementObject (or objects) with a
	 * specific capability.
	 */
	public abstract Capability setServiceElementManagementObjects(
			Capability capability,
			List<ServiceElementManagementObject> managementObjects);

	/**
	 * Get a list of all ServiceElementManagementObejcts.
	 * 
	 * @return
	 */
	public abstract List<ServiceElementManagementObject> getAllServiceElementManagementObjects();

	/**
	 * 
	 * @return List of ServiceElementManagementObjects that have not been
	 *         assigned to a capability.
	 */
	public abstract List<ServiceElementManagementObject> getUnassignedServiceElementManagementObjects();

}