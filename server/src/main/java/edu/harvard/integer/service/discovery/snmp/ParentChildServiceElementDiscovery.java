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
			
			if ( snmpLevel.getCategory() != null && discNode.getTopServiceElementType().getCategory() == snmpLevel.getCategory() ) {
				
				SNMP snmp = snmpLevel.getContextOID();
				if ( snmp.getScalarVB() != null && !snmp.getScalarVB() ) {
					
					/*
					 * We will handle the non-scalar case later on.
					 */
					if ( snmpLevel.getDescriminatorOID() != null ) {
						
						
					}
				}
				else {
				
					SnmpServiceElementTypeDiscriminator discriminator = snmpLevel.getDisriminators().get(0);
					
					ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
					ServiceElement se =  createServiceElementFromType(discNode, set, "0", discNode.getAccessElement());						
					se = accessMgr.updateServiceElement(se);
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
				recursiveDiscovery(topEntity);
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
				
				ServiceElement se = createAndDiscoverServiceElement(set, row);
				
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
					
					String globalDiscriminatorVal = null;
					if ( levelOid.getGlobalDiscriminatorOID() != null ) {
						
						SNMP snmp = levelOid.getGlobalDiscriminatorOID();
						
						PDU pdu = new PDU();
		        		pdu.add(new VariableBinding(new OID(snmp.getOid() + ".0")));
		        		PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), pdu);
		        		globalDiscriminatorVal = rpdu.get(0).getVariable().toString();
		        		
		        		if ( snmp.getTextualConvetion().equals("TruthValue")) {
		        			if ( globalDiscriminatorVal.equals("1")) {
		        				globalDiscriminatorVal = "true";
		        			}
		        			else {
		        				globalDiscriminatorVal = "false";
		        			}
		        		}
					}
					
					if ( levelOid.getCategory() != null && levelOid.getCategory().getID().equals(set.getCategory().getID())  ) {
						
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
							    
							    	/**
							    	 * Skip it if the global discriminator value is not match.
							    	 */
							    	if ( globalDiscriminatorVal != null ) {
							    		
							    		if ( !globalDiscriminatorVal.equals(disc.getGlobaldiscriminatorValue().getValue().toString()) ) {
							    			continue;
							    		}
							    	}
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
							        		OID contextOID = new OID(sRelation.getMappingContext().getOid());
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
							        	
							        	if ( instOid.equals("5179") ) {
							        		System.out.println("Stop in here ....");
							        	}
							        	
							        	String discrominatorValue = null;
							        	if ( levelOid.getDescriminatorOID() != null ) {
							        		
							        		try {
							        			SNMP snmp = levelOid.getDescriminatorOID();
								        		PDU pdu = new PDU();
								        		pdu.add(new VariableBinding(new OID(snmp.getOid() + "." + instOid)));
								        		PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), pdu);
								        		discrominatorValue = rpdu.get(0).getVariable().toString();
								        		if ( snmp.getTextualConvetion().equals("TruthValue")) {
								        			if ( discrominatorValue.equals("1")) {
								        				discrominatorValue = "true";
								        			}
								        			else {
								        				discrominatorValue = "false";
								        			}
								        		}
								        		
								        		if ( !discrominatorValue.equals(disc.getDiscriminatorValue().getValue().toString()) ) {
								        			continue;
								        		}
							        		}
							        		catch ( Exception e ) {
							        		
							        		}
							        	}
							        	
							        	ServiceElementType relType = discMgr.getServiceElementTypeById(disc.getServiceElementTypeId());
							        	ServiceElement relTypese =  null;
							        	try {
							        		relTypese = createServiceElementFromType(discNode, relType, instOid, se);
							        	}
							        	catch ( Exception e ) {
							        		continue;
							        	}
							        	
							        	if ( relTypese.getName() == null ) {
							        		
							        		String rIndex = row.getIndex();
							        		String pIndex = pcmi.getParentIndex();
							        		
							        		OID ro = new OID(rIndex);
							        		OID po = new OID(pIndex);
							        		
							        		if ( po.size() > ro.size() ) {
							        			
							        			int diffs = po.size() - ro.size();
							        			int[] diffi = new int[diffs];
							        			
							        			OID o = new OID(diffi);
							        			relTypese.setName(relType.getCategory().getName() + " " + o.toString());
							        		}
							        		else {
							        			relTypese.setName(relType.getCategory().getName() + " " + po.toString());
							        		}
							        	}
							        	
							        	relTypese = accessMgr.updateServiceElement(relTypese);
							        	for ( SnmpLevelOID subLevel : containment.getSnmpLevels() ) {
							        	
							        		if ( subLevel.getCategory() != null && subLevel.getCategory() == relType.getCategory()  ) {
							        			
							        			if ( subLevel.getRelationToParent() == null ) {
							        				
							        				SNMPTable snmpTbl = (SNMPTable) subLevel.getContextOID();
							        			}
							        		}
							        	}					
							        	/**
							        	 * The following related to interface and ipAddr is temporary.  
							        	 * Some how I cannot get the containment from yaml which contains that containment relationship.
							        	 * It needs to debug later.
							        	 */
							        	if ( relType.getCategory().getName().equals(CategoryTypeEnum.portIf.getName()) ) {
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
							        					addrSe.setName(addrSet.getCategory().getName() + " " + p.getChildIndex());
							        				}
                                                    if ( addrSet.getCategory().getName().equals( CategoryTypeEnum.topology.getName()) ) {
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
		
		String seName = set.getCategory().getName() + " " + row.getSiblingValue();
		if ( row.getSiblingValue() == -1 ) {
			seName = set.getCategory().getName();
		}
		
		se.setName(seName);
		logger.info("Create Element <" + se.getName() + "> " + " entityClass:" + set.getCategory().getName() 
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
	
	
	/**
	 * This method search for ParentChildMappingIndex from a list for which the "parentIndex" contains
	 * lookingInst in "indexPosition".  It is working well when indexPosition is 0.  Or each index instance 
	 * size is equal to 1. Other case we need to implement later.
	 *
	 * @param mappingIndexList the mapping index list
	 * @param lookingInst the looking inst
	 * @param indexPosition the index position
	 * @return the parent child mapping index
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
	
	
	
//	/**
//	 * Convert entity class type to Integer category.
//	 *
//	 * @param e the e
//	 * @return the category type enum
//	 */
//	public static CategoryTypeEnum convertEntityClassType( EntityClassEnum e ) {
//		
//		
//		switch (e) {
//		
//		   case backplane:
//			   return CategoryTypeEnum.backplane;
//			
//		   case  chassis: 
//			   return CategoryTypeEnum.chassis;
//			   
//		   case cpu:
//			   return CategoryTypeEnum.cpu;
//			   
//		   case module:
//			   return CategoryTypeEnum.module;
//			   
//		   case port:
//			   return CategoryTypeEnum.port;
//				 
//		   case sensor:
//			   return CategoryTypeEnum.sensor;
//			   
//		   case fan:
//			   return CategoryTypeEnum.fan;
//			   
//		   case powerSupply:
//			   return CategoryTypeEnum.powerSupply;
//			   
//		   case stack:
//			   return CategoryTypeEnum.stack;
//
//		    default:
//			   break;
//		}
//		return CategoryTypeEnum.other;
//	}
//	
//	
	
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
