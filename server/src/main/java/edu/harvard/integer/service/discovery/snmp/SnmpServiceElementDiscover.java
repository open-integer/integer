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
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.access.snmp.ParentChildMappingIndex;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDescriminatorIntegerValue;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorStringValue;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminatorValue;
import edu.harvard.integer.common.discovery.VendorDiscoveryTemplate;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.managementobject.ManagementObjectIntegerValue;
import edu.harvard.integer.common.managementobject.ManagementObjectStringValue;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementProtocolInstanceIdentifier;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.element.ElementDiscoveryBase;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.topology.TopologyManagerInterface;
import edu.harvard.integer.service.topology.TopologyManagerLocalInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;

/**
 * The SnmpServiceElementDiscover is the base class for all SNMP service element
 * discover.  It provides some basic methods used on SNMP service element discovery.
 * 
 * In order to kick off a discover for a network SNMP device, it needs to call the abstract method "discover".
 * The output of the method "discover" a service element associated with network SNMP device.
 *
 * @author dchan
 */
public abstract class SnmpServiceElementDiscover implements ElementDiscoveryBase {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(SnmpServiceElementDiscover.class);

    
    /** The discovery manager. */
	protected ServiceElementDiscoveryManagerInterface discMgr;
	
	/** The service element access manager. */
	protected ServiceElementAccessManagerInterface accessMgr;
	
	/** The snmp manager. */
	protected SnmpManagerInterface snmpMgr;
	
	/** The capability manager. */
	protected ManagementObjectCapabilityManagerInterface capMgr;
	
	protected TopologyManagerInterface topologyMgr;
	
	/** 
	 * The mapping table is use to store pairs of parent index and child index.
	 * This table is really for performance purpose.  In a lot of cases,
	 * retrieve all the parent and child index in one SNMP block request from a device and save it in cache
	 * will reduce a lot of requests which each request may contain a small set of data.   
	 * The key is a string and in general it is the ContextOID of a Service Element Type. 
	 */
	private Map<String, List<ParentChildMappingIndex>> indexMappingTbl = new HashMap<String, List<ParentChildMappingIndex>>();
	
	/**
	 * This mapping table stores all instance oids for a table.  Discovery Service retrieves all instances for a table
	 * at once and used them for later references.  
	 */
	private Map<String, List<String>>  instanceMappingTbl = new HashMap<>();


