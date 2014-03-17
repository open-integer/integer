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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.commons.net.util.SubnetUtils;

import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.AccessUtil;
import edu.harvard.integer.access.Authentication;
import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.discovery.DiscoveryManager;
import edu.harvard.integer.service.discovery.element.ElementDiscoverCB;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;



/**
 * The Class DiscoverSubnet is used to do a subnet discover.
 * During discover, if "seed" nodes contains on discoverNodes will be discovered first.
 *
 * @author dchan
 */
public class DiscoverSubnetTask implements Callable<DiscoveredNet> {

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public DiscoveredNet call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
//	/** Specify the network of the subnet. */
//	final private String network;
//	
//	/** Network Mask of the Subnet. */
//	final private String netmask;
//
//	/** If it is true, done with discovery. */
//	private boolean doneDiscovery;
//	
//	
//	/**
//	 * Used to make sure every nodes added are in the subnet range.
//	 */
//	private SubnetUtils subUtils;
//
//	/**
//	 * The management ports used for discovery.  If null, default port will be used.
//	 */
//	private List<AccessPort>  accessPorts;
//	
//
//
//	/**
//	 * Discover nodes to be discovered before auto-discovered on the subnet.  
//	 * In general, they are considering the seed nodes.
//	 */
//	private List<DiscoverNode> discoverNodes;
//	
//	/** The cb. */
//	final private ElementDiscoverCB<ServiceElement> cb;
//	
//	
//	/**
//	 * Authentication for accessing discover nodes.
//	 */
//	private final List<Authentication>  access;
//	
//	
//	
//	/**http://i2.cdn.turner.com/cnn/dam/assets/111029012252-china-population-control-story-top.jpg
//	 * Instantiates a new discover subnet.
//	 *
//	 * @param network the network
//	 * @param netmask the netmask
//	 * @param access the access
//	 * @param cb the cb
//	 */
//	public DiscoverSubnetTask( String network, String netmask, 
//			                   List<Authentication> access,
//			                   ElementDiscoverCB<ServiceElement> cb ) {
//		
//		this.access = access;
//		this.network = network;
//		this.netmask = netmask;
//		this.cb = cb;
//		
//		subUtils = new SubnetUtils(network, netmask);		
//	}
//	
//	/**
//	 * Return true if "ipAddr" is in the range of this subnet.
//	 *
//	 * @param ipAddr the ip addr
//	 * @return true, if is in range
//	 */
//	public boolean isInRange( String ipAddr ) {
//		
//		return subUtils.getInfo().isInRange(ipAddr);
//	}
//	
//	
//	/**
//	 * Gets the network of the subnet.
//	 *
//	 * @return the network
//	 */
//	public String getNetwork() {
//		return network;
//	}
//
//	/**
//	 * Gets the netmask of the subnet.
//	 *
//	 * @return the netmask
//	 */
//	public String getNetmask() {
//		return netmask;
//	}
//
//	
//	/**
//	 * Checks if is done discovery.
//	 *
//	 * @return true, if is done discovery
//	 */
//	public boolean isDoneDiscovery() {
//		return doneDiscovery;
//	}
//
//	/**
//	 * Sets the done discovery.
//	 *
//	 * @param doneDiscovery the new done discovery
//	 */
//	public void setDoneDiscovery(boolean doneDiscovery) {
//		this.doneDiscovery = doneDiscovery;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.util.concurrent.Callable#call()
//	 */
//	@Override
//	public DiscoveredNet call() throws Exception {
//		
//		List<Future<DiscoverNode>> futures = new ArrayList<>(); 
//		/**
//		 * 
//		 */
//		if ( discoverNodes != null ) {
//			
//			for ( DiscoverNode node : discoverNodes ) {
//				
//				int extraAuthIndex = 0;
//				int defaultPort = -1;
//				ElementEndPoint elmEpt = node.getElementEndPoint();
//				if ( elmEpt == null ) {	
//					
//					if ( access == null || access.size() == 0) {
//						throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
//					}
//					defaultPort = AccessUtil.getDefaultPort(access.get(0).getAccessType());
//					elmEpt = new ElementEndPoint(node.getIpAddress(), defaultPort, access.get(0));
//					extraAuthIndex++;	
//				}
//				ElementDiscoverTask task = new ElementDiscoverTask(cb, elmEpt);
//				if ( accessPorts != null ) {
//					
//					for ( AccessPort p : accessPorts ) {
//						if ( p.getPort() == defaultPort ) {
//							continue;
//						}
//						task.addOtherPort(p);
//					}
//				}
//				for ( int i=extraAuthIndex;i<access.size(); i++ ) {
//					task.addOtherAuth(access.get(i));
//				}
//				futures.add(DiscoveryManager.getInstance().sutmitElementTask(task));
//			}
//		}
//		Ipv4Range range = new Ipv4Range(subUtils.getInfo().getLowAddress(), subUtils.getInfo().getHighAddress());
//		
//		while ( range.hasNext() ) {
//			
//			String ip = range.next();
//			boolean skip = false;
//			
//			/**
//			 * Skip any node which provided already on the seed nodes.
//			 */
//			for ( DiscoverNode node : discoverNodes ) {
//				if ( ip.equals(node.getIpAddress()) ) {
//					skip = true;
//					break;
//				}
//			}
//			if ( skip ) {
//				continue;
//			}
//			if ( access == null || access.size() == 0) {
//				throw new IntegerException(null, NetworkErrorCodes.NoAuthentication);
//			}
//			int defaultPort = AccessUtil.getDefaultPort(access.get(0).getAccessType());
//			ElementEndPoint elmEpt = new ElementEndPoint(ip, defaultPort, access.get(0));
//			
//			ElementDiscoverTask task = new ElementDiscoverTask(cb, elmEpt);
//			if ( accessPorts != null ) {
//				
//				for ( AccessPort p : accessPorts ) {
//					if ( p.getPort() == defaultPort ) {
//						continue;
//					}
//					task.addOtherPort(p);
//				}
//			}
//			for ( int i=1;i<access.size(); i++ ) {
//				task.addOtherAuth(access.get(i));
//			}
//			futures.add(DiscoveryManager.getInstance().sutmitElementTask(task));
//			
//		}
//		
//		DiscoveredNet dNet = new DiscoveredNet(network, netmask);
//		for ( Future<DiscoverNode> f : futures ) {
//			
//			DiscoverNode serviceElm = f.get();
//			dNet.getElmMap().put(serviceElm.getIpAddress(), serviceElm);
//		}		
//		return dNet;
//	}
//	
//
//	/**
//	 * Add a discover node for this subnet.
//	 *
//	 * @param node the node
//	 * @throws IntegerException the integer exception
//	 */
//	public void addDiscoverNode( DiscoverNode node ) throws IntegerException {
//		
//		if ( !isInRange(node.getIpAddress()) ) {
//			throw new IntegerException(null, NetworkErrorCodes.OutOfSubnetRangeError);
//		}
//		if ( discoverNodes == null ) {
//			discoverNodes = new ArrayList<>();
//		}		
//		discoverNodes.add(node);
//	}
//	
//	
//	/**
//	 * Return the discover node count.
//	 *
//	 * @return the discover node count
//	 */
//	public int getDiscoverNodeCount() {
//		
//		if ( discoverNodes == null ) {
//			return 0;
//		}
//			
//		return discoverNodes.size();
//	}
//	
//	
//	/**
//	 * Return the DiscoverNode on index i.
//	 *
//	 * @param i the i
//	 * @return the discover node
//	 */
//	public DiscoverNode getDiscoverNode( int i ) {
//		
//		if ( discoverNodes == null || discoverNodes.size() <= 0 ) {
//			return null;
//		}
//		return discoverNodes.get(i);
//	}
//	
//	
//	/**
//	 * Gets the access ports.
//	 *
//	 * @return the access port used for discovery.
//	 */
//	public List<AccessPort> getAccessPorts() {
//		return accessPorts;
//	}
//
//	/**
//	 * Set the access ports used for discovery.
//	 *
//	 * @param accessPorts the new access ports
//	 */
//	public void setAccessPorts(List<AccessPort> accessPorts) {
//		this.accessPorts = accessPorts;
//	}
//
//		
}
