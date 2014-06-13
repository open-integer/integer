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
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpContainmentType;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpParentChildRelationship;
import edu.harvard.integer.common.discovery.SnmpRelationship;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorStringValue;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorValue;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.YamlParserErrrorCodes;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.snmp.SnmpDisplayStringTC;
import edu.harvard.integer.common.technology.Mechanism;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.yaml.YamlCapability;
import edu.harvard.integer.common.yaml.YamlDomainData;
import edu.harvard.integer.common.yaml.YamlManagementObject;
import edu.harvard.integer.common.yaml.YamlMechanismType;
import edu.harvard.integer.common.yaml.YamlServiceElementType;
import edu.harvard.integer.common.yaml.YamlTechnology;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlSnmpContainmentRelation;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlSnmpLevelOID;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlSnmpParentChildRelationship;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlSnmpRelationship;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlSnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlVendorContainment;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.managementobject.CapabilityDAO;
import edu.harvard.integer.service.persistance.dao.snmp.SNMPDAO;
import edu.harvard.integer.service.persistance.dao.topology.ServiceElementTypeDAO;
import edu.harvard.integer.service.persistance.dao.topology.vendortemplate.SnmpLevelOIDDAO;
import edu.harvard.integer.service.technology.TechnologyManagerInterface;

/**
 * @author David Taylor
 * 
 */
