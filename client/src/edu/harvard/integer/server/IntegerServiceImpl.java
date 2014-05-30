package edu.harvard.integer.server;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.discovery.DiscoveryManagerInterface;
import edu.harvard.integer.service.discovery.DiscoveryServiceInterface;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.distribution.ServiceTypeEnum;
import edu.harvard.integer.service.event.EventManagerInterface;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManager;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerInterface;

// TODO: Auto-generated Javadoc
/**
 * The server side implementation of the RPC service.
 */
@ServletSecurity(@HttpConstraint(rolesAllowed = { "DirectUser", "CASUser" }))
public class IntegerServiceImpl extends RemoteServiceServlet implements
		IntegerService {
	
	/**
	 * Serial Version UID
	 */
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

	@Override
	public ServiceElement[] getTopLevelElements() throws Exception {
		ServiceElement[] serviceElements;

		try {
			ServiceElementAccessManagerInterface serviceElementService = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
			System.out.println("Enter getTopLevelElements: serviceElementService: " + serviceElementService);
			
			serviceElements = serviceElementService.getTopLevelServiceElements();
		} 
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return serviceElements;
	}


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
			subnet.setAddress(new Address( "10.240.127.0" ));
			subnet.setMask(new Address("255.255.255.0"));
			
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
				
				e.printStackTrace();
				
			}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#startDiscovery(java.lang.String, java.lang.String)
	 */
	@Override
	public void startDiscovery(String address, String mask) throws Exception {
		
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		subnet.setAddress(new Address(address ));
		subnet.setMask(new Address(mask));
		
		seed.setSubnet(subnet);
		seed.setRadius(Integer.valueOf(0));
		
		List<Credential> credentials = new ArrayList<Credential>();
		
		SnmpV2cCredentail credential = new SnmpV2cCredentail();
		credential.setReadCommunity("integer");
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
			
			e.printStackTrace();
			
		}
	}

	@Override
	public DeviceDetails getDeviceDetails(ID id) throws Exception {
		DeviceDetails deviceDetails = null;

		try {
			ServiceElementAccessManagerInterface serviceElementService = DistributionManager.getManager(ManagerTypeEnum.ServiceElementAccessManager);
			
			deviceDetails = serviceElementService.getDeviceDetails(id);
		} 
		catch (IntegerException e) {
			e.printStackTrace();
		}

		return deviceDetails;
	}
}
