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

import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.common.discovery.RelationMappingTypeEnum;
import edu.harvard.integer.common.discovery.SnmpContainmentRelation;
import edu.harvard.integer.common.discovery.SnmpLevelOID;
import edu.harvard.integer.common.discovery.SnmpServiceElementTypeDiscriminator;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPTable;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;

/**
 * @author dchan
 *
 */
public class HostMIBSnmpInfo {

	public static SnmpContainmentRelation getContainmentRelationForPort() throws IntegerException {
		
		SnmpManagerInterface snmpMgr =  DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
		SnmpContainmentRelation relation = new SnmpContainmentRelation();
		SnmpLevelOID levelOid = new SnmpLevelOID();
		
		levelOid.setName("PortMappingLevel");
        SNMP snmp = snmpMgr.getSNMPByName("ifEntry");
		levelOid.setContextOID(snmp);
		levelOid.setCategory(CategoryTypeEnum.port);
		
		if ( levelOid.getDisriminators() == null ) {
			
			List<SnmpServiceElementTypeDiscriminator> discList = new ArrayList<>();
			levelOid.setDisriminators(discList);
		}
		
		ServiceElementDiscoveryManagerInterface discMgr =  DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
		ServiceElementType set = discMgr.getServiceElementTypeByName("interface");
		
		SnmpServiceElementTypeDiscriminator std = new SnmpServiceElementTypeDiscriminator();
		std.setServiceElementTypeId(set.getID());
		
		levelOid.getDisriminators().add(std);
		SNMPTable networkTbl = (SNMPTable) snmpMgr.getSNMPByOid(CommonSnmpOids.hrNetworkEntry);
		
		relation.setMappingTable(networkTbl);
		relation.setMappingType(RelationMappingTypeEnum.InstanceOnly);
		
		snmp = snmpMgr.getSNMPByName("hrNetworkIfIndex");
		relation.setMappingOid(snmp);
		
	    return relation;
	}
}
