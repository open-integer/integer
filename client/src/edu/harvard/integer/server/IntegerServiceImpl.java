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
import edu.harvard.integer.common.event.Event;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Filter;
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
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.DiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerInterface;
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

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#mibImport(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String mibImport(String fileName, String mib, boolean standardMib)
			throws IllegalArgumentException {
		MIBImportInfo mibImportInfo = new MIBImportInfo();
		mibImportInfo.setFileName(fileName);
		mibImportInfo.setMib(mib);
		mibImportInfo.setStandardMib(standardMib);
		
		ArrayList<MIBImportInfo> mibList = new ArrayList<MIBImportInfo>();
		mibList.add(mibImportInfo);
		try {
			SnmpManagerInterface snmpService = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
			snmpService.importMib(mibList.toArray(new MIBImportInfo[0]));
		} catch (IntegerException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return "";
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getImportedMibs()
	 */
	@Override
	public MIBInfo[] getImportedMibs() throws Exception {
		MIBInfo[] results;
		
		try {
			SnmpManagerInterface snmpService = DistributionManager.getManager(ManagerTypeEnum.SnmpManager);
			
			System.out.println("Enter getImportedMibs: snmpService: " + snmpService);
			results = snmpService.getImportedMibs();

		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getBaseEntity(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public GWTWhitelist getGWTWhitelist(GWTWhitelist be) {
		
		return be;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getCapabilities()
	 */
	@Override
	public List<Capability> getCapabilities() throws Exception {
		List<Capability> capabilityList;
		try {
			ManagementObjectCapabilityManager managedObjectService = DistributionManager.getManager(ManagerTypeEnum.ManagementObjectCapabilityManager);
			
			capabilityList = managedObjectService.getCapabilities();
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		return capabilityList;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getEvents()
	 */
	@Override
	public List<Object> getEvents() throws Exception {
		EventManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.EventManager);
		
		List<Object> events = new ArrayList<Object>();
		for (Event event : manager.getAllEvents()) {
			events.add(event);
		}
		return events;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getTopLevelElements()
	 */
	@Override
	public ServiceElement[] getTopLevelElements() throws Exception {
		ServiceElement[] serviceElements;

		try {
			ServiceElementAccessManagerInterface serviceElementService = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
			System.out.println("Enter getTopLevelElements: serviceElementService: " + serviceElementService);
			
			serviceElements = serviceElementService.getTopLevelServiceElements();
			
			System.out.println("Return " + serviceElements.length + " Top level services elememts");
		} 
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return serviceElements;
	}


	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getServiceElementByParentId(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElement[] getServiceElementByParentId(ID id) throws Exception {
		ServiceElement[] serviceElements = null;

		try {
			ServiceElementAccessManagerInterface serviceElementService = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
			serviceElements = serviceElementService.getServiceElementByParentId(id);
			System.out.println("Found " + serviceElements.length + " ServiceElements for " + id);
		} 
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return serviceElements;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#startDiscovery()
	 */
	@Override
	public void startDiscovery() throws Exception {
			
			IpTopologySeed seed = new IpTopologySeed();
			Subnet subnet = new Subnet();
			Address address = new Address();
			address.setAddress("10.240.127.0");
			subnet.setAddress(new Address( "10.240.127.0", "255.255.255.0"));
			
			seed.setSubnet(subnet);
			seed.setRadius(Integer.valueOf(0));
			
			List<Credential> credentials = new ArrayList<Credential>();
			
			SnmpV2cCredentail credential = new SnmpV2cCredentail();
			credential.setReadCommunity("integerrw");
			credential.setWriteCommunity("integerrw");;
			
			credentials.add(credential);
			seed.setCredentials(credentials);
			
			List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
			topologySeeds.add(seed);
			DiscoveryRule rule = new DiscoveryRule();
			
			rule.setTopologySeeds(topologySeeds);
			rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
			rule.setCreated(new Date());
			
			try {
				DiscoveryManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.DiscoveryManager);
				
				manager.startDiscovery(rule);
			} catch (IntegerException e) {
				throw new Exception(e.getMessage());
			}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#startDiscovery(java.lang.String, java.lang.String)
	 */
	@Override
	public void startDiscovery(String address, String mask) throws Exception {
		
		DiscoveryManagerInterface manager = DistributionManager.getManager(ManagerTypeEnum.DiscoveryManager);
		
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		subnet.setAddress(new Address(address, mask ));
		
		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));
		
		SnmpGlobalReadCredential[] globalCredentails = manager.getAllGlobalCredentails();
		List<Credential> credentials = new ArrayList<Credential>();
		
		for (SnmpGlobalReadCredential snmpGlobalReadCredential : globalCredentails) {
			credentials.addAll(snmpGlobalReadCredential.getCredentials());
		}

		seed.setCredentials(credentials);
		
		List<IpTopologySeed> topologySeeds = new ArrayList<IpTopologySeed>();
		topologySeeds.add(seed);
		DiscoveryRule rule = new DiscoveryRule();
		
		rule.setName("Subnet: " + address + " mask " + mask);
		
		rule.setTopologySeeds(topologySeeds);
		rule.setDiscoveryType(DiscoveryTypeEnum.ServiceElement);
		rule.setCreated(new Date());
		
		DiscoveryRule dbRule = manager.getDiscoveryRuleByName(rule.getName());
		
		if (dbRule == null)
			dbRule = manager.updateDiscoveryRule(rule);
		
		try {
			
			manager.startDiscovery(dbRule);
			
		} catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getDeviceDetails(edu.harvard.integer.common.ID)
	 */
	@Override
	public DeviceDetails getDeviceDetails(ID id) throws Exception {
		DeviceDetails deviceDetails = null;

		try {
			ServiceElementAccessManagerInterface serviceElementService = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
			deviceDetails = serviceElementService.getDeviceDetails(id);
		} 
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}

		return deviceDetails;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getBlankSelection()
	 */
	@Override
	public Selection getBlankSelection() throws Exception {
		Selection selection = null;
		
		try {
			SelectionManagerInterface selectionService = DistributionManager.getManager(ManagerTypeEnum.SelectionManager);
			selection = selectionService.getBlankSelection();
			
//			
			StringBuffer b = new StringBuffer();
			for (Filter filter : selection.getFilters()) {
//				
//				b.append("Technologies: \n");
//				for (FilterNode filterNode : filter.getTechnologies()) {
//					printFilterNode(filterNode, b, "\tTechnology: ").append('\n');
//				}
//				b.append("Link:\n");
//				for (FilterNode filterNode : filter.getLinkTechnologies()) {
//					printFilterNode(filterNode, b, "\tLink Technology: ").append('\n');
//				}
//
				b.append("Categories: \n");
				for (FilterNode category : filter.getCategories())
					printFilterNode(category, b, "\tCategory: ").append('\n');
//					//b.append("\tCategory: ").append(category.getName()).append('\n');
//				
//				b.append("Criticaliy\n");
//				for (CriticalityEnum criticatlity :  filter.getCriticalities() )
//					b.append("Criticatly: ").append(criticatlity).append('\n');
			}
//			
			System.out.println("Return Blank selection " + b.toString());
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return selection;
	}

	/**
	 * Prints the filter node.
	 *
	 * @param node the node
	 * @param b the b
	 * @param indent the indent
	 * @return the string buffer
	 */
	private StringBuffer printFilterNode(FilterNode node, StringBuffer b, String indent) {
		b.append(indent).append(node.getName()).append(":").append(node.getItemId().getIdentifier());
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
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getServiceElementTypeById(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElementType getServiceElementTypeById(ID serviceElementTypeId) throws Exception {
		ServiceElementType serviceElementType = null;
		
		try {
			ServiceElementDiscoveryManagerInterface discMgr = DistributionManager.getManager(ManagerTypeEnum.ServiceElementDiscoveryManager);
			serviceElementType = discMgr.getServiceElementTypeById(serviceElementTypeId);
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return serviceElementType;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getAllNetworks()
	 */
	@Override
	public Network[] getAllNetworks() throws Exception {
		Network[] networks;

		try {
			TopologyManagerInterface topologyService = DistributionManager.getManager(ManagerTypeEnum.TopologyManager);
			
			networks = topologyService.getAllNetworks();
			
			System.out.println("getAllNetworks return " + networks.length + " netowrks");
		} 
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		return networks;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getNetworkInformation()
	 */
	@Override
	public NetworkInformation getNetworkInformation() throws Exception {
		NetworkInformation networkInfo = null;
		
		try {
			TopologyManagerInterface topologyService = DistributionManager.getManager(ManagerTypeEnum.TopologyManager);
			networkInfo = topologyService.getNetworkInformation();
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return networkInfo;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getAllDiscoveryRules()
	 */
	@Override
	public DiscoveryRule[] getAllDiscoveryRules() throws Exception {
		DiscoveryRule[] discoveryRules;

		try {
			DiscoveryManagerInterface discoveryService = DistributionManager.getManager(ManagerTypeEnum.DiscoveryManager);
			
			discoveryRules = discoveryService.getAllDiscoveryRules();
			
			System.out.println("getAllDiscoveryRules return " + discoveryRules.length + " discoveryRules");
		} 
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		return discoveryRules;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getAllGlobalCredentails()
	 */
	@Override
	public SnmpGlobalReadCredential[] getAllGlobalCredentails() throws Exception {
		SnmpGlobalReadCredential[] snmpGlobalReadCredentials;

		try {
			DiscoveryManagerInterface discoveryService = DistributionManager.getManager(ManagerTypeEnum.DiscoveryManager);
			
			snmpGlobalReadCredentials = discoveryService.getAllGlobalCredentails();
			
			System.out.println("getAllGlobalCredentails return " + snmpGlobalReadCredentials.length + " snmpGlobalReadCredentials");
		} 
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		return snmpGlobalReadCredentials;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getAllIpTopologySeeds()
	 */
	@Override
	public IpTopologySeed[] getAllIpTopologySeeds() throws Exception {
		IpTopologySeed[] ipTopologySeeds;

		try {
			DiscoveryManagerInterface discoveryService = DistributionManager.getManager(ManagerTypeEnum.DiscoveryManager);
			
			ipTopologySeeds = discoveryService.getAllIpTopologySeeds();
			
			System.out.println("getAllIpTopologySeeds return " + ipTopologySeeds.length + " ipTopologySeeds");
		} 
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		return ipTopologySeeds;
	}
}
