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

package edu.harvard.integer.service.persistance;

import javax.ejb.Local;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.persistence.DataPreLoadFile;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.persistance.dao.discovery.DiscoveryRuleDAO;
import edu.harvard.integer.service.persistance.dao.discovery.IpTopologySeedDAO;
import edu.harvard.integer.service.persistance.dao.discovery.SnmpAssociationDAO;
import edu.harvard.integer.service.persistance.dao.discovery.VendorIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedManagerDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedServiceDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.IntegerServerDAO;
import edu.harvard.integer.service.persistance.dao.event.DiscoveryCompleteEventDAO;
import edu.harvard.integer.service.persistance.dao.event.EventDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ApplicabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ManagementObjectValueDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.SnmpSyntaxDAO;
import edu.harvard.integer.service.persistance.dao.persistance.DataPreLoadFileDAO;
import edu.harvard.integer.service.persistance.dao.security.DirectUserLoginDAO;
import edu.harvard.integer.service.persistance.dao.selection.FilterDAO;
import edu.harvard.integer.service.persistance.dao.selection.FilterNodeDAO;
import edu.harvard.integer.service.persistance.dao.selection.LayerDAO;
import edu.harvard.integer.service.persistance.dao.selection.SelectionDAO;
import edu.harvard.integer.service.persistance.dao.selection.ViewDAO;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPIndexDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleHistoryDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SnmpGlobalReadCredentialDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SnmpV2CredentialDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SnmpV3CredentialDAO;
import edu.harvard.integer.service.persistance.dao.technology.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.technology.ServiceDAO;
import edu.harvard.integer.service.persistance.dao.technology.TechnologyDAO;
import edu.harvard.integer.service.persistance.dao.topology.CategoryDAO;
import edu.harvard.integer.service.persistance.dao.topology.EnvironmentLevelDAO;
import edu.harvard.integer.service.persistance.dao.topology.InterDeviceLinkDAO;
import edu.harvard.integer.service.persistance.dao.topology.InterNetworkLinkDAO;
import edu.harvard.integer.service.persistance.dao.topology.NetworkDAO;
import edu.harvard.integer.service.persistance.dao.topology.PathDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementAssociationDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementAssociationTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementProtocolInstanceIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.SignatureDAO;
import edu.harvard.integer.service.persistance.dao.topology.SignatureValueOperatorDAO;
import edu.harvard.integer.service.persistance.dao.topology.SnmpServiceElementTypeOverrideDAO;
import edu.harvard.integer.service.persistance.dao.topology.TopologyElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseStringDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpLevelOIDDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpRelationshipDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpServiceElementTypeDiscriminatorValueDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpVendorDiscoveryTemplateDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.VendorContainmentSelectorDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.VendorSignatureDAO;
import edu.harvard.integer.service.persistance.dao.user.AccessPolicyDAO;
import edu.harvard.integer.service.persistance.dao.user.AuthInfoDAO;
import edu.harvard.integer.service.persistance.dao.user.ContactDAO;
import edu.harvard.integer.service.persistance.dao.user.LocationDAO;
import edu.harvard.integer.service.persistance.dao.user.OrganizationDAO;
import edu.harvard.integer.service.persistance.dao.user.RoleDAO;
import edu.harvard.integer.service.persistance.dao.user.UserDAO;

/**
 * The PersistenceManager is used to get the DAO that will be used to hide the
 * interaction with the database. All objects saved in the database extend
 * BaseEntity. All DAO's extend BaseDAO. The BaseDAO is contains all the common
 * access to the database as well as the all calls to the database. All access
 * to the database is done in a DAO.
 * 
 * Ex. 
 * @Inject
 * PersistenceManagerInterface persistenceManager;
 *
 * UserDAO dao = persistenceManager.getUserDAO();
 * user = dao.update(user); // Update one user. 
 * 	                        // NOTE: the database ID is valid after this call.
 * 
 * User[] users = dao.findAll(); // Get a list of all users in the system.
 * 
 * @author David Taylor
 * 
 */

@Local
public interface PersistenceManagerInterface extends BaseManagerInterface {

	/**
	 * @return DAO for the SNMPModule.
	 */
	SNMPModuleDAO getSNMPModuleDAO();

	/**
	 * @return DAO for the SNMP object
	 */
	SNMPDAO getSNMPDAO();

	/**
	 * @return DAO for The SNMPIndex
	 */
	SNMPIndexDAO getSNMPIndexDAO();

	/**
	 * @return DAO for MIBInfo
	 */
	MIBInfoDAO getMIBInfoDAO();

	/**
	 * @return DAO for User.
	 */
	UserDAO getUserDAO();

	/**
	 * @return
	 */
	ServiceElementTypeDAO getServiceElementTypeDAO();

	/**
	 * @return
	 */
	CapabilityDAO getCapabilityDAO();

	/**
	 * @return
	 */
	SNMPModuleHistoryDAO getSNMPModuleHistoryDAO();

	/**
	 * @return
	 */
	ServiceElementManagementObjectDAO getServiceElementManagementObjectDAO();

	/**
	 * @return
	 */
	ServiceElementDAO getServiceElementDAO();

	/**
	 * @return
	 */
	ServiceElementProtocolInstanceIdentifierDAO getServiceElementProtocolInstanceIdentifierDAO();

