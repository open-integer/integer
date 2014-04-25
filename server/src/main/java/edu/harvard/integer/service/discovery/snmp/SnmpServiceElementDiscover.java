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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.managementobject.ManagementObjectIntegerValue;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.common.topology.ServiceElementProtocolInstanceIdentifier;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;

/**
 * @author dchan
 *
 */
public abstract class SnmpServiceElementDiscover {

	/** The logger. */
    private static Logger logger = LoggerFactory.getLogger(SnmpServiceElementDiscover.class);
    
    
	/**
	 * 
	 * @param ePoint
	 * @param se
	 * @param set
	 * @throws IntegerException 
	 */
	public void discoverServiceElementAttribute( ElementEndPoint ePoint, 
			                                     ServiceElement se, 
			                                     ServiceElementType set,
			                                     Map<String, TableRowIndex> discoveredTableIndexMap,
			                                     ManagementObjectCapabilityManagerInterface capMgr ) throws IntegerException {
		
		
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
						
					}
				}				
			}	
			
		}
	}
	
	
	
	/**
	 * This method get part MIB table oid from a variable binding oid which is an attribute of the table.
	 * 
	 * @param vbOid
	 * @return
	 * @throws IntegerException 
	 */
	public static String getTableOidFromVBOid( String vbOid ) throws IntegerException {
		
		OID o = new OID(vbOid);
		if ( o.get(o.size() - 2 ) != 1 ) {
			throw new IntegerException(null, NetworkErrorCodes.SNMPError) ;
		}
		o.trim(2);
		return o.toString();
	}
	
	
	public VariableBinding findMatchVB( SNMP snmp, PDU rpdu ) {
		
		for ( VariableBinding vb : rpdu.getVariableBindings() ) {
			
			if ( vb.getOid().startsWith(new OID(snmp.getOid())) ) {
				return vb;
			}
		}
		return null;
	}



	/**
	 * @param sc
	 * @param discNode
	 * @return
	 * @throws IntegerException
	 */
	public abstract ServiceElement discover(SnmpContainment sc, DiscoverNode discNode) throws IntegerException;
}

