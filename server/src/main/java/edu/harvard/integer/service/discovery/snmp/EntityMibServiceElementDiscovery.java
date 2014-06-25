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
package edu.harvard.integer.service.discovery.snmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * The Class EntityMibServiceElementDiscovery discover service element using 
 * physical entity,
 * 
 * This class will be deprecated soon.
 *
 * @author dchan
 */
public class EntityMibServiceElementDiscovery extends SnmpServiceElementDiscover {
	
	
	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(EntityMibServiceElementDiscovery.class);
    
	/** The snmp containment. */
	private SnmpContainment snmpContainment;
	
	/** The discovery node. */
	private DiscoverNode discNode;
	
	/** The top entity. */
	private PhysEntityRow topEntity;
	
	/** The phyical entity row map. The key is the parent index.  The value is a list physical rows which contains by the
	 * parent
	 */
	private Map<String, List<PhysEntityRow>>  physRowMap; 
	
	/** The elm map uses to keep track of entity element discovered so far */
	private Map<String, EntityElement> elmMap; 
	
	/** The entity mapping table use to map an entity row to other SNMP table. */
	private Map<String, List<AliasMapping>> entityMappingTbl;
	
	
	 /**
	  * @throws IntegerException
	  */
	public EntityMibServiceElementDiscovery() throws IntegerException {
		super();
	}
	
