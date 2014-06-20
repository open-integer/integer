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
import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpVendorDiscoveryTemplate;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.discovery.VendorIdentifierDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpContainmentDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpVendorDiscoveryTemplateDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.VendorContainmentSelectorDAO;

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
		ServiceElementDiscoveryManagerLocalInterface, ServiceElementDiscoveryManagerRemoteInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface dbm;

	/**
	 * @param managerType
	 */
	public ServiceElementDiscoveryManager() {
		super(ManagerTypeEnum.ServiceElementDiscoveryManager);
		
	}

	/**
	 * Start a discovery of ServiceElements with the given Discovery seed. 
	 * 
	 * @param id
	 * @param seed
	 * @return
	 * @throws IntegerException
	 */
	@Override
	public NetworkDiscovery startDiscovery(DiscoveryId id, IpDiscoverySeed seed) throws IntegerException {
		
		List<VariableBinding> vbs = new ArrayList<VariableBinding>();
	
		List<SNMP> mgrObjects = getToplLevelOIDs();
		for ( SNMP snmp : mgrObjects ) {

			VariableBinding vb = new VariableBinding(new OID(snmp.getOid() + ".0" ));
			vbs.add(vb);
		}
		NetworkDiscovery discovery = new NetworkDiscovery(seed, vbs, id);
		discovery.discoverNetwork();
		
		logger.info("Start ServiceElement discovery of " + seed.getSeedId());
		
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
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#updateVendorContainmentSelector(edu.harvard.integer.common.discovery.VendorContainmentSelector)
	 */
	@Override
	public VendorContainmentSelector updateVendorContainmentSelector(VendorContainmentSelector selector) throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();
		
		selector = dao.update(selector);
		
		return selector;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getAllVendorContainmentSelectors()
	 */
	@Override
	public VendorContainmentSelector[] getAllVendorContainmentSelectors() throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getVendorContainmentSelectorById(edu.harvard.integer.common.ID)
	 */
	@Override
	public VendorContainmentSelector getVendorContainmentSelectorById(ID id) throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();
		
		return dao.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getVendorContainmentSelector(edu.harvard.integer.common.discovery.VendorContainmentSelector)
	 */
	@Override
	public VendorContainmentSelector[] getVendorContainmentSelector(VendorContainmentSelector selector) throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();

		return dao.findBySelector(selector);
	}
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#deleteVendorContianmentSelector(edu.harvard.integer.common.ID)
	 */
	@Override
	public void deleteVendorContianmentSelector(ID id) throws IntegerException {
		VendorContainmentSelectorDAO dao = dbm
				.getVendorContainmentSelectorDAO();
		
		dao.delete(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getSnmpContainment(edu.harvard.integer.common.discovery.VendorContainmentSelector)
	 */
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

	public SnmpContainment[] getSnmpContainments(VendorContainmentSelector selector)
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

		List<SnmpContainment> snmpContainments = new ArrayList<SnmpContainment>();
		for (VendorContainmentSelector vendorContainmentSelector : containmentSelectors) {
			SnmpContainment snmpContainment = snmpContainmentDAO.findById(vendorContainmentSelector.getContainmentId());
			if (snmpContainment != null)
				snmpContainments.add(snmpContainment);
		}
		
		return (SnmpContainment[]) snmpContainments
				.toArray(new SnmpContainment[snmpContainments.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#updataSnmpContainment(edu.harvard.integer.common.discovery.SnmpContainment)
	 */
	@Override
	public SnmpContainment updateSnmpContainment(SnmpContainment snmpContainment) throws IntegerException {
		SnmpContainmentDAO dao = dbm.getSnmpContainmentDAO();
		
		return dao.update(snmpContainment);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManager#getSnmpVendorDiscoveryTemplateByVendor(String)
	 * 
	 */
	@Override
	public VendorIdentifier getVendorIdentifier(String vendorOid) throws IntegerException {
		VendorIdentifierDAO dao = dbm.getVendorIdentifierDAO();
		
		if (countDots(vendorOid) <= 6)
			return dao.findByVendorOid(vendorOid);
		else
			return dao.findByVendorSubtypeId(vendorOid);
	}
	
	@Override
	public VendorIdentifier getVenderIdentiferBySubTypeName(String vendorSubTypeName) throws IntegerException {
		VendorIdentifierDAO dao = dbm.getVendorIdentifierDAO();
		
		return dao.findByVendorSubtypeName(vendorSubTypeName);
	}
	
	private int countDots(String oidString) {
		int count = 0;
		int offset = 0;
		offset = oidString.indexOf('.');
		
		while (offset > 0) {
			offset = oidString.indexOf(offset + 1, '.');
			count++;
		}
		
		return count;
	}
	
	@Override
	public List<VendorIdentifier> findVendorSubTree(String name) throws IntegerException {
		VendorIdentifierDAO dao = dbm.getVendorIdentifierDAO();
		
		List<VendorIdentifier> oids = dao.findByOidSubtree(name);
		
		return oids;
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
		
		return (ServiceElementType) dao.createCleanCopy(dao.findById(serviceElementTypeId));
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getEntityMIBCollumn()
	 */
	@Override
	public List<SNMP> getEntityMIBInfo() {
		
        SNMPDAO snmpdao = dbm.getSNMPDAO();
		
		List<SNMP> snmps = new ArrayList<>();
		
		snmps = addOid(CommonSnmpOids.entPhysicalClass, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalContainedIn, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalDescr, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalFirmwareRev, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalHardwareRev, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalModelName, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalName, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalParentRelPos, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalSoftwareRev, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalSerialNum, snmps, snmpdao);
		snmps = addOid(CommonSnmpOids.entPhysicalVendorType, snmps, snmpdao);
		
		return snmps;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getServiceElementTypesByCategoryAndVendor()
	 */
	@Override
	public ServiceElementType[] getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum catetory, String vendorType) throws IntegerException {
		
		ServiceElementTypeDAO dao = dbm.getServiceElementTypeDAO();
		
		ServiceElementType[] types = dao.findByCategoryAndVendor(catetory, vendorType);
		
		return types;
	}
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface#getServiceElementTypesBySubtypeAndVendor(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceElementType[] getServiceElementTypesBySubtypeAndVendor(
			String subtype, String vendorType) throws IntegerException {
		
		ServiceElementTypeDAO dao = dbm.getServiceElementTypeDAO();
		
		ServiceElementType[] types = dao.findBySubTypeAndVendor(subtype, vendorType);
		
		return types;
	}

}
