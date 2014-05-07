
/*
 * 
 */
package edu.harvard.integer.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.ServiceElement;

// TODO: Auto-generated Javadoc
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
	 * @param be the be
	 * @param calllback the calllback
	 * @return GWTThitelist
	 */
	void getGWTWhitelist(GWTWhitelist be, AsyncCallback<GWTWhitelist> calllback);

	/**
	 * Gets the capabilities.
	 *
	 * @param callback the callback
	 * @return the capabilities
	 */
	void getCapabilities(AsyncCallback<List<Capability>> callback);

	/**
	 * Gets the events.
	 *
	 * @param callback the callback
	 * @return the events
	 */
	void getEvents(AsyncCallback<List<Object>> callback);

	/**
	 * Gets the top level elements.
	 *
	 * @param callback the callback
	 * @return the top level elements
	 */
	void getTopLevelElements(AsyncCallback<ServiceElement[]> callback);

	/**
	 * Gets the service element by parent id.
	 *
	 * @param id the id
	 * @param callback the callback
	 * @return the service element by parent id
	 */
	void getServiceElementByParentId(ID id, AsyncCallback<ServiceElement[]> callback);

	void startDiscovery(AsyncCallback<Void> callback);

	void startDiscovery(String address, String mask,
			AsyncCallback<Void> callback);

}