	/**
	 * @return
	 */
	AccessPolicyDAO getAccessPolicyDAO();

	/**
	 * @return
	 */
	AuthInfoDAO getAuthInfoDAO();

	/**
	 * @return
	 */
	LocationDAO getLocationDAO();

	/**
	 * @return
	 */
	RoleDAO getRoleDAO();

	/**
	 * @return
	 */
	OrganizationDAO getOrganizationDAO();

	/**
	 * @return
	 */
	ContactDAO getContactDAO();

	/**
	 * @return
	 */
	MechanismDAO getMechanismDAO();

	/**
	 * @return
	 */
	DirectUserLoginDAO getDirectUserLoginDAO();

	/**
	 * @return
	 */
	SnmpVendorDiscoveryTemplateDAO getSnmpVendorDiscoveryTemplateDAO();

	/**
	 * @return
	 */
	VendorContainmentSelectorDAO getVendorContainmentSelectorDAO();

	/**
	 * @return
	 */
	SnmpContainmentDAO getSnmpContainmentDAO();

	/**
	 * @return
	 */
	DiscoveryParseStringDAO getDiscoveryParseStringDAO();

	/**
	 * @return
	 */
	DiscoveryParseElementDAO getDiscoveryParseElementDAO();

	/**
	 * @return
	 */
	VendorIdentifierDAO getVendorIdentifierDAO();

	/**
	 * @return
	 */
	ManagementObjectValueDAO getManagementObjectValueDAO();

	/**
	 * @return
	 */
	SnmpServiceElementTypeDiscriminatorValueDAO getSnmpServiceElementTypeDiscriminatorValueDAO();

	/**
	 * @return
	 */
	ApplicabilityDAO getApplicabilityDAO();

	/**
	 * @return
	 */
	SnmpServiceElementTypeOverrideDAO getSnmpServiceElementTypeOverrideDAO();

	/**
	 * @return
	 */
	DistributedManagerDAO getDistributedManagerDAO();

	/**
	 * @return
	 */
	DistributedServiceDAO getDistributedServiceDAO();

	/**
	 * @return
	 */
	SnmpSyntaxDAO getTextualConventionDAO();

	/**
	 * @return
	 */
	IntegerServerDAO getIntegerServerDAO();

	/**
	 * @return
	 */
	SelectionDAO getSelectionDAO();

	/**
	 * @return
	 */
	FilterDAO getFilterDAO();

	/**
	 * @return
	 */
	ViewDAO getViewDAO();

	/**
	 * @return
	 */
	EventDAO getEventDAO();

	/**
	 * @return
	 */
	DiscoveryCompleteEventDAO getDiscoveryCompleteEventDAO();

	/**
	 * @return
	 */
	LayerDAO getLayerDAO();

	/**
	 * @return
	 */
	ServiceDAO getServiceDAO();

	/**
	 * @return
	 */
	TechnologyDAO getTechnologyDAO();

	/**
	 * @return
	 */
	FilterNodeDAO getFilterNodeDAO();

	/**
	 * @return
	 */
	DataPreLoadFileDAO getDataPreLoadFileDAO();

	/**
	 * @return
	 */
	SnmpLevelOIDDAO getSnmpLevelOIDDAO();

	/**
	 * @return
	 * @throws IntegerException
	 */
	DataPreLoadFile[] getAllPreloads() throws IntegerException;

	/**
	 * @return
	 */
	SnmpRelationshipDAO getSnmpSnmpRelationshipDAO();

	/**
	 * @return
	 */
	SignatureDAO getSignatureDAO();

	/**
	 * @return
	 */
	SignatureValueOperatorDAO getSignatureValueOperatorDAO();

	/**
	 * @return
	 */
	CategoryDAO getCategoryDAO();

	/**
	 * @return
	 */
	NetworkDAO getNetworkDAO();

	/**
	 * @return
	 */
	InterDeviceLinkDAO getInterDeviceLinkDAO();

	/**
	 * @return
	 */
	TopologyElementDAO getTopologyElementDAO();

	/**
	 * @return
	 */
	PathDAO getPathDAO();

	/**
	 * @return
	 */
	InterNetworkLinkDAO getInterNetworkLinkDAO();

	/**
	 * @return
	 */
	VendorSignatureDAO getVendorSignatureDAO();

	/**
	 * @return
	 */
	DiscoveryRuleDAO getDiscoveryRuleDAO();

	/**
	 * @return
	 */
	IpTopologySeedDAO getIpTopologySeedDAO();

	/**
	 * @return
	 */
	SnmpV2CredentialDAO getSnmpV2cCredentailDAO();

	/**
	 * @return
	 */
	SnmpV3CredentialDAO getSnmpV3CredentailDAO();

	/**
	 * @return SnmpGlobalReadCredentialDAO 
	 * @return
	 */
	SnmpGlobalReadCredentialDAO getSnmpGlobalReadCredentialDAO();

	/**
	 * @return
	 */
	ServiceElementAssociationDAO getServiceElementAssociationDAO();

	/**
	 * @return
	 */
	ServiceElementAssociationTypeDAO getServiceElementAssociationTypeDAO();

	/**
	 * @return
	 */
	SnmpAssociationDAO getSnmpAssociationDAO();

	/**
	 * @return
	 */
	EnvironmentLevelDAO getEnvironmentLevelDAO();
}
