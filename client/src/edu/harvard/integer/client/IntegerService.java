package edu.harvard.integer.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.harvard.integer.common.GWTWhitelist;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.common.topology.DeviceDetails;
import edu.harvard.integer.common.topology.ServiceElement;

// TODO: Auto-generated Javadoc
/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("integer")
public interface IntegerService extends RemoteService {

	/**
	 * Mib import.
	 * 
	 * @param fileName
	 *            the file name
	 * @param mib
	 *            the mib
	 * @param standardMib
	 *            the standard mib
	 * @return the string
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	String mibImport(String fileName, String mib, boolean standardMib)
			throws IllegalArgumentException;

	/**
	 * Gets the imported mibs.
	 * 
	 * @return the imported mibs
	 * @throws Exception
	 *             the exception
	 */
	MIBInfo[] getImportedMibs() throws Exception;

	/**
	 * Fake class used to force GWT to add classes to the whitelist. The white
	 * list is used to say what classes can be serialized and sent to the
	 * client. The inherited abstract classes do not get added to the whitelist.
	 * 
	 * @param be
	 *            the be
	 * @return GWTThitelist
	 */
	GWTWhitelist getGWTWhitelist(GWTWhitelist be);

	/**
	 * Gets the capabilities.
	 * 
	 * @return the capabilities
	 * @throws Exception
	 *             the exception
	 */
	List<Capability> getCapabilities() throws Exception;

	/**
	 * Gets the top level elements.
	 * 
	 * @return the top level elements
	 * @throws Exception
	 *             the exception
	 */
	ServiceElement[] getTopLevelElements() throws Exception;

	/**
	 * Gets the service element by parent id.
	 * 
	 * @param id
	 *            the id
	 * @return the service element by parent id
	 * @throws Exception
	 *             the exception
	 */
	ServiceElement[] getServiceElementByParentId(ID id) throws Exception;
	
	DeviceDetails getDeviceDetails(ID id) throws Exception;

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 * @throws Exception
	 *             the exception
	 */
	List<Object> getEvents() throws Exception;

	/**
	 * Start Discovery. This is only for testing the initial functionallity.
	 */
	void startDiscovery() throws Exception;

	/**
	 * Discovery a subnet using all the defaults. This will discovery all the
	 * nodes on the subnet. Only the given subnet will be discovered. (Radius ==
	 * 0)
	 * 
	 * @param address
	 * @param mask
	 * @throws IntegerException
	 */
	void startDiscovery(String address, String mask) throws Exception;
	
	Selection getBlankSelection() throws Exception;

}
