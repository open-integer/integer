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

import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

/**
 * @author David Taylor
 * 
 * 
 *         This template is used for a specific vendor in order to determine
 *         what the model, firmware and software revision are of an instance of
 *         something from that vendor. There are two ways to collect this
 *         information. If model, firmware and software revision OIDs have been
 *         populated in an instance of this class, then the discovery system
 *         retrieves that information. If the firmware, software and model
 *         information are not populated in an instance of this class, then the
 *         parseStringOid must be present. The discovery system will use that
 *         information to retrieve an SNMP Object with structured information.
 *         It will use the parseSting attribute that has been configured to
 *         parse out the available model, firmware and software information.
 *         Then it can query the database for the correct
 *         SnmpVendorContainmentSelector object instance. In the rare case where
 *         one of the three pieces of information is in an OID, then that OID
 *         (e..g, firmware) would be populated, the other two would be blank and
 *         if there was an object with a parseString that could get the other
 *         two, then the parseStringOID would be populated with the OID of the
 *         object to get and the parseString used to get the other two
 *         attributes.
 * 
 *         What is in the parseString is the information needed to figure out
 *         how to parse the string.
 */
public abstract class VendorDiscoveryTemplate<T extends ServiceElementManagementObject>
		extends BaseEntity {

	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Description of this instance.
	 */
	private String description = null;

	/**
	 * The ServiceElementManagementObject used to determine the model of the ServiceElement being discovered. 
	 */
	private T model = null;

	/**
	 * The ServiceElementManagementObject used to determine the firmware of the ServiceElement being discovered.
	 */
	private T firmware = null;

	/**
	 * The ServiceElementManagementObject used to determine the softwareRevision of the ServiceElement being discovered.
	 */
	private T softwareRevision = null;

	private DiscoveryParseString parseString = null;

	/**
	 * The name of the vendor associated with this SnmpVendorDiscoveryTemplate.
	 */
	private String vendor = null;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the model
	 */
	public T getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(T model) {
		this.model = model;
	}

	/**
	 * @return the firmware
	 */
	public T getFirmware() {
		return firmware;
	}

	/**
	 * @param firmware
	 *            the firmware to set
	 */
	public void setFirmware(T firmware) {
		this.firmware = firmware;
	}

	/**
	 * @return the softwareRevision
	 */
	public T getSoftwareRevision() {
		return softwareRevision;
	}

	/**
	 * @param softwareRevision
	 *            the softwareRevision to set
	 */
	public void setSoftwareRevision(T softwareRevision) {
		this.softwareRevision = softwareRevision;
	}

	/**
	 * @return the parseString
	 */
	public DiscoveryParseString getParseString() {
		return parseString;
	}

	/**
	 * @param parseString
	 *            the parseString to set
	 */
	public void setParseString(DiscoveryParseString parseString) {
		this.parseString = parseString;
	}

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
}
