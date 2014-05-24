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
package edu.harvard.integer.service.persistance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.persistance.dao.discovery.VendorIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedManagerDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedServiceDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.IntegerServerDAO;
import edu.harvard.integer.service.persistance.dao.event.DiscoveryCompleteEventDAO;
import edu.harvard.integer.service.persistance.dao.event.EventDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ApplicabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.ManagementObjectValueDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.MechanismDAO;
import edu.harvard.integer.service.persistance.dao.managementobject.SnmpSyntaxDAO;
import edu.harvard.integer.service.persistance.dao.security.DirectUserLoginDAO;
import edu.harvard.integer.service.persistance.dao.selection.FilterDAO;
import edu.harvard.integer.service.persistance.dao.selection.LayerDAO;
import edu.harvard.integer.service.persistance.dao.selection.SelectionDAO;
import edu.harvard.integer.service.persistance.dao.selection.ViewDAO;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPIndexDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPModuleHistoryDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementManagementObjectDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementProtocolInstanceIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.SnmpServiceElementTypeOverrideDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseElementDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.DiscoveryParseStringDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;
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
@Stateless
public class PersistenceManager extends BaseManager implements PersistenceManagerInterface {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private Logger logger;
		
	
	/**
	 * Get the SNMPModuleDAO. 
	 * 
	 * @return SNMPModuleDAO. 
	 */
	@Override
	public SNMPModuleDAO getSNMPModuleDAO() {
		return new SNMPModuleDAO(em, logger);
	}
	
	/**
	 * Get the SNMPDAO.
	 * 
	 * @return
	 */
	@Override
	public SNMPDAO getSNMPDAO() {
		return new SNMPDAO(em, logger);
	}
	
	/**
	 * Get the SNMPIndexDAO.
	 * 
	 * @return
	 */
	@Override
	public SNMPIndexDAO getSNMPIndexDAO() {
		return new SNMPIndexDAO(em, logger);
	}
	
	/**
	 * Get the MIBInfoDAO.
	 * 
	 * @return
	 */
	@Override
	public MIBInfoDAO getMIBInfoDAO() {
		return new MIBInfoDAO(em, logger);
	}
	
	/**
	 * Get the UserDAO
	 * 
	 * @return
	 */
	@Override
	public UserDAO getUserDAO() {
		return new UserDAO(em, logger);
	}
	
	/**
	 * Get the ServiceElementTypeDAO
	 * 
	 * @return
	 */
	@Override
	public ServiceElementTypeDAO getServiceElementTypeDAO() {
		return new ServiceElementTypeDAO(em, logger);
	}
	
	/**
	 * Get the CapabilityDAO
	 * 
	 * @return
	 */
	@Override
	public CapabilityDAO getCapabilityDAO() {
		return new CapabilityDAO(em, logger);
	}

	/**
	 * Get the SNMPHistoryDAO()
	 * 
	 * @return SNMPHistoryDAO
	 */
	@Override
	public SNMPModuleHistoryDAO getSNMPModuleHistoryDAO() {
		
		return new SNMPModuleHistoryDAO(em, logger);
	}
	
	/**
	 * Get the ServiceElementManagementObjectDAO
	 * 
	 * @return ServiceElementManagementObjectDAO
	 */
	@Override
	public ServiceElementManagementObjectDAO getServiceElementManagementObjectDAO() {
		return new ServiceElementManagementObjectDAO(em, logger);
	}
	
	/**
	 * get the ServiceElementDAO
	 * 
	 * @return ServiceElementDAO. The DAO is initialized with the persistence manager and logger.
	 */
	@Override
	public ServiceElementDAO getServiceElementDAO() {
		return new ServiceElementDAO(em, logger);
	}
	
	/**
	 * Get the ServiceElementProtocolInstanceIdentifierDAO
	 * 
	 * @return ServiceElementProtocolInstanceIdentifierDAO. The DAO is initialized with the persistence manager and logger.
	 */
	@Override
	public ServiceElementProtocolInstanceIdentifierDAO getServiceElementProtocolInstanceIdentifierDAO() {
		return new ServiceElementProtocolInstanceIdentifierDAO(em, logger);
	}
	
	/**
	 * Get the AccessPolicyDAO()
	 * @return AccessPolicyDAO();
	 */
	@Override
	public AccessPolicyDAO getAccessPolicyDAO() {
		return new AccessPolicyDAO(em, logger);
	}
	
