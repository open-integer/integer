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

package edu.harvard.integer.common.discovery;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

/**
 * 
 * 
 * This object is created by the user. It tells the system which SnmpContainment
 * instance (in the case of SNMP) to use based on a combination of vendor, model
 * firmware and software revision. Not all of these attributes need be filled
 * in.
 * 
 * <p>The SnmpVendorDiscoveryTemplate tells the system where to look on the systems
 * to get the model, firmware and softwareVersion data.
 * 
 * <p>The containment ID will contain the ID of the specific containment object to
 * use for this specific type of system. Other containment types could use SSH
 * or other protocols.
 * 
 * @author David Taylor
 * 
 * 
 */
@Entity
public class VendorContainmentSelector extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This attribute must always be present. It is based on our lookup from the
	 * SysOID retrieved from the system group.
	 */
	private String vendor = null;

	/**
	 * The model of the system as retrieved by the SnmpVendorDiscoveryTemplate
	 * earlier in the discovery process.
	 */
	private String model = null;

	/**
	 * The firmware version as retrieved from the
	 * ServiceElementDiscoveryManager.
	 */
	private String firmware = null;

	/**
	 * Software version of the device as retrieved during the earlier phase of
	 * discovery.
	 */
	private String softwareVersion = null;

	/**
	 * If the vendor, model, firmware and software version do not map to a
	 * specific VendorContainmentSelector then this value can be used to test
	 * the device for existence of a value. If a value is returned then this
	 * VendorContainmentSelector can be used to discover the device containment.
	 */
	private ServiceElementManagementObject testManagementObject = null;

	/**
	 * The idId of the instances of the hardware containment structure class
	 * that tells the discovery system how to learn the structure of
	 * serviceElements from a particular vendors.
	 * 
	 * These values can be retrieved from the database based on the vendor,
	 * model, firmware and software revision information.
	 * 
	 * There are two ways to collect this information. If model, firmware and
	 * software revision OIDs have been populated in an instance of this class,
	 * then the discovery system retrieves that information.
	 * 
	 * If the firmware, software and model information are not populated in an
	 * instance of this class, then the parseStringOid must be present. The
	 * discovery system will use that information to retrieve an SNMP Object
	 * with structured information. It will use the parseSting attribute that
	 * has been configured to parse out the available model, firmware and
	 * software information. Then it can query the database for the correct
	 * containment object instances.
	 * 
	 * It is possible to create an instance of the VendorContainmentSelector
	 * without all of the fields complete, for example without the firmware or
	 * softwareVersion attributed filled.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "containmentId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "containmentType")),
			@AttributeOverride(name = "name", column = @Column(name = "containmentName")) })
	private ID containmentId = null;

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the firmware
	 */
	public String getFirmware() {
		return firmware;
	}

	/**
	 * @param firmware
	 *            the firmware to set
	 */
	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion
	 *            the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the containmentId
	 */
	public ID getContainmentId() {
		return containmentId;
	}

	/**
	 * @param containmentId
	 *            the containmentId to set
	 */
	public void setContainmentId(ID containmentId) {
		this.containmentId = containmentId;
	}

}
