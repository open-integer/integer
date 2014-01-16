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
