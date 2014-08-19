package edu.harvard.integer.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.discovery.DiscoveryId;
import edu.harvard.integer.common.event.Event;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.SystemErrorCodes;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SnmpGlobalReadCredential;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.MapItemPosition;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.DiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
import edu.harvard.integer.service.distribution.CallWithRetry;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.event.EventManagerInterface;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManager;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.selection.SelectionManagerInterface;
import edu.harvard.integer.service.topology.TopologyManagerInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;

/**
 * The server side implementation of the RPC service.
 */
@ServletSecurity(@HttpConstraint(rolesAllowed = { "DirectUser", "CASUser" }))
public class IntegerServiceImpl extends RemoteServiceServlet implements
		IntegerService {

	/** Serial Version UID. */
	private static final long serialVersionUID = 1L;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#mibImport(java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public String mibImport(final String fileName, final String mib,
			final boolean standardMib) throws IllegalArgumentException, Exception {

		CallWithRetry<String, SnmpManagerInterface> callWithRetry = new CallWithRetry<String, SnmpManagerInterface>(
				3, SnmpManagerInterface.class) {

			@Override
			public String call(SnmpManagerInterface snmpService)
					throws IntegerException {
				MIBImportInfo mibImportInfo = new MIBImportInfo();
				mibImportInfo.setFileName(fileName);
				mibImportInfo.setMib(mib);
				mibImportInfo.setStandardMib(standardMib);

				ArrayList<MIBImportInfo> mibList = new ArrayList<MIBImportInfo>();
				mibList.add(mibImportInfo);

				snmpService.importMib(mibList.toArray(new MIBImportInfo[0]));

				return "";
			}
		};

	
		callWithRetry.invokeCall();
	
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getImportedMibs()
	 */
	@Override
	public MIBInfo[] getImportedMibs() throws Exception {
		MIBInfo[] results = null;

		CallWithRetry<MIBInfo[], SnmpManagerInterface> callWithRetry = new CallWithRetry<MIBInfo[], SnmpManagerInterface>(
				3, SnmpManagerInterface.class) {

			@Override
			public MIBInfo[] call(SnmpManagerInterface snmpService)
					throws IntegerException {

				MIBInfo[] results = null;

				System.out.println("Enter getImportedMibs: snmpService: "
						+ snmpService);
				results = snmpService.getImportedMibs();

				return results;
			}
		};

		results = callWithRetry.invokeCall();

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#getBaseEntity(edu.harvard.integer
	 * .common.BaseEntity)
	 */
	@Override
	public GWTWhitelist getGWTWhitelist(GWTWhitelist be) {

		return be;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getCapabilities()
	 */
	@Override
	public List<Capability> getCapabilities() throws Exception {

		List<Capability> capabilityList = null;

		CallWithRetry<List<Capability>, ManagementObjectCapabilityManager> callWithRetry = new CallWithRetry<List<Capability>, ManagementObjectCapabilityManager>(
				3, ManagementObjectCapabilityManager.class) {

			@Override
			public List<Capability> call(
					ManagementObjectCapabilityManager managedObjectService)
					throws IntegerException {
				List<Capability> capabilityList = null;

				capabilityList = managedObjectService.getCapabilities();

				return capabilityList;
			}
		};

		capabilityList = callWithRetry.invokeCall();

		return capabilityList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getEvents()
	 */
	@Override
	public List<Object> getEvents() throws Exception {
		EventManagerInterface manager = DistributionManager
				.getManager(ManagerTypeEnum.EventManager);

		List<Object> events = new ArrayList<Object>();
		for (Event event : manager.getAllEvents()) {
			events.add(event);
		}
		return events;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getTopLevelElements()
	 */
	@Override
	public ServiceElement[] getTopLevelElements() throws Exception {
		ServiceElement[] serviceElements = null;

		CallWithRetry<ServiceElement[], ServiceElementAccessManagerInterface> callWithRetry = new CallWithRetry<ServiceElement[], ServiceElementAccessManagerInterface>(
				3, ServiceElementAccessManagerInterface.class) {

			@Override
			public ServiceElement[] call(
					ServiceElementAccessManagerInterface serviceElementService)
					throws IntegerException {
				ServiceElement[] serviceElements;

				System.out
						.println("Enter getTopLevelElements: serviceElementService: "
								+ serviceElementService);

				serviceElements = serviceElementService
						.getTopLevelServiceElements();

				System.out.println("Return " + serviceElements.length
						+ " Top level services elememts");

				return serviceElements;
			}
		};

		serviceElements = callWithRetry.invokeCall();

		return serviceElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#getServiceElementByParentId
	 * (edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElement[] getServiceElementByParentId(final ID id)
			throws Exception {
		ServiceElement[] serviceElements = null;

		CallWithRetry<ServiceElement[], ServiceElementAccessManagerInterface> callWithRetry = new CallWithRetry<ServiceElement[], ServiceElementAccessManagerInterface>(
				3, ServiceElementAccessManagerInterface.class) {

			@Override
			public ServiceElement[] call(
					ServiceElementAccessManagerInterface serviceElementService)
					throws IntegerException {
				ServiceElement[] serviceElements = null;

				serviceElements = serviceElementService
						.getServiceElementByParentId(id);

				System.out.println("Found " + serviceElements.length
						+ " ServiceElements for " + id);

				return serviceElements;
			}
		};

		serviceElements = callWithRetry.invokeCall();

		return serviceElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#startDiscovery()
	 */
	@Override
	public void startDiscovery() throws Exception {

		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		Address address = new Address();
		address.setAddress("10.240.127.0");
		subnet.setAddress(new Address("10.240.127.0", "255.255.255.0"));

		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));

		List<Credential> credentials = new ArrayList<Credential>();

		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integerrw");
		credential.setWriteCommunity("integerrw");
		;

		credentials.add(credential);
		seed.setCredentials(credentials);

		List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
		topologySeeds.add(seed);
		DiscoveryRule rule = new DiscoveryRule();

		rule.setTopologySeeds(topologySeeds);
		rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
		rule.setCreated(new Date());

		try {
			DiscoveryManagerInterface manager = DistributionManager
					.getManager(ManagerTypeEnum.DiscoveryManager);

			manager.startDiscovery(rule);
		} catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#startDiscovery(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public void startDiscovery(final String address, final String mask)
			throws Exception {

		CallWithRetry<DiscoveryId, DiscoveryManagerInterface> callWithRetry = new CallWithRetry<DiscoveryId, DiscoveryManagerInterface>(
				3, DiscoveryManagerInterface.class) {

			@Override
			public DiscoveryId call(DiscoveryManagerInterface manager)
					throws IntegerException {

				IpTopologySeed seed = new IpTopologySeed();

				Subnet subnet = new Subnet();
				subnet.setAddress(new Address(address, mask));

				seed.setName("Subnet: " + subnet);
				seed.setDescription("Default for " + subnet);

				seed.setSubnet(subnet);
				seed.setRadius(Integer.valueOf(1));

				seed.setSnmpRetriesServiceElementDiscovery(Integer.valueOf(2));
				seed.setSnmpTimeoutServiceElementDiscovery(Long.valueOf(800));

				seed.setSnmpRetriesTopologyDiscovery(Integer.valueOf(2));
				seed.setSnmpTimeoutTopologyDiscovery(Long.valueOf(800));

				SnmpGlobalReadCredential[] globalCredentails = manager
						.getAllGlobalCredentails();
				List<Credential> credentials = new ArrayList<Credential>();

				for (SnmpGlobalReadCredential snmpGlobalReadCredential : globalCredentails) {
					credentials.addAll(snmpGlobalReadCredential
							.getCredentials());
				}

				seed.setCredentials(credentials);

				List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
				topologySeeds.add(seed);
				DiscoveryRule rule = new DiscoveryRule();

				rule.setName("Subnet: " + subnet);

				rule.setTopologySeeds(topologySeeds);
				rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
				rule.setCreated(new Date());

				DiscoveryRule dbRule = manager.getDiscoveryRuleByName(rule
						.getName());

				if (dbRule == null)
					dbRule = manager.updateDiscoveryRule(rule);

				manager.startDiscovery(dbRule);

				return null;
			}
		};

		callWithRetry.invokeCall();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#getDeviceDetails(edu.harvard
	 * .integer.common.ID)
	 */
	@Override
	public DeviceDetails getDeviceDetails(final ID id) throws Exception {
		DeviceDetails deviceDetails = null;

		CallWithRetry<DeviceDetails, ServiceElementAccessManagerInterface> callWithRetry = new CallWithRetry<DeviceDetails, ServiceElementAccessManagerInterface>(
				3, ServiceElementAccessManagerInterface.class) {

			@Override
			public DeviceDetails call(
					ServiceElementAccessManagerInterface serviceElementService)
					throws IntegerException {
				DeviceDetails deviceDetails = null;

				deviceDetails = serviceElementService.getDeviceDetails(id);

				return deviceDetails;
			}
		};

		deviceDetails = callWithRetry.invokeCall();

		return deviceDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getBlankSelection()
	 */
	@Override
	public Selection getBlankSelection() throws Exception {
		Selection selection = null;

		CallWithRetry<Selection, SelectionManagerInterface> callWithRetry = new CallWithRetry<Selection, SelectionManagerInterface>(
				3, SelectionManagerInterface.class) {

			@Override
			public Selection call(SelectionManagerInterface selectionService)
					throws IntegerException {
				Selection selection = null;

				selection = selectionService.getBlankSelection();

				return selection;
			}
		};

		selection = callWithRetry.invokeCall();

		return selection;
	}

	/**
	 * Prints the filter node.
	 * 
	 * @param node
	 *            the node
	 * @param b
	 *            the b
	 * @param indent
	 *            the indent
	 * @return the string buffer
	 */
	private StringBuffer printFilterNode(FilterNode node, StringBuffer b,
			String indent) {
		b.append(indent).append(node.getName()).append(":")
				.append(node.getItemId().getIdentifier());
		if (node.getChildren() != null) {
			b.append('\n');

			for (FilterNode child : node.getChildren()) {
				printFilterNode(child, b, indent + "  ");
			}

			if (node.getChildren().size() > 0)
				b.append('\n');
		}

		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#getServiceElementTypeById(edu
	 * .harvard.integer.common.ID)
	 */
	@Override
	public ServiceElementType getServiceElementTypeById(
			final ID serviceElementTypeId) throws Exception {
		ServiceElementType serviceElementType = null;

		CallWithRetry<ServiceElementType, ServiceElementDiscoveryManagerInterface> callWithRetry = new CallWithRetry<ServiceElementType, ServiceElementDiscoveryManagerInterface>(
				3, ServiceElementDiscoveryManagerInterface.class) {

			@Override
			public ServiceElementType call(
					ServiceElementDiscoveryManagerInterface discMgr)
					throws IntegerException {

				ServiceElementType serviceElementType = null;

				serviceElementType = discMgr
						.getServiceElementTypeById(serviceElementTypeId);

				return serviceElementType;
			}
		};

		serviceElementType = callWithRetry.invokeCall();

		return serviceElementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getAllNetworks()
	 */
	@Override
	public Network[] getAllNetworks() throws Exception {
		Network[] networks = null;

		CallWithRetry<Network[], TopologyManagerInterface> callWithRetry = new CallWithRetry<Network[], TopologyManagerInterface>(
				3, TopologyManagerInterface.class) {

			@Override
			public Network[] call(TopologyManagerInterface topologyService)
					throws IntegerException {
				Network[] networks = null;

				try {

					networks = topologyService.getAllNetworks();

					System.out.println("getAllNetworks return "
							+ networks.length + " netowrks");
				} catch (IntegerException e) {
					throw new IntegerException(
							e,
							SystemErrorCodes.UnknownException);
				}

				return networks;
			}
		};

		networks = callWithRetry.invokeCall();

		return networks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getNetworkInformation()
	 */
	@Override
	public NetworkInformation getNetworkInformation() throws Exception {
		NetworkInformation networkInfo = null;

		CallWithRetry<NetworkInformation, TopologyManagerInterface> callWithRetry = new CallWithRetry<NetworkInformation, TopologyManagerInterface>(
				3, TopologyManagerInterface.class) {

			@Override
			public NetworkInformation call(
					TopologyManagerInterface topologyService)
					throws IntegerException {
				NetworkInformation networkInfo = null;

				networkInfo = topologyService.getNetworkInformation();

				return networkInfo;

			}
		};

		networkInfo = callWithRetry.invokeCall();

		return networkInfo;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getAllDiscoveryRules()
	 */
	@Override
	public DiscoveryRule[] getAllDiscoveryRules() throws Exception {
		DiscoveryRule[] discoveryRules = null;

		CallWithRetry<DiscoveryRule[], DiscoveryManagerInterface> callWithRetry = new CallWithRetry<DiscoveryRule[], DiscoveryManagerInterface>(
				3, DiscoveryManagerInterface.class) {

			@Override
			public DiscoveryRule[] call(
					DiscoveryManagerInterface discoveryService)
					throws IntegerException {
				DiscoveryRule[] discoveryRules = null;

				discoveryRules = discoveryService.getAllDiscoveryRules();

				System.out.println("getAllDiscoveryRules return "
						+ discoveryRules.length + " discoveryRules");
				return discoveryRules;
			}
		};

		discoveryRules = callWithRetry.invokeCall();

		return discoveryRules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getAllGlobalCredentails()
	 */
	@Override
	public SnmpGlobalReadCredential[] getAllGlobalCredentails()
			throws Exception {

		SnmpGlobalReadCredential[] snmpGlobalReadCredentials;

		CallWithRetry<SnmpGlobalReadCredential[], DiscoveryManagerInterface> callWithRetry = new CallWithRetry<SnmpGlobalReadCredential[], DiscoveryManagerInterface>(
				3, DiscoveryManagerInterface.class) {

			@Override
			public SnmpGlobalReadCredential[] call(
					DiscoveryManagerInterface discoveryService)
					throws IntegerException {

				SnmpGlobalReadCredential[] snmpGlobalReadCredentials;

				snmpGlobalReadCredentials = discoveryService
						.getAllGlobalCredentails();

				System.out.println("getAllGlobalCredentails return "
						+ snmpGlobalReadCredentials.length
						+ " snmpGlobalReadCredentials");

				return snmpGlobalReadCredentials;
			}
		};

		snmpGlobalReadCredentials = callWithRetry.invokeCall();

		return snmpGlobalReadCredentials;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getAllIpTopologySeeds()
	 */
	@Override
	public IpTopologySeed[] getAllIpTopologySeeds() throws Exception {
		IpTopologySeed[] ipTopologySeeds = null;

		CallWithRetry<IpTopologySeed[], DiscoveryManagerInterface> callWithRetry = new CallWithRetry<IpTopologySeed[], DiscoveryManagerInterface>(
				3, DiscoveryManagerInterface.class) {

			@Override
			public IpTopologySeed[] call(
					DiscoveryManagerInterface discoveryService)
					throws IntegerException {
				IpTopologySeed[] ipTopologySeeds;

				ipTopologySeeds = discoveryService.getAllIpTopologySeeds();

				System.out.println("getAllIpTopologySeeds return "
						+ ipTopologySeeds.length + " ipTopologySeeds");

				return ipTopologySeeds;
			}
		};

		callWithRetry.invokeCall();

		return ipTopologySeeds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.integer.client.IntegerService#getPositionsByNetwork()
	 */
	@Override
	public MapItemPosition[] getPositionsByNetwork(final ID networkId)
			throws Exception {

		MapItemPosition[] mapItemPositions = null;

		CallWithRetry<MapItemPosition[], TopologyManagerInterface> callWithRetry = new CallWithRetry<MapItemPosition[], TopologyManagerInterface>(
				3, TopologyManagerInterface.class) {

			@Override
			public MapItemPosition[] call(
					TopologyManagerInterface topologyService)
					throws IntegerException {

				MapItemPosition[] mapItemPositions = null;

				try {

					mapItemPositions = topologyService
							.getPositionsByMap(networkId);

					System.out.println("getPositionsByNetwork return "
							+ mapItemPositions.length
							+ " mapItemPositions for " + networkId.getName());

				} catch (IntegerException e) {
					throw new IntegerException(
							e,
							SystemErrorCodes.UnknownException);
				}

				return mapItemPositions;
			}
		};

		mapItemPositions = callWithRetry.invokeCall();

		return mapItemPositions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.client.IntegerService#updateMapItemPosition(edu.harvard
	 * .integer.common.topology.MapItemPosition)
	 */
	@Override
	public void updateMapItemPosition(final MapItemPosition position)
			throws Exception {

		CallWithRetry<MapItemPosition, TopologyManagerInterface> callWithRetry = new CallWithRetry<MapItemPosition, TopologyManagerInterface>(
				3, TopologyManagerInterface.class) {

			@Override
			public MapItemPosition call(TopologyManagerInterface topologyService)
					throws IntegerException {

				MapItemPosition mapItemPosition = topologyService
						.updateMapItemPosition(position);

				return mapItemPosition;
			}
		};

		callWithRetry.invokeCall();

	}
}
