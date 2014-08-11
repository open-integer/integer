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

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.topology.ServiceElementAssociationType;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementAssociationTypeDAO;

/**
 * The service element discovery manager is responsible for both initial
 * discovery of network elements and subsequent passes over the same elements.
 * It is not responsible for computing network topology though it collects
 * information that is used by that service.
 * 
 * 
 * The ServiceElementDiscoveryManager is started by the Service and there is one
 * instance created for each subnet of the ServiceElementDiscoveryManager. When
 * it is called the DiscoveryService strips out unneeded information and it only
 * creates a ServiceElementDiscoverySeed. That will contain, the subnet, list of
 * found hosts, and serviceElementTypeExclusions.
 * 
 * @author David Taylor
 * 
 */
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
	 * Get the SnmpContainment hierarchy for the VendorContainmentSelector.
	 * <p> 
	 * NOTE: this will return the first match.
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
	 * Get all the SnmpContainment's hierarchy for the VendorContainmentSelector.
	 * 
	 * @param selector
	 *            . The VendorContainmanetSelector to the the SnmpContainment
	 *            for
	 * @return SnmpContianment object that describes the hierarchy for this
	 *         device type.
	 * @throws IntegerException
	 */
	public SnmpContainment[] getSnmpContainments(
			VendorContainmentSelector selector) throws IntegerException;
	
	/**
	 * Get all the SnmpContainments that are in the database.
	 * @return SnmpContainment[] of all SnmpContainments.
	 * 
	 * @throws IntegerException
	 */
	SnmpContainment[] getAllSnmpContainments() throws IntegerException;
	
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
	VendorIdentifier getVendorIdentifier(String vendorOid)
			throws IntegerException;

	/**
	 * Update/save the given vendor identifier in the database. If the vendor
	 * identifier does not exist then a new entery will be created in the
	 * database. The identifier for this object will be valid in the vendor
	 * identifier returned.
	 * 
	 * @param vendorId
	 *            . Object to be saved / updated in the database.
	 * @return VendorIdentifier that has been saved in the database. The
	 *         identifier is valid on the returned object.
	 * 
	 * @throws IntegerException
	 */
	VendorIdentifier updateVendorIdentifier(VendorIdentifier vendorIdentifier)
			throws IntegerException;

	/**
	 * Find the service element type by the given ID.
	 * 
	 * @param serviceElementTypeId
	 * @return ServiceElment with the given ID or null if not found.
	 * @throws IntegerException
	 */
	ServiceElementType getServiceElementTypeById(ID serviceElementTypeId)
			throws IntegerException;

	/**
	 * Find the service element type by name.
	 * 
	 * @param serviceElementTypeId
	 * @return ServiceElment with the given name or null if not found.
	 * @throws IntegerException
	 */
	ServiceElementType getServiceElementTypeByName(String name)
			throws IntegerException;
	
	
	/**
	 * Find Service Element Association Type by id.
	 * 
	 * @param id
	 * @return
	 * @throws IntegerException
	 */
	ServiceElementAssociationType getServiceElementAssociationTypeById( ID id )
			throws IntegerException;
	
	

	/**
	 * This method is called by the DiscoveryService to start a discovery on the
	 * devices described by the IpDiscoverySeed.
	 * 
	 * @param id
	 *            . DiscoverId that identifies this discovery. This ID is to be
	 *            used for calls to notify the DiscoveryService that the
	 *            discovery has completed or for any error that occurs during
	 *            the discovery.
	 * 
	 * @param seed
	 *            . IpDescoverySeed that describes the what the NetworkDiscovery
	 *            process should do.
	 * @return NetworkDiscovery worker that is running the discovery.
	 * @throws IntegerException
	 */
	NetworkDiscovery startDiscovery(DiscoveryId id, IpDiscoverySeed seed)
			throws IntegerException;

	/**
	 * Find the ServiceElements for the given category and vendor.
	 * 
	 * @return a list of ServiceElements that match the given category and
	 *         vendor.
	 * @throws IntegerException
	 */
	ServiceElementType[] getServiceElementTypesByCategoryAndVendor(
			Category catetory, String vendorType)
			throws IntegerException;

	/**
	 * Find the ServiceElementTypes for the given vendorSubType and vendor.
	 * 
	 * @return a list of ServiceElements that match the given subtype and
	 *         vendor.
	 * @throws IntegerException
	 */
	ServiceElementType[] getServiceElementTypesBySubtypeAndVendor(
			String subtype, String vendorType) throws IntegerException;

	/**
	 * Update or save the vendor containment selector. The object returned from
	 * this call will have the identifier filled in.
	 * 
	 * @param vendorContainmentSelector
	 *            . The VendorContainmentSelector that is to be saved in the
	 *            database.
	 * @return VendorContainmentSelector with the identifier filled in.
	 * 
	 * @throws IntegerException
	 */
	VendorContainmentSelector updateVendorContainmentSelector(
			VendorContainmentSelector selector) throws IntegerException;

	/**
	 * @return
	 * @throws IntegerException
	 */
	VendorContainmentSelector[] getAllVendorContainmentSelectors()
			throws IntegerException;

	/**
	 * Get all the vendor containment selectors in the database that match the
	 * values in the given vendor containment selector.
	 * 
	 * @param vendorContaimentSelector
	 *            . This object has the values to use for selecting the
	 *            VendorContainmentSelectors. This is the search criteria for
	 *            the query.s
	 * @return List of VendorContainmentSelectors that match the
	 * @throws IntegerException
	 */
	VendorContainmentSelector[] getVendorContainmentSelector(
			VendorContainmentSelector selector) throws IntegerException;

	/**
	 * Get the vendor containment selector from the database that is identified
	 * by the given ID.
	 * 
	 * @param id
	 *            . ID of the vendor containment selector to return.
	 * @return the VendorContainmentSelector or null if not found in the
	 *         database.
	 * 
	 * @throws IntegerException
	 */
	VendorContainmentSelector getVendorContainmentSelectorById(ID id)
			throws IntegerException;

	/**
	 * Delete the VendorContainmentSelector from the database that is specified
	 * by the given ID.
	 * 
	 * @param id
	 *            . ID of the VendorContainmentSelector to delete.
	 * 
	 * @throws IntegerException
	 */
	void deleteVendorContianmentSelector(ID id) throws IntegerException;

	/**
	 * Get the list of OIDs that are used to retrieve the Entity MIB Physical
	 * table. 
	 * 
	 * @return List of SNMP OIDs used to discover the entityPhysicalTable.
	 */
	List<SNMP> getEntityPhysicalTableOIDs();

	/**
	 * Find the VendorIdentifiers that are in the subtree specified by the
	 * rootOid.
	 * 
	 * @param rootOid
	 * @return List of VendorIdentifier's that are in the given subTree.
	 * 
	 * @throws IntegerException
	 */
	List<VendorIdentifier> findVendorSubTree(String rootOid)
			throws IntegerException;

	/**
	 * Find the VendorIdentifiers that are in the subtree specified by the
	 * rootName.
	 * 
	 * @param rootName
	 * @return List of VendorIdentifier's that are in the given subTree.
	 * 
	 * @throws IntegerException
	 */
	List<VendorIdentifier> findVendorNameSubTree(String rootOid)
			throws IntegerException;

	/**
	 * Find the VendorIdentifier with the given subtype name.
	 * 
	 * @param vendorSubTypeName
	 * @return
	 * @throws IntegerException
	 */
	public VendorIdentifier getVenderIdentiferBySubTypeName(
			String vendorSubTypeName) throws IntegerException;

	/**
	 * Update the SnmpContainment in the database. If this is called with a new
	 * unsaved instance then a new entry will be added to the SnmpContainment
	 * table and the identifier will be set on the SnmpContainment object that
	 * is returned.
	 * 
	 * @param snmpContainment
	 *            . Object to save
	 * @return saved SnmpContainmet object.
	 * 
	 * @throws IntegerException
	 */
	public SnmpContainment updateSnmpContainment(SnmpContainment snmpContainment)
			throws IntegerException;

	/**
	 * Update/save the ServiceElementAssociationType in the database.
	 * 
	 * @param serviceElementAssociationType
	 * @return The updated ServiceElementAssociationType
	 * @throws IntegerException
	 */
	ServiceElementAssociationType updateServiceElementAssociationType(
			ServiceElementAssociationType serviceElementAssociationType)
			throws IntegerException;

}