	/**
	 * Instantiates a new snmp service element discover.
	 *
	 * @throws IntegerException the integer exception
	 */
	public SnmpServiceElementDiscover() throws IntegerException {
		
		capMgr = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
		accessMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
		snmpMgr = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		discMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		topologyMgr = DistributionManager.getManager(ManagerTypeEnum.TopologyManager);
	}
	
    
	/**
	 * Discover service element attributes which their SEMO are defined in the associated service element type.
	 *
	 * @param ePoint for the device
	 * @param se -- service element on which attributes be retrieved.
	 * @param set the service element type for the service element
	 * @param instOid the instance OID of the service element attributes.
	 * @throws IntegerException the integer exception
	 */
	public void discoverServiceElementAttribute( ElementEndPoint ePoint, 
			                                     ServiceElement se, 
			                                     ServiceElementType set,
			                                     String instOid ) throws IntegerException {
		
		if ( set.getAttributeIds() != null && set.getAttributeIds().size() > 0 ) {
			
			logger.info("Size of attributesIds " + set.getAttributeIds().size() );
			PDU pdu = new PDU();
			List<VariableBinding> vbs = new ArrayList<>();
			
			/**
			 * Walk through the attribute SEMO list to construct a PDU to retrieve data from a device
			 */
			List<ID>  attributeIds =  set.getAttributeIds();		
			for ( ID id : attributeIds ) {
				
				ServiceElementManagementObject mrgObj = capMgr.getManagementObjectById(id);
				if ( mrgObj instanceof SNMP ) {
					
					SNMP snmp = (SNMP) mrgObj;
					OID vbOid = new OID(snmp.getOid());
					vbOid.append(instOid);
					
					vbs.add(new VariableBinding(vbOid));
					
					logger.info("Search for this oid ****************************** " + vbOid );
				}
			}
			PDU rpdu = null;
			if ( vbs.size() > 0 ) {
				
				pdu.addAll(vbs);
				
				logger.info("Start Retrieve SNMP request back from " + ePoint.getIpAddress());
				
				for ( int i=0; i<pdu.getVariableBindings().size(); i++ ) {
					
					logger.info("Get value from this oid " +  pdu.getVariableBindings().get(i).getOid().toString());
				}
				
			    rpdu = SnmpService.instance().getPdu(ePoint, pdu);
			    List<ManagementObjectValue> attributes = se.getAttributeValues();
				if ( attributes == null )
				{
					attributes = new ArrayList<>();
					se.setAttributeValues(attributes);
				}
				List<ServiceElementProtocolInstanceIdentifier> insts = se.getValues();
				if ( insts == null ) {
					insts = new ArrayList<>();
					se.setValues(insts);
				}
				
				/**
				 * Store retrieved SEMO values in the Service Element.
				 */
			    for ( ID id : attributeIds ) {
					
					ServiceElementManagementObject mrgObj = capMgr.getManagementObjectById(id);
					if ( mrgObj instanceof SNMP ) {
						
						SNMP snmp = (SNMP) mrgObj;
						VariableBinding vb = findMatchVB(snmp, rpdu);
						
						if ( vb.getVariable() instanceof UnsignedInteger32 ||
								vb.getVariable() instanceof Integer32 ) {
							
							ManagementObjectIntegerValue iv = new ManagementObjectIntegerValue();
							
							iv.setValue(vb.getVariable().toInt());
							iv.setManagementObject(snmp.getID());
							
							se.getAttributeValues().add(iv);
							ServiceElementProtocolInstanceIdentifier inst = new ServiceElementProtocolInstanceIdentifier();        
				            inst.setValue(instOid);
				            se.getValues().add(inst);
						}
						else {
						
							ManagementObjectStringValue sv = new ManagementObjectStringValue();
                            sv.setValue(vb.getVariable().toString());
                            sv.setManagementObject(snmp.getID());
                            
							se.getAttributeValues().add(sv);
							ServiceElementProtocolInstanceIdentifier inst = new ServiceElementProtocolInstanceIdentifier();        
				            inst.setValue(instOid);
				            se.getValues().add(inst);
						}						
					}
				}				
			}	
			
		}
	}
	
    
    
