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

import edu.harvard.integer.service.BaseManagerInterface;
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
import edu.harvard.integer.service.persistance.dao.technology.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.technology.ServiceDAO;
import edu.harvard.integer.service.persistance.dao.technology.TechnologyDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementProtocolInstanceIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.SnmpServiceElementTypeOverrideDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseStringDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpLevelOIDDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpServiceElementTypeDiscriminatorValueDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpVendorDiscoveryTemplateDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.VendorContainmentSelectorDAO;
import edu.harvard.integer.service.persistance.dao.user.AccessPolicyDAO;
import edu.harvard.integer.service.persistance.dao.user.AuthInfoDAO;
import edu.harvard.integer.service.persistance.dao.user.ContactDAO;
import edu.harvard.integer.service.persistance.dao.user.LocationDAO;
import edu.harvard.integer.service.persistance.dao.user.OrganizationDAO;
import edu.harvard.integer.service.persistance.dao.user.RoleDAO;
import edu.harvard.integer.service.persistance.dao.user.UserDAO;

/**
 * @author David Taylor
 * 
 */

@Local
public interface PersistenceManagerInterface extends BaseManagerInterface {

	/**
	 * @return
	 */
	SNMPModuleDAO getSNMPModuleDAO();

	/**
	 * @return
	 */
	SNMPDAO getSNMPDAO();

	/**
	 * @return
	 */
	SNMPIndexDAO getSNMPIndexDAO();

	/**
	 * @return
	 */
	MIBInfoDAO getMIBInfoDAO();

	/**
	 * @return
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

}