	/* 
	 * Here are the steps for discovery based on entity mib.
	 * First it retrieves all rows in AliaisMapping table and physical entity table.
	 * 
	 * Each row of physical entity store in a list of the physRowMap map which its key the parent index and 
	 * the list contains the children of the parent.
	 * 
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover#discover(edu.harvard.integer.common.discovery.SnmpContainment, edu.harvard.integer.service.discovery.subnet.DiscoverNode)
	 */
	@Override
	public ServiceElement discover( SnmpContainment sc, DiscoverNode discNode ) throws IntegerException 
	{
		logger.debug("Start EntityMibServiceElementDiscovery ***************************************** " + discNode.getIpAddress());
		
		this.snmpContainment = sc;
		this.discNode = discNode;
		
		ElementEndPoint endPoint = discNode.getElementEndPoint();
		
		physRowMap = new HashMap<String, List<PhysEntityRow>>();
		elmMap = new HashMap<>();
		entityMappingTbl = new HashMap<>();
		
		
		OID[] aliasMap = new OID[1];
		aliasMap[0] = new OID(CommonSnmpOids.entAliasMappingIdentifier);
		List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(endPoint, aliasMap);
		
		for ( TableEvent te : tblEvents ) {
			
			String entityElmIndex = Integer.toString(te.getIndex().get(0));
			List<AliasMapping> mappings = entityMappingTbl.get(entityElmIndex);
			if ( mappings == null ) {				
				mappings = new ArrayList<>();
				entityMappingTbl.put(entityElmIndex, mappings);
			}
			AliasMapping am = new AliasMapping();
			am.logicalIndex = Integer.toString(te.getIndex().get(1));
			am.mappingIndex = te.getColumns()[0].getVariable().toString();
			
			mappings.add(am);
		}
				
		OID[] entityColumns =  EntitySNMPInfo.getEntityInfoInstance().getColumnOids();
		
		topEntity = null;
		tblEvents = SnmpService.instance().getTablePdu(endPoint, entityColumns);
		for ( TableEvent te : tblEvents ) {
			
			PhysEntityRow er = new PhysEntityRow(te);
			String containIndex = Integer.toString(er.getEntPhysicalContainedIn());
			
			List<PhysEntityRow> physRows = physRowMap.get(containIndex);
			if ( physRows == null ) {
				physRows = new ArrayList<>();
				physRowMap.put(containIndex, physRows);
			}
			if ( er.getEntPhysicalContainedIn() == 0 ) {
				topEntity = er;
			}
			
			physRows.add(er);	
			
			/*
			if ( er.getEntPhysicalContainedIn() == 0 ) {
				topEntity = er;
				
				EntityElement ee = new EntityElement();
				ee.index = er.getIndex();
				ee.parentIndex = Integer.toString(er.getEntPhysicalContainedIn());
				
				
				ee.serviceElement = discNode.getAccessElement();
				ee.serviceElement.setName(er.getEntPhysicalDescr());
				
				elmMap.put(te.getIndex().toString(), ee);
			}
			else {
				
				String containIndex = Integer.toString(er.getEntPhysicalContainedIn());
				
				List<PhysEntityRow> physRows = physRowMap.get(containIndex);
				if ( physRows == null ) {
					physRows = new ArrayList<>();
					physRowMap.put(containIndex, physRows);
				}
				physRows.add(er);				
			}	
			*/		
		}
		
		if ( snmpContainment.getSnmpLevels() != null && snmpContainment.getSnmpLevels().size() > 0 ) {
			recursiveDiscovery(topEntity, snmpContainment.getSnmpLevels());				
		}
		else {
			recursiveDiscovery(topEntity, null);
		}	
		return discNode.getAccessElement();
	}
	
	
	/**
	 * Do a recursive discovery based on a physical entity row. 
	 * Continue with each child of the row until there is no child contains for a row.
	 *
	 * @param row the row
	 * @param levelOid the level oid
	 */
	private void recursiveDiscovery( PhysEntityRow row,  List<SnmpLevelOID> levelOids ) {

		SnmpLevelOID pickLevelOid = null;
		if ( row.getEntityClass() != EntityClassEnum.other ) {
			/**
			 * Check if an associated service element being discovered for current Physical Entity row or not.
			 */
			EntityElement ee = elmMap.get(row.getIndex());
			ServiceElementType set = null;
			
			/**
			 * If EntityElement being create for this row index, skip the service element creation part.
			 * Else create  service element.
			 */
			if ( ee == null ) {
			
				ServiceElement se = null;
				CategoryTypeEnum ce = convertEntityClassType(row.getEntityClass());
				try {
					
					/**
					 * If passing SnmpLevelOID is not null, find the ServiceElementType from SnmpLevelOid.
					 */
					if ( levelOids != null ) {
						
						for ( SnmpLevelOID levelOid : levelOids ) {
							
							SNMP descriminator = levelOid.getDescriminatorOID();
							Object descriminatorVal = row.getValueByOid(descriminator.getOid());
							
							for ( SnmpServiceElementTypeDiscriminator ssetd : levelOid.getDisriminators() ) {
								
								if ( ssetd.getDiscriminatorValue().getValue().toString().equals(descriminatorVal.toString()) ) {
									
									logger.info("Find ServiceElementType with SNMP " + descriminator.getName() );
									set = discMgr.getServiceElementTypeById(ssetd.getServiceElementTypeId());
									
									pickLevelOid = levelOid;
									break;
								}
							}
						}
					}
					/**
					 * 
					 */
					if ( set == null ) {
						
						/**
						 * Check for if there is one in the database already exist.  If it is use it.
						 * Else create a serviceElementType associated with the current entity row. 
						 */
						ServiceElementType[] sets =  discMgr.getServiceElementTypesByCategoryAndVendor(ce, discNode.getTopServiceElementType().getVendor());
						if ( sets != null && sets.length > 0 ) {
							
							for ( ServiceElementType tmpSet : sets ) {
								
								if ( findServiceElementTypeMatch(tmpSet, row) ) {
									
									logger.info("Find a match ServiceElementType with SNMP " + row.getEntPhysicalVendorType() );
									set = tmpSet;
									break;
								}
							}
						}
					}
					/**
					 * If ServiceElementType is still null, create one. 
					 */
					if ( set == null ) {
						set = createServiceElementType(row);
					}
					se = createAndDiscoverServiceElement(set, row);
					
				} catch (IntegerException e) {
					
					logger.info("Exception to retrieve service element type. " + e.getLocalizedMessage());
					return;
				}
				
				ee = new EntityElement();
				ee.index = row.getIndex();
				ee.serviceElement = se;
				
				ee.parentIndex = Integer.toString(row.getEntPhysicalContainedIn());
				
				/** 
				 * Create a service element and associated with it parent service element.
				 */
				elmMap.put(row.getIndex(), ee);
				/**
				 * Discover more detail for that service element.
				 */
				try {
					
					String tblOid = getTableOidFromVBOid(CommonSnmpOids.entPhysicalClass);
					Map<String, TableRowIndex> tblInstMap = new HashMap<>();
					
					TableRowIndex trIndex = new TableRowIndex(tblOid, row.getIndex());
					tblInstMap.put(tblOid, trIndex);	
					
					discoverServiceElementAttribute(discNode.getElementEndPoint(), ee.serviceElement, set, tblInstMap );
					
				} catch (IntegerException e2) {
					e2.printStackTrace();
				}
				/**
				 * Associate this service element with its parant service element.
				 */
				if ( row.getEntPhysicalContainedIn() != 0 ) {
				
					EntityElement parentEE = elmMap.get( Integer.toString(row.getEntPhysicalContainedIn() ));
					ServiceElement pse = parentEE.serviceElement;
					
					if ( pse == null ) {
						
						parentEE = elmMap.get(parentEE.parentIndex);
						pse = parentEE.serviceElement;
					}
					
					if ( pse != null ) {
						se.setParentId(pse.getID());
						System.out.println(" Parent element <" + pse.getName() + ">" );
					}
					else {
						se.setParentId(discNode.getAccessElement().getID());
						logger.info( " No direct parent for **************************** " + row.getEntPhysicalName() 
								   + " " + row.getIndex() + " " + row.getEntPhysicalContainedIn() + " indirect parent name " + discNode.getAccessElement().getName() );
					}
				}
				
				/**
				 * If it is a physical port, create If service element on that port.
				 */
				if ( row.getEntityClass() == EntityClassEnum.port ) {
				
					 List<AliasMapping> mapps = entityMappingTbl.get(row.getIndex());
					 if ( mapps != null ) {
						 
						 for ( AliasMapping mapp : mapps ) {
							 
							 PDU p = new PDU();
							 /**
							  * Get the ifIndex from the alias mapping index.
							  */
							 OID o = new OID(mapp.mappingIndex);
							 int ifIndex = o.get(o.size() - 1);
							 
							 o = new OID( CommonSnmpOids.ifType);
							 String tblOid = null;
							 
							 try {
								tblOid = getTableOidFromVBOid(CommonSnmpOids.ifType);
							 } catch (IntegerException e1) {}
							 o.append(ifIndex);
							 p.add(new VariableBinding(o));
							 
							 o = new OID((CommonSnmpOids.ifDescr));
							 o.append(ifIndex);
							 p.add(new VariableBinding(o));
							 
							 /**
							  * Store ifTable instance oid for later used.
							  */
							 Map<String, TableRowIndex> tblInstMap = new HashMap<>();
							 TableRowIndex trIndex = new TableRowIndex(tblOid, Integer.toString(ifIndex));
							 tblInstMap.put(tblOid, trIndex);
							 
							 try {								 
								 PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), p);
								 
								 ServiceElementType[] sets = discMgr.getServiceElementTypesByCategoryAndVendor(CategoryTypeEnum.portIf, 
										                             discNode.getTopServiceElementType().getVendor() );
								 
								 String ifSubType = row.getEntPhysicalVendorType() + ":" + rpdu.get(0).getVariable().toInt();
								 
								 ServiceElementType iset = null;
								 if ( sets != null ) {
									 
									 for ( ServiceElementType s : sets ) {
										 
										 if ( ifSubType.equals(s.getVendorSpecificSubType()) ) {
											 iset = s;
											 break;
										 }
									 }									 
								 }
								 ServiceElement ise = new ServiceElement();
								 if ( iset == null ) {
									 iset = new ServiceElementType();
									 iset.setCategory(CategoryTypeEnum.portIf);
									 iset.addSignatureValue(null, SignatureTypeEnum.Vendor, discNode.getTopServiceElementType().getVendor());
									 iset.setVendorSpecificSubType(ifSubType);
									 
									 List<ID> attributes = new ArrayList<>();
									 iset.setAttributeIds(attributes);
									 
									 SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifDescr);
									 iset.getAttributeIds().add(snmp.getID());
									 
									 snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifMtu);
									 iset.getAttributeIds().add(snmp.getID());
									 
									 snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifPhysAddress);
									 iset.getAttributeIds().add(snmp.getID());
									 
									 snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifSpeed);
									 iset.getAttributeIds().add(snmp.getID());
									 
									 snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.ifType);
									 iset.getAttributeIds().add(snmp.getID());
									 
									 iset = capMgr.updateServiceElementType(iset);
								 }

