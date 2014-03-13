package edu.harvard.integer.server;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerLocalInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpObjectManagerLocalInterface;

// TODO: Auto-generated Javadoc
/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class IntegerServiceImpl extends RemoteServiceServlet implements
		IntegerService {
	
	/** The snmp service. */
	@EJB
	SnmpObjectManagerLocalInterface snmpService;
	
	/** The managed object service. */
	@EJB
	ManagementObjectCapabilityManagerLocalInterface managedObjectService;

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
		//ArrayList<MIBInfo> mibInfoList = new ArrayList<MIBInfo>();
		MIBInfo[] results;
		
		try {
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
	 * @see edu.harvard.integer.client.IntegerService#getAllServiceElementManagementObjects()
	 */
	@Override
	public List<ServiceElementManagementObject> getAllServiceElementManagementObjects()
			throws Exception {
		List<ServiceElementManagementObject> list;

		try {
			list = managedObjectService.getAllServiceElementManagementObjects();
		} 
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return list;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.client.IntegerService#getEvents()
	 */
	@Override
	public List<Object> getEvents() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
}