	/**
	 * Get the AuthInfoDAO
	 * @return AuthInfoDAO
	 */
	@Override
	public AuthInfoDAO getAuthInfoDAO() {
		return new AuthInfoDAO(em, logger);
	}
	
	/**
	 * Get the LocationDAO
	 * @return LocationDAO
	 */
	@Override
	public LocationDAO getLocationDAO() {
		return new LocationDAO(em, logger);
	}
	
	/**
	 * Get the RoleDAO
	 * @return RoleDAO
	 */
	@Override
	public RoleDAO getRoleDAO() {
		return new RoleDAO(em, logger);
	}
	
	/**
	 * Get the OrganizationDAO
	 * @return OrganizationDAO
	 */
	@Override
	public OrganizationDAO getOrganizationDAO() {
		return new OrganizationDAO(em, logger);
	}
	
	/**
	 * Get the ConcactDAO
	 * @return ContactDAO
	 */
	@Override
	public ContactDAO getContactDAO() {
		return new ContactDAO(em, logger);
	}
	
	/**
	 * Get the MechanismDAO
	 * @return
	 */
	@Override
	public MechanismDAO getMechanismDAO() {
		return new MechanismDAO(em, logger);
	}
	
	@Override
	public DirectUserLoginDAO getDirectUserLoginDAO() {
		return new DirectUserLoginDAO(em, logger);
	}
	
	@Override
	public SnmpVendorDiscoveryTemplateDAO getSnmpVendorDiscoveryTemplateDAO() {
		return new SnmpVendorDiscoveryTemplateDAO(em, logger);
	}
	
	@Override
	public VendorContainmentSelectorDAO getVendorContainmentSelectorDAO() {
		return new VendorContainmentSelectorDAO(em, logger);
	}
	
	@Override
	public SnmpContainmentDAO getSnmpContainmentDAO() {
		return new SnmpContainmentDAO(em, logger);
	}
	
	@Override
	public DiscoveryParseStringDAO getDiscoveryParseStringDAO() {
		return new DiscoveryParseStringDAO(em, logger);
	}
	
	@Override
	public DiscoveryParseElementDAO getDiscoveryParseElementDAO() {
		return new DiscoveryParseElementDAO(em, logger);
	}
	
	@Override
	public VendorIdentifierDAO getVendorIdentifierDAO() {
		return new VendorIdentifierDAO(em, logger);
	}
	
	@Override
	public ManagementObjectValueDAO getManagementObjectValueDAO() {
		return new ManagementObjectValueDAO(em, logger);
	}
	
	@Override
	public SnmpServiceElementTypeDiscriminatorValueDAO getSnmpServiceElementTypeDiscriminatorValueDAO() {
		return new SnmpServiceElementTypeDiscriminatorValueDAO(em, logger);
	}
	
	@Override
	public ApplicabilityDAO getApplicabilityDAO() {
		return new ApplicabilityDAO(em, logger);
	}
	
	@Override
	public SnmpServiceElementTypeOverrideDAO getSnmpServiceElementTypeOverrideDAO() {
		return new SnmpServiceElementTypeOverrideDAO(em, logger);
	}
	
	@Override
	public DistributedManagerDAO getDistributedManagerDAO() {
		return new DistributedManagerDAO(em, logger);
	}
	
	@Override
	public DistributedServiceDAO getDistributedServiceDAO() {
		return new DistributedServiceDAO(em, logger);
	}
	
	@Override
	public IntegerServerDAO getIntegerServerDAO() {
		return new IntegerServerDAO(em, logger);
	}
	
	@Override
	public SnmpSyntaxDAO getTextualConventionDAO() {
		return new SnmpSyntaxDAO(em, logger);
	}
	
	@Override
	public SelectionDAO getSelectionDAO() {
		return new SelectionDAO(em, logger);
	}
	
	@Override
	public FilterDAO getFilterDAO() {
		return new FilterDAO(em, logger);
	}
	
	@Override
	public ViewDAO getViewDAO() {
		return new ViewDAO(em, logger);
	}
	
	public LayerDAO getLayerDAO() {
		return new LayerDAO(em, logger);
	}
	
	@Override
	public EventDAO getEventDAO() {
		return new EventDAO(em, logger);
	}
	
	@Override
	public DiscoveryCompleteEventDAO getDiscoveryCompleteEventDAO() {
		return new DiscoveryCompleteEventDAO(em, logger);
	}
}