	/**
	 * Discover service element attributes similar to another discover attributes method. 
	 * However Passing in includes a map contains instance oids for discovered MIB tables.
	 * 
	 * Note this method will be deprecated soon since sooner EntityMibServiceElementDiscovery will be deprecated also.
	 *
	 * @param ePoint for the device
	 * @param se -- service element on which attributes be set.
	 * @param set the service element type for the service element
	 * @param discoveredTableIndexMap the discovered table index map
	 * @throws IntegerException the integer exception
	 */
	public void discoverServiceElementAttribute( ElementEndPoint ePoint, 
			                                     ServiceElement se, 
			                                     ServiceElementType set,
			                                     Map<String, TableRowIndex> discoveredTableIndexMap) throws IntegerException {
		
		
		if ( set.getAttributeIds() != null && set.getAttributeIds().size() > 0 ) {
			
			logger.info("Size of attributesIds " + set.getAttributeIds().size() );
			PDU pdu = new PDU();
			List<VariableBinding> vbs = new ArrayList<>();
			
			List<ID>  attributeIds =  set.getAttributeIds();		
			for ( ID id : attributeIds ) {
				
				ServiceElementManagementObject mrgObj = capMgr.getManagementObjectById(id);
				if ( mrgObj instanceof SNMP ) {
					
					SNMP snmp = (SNMP) mrgObj;
					OID vbOid = new OID(snmp.getOid());
					
					logger.info("SNMP attribute oid " + snmp.getOid() + " " + vbOid.get(vbOid.size() - 2));
					
					if ( vbOid.get(vbOid.size() - 2) == 1 ) {
						
						String tblOid = getTableOidFromVBOid(vbOid.toString());
			            if ( tblOid != null ) {
			            	
			            	TableRowIndex rowIndex = discoveredTableIndexMap.get(tblOid);
			            	
			            	if ( rowIndex != null ) {
			            		vbOid.append(rowIndex.getInstanceOid());
			            		vbs.add(new VariableBinding(vbOid));
			            	}
			            }
			            else {
			            	logger.info("Cannot find index information for " + snmp.getOid());
			            }
						
					}
					else {
						
						/*
						 * It is a scalar varbind.  
						 */
						vbOid.append(0);
						vbs.add(new VariableBinding(vbOid));
					}
				}
			}
			PDU rpdu = null;
			if ( vbs.size() > 0 ) {
				
				pdu.addAll(vbs);
				
				logger.info("Start Retrieve SNMP request back from " + ePoint.getIpAddress());
			    rpdu = SnmpService.instance().getPdu(ePoint, pdu);
			    logger.info("Retrieve SNMP request back from " + ePoint.getIpAddress());
			    
			    List<ManagementObjectValue> attributes = se.getAttributeValues();
				if ( attributes == null )
				{
					attributes = new ArrayList<>();
					se.setAttributeValues(attributes);
				}
				List<ServiceElementProtocolInstanceIdentifier> insts = se.getValues();
				if ( insts == null ) {
					insts = new ArrayList<>();
					se.setValues(insts);
				}
				
			    for ( ID id : attributeIds ) {
					
					ServiceElementManagementObject mrgObj = capMgr.getManagementObjectById(id);
					if ( mrgObj instanceof SNMP ) {
						
						SNMP snmp = (SNMP) mrgObj;
						VariableBinding vb = findMatchVB(snmp, rpdu);
						
						if ( vb.getVariable() instanceof UnsignedInteger32 ||
								vb.getVariable() instanceof Integer32 ) {
							
							ManagementObjectIntegerValue iv = new ManagementObjectIntegerValue();
							
							iv.setValue(vb.getVariable().toInt());
							iv.setManagementObject(snmp.getID());
							
							se.getAttributeValues().add(iv);
							ServiceElementProtocolInstanceIdentifier inst = new ServiceElementProtocolInstanceIdentifier();
							
							String tblOid = getTableOidFromVBOid(snmp.getOid());
				            if ( tblOid != null ) {
				            	
				            	TableRowIndex rowIndex = discoveredTableIndexMap.get(tblOid); 
				            	inst.setValue(rowIndex.getInstanceOid());
				            }
				            else {
				            	inst.setValue("0");
				            }
				            se.getValues().add(inst);
						}
						else {
							
							ManagementObjectStringValue sv = new ManagementObjectStringValue();
                            sv.setValue(vb.getVariable().toString());
                            sv.setManagementObject(snmp.getID());
							
							se.getAttributeValues().add(sv);
							ServiceElementProtocolInstanceIdentifier inst = new ServiceElementProtocolInstanceIdentifier();
							
							String tblOid = getTableOidFromVBOid(snmp.getOid());
				            if ( tblOid != null ) {
				            	
				            	TableRowIndex rowIndex = discoveredTableIndexMap.get(tblOid); 
				            	inst.setValue(rowIndex.getInstanceOid());
				            }
				            else {
				            	inst.setValue("0");
				            }
				            se.getValues().add(inst);
						}
						
					}
				}				
			}	
			
		}
	}
	
	
	
	
	
	
	/**
	 * This method get table part of a MIB oid from a variable binding.
	 *
	 * @param vbOid the vb oid
	 * @return the table oid from vb oid
	 * @throws IntegerException the integer exception
	 */
	public static String getTableOidFromVBOid( String vbOid ) throws IntegerException {
		
		OID o = new OID(vbOid);
		if ( o.get(o.size() - 2 ) != 1 ) {
			throw new IntegerException(null, NetworkErrorCodes.SNMPError) ;
		}
		o.trim(2);
		return o.toString();
	}
	
	
	/**
	 * Find match variable binding from a return PDU based on a SNMP object.
	 *
	 * @param snmp the snmp
	 * @param rpdu the rpdu
	 * @return the variable binding
	 */
	public VariableBinding findMatchVB( SNMP snmp, PDU rpdu ) {
		
		for ( VariableBinding vb : rpdu.getVariableBindings() ) {
			
			if ( vb.getOid().startsWith(new OID(snmp.getOid())) ) {
				return vb;
			}
		}
		return null;
	}

	
	
