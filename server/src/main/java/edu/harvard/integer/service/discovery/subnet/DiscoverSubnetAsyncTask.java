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
package edu.harvard.integer.service.discovery.subnet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.net.util.SubnetUtils;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.AccessUtil;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.snmp.CommonSnmpOids;
import edu.harvard.integer.access.snmp.SnmpAuthentication;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode.DiscoverStageE;

/**
 * @author dchan
 *
 */
public class DiscoverSubnetAsyncTask <T extends ElementAccess> implements Callable<Void>, ResponseListener {


	
	
	/** Specify the network of the subnet. */
	final private String network;
	
	/** Network Mask of the Subnet. */
	final private String netmask;

	/** If it is true, done with discovery. */
	private boolean doneDiscovery;
	
	
	/**
	 * Used to make sure every nodes added are in the subnet range.
	 */
	private SubnetUtils subUtils;
	
	/**
	 * 
	 */
	private List<Access> accesses = new ArrayList<>();  

	/**
	 * The management ports used for discovery.  If null, default port will be used.
	 */
	private List<AccessPort>  accessPorts;
	
	private ConcurrentHashMap<String, DiscoverNode> discoverMap = new ConcurrentHashMap<>();
	
	private NetworkDiscovery<T>  netDisc;
	
	/**
	 * 
	 * @param dis
	 * @param network
	 * @param netmask
	 * @param auths
	 * @throws IntegerException
	 */
	public DiscoverSubnetAsyncTask( NetworkDiscovery<T> dis,
			                        String network, String netmask, 
                                    List<Authentication> auths ) throws IntegerException {
		
		netDisc = dis;
		this.network = network;
		this.netmask = netmask;
		
		List<SnmpAuthentication>  auth = new ArrayList<>();
		for ( Authentication a : auths ) {
			if ( a instanceof SnmpAuthentication ) {
				auth.add((SnmpAuthentication) a);
			}
		}
		if ( auth.size() == 0 ) {
			throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
		}
		Collections.sort(auth);
		
		for ( SnmpAuthentication a : auth ) {
			
			Access ac = new Access(AccessUtil.getDefaultPort(a.getAccessType()), a);
			accesses.add(ac);
		}
		
		subUtils = new SubnetUtils(network, netmask);
	}
	
	
	/**
	 * 
	 * @param dis
	 * @param network
	 * @param netmask
	 * @param auths
	 * @param ports
	 * @throws IntegerException
	 */
	public DiscoverSubnetAsyncTask( NetworkDiscovery<T> dis,
                  String network, String netmask, 
                  List<Authentication> auths,
                  List<AccessPort> ports ) throws IntegerException {

            netDisc = dis;
            this.network = network;
            this.netmask = netmask;

            List<SnmpAuthentication>  auth = new ArrayList<>();
            for ( Authentication a : auths ) {
             
        	   if ( a instanceof SnmpAuthentication ) {
                 auth.add((SnmpAuthentication) a);
               }
            }
            if ( auth.size() == 0 ) {
                throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
            }
            Collections.sort(auth);

            for ( SnmpAuthentication a : auth ) {

            	for ( AccessPort port : ports ) {
            		if ( port.isAccessSupport(a.getAccessType())) {
            			Access ac = new Access(port.getPort(), a);
            			accesses.add(ac);
            		}
            	}
            }
            subUtils = new SubnetUtils(network, netmask);
     }
	
	
	
	
	
	/**
	 * Return true if "ipAddr" is in the range of this subnet.
	 *
	 * @param ipAddr the ip addr
	 * @return true, if is in range
	 */
	public boolean isInRange( String ipAddr ) {
		
		return subUtils.getInfo().isInRange(ipAddr);
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
				
        Ipv4Range range = new Ipv4Range(subUtils.getInfo().getLowAddress(), subUtils.getInfo().getHighAddress());
		while ( range.hasNext() ) {
	
			if ( netDisc.isStopDiscovery() ) {
				break;
			}
			String ip = range.next();
			DiscoverNode dn = new DiscoverNode(ip);
			dn.setAccess(accesses.get(0));
			
			discoverMap.put(dn.getIpAddress(), dn);
			
			
			
			PDU pdu = new PDU();
			pdu.addAll(CommonSnmpOids.sysVB);
			
			SnmpService.instance().getAsyncPdu(dn.getElementEndPoint(), pdu, this, ip);
		}
		return null;
	}


	
	/* 
	 * 
	 * (non-Javadoc)
	 * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.ResponseEvent)
	 */
	@Override
	public void onResponse(ResponseEvent event) {
		
		/**
		 * Alawys cancel async request when response has been received otherwise a memory leak is created.
		 */
		((Snmp)event.getSource()).cancel(event.getRequest(), this);
	
		DiscoverNode dn = discoverMap.get((String) event.getUserObject());
		try {
			SnmpService.assertPDU(event);
		} 
		catch (IntegerException e) {

			if ( e.getErrorCode().getErrorCode().equals(NetworkErrorCodes.AuthError.name()) ||
					e.getErrorCode().getErrorCode().equals(NetworkErrorCodes.CannotReach) &&
					!netDisc.isStopDiscovery() ) {
		
				for ( int i=0; i< accesses.size(); i++ ) {
					
					Access a = accesses.get(i);
					if ( dn.getAccess().equal(a) && i < ( accesses.size() - 1 ) ) {
						
						a = accesses.get(i+1);
						dn.setAccess(a);
						
						PDU pdu = event.getRequest();
						try {
							SnmpService.instance().getAsyncPdu(dn.getElementEndPoint(), pdu, this, dn.getIpAddress());
							return;							
						} 
						catch (IntegerException e1) {}						
					}
				}
			}
			
			netDisc.getCb().errorOccur((NetworkErrorCodes) e.getErrorCode(), "Error when reaching address " + dn.getIpAddress());
			return;
		}
		PDU response = event.getResponse();
		dn.setStage(DiscoverStageE.DetailScan);
		
		
		
	}
	
	
	public void addAccessPort( AccessPort accessPort ) {
		
		if ( accessPorts == null ) {
			accessPorts = new ArrayList<>();
		}
		accessPorts.add(accessPort);
	}
	
	
	public Access getNextAccess( Access currAccess ) {
		
		if ( accesses == null ) {
			return null;
		}
		
		
		
		return null;
	}

}
