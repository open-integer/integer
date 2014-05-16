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

import java.util.List;

import org.snmp4j.smi.OID;
import org.snmp4j.util.TableEvent;

import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.discovery.SnmpContainment;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;

/**
 * @author dchan
 *
 */
public class ContainmentServiceElementWorker extends SnmpServiceElementDiscover {

	/**
	 * @throws IntegerException
	 */
	public ContainmentServiceElementWorker() throws IntegerException {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.SnmpServiceElementDiscover#discover(edu.harvard.integer.common.discovery.SnmpContainment, edu.harvard.integer.service.discovery.subnet.DiscoverNode, edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface)
	 */
	@Override
	public ServiceElement discover(SnmpContainment sc, DiscoverNode discNode )
			throws IntegerException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/**
	 * 
	 * 
	 * @param discNode
	 * @param levelOid
	 * @param set
	 * @param parentSe
	 * @throws IntegerException
	 */
	public void discoverByContainment( DiscoverNode discNode, SnmpLevelOID levelOid, 
			                           ServiceElementType parentSet,
			                           ServiceElement parentSe ) throws IntegerException {

		SNMP discrimatorSnmp = levelOid.getDescriminatorOID();		
		OID[] oids = new OID[1];
        oids[0] = new OID(discrimatorSnmp.getOid());
        
        List<TableEvent> deviceEvents = SnmpService.instance().getTablePdu( discNode.getElementEndPoint(), oids);
		if (levelOid.getDisriminators() != null && levelOid.getDisriminators().size() > 1 ) {

			for (SnmpServiceElementTypeDiscriminator discriminator : levelOid.getDisriminators()) {

				List<TableEvent> tes = findTableEventRow(deviceEvents, discrimatorSnmp.getOid(), discriminator.getDiscriminatorValue());
				for ( TableEvent te : tes ) {
					ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
					ServiceElement se =  createServiceElementFromType(discNode, matchSet, te, parentSe );						
					se = accessMgr.updateServiceElement(se);
				}
			}
		}
		else if ( levelOid.getDisriminators() != null && levelOid.getDisriminators().size() == 1 ) {
			
			SnmpServiceElementTypeDiscriminator discriminator = levelOid.getDisriminators().get(0);
			if ( discriminator.getDiscriminatorValue() != null ) {
				
				List<TableEvent> tes = findTableEventRow(deviceEvents, discrimatorSnmp.getOid(), discriminator.getDiscriminatorValue());
				for ( TableEvent te : tes ) {
					ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
					ServiceElement se =  createServiceElementFromType(discNode, matchSet, te, parentSe);						
					se = accessMgr.updateServiceElement(se);
				}
			}
			else {
				
				ServiceElementType matchSet = discMgr.getServiceElementTypeById(discriminator.getServiceElementTypeId());
				for ( TableEvent de : deviceEvents ) {
					
					ServiceElement se =  createServiceElementFromType(discNode, matchSet, de, parentSe);
					se = accessMgr.updateServiceElement(se);
				}
			}
		}
		
		if ( levelOid.getChildren() != null ) {
			
			List<SnmpLevelOID> snmpLevels = levelOid.getChildren();				
			for ( SnmpLevelOID so : snmpLevels ) {	
				
				 so.getDisriminators().get(0).getServiceElementTypeId();
				
				
			}	
		}	  
		
	}
	
}
