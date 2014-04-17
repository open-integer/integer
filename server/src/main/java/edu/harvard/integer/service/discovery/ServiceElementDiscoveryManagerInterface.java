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

package edu.harvard.integer.service.discovery;

import java.util.List;

import javax.ejb.Local;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * 
 */
@Local
public interface ServiceElementDiscoveryManagerInterface extends
		BaseManagerInterface {

	/**
	 * Get the SnmpVendorDiscoveryTemplate for the given vendorId. To get the
	 * vendorId use the getVendorIdentifier()
	 */
	SnmpVendorDiscoveryTemplate getSnmpVendorDiscoveryTemplateByVendor(
			ID vendorId) throws IntegerException;

	/**
	 * Get the SnmpVendorDiscoveryTemplate for the given vendorId. To get the
	 * vendorId use the getVendorIdentifier()
	 */
	SnmpVendorDiscoveryTemplate getSnmpVendorDiscoveryTemplateByVendor(
			Long vendorId) throws IntegerException;

	/**
	 * Get the SnmpContainment hierarchy for the VendorContainmentSelector
	 * 
	 * @param selector
	 *            . The VendorContainmanetSelector to the the SnmpContainment
	 *            for
	 * @return SnmpContianment object that describes the hierarchy for this
	 *         device type.
	 * @throws IntegerException
	 */
	SnmpContainment getSnmpContainment(VendorContainmentSelector selector)
			throws IntegerException;

	/**
	 * Gets the top level polls. On IP network topology discovery, the list
	 * should be a list of SNMP objects in the system group.
	 * 
	 * @return the top level polls for discovery.
	 * 
	 */
	public List<SNMP> getToplLevelOIDs();

	/**
	 * Insert or update the SnmpVendorTemplate.
	 * 
	 * @param template
	 *            . Template to be updated.
	 * @return The updated SnmpVendorTemplate. This has the identifier filled in
	 *         if this object was just created.
	 * 
	 * @throws IntegerException
	 */
	public SnmpVendorDiscoveryTemplate updateSnmpVendorDiscoveryTemplate(
			SnmpVendorDiscoveryTemplate template) throws IntegerException;

	/**
	 * Get all the SnmpVendorTemlates in the system.
	 * 
	 * @return
	 * @throws IntegerException
	 */
	SnmpVendorDiscoveryTemplate[] getAllSnmpVendorDiscoveryTemplates()
			throws IntegerException;

	/**
	 * Find the vendor based on the vendor ID. The vendor ID is the sysObjectID
	 * for the device. Note: The value of the first octet only is used for the
	 * vendor ID. ex. A snmpwalk with the following value
	 * SNMPv2-MIB::sysObjectID.0 = OID: SNMPv2-SMI::enterprises.9.1.658 would
	 * use the "9" as the vendor ID. The return from this would be a "Cisco"
	 * VendorIdentifier.
	 * 
	 * @param vendorId
	 * @return
	 * @throws IntegerException
	 */
	VendorIdentifier getVendorIdentifier(Long vendorId) throws IntegerException;

	/**
	 * 
	 * @param vendorId
	 * @param name
	 * @return
	 * @throws IntegerException
	 */
	VendorIdentifier updateVendorIdentifier(VendorIdentifier vendorIdentifier)
			throws IntegerException;

	/**
	 * Find the service element type by the givne ID.
	 * 
	 * @param serviceElementTypeId
	 * @return
	 * @throws IntegerException
	 */
	ServiceElementType getServiceElementTypeById(ID serviceElementTypeId)
			throws IntegerException;

	/**
	 * @param id
	 * @param seed
	 * @return
	 * @throws IntegerException
	 */
	NetworkDiscovery<ServiceElement> startDiscovery(DiscoveryId id,
			IpDiscoverySeed seed) throws IntegerException;
}
