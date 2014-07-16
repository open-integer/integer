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

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.topology.NetworkLayer;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;

/**
 * @author dchan
 *
 */
public class CdpConnection implements NetworkConnection {
	
	private int connifIndex;
	private String remoteIpAddress;
	private String remotePort;
	private String remoteDeviceId;
	private String remotePlatform;
	private String remoteVersion;
	

	/**
	 * The Service Element has to be CDP service element type.  Otherwise
	 * an exception will throw.
	 * @param se
	 * @throws IntegerException 
	 */
	public CdpConnection( ServiceElement se ) throws IntegerException {
		
		ManagementObjectCapabilityManagerInterface capMgr = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
		for ( int i=0; i<se.getAttributeValues().size(); i++ ) { 
			 ManagementObjectValue<?> objVal =  se.getAttributeValues().get(0);
			 SNMP snmp =  (SNMP) capMgr.getManagementObjectById(objVal.getManagementObject());
			 if ( snmp.getName().equals("cdpCacheAddress")) {
				 remoteIpAddress = objVal.getValue().toString();
			 }			 
			 else if ( snmp.getName().equals("cdpCacheVersion") ) {
				 remoteVersion = objVal.getValue().toString();
			 }
			 else if ( snmp.getName().equals("cdpCacheDeviceId")) {
				 remoteDeviceId = objVal.getValue().toString();
			 }
			 else if ( snmp.getName().equals("cdpCachePlatform") ) {
				 remotePlatform = objVal.getValue().toString();
			 }
			 else if ( snmp.getName().equals("cdpCacheDevicePort") ) {
				 remotePort = objVal.getValue().toString();
			 }
		}
		
		se.getServiceElementTypeId();
	}
	

	public String getRemoteIpAddress() {
		return remoteIpAddress;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public String getRemoteDeviceId() {
		return remoteDeviceId;
	}

	public String getRemotePlatform() {
		return remotePlatform;
	}
	

	
	public String getRemoteVersion() {
		return remoteVersion;
	}

	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.NetworkConnection#getNetworkLayer()
	 */
	@Override
	public NetworkLayer getNetworkLayer() {
		return NetworkLayer.Layer2;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.snmp.NetworkConnection#getIfIndex()
	 */
	@Override
	public int getIfIndex() {
		return connifIndex;
	}

}
