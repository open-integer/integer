/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.common.topology;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import edu.harvard.integer.common.BaseEntity;

@Entity
public abstract class ServiceElementManagementObject extends BaseEntity {

	/*
	 * A list of the ServiceElementTypes that support this management object.
	 */
	@OneToMany
	public List<ServiceElementType> serviceElementTypes = null;

	/*
	 * While it is possible for a ServiceElementManagementObject to be useful
	 * for configuration and security or for potentially several different FCAPS
	 * and service level management functions, there must be a separate instance
	 * of this object for different access methods since the syntax and all the
	 * other details (including security parameters for accessing the device)
	 * will be different.
	 */
	public AccessMethod accessMethod = null;

	/*
	 * The namespace of the scopeName implemented in the object that realizes
	 * this interface. For example Cisco CLI, SNMP, SNMP-Private vendor, etc.
	 */
	public String namespace = null;


	/*
	 * A short name that can be used by the human interface to identify this
	 * management object.
	 */
	public String displayName = null;

	
	/**
	 * @return the serviceElementTypes
	 */
	public List<ServiceElementType> getServiceElementTypes() {
		return serviceElementTypes;
	}

	/**
	 * @param serviceElementTypes
	 *            the serviceElementTypes to set
	 */
	public void setServiceElementTypes(
			List<ServiceElementType> serviceElementTypes) {
		this.serviceElementTypes = serviceElementTypes;
	}

	/**
	 * @return the accessMethod
	 */
	public AccessMethod getAccessMethod() {
		return accessMethod;
	}

	/**
	 * @param accessMethod
	 *            the accessMethod to set
	 */
	public void setAccessMethod(AccessMethod accessMethod) {
		this.accessMethod = accessMethod;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace
	 *            the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

//	/**
//	 * @return the capability
//	 */
//	public Capability getCapability() {
//		return capability;
//	}
//
//	/**
//	 * @param capability
//	 *            the capability to set
//	 */
//	public void setCapability(Capability capability) {
//		this.capability = capability;
//	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
