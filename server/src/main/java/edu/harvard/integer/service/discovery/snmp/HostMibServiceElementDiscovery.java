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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.snmp.MaxAccess;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * The Class HostMibServiceElementDiscovery is used to discover host mib devices.
 * The discovery is based on level containment structure within components.
 * However the mapping from port to interface need to be more work.
 *
 * @author dchan
 */
public class HostMibServiceElementDiscovery extends SnmpServiceElementDiscover {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(HostMibServiceElementDiscovery.class);
    
	/**
	 * Map for mapping device table index to ifIndex.
	 */
	private Map<Integer, String> ifIndexMap = new HashMap<Integer, String>();

	/**
	 * Instantiates a new host mib service element discovery.
	 *
	 * @throws IntegerException the integer exception
	 */
	public HostMibServiceElementDiscovery() throws IntegerException {
		super();

	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover
	 * #discover(edu.harvard.integer.common.discovery.SnmpContainment,
	 * edu.harvard.integer.service.discovery.subnet.DiscoverNode,
	 * edu.harvard.integer
	 * .service.discovery.ServiceElementDiscoveryManagerInterface)
	 */
	@Override
	public ServiceElement discover(SnmpContainment sc, DiscoverNode discNode)
			throws IntegerException {

		logger.info("In HostMibServiceElementDiscovery discover ");
		ElementEndPoint endPoint = discNode.getElementEndPoint();
		
		this.discNode = discNode;
		/*
		 * Set up the if mapping table to create mapping for port if.
		 */
		SnmpContainmentRelation relation = HostMIBSnmpInfo.getContainmentRelationForPort();
		OID[] ifMap = new OID[1];
		ifMap[0] = new OID(relation.getMappingOid().getOid());
		List<TableEvent> tblEvents = SnmpService.instance().getTablePdu(endPoint, ifMap);

		for (TableEvent te : tblEvents) {

			Integer deviceIndex = Integer.valueOf(te.getIndex().get(0));
			String ifIndex = te.getColumns()[0].getVariable().toString();

			ifIndexMap.put(deviceIndex, ifIndex);
		}
		
		/**
		 * Scan through each SnmpLevelOID to create sub-components.
		 */
		List<SnmpLevelOID> levelOids = sc.getSnmpLevels();
		for (SnmpLevelOID levelOid : levelOids) {

			List<TableEvent> snmpTblEvents = null; 
			SNMP doid = null;
			if ( levelOid.getDescriminatorOID() != null ) {
				
				doid = levelOid.getDescriminatorOID();
				OID[] oids = new OID[1];
		        oids[0] = new OID(doid.getOid());
				snmpTblEvents = SnmpService.instance().getTablePdu( endPoint, oids);
				logger.info("Number of table event " + snmpTblEvents.size());
			}
	        
	        
			if (levelOid.getDisriminators() != null && levelOid.getDisriminators().size() > 1 ) {

				for (SnmpServiceElementTypeDiscriminator discriminator : levelOid.getDisriminators()) {

					List<TableEvent> tes = findTableEventRow(snmpTblEvents, doid.getOid(), discriminator.getDiscriminatorValue());
					for ( TableEvent te : tes ) {
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, 
								                     te.getIndex().toDottedString(), (SNMPTable)levelOid.getContextOID());	
						
						SNMP nameAttr = null;
						if (set.getDefaultNameCababilityId() != null) {

							for ( ID snmpId : set.getAttributeIds() ) {
								SNMP tmpSnmp = (SNMP) capMgr.getManagementObjectById(snmpId);
								if ( tmpSnmp.getCapabilityId().getIdentifier().longValue() == set.getDefaultNameCababilityId().getIdentifier().longValue() ) {
									nameAttr = tmpSnmp;
									break;
								}				
							}
							if ( nameAttr != null ) {
								for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
									
									SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
									 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {
										 
										 se.setName(mval.getValue().toString());
										 break;
									 }	
								}
							}
						}
						else {
							
							nameAttr = snmpMgr.getSNMPByName("hrDeviceDescr");
							for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
								
								SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
								 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {
									 
									 if ( set.getCategory().getName().equals("CPU") ) {
										 se.setName(mval.getValue().toString() + " " + te.getIndex().toString());
									 }
									 else {
									     se.setName(mval.getValue().toString());
									 }
									 break;
								 }	
							}
							
						}
						se = updateServiceElement(se, set, discNode.getAccessElement(), levelOid);
					}
				}
			}
			else if ( levelOid.getDisriminators() != null && levelOid.getDisriminators().size() == 1 ) {
				
				SnmpServiceElementTypeDiscriminator discriminator = levelOid.getDisriminators().get(0);
				if ( levelOid.getDescriminatorOID() != null ) {
					
					List<TableEvent> tes = findTableEventRow(snmpTblEvents, doid.getOid(), discriminator.getDiscriminatorValue());
					for ( TableEvent te : tes ) {
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, 
								                                      te.getIndex().toString(), (SNMPTable)levelOid.getContextOID());	
						
						if (set.getDefaultNameCababilityId() != null) {

							SNMP nameAttr = null;
							if (set.getDefaultNameCababilityId() != null) {

								for ( ID snmpId : set.getAttributeIds() ) {
									SNMP tmpSnmp = (SNMP) capMgr.getManagementObjectById(snmpId);
									if ( tmpSnmp.getCapabilityId().getIdentifier().longValue() == set.getDefaultNameCababilityId().getIdentifier().longValue() ) {
										nameAttr = tmpSnmp;
										break;
									}				
								}
								if ( nameAttr != null ) {
									for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
										
										SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
										 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {
											 
											 if ( set.getCategory().getName().equals("CPU") ) {
												 se.setName(mval.getValue().toString() + " " + te.getIndex().toString());
											 }
											 else {
											     se.setName(mval.getValue().toString());
											 }
											 break;
										 }	
									}
								}
							}
							else {
								
								nameAttr = snmpMgr.getSNMPByName("hrDeviceDescr");
								for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
									
									SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
									 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {
										 
										 if ( set.getCategory().getName().equals("CPU") ) {
											 se.setName(mval.getValue().toString() + " " + te.getIndex().toString());
										 }
										 else {
										     se.setName(mval.getValue().toString());
										 }
										 break;
									 }	
								}
							}
						}
						se = updateServiceElement(se, set, discNode.getAccessElement(), levelOid);
					}
				}
				else {
					
					ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
					List<ID> attributeIds = set.getAttributeIds();
					
					List<SNMP> accessSnmps = new ArrayList<>();
					List<SNMP> nonAccessSnmps = new ArrayList<>();
					
					for ( ID id : attributeIds ) {
						
						SNMP s = (SNMP) capMgr.getManagementObjectById(id);
						if ( s.getMaxAccess() == null || s.getMaxAccess() == MaxAccess.NotAccessible ) {
							nonAccessSnmps.add(s);
						}
						else {
							accessSnmps.add(s);
						}
					}
					OID[] tblOids = new OID[accessSnmps.size()];
					for ( int z=0; z<accessSnmps.size(); z++ ) {
						tblOids[z] = new OID(accessSnmps.get(z).getOid());
					}
					
					try {
					    snmpTblEvents = SnmpService.instance().getTablePdu(endPoint, tblOids);
					}
					catch ( Exception e ) {
						
						/**
						 * This is a bug on net-snmp. In general make a sleep and it will work again.
						 */
						try {
							TimeUnit.SECONDS.sleep(5);
							snmpTblEvents = SnmpService.instance().getTablePdu(endPoint, tblOids);
						} catch (InterruptedException e1) 
						{ 
							continue;
						}
						
					}
					for ( TableEvent snmpTblEvent : snmpTblEvents ) {
						
						ServiceElement se = createServiceElementFromType(discNode, set, snmpTblEvent, null);
						if ( set.getCategory().getName().equals("Software")) {
							SNMP nameAttr = snmpMgr.getSNMPByName("hrSWInstalledName");
							for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
								
								SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
								 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {									
									  se.setName(mval.getValue().toString());
								 }	
							}
						}
						else if ( set.getCategory().getName().equals("Storage") ) {
							SNMP nameAttr = snmpMgr.getSNMPByName("hrStorageDescr");
							for ( ManagementObjectValue<?> mval : se.getAttributeValues() ) {
								
								SNMP snmp =  (SNMP) capMgr.getManagementObjectById(mval.getManagementObject());
								 if ( snmp.getIdentifier().longValue() == nameAttr.getIdentifier().longValue() ) {									
									  se.setName(mval.getValue().toString());
								 }	
							}
						}
						se = updateServiceElement(se, set, discNode.getAccessElement(), levelOid);
					}
				}
			}
		}

		return discNode.getAccessElement();
	}

	
	

}
