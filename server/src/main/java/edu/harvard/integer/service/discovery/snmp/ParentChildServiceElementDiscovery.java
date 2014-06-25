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

import edu.harvard.integer.access.snmp.ParentChildMappingIndex;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.RelationMappingTypeEnum;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpParentChildRelationship;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.FieldReplaceableUnitEnum;
import edu.harvard.integer.common.topology.NetworkLayer;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Signature;
import edu.harvard.integer.common.topology.SignatureTypeEnum;
import edu.harvard.integer.common.topology.TopologyElement;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * @author dchan
 *
 */
public class ParentChildServiceElementDiscovery extends
		SnmpServiceElementDiscover {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(EntityMibServiceElementDiscovery.class);
    
	/** The discovery node. */
	private DiscoverNode discNode;
	
	private SnmpContainment containment;
	
	/** The top entity. */
	private ParentChildRelationNode topEntity;
	
	/** The phyical entity row map. The key is the parent index.  The value is a list physical rows which contains by the
	 * parent
	 */
	private Map<String, List<ParentChildRelationNode>>  physRowMap; 
	
	/** The elm map uses to keep track of entity element discovered so far */
	private Map<String, EntityElement> elmMap; 
	
	
	
	/**
	 * @throws IntegerException
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
			
			if ( snmpLevel.getRelationToParent() instanceof SnmpParentChildRelationship ) {
				
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
				recursiveDiscovery(topEntity);
			}
			else {
				//
				//
				//
			}
		}		
		return discNode.getAccessElement();
	}


	/**
	 * Do a recursive discovery based on a physical entity row. 
	 * Continue with each child of the row until there is no child contains for a row.
	 *
	 * @param row the row
	 * @param levelOid the level oid
	 * @throws IntegerException 
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
				
				ServiceElement se = createAndDiscoverServiceElement(set, row);
				
				ee = new EntityElement();
				ee.index = row.getIndex();
				ee.serviceElement = se;
				
				ee.parentIndex = row.getContainmentValue();
				/** 
				 * Create a service element and associated with it parent service element.
				 */
				elmMap.put(row.getIndex(), ee);
				
				for ( SnmpLevelOID levelOid : containment.getSnmpLevels() ) {
					
					if ( levelOid.getCategory() != null && levelOid.getCategory() == set.getCategory()  ) {
						
						if ( levelOid.getRelationToParent() != null ) {
							
							if ( levelOid.getRelationToParent() instanceof SnmpContainmentRelation ) {
								
								SnmpContainmentRelation sRelation = (SnmpContainmentRelation) levelOid.getRelationToParent();
								List<ParentChildMappingIndex> indexTable = getIndexMapping(sRelation.getMappingTable().getOid());
								if ( indexTable == null ) {
									
									indexTable = new ArrayList<>();
									
									OID[] aliasMap = new OID[1];
									aliasMap[0] = new OID(sRelation.getMappingOid().getOid());
									List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(discNode.getElementEndPoint(), aliasMap);
									
									for ( TableEvent te : tblEvents ) {
										
										String mappingTblIndex = te.getIndex().toString();
										String childIndex = te.getColumns()[0].getVariable().toString();
										
										ParentChildMappingIndex pcmi = new ParentChildMappingIndex(mappingTblIndex, sRelation.getMappingType());
										pcmi.setChildIndex(childIndex);
										
										indexTable.add(pcmi);
									}
									addIndexMapping(sRelation.getMappingTable().getOid(), indexTable);									
								}								
							    for ( SnmpServiceElementTypeDiscriminator disc : levelOid.getDisriminators() ) {
							    
							    	ServiceElementType relType = discMgr.getServiceElementTypeById(disc.getServiceElementTypeId());
							        List<SNMP> indexSnmps = sRelation.getMappingTable().getIndex();	
							    	
							        int indexLocation = 0;
							        for ( SNMP snmp : indexSnmps ) {
							        	
							        	OID contextO = new OID(row.getContextOid());
							        	OID indexO = new OID(snmp.getOid());
							        	
							        	if ( indexO.startsWith(contextO) ) {
							        		break;
							        	}							        	
							        	indexLocation++;
							        }
							        
							        ParentChildMappingIndex pcmi = findMappingIndex(indexTable, row.getIndex(), indexLocation);
							        if ( pcmi != null ) {
							        	
							        	String instOid = pcmi.getChildIndex();
							        	if ( pcmi.getMappingType() == RelationMappingTypeEnum.FullOid ) {
							        		
							        		OID fullOid = new OID(pcmi.getChildIndex());
							        		OID contextOID = new OID(levelOid.getContextOID().getOid());
							        		/*
							        		 * If contextOID is table entry, the size of attribute needs to include attribute oid.
							        		 */
							        		int attrOidSize = contextOID.size();
							        		if ( contextOID.get(contextOID.size() -1) == 1 ) {
							        			attrOidSize++;
							        		}
							        		
							        		int diff = fullOid.size() - attrOidSize;
							        		int[] instOidi = new int[diff];
							        		
							        		indexLocation = 0;
							        		for ( int i=attrOidSize;i<fullOid.size(); i++ ) {
							        			
							        			instOidi[indexLocation] = fullOid.get(i);
							        		}
							        		OID io = new OID(instOidi);
							        		instOid = io.toString();
							        	}
							        	ServiceElement relTypese =  createServiceElementFromType(discNode, relType, instOid, se);
							        	if ( relTypese.getName() == null ) {
							        		
							        		String rIndex = row.getIndex();
							        		String pIndex = pcmi.getParentIndex();
							        		
							        		OID ro = new OID(rIndex);
							        		OID po = new OID(pIndex);
							        		
							        		if ( po.size() > ro.size() ) {
							        			
							        			int diffs = po.size() - ro.size();
							        			int[] diffi = new int[diffs];
							        			
							        			OID o = new OID(diffi);
							        			relTypese.setName(relType.getCategory().name() + " " + o.toString());
							        		}
							        		else {
							        			relTypese.setName(relType.getCategory().name() + " " + po.toString());
							        		}
							        		
							        		System.out.println("ParentIndex " + pIndex + " " + rIndex);
							        	}
							        	
							        	relTypese = accessMgr.updateServiceElement(relTypese);
							        	for ( SnmpLevelOID subLevel : containment.getSnmpLevels() ) {
							        	
							        		if ( subLevel.getCategory() != null && subLevel.getCategory() == relType.getCategory()  ) {
							        			
							        			if ( subLevel.getRelationToParent() == null ) {
							        				
							        				SNMPTable snmpTbl = (SNMPTable) subLevel.getContextOID();
							        				System.out.println("Table name " + snmpTbl.getName());
							        			}
							        		}
							        	}							        	
							        	if ( relType.getCategory() == CategoryTypeEnum.portIf ) {
							        		SNMPTable snmpTbl = (SNMPTable) snmpMgr.getSNMPByName("ipAddrEntry");
							        		SNMP ifAttr = snmpMgr.getSNMPByName("ipAdEntIfIndex");
							        		
							        		List<ParentChildMappingIndex> idxTable = getIndexMapping(snmpTbl.getOid());
							        		if ( idxTable == null ) {
												
												idxTable = new ArrayList<>();
												
												OID[] aliasMap = new OID[1];
												aliasMap[0] = new OID(ifAttr.getOid());
												List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(discNode.getElementEndPoint(), aliasMap);
												
												for ( TableEvent te : tblEvents ) {
													
													String mappingTblIndex = te.getColumns()[0].getVariable().toString();
													String childIndex = te.getIndex().toString();
													
													pcmi = new ParentChildMappingIndex(mappingTblIndex, RelationMappingTypeEnum.InstanceOnly);
													pcmi.setChildIndex(childIndex);
													
													idxTable.add(pcmi);
												}
												addIndexMapping(snmpTbl.getOid(), idxTable);									
											}
							        		for ( ParentChildMappingIndex p : idxTable ) {
							        			
							        			if ( instOid.equals(p.getParentIndex() ) ) {
							        		
							        				ServiceElementType addrSet = discMgr.getServiceElementTypeByName("ipv4Address");
							        				ServiceElement addrSe =  createServiceElementFromType(discNode, addrSet, p.getChildIndex(), relTypese);
							        				if ( addrSe.getName() == null ) {
							        					addrSe.setName(addrSet.getCategory().name() + " " + p.getChildIndex());
							        				}
                                                    if ( addrSet.getCategory() == CategoryTypeEnum.topology ) {
							        					addrSe.setNetworkLayer(NetworkLayer.Layer3);
							        				} 
							        				addrSe = accessMgr.updateServiceElement(addrSe);
							        				System.out.println("Child instance " + p.getChildIndex());
							        			}
							        		}
							        	}
							        	logger.info("Save service element " + relTypese.getName());
							        }
							        System.out.println("Number of index " + indexSnmps.size());
							    }
							}
						}
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
		
		if ( row.getIndex().equals("3000") ) {
			System.out.println("ContainRows " + containRows.size());
		}
		for ( ParentChildRelationNode pr : containRows ) {		
			recursiveDiscovery(pr);
		}		
	}
	
	
	private ServiceElementType signatureMatch( PDU rpdu, ServiceElementType[] sets ) {
		
		return sets[0];
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
		
		if ( discNode.getExistingSE() == null ) {
			se.setCreated(new Date());
		}
		else {
			se.setCreated(discNode.getExistingSE().getCreated());
		}
		se.setUpdated(new Date());
		if ( row.getContainmentValue().equals("0")) {
			se.setParentId(discNode.getAccessElement().getID());
		}
		else {

			EntityElement parentEE = elmMap.get( row.getContainmentValue());
			se.setParentId(parentEE.serviceElement.getID());
		}
		
		String seName = set.getCategory().name() + " " + row.getSiblingValue();
		if ( row.getSiblingValue() == -1 ) {
			seName = set.getCategory().name();
		}
		
		se.setName(seName);
		logger.info("Create Element <" + se.getName() + "> " + " entityClass:" + set.getCategory().name() 
				         + " Index:" + row.getIndex() + " VendorType:" + row.getSubTypeValue() );
		
	    se.setServiceElementTypeId(set.getID());
	    /**
	     * Discover more detail for that service element.
	     */
		findUIDForServiceElement(set, se, discNode.getElementEndPoint());
	    discoverServiceElementAttribute(discNode.getElementEndPoint(), se, set, row.getIndex() );
	    
	    /**
	     * Create service element in the database.
	     */
	    se = accessMgr.updateServiceElement(se);
	    return se;
	}
	
	
	/**
	 * Create service element type from a physical entity row.
	 *
	 * @param pr the pr
	 * @return the service element type
	 * @throws IntegerException 
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
		set.setCategory(convertEntityClassType(pr.getEntityClass()));
		
		set.setVendorSpecificSubType(pr.getEntPhysicalVendorType());
		set.setDescription(pr.getEntPhysicalDescr());
		
		set.addSignatureValue(null, SignatureTypeEnum.Vendor, discNode.getTopServiceElementType().getVendor());
		set.addSignatureValue(null, SignatureTypeEnum.Firmware, pr.getEntPhysicalFirmwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.SoftwareVersion, pr.getEntPhysicalSoftwareRev());
		set.addSignatureValue(null, SignatureTypeEnum.Model, pr.getEntPhysicalModelName());
		
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
	
	
	/**
	 * This method search for ParentChildMappingIndex from a list for which the "parentIndex" contains
	 * lookingInst in "indexPosition".  It is working well when indexPosition is 0.  Or each index instance 
	 * size is equal to 1. Other case we need to implement later.
	 * 
	 * @param mappingIndexList
	 * @param lookingInst
	 * @param indexPosition
	 * @return
	 */
	private ParentChildMappingIndex findMappingIndex(  List<ParentChildMappingIndex> mappingIndexList,
			                                                       String lookingInst, int indexPosition ) {
		
		if ( indexPosition == 0 ) {
			
			for ( ParentChildMappingIndex pcmi : mappingIndexList ) {
			
				OID po = new OID(pcmi.getParentIndex());
				OID lo = new OID(lookingInst);
				
				if ( po.startsWith(lo) ) {
					return pcmi;
				}				
			}			
		}
		else {
			for ( ParentChildMappingIndex pcmi : mappingIndexList ) {
				
				OID lo = new OID(lookingInst);
				OID po = new OID(pcmi.getParentIndex());
				
				boolean match = true;
				int startIndex = 0;
				for ( int i=indexPosition; i<lo.size(); i++ ) {
					
					if ( po.get(i) != lo.get(startIndex) ) {
						match = false;
						break;
					}
				}
				if ( match ) {
					return pcmi;
				}
			}
		}
		
		return null;
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
