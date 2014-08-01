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
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpParentChildRelationship;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDescriminatorIntegerValue;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorStringValue;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorValue;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MaxAccess;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Signature;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;

/**
 * The Class ParentChildServiceElementDiscovery is used to discover IP nodes which their
 * component structure in parent and child containment relationship presenting in SnmpParentChildRelationship
 * class. One of the example is devices using Entity MIB to layout components.
 * In this type of structure, the parent and child relationship for all components is contained in a column of
 * an SNMP table. To discover all the components and the containment relationship between them, this class provide
 * a recursive method to discover each layer of components.
 * 
 *
 * @author dchan
 */
public class ParentChildServiceElementDiscovery extends
		SnmpServiceElementDiscover {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(EntityMibServiceElementDiscovery.class);
    
	/** The discovery node. */
	private DiscoverNode discNode;
	
	/** The containment class which the child context OID and which SNMP object holds the relation. */
	private SnmpContainment containment;
	
	/** The top entity. */
	private ParentChildRelationNode topEntity;
	
	/** The map to hold each physical entity row. The key is the parent index.  The value is a list physical rows which contains by the
	 * parent which index is the "key"
	 */
	private Map<String, List<ParentChildRelationNode>>  physRowMap; 
	
	/**  The element map uses to keep track of entity element discovered so far. */
	private Map<String, EntityElement> elmMap; 
	
	
	
	/**
	 * Instantiates a new parent child service element discovery.
	 *
	 * @throws IntegerException the integer exception
	 */
	public ParentChildServiceElementDiscovery() throws IntegerException {
		super();
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover#discover(edu.harvard.integer.common.discovery.SnmpContainment, edu.harvard.integer.service.discovery.subnet.DiscoverNode)
	 */
	@Override
	public ServiceElement discover(SnmpContainment sc, DiscoverNode discNode)
			throws IntegerException {
		
		this.containment = sc;
		this.discNode = discNode;
		elmMap = new HashMap<>();
		physRowMap = new HashMap<String, List<ParentChildRelationNode>>();		
		for ( SnmpLevelOID snmpLevel : sc.getSnmpLevels() ) {
			
			try {
				if ( snmpLevel.getCategory() != null && discNode.getTopServiceElementType().getCategory().getName().equals(snmpLevel.getCategory().getName()) ) {
					
					SNMP contextSnmp = snmpLevel.getContextOID();
					if ( contextSnmp.getScalarVB() == null || !contextSnmp.getScalarVB() ) {
					
						if ( snmpLevel.getRelationToParent() == null ) {	
							
							levelDiscovery(snmpLevel, discNode.getTopServiceElementType(), 
								           discNode.getAccessElement(), null, null, null, discNode);
							
							/*
							if ( snmpLevel.getDescriminatorOID() == null ) {
						
								SnmpServiceElementTypeDiscriminator ssetd = snmpLevel.getDisriminators().get(0);
								ServiceElementType set = discMgr.getServiceElementTypeById(ssetd.getServiceElementTypeId());
								
								List<ID> ids = set.getAttributeIds();
								List<SNMP> accessSnmps = new ArrayList<>();
								List<SNMP> nonAccessSnmps = new ArrayList<>();
								
								for ( ID id : ids ) {
									
									SNMP s = (SNMP) capMgr.getManagementObjectById(id);
									if ( s.getMaxAccess() == null || s.getMaxAccess() == MaxAccess.NotAccessible ) {
										nonAccessSnmps.add(s);
									}
									else {
										accessSnmps.add(s);
									}
								}
								
								OID[] oidColumns = new OID[accessSnmps.size()];
								for ( int i=0; i<accessSnmps.size(); i++ ) {
									
									SNMP s = accessSnmps.get(i);
									oidColumns[i] = new OID(s.getOid());
								}
								
								List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(discNode.getElementEndPoint(), oidColumns);
								for ( TableEvent tblEvent : tblEvents ) {
									
									List<IndexSNMPValue> indexVals = new ArrayList<>();
									if ( nonAccessSnmps.size() > 0 ) {
										
										SNMPTable snmpTbl = (SNMPTable) contextSnmp;
										List<SNMP> indexs =  snmpTbl.getIndex();
										for ( SNMP ss : nonAccessSnmps ) {
											
											//
											// If index size is 1, we know the whole part of table index is
											// the non access SNMP value.
											//
											if ( indexs.size() == 1 ) {
												SNMP is = indexs.get(0);
												if ( is.getOid().equals(ss.getOid()) ) {
												
													IndexSNMPValue indexVal = new IndexSNMPValue();
													indexVal.indexSNMP = is;
													indexVal.indexVal = tblEvent.getIndex().toString();
													
													indexVals.add(indexVal);
													
												}
											}
											else {
												
												for ( int j=0; j<indexs.size(); j++ ) {
													
													SNMP is = indexs.get(j);
													if ( is.getOid().equals(ss.getOid()) ) {
													
														IndexSNMPValue indexVal = new IndexSNMPValue();
														indexVal.indexSNMP = is;
														indexVal.indexVal =  Integer.toString(tblEvent.getIndex().get(j));
														indexVals.add(indexVal);
													}
												}
											}
										}
									}
									ServiceElement se = createServiceElementFromType(discNode, set, tblEvent, indexVals);
									se = updateServiceElement(se, set, discNode.getAccessElement());
								}
							}
							else {
								
								SNMP snmp = snmpLevel.getDescriminatorOID();
								OID[] oids = new OID[1];
								oids[0] = new OID(snmp.getOid());
								
								List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(discNode.getElementEndPoint(), oids);
								for ( TableEvent tblEvent : tblEvents ) {
									
									VariableBinding vb = tblEvent.getColumns()[0];
									SnmpServiceElementTypeDiscriminator ssetd = findDiscriminator(vb.getVariable(), snmpLevel);
									if ( ssetd != null ) {
										
										
									}
									else {
										
										ssetd = findDefaultDiscriminator(snmpLevel);
										if ( ssetd != null ) {
											
										}
									}									
								}
							}
							*/
						}
						
					}
					else {
					
						SnmpServiceElementTypeDiscriminator discriminator = snmpLevel.getDisriminators().get(0);
						
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, "0");						
						se = updateServiceElement(se, set, discNode.getAccessElement());
					}
				}
				else if ( snmpLevel.getRelationToParent() != null && snmpLevel.getRelationToParent() instanceof SnmpParentChildRelationship ) {
					
					/** 
					 * Retrieve all rows in a MIB table which contains the parent and child relation in once
					 * to save network requests and store them into the map.
					 */
					SnmpParentChildRelationship relation = (SnmpParentChildRelationship) snmpLevel.getRelationToParent();
					int count = 4;
					if ( relation.getCategoryOid() == null ) {
						count = 3;
					}
					
					OID[] entityColumns = new OID[count];
					entityColumns[0] = new OID(relation.getContainmentOid().getOid());
					entityColumns[1] = new OID(relation.getSubTypeOid().getOid());
					entityColumns[2] = new OID(relation.getSiblingOid().getOid());
					if ( relation.getCategoryOid() != null ) {
						entityColumns[3] = new OID(relation.getCategoryOid().toString());
					}
					
					topEntity = null;
					List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(discNode.getElementEndPoint(), entityColumns);
					for ( TableEvent te : tblEvents ) {
						
						ParentChildRelationNode pnode = new ParentChildRelationNode(te, relation, snmpLevel.getContextOID().getOid());
						String containIndex = pnode.getContainmentValue();
						
						List<ParentChildRelationNode> physRows = physRowMap.get(containIndex);
						if ( physRows == null ) {
							physRows = new ArrayList<>();
							physRowMap.put(containIndex, physRows);
						}
						if ( pnode.getContainmentValue() == null || "0".equals(pnode.getContainmentValue()) ) {
							topEntity = pnode;
						}					
						physRows.add(pnode);	
					}
					
					
					/**
					 * Call this method to discover components on each level.
					 */
					try {
					    recursiveDiscovery(topEntity);
					}
					catch ( Exception e ) {
						e.printStackTrace();
					}
				}
				
			}	
			catch ( IntegerException e ) {
				
				e.printStackTrace();
				throw e;
			}
		}		
		return discNode.getAccessElement();
	}


	/**
	 * Do a recursive discovery based on a physical entity row. 
	 * Continue with each child of the row until there is no child contains for a row.
	 *
	 * @param row the row
	 * @throws IntegerException the integer exception
	 */
	private void recursiveDiscovery( ParentChildRelationNode row ) throws IntegerException {
		
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
			
			ServiceElementType[] sets = null;
			VendorIdentifier vif = discMgr.getVendorIdentifier(row.getSubTypeValue());
			if ( vif != null ) {
				sets = discMgr.getServiceElementTypesBySubtypeAndVendor(vif.getVendorSubtypeName(), 
                                              discNode.getTopServiceElementType().getVendor());
			}
			
			if ( sets != null && sets.length == 1 ) {
				set = sets[0];
			}
			else if ( sets != null && sets.length > 0 ) {
				
				PDU pdu = new PDU();
				ServiceElementType tmpSet = sets[0];
				for ( int i=0; i<tmpSet.getSignatures().size(); i++ ) {
					
					Signature sig = tmpSet.getSignatures().get(i);
					SNMP mrgObj = (SNMP) capMgr.getManagementObjectById(sig.getSemoId());
					
					pdu.add(new VariableBinding(new OID(mrgObj.getOid() + "." + row.getIndex())));
				}
				PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), pdu);	
				set = signatureMatch(rpdu, sets);
			}
			if ( set == null ) {
				set = createServiceElementType(row);
			}
			
			if ( set != null ) {
				
				ServiceElement se = createAndDiscoverServiceElement(set, row, vif, discNode.getTopServiceElementType().getVendor());
				
				ee = new EntityElement();
				ee.index = row.getIndex();
				ee.serviceElement = se;
				
				ee.parentIndex = row.getContainmentValue();
				/** 
				 * Create a service element and associated with it parent service element.
				 */
				elmMap.put(row.getIndex(), ee);
				
				/**
				 * Scan through the top SnmpLevel list for this containment to find out if it contains
				 * any relation which is based on category and contextOID.  If it exists, continue to discover 
				 * the subcomponents related to the service element.
				 * 
				 * Example, port defined on EntityMIB has the SnmpContainmentRelation with interface.
				 * To discover interfaces which associated with a port, the containment should have a SnmpContainmentRelation
				 * to specify this relationship.
				 * 
				 * Note the current code is only handle the SnmpContainmentRelation.  Since this class is for general,
				 * it should handle more relation other than SnmpContainmentRelation.
				 * 
				 */
				for ( SnmpLevelOID levelOid : containment.getSnmpLevels() ) {
					
					if ( levelOid.getCategory() != null && levelOid.getCategory().getID().equals(set.getCategory().getID())  ) {
						levelDiscovery(levelOid, set, se, row.getContextOid(), row.getIndex(), null, discNode);
					}
				}
			}		
		}
		
		List<ParentChildRelationNode>  containRows = physRowMap.get(row.getIndex());
		if ( containRows == null ) {
			/**
			 * Stop here because there is no sub component for the current row.
			 */
			return;			
		}		
		for ( ParentChildRelationNode pr : containRows ) {		
			recursiveDiscovery(pr);
		}		
	}
	
	
	
	
	/**
	 * Signature match.  Need to add the implemenation later.
	 *
	 * @param rpdu the rpdu
	 * @param sets the sets
	 * @return the service element type
	 */
	private ServiceElementType signatureMatch( PDU rpdu, ServiceElementType[] sets ) {
		
		return sets[0];
	}
	
	
	/**
	 * Create and discovery more detail of a Service Element.  This is a special case
	 * for entity sub type service element construction.  Since the service element type
	 * defined is very generic and it based on entity class type.  If the service element
	 * name is based on service element type name, it is too generic.  It is better to based
	 * on entity sub type name.
	 *
	 * @param set the set
	 * @param row the row
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public ServiceElement createAndDiscoverServiceElement( ServiceElementType set, 
			                                               ParentChildRelationNode row,
			                                               VendorIdentifier vif,
			                                               String vendor ) throws IntegerException {
		

		String description = null;
		String seName = null;
		if ( vif != null && vif.getVendorSubtypeId() != null ) {
			
			OID o = new OID(vif.getVendorSubtypeId());
			o = o.trim();
			
			VendorIdentifier parentVif = discMgr.getVendorIdentifier(o.toString());
			int parentNameIndex = vif.getVendorSubtypeName().indexOf(parentVif.getVendorSubtypeName());
			
			if ( parentNameIndex == 0 ) {
			
				if ( row.getSiblingValue() == -1 ) {
					seName = vif.getVendorSubtypeName().substring(parentVif.getVendorSubtypeName().length());
				}
				else {
					String seNameType =  vif.getVendorSubtypeName().substring(parentVif.getVendorSubtypeName().length());
					if ( seNameType.equals("Unknown") ) {
						seNameType = set.getCategory().getName();
					}
					seName = seNameType + 	" " + row.getSiblingValue();
				}
				description = vendor + " " + vif.getVendorSubtypeName().substring(parentVif.getVendorSubtypeName().length()) + " " + set.getCategory().getName();
			}
			else {
				if ( row.getSiblingValue() == -1 ) {
					seName = vif.getVendorSubtypeName();
				}
				else {
					seName = vif.getVendorSubtypeName() + 	" " + row.getSiblingValue();
				}
				description = vendor + " " + vif.getVendorSubtypeName() + " " + set.getCategory().getName();
			}
		}
		else {
			seName = set.getName() + " " + row.getSiblingValue();
			if ( row.getSiblingValue() == -1 ) {
				seName = set.getName();
			}
			description = vendor + " " + set.getName() + " " + set.getCategory().getName();
		}
		
		ServiceElement se = new ServiceElement();
		se.setDescription(description);
		
		if ( discNode.getExistingSE() == null ) {
			se.setCreated(new Date());
		}
		else {
			se.setCreated(discNode.getExistingSE().getCreated());
		}
		se.setUpdated(new Date());
		ServiceElement parentSe = null;
		
		if ( row.getContainmentValue().equals("0")) {
			
			parentSe = discNode.getAccessElement();
		}
		else {

			parentSe = elmMap.get( row.getContainmentValue() ).serviceElement;
		}
		logger.info("Create Element <" + se.getName() + "> " + " entityClass:" + set.getCategory().getName() 
				         + " Index:" + row.getIndex() + " VendorType:" + row.getSubTypeValue() );
		
	    se.setServiceElementTypeId(set.getID());
	    se.setCategory(set.getCategory());
	    /**
	     * Discover more detail for that service element.
	     */
		findUIDForServiceElement(set, se, discNode.getElementEndPoint());
	    discoverServiceElementAttribute(discNode.getElementEndPoint(), se, set, row.getIndex() );
	    
	    if ( se.getName() == null ) {
			se.setName(seName);
	    }
	    
	    /**
	     * Create service element in the database.
	     */
	    se = updateServiceElement(se, set, parentSe);
	    return se;
	}
	
	
	
	/**
	 * Create and discovery more detail of a Service Element.
	 *
	 * @param set the set
	 * @param row the row
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public ServiceElement createAndDiscoverServiceElement( ServiceElementType set, ParentChildRelationNode row ) throws IntegerException {
		
		ServiceElement se = new ServiceElement();
		se.setCategory(set.getCategory());
		 
		if ( discNode.getExistingSE() == null ) {
			se.setCreated(new Date());
		}
		else {
			se.setCreated(discNode.getExistingSE().getCreated());
		}
		se.setUpdated(new Date());
	
		ServiceElement parentSe = null;
		
		if ( row.getContainmentValue().equals("0")) {
			
			parentSe = discNode.getAccessElement();
		}
		else {

			parentSe = elmMap.get( row.getContainmentValue() ).serviceElement;
		}
		logger.info("Create Element <" + se.getName() + "> " + " entityClass:" + set.getCategory().getName() 
				         + " Index:" + row.getIndex() + " VendorType:" + row.getSubTypeValue() );
		
	    se.setServiceElementTypeId(set.getID());	    
	    /**
	     * Discover more detail for that service element.
	     */
		findUIDForServiceElement(set, se, discNode.getElementEndPoint());
	    discoverServiceElementAttribute(discNode.getElementEndPoint(), se, set, row.getIndex() );
	    
	    if ( se.getName() == null ) {
	    	
			String seName = set.getName() + " " + row.getSiblingValue();
			if ( row.getSiblingValue() == -1 ) {
				seName = set.getName();
			}
			se.setName(seName);
	    }
	    
	    
	    /**
	     * Create service element in the database.
	     */
	    se = updateServiceElement(se, set, parentSe);
	    return se;
	}
	
	
	/**
	 * Create service element type from a physical entity row.
	 *
	 * @param row the row
	 * @return the service element type
	 * @throws IntegerException the integer exception
	 */
	public ServiceElementType createServiceElementType( ParentChildRelationNode row ) throws IntegerException {
		
		PDU pdu = new PDU();
		OID[] entityColumns =  EntitySNMPInfo.getEntityInfoInstance().getColumnOids();
		for ( OID o : entityColumns ) {		
			
			OID co = new OID(o);
			co.append(row.getIndex());
			pdu.add(new VariableBinding(co));
		}
		
		PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), pdu);
		PhysEntityRow pr = new PhysEntityRow(rpdu, row.getIndex());
		
		ServiceElementType set = new ServiceElementType();
		ManagementObjectCapabilityManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
		set.setCategory(manager.getCategoryByName(pr.getEntityClass().name()));
		
		set.setVendorSpecificSubType(pr.getEntPhysicalVendorType());
		set.addSignatureValue(null, SignatureTypeEnum.Vendor, discNode.getTopServiceElementType().getVendor());
		set.addSignatureValue(null, SignatureTypeEnum.Firmware, pr.getEntPhysicalFirmwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.SoftwareVersion, pr.getEntPhysicalSoftwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.Model, pr.getEntPhysicalModelName());
		
		VendorIdentifier vendorIdent = discMgr.getVendorIdentifier(pr.getEntPhysicalVendorType());
		if ( vendorIdent != null ) {
			
			if ( vendorIdent.getVendorSubtypeName() != null ) {
				
				set.setName(vendorIdent.getVendorSubtypeName());
			}
			else {
				set.setName(pr.getEntPhysicalVendorType());
			}
		}
		else {
			set.setName(pr.getEntPhysicalVendorType());
		}
		set.setDescription(pr.getEntityClass().name() + " " + set.getName());
		
		if ( pr.isEntPhysicalIsFRU() ) {
			set.setFieldReplaceableUnit(FieldReplaceableUnitEnum.Yes);
		}
		else {
			set.setFieldReplaceableUnit(FieldReplaceableUnitEnum.No);
		}

		try {
			
			List<ID> attributeIds = new ArrayList<>();
			List<SNMP> snmps = EntitySNMPInfo.getEntityInfoInstance().getEntityColumns();
			
			for ( SNMP snmp : snmps ) {
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
	
	
	private SnmpServiceElementTypeDiscriminator findDiscriminator( Variable v, SnmpLevelOID snmpLevel ) {
		
		for ( SnmpServiceElementTypeDiscriminator ssetd : snmpLevel.getDisriminators() ) {
			
			 SnmpServiceElementTypeDiscriminatorValue<?> ssetdv = ssetd.getDiscriminatorValue();
			 if ( ssetdv != null ) {
				  
				 if ( ssetdv instanceof SnmpServiceElementTypeDescriminatorIntegerValue ) {
					  
					 Integer iv = (Integer) ssetdv.getValue();
					 if ( v.toInt() == iv ) {
						  return ssetd;
					 }
				 }
				 else if ( ssetdv instanceof SnmpServiceElementTypeDiscriminatorStringValue ) {
					  
					 String sv = (String) ssetdv.getValue();
					 if ( sv.equals(v.toString()) ) {
						 return ssetd;
					 }
				 }
			 }			
		}
		return null;
	}
	
	
    private SnmpServiceElementTypeDiscriminator findDefaultDiscriminator( SnmpLevelOID snmpLevel ) {
		
		for ( SnmpServiceElementTypeDiscriminator ssetd : snmpLevel.getDisriminators() ) {
			
			SnmpServiceElementTypeDiscriminatorValue<?> ssetdv = ssetd.getDiscriminatorValue();
			if ( ssetdv == null ) {
				 return ssetd;
			}			 
		}
		return null;
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
}
