package edu.harvard.integer.server;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.ejb.EJB;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.harvard.integer.client.IntegerService;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.service.managementobject.snmp.SnmpObjectManagerLocalInterface;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class IntegerServiceImpl extends RemoteServiceServlet implements
		IntegerService {
	
	/** The snmp service. */
	@EJB
	SnmpObjectManagerLocalInterface snmpService;

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
			/*if (results != null && results.length > 0) {
				for (MIBInfo mibInfo : results)
					mibInfoList.add(mibInfo);
			}*/
			for (int i = 0; i < results.length; i++) {
				
				String moduleName = ""+results[i].getName();
				String lastUpdate = ""+results[i].getModule().getLastUpdated();
				String oid = ""+results[i].getModule().getOid();
				String description = ""+results[i].getModule().getDescription();
				String vendor = ""+results[i].getVendor();
				
				System.out.println("Module: " + moduleName + " Last Update: " + lastUpdate 
						+ " OID: " + oid + " Desc: " + description + " Vendor: " + vendor);
			}
			
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return results;
	}


}