								 logger.info("ServiceElement and ServiceElemenType identifier " + se.getIdentifier() + " * " + iset.getIdentifier() + " " + iset.getVendorSpecificSubType());
								 
								 ise.setParentId(se.getID());
								 ise.setServiceElementTypeId(iset.getID());
								 ise.setName(se.getName() + ":If" + mapp.logicalIndex);
								 
								 if ( discNode.getExistingSE() != null ) {
									 ise.setCreated(discNode.getExistingSE().getCreated());
								 }
								 else {
									 ise.setCreated(new Date());
								 }
								 ise.setUpdated(new Date());
								 ise.setDescription(rpdu.get(1).getVariable().toString());
								 
								 discoverServiceElementAttribute(discNode.getElementEndPoint(), ise, iset, tblInstMap);
								 
								 /**
								  * Create service element in the database.
								  */
								 ise = accessMgr.updateServiceElement(ise);
								 logger.info("Create If element " + " ifIndex:" + ifIndex + " Descr:" + rpdu.get(0).getVariable().toString() + " on Parent:" + se.getName());
								 
							 } catch (IntegerException e) {
								 
								 logger.warn("Exception on create port if element " + e.getMessage());
							}
						 }
					 }
					 
				}
			}
		}	
		else {
			
			/**
			 * Integer does not model entity class "container".  Just create an entity element with no service element associate with it.
			 */
			EntityElement ee = new EntityElement();
			ee.index = row.getIndex();
			ee.parentIndex = Integer.toString(row.getEntPhysicalContainedIn());
			
			/** 
			 * Create a service element and associated with it parent service element.
			 */
			elmMap.put(row.getIndex(), ee);
			
		}
		
		List<PhysEntityRow>  containRows = physRowMap.get(row.getIndex());
		if ( containRows == null ) {
			/**
			 * Stop here because there is no sub component for the current row.
			 */
			return;			
		}
		for ( PhysEntityRow pr : containRows ) {
		
			SnmpLevelOID levelOID = null;
			/**
			 * Pick the right SNMP oid for each sub-children if levelOid is not empty.
			 */
			if ( pickLevelOid != null &&  pickLevelOid.getChildren() != null ) {
				
				List<SnmpLevelOID> snmpLevels = pickLevelOid.getChildren();	
				/**
				 * Empty is allowed in that case it will create service element type base on default type.
				 */
				if ( snmpLevels != null ) {
					
					for ( SnmpLevelOID so : snmpLevels ) {
						SNMP oid = so.getContextOID();
						
						Object v = pr.getValueByOid(oid.getOid());
						for ( SnmpServiceElementTypeDiscriminator sst : so.getDisriminators() ) {
							
							if ( v.toString().equals(sst.getDiscriminatorValue().getValue().toString()) ) {
								levelOID = so;
								break;
							}
						}
					}					
				}
			}	   
			if ( levelOID != null ) {
				recursiveDiscovery(pr, levelOID.getChildren());
			}
			else {
			    recursiveDiscovery(pr, null);
			}
		}		
	}
	
	
	/**
	 * Convert entity class type.
	 *
	 * @param e the e
	 * @return the category type enum
	 */
	public static CategoryTypeEnum convertEntityClassType( EntityClassEnum e ) {
		
		switch (e) {
		
		   case backplane:
			   return CategoryTypeEnum.backplane;
			
		   case  chassis: 
			   return CategoryTypeEnum.chassis;
			   
		   case cpu:
			   return CategoryTypeEnum.cpu;
			   
		   case module:
			   return CategoryTypeEnum.module;
			   
		   case port:
			   return CategoryTypeEnum.port;
				 
		   case sensor:
			   return CategoryTypeEnum.sensor;
			   
		   case fan:
			   return CategoryTypeEnum.fan;
			   
		   case powerSupply:
			   return CategoryTypeEnum.powerSupply;
			   
		   case stack:
			   return CategoryTypeEnum.stack;

		    default:
			   break;
		}
		
		return CategoryTypeEnum.other;
	}
	
	
	
	/**
	 * Define model name.
	 *
	 * @param relIndex the rel index
	 * @param e the e
	 * @param pe the pe
	 * @return the string
	 */
	public static String defineModelName( int relIndex, EntityClassEnum e, PhysEntityRow pe ) 
	{
		switch ( e ) {
		
		    case backplane:			
		    	return e.name();
			   
		    case fan:
		    case module:
		    case port:
		    case powerSupply:
		    case sensor:
		    case cpu:
		    	return e.name() + " " +relIndex;

		    default:
			   break;
		}
		
		return pe.getEntPhysicalName();
	}
	
	
	/**
	 * The Class AliasMapping.
	 */
	public class AliasMapping {
		
		/** The logical index. */
		String logicalIndex;
		
		/** The mapping index. */
		String mappingIndex;
	}
	
	
	/**
	 * The Class EntityElement.
	 */
	public class EntityElement {
		
		/** The index. */
		String index;
		
		/** The parent index. */
		String parentIndex;
		
		/** The service element. */
		ServiceElement serviceElement;
	}
	
	
	
	/**
	 * Find service element type match.
	 *
	 * @param set the set
	 * @param pr the pr
	 * @return true, if successful
	 */
	public boolean findServiceElementTypeMatch( ServiceElementType set, PhysEntityRow pr ) {
	
		/**
		 * Only module need to check for version information.
		 */
		if ( pr.getEntityClass() == EntityClassEnum.module || pr.getEntityClass() == EntityClassEnum.chassis ) {
			
			if ( pr.getEntPhysicalSoftwareRev() != null )
			{
				if ( !set.getSoftware().equals(pr.getEntPhysicalSoftwareRev()) ) {
					return false;
				}
			}
			if ( pr.getEntPhysicalFirmwareRev() != null ) {
				
				if ( !set.getFirmware().equals(pr.getEntPhysicalFirmwareRev()) ) {
					return false;
				}
			}
		}
		
		if ( pr.getEntPhysicalVendorType().equals(set.getVendorSpecificSubType()) ) {
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Create service element type from a physical entity row.
	 *
	 * @param pr the pr
	 * @return the service element type
	 * @throws IntegerException 
	 */
	public ServiceElementType createServiceElementType( PhysEntityRow pr ) throws IntegerException {
		
		ServiceElementType set = new ServiceElementType();
		set.setCategory(convertEntityClassType(pr.getEntityClass()));
		
		set.setVendorSpecificSubType(pr.getEntPhysicalVendorType());
		set.setDescription(pr.getEntPhysicalDescr());
		
		set.addSignatureValue(null, SignatureTypeEnum.Vendor, discNode.getTopServiceElementType().getVendor());
		set.addSignatureValue(null, SignatureTypeEnum.Firmware, pr.getEntPhysicalFirmwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.SoftwareVersion, pr.getEntPhysicalSoftwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.Model, pr.getEntPhysicalModelName());
		
		logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& create an service element type " + pr.getEntPhysicalVendorType());
		
		if ( pr.isEntPhysicalIsFRU() ) {
			set.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);
		}
		else {
			set.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
		}

		try {
			
			List<ID> attributeIds = new ArrayList<>();
			if ( pr.getEntityClass() == EntityClassEnum.module || pr.getEntityClass() == EntityClassEnum.chassis ) {
				SNMP snmp = snmpMgr.getSNMPByOid(CommonSnmpOids.entPhysicalSerialNum);
				attributeIds.add(snmp.getID());
			}	
			set.setApplicabilities(attributeIds);;
		}
		catch ( IntegerException ie ) {
			logger.warn("Fail to add attribute on ServiceElementType");
		}	
		set = capMgr.updateServiceElementType(set);
		return set;
	}
	
	
	/**
	 * Create and discovery more detail of a Service Element.
	 *
	 * @param set the set
	 * @param row the row
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public ServiceElement createAndDiscoverServiceElement( ServiceElementType set, PhysEntityRow row ) throws IntegerException {
		
		ServiceElement se = new ServiceElement();
		
		if ( discNode.getExistingSE() == null ) {
			se.setCreated(new Date());
		}
		else {
			se.setCreated(discNode.getExistingSE().getCreated());
		}
		se.setUpdated(new Date());
		row.getEntPhysicalContainedIn();
		se.setDescription(row.getEntPhysicalDescr());
		
		if ( row.getEntPhysicalContainedIn() == 0 ) {
			se.setParentId(discNode.getAccessElement().getID());
		}
		else {

			EntityElement parentEE = elmMap.get( Integer.toString(row.getEntPhysicalContainedIn() ));
			se.setParentId(parentEE.serviceElement.getID());
		}
		
		se.setName(defineModelName(row.getEntPhysicalParentRelPos(), row.getEntityClass(), row));
		logger.info("Create Element <" + se.getName() + "> " + " entityClass:" + row.getEntityClass().name() 
				         + " Index:" + row.getIndex() + " VendorType:" + row.getEntPhysicalVendorType()  + " PhysicalName: " + row.getEntPhysicalName());
		
	    se.setServiceElementTypeId(set.getID());
	    
	    String tblOid = getTableOidFromVBOid(CommonSnmpOids.entPhysicalClass);	    
		Map<String, TableRowIndex> tblInstMap = new HashMap<>();
		
		TableRowIndex trIndex = new TableRowIndex(tblOid, row.getIndex());
		tblInstMap.put(tblOid, trIndex);			
	    /**
	     * Discover more detail for that service element.
	     */
		findUIDForServiceElement(set, se, discNode.getElementEndPoint());
	    discoverServiceElementAttribute(discNode.getElementEndPoint(), se, set, tblInstMap );
	    
	    /**
	     * Create service element in the database.
	     */
	    se = accessMgr.updateServiceElement(se);
	    
	    return se;
	}
	
}

