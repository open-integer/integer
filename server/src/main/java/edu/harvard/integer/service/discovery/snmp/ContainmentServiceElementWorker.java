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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.RelationMappingTypeEnum;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpParentChildRelationship;
import edu.harvard.integer.common.discovery.SnmpRelationship;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPIndex;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementProtocolInstanceIdentifier;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;


/**
 * The Class ContainmentServiceElementWorker for discover device which the relation between components are 
 * in containment relationship.  
 * 
 * 
 *
 * @author dchan
 */
public class ContainmentServiceElementWorker extends SnmpServiceElementDiscover {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(ContainmentServiceElementWorker.class);
	
    /**
     * Relation mapping table cache for reducing network traffic during discovery.
     * The key of the map is the mapping table oid.
     */
    private ConcurrentHashMap<String, List<TableEvent>> relMapTblInfo = new ConcurrentHashMap<>();
    
    
    
    
	/**
	 * Instantiates a new containment service element worker.
	 *
	 * @throws IntegerException the integer exception
	 */
	public ContainmentServiceElementWorker() throws IntegerException {
		super();
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover#discover(edu.harvard.integer.common.discovery.SnmpContainment, edu.harvard.integer.service.discovery.subnet.DiscoverNode, edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface)
	 */
	@Override
	public ServiceElement discover(SnmpContainment sc, DiscoverNode discNode )
			throws IntegerException {
		
		logger.info("In ContainmentServiceElementWorker discover ");
		ElementEndPoint endPoint = discNode.getElementEndPoint();
		
		List<SnmpLevelOID> levelOids = sc.getSnmpLevels();
		for (SnmpLevelOID levelOid : levelOids) {

			SNMP doid = levelOid.getDescriminatorOID();
			OID[] oids = new OID[1];
	        oids[0] = new OID(doid.getOid());
	        
	        logger.info("get descriminator table " + doid.getOid());
	        	        
	        PDU pdu = new PDU();
	        VariableBinding vb = new VariableBinding(new OID(doid.getOid()));
	        pdu.add(vb);
	        
	        List<TableEvent> deviceEvents = SnmpService.instance().getTablePdu( endPoint, oids);
	        logger.info("Number of table event " + deviceEvents.size());
	        
			if (levelOid.getDisriminators() != null && levelOid.getDisriminators().size() > 1 ) {

				for (SnmpServiceElementTypeDiscriminator discriminator : levelOid.getDisriminators()) {

					List<TableEvent> tes = findTableEventRow(deviceEvents, doid.getOid(), discriminator.getDiscriminatorValue());
					for ( TableEvent te : tes ) {
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, te.getIndex().toString(), discNode.getAccessElement());						
						se = accessMgr.updateServiceElement(se);
					}
				}
			}
			else if ( levelOid.getDisriminators() != null && levelOid.getDisriminators().size() == 1 ) {
				
				SnmpServiceElementTypeDiscriminator discriminator = levelOid.getDisriminators().get(0);
				if ( discriminator.getDiscriminatorValue() != null ) {
					
					List<TableEvent> tes = findTableEventRow(deviceEvents, doid.getOid(), discriminator.getDiscriminatorValue());
					for ( TableEvent te : tes ) {
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, te.getIndex().toString(), discNode.getAccessElement());						
						se = accessMgr.updateServiceElement(se);
					}
				}
				else {
					
					ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
					logger.info("found this SET " + set.getCategory() + " " + set.getName());
					
					for ( TableEvent de : deviceEvents ) {
						
						ServiceElement se =  createServiceElementFromType(discNode, set, de.getIndex().toString(), discNode.getAccessElement());
						se = accessMgr.updateServiceElement(se);
					}
				}
			}
		}

