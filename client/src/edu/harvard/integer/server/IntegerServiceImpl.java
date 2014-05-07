package edu.harvard.integer.server;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SnmpV2cCredentail;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.Credential;
import edu.harvard.integer.common.topology.DiscoveryRule;
import edu.harvard.integer.common.topology.DiscoveryTypeEnum;
import edu.harvard.integer.common.topology.IpTopologySeed;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.Subnet;
import edu.harvard.integer.service.discovery.DiscoveryServiceInterface;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerInterface;
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

	/** The snmp service. */
	@EJB
	private SnmpManagerInterface snmpService;
	
	/** The managed object service. */
	@EJB
	private ManagementObjectCapabilityManagerInterface managedObjectService;
	
	@EJB
	private ServiceElementAccessManagerInterface serviceElementService;

	@EJB
	private DiscoveryServiceInterface discoveryService;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceElement[] getTopLevelElements() throws Exception {
		ServiceElement[] serviceElements;

		try {
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
		ServiceElement[] serviceElements;

		try {
			serviceElements = serviceElementService.getServiceElementByParentId(id);
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
				discoveryService.startDiscovery(rule);
			} catch (IntegerException e) {
				
				e.printStackTrace();
				fail(e.toString());
			}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#startDiscovery(java.lang.String, java.lang.String)
	 */
	@Override
	public void startDiscovery(String address, String mask) throws Exception{
		
		IpTopologySeed seed = new IpTopologySeed();
		Subnet subnet = new Subnet();
		subnet.setAddress(new Address(address ));
		subnet.setMask(new Address(mask));
		
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
			discoveryService.startDiscovery(rule);
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
}
}
