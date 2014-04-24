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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;

import edu.harvard.integer.access.Access;
import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.AccessUtil;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.access.snmp.SnmpAuthentication;
import edu.harvard.integer.access.snmp.SnmpService;
import edu.harvard.integer.access.snmp.SnmpSysInfo;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.service.discovery.DiscoveryManager;
import edu.harvard.integer.service.discovery.IpDiscoverySeed;
import edu.harvard.integer.service.discovery.NetworkDiscovery;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode.DiscoverStageE;


/**
 * The Class DiscoverSubnetAsyncTask is the subnet discover task.
 * It will scan each IP on a subnet to determine a IP is associated with a device in the network or not.
 * 
 * If a given discovered seed contains more than one authentications or management port,
 * for an IP which is unreachable, it will scan it again by using different authentication or port.
 * 
 * If an IP address is reachable, it will create a task for detail discovery.  If an IP address
 * is un-reachable after trying all given authentications or port, it will skip it.
 * 
 *
 * @author dchan
 * @param <E> the element type
 * @param <T> the generic type
 */
public class DiscoverSubnetAsyncTask <E extends ElementAccess>  implements Callable<Ipv4Range>, ResponseListener {


	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(DiscoverSubnetAsyncTask.class);
	
	/** The seed. */
	private IpDiscoverySeed seed;

	/** If it is true, done with discovery. */
	private boolean doneDiscovery;
	

	
	/** The accesses. */
	private List<Access> accesses = new ArrayList<>();  

	/**
	 * The management ports used for discovery.  If null, default port will be used.
	 */
	private List<AccessPort>  accessPorts;
	
	/** The discover map. */
	private ConcurrentHashMap<String, DiscoverNode> discoverMap = new ConcurrentHashMap<>();
	
	/** The net disc. */
	private NetworkDiscovery  netDisc;
	
	
	
	/**
	 * Instantiates a new DiscoverSubnetAsyncTask object.
	 *
	 * @param dis the dis
	 * @param seed the seed
	 * @throws IntegerException the integer exception
	 */
	public DiscoverSubnetAsyncTask( NetworkDiscovery dis,
			                        IpDiscoverySeed seed) throws IntegerException {
		
		this.seed = seed;
		netDisc = dis;
		
		/**
		 * First create an access list and sort them.
		 */
		List<SnmpAuthentication>  auth = new ArrayList<>();
		for ( Authentication a : seed.getAuths() ) {
			if ( a instanceof SnmpAuthentication ) {
				
				SnmpAuthentication snmpauth = (SnmpAuthentication) a;
				snmpauth.setTimeOut(seed.getSnmpTimeout());
				snmpauth.setTryCount(seed.getSnmpRetries());
				
				auth.add(snmpauth);
			}
		}
		if ( auth.size() == 0 ) {
			throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
		}
		Collections.sort(auth);
		
		for ( SnmpAuthentication a : auth ) {
			
			if ( seed.getPorts() != null && seed.getPorts().size() > 0 ) {
			
				List<AccessPort> ports = seed.getPorts();
				for ( AccessPort port : ports ) {
            		if ( port.isAccessSupport(a.getAccessType())) {
            			Access ac = new Access(port.getPort(), a);
            			accesses.add(ac);
            		}
            	}				
			}
			else {
				Access ac = new Access(AccessUtil.getDefaultPort(a.getAccessType()), a);
				accesses.add(ac);
			}
		}
		
		/**
		 * Verify the start ip and end ip address.
		 */
		if ( seed.getStartIp() != null && ! seed.getDiscoverNet().isInRange(seed.getStartIp()) ) {
			throw new IntegerException(null, NetworkErrorCodes.OutOfSubnetRangeError);
		}
		
		if ( seed.getEndIp() != null && !seed.getDiscoverNet().isInRange(seed.getEndIp()) ) {
			throw new IntegerException(null, NetworkErrorCodes.OutOfSubnetRangeError);
		}
	}
	
	
	
	
	
	
	
