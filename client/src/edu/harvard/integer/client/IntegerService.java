package edu.harvard.integer.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("integer")
public interface IntegerService extends RemoteService {
	
	/**
	 * Mib import.
	 *
	 * @param fileName the file name
	 * @param mib the mib
	 * @param standardMib the standard mib
	 * @return the string
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	String mibImport(String fileName, String mib, boolean standardMib) throws IllegalArgumentException;
	
	/**
	 * Gets the imported mibs.
	 *
	 * @return the imported mibs
	 * @throws IntegerException 
	 */
	MIBInfo[] getImportedMibs() throws Exception;
	
	
	/**
	 * Fake class used to force GWT to add classes to the whitelist. The white list is used to 
	 * say what classes can be serialized and sent to the client. The inherited abstract classes
	 * do not get added to the whitelist. 
	 * 
	 * @param GWTWhitelist
	 * @return GWTThitelist
	 */
	GWTWhitelist getGWTWhitelist(GWTWhitelist be);
}