		return discNode.getAccessElement();
	}

	
	
	
	
	
	/**
	 * Discover service element based on containment relationship.
	 *
	 * @param discNode the disc node
	 * @param levelOid the level oid
	 * @param parentSet the parent set
	 * @param parentSe the parent se
	 * @throws IntegerException the integer exception
	 */
	public void discoverByContainment( DiscoverNode discNode, SnmpLevelOID levelOid, 
			                           ServiceElementType parentSet,
			                           ServiceElement parentSe ) throws IntegerException  {

		List<SEAndSETPair> discoverComps = new ArrayList<>();
		
        SnmpRelationship rel = levelOid.getRelationToParent();
        if ( rel != null ) {
        	
        	if ( rel instanceof SnmpContainmentRelation ) {
        	
        		SnmpContainmentRelation contRel = (SnmpContainmentRelation) rel;       		
        		List<TableEvent> tblevents = relMapTblInfo.get(contRel.getMappingTable().getOid());
        		
        		if ( tblevents == null ) {
        			
        			OID[] oids = new OID[1];
        	        oids[0] = new OID(contRel.getMappingOid().getOid());
        	        tblevents = SnmpService.instance().getTablePdu( discNode.getElementEndPoint(), oids);
        	        
        	        relMapTblInfo.put(contRel.getMappingTable().getOid(), tblevents);
        		}        		
        		
        		SNMPTable tbl = contRel.getMappingTable();
        	    List<SNMP> indexSnmps = tbl.getIndex();	
        	          	    
        	    /**
        	     * Two cases, the table may not contains index, in that case we are assuming
        	     * the instance oid of the mapping table contains parent instance oid and the index
        	     * attribute the context oid of parent levelOid.
        	     * 
        	     */
        	    ParentIndexInstance pindex = null;
        	    if ( indexSnmps == null || indexSnmps.size() == 0) {
        	    
        	    	int mappingIndex = findMappingIndexInSET(parentSet, levelOid.getContextOID().getOid());
        	    	if ( mappingIndex != -1 ) {
        	    		String mappingInst = getMappingParentInstanceFromSE(parentSe, mappingIndex);
        	    		
        	    		pindex = new ParentIndexInstance(0, levelOid.getContextOID().getOid(), contRel.getChildTable());
        	    		pindex.setInstanceOid(mappingInst);
        	    	}
        	    }
        	    else {
        	    
        	    	for ( int i=0; i<indexSnmps.size(); i++ ) {
        	    		
        	    	    SNMPIndex si = (SNMPIndex) indexSnmps.get(i);
        	    	    int mappingIndex = findMappingIndexInSET(parentSet, si.getOid());
        	    	    if ( mappingIndex != -1 ) {
            	    		String mappingInst = getMappingParentInstanceFromSE(parentSe, mappingIndex);
            	    		
            	    		pindex = new ParentIndexInstance(i, si.getOid(), contRel.getChildTable());
            	    		pindex.setInstanceOid(mappingInst);
            	    		break;
            	    	}
        	    	}
        	    }
        	    if ( pindex != null ) {
        	    	
        	    	RelationMappingInstance rmi = findParentInMappingTable(tblevents, pindex);
        	    	if ( rmi != null && rmi.getChildInstances().size() > 0 ) {
        	    		
        	    		 SnmpLevelOID childLevel =  rmi.getParentIndex().getChildSnmpLevel();        	    		 
        	    		 if ( childLevel.getDisriminators() != null && childLevel.getDisriminators().size() == 1 ) {
        	 				
        	 				SnmpServiceElementTypeDiscriminator discriminator = childLevel.getDisriminators().get(0);
        	 				ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
        	 				
        	 				if ( set != null ) {
        	 					
        	 					if ( contRel.getMappingType() != null || contRel.getMappingType() == RelationMappingTypeEnum.InstanceOnly ) {
        	 						
        	 						for ( String inst : rmi.getChildInstances() ) {
        	 							ServiceElement se =  createServiceElementFromType(discNode, set, inst, discNode.getAccessElement());						
            	 						se = accessMgr.updateServiceElement(se);
            	 						
            	 						SEAndSETPair discComp = new SEAndSETPair();
            	 						discComp.set = set;
            	 						discComp.se = se;
            	 						discComp.snmpLevel = childLevel;
            	 						
            	 						discoverComps.add(discComp);
        	 						}
        	 					}
        	 					else if ( contRel.getMappingType() == RelationMappingTypeEnum.FullOid ){
        	 						
        	 						for ( String fullStr : rmi.getChildInstances() ) {
        	 							
        	 							OID fullOID = new OID(fullStr);
        	 							OID contexSnmp = new OID(levelOid.getContextOID().getOid());
        	 							int startIndex = -1;
        	 							
        	 							if ( contexSnmp.get(contexSnmp.size() - 1) == 1 ) {        	 								
        	 								startIndex = contexSnmp.size() + 1;
        	 							}
        	 							else {
        	 								startIndex = contexSnmp.size() + 2;
        	 							}
        	 							if ( fullOID.size() > startIndex ) {
        	 								
        	 								int[] insta = new int[fullOID.size() - startIndex];       	 								
        	 								for ( int i=startIndex;i<fullOID.size();i++ ) {
        	 									
        	 									insta[i] = fullOID.get(i);
        	 								}
        	 								OID inst = new OID(insta);
        	 								ServiceElement se =  createServiceElementFromType(discNode, set, inst.toString(), discNode.getAccessElement());						
                	 						se = accessMgr.updateServiceElement(se);
                	 						
                	 						SEAndSETPair discComp = new SEAndSETPair();
                	 						discComp.set = set;
                	 						discComp.se = se;
                	 						discComp.snmpLevel = childLevel;
                	 						
                	 						discoverComps.add(discComp);
        	 							}
        	 							else {
        	 								logger.info("Cannot determine instance oid:" + fullStr + " context oid:" + levelOid.getContextOID().getOid());
        	 							}
        	 						}
        	 					}        	 					
        	 				}
        	 				else {
        	 				   
        	 					logger.info("Cannot find service element type on type: " + discriminator.getServiceElementTypeId().toString());
        	 				}
        	 			}
        	    	}
        	    	else {
        	    		
        	    		logger.info("Cannot find relation mapping. " + pindex.getIndexOid());
        	    	}
        	    }
        	}
        	else if ( rel instanceof SnmpParentChildRelationship ) {
        		
        		SnmpParentChildRelationship pcRel = (SnmpParentChildRelationship) rel;
        		
        		String mappingOid = pcRel.getContainmentOid().getOid();
        		
        		List<TableEvent> tableEvents = relMapTblInfo.get(mappingOid);
        		if ( tableEvents == null ) {
        			
        			/**
        			 * At least it should contain containment OID and subType
        			 */
        			int i = 2;
        			if ( pcRel.getSoftwareVersionOid() != null ) {
        				i++;
        			}
        			if ( pcRel.getSiblingOid() != null ) {
        				i++;
        			}
        			if ( pcRel.getModelOid() != null ) {
        				i++;
        			}        			
        			
        			OID[] oids = new OID[i];
        			
        			i = 0;
        			oids[i++] = new OID(pcRel.getContainmentOid().getOid());
        			oids[i++] = new OID(pcRel.getSubTypeOid().getOid());
        			
        			if ( pcRel.getSoftwareVersionOid() != null ) {
        				oids[i++] = new OID(pcRel.getSoftwareVersionOid().getOid());
        			}
        			if ( pcRel.getSiblingOid() != null ) {
        				oids[i++] = new OID(pcRel.getSiblingOid().getOid());
        			}
        			if ( pcRel.getModelOid() != null ) {
        				oids[i++] = new OID(pcRel.getModelOid().getOid());
        			}     
        			tableEvents = SnmpService.instance().getTablePdu( discNode.getElementEndPoint(), oids);
        	        relMapTblInfo.put(mappingOid, tableEvents);
        		}
        		
        		List<TableEvent> childEvent = getChildrenBaseOnParentInst(tableEvents, pcRel.getContainmentOid().getOid(), 
        				                           parentSe, parentSet, pcRel.getMappingType());
        		if ( childEvent != null && childEvent.size() > 0 ) {
        			
        			for ( TableEvent te : childEvent ) {
        			
        				String softVer = getColumnValue(te.getColumns(), pcRel.getSoftwareVersionOid().getOid());
        				String sibling = getColumnValue(te.getColumns(), pcRel.getSiblingOid().getOid());
        				String model = getColumnValue(te.getColumns(), pcRel.getModelOid().getOid());
        				
        				String subtype = te.getColumns()[1].getVariable().toString();        				
        				ServiceElementType[] sets = discMgr.getServiceElementTypesBySubtypeAndVendor(subtype, parentSet.getVendor());
        				if ( sets != null ) {
        				
        					for ( ServiceElementType set : sets ) {
        					
        						if ( model != null && set.getModel() != null ) {
        							
        							if ( !model.equals(set.getModel()) ) {
        								continue;
        							}
        						}
        						if ( softVer != null && set.getSoftware() != null ) {
        						
        							if ( !softVer.equalsIgnoreCase(set.getSoftware() )) {
        								continue;
        							}
        						}     
        						
        						/**
        						 * OK we pick the right 
        						 */
        						break;
        						
        					}        					
        				}        				
        			}        			
        		}
        		else {  			
        			logger.info("Cannot find children for " + parentSe.getName());
        		}        		
        	}
        }
        else {
        	
        	SNMP discrimatorSnmp = levelOid.getDescriminatorOID();		
    		OID[] oids = new OID[1];
            oids[0] = new OID(discrimatorSnmp.getOid());
            
            List<TableEvent> deviceEvents = SnmpService.instance().getTablePdu( discNode.getElementEndPoint(), oids);
    		if (levelOid.getDisriminators() != null && levelOid.getDisriminators().size() > 1 ) {

    			for (SnmpServiceElementTypeDiscriminator discriminator : levelOid.getDisriminators()) {

    				List<TableEvent> tes = findTableEventRow(deviceEvents, discrimatorSnmp.getOid(), discriminator.getDiscriminatorValue());
    				for ( TableEvent te : tes ) {
    					ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
    					ServiceElement se =  createServiceElementFromType(discNode, matchSet, te.getIndex().toString(), parentSe );						
    					se = accessMgr.updateServiceElement(se);
    					
    					for ( SnmpLevelOID snmpLevel : levelOid.getChildren() ) {
    						
    						SEAndSETPair discComp = new SEAndSETPair();
     						discComp.set = matchSet;
     						discComp.se = se;
     						discComp.snmpLevel = snmpLevel;
     						
     						discoverComps.add(discComp);
    					}    					
    					
    				}
    			}
    		}
    		else if ( levelOid.getDisriminators() != null && levelOid.getDisriminators().size() == 1 ) {
    			
    			SnmpServiceElementTypeDiscriminator discriminator = levelOid.getDisriminators().get(0);
    			if ( discriminator.getDiscriminatorValue() != null ) {
    				
    				List<TableEvent> tes = findTableEventRow(deviceEvents, discrimatorSnmp.getOid(), discriminator.getDiscriminatorValue());
    				for ( TableEvent te : tes ) {
    					ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
    					ServiceElement se =  createServiceElementFromType(discNode, matchSet, te.getIndex().toString(), parentSe);						
    					se = accessMgr.updateServiceElement(se);
    					
                     for ( SnmpLevelOID snmpLevel : levelOid.getChildren() ) {
    						
    						SEAndSETPair discComp = new SEAndSETPair();
     						discComp.set = matchSet;
     						discComp.se = se;
     						discComp.snmpLevel = snmpLevel;
     						
     						discoverComps.add(discComp);
    					}    
    				}
    			}
    			else {
    				
    				ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
    				for ( TableEvent te : deviceEvents ) {
    					
    					ServiceElement se =  createServiceElementFromType(discNode, matchSet, te.getIndex().toString(), parentSe);
    					se = accessMgr.updateServiceElement(se);
    					
                        for ( SnmpLevelOID snmpLevel : levelOid.getChildren() ) {
    						
    						SEAndSETPair discComp = new SEAndSETPair();
     						discComp.set = matchSet;
     						discComp.se = se;
     						discComp.snmpLevel = snmpLevel;
     						
     						discoverComps.add(discComp);
    					}    
    				}
    			}
    		}
        }
        for ( SEAndSETPair discoveredComp : discoverComps ) {
        	discoverByContainment(discNode, discoveredComp.snmpLevel, discoveredComp.set, discoveredComp.se);
        }
        
	}
	
	
	
	/**
	 * Retrieve instance oid associated with indexOID from a service element.
	 *
	 * @param se the se
	 * @param mappingIndex the mapping index
	 * @return the mapping parent instance from se
	 * @throws IntegerException the integer exception
	 */
	public String getMappingParentInstanceFromSE( ServiceElement se, int mappingIndex ) throws IntegerException {

		if ( mappingIndex != -1 ) {
			
			List<ServiceElementProtocolInstanceIdentifier> seps =  se.getValues();
			ServiceElementProtocolInstanceIdentifier sep = seps.get(mappingIndex);
			
			return sep.getValue();
		}
		return null;
	}
	
	
	/**
	 * Find mapping index in set.
	 *
	 * @param set the set
	 * @param indexOid the index oid
	 * @return the int
	 * @throws IntegerException the integer exception
	 */
	private int findMappingIndexInSET( ServiceElementType set, String indexOid ) throws IntegerException {
		
		OID o = new OID(indexOid);	
		String tblOid = indexOid;
		if ( o.get(o.size() - 2) == 1 ) {
			
			o.trim(2);
			tblOid = o.toString();
		}
		
		List<ID>  attributeIds =  set.getAttributeIds();
		
		int mappingIndex = -1;
		for ( int i=0; i<attributeIds.size(); i++ ) {
			
			ID id = attributeIds.get(i);
			ServiceElementManagementObject sem = capMgr.getManagementObjectById(id);
			if ( sem instanceof SNMP ) {
				SNMP mrgObj = (SNMP)sem;
			
				if ( mrgObj.getOid().indexOf(tblOid) >= 0 ) {
					mappingIndex = i;
					break;
				}
			}
		}
		return mappingIndex;
	}
	
	
	/**
	 *   
	 *
	 * @param events the events
	 * @param mappingStr the mapping str
	 * @param se the se
	 * @param set the set
	 * @param mapType the map type
	 * @return the children base on parent inst
	 */
	private List<TableEvent> getChildrenBaseOnParentInst( List<TableEvent> events, 
			                                              String mappingStr,
			                                              ServiceElement se,
			                                              ServiceElementType set,
			                                              RelationMappingTypeEnum mapType) {
		
		List<TableEvent> children = new ArrayList<>();
		
		OID mappingOid = new OID(mappingStr);
		ServiceElementProtocolInstanceIdentifier sep = se.getValues().get(0);		
		for ( TableEvent te : events ) {
			
			for ( VariableBinding vb : te.getColumns() ) {
				if ( vb.getOid().startsWith(mappingOid) ) {
					
					String inst = vb.getVariable().toString();
					if ( mapType == RelationMappingTypeEnum.InstanceOnly ) {
						
						if ( inst.equals(sep.getValue() ) ) {
							children.add(te);
						}							
					}
					else if ( mapType == RelationMappingTypeEnum.FullOid ) {
						
						throw new RuntimeException("Unsupport relationship time " + mapType);
					}
					else {
					    throw new RuntimeException("Unsupport relationship time " + mapType);
					}
				}
			}
		}
		return children;
	}
	
	
	
	/**
	 * Find parent in mapping table.
	 *
	 * @param tes the tes
	 * @param pii the pii
	 * @return the relation mapping instance
	 */
	private RelationMappingInstance findParentInMappingTable( List<TableEvent> tes, ParentIndexInstance pii ) {
		
		RelationMappingInstance rmi = new RelationMappingInstance(pii);
		for ( TableEvent te : tes ) {
			
			OID o = te.getIndex();
			int pinst = o.get(pii.getIndexPosition());
			
			if ( Integer.parseInt(pii.getInstanceOid()) == pinst ) {
				
				VariableBinding[] vbs = te.getColumns();
				rmi.addInstance(vbs[0].getVariable().toString());
			}
		}
		return rmi;
	}
	
	
	/**
	 * Gets the column value.
	 *
	 * @param vbs the vbs
	 * @param attrOid the attr oid
	 * @return the column value
	 */
	public String getColumnValue( VariableBinding[] vbs, String attrOid ) {
		
		OID o = new OID(attrOid);
	    for ( VariableBinding vb : vbs ) {
	    	
	    	if ( vb.getOid().startsWith(o) ) {
	    		return vb.getVariable().toString();
	    	}
	    }
		return null;
	}
	
	
	/**
	 * The Class SEAndSETPair.
	 *
	 * @author dchan
	 */
	public class SEAndSETPair {
		
		/** The se. */
		private ServiceElement se;
		
		/** The set. */
		private ServiceElementType set;
		
		/** The snmp level. */
		private SnmpLevelOID snmpLevel;
	}
	
	
	
}