	/**
	 * Return true if "ipAddr" is in the range of this subnet.
	 *
	 * @param ipAddr the ip addr
	 * @return true, if is in range
	 */
	public boolean isInRange( String ipAddr ) {
		
		if ( seed.getStartIp() != null ) {
			
			long startIpl = seed.getDiscoverNet().getStartIpi();  
			if ( seed.getDiscoverNet().getIpInteger(ipAddr) < startIpl ) {
				return false;
			}			
		}
		if ( seed.getEndIp() != null ) {
			
			long endIpl = seed.getDiscoverNet().getEndIpi();
			if ( seed.getDiscoverNet().getIpInteger(ipAddr) < endIpl ) {
				return false;
			}			
		}		
		return seed.getDiscoverNet().isInRange(ipAddr);
	}
	
	
	/* 
	 * Walk through each IP address and generated SNMP async get request to verify
	 * if it is reachable or not.
	 * 
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Ipv4Range call() throws Exception {
				
		String startIp = seed.getStartIp();
		if ( startIp == null ) {
			startIp = seed.getDiscoverNet().getStartIp(); 
		}
		
		String endIp = seed.getEndIp(); 
		if ( endIp == null ) {
			endIp = seed.getDiscoverNet().getEndIp();
		}
		logger.debug("Discover subnet startip " + startIp + " endip " + endIp );
        Ipv4Range range = new Ipv4Range(startIp, endIp);
        
        try {
        	while ( range.hasNext() ) {
        		
    			if ( netDisc.isStopDiscovery() ) {
    				break;
    			}
    			
    			String ip = range.next();
    			
    			DiscoverNode dn = new DiscoverNode(ip);
    			dn.setSubnetId(seed.getSeedId());
    			
    			dn.setAccess(accesses.get(0));
    			
    			discoverMap.put(dn.getIpAddress(), dn);
    					
    			PDU pdu = new PDU();
    			pdu.addAll(netDisc.getTopLevelVBs());
    			SnmpService.instance().getAsyncPdu(dn.getElementEndPoint(), pdu, this, ip);
    		}
        }
        catch ( Exception e ) {
        	e.printStackTrace();
        }
		return range;
	}


	
	/* 
	 * SNMP asynch request response handler.  If an IP address is un-reachable, try other scan based 
	 * on the access list.
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

			/**
			 * If unreachable 
			 */
			if ( (e.getErrorCode().getErrorCode().equals(NetworkErrorCodes.AuthError.name()) ||
					e.getErrorCode().getErrorCode().equals(NetworkErrorCodes.CannotReach.name())) &&
					!netDisc.isStopDiscovery() ) {
		
				for ( int i=0; i< accesses.size(); i++ ) {
					
					Access a = accesses.get(i);
					if ( dn.getAccess().equal(a) && i < ( accesses.size() - 1 ) ) {
						
						a = accesses.get(i+1);
						dn.setAccess(a);
						
						PDU pdu = event.getRequest();
						try {
							
							logger.debug("Try another " + dn.getElementEndPoint().toString());
							SnmpService.instance().getAsyncPdu(dn.getElementEndPoint(), pdu, this, dn.getIpAddress());
							return;							
						} 
						catch (IntegerException e1) {}						
					}
				}
			}
			/**
			 * Notify the network discovery that an ip address has no response.
			 */
			netDisc.ipAddressNoResponse((String) event.getUserObject(), seed.getSeedId());
			return;
		}
		
		/**
		 * At this point the scan ip is reachable.  Create a element discover task for detail discovery.
		 */
		dn.setStage(DiscoverStageE.DetailScan);
		PDU response = event.getResponse();
		ElementDiscoverTask<E> elmTask =  new ElementDiscoverTask<E>((NetworkDiscovery) netDisc, dn, new SnmpSysInfo(response));		
		DiscoveryManager.getInstance().sutmitElementTask(elmTask);		
	}
	
	
	/**
	 * Adds the access port.
	 *
	 * @param accessPort the access port
	 */
	public void addAccessPort( AccessPort accessPort ) {
		
		if ( accessPorts == null ) {
			accessPorts = new ArrayList<>();
		}
		accessPorts.add(accessPort);
	}
	
	
	/**
	 * Gets the next access.
	 *
	 * @param currAccess the curr access
	 * @return the next access
	 */
	public Access getNextAccess( Access currAccess ) {
		
		if ( accesses == null ) {
			return null;
		}		
		return null;
	}

	
	/**
	 * Checks if is done discovery.
	 *
	 * @return true, if is done discovery
	 */
	public boolean isDoneDiscovery() {
		return doneDiscovery;
	}


	/**
	 * Sets the done discovery.
	 *
	 * @param doneDiscovery the new done discovery
	 */
	public void setDoneDiscovery(boolean doneDiscovery) {
		this.doneDiscovery = doneDiscovery;
	}

	
	/**
	 * Gets the seed.
	 *
	 * @return the seed
	 */
	public IpDiscoverySeed getSeed() {
		return seed;
	}

	
	/**
	 * Discovery node count.
	 *
	 * @return the int
	 */
	public int discoveryNodeCount() {
		return discoverMap.size();
	}

	/**
	 * Removes the discover node.
	 *
	 * @param ip the ip
	 * @return the discover node
	 */
	public DiscoverNode removeDiscoverNode( String ip ) {
		return discoverMap.remove(ip);
	}
}