@Stateless
public class YamlManager extends BaseManager implements
		YamlManagerLocalInterface, YamlManagerRemoteInterface {

	@Inject
	private Logger logger;

	@Inject
	private TechnologyManagerInterface technologyManager;

	@Inject
	PersistenceManagerInterface persistanceManager;

	/**
	 * @param managerType
	 */
	public YamlManager() {
		super(ManagerTypeEnum.YamlManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.yaml.YamlManagerInterface#loadTechnologyTree
	 * (java.lang.String)
	 */
	@Override
	public String loadTechnologyTree(String content) throws IntegerException {

		Yaml yaml = new Yaml(new CustomClassLoaderConstructor(
				YamlTechnology.class, getClass().getClassLoader()));

		YamlTechnology load = null;

		try {
			load = (YamlTechnology) yaml.load(content);
		} catch (Throwable e) {
			logger.error("Unexpected error reading in YAML! " + e.toString());
			e.printStackTrace();
			throw new IntegerException(e, YamlParserErrrorCodes.ParsingError);
		}

		logger.info("YAML Object is " + load.getClass().getName());

		try {
			Technology[] root = technologyManager.getTopLevelTechnology();

			Technology rootTech = null;
			if (root == null || root.length == 0) {
				rootTech = new Technology();
				rootTech.setName("root");
				rootTech = technologyManager.updateTechnology(rootTech);

			} else {// Take first root. There should be only one!

				rootTech = root[0];
			}

			parseTechnologyTree(rootTech, load.getTechnologies());

			root = technologyManager.getTopLevelTechnology();
			if (root != null) {
				String dump = yaml.dump(root);
				logger.info("Technology: " + dump);
			}
		} catch (IntegerException e) {
			logger.error("Error reading in YAML file! " + e.toString()
					+ " Conent " + content);
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			logger.error("Unexpected Error reading in YAML file! "
					+ e.toString() + " Conent " + content);
			e.printStackTrace();
		}

		return "Success";
	}

	/**
	 * @param technology
	 * @param list
	 * @throws IntegerException
	 */
	private void parseTechnologyTree(Technology parentTechnology,
			List<YamlTechnology> list) throws IntegerException {

		for (YamlTechnology node : list) {
			Technology technology = technologyManager.getTechnologyByName(node
					.getName());
			if (technology == null) {
				technology = new Technology();
				technology.setName(node.getName());
			}

			technology.setParentId(parentTechnology.getID());
			technology.setDescription(node.getDescription());
			technology.setMechanisims(saveMechanisms(technology,
					node.getMechanisms()));

			technology = technologyManager.updateTechnology(technology);

			if (node.getTechnologies() != null)
				parseTechnologyTree(technology, node.getTechnologies());

		}

	}

	/**
	 * @param mechanismTypes
	 * @throws IntegerException
	 */
	private List<ID> saveMechanisms(Technology technology,
			List<YamlMechanismType> mechanismTypes) throws IntegerException {

		List<ID> ids = new ArrayList<ID>();

		if (mechanismTypes == null)
			return ids;

		for (YamlMechanismType mechanism : mechanismTypes) {
			Mechanism dbMechanism = technologyManager
					.getMechanismByName(mechanism.getName());

			if (dbMechanism == null) {
				dbMechanism = new Mechanism();
				dbMechanism.setName(mechanism.getName());
			}

			dbMechanism.setDescription(mechanism.getDescription());

			List<ID> capabilitiIds = saveCapabilites(mechanism
					.getCapabilities());

			dbMechanism.setCapabilities(capabilitiIds);
			dbMechanism = technologyManager.updateMechanism(dbMechanism);

			ids.add(dbMechanism.getID());
		}

		return ids;
	}

	/**
	 * @param capabilities
	 * @return
	 * @throws IntegerException
	 */
	private List<ID> saveCapabilites(List<YamlCapability> capabilities)
			throws IntegerException {

		CapabilityDAO dao = persistanceManager.getCapabilityDAO();

		List<ID> ids = new ArrayList<ID>();

		if (capabilities == null)
			return ids;

		for (YamlCapability yamlCapability : capabilities) {
			Capability dbCapablity = dao.findByName(yamlCapability.getName());
			if (dbCapablity == null) {
				dbCapablity = new Capability();
				dbCapablity.setName(yamlCapability.getName());
			}

			dbCapablity.setDescription(yamlCapability.getDescription());

			dbCapablity = dao.update(dbCapablity);

			ids.add(dbCapablity.getID());
		}

		return ids;
	}

	private void parseArrayList(ID parentId, String indent,
			ArrayList<Object> list) throws IntegerException {
		for (Object value : list) {
			// logger.info("Item " + value + " class " +
			// value.getClass().getName());
			parseObject(parentId, indent, value);
		}
	}

	private void parseHashMap(ID parentId, String indent,
			LinkedHashMap<String, Object> map) throws IntegerException {
		for (String key : map.keySet()) {
			Object value = map.get(key);

			logger.info(indent + "Key " + key);

			if (key.contains("placeholder"))
				continue;

			Technology technology = technologyManager.getTechnologyByName(key);
			if (technology == null) {
				technology = createTechnology(key, indent, parentId);
			}
			parentId = technology.getID();

			parseObject(parentId, indent, value);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseObject(ID parentId, String indent, Object value)
			throws IntegerException {
		if (value instanceof ArrayList)
			parseArrayList(parentId, indent + "  ", (ArrayList<Object>) value);
		else if (value instanceof LinkedHashMap)
			parseHashMap(parentId, indent + "  ",
					(LinkedHashMap<String, Object>) value);
		else if (value instanceof String) {
			logger.info(indent + "String " + value);
			createTechnology((String) value, indent, parentId);
		} else
			logger.error("Unknown Value type " + value.getClass().getName()
					+ " " + value);
	}

	private Technology createTechnology(String name, String indent, ID parentId)
			throws IntegerException {

		if (name.contains("placeholder"))
			return null;

		Technology technology = technologyManager.getTechnologyByName(name);
		if (technology == null)
			technology = new Technology();

		int indexOf = name.indexOf('(');
		if (indexOf > 0) {
			technology.setName(name.substring(0, name.indexOf('(') - 1));

			int endIndex = name.lastIndexOf(')');
			if (endIndex < indexOf)
				endIndex = name.length();

			technology.setDescription(name.substring(name.indexOf('(') + 1,
					endIndex));
		} else {
			technology.setDescription(name);
			technology.setName(name);
		}

		logger.info("Create Technology " + technology.getName() + "("
				+ technology.getName().length() + ")" + "== "
				+ technology.getDescription() + "("
				+ technology.getDescription().length() + ")");

		technology.setParentId(parentId);

		StringBuffer b = new StringBuffer();
		b.append('\n');
		b.append(indent).append("- name: ").append(technology.getName())
				.append('\n');
		b.append(indent).append("  description: ")
				.append(technology.getDescription()).append('\n');
		b.append(indent).append("  parent: ")
				.append(technology.getParentId().getName()).append('\n');

		logger.info(b.toString());

		technology = technologyManager.updateTechnology(technology);

		return technology;
	}

	@Override
	public String loadServiceElementType(String content)
			throws IntegerException {
		Yaml yaml = new Yaml(new CustomClassLoaderConstructor(
				YamlDomainData.class, getClass().getClassLoader()));

		YamlDomainData load = null;

		try {
			load = (YamlDomainData) yaml.load(content);
		} catch (Throwable e) {
			logger.error("Unexpected error reading in YAML! " + e.toString());
			e.printStackTrace();
			throw new IntegerException(e, YamlParserErrrorCodes.ParsingError);
		}

		logger.info("YAML Object is " + load.getClass().getName());

		ServiceElementTypeDAO serviceElementTypeDao = persistanceManager
				.getServiceElementTypeDAO();

		parseServiceElements(load.getServiceElementTypes(),
				serviceElementTypeDao);

		return "Success";
	}

	/**
	 * @param serviceElementTypes
	 * @param serviceElementTypeDao
	 * @throws IntegerException
	 */
	private void parseServiceElements(
			List<YamlServiceElementType> serviceElementTypes,
			ServiceElementTypeDAO serviceElementTypeDao)
			throws IntegerException {

		for (YamlServiceElementType yamlServiceElementType : serviceElementTypes) {
			ServiceElementType serviceElementType = serviceElementTypeDao
					.findByName(yamlServiceElementType.getName());
			if (serviceElementType == null) {
				serviceElementType = new ServiceElementType();
				serviceElementType.setName(yamlServiceElementType.getName());
				serviceElementType.setDescription(yamlServiceElementType
						.getDescription());
			}

			List<ID> managementObjects = new ArrayList<ID>();

			for (YamlManagementObject yamlManagementObject : yamlServiceElementType
					.getManagementObjects()) {
				SNMPDAO dao = persistanceManager.getSNMPDAO();
				SNMP snmp = dao.findByName(yamlManagementObject.getName());
				if (snmp == null) {
					logger.error("No SNMP object found with name "
							+ yamlManagementObject.getName());
				} else {
					CapabilityDAO capabilityDAO = persistanceManager
							.getCapabilityDAO();
					Capability capability = capabilityDAO
							.findByName(yamlManagementObject.getCapability());
					if (capability == null) {
						logger.error("No Capability found with name "
								+ yamlManagementObject.getCapability()
								+ " ServiceElement "
								+ yamlServiceElementType.getName());
					} else {
						snmp.setCapabilityId(capability.getID());

						snmp = dao.update(snmp);

						managementObjects.add(snmp.getID());

					}
				}
			}

			logger.info("Setting Attributes " + managementObjects + " On "
					+ serviceElementType.getName());
			serviceElementType.setAttributeIds(managementObjects);
			serviceElementTypeDao.update(serviceElementType);

		}

	}

	@Override
	public String loadVendorContainment(String content) throws IntegerException {
		Yaml yaml = new Yaml(new CustomClassLoaderConstructor(
				YamlVendorContainment.class, getClass().getClassLoader()));

		YamlVendorContainment load = null;

		try {
			load = (YamlVendorContainment) yaml.load(content);
		} catch (Throwable e) {
			logger.error("Unexpected error reading in YAML! " + e.toString());
			e.printStackTrace();
			throw new IntegerException(e, YamlParserErrrorCodes.ParsingError);
		}

		logger.info("YAML Object is " + load.getClass().getName());

		parseVendorDeployment(load);

		return "Success";

	}

	/**
	 * @param load
	 * @throws IntegerException
	 */
	private void parseVendorDeployment(YamlVendorContainment load)
			throws IntegerException {
		ServiceElementDiscoveryManagerInterface discoveryManager = DistributionManager
				.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);

		if (discoveryManager == null) {
			logger.error("Unable to load VendorContianment YAML! NO ServiceElementDiscoveryManager found!!");
			return;
		}

		VendorContainmentSelector selector = new VendorContainmentSelector();
		selector.setFirmware(load.getFirmware());
		selector.setModel(load.getModel());
		selector.setSoftwareVersion(load.getSoftwareVersion());
		selector.setVendor(load.getVendor());
		VendorContainmentSelector[] vendorContainmentSelectors = discoveryManager
				.getVendorContainmentSelector(selector);
		if (vendorContainmentSelectors == null
				|| vendorContainmentSelectors.length == 0) {
			selector = discoveryManager
					.updateVendorContainmentSelector(selector);
		} else
			selector = vendorContainmentSelectors[0];

		SnmpContainment snmpContainment = discoveryManager
				.getSnmpContainment(selector);

		if (snmpContainment == null) {
			snmpContainment = new SnmpContainment();
			snmpContainment.setName(load.getVendor() + ":"
					+ load.getSoftwareVersion() + ":" + load.getModel() + ":"
					+ load.getFirmware());
		}

		SnmpContainmentType contanmentType = SnmpContainmentType.valueOf(load
				.getSnmpContainment().getContainmentType());
		snmpContainment.setContainmentType(contanmentType);
		snmpContainment.setSnmpLevels(createSnmpLevelOIDs(load
				.getSnmpContainment().getSnmpLevels(), snmpContainment
				.getSnmpLevels()));
		snmpContainment.setServiceElementTypeId(createServiceElement(load
				.getSnmpContainment().getServiceElementType()));

		discoveryManager.updateVendorContainmentSelector(selector);

	}

	private List<SnmpLevelOID> createSnmpLevelOIDs(
			List<YamlSnmpLevelOID> yamlSnmpLevelOids,
			List<SnmpLevelOID> topLevels) throws IntegerException {

		SnmpLevelOIDDAO levelDao = persistanceManager.getSnmpLevelOIDDAO();
		SNMPDAO snmpDao = persistanceManager.getSNMPDAO();

		List<SnmpLevelOID> dbLevels = new ArrayList<SnmpLevelOID>();

		for (YamlSnmpLevelOID levelOid : yamlSnmpLevelOids) {
			SNMP snmp = snmpDao.findByName(levelOid.getContextOID());
			if (snmp == null) {
				logger.error("OID not found for " + levelOid.getContextOID()
						+ " Unable to create SnmpLevelOID!");
				continue;
			}

			SnmpLevelOID dbLevelOid = findSnmpLevelOID(
					levelOid.getContextOID(), topLevels);

			if (dbLevelOid == null) {
				dbLevelOid = new SnmpLevelOID();
				dbLevelOid.setContextOID(getSnmpOid(levelOid.getContextOID()));
			}

			dbLevelOid.setDescriminatorOID(getSnmpOid(levelOid
					.getDescriminatorOID()));
			dbLevelOid
					.setDisriminators(createDiscriminatorList(
							levelOid.getDisriminators(),
							dbLevelOid.getDisriminators()));

			SnmpRelationship contianmentRelationship = createContainmentRelation(
					levelOid.getContainmentRelationship(),
					dbLevelOid.getRelationToParent(), topLevels);

			if (contianmentRelationship != null)
				dbLevelOid.setRelationToParent(contianmentRelationship);
			else {
				dbLevelOid.setRelationToParent(createParentChildRelationship(
						levelOid.getParentChildRelationship(),
						dbLevelOid.getRelationToParent()));
			}

			if (levelOid.getChildren() != null)
				dbLevelOid.setChildren(createSnmpLevelOIDs(
						levelOid.getChildren(), topLevels));

			dbLevelOid = levelDao.update(dbLevelOid);

			dbLevels.add(dbLevelOid);
		}

		return dbLevels;
	}

	/**
	 * @param parentChildRelationship
	 * @param relationToParent
	 * @throws IntegerException 
	 */
	private SnmpRelationship createParentChildRelationship(
			YamlSnmpParentChildRelationship yamlParentChildRelation,
			SnmpRelationship relationToParent) throws IntegerException {

		if (yamlParentChildRelation == null)
			return relationToParent;
	
		if (relationToParent == null || !(relationToParent instanceof SnmpParentChildRelationship))
			relationToParent = new SnmpParentChildRelationship();
		
		SnmpParentChildRelationship parentChildRelation = (SnmpParentChildRelationship) relationToParent;
		
		parentChildRelation.setContainmentOid(getSnmpOid(yamlParentChildRelation.getContainmentOid()));
		
		parentChildRelation.setModelOid(getSnmpOid(yamlParentChildRelation.getModelOid()));
		
		parentChildRelation.setSiblingOid(getSnmpOid(yamlParentChildRelation.getSiblingOid()));
		
		parentChildRelation.setSoftwareVersionOid(getSnmpOid(yamlParentChildRelation.getSoftwareVersionOid()));
		
		parentChildRelation.setSubTypeOid(getSnmpOid(yamlParentChildRelation.getSubTypeOid()));
		
		return parentChildRelation;
	}

	/**
	 * @param yamlSnmpRelationship
	 * @param relationToParent2
	 * @return
	 * @throws IntegerException 
	 */
	private SnmpRelationship createContainmentRelation(
			YamlSnmpContainmentRelation yamlSnmpRelationship,
			SnmpRelationship dbRelationToParent, List<SnmpLevelOID> levels) throws IntegerException {

		if (yamlSnmpRelationship == null)
			return dbRelationToParent;
		
		if (dbRelationToParent == null || !(dbRelationToParent instanceof SnmpContainmentRelation))
			dbRelationToParent = new SnmpContainmentRelation();
		
		SnmpContainmentRelation containmentRelation = (SnmpContainmentRelation) dbRelationToParent;
		
		containmentRelation.setMappingOid(getSnmpOid(yamlSnmpRelationship.getMappingOid()));
		
		containmentRelation.setMappingTable((SNMPTable) getSnmpOid(yamlSnmpRelationship.getChildTable()));
		
		if (yamlSnmpRelationship.getChildTable() != null) {
			SnmpLevelOID childSnmpLevel = findSnmpLevelOID(yamlSnmpRelationship.getChildTable(), levels);
			containmentRelation.setChildTable(childSnmpLevel);
		}
		
		return containmentRelation;
	}

	/**
	 * @param disriminators
	 * @param list
	 * @return
	 * @throws IntegerException
	 */
	private List<SnmpServiceElementTypeDiscriminator> createDiscriminatorList(
			List<YamlSnmpServiceElementTypeDiscriminator> disriminators,
			List<SnmpServiceElementTypeDiscriminator> list)
			throws IntegerException {

		if (list == null)
			list = new ArrayList<SnmpServiceElementTypeDiscriminator>();

		for (YamlSnmpServiceElementTypeDiscriminator yamlSnmpServiceElementTypeDiscriminator : disriminators) {
			SnmpServiceElementTypeDiscriminator dbDiscriminator = findDiscriminator(
					yamlSnmpServiceElementTypeDiscriminator, list);
			if (dbDiscriminator == null) {
				dbDiscriminator = new SnmpServiceElementTypeDiscriminator();
				dbDiscriminator
						.setDiscriminatorValue(createDiscrimintatorValue(yamlSnmpServiceElementTypeDiscriminator));
				dbDiscriminator
						.setServiceElementTypeId(createServiceElement(yamlSnmpServiceElementTypeDiscriminator
								.getServiceElementType()));
				list.add(dbDiscriminator);
			}

		}

		return list;
	}

	/**
	 * @param serviceElementType
	 * @return
	 * @throws IntegerException
	 */
	private ID createServiceElement(
			edu.harvard.integer.common.yaml.vendorcontainment.YamlServiceElementType serviceElementType)
			throws IntegerException {

		if (serviceElementType == null)
			return null;

		ServiceElementTypeDAO dao = persistanceManager
				.getServiceElementTypeDAO();
		ServiceElementType dbSet = dao.findByName(serviceElementType.getName());
		if (dbSet != null) {
			return dbSet.getID();
		}

		dbSet = new ServiceElementType();
		dbSet.setName(serviceElementType.getName());
		dbSet.setAttributeIds(createAttributeList(
				serviceElementType.getAttributes(),
				serviceElementType.getName()));
		if (serviceElementType.getFieldReplaceableUnit()
				.equalsIgnoreCase("Yes")
				|| serviceElementType.getFieldReplaceableUnit()
						.equalsIgnoreCase("true"))
			dbSet.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);
		else
			dbSet.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);

		dbSet.setDescription(serviceElementType.getDescription());

		dbSet.setDefaultNameCababilityId(getCapability(serviceElementType
				.getDefaultNameCabability()));
		dbSet.setCategory(serviceElementType.getCategory());
		dbSet.setUniqueIdentifierCapabilities(createAttributeList(
				serviceElementType.getUniqueIdentifierCapabilities(),
				serviceElementType.getName()));

		dbSet = dao.update(dbSet);

		return dbSet.getID();
	}

	/**
	 * @param defaultNameCabability
	 * @return
	 */
	private ID getCapability(String defaultNameCabability) {
		CapabilityDAO dao = persistanceManager.getCapabilityDAO();
		Capability capability = dao.findByName(defaultNameCabability);

		if (capability != null)
			return capability.getID();
		else
			logger.error("Capability not found with name "
					+ defaultNameCabability);

		return null;
	}

	/**
	 * @param attributes
	 * @return
	 * @throws IntegerException
	 */
	private List<ID> createAttributeList(List<String> attributes,
			String serviceElementName) throws IntegerException {
		List<ID> ids = new ArrayList<ID>();

		for (String string : attributes) {
			SNMP oid = getSnmpOid(string);
			if (oid != null)
				ids.add(oid.getID());
			else {
				logger.error("OID " + string
						+ " Not found!! Can not add to ServiceElement "
						+ serviceElementName);
			}
		}

		return ids;
	}

	private SnmpServiceElementTypeDiscriminatorValue<?> createDiscrimintatorValue(
			YamlSnmpServiceElementTypeDiscriminator yamlDiscriminator) {
		SnmpServiceElementTypeDiscriminatorStringValue stringValue = new SnmpServiceElementTypeDiscriminatorStringValue();
		stringValue.setValue(yamlDiscriminator.getDiscriminatorValue());
		stringValue.setName(yamlDiscriminator.getDiscriminatorValue());

		return stringValue;
	}

	private SnmpServiceElementTypeDiscriminator findDiscriminator(
			YamlSnmpServiceElementTypeDiscriminator yamlDescriminator,
			List<SnmpServiceElementTypeDiscriminator> descrimintators) {

		for (SnmpServiceElementTypeDiscriminator snmpServiceElementTypeDiscriminator : descrimintators) {
			if (yamlDescriminator.getDiscriminatorValue().equals(
					snmpServiceElementTypeDiscriminator.getDiscriminatorValue()
							.getValue().toString()))
				return snmpServiceElementTypeDiscriminator;
		}

		return null;
	}

	private SnmpLevelOID findSnmpLevelOID(String contextOidName,
			List<SnmpLevelOID> levels) {
		if (levels == null)
			return null;

		for (SnmpLevelOID snmpLevelOID : levels) {
			if (snmpLevelOID.getContextOID().getName().equals(contextOidName))
				return snmpLevelOID;

			// Check the child SnmpLevelOID's for this context OID
			if (snmpLevelOID.getChildren() != null) {
				SnmpLevelOID childLevel = findSnmpLevelOID(contextOidName,
						snmpLevelOID.getChildren());

				if (childLevel != null)
					return childLevel;
			}
		}

		return null;
	}

	/**
	 * Return the SNMP object for the OID given.
	 * 
	 * @param oidString
	 * @return
	 * @throws IntegerException
	 */
	private SNMP getSnmpOid(String oidString) throws IntegerException {
		SNMPDAO snmpdao = persistanceManager.getSNMPDAO();
		SNMP findByName = snmpdao.findByName(oidString);
		if (findByName == null)
			findByName = snmpdao.findByOid(oidString);

		if (findByName != null)
			return findByName;

		return null;
	}

}