	/**
	 * Find a table event row from a TableEvent list which its attribute oid matched with "attrOid" and
	 * the value is matched with pass in "SnmpServiceElementTypeDiscriminatorValue".
	 *
	 * @param events the events
	 * @param attrOid the attr oid
	 * @param value the value
	 * @return the list
	 */
	public List<TableEvent> findTableEventRow( List<TableEvent> events, String attrOid, SnmpServiceElementTypeDiscriminatorValue<?> value ) {
		
		List<TableEvent> targetTes = new ArrayList<>();
		for ( TableEvent event : events ) {
			
			VariableBinding[] vbs = event.getColumns();
			for ( VariableBinding vb : vbs ) {
				
				if ( vb == null ) {
					
					logger.info( "vb is null *** " + attrOid + " " + event.getErrorMessage() );
				}
				else if ( vb.getOid() == null ) {
					logger.info( "vb Oid is null *** " +  event.getErrorMessage() + " " + attrOid );
				}
				logger.info(vb.getOid().toString() + " *** " + attrOid );
				if ( vb.getOid().toString().indexOf(attrOid) >= 0 ) {
					
					if ( value instanceof SnmpServiceElementTypeDescriminatorIntegerValue ) {
						
						if ( vb.getVariable().toInt() == ((SnmpServiceElementTypeDescriminatorIntegerValue)value).getValue().intValue() ) {
							targetTes.add(event);
						}
					}
					else if ( value instanceof SnmpServiceElementTypeDiscriminatorStringValue ) {
						
						if ( vb.getVariable().toString().indexOf(((SnmpServiceElementTypeDiscriminatorStringValue)value).getValue()) >= 0 ) {
							targetTes.add(event);
						}
					}
				}
			}
			
		}
		
		return targetTes;
	}
	
	
	/**
	 * Discover a service element from a device.  The retrieving information is specified in the pass in
	 * service element type of the service element:
	 * 
	 * The default name, unique identifier and attributes.
	 *
	 * @param discNode the discover node
	 * @param set Service Element Type for the discovering Service Element.
	 * @param parentSE the parent Service Element
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public ServiceElement createServiceElementFromType( DiscoverNode discNode,  ServiceElementType set,
			                                            String instOid, ServiceElement parentSe ) throws IntegerException {
		
		/**
		 * Setting the general information to the Service Element.
		 */
		ServiceElement se = new ServiceElement();
		se.setUpdated(new Date());
		
		if ( discNode.getExistingSE() == null ) {
			se.setCreated(new Date());
		}
		
		se.setServiceElementTypeId(set.getID());
        se.setDescription(set.getCategory().getName());
		
		if (parentSe != null) {
			se.setParentId(parentSe.getID());
		}

		/**
		 * Retrieve the name to identify the service element.
		 */
		SNMP nameAttr = null;
		if (set.getDefaultNameCababilityId() != null) {

			nameAttr = (SNMP) capMgr.getManagementObjectById(set.getDefaultNameCababilityId()); 
			PDU pdu = new PDU();
			
			OID o = new OID(nameAttr.getOid());
			o.append(instOid);
						
			pdu.add(new VariableBinding(o));
			PDU rpdu = SnmpService.instance().getPdu(discNode.getElementEndPoint(), pdu);
			se.setName(rpdu.get(0).getVariable().toString());
		}
		
