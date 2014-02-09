package edu.harvard.integer.server;

import java.util.ArrayList;
import java.util.List;

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
		
		List<MIBImportInfo> mibList = new ArrayList<MIBImportInfo>();
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
		MIBInfo[] mibInfos = null;
		try {
			mibInfos = snmpService.getImportedMibs();
		}
		catch (IntegerException e) {
			throw new Exception(e.getMessage());
		}
		
		return mibInfos;
	}


}
