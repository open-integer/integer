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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpVendorDiscoveryTemplateDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.VendorContainmentSelectorDAO;
import edu.harvard.integer.service.persistence.dao.discovery.VendorIdentifierDAO;

/**
 * @author David Taylor
 * 
 *         The service element discovery manager is responsible for both initial
 *         discovery of network elements and subsequent passes over the same
 *         elements. It is not responsible for computing network topology though
 *         it collects information that is used by that service.
 * 
 * 
 *         The ServiceElementDiscoveryManager is started by the Service and
 *         there is one instance created for each subnet of the
 *         ServiceElementDiscoveryManager. When it is called the
 *         DiscoveryService strips out unneeded information and it only creates
 *         a ServiceElementDiscoverySeed. That will contain, the subnet, list of
 *         found hosts, and serviceElementTypeExclusions.
 *         
 */
@Stateless
public class ServiceElementDiscoveryManager extends BaseManager implements
		ServiceElementDiscoveryManagerInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface dbm;

	/**
	 * Start a discovery of ServiceElements with the given Discovery seed. 
	 * 
	 * @param id
	 * @param seed
	 * @return
	 * @throws IntegerException
	 */
	@Override
	public NetworkDiscovery<ServiceElement> startDiscovery(DiscoveryId id, IpDiscoverySeed seed) throws IntegerException {
		
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
	
		List<SNMP> mgrObjects = getToplLevelOIDs();
		for ( SNMP snmp : mgrObjects ) {

			VariableBinding vb = new VariableBinding(new OID(snmp.getOid()));
			vbs.add(vb);

		}
		
		NetworkDiscovery<ServiceElement> discovery = new NetworkDiscovery<ServiceElement>(seed, vbs, id);
		
		discovery.discoverNetwork();
		
		return discovery;
	}
	
	@Override
	public SnmpVendorDiscoveryTemplate getSnmpVendorDiscoveryTemplateByVendor(
			ID vendorId) throws IntegerException {

		SnmpVendorDiscoveryTemplateDAO dao = dbm
				.getSnmpVendorDiscoveryTemplateDAO();

		return dao.findByVendor(vendorId);
	}
	
	@Override
	public SnmpVendorDiscoveryTemplate getSnmpVendorDiscoveryTemplateByVendor(
			Long vendor) throws IntegerException {

		SnmpVendorDiscoveryTemplateDAO dao = dbm
				.getSnmpVendorDiscoveryTemplateDAO();

		return dao.findByVendor(vendor);
	}
	
	@Override
	public SnmpVendorDiscoveryTemplate updateSnmpVendorDiscoveryTemplate(SnmpVendorDiscoveryTemplate template) throws IntegerException {
	
		SnmpVendorDiscoveryTemplateDAO dao = dbm
				.getSnmpVendorDiscoveryTemplateDAO();

		
		return dao.update(template);
	}
	
	/**
	 * Return all the SnmpVendorTemplates in the system.
	 * 
	 * @return
	 * @throws IntegerException
	 */
	@Override
	public SnmpVendorDiscoveryTemplate[] getAllSnmpVendorDiscoveryTemplates() throws IntegerException {
		SnmpVendorDiscoveryTemplateDAO dao = dbm
				.getSnmpVendorDiscoveryTemplateDAO();

		return dao.findAll();
	}
	
	@Override
	public SnmpContainment getSnmpContainment(VendorContainmentSelector selector)
			throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();

		VendorContainmentSelector[] containmentSelectors = dao
				.findBySelector(selector);
		if (containmentSelectors == null || containmentSelectors.length == 0)
			return null;

		if (logger.isDebugEnabled())
			logger.debug("Found VendorContainmentSelector "
					+ containmentSelectors[0].getID());

		SnmpContainmentDAO snmpContainmentDAO = dbm.getSnmpContainmentDAO();

		return snmpContainmentDAO.findById(containmentSelectors[0]
				.getContainmentId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManager#getSnmpVendorDiscoveryTemplateByVendor(String)
	 * 
	 */
	@Override
	public VendorIdentifier getVendorIdentifier(Long vendorId) throws IntegerException {
		VendorIdentifierDAO dao = dbm.getVendorIdentifierDAO();
		
		return dao.findByVendorId(vendorId);
	}
	
	/*
	 *  (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManager#updateVendorIdentifier(VendorIdentifier)
	 * 
	 */
	@Override
	public VendorIdentifier updateVendorIdentifier(VendorIdentifier vendorIdentifier) throws IntegerException {
		VendorIdentifierDAO dao = dbm.getVendorIdentifierDAO();
		
		
		return dao.update(vendorIdentifier);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getTopLevelPolls()
	 */
	@Override
	public List<SNMP> getToplLevelOIDs() {
		
		SNMPDAO snmpdao = dbm.getSNMPDAO();
		
		List<SNMP> snmps = new ArrayList<>();
		
		snmps = addOid(CommonSnmpOids.sysContact, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.sysDescr, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.sysLocation, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.sysName, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.sysObjectID, snmps, snmpdao);
		
		return snmps;
	}
	
	private List<SNMP> addOid(String oid, List<SNMP> snmps, SNMPDAO dao) {
		SNMP snmp = dao.findByOid(oid);
		if (snmp != null)
			snmps.add(snmp);
		else {
			logger.error("OID " + oid + " not found!!");
		}
		
		return snmps;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getServiceElementTypeById(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElementType getServiceElementTypeById(ID serviceElementTypeId) throws IntegerException {
		ServiceElementTypeDAO dao = dbm.getServiceElementTypeDAO();
		
		return dao.findById(serviceElementTypeId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getServiceElementTypesByCategoryAndVendor()
	 */
	@Override
	public ServiceElementType[] getServiceElementTypesByCategoryAndVendor(String catetory, String vendorType) throws IntegerException {
		
		ServiceElementTypeDAO dao = dbm.getServiceElementTypeDAO();
		
		ServiceElementType[] types = dao.findByCategoryAndVendor(catetory, vendorType);
		
		return types;
	}
	
	
}