		/**
		 * Retrieve the attributes of the service element.
		 */
		discoverServiceElementAttribute(discNode.getElementEndPoint(), se, set, instOid);
		findUIDForServiceElement(set, se, discNode.getElementEndPoint());
		
		return se;
	}
	

	/**
	 * Find UID For a Service Element.  The UID is specified on the Service Element Type Unique Identifier list.
	 * 
	 * @param set
	 * @param se
	 * @param ept
	 * @throws IntegerException
	 */
	public void findUIDForServiceElement( ServiceElementType set, ServiceElement se, ElementEndPoint ept ) throws IntegerException {
		
       if ( set.getUniqueIdentifierCapabilities() != null ) {
			
			PDU rpdu = null;
			PDU pdu = new PDU();
			for ( ID id : set.getUniqueIdentifierCapabilities() ) {
				
				SNMP snmp = (SNMP) capMgr.getManagementObjectById(id);			
				if ( snmp.getScalarVB() == null || snmp.getScalarVB() ) {
					OID o = new OID(snmp.getOid());
					o.append(0);
					
					logger.info("pick out " + o.toString());
					pdu.add(new VariableBinding(o));
					
					rpdu = SnmpService.instance().getPdu(ept, pdu);
					if ( rpdu != null ) {
						
						ManagementObjectValue<?> mov = null;
						VariableBinding vb = findMatchVB(snmp, rpdu);
						
						if ( vb.getVariable() instanceof UnsignedInteger32 ||
								vb.getVariable() instanceof Integer32 ) {
							
							ManagementObjectIntegerValue iv = new ManagementObjectIntegerValue();
							iv.setValue(vb.getVariable().toInt());
							iv.setManagementObject(snmp.getID());
							
							mov = iv;
						}
						else {
							ManagementObjectStringValue sv = new ManagementObjectStringValue();
							sv.setValue(vb.getVariable().toString());
							sv.setManagementObject(snmp.getID());
							
							mov = sv;						
						}
						mov = capMgr.updateManagementObjectValue(mov);
						List<ID> uids = se.getUniqueIdentifierIds();
						
						if ( uids == null ) {
							uids = new ArrayList<>();
							se.setUniqueIdentifierIds(uids);
						}
						uids.add(mov.getID());
					}
				}
				else {
					
					OID[] ids = new OID[1];
					ids[0] = new OID(snmp.getOid());
					List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(ept, ids);
					
					for (TableEvent te : tblEvents) {
						
						String idValue = te.getColumns()[0].getVariable().toString();
						if ( idValue != null && !idValue.equals("")) {
							
							ManagementObjectStringValue sv = new ManagementObjectStringValue();
							sv.setValue(idValue);
							sv.setManagementObject(snmp.getID());
							
							sv = (ManagementObjectStringValue) capMgr.updateManagementObjectValue(sv);
							List<ID> uids = se.getUniqueIdentifierIds();
							
							if ( uids == null ) {
								uids = new ArrayList<>();
								se.setUniqueIdentifierIds(uids);
							}
							uids.add(sv.getID());
						}	
					}
				}
				
			}
		}
	}
	
	

	/**
	 * Find variable binding from snmp4j table event which its variable binding oid equal to "oid"
	 *
	 * @param te the te
	 * @param oid the oid
	 * @return the variable binding
	 */
	public VariableBinding findVBFromTableEvent(TableEvent te, String oid) {

		VariableBinding[] vbs = te.getColumns();
		for (VariableBinding vb : vbs) {

			if (vb.getOid().toString().indexOf(oid) >= 0) {

				return vb;
			}
		}
		return null;
	}
	
	
	/**
	 * Return mac address from an if table.
	 * 
	 * @param ept
	 * @return
	 * @throws IntegerException
	 */
    public List<String>  getMacAddress( ElementEndPoint ept ) throws IntegerException {
    	
    	List<String>  macs = new ArrayList<>();
    	
    	OID[] ifp = new OID[1];
    	ifp[0] = new OID(CommonSnmpOids.ifPhysAddress);
    	
    	List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(ept, ifp);

		for (TableEvent te : tblEvents) {

			String ifphy = te.getColumns()[0].getVariable().toString();
			if ( ifphy != null && !ifphy.equals("") ) {
				macs.add(ifphy);
			}
		}		
		return macs;
    }
	
	
	
	

	/* 
	 * This method is not yet implemented.  The purpose of this method is to do a light scan of a service element.
	 * 
	 * (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#scanElement(edu.harvard.integer.agent.serviceelement.ElementEndPoint, edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public void scanElement(ElementEndPoint endEpt, ServiceElement element)
			throws IntegerException {
		// TODO Auto-generated method stub
		
	}




	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.discovery.ElementDiscoveryBase#checkReachable(edu.harvard.integer.agent.serviceelement.ElementEndPoint)
	 */
	@Override
	public String checkReachable(ElementEndPoint endEpt)
			throws IntegerException {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoveryBase#stopDiscovery()
	 */
	@Override
	public void stopDiscovery() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.element.ElementDiscoveryBase#discoverElement(edu.harvard.integer.common.discovery.VendorDiscoveryTemplate, edu.harvard.integer.service.discovery.subnet.DiscoverNode)
	 */
	@Override
	public void discoverElement(
			VendorDiscoveryTemplate<ServiceElementManagementObject> template,
			DiscoverNode disNode) throws IntegerException {
				
	}



	public void addIndexMapping( String key, List<ParentChildMappingIndex> indexMappingTbl) {
		
		this.indexMappingTbl.put(key, indexMappingTbl);
	}

	public List<ParentChildMappingIndex>  getIndexMapping( String key ) {
		
		if ( indexMappingTbl == null ) {
			return null;
		}
		
		return indexMappingTbl.get(key);
	}
	


	public Map<String, List<String>> getInstanceMappingTbl() {
		return instanceMappingTbl;
	}


	public void setInstanceMappingTbl(Map<String, List<String>> instanceMappingTbl) {
		this.instanceMappingTbl = instanceMappingTbl;
	}

	
	/**
	 * 
	 * @param se
	 * @return
	 * @throws IntegerException
	 */
	public String getIpAddressFromSE( ServiceElement se ) throws IntegerException {
	
		for ( int i=0; i<se.getAttributeValues().size(); i++ ) { 
			 ManagementObjectValue<?> objVal =  se.getAttributeValues().get(0);
			 SNMP snmp =  (SNMP) capMgr.getManagementObjectById(objVal.getManagementObject());
			 if ( snmp.getName().equals("ipAdEntAddr")) {
				 return objVal.getValue().toString();
			 }			 
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param se
	 * @return
	 * @throws IntegerException
	 */
	public String getIpMaskFromSE( ServiceElement se ) throws IntegerException {
		
		for ( int i=0; i<se.getAttributeValues().size(); i++ ) { 
			 ManagementObjectValue<?> objVal =  se.getAttributeValues().get(0);
			 SNMP snmp =  (SNMP) capMgr.getManagementObjectById(objVal.getManagementObject());
			 if ( snmp.getName().equals("ipAdEntNetMask")) {
				 return objVal.getValue().toString();
			 }			 
		}
		return null;
	}
	
	

	/**
	 * Discover.
	 *
	 * @param sc the sc
	 * @param discNode the disc node
	 * @return the service element
	 * @throws IntegerException the integer exception
	 */
	public abstract ServiceElement discover(SnmpContainment sc, DiscoverNode discNode) throws IntegerException;
}

