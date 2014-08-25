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

package edu.harvard.integer.service.yaml;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.NetworkLayer;
import edu.harvard.integer.common.topology.ServiceElementAssociationType;
import edu.harvard.integer.common.topology.ServiceElementInstanceUniqueSignature;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Signature;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.common.topology.SignatureValueOperator;
import edu.harvard.integer.common.yaml.YamlManagementObject;
import edu.harvard.integer.common.yaml.YamlServiceElementAssociationType;
import edu.harvard.integer.common.yaml.YamlServiceElementType;
import edu.harvard.integer.common.yaml.YamlServiceElementTypeTranslate;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlServiceElementInstanceUniqueSignature;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementAssociationTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementInstanceUniqueSignatureDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;

/**
 * @author David Taylor
 * 
 */
public class YamlServiceElementTypeParser implements
		YamlListParserInterface<YamlServiceElementType> {

	private ServiceElementTypeDAO serviceElementTypeDao = null;
	private ServiceElementAssociationTypeDAO associationDao = null;
	private SNMPDAO snmpDao = null;
	private CapabilityDAO capabilityDAO = null;
	private ServiceElementInstanceUniqueSignatureDAO serviceElementInstanceUniqueSignatureDao = null;
	
	private Logger logger = LoggerFactory
			.getLogger(YamlServiceElementTypeParser.class);

	private ServiceElementDiscoveryManagerInterface discoveryManager = null;
	private ManagementObjectCapabilityManagerInterface managementObjectManager = null;

	public YamlServiceElementTypeParser(
			ServiceElementTypeDAO serviceElementTypeDao,
			ServiceElementAssociationTypeDAO associationDao, SNMPDAO snmpDao,
			CapabilityDAO capabilityDAO,
			ServiceElementInstanceUniqueSignatureDAO serviceElementInstanceUniqueSignatureDao) {
		this.associationDao = associationDao;
		this.serviceElementTypeDao = serviceElementTypeDao;
		this.snmpDao = snmpDao;
		this.capabilityDAO = capabilityDAO;
		this.serviceElementInstanceUniqueSignatureDao = serviceElementInstanceUniqueSignatureDao;

		try {
			discoveryManager = DistributionManager
					.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);

			managementObjectManager = DistributionManager
					.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
		} catch (IntegerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.yaml.YamlListParserInterface#parse(edu.harvard
	 * .integer.common.yaml.YamlBaseInfoInterface[])
	 */
	@Override
	public String parse(YamlServiceElementType[] serviceElementTypes)
			throws IntegerException {

		for (YamlServiceElementType yamlServiceElementType : serviceElementTypes) {

			List<YamlServiceElementTypeTranslate> typeTranslates = yamlServiceElementType
					.getServiceElementTypeTranslates();
			if (typeTranslates == null) {
				logger.warn("Missing Service Element Type Translation "
						+ yamlServiceElementType.getName());
				continue;
			}

			for (YamlServiceElementTypeTranslate typeTranslate : typeTranslates) {

				ServiceElementType exemplarSet = getServiceElementType(
						serviceElementTypeDao, yamlServiceElementType,
						typeTranslate.getName());

				if (typeTranslate.getDefaultNameCabability() != null) {

					ID nameID = getCapability(typeTranslate
							.getDefaultNameCabability());
					exemplarSet.setDefaultNameCababilityId(nameID);
				}
				
				if ( typeTranslate.getNetworkLayer() != null ) {
					
					NetworkLayer netLayer = NetworkLayer.valueOf(typeTranslate.getNetworkLayer());
					exemplarSet.setNetworkLayer(netLayer);
				}

				exemplarSet.setCategory(managementObjectManager
						.getCategoryByName(typeTranslate.getCategory()));

				if (typeTranslate.getMapping().equalsIgnoreCase(
						"subObjIdentify")) {

					List<VendorIdentifier> vis = discoveryManager
							.findVendorNameSubTree(typeTranslate.getName());
					if (vis != null) {
						saveVendorSubTree(vis, serviceElementTypeDao,
								typeTranslate, exemplarSet,
								yamlServiceElementType);
					} else {
						logger.warn("Cannot find sub-tree "
								+ typeTranslate.getName());
					}

				} else {

					List<ID> associationIds = new ArrayList<>();
					if (yamlServiceElementType.getAssociations() != null) {

						for (YamlServiceElementAssociationType yassType : yamlServiceElementType
								.getAssociations()) {

							ServiceElementAssociationType assType = new ServiceElementAssociationType();
							assType.setName(yassType.getName());

							ServiceElementType serviceElementType = serviceElementTypeDao
									.findByName(yassType
											.getAssociateServiceElementTypeName());

							/**
							 * It is for avoid junit test error.
							 */
							if (serviceElementType == null) {
								continue;
							}
							assType.setServiceElementTypeId(serviceElementType
									.getID());

							if (yassType.getManagementObjects() != null) {

								List<ID> managementObjects = new ArrayList<ID>();
								for (YamlManagementObject yamlManagementObject : yassType
										.getManagementObjects()) {

									SNMP snmp = snmpDao
											.findByName(yamlManagementObject
													.getName());
									if (snmp == null) {
										logger.error("No SNMP object found with name "
												+ yamlManagementObject
														.getName());
									} else {

										Capability capability = capabilityDAO
												.findByName(yamlManagementObject
														.getCapability());
										if (capability == null) {
											logger.error("No Capability found with name "
													+ yamlManagementObject
															.getCapability()
													+ " ServiceElement "
													+ yamlServiceElementType
															.getName());
										} else {
											snmp.setCapabilityId(capability
													.getID());
											snmp = snmpDao.update(snmp);
											managementObjects.add(snmp.getID());
										}
									}
								}

								if (managementObjects.size() > 0) {
									assType.setAttributeIds(managementObjects);
								}
								assType = associationDao.update(assType);
								associationIds.add(assType.getID());
							}
						}
					}

					ServiceElementType serviceElementType = serviceElementTypeDao
							.findByName(typeTranslate.getName());
					if (serviceElementType == null) {

						serviceElementType = new ServiceElementType();
						serviceElementType
								.setCategory(managementObjectManager
										.getCategoryByName(typeTranslate
												.getCategory()));
						serviceElementType.setName(typeTranslate.getName());
						serviceElementType
								.setDescription(yamlServiceElementType
										.getDescription());
						serviceElementType.setIconName(typeTranslate
								.getIconName());
					}

					serviceElementType.setAssociations(exemplarSet
							.getAssociations());
					if (associationIds.size() > 0) {

						if (serviceElementType.getAssociations() == null) {
							serviceElementType
									.setAssociations(new ArrayList<ID>());
						}
						for (ID id : associationIds) {
							serviceElementType.getAssociations().add(id);
						}
					}

					serviceElementType.addSignatureValue(null,
							SignatureTypeEnum.Vendor,
							yamlServiceElementType.getVendor());

					serviceElementType.setAttributeIds(exemplarSet
							.getAttributeIds());
					if (exemplarSet.getDefaultNameCababilityId() != null) {
						serviceElementType
								.setDefaultNameCababilityId(exemplarSet
										.getDefaultNameCababilityId());
					}
					
					if (exemplarSet.getNetworkLayer() != null ) {
						serviceElementType.setNetworkLayer(exemplarSet.getNetworkLayer());
					}
						
					serviceElementType
							.setUniqueIdentifierCapabilities(exemplarSet
									.getUniqueIdentifierCapabilities());

					serviceElementType = serviceElementTypeDao.update(serviceElementType);
					
					if (yamlServiceElementType.getUniqueInstanceSignature() != null)
						createUniqueInstanceSignature(yamlServiceElementType.getUniqueInstanceSignature(), serviceElementType);

				}
			}
		}
		return null;
	}

	/**
	 * @param uniqueInstanceSignature
	 * @param serviceElementType
	 * @throws IntegerException 
	 */
	private void createUniqueInstanceSignature(
			YamlServiceElementInstanceUniqueSignature yamlSignature,
			ServiceElementType serviceElementType) throws IntegerException {
		
		ServiceElementInstanceUniqueSignature signature = new ServiceElementInstanceUniqueSignature();
		signature.setName(yamlSignature.getName());
		signature.setDescription(yamlSignature.getDescription());
		signature.setServiceElementId(serviceElementType.getID());
		
		if ( yamlSignature.getUniqueSemos() != null ) {
		    signature.setUniqueSemos(getSemosFromNames(yamlSignature.getUniqueSemos()));
		}
		
		if ( yamlSignature.getChangeSemos() != null ) {
			signature.setChangeSemos(getSemosFromNames(yamlSignature.getChangeSemos()));
		}
		serviceElementInstanceUniqueSignatureDao.update(signature);	
	}
	
	
	private List<ServiceElementManagementObject> getSemosFromNames(List<String> names) throws IntegerException {
		List<ServiceElementManagementObject> semos = new ArrayList<ServiceElementManagementObject>();
		for (String semoName : names) {
			semos.add(getSnmpOid(semoName));
		}
		
		return semos;
	}
	
	/**
	 * Return the SNMP object for the OID given.
	 * 
	 * @param oidString
	 * @return
	 * @throws IntegerException
	 */
	private SNMP getSnmpOid(String oidString) throws IntegerException {
		
		SNMP findByName = snmpDao.findByName(oidString);
		if (findByName == null)
			findByName = snmpDao.findByOid(oidString);

		if (findByName != null)
			return findByName;

		return null;
	}
	
	/**
	 * Get the capability with the given name from the database if one exists.
	 * 
	 * @param defaultNameCabability
	 *            . Name of capability to get.
	 * @return ID of capability found in the database or null if not found.
	 */
	private ID getCapability(String defaultNameCabability) {

		Capability capability = capabilityDAO.findByName(defaultNameCabability);

		if (capability != null)
			return capability.getID();
		else {
			logger.error("Capability not found with name "
					+ defaultNameCabability);
		}

		return null;
	}

	/**
	 * Save a vendor identifier sub tree.
	 * 
	 * @param vendorIdentifers
	 *            . List of VendorIdentifers to save
	 * @param serviceElementTypeDao
	 *            . DAO to use for saving the ServiceElementTypes.
	 * @param typeTranslate
	 *            . Type of translation to do.
	 * @param exemplarSet
	 *            . Exemplar SeviceElementType to use for creation of new
	 *            Service Element types.
	 * @param yamlServiceElementType
	 *            . YamlServiceElementType to use for creation of the service
	 *            element type
	 * @throws IntegerException
	 */
	private void saveVendorSubTree(List<VendorIdentifier> vis,
			ServiceElementTypeDAO serviceElementTypeDao,
			YamlServiceElementTypeTranslate typeTranslate,
			ServiceElementType exemplarSet,
			YamlServiceElementType yamlServiceElementType)
			throws IntegerException {

		for (VendorIdentifier vi : vis) {
			boolean foundSubType = false;

			for (Signature signature : exemplarSet.getSignatures()) {

				for (SignatureValueOperator value : signature
						.getValueOperators()) {
					if (value.getValue().equals(vi.getVendorSubtypeName())) {
						foundSubType = true;
						break;
					}

					if (foundSubType)
						break;
				}

			}

			if (!foundSubType)
				exemplarSet.addSignatureValue(null,
						SignatureTypeEnum.VendorSubType,
						vi.getVendorSubtypeName());

		}

		serviceElementTypeDao.update(exemplarSet);
	}

	/**
	 * Create a service element type for the YamlServiceElementType. First look
	 * in the database for the service element type. If found then update the
	 * database version.
	 * 
	 * @param serviceElementTypeDao
	 *            . DAO to use to save the service element type.
	 * @param typeTranslate
	 *            . Type of translation to do.
	 * @param yamlServiceElementType
	 *            . YamlServiceElementType describing the service element type.
	 * @throws IntegerException
	 */
	public ServiceElementType getServiceElementType(
			ServiceElementTypeDAO serviceElementTypeDao,
			YamlServiceElementType yamlServiceElementType, String name)
			throws IntegerException {

		ServiceElementType serviceElementType = serviceElementTypeDao
				.findByName(name);
		if (serviceElementType == null) {
			serviceElementType = new ServiceElementType();

			serviceElementType.setName(name);
			serviceElementType.addSignatureValue(null,
					SignatureTypeEnum.Vendor,
					yamlServiceElementType.getVendor());
		}

		List<ID> managementObjects = new ArrayList<ID>();
		if (yamlServiceElementType.getExtendServiceElementType() != null) {

			ServiceElementType extendSet = serviceElementTypeDao
					.findByName(yamlServiceElementType
							.getExtendServiceElementType());
			if (extendSet != null) {

				List<ID> extendUniqueIds = extendSet
						.getUniqueIdentifierCapabilities();
				List<ID> snmpIds = extendSet.getAttributeIds();

				for (ID id : snmpIds) {

					SNMP snmp = snmpDao.findById(id);
					managementObjects.add(snmp.getID());

					boolean isUid = false;
					if (extendUniqueIds != null) {
						for (ID uid : extendUniqueIds) {

							if (id.getIdentifier() == uid.getIdentifier()) {
								isUid = true;
							}
						}
					}
					if (isUid) {
						List<ID> uniqueIds = serviceElementType
								.getUniqueIdentifierCapabilities();
						if (uniqueIds == null) {
							uniqueIds = new ArrayList<>();
							serviceElementType
									.setUniqueIdentifierCapabilities(uniqueIds);
						}
						uniqueIds.add(id);
					}
				}
				serviceElementType.setAssociations(new ArrayList<ID>());
				if (extendSet.getAssociations() != null
						&& extendSet.getAssociations().size() > 0) {

					for (ID id : extendSet.getAssociations()) {
						serviceElementType.getAssociations().add(id);
					}
				}
			}
		}

		if (yamlServiceElementType.getManagementObjects() != null) {
			for (YamlManagementObject yamlManagementObject : yamlServiceElementType
					.getManagementObjects()) {

				SNMP snmp = snmpDao.findByName(yamlManagementObject.getName());
				if (snmp == null) {
					logger.error("No SNMP object found with name "
							+ yamlManagementObject.getName());
				} else {

					Capability capability = capabilityDAO
							.findByName(yamlManagementObject.getCapability());
					if (capability == null) {
						logger.error("No Capability found with name "
								+ yamlManagementObject.getCapability()
								+ " ServiceElement "
								+ yamlServiceElementType.getName());
					} else {
						snmp.setCapabilityId(capability.getID());
						snmp = snmpDao.update(snmp);
						managementObjects.add(snmp.getID());
					}
					if (yamlManagementObject.getUnique() == 1) {
						List<ID> ids = serviceElementType
								.getUniqueIdentifierCapabilities();
						if (ids == null) {
							ids = new ArrayList<>();
							serviceElementType
									.setUniqueIdentifierCapabilities(ids);
						}
						ids.add(snmp.getID());
					}
				}
			}
		}

		logger.info("Setting Attributes " + managementObjects + " On "
				+ serviceElementType.getName());
		serviceElementType.setAttributeIds(managementObjects);
		// serviceElementTypeDao.update(serviceElementType);

		return serviceElementType;
	}

}
