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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import edu.harvard.integer.access.AccessPort;
import edu.harvard.integer.access.AccessTypeEnum;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.discovery.DiscoveryStatusEnum;
import edu.harvard.integer.common.event.DiscoveryCompleteEvent;
import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.IntegerPropertyNames;
import edu.harvard.integer.common.properties.LongPropertyNames;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.common.type.displayable.DisplayableInterface;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.discovery.element.ElementDiscoverTask;
import edu.harvard.integer.service.discovery.snmp.DiscoverCdpTopologyTask;
import edu.harvard.integer.service.discovery.subnet.DiscoverNet;
import edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask;
import edu.harvard.integer.service.discovery.subnet.Ipv4Range;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.event.DiscoveryCompleteEventDAO;

/**
 * The discovery service holds the list of discovery's that are running in the
 * system. The status of all discoveries is mentioned as well. The discovery
 * service will also start any discoveries that were active when the server was
 * shut down and need to complete.
 * 
 * @author David Taylor
 * 
 */
@Singleton
@Startup
public class DiscoveryService extends BaseService implements
		DiscoveryServiceInterface {

	@Inject
	private Logger logger;

	@Inject
	private ServiceElementDiscoveryManagerInterface serviceElementDiscoveryManager;

	@Inject
	private PersistenceManagerInterface persistenceManager;

	private int subTaskLimit = 10;

	/**
	 * Use to limit the number of element discovery task.
	 */
	private int elementTaskLimit = 20;

	private ExecutorService subPool = Executors
			.newFixedThreadPool(subTaskLimit);

	/**
	 * Used to manager the task pool for element discovery.
	 */
	private ExecutorService elementPool = Executors
			.newFixedThreadPool(elementTaskLimit);

	/**
	 * Discovery sequence id used for network discovery. This id only valid
	 * within an integer server.
	 */
	private long discoverySeqId = 0;

	private Map<DiscoveryId, RunningDiscovery> runningDiscoveries = new ConcurrentHashMap<DiscoveryId, RunningDiscovery>();

	/**
	 * Called after service has been created. Initialize of the discovery
	 * service is done here.
	 */
	@PostConstruct
	private void init() {
		try {
			logger.info("Discovery service starting.... on server "
					+ IntegerProperties.getInstance().getIntProperty(
							IntegerPropertyNames.ServerId));
		} catch (IntegerException e) {

			e.printStackTrace();
			logger.error("Error getting serverID " + e.toString());
		}

	}

	public ExecutorService getSubPool() {
		return subPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * submitElementDiscoveryTask
	 * (edu.harvard.integer.service.discovery.element.ElementDiscoverTask)
	 */
	@Override
	public void submitElementDiscoveryTask(ElementDiscoverTask discoveryTask) {
		elementPool.submit(discoveryTask);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * submitSubnetDiscovery
	 * (edu.harvard.integer.service.discovery.subnet.DiscoverSubnetAsyncTask)
	 */
	@Override
	public Future<Ipv4Range> submitSubnetDiscovery(DiscoverSubnetAsyncTask task) {
		return subPool.submit(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * startDiscovery(edu.harvard.integer.common.topology.DiscoveryRule)
	 */
	@Override
	public DiscoveryId startDiscovery(DiscoveryRule rule)
			throws IntegerException {

		DiscoveryId id = new DiscoveryId();
		id.setServerId(IntegerProperties.getInstance().getLongProperty(
				LongPropertyNames.ServerId));
		id.setDiscoveryId(discoverySeqId++);

		switch (rule.getDiscoveryType()) {
		case Both:
		case ServiceElement:
			startServiceElementDiscovery(id, rule.getTopologySeeds());
			break;

		case Topology:
			startTopologyDiscovery(rule.getTopologySeeds());

		case None:
			logger.error("NO discovery type specifed! Discovery will not run for rule "
					+ rule.getName());
		}

		return id;
	}

	/**
	 * Start a topology discovery using the list of topology seeds. This
	 * discovery assumes that the service element discovery for the networks
	 * include by the toplogy seeds have already been discovered.
	 * 
	 * @param topologySeeds
	 *            . List of seeds for the neteworks to be discovered.
	 */
	private void startTopologyDiscovery(List<IpTopologySeed> topologySeeds) {

	}

	/**
	 * Start a service element discovery on the devices specified by the
	 * topology seeds. This method will return after the discovery has been
	 * started. To get the status of the discovery call
	 * getDiscoveryStatus(DiscoveryId id).
	 * 
	 * @param topologySeeds
	 * @throws IntegerException
	 */
	private void startServiceElementDiscovery(DiscoveryId id,
			List<IpTopologySeed> topologySeeds) throws IntegerException {

		for (IpTopologySeed ipTopologySeed : topologySeeds) {

			IpDiscoverySeed seed = createIpDiscoverySeed(ipTopologySeed);

			try {
				NetworkDiscovery discovery = serviceElementDiscoveryManager
						.startDiscovery(id, seed);
				RunningDiscovery runningDiscovery = runningDiscoveries.get(id);
				if (runningDiscovery == null) {
					runningDiscovery = new RunningDiscovery();
					runningDiscovery.setId(id);
					List<NetworkDiscovery> discoveries = new ArrayList<NetworkDiscovery>();
					runningDiscovery.setRunningDiscoveries(discoveries);

					logger.info("Add to running queue ServiceElement discovery of "
							+ seed.getSeedId());
					runningDiscoveries.put(id, runningDiscovery);
				}

				runningDiscovery.getRunningDiscoveries().add(discovery);

			} catch (IntegerException e) {
				logger.error("Error starting ServiceElementDiscovery "
						+ e.toString());
				e.printStackTrace();
				throw e;
			}
		}

	}

	/**
	 * Create a IpDescoverySeed from the IpToplogySeed.
	 * 
	 * @param ipTopologySeed
	 * @return
	 */
	private IpDiscoverySeed createIpDiscoverySeed(IpTopologySeed ipTopologySeed) {

		DiscoverNet net = createDiscoverNet(ipTopologySeed.getSubnet());

		IpDiscoverySeed seed = new IpDiscoverySeed(net,
				ipTopologySeed.getCredentials());

		seed.setRadius(ipTopologySeed.getRadius());

		if (ipTopologySeed.getSnmpRetriesServiceElementDiscovery() != null)
			seed.setSnmpRetries(ipTopologySeed
					.getSnmpRetriesServiceElementDiscovery().intValue());

		if (ipTopologySeed.getSnmpRetriesServiceElementDiscovery() != null)
			seed.setSnmpTimeout(ipTopologySeed
					.getSnmpTimeoutServiceElementDiscovery().intValue());

		seed.setNotDiscoverNet(createDiscoverNets(ipTopologySeed.getNetExclustions()));

		seed.setPorts(createAccessPorts(ipTopologySeed.getAlternateSNMPports()));

		return seed;
	}

	private List<DiscoverNet> createDiscoverNets(List<Subnet> subnets) {
		List<DiscoverNet> notDiscoverNets = new ArrayList<DiscoverNet>();

		if (subnets != null) {
			for (Subnet subnet : subnets) {
				notDiscoverNets.add(createDiscoverNet(subnet));
			}
		}

		return notDiscoverNets;
	}

	private List<AccessPort> createAccessPorts(List<Integer> ports) {
		List<AccessPort> accessPorts = new ArrayList<AccessPort>();

		if (ports != null) {
			for (Integer integer : ports) {

				AccessPort accessPort = new AccessPort(integer,
						AccessTypeEnum.SNMPv2c);
				accessPorts.add(accessPort);
			}
		}

		return accessPorts;
	}

	private DiscoverNet createDiscoverNet(Subnet subnet) {

		DiscoverNet net = new DiscoverNet(subnet.getAddress().getAddress(),
				subnet.getAddress().getMask());

		return net;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * discoveryComplete(edu.harvard.integer.common.discovery.DiscoveryId)
	 */
	@Override
	public void discoveryComplete(DiscoveryId discoveryId)
			throws IntegerException {
		RunningDiscovery runningDiscovery = runningDiscoveries.get(discoveryId);

		if (runningDiscovery != null)
			logger.info("Discovery complete for "
					+ discoveryId.getDiscoveryId());
		else
			logger.warn("Discovery " + discoveryId.getDiscoveryId()
					+ " not running. Unable to mark as complete!");

		DiscoveryCompleteEvent discoveryComplete = new DiscoveryCompleteEvent();
		discoveryComplete.setDiscoveryStatus(DiscoveryStatusEnum.Complete);
		discoveryComplete.setName("Discovery Complete");

		DiscoveryCompleteEventDAO dao = persistenceManager
				.getDiscoveryCompleteEventDAO();
		dao.update(discoveryComplete);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * discoveryError(edu.harvard.integer.common.discovery.DiscoveryId,
	 * edu.harvard.integer.common.exception.ErrorCodeInterface,
	 * edu.harvard.integer.common.util.DisplayableInterface[])
	 */
	@Override
	public void discoveryError(DiscoveryId id, ErrorCodeInterface errorCode,
			DisplayableInterface[] args) {
		logger.error("Error during discovery " + id + " Error " + errorCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * discoveredServiceElement
	 * (edu.harvard.integer.common.topology.ServiceElement)
	 */
	@Override
	public void discoveredServiceElement(ServiceElement accessElement) {
		logger.info("Found ServiceElemet " + accessElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * getRunningDiscoveries()
	 */
	@Override
	public DiscoveryId[] getRunningDiscoveries() throws IntegerException {
		List<DiscoveryId> discoveries = new ArrayList<DiscoveryId>();
		for (DiscoveryId discoveryId : runningDiscoveries.keySet()) {
			RunningDiscovery runningDiscovery = runningDiscoveries
					.get(discoveryId);
			if (runningDiscovery != null)
				discoveries.add(discoveryId);
		}

		return (DiscoveryId[]) discoveries.toArray(new DiscoveryId[discoveries
				.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.discovery.DiscoveryServiceInterface#stopDiscovery
	 * (edu.harvard.integer.common.discovery.DiscoveryId)
	 */
	@Override
	public void stopDiscovery(DiscoveryId id) {

		RunningDiscovery runningDiscovery = runningDiscoveries.get(id);
		if (runningDiscovery != null) {

			System.out.println("Call Discovery " + id.toString());
			runningDiscovery.stopDiscovery();
		} else {
			System.out.println("Null RunningDiscovery " + id.getServerId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * getDiscoveryStatus(edu.harvard.integer.common.ID)
	 */
	@Override
	public DiscoveryCompleteEvent[] getDiscoveryStatus(ID serviceElementId)
			throws IntegerException {
		DiscoveryCompleteEventDAO dao = persistenceManager
				.getDiscoveryCompleteEventDAO();

		return dao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * discoveryServiceElementNoResponse
	 * (edu.harvard.integer.common.topology.ServiceElement, java.lang.String)
	 */
	@Override
	public void discoveryServiceElementNoResponse(ServiceElement se,
			String ipAddress) {

		logger.info("No response on Service Element " + se.getName()
				+ " on IP " + ipAddress);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * submitSubnetTopologyDiscovery
	 * (edu.harvard.integer.service.discovery.snmp.DiscoverSubnetTopologyTask)
	 */
	@Override
	public Future<Void> submitSubnetTopologyDiscovery(
			DiscoverCdpTopologyTask task) {

		return subPool.submit(task);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.service.discovery.DiscoveryServiceInterface#
	 * discoveryTopologyComplete()
	 */
	@Override
	public void discoveryTopologyComplete() throws IntegerException {

		logger.info("Complete topology discovery. ");

	}

}
