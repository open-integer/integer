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
package edu.harvard.integer.service.discovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.access.ElementAccess;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.snmp.DiscoverCdpTopologyTask;
import edu.harvard.integer.service.discovery.snmp.ExclusiveNode;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;
import edu.harvard.integer.service.discovery.subnet.DiscoverNode;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;
import edu.harvard.integer.service.discovery.subnet.Ipv4Range;
import edu.harvard.integer.service.discovery.subnet.SubnetUtil;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.distribution.ServiceTypeEnum;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;


/**
 * The Class NetworkDiscovery provides method for IP node discovery or topology discovery. 
 * 
 * The first stage of the discovery is the scanning subnet to find out any IP devices on subnet.
 * The second stage of the discovery is the service element discovery.
 * The third stage of the discovery is the topology discovery.
 * 
 *  The topology discovery is based Layer2, Layer3 and end host found in the first stage.
 *  When the topology discovery is done, it should have connection information between IP nodes.
 * 
 * It manages 3 different pools for discovery.
 * 
 * Discovery request pool which controls how many concurrent request we should have.
 * Subnet discovery pool which controls how many concurrent subnet discovery we can have.
 * Element discovery pool which controls how many concurrent element discovery.we can have.
 *
 * @author dchan
 * @param <T> the generic type
 */
@SuppressWarnings("rawtypes")
public class NetworkDiscovery  implements NetworkDiscoveryBase {

	public static String IPIDENTIFY = "IpIdentify";
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(NetworkDiscovery.class);
	
	/**
	 * Map to keep track of each seed subnet tasks.  The key is the subnet id.
	 */
	
	private ConcurrentHashMap<String, DiscoverSubnetAsyncTask>  subnetTasks = new ConcurrentHashMap<>();
	
	
	/**
	 * Map to keep track of each found subnet tasks.  The key is the subnet id.
	 */
	private ConcurrentHashMap<String, DiscoverSubnetAsyncTask>  foundSubnetTask = new ConcurrentHashMap<>();
	
	
    /**
     * Map to hold discovered nodes which contains layer 2 connection information.
     */
	private List<DiscoverNode> linkLayerConnections = new ArrayList<DiscoverNode>();
	
	/**
	 * Map to hold discovered unknown service elements.  The key is the device id.
	 */
	private ConcurrentHashMap<String, ServiceElement>  unknownElmMap = new ConcurrentHashMap<>();
	

	/** The discover seed. */
	private final List<IpDiscoverySeed> discoverSeeds;
	
	/** The stop discovery. */
	private volatile boolean stopDiscovery;
		
	/** Top level polling variable bindings.  It is getting from IntegerInterface and instantiate in the beginning
	 *  for performance reason.
	 * 
	 */
	private List<VariableBinding> topLevelVBs;
	
	/**
	 * Use to store discovered SubNet.
	 * The key is the highest address plus the lowerest address of the subnet in a format as upperAddrr + ":" lowerAddrr
	 */
	private ConcurrentHashMap<String, DiscoverNet>  foundSubnets = new ConcurrentHashMap<>();
	
	
	/**
	 *  Hash map store IPAddress associated with an discovered node.  Take router as an example,
	 *  it can have more than one IP addresses.  During discovering
	 *  the discovering processing will skip any IP address in the map since the node they belong is already
	 *  discovered.
	 */
	private ConcurrentHashMap<String, String>  discoverdAddresses = new ConcurrentHashMap<>();
	
	
	/**
	 *  Hash map store System Name associated with an discovered node.  Take router as an example,
	 *  it can have more than one IP addresses.  During discovering
	 *  the discovering processing will skip any node in the map since the node they is already
	 *  discovered.
	 */
	private Map<String, DiscoverNode>  discoverdSystems = new HashMap<>();
	

	/**
	 * Discovery id to keep track of discovery.
	 */
	private final DiscoveryId discoverId;
	
	private final List<Subnet>  exclusiveSubnets;
	
	private final List<ExclusiveNode>  exclusiveNodes;
	

