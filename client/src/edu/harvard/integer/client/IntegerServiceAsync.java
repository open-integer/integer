/*
 * 
 */
package edu.harvard.integer.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.topology.Capability;

/**
 * The async counterpart of <code>IntegerService</code>.
 */
public interface IntegerServiceAsync {

	/**
	 * Mib import.
	 *
	 * @param fileName the file name
	 * @param mib the mib
	 * @param standardMib the standard mib
	 * @param callback the callback
	 */
	void mibImport(String fileName, String mib, boolean standardMib, AsyncCallback<String> callback);

	/**
	 * Gets the imported mibs.
	 *
	 * @param callback the callback
	 * @return the imported mibs
	 */
	void getImportedMibs(AsyncCallback<MIBInfo[]> callback);

	/**
	 * Fake class used to force GWT to add classes to the whitelist. The white list is used to 
	 * say what classes can be serialized and sent to the client. The inherited abstract classes
	 * do not get added to the whitelist. 
	 * 
	 * @param GWTWhitelist
	 * @return GWTThitelist
	 */
	void getGWTWhitelist(GWTWhitelist be, AsyncCallback<GWTWhitelist> calllback);

	void addCapability(Capability capability, AsyncCallback<Void> callback);

	void getAllCapabilities(AsyncCallback<List<Capability>> callback);
}
