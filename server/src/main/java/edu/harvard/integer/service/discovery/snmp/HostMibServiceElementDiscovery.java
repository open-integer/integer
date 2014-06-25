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
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
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

			SNMP doid = levelOid.getDescriminatorOID();
			OID[] oids = new OID[1];
	        oids[0] = new OID(doid.getOid());
	        
	        logger.info("get descriminator table " + doid.getOid());
	        	        
	        PDU pdu = new PDU();
	        VariableBinding vb = new VariableBinding(new OID(doid.getOid()));
	        pdu.add(vb);
	        
	        List<PDU> rpdu = SnmpService.instance().getAllEntryPduByNext(endPoint, pdu);
	        logger.info("Number of row " + rpdu.size());
	        
	        
	        List<TableEvent> deviceEvents = SnmpService.instance().getTablePdu( endPoint, oids);
	        logger.info("Number of table event " + deviceEvents.size());
	        
	       
			if (levelOid.getDisriminators() != null && levelOid.getDisriminators().size() > 1 ) {

				for (SnmpServiceElementTypeDiscriminator discriminator : levelOid.getDisriminators()) {

					List<TableEvent> tes = findTableEventRow(deviceEvents, doid.getOid(), discriminator.getDiscriminatorValue());
					for ( TableEvent te : tes ) {
						ServiceElementType set = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
						ServiceElement se =  createServiceElementFromType(discNode, set, te.getIndex().toDottedString(), discNode.getAccessElement());						
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

	
	

}