	/**
	 * Create a discovery  
	 * 
	 * @param seed
	 * @param toplevelVarBinds
	 * @param id
	 */
	public NetworkDiscovery(IpDiscoverySeed seed, List<VariableBinding> toplevelVarBinds, DiscoveryId id)  {
		
		if ( seed != null ) {
		      
			 exclusiveSubnets = seed.getExclusiveNet();
		     this.exclusiveNodes = seed.getExclusiveNodes(); 
		}
		else {
			
			exclusiveNodes = null;
			exclusiveSubnets = null;
		}
		
		this.discoverId = id;
		List<IpDiscoverySeed> seeds = new ArrayList<IpDiscoverySeed>();
		seeds.add(seed);
		this.discoverSeeds = seeds;
		this.topLevelVBs = toplevelVarBinds;
	}
	
	
	/**
	 * Discover network. Each subnet it will spawn a thread for discovery.
	 * The subnet information is contained in each IpDiscoverySeed
	 * @throws  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Future<Ipv4Range>> discoverNetwork()  {
		
		logger.debug("In discoverNetwork ");
		
		List<Future<Ipv4Range>> discFuture = new ArrayList<>();
		/**
		 * Create subnet tasks based on discover configuration subnet.
		 */
		if ( discoverSeeds != null ) {
			
			DiscoveryServiceInterface discoveryService = null;
			try {
				discoveryService = DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
			} catch (IntegerException e1) {
				
				logger.error("Error getting DiscoveryService " + e1.toString(), e1);
				return discFuture;
			}
		
			for ( IpDiscoverySeed discoverSeed : discoverSeeds ) {
			
				/**
				 * It is possible that discover seed contains exclusive subnets including itself.
				 * In this case, log it as an warning and go on.
				 */
				if ( exclusiveSubnets != null ) {
					
					if ( SubnetUtil.isSubnetInList(discoverSeed.getDiscoverNet(), exclusiveSubnets)) {
						logger.warn("Would not discover this exclusive subnet " + discoverSeed.getDiscoverNet().getCidr());
						continue;
					}
				}
				
				try {
					
					logger.info("Discover radius " + discoverSeed.getRadius());
					DiscoverSubnetAsyncTask<ElementAccess> subTask = null;
					
					subTask = new DiscoverSubnetAsyncTask(this, discoverSeed);
	                subnetTasks.put(subTask.getSeed().getSeedId(), subTask);
					
					Future<Ipv4Range> v = discoveryService.submitSubnetDiscovery(subTask);
					discFuture.add(v);
					
				} catch (IntegerException e) {
					
					logger.equals("Error on submit subnet discover...  " + e.toString() );
					e.printStackTrace();
				} 
			}
		}
		return discFuture;
	}



	/**
	 * 
	 * @param discoverNode
	 * @param subnetId
	 */
	public void removeAliasIp(DiscoverNode discoverNode, String subnetId) {
		
		boolean subnetComplete = removeIpAddressFromSubnet(discoverNode.getIpAddress(), subnetId);
		if ( subnetComplete ) {
			logger.info("Subnet discovery complete " + subnetId);
		}
	}
	


	/**
	 * Discovered element. -- Be called after discovered each service element. It is used to 
	 * indicate that discovery being finished.
	 *
	 * @param elm the discoverd element.
	 */
	public void discoveredElement(DiscoverNode discoverNode, String subnetId ) {
		
		DiscoveryServiceInterface discoveryService = null;
		try {
			discoveryService = DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
			discoveryService.discoveredServiceElement(discoverNode.getAccessElement());
		} 
		catch (IntegerException e) {
			logger.error("Error saveing ServiceElement " + discoverNode.getAccessElement());
		}		
		/**
		 * If the discovered node contains protocol connection, store it 
		 */
		if ( discoverNode.hasProtocolConnection() && discoverNode.isFwdNode() ) {		
			
			logger.info("Store node contain connectionns " + discoverNode.getIpAddress() + " " + discoverNode.getSysName());
			addConnectionNode(subnetId, discoverNode);
		}
		
		boolean subnetComplete = removeIpAddressFromSubnet(discoverNode.getIpAddress(), subnetId);
		if ( subnetComplete ) {
			logger.info("Subnet discovery complete " + subnetId);
		}
	}
	
	
	/**
	 * Error occur -- Call when errors occurs during discovering.
	 *
	 * @param errorCode the error code.
	 * @param msg the associated message.
	 */
	public void discoverErrorOccur( ErrorCodeInterface errorCode, String msg ) {
		try {
			((DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService)).discoveryError(discoverId, errorCode, null);
		} catch (IntegerException e) {
			
			logger.error("Error sending error " + errorCode + " args " + msg);
		}		
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void discoverTopologyComplete() {
		
		try {
			((DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService)).discoveryTopologyComplete();
		} catch (IntegerException e) {
			
			logger.error("Error sending error " + e.getLocalizedMessage());
		}
	}
	
	
	
	/**
	 * This method being called when there is no response for a IP address during discover.
	 * If on the database there is a service element is associated with this IP address.  It should issue
	 * a no response message.
	 * 
	 * @param ipAddress
	 * @param subnetId
	 */
	public void ipAddressNoResponse( String ipAddress, String subnetId ) {
		
		logger.debug("No response for this IP " + ipAddress + " for this subnet " + subnetId );
		try {
			ServiceElementAccessManagerInterface access = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			ServiceElement se =  access.getServiceElementByIpAddress(ipAddress);
			if ( se != null ) {
				DiscoveryServiceInterface dsif = (DiscoveryServiceInterface) DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
				dsif.discoveryServiceElementNoResponse(se, ipAddress);
			}
						
		} catch (IntegerException e) {
		
			e.printStackTrace();
			logger.error("Unable to call Service Manager to mark no response on a service element !! " + e.toString());
		}
		removeIpAddressFromSubnet(ipAddress, subnetId);
	}
	
	
	

	/**
	 * Checks if the discovery being stopped.
	 *
	 * @return true, if is stop discovery
	 */
	public boolean isStopDiscovery() {
		return stopDiscovery;
	}



	/**
	 * Remove IP Address discovery from subnet map. This method being called a IP device being done 
	 * with discovery.  Return true if all subnet done with discovered.
	 * 
	 * @param ip
	 * @param subnetid
	 * @throws  
	 */
	@SuppressWarnings("unchecked")
	private synchronized boolean removeIpAddressFromSubnet( String ip, String subnetid )  {
		
		DiscoveryServiceInterface discoveryService = null;
		/**
		 * Scan though the new discovered subnet and discover them.
		 */
		try {
			discoveryService = DistributionManager.getService(ServiceTypeEnum.DiscoveryService);
		} 
		catch (IntegerException e1) {
			
			logger.error("Error getting DiscoveryService " + e1.toString(), e1);
			return false;
		}
		
		if ( subnetid != null ) {
			
			DiscoverSubnetAsyncTask<ElementAccess> subTask = subnetTasks.get(subnetid);		
			if ( subTask != null ) {
				
				subTask.removeDiscoverNode(ip);
				if ( subTask.discoveryNodeCount() == 0 ) {
					
					subnetTasks.remove(subnetid);
					logger.info("Discovered subnet **** " + subnetid);
				}
				if ( subnetTasks.size() == 0 ) {
				    for ( DiscoverNet dnet : foundSubnets.values() ) {
				    	
				    	IpDiscoverySeed discoverSeed = new IpDiscoverySeed( discoverSeeds.get(0).getAuths(), dnet );
				    	try {
				    		logger.info("Found another subnet " + dnet.getStartIp() + ":" + dnet.getEndIp());
				    		
				    		if ( exclusiveSubnets != null ) {
								
								if ( SubnetUtil.isSubnetInList(discoverSeed.getDiscoverNet(), exclusiveSubnets)) {
									logger.warn("Would not discover this exclusive subnet " + discoverSeed.getDiscoverNet().getCidr());
									continue;
								}
							}
				    		
							DiscoverSubnetAsyncTask<ElementAccess> foundSubTask = new DiscoverSubnetAsyncTask(this, discoverSeed);
			                foundSubnetTask.put(foundSubTask.getSeed().getSeedId(), subTask);
							discoveryService.submitSubnetDiscovery(foundSubTask);
							
						} catch (IntegerException e) {
							
							logger.equals("Error on submit found subnet discover...  " + e.toString() );
							e.printStackTrace();
						} 
				    }
				}
			}
			else {
				
			   subTask = foundSubnetTask.get(subnetid);	
			   if ( subTask != null ) {
					
					subTask.removeDiscoverNode(ip);
					if ( subTask.discoveryNodeCount() == 0 ) {
						
						foundSubnetTask.remove(subnetid);
						logger.info("Discovered on new found subnet **** " + subnetid);
					}
			   }
			}
		}
		if ( foundSubnetTask.size() == 0 && subnetTasks.size() == 0 ) {
			
			if ( linkLayerConnections.size() > 0 ) {
				
				logger.info("Linklayer connection node count  **** " + linkLayerConnections.size());				
				DiscoverCdpTopologyTask task = new DiscoverCdpTopologyTask(linkLayerConnections, this);
				task.call();
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param subnetId
	 * @param dn
	 */
	private synchronized  void addConnectionNode( String subnetId, DiscoverNode dn ) {
		
		linkLayerConnections.add(dn);
	}
	
	

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.discovery.NetworkDiscoveryBase#stopDiscovery()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void stopDiscovery() {
		
		for ( DiscoverSubnetAsyncTask<ElementAccess> subTask : subnetTasks.values() ) {
			
			subTask.stopDiscover();
		}
		stopDiscovery = true;
		
	}


	/**
	 * Retrieve the top level of VB (Variable Bindings in System Group)
	 * @return
	 */
	public List<VariableBinding> getTopLevelVBs() {
		return topLevelVBs;
	}



	/*
	 * Return the discovery id.
	 */
	public DiscoveryId getDiscoverId() {
		return discoverId;
	}

	
	public void addUnknownServiceElement( String elmId, ServiceElement se ) {
		
		unknownElmMap.put( elmId, se);
	}
	
	public ServiceElement getUnknownServiceElement( String elmId ) {
		
		return unknownElmMap.get(elmId);
	}
	
	
	
	/**
	 * Store a found subnet into foundSubnet map and mark IPAddress as discovered address.
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public boolean putFoundSubNet( String ip, String mask, int preRadius ) {
		
		if ( preRadius == 0 ) {
			return false;
		}
        DiscoverNet dnet = new DiscoverNet(ip, mask, --preRadius);
		String rangeKey = dnet.getStartIp() + ":" + dnet.getEndIp();
		
		boolean found = true;
		if ( foundSubnets.get(rangeKey) == null ) {
			
			logger.info("Found another subnet during discovery " + dnet.getNetworkAddress() + " " + dnet.getNetmask());
			foundSubnets.put(rangeKey, dnet);
			found = false;
		}
		
		discoverdAddresses.put(ip, ip);
		return found;
	}

	
	/**
	 * Find disocvered IP address 
	 * @param ipAddress
	 * @return
	 */
	public String findDiscoveredIpAddresses( String ipAddress ) {
		return discoverdAddresses.get(ipAddress);
	}
	

	/**
	 * Check if the node with a same system name is already discovered.
	 * If not discovered, it will put into the map.
	 * 
	 * @param sysName
	 * @return
	 */
	public synchronized boolean alreadyDiscovered( String sysName, DiscoverNode dnode ) {
		
		if ( discoverdSystems.get(sysName.trim()) != null ) {
			return true;
		}
		
		logger.info("Store a system in discoveredSystems " + sysName);
		discoverdSystems.put(sysName.trim(), dnode);
		return false;
	}
	

	
	public List<ExclusiveNode> getExclusiveNodes() {
		return exclusiveNodes;
	}
	
}
