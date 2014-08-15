/*
 *  Copyright (c) 2014 Harvard University and the persons
 *  identified as authors of the code.  All rights reserved. 
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 * 	.    Redistributions of source code must retain the above copyright
 * 		 notice, this list of conditions and the following disclaimer.
 * 
 * 	.    Redistributions in binary form must reproduce the above copyright
 * 		 notice, this list of conditions and the following disclaimer in the
 * 		 documentation and/or other materials provided with the distribution.
 * 
 * 	.    Neither the name of Harvard University, nor the names of specific
 * 		 contributors, may be used to endorse or promote products derived from
 * 		 this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *      
 */

package edu.harvard.integer.service.distribution;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.distribution.DistributedManager;
import edu.harvard.integer.common.distribution.DistributedService;
import edu.harvard.integer.common.distribution.IntegerServer;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.SystemErrorCodes;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.LongPropertyNames;
import edu.harvard.integer.common.properties.StringPropertyNames;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.BaseServiceInterface;

/**
 * This class is used to lookup managers in the Integer system. The manager may
 * be running on the local server or on a remote server. The lookup of the
 * manager is the same in both cases.
 * <p>
 * Ex.
 * <p>
 * ServiceElementDiscoveryManager discoveryManager =
 * DitributionManager.getManager
 * (ManagerTypeEnum.ServiceElementDiscoveryManager);
 * 
 * <p>
 * To lookup a manager on a specific server the serverId will need to be passed
 * into the getManager() call.
 * 
 * @author David Taylor
 * 
 */
public class DistributionManager {

	private static final Logger logger = LoggerFactory
			.getLogger(DistributionManager.class);

	/**
	 * List of all managers in the Integer system regardless of where the
	 * manager is running. This list will be updated by the DistributionService.
	 * 
	 */
	private static DistributedManager[] managers = null;

	/**
	 * List of all Services running in the Integer system regardless of where
	 * the services are running. This list will be updated by the
	 * DistributionService.
	 */
	private static DistributedService[] services = null;

	/**
	 * List of IntegerServers. This list will be updated by the
	 * DistributionService.
	 */
	private static IntegerServer[] servers = null;

	/**
	 * This method is used to get a reference to a service. The service could be
	 * running on this server or a remote service. The returned instance can be
	 * used as if it was a local reference.
	 * <p>
	 * Ex:
	 * <p>
	 * DiscoveryService discoveryService =
	 * DistributionManager.getService(ServiceTypeEnum.DiscoveryService)
	 * 
	 * @param type
	 *            . ServiceTypeEnum of the service that is requested.
	 * @return reference to a Service.
	 * 
	 * @throws IntegerException
	 */
	public static <T extends BaseServiceInterface> T getService(
			ServiceTypeEnum type) throws IntegerException {

		String moduleName = IntegerProperties.getInstance().getProperty(
				StringPropertyNames.ModuleName);
		try {

			if (logger.isDebugEnabled())
				logger.debug("Lookup " + type + " module " + moduleName);

			String hostName = getHostNameForService(type);

			if (moduleName.length() > 1) {
				if (isLocalhost(type))
					return lookupLocalBean(hostName,
							getLocalServiceName(moduleName, type));
				else
					return lookupRemoteBean(getHostNameForService(type),
							getRemoteServiceName(moduleName, type));

			} else
				return lookupLocalBean("localhost", getLocalServiceName(type));
		} catch (IntegerException e) {
			if (SystemErrorCodes.ManagerNotFound.equals(e.getErrorCode())) {
				logger.error("Unable to find " + type + " with module "
						+ moduleName + " try 'integer/server-1.0'");
				return lookupRemoteBean(getHostNameForService(type),
						getLocalServiceName("integer/server-1.0", type));
			} else
				throw e;
		}
	}

	/**
	 * This call is used when there are multiple instances of the service
	 * running in the integer system.
	 * <p>
	 * This method is used to get a reference to a service. The service could be
	 * running on this server or a remote service. The returned instance can be
	 * used as if it was a local reference.
	 * 
	 * <p>
	 * Ex:
	 * <p>
	 * DiscoveryService discoveryService =
	 * DistributionService.getService(ServiceTypeEnum.DiscoveryService)
	 * 
	 * @param ServiceId
	 *            . The serverID that the service is running on.
	 * @param type
	 *            . ServiceTypeEnum of the service that is requested.
	 * @return reference to a Service.
	 * 
	 * @throws IntegerException
	 */
	public static <T extends BaseServiceInterface> T getService(Long serverId,
			ServiceTypeEnum type) throws IntegerException {

		String moduleName = IntegerProperties.getInstance().getProperty(
				StringPropertyNames.ModuleName);
		try {

			// if (logger.isDebugEnabled())
			logger.info("Lookup " + type + " module " + moduleName);

			if (moduleName.length() > 1)
				return lookupRemoteBean(getHostName(serverId),
						getLocalServiceName(moduleName, type));
			else
				return lookupRemoteBean(getHostName(serverId),
						getLocalServiceName(type));
		} catch (IntegerException e) {
			if (SystemErrorCodes.ManagerNotFound.equals(e.getErrorCode())) {
				logger.error("Unable to find " + type + " with module "
						+ moduleName + " try 'integer/server-1.0'");
				return lookupRemoteBean(getHostName(serverId),
						getLocalServiceName("integer/server-1.0", type));
			} else
				throw e;
		}
	}

	/**
	 * Used to find out if the server id specified is for this server instance
	 * or a remote server.
	 * 
	 * @param serverId
	 * @return true if the server ID is for this server instance.
	 *         <p>
	 *         False if this is not the server specified by the server id.
	 * 
	 */
	private static boolean isLocalHost(Long serverId) {
		try {
			Long id = IntegerProperties.getInstance().getLongProperty(
					LongPropertyNames.ServerId);

			if (id.equals(serverId))
				return true;
			else
				return false;

		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Get the hostname the specified server is running on.
	 * 
	 * @param serverId
	 * @return hostname of the server.
	 */
	public static String getHostName(Long serverId) {
		for (IntegerServer server : servers) {
			if (server.getServerId().equals(serverId)) {
				String hostUrl = server.getServerAddress().getAddress() + ":"
						+ server.getPort();

				if (logger.isDebugEnabled())
					logger.debug("Using " + hostUrl + " for serverId "
							+ serverId);

				return hostUrl;
			}
		}

		logger.error("Unable to find server for " + serverId);

		return "localhost";
	}

	/**
	 * Get the hostname for the service specified by the server type enum.
	 * 
	 * @param type
	 *            . Service type to get the hostname for.
	 * @return hostname of the server the service is running on.
	 */
	private static String getHostNameForService(ServiceTypeEnum type) {
		if (services == null) {
			logger.error("Services list is empty! Has the server completed startup? Try loalhost");
			return "localhost";
		}

		for (DistributedService service : services) {
			if (service.getService().equals(type.name()))
				return getHostName(service.getServerId());
		}

		logger.error("Service " + type
				+ " Not found in Distribtued service cache!!");

		return "localhost";
	}

	/**
	 * Check to see if the service type is running on the localhost or a remote
	 * host.
	 * 
	 * @param type
	 *            . Service type to look up
	 * @return true if the service is running on the localhost.
	 *         <p>
	 *         flase if the service is not running on the localhost.
	 */
	private static boolean isLocalhost(ServiceTypeEnum type) {
		if (services == null) {
			logger.error("Services list is empty! Has the server completed startup? Try loalhost");
			return true;
		}

		Long id = null;
		try {
			id = IntegerProperties.getInstance().getLongProperty(
					LongPropertyNames.ServerId);
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}

		for (DistributedService service : services) {
			if (service.getService().equals(type.name()))
				return service.getServerId().equals(id);
		}

		logger.error("Service " + type
				+ " Not found in Distribtued service cache!!");

		return true;
	}

	/**
	 * Get the DistributedManager for the given ManagerTypeEnum. The distributed
	 * manager is used to configure where a manager is running or state of where
	 * the manager is running.
	 * 
	 * @param type
	 *            . MangerTypeEnum for the manager that is requested.
	 * @return DistrbutedManager for the manager type.
	 */
	public DistributedManager getDistributedManager(ManagerTypeEnum type) {
		if (managers == null) {
			logger.error("Manager list is empty! Has the server completed startup? Can not get "
					+ type);
			return null;
		}

		for (DistributedManager manager : managers) {
			if (manager.getManagerType().equals(type.name()))
				return manager;
		}

		logger.error("Manager " + type
				+ " Not found in Distribtued manager cache!!");

		return null;
	}

	/**
	 * Get the DistributedManager for the given ManagerTypeEnum. The distributed
	 * manager is used to configure where a manager is running or state of where
	 * the manager is running.
	 * 
	 * @param type
	 *            . MangerTypeEnum for the manager that is requested.
	 * @return DistrbutedManager for the manager type.
	 */
	public DistributedService getDistributedService(ServiceTypeEnum type) {

		if (services == null) {
			logger.error("Service list is empty! Has the server completed startup? Can not get "
					+ type);
			return null;
		}

		for (DistributedService service : services) {
			if (service.getService().equals(type.name()))
				return service;
		}

		logger.error("Manager " + type
				+ " Not found in Distribtued manager cache!!");

		return null;
	}

	/**
	 * Get the local service name for the given service type enum. This is used
	 * in the lookup of the service.
	 * 
	 * @param serviceType
	 *            . ServiceTypeEnum of the service requested.
	 * @return String lookup string for the service.
	 */
	private static String getLocalServiceName(ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
		b.append(serviceType.getServiceClass().getSimpleName());
		b.append("!");
		b.append(serviceType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * Get the local service name for the given service type enum. This is used
	 * in the lookup of the service.
	 * 
	 * @param module
	 *            . Name of the module to lookup the service in.
	 * @param serviceType
	 *            . ServiceTypeEnum of the service requested.
	 * @return String lookup string for the service.
	 */
	private static String getLocalServiceName(String module,
			ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();

		b.append("java:global/");
		b.append(module);
		b.append('/');

		b.append(serviceType.getServiceClass().getSimpleName());
		b.append("!");
		b.append(serviceType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * Get the name of the service when the service is running on a remote
	 * server.
	 * 
	 * @param module
	 *            . Name of the module to lookup the service in.
	 * @param serviceType
	 *            . ServiceTypeEnum for the service that is to be looked up.
	 * 
	 * @return service name of the service. This is used to lookup the service.
	 */
	private static String getRemoteServiceName(String module,
			ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
		// b.append(module);
		// b.append('/');

		b.append(serviceType.getServiceClass().getSimpleName());
		b.append("!");
		b.append(serviceType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * This method is used to get a reference to a manager. The manager could be
	 * running on this server or a remote server. The returned instance can be
	 * used as if it was a local reference.
	 * <p>
	 * Ex:
	 * <p>
	 * DiscoveryManager discoveryManager =
	 * DistributionManger.getManager(ManagerTypeEnum.DiscoveryManager)
	 * 
	 * @param type
	 *            . ManagerTypeEnum of the manager that is requested.
	 * @return reference to a manager.
	 * 
	 * @throws IntegerException
	 */
	public static <T extends BaseManagerInterface> T getManager(
			ManagerTypeEnum managerType) throws IntegerException {

		for (DistributedManager manager : managers) {
			if (manager.getManagerType().equals(managerType.name()))
				return getManager(manager.getServerId(), managerType);
		}

		System.out.println("Manager not found for " + managerType + " Types "
				+ managers);
		for (DistributedManager manager : managers) {
			System.out.println("Manager " + manager);
		}
		logger.error("Manager not found for " + managerType);
		return null;
	}

	/**
	 * Is the manager running on the localhost?
	 * 
	 * @param managerType
	 * @return
	 * @throws IntegerException
	 */
	public static boolean isLocalManager(ManagerTypeEnum managerType)
			throws IntegerException {
		if (managers == null) {
			logger.error("Managers list not loaded!!");
			return false;
		}

		for (DistributedManager manager : managers) {
			if (manager.getManagerType().equals(managerType.name()))
				return isLocalHost(manager.getServerId());
		}

		System.out.println("Manager not found for " + managerType + " Types "
				+ managers);
		for (DistributedManager manager : managers) {
			System.out.println("Manager " + manager);
		}
		logger.error("Manager not found for " + managerType);

		return false;
	}

	/**
	 * Get a reference to a manager. This call is used when the caller knows
	 * where the manager is running and there is more than one instance of the
	 * manager running in the system.
	 * <p>
	 * 
	 * Ex:
	 * <p>
	 * Long serverId = CoreServerId;
	 * <p>
	 * DiscoveryManager discoveryManager =
	 * DistributionManger.getManager(serverid, ManagerTypeEnum.DiscoveryManager)
	 * 
	 * 
	 * @param serverId
	 *            . The server id of the server where the manager is running.
	 * @param managerType
	 *            . Manager type of the manager that is desired.
	 * @return A reference to a manager.
	 * 
	 * @throws IntegerException
	 */
	public static <T extends BaseManagerInterface> T getManager(Long serverId,
			ManagerTypeEnum managerType) throws IntegerException {

		String moduleName = IntegerProperties.getInstance().getProperty(
				StringPropertyNames.ModuleName);

		try {
			if (moduleName.length() > 1) {
				if (isLocalHost(serverId))
					return lookupLocalBean(getHostName(serverId),
							getLocalManagerName(moduleName, managerType));
				else
					return lookupRemoteBean(getHostName(serverId),
							getRemoteManagerName(moduleName, managerType));
			} else
				return lookupLocalBean(getHostName(serverId),
						getLocalManagerName(managerType));
		} catch (Throwable e) {
			logger.error("Failed to get manager " + managerType + " Error "
					+ e.toString());
		}

		return null;
	}

	/**
	 * Get the lookup name for this manager when running on the local server.
	 * 
	 * @param moduleName
	 *            . Name of module that is to be looked up.
	 * @param managerType
	 *            . Manager to lookup.
	 * @return lookup string to find this manager.
	 */
	private static String getLocalManagerName(String moduleName,
			ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();

		b.append("java:global/");
		b.append(moduleName);
		b.append('/');
		b.append(managerType.getBeanClass().getSimpleName());
		b.append("!");
		b.append(managerType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * Get the remote lookup string to find the manager in the given module.
	 * 
	 * @param moduleName
	 *            . Name of module that is to be looked up.
	 * @param managerType
	 *            . Manager to lookup.
	 * @return lookup string to find this manager.
	 * @return
	 */
	private static String getRemoteManagerName(String moduleName,
			ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();

		// b.append("java:global/");
		b.append(moduleName);
		b.append('/');
		b.append(managerType.getBeanClass().getSimpleName());
		b.append("!");
		b.append(managerType.getBeanRemoteInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * Get the lookup name for this manager when running on the local server.
	 * 
	 * @param managerType
	 *            . Manager to lookup.
	 * @return lookup string to find this manager.
	 */
	private static String getLocalManagerName(ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
		b.append(managerType.getBeanClass().getSimpleName());
		b.append("!");
		b.append(managerType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	/**
	 * Lookup the manager in the local server.
	 * 
	 * @param hostName
	 *            . Name of the localhost.
	 * @param managerName
	 *            . Lookup string for the manager to be looked up.
	 * @return manager. A reference to a manager
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	private static <T> T lookupLocalBean(String hostName, String managerName)
			throws IntegerException {

		InitialContext ctx = null;
		try {

			final Properties env = new Properties();
			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			env.put("jboss.naming.client.ejb.context", true);

			ctx = new InitialContext(env);

			T manager = null;

			manager = (T) lookupBean(managerName, ctx);
			if (manager == null) {

				throw new IntegerException(null,
						SystemErrorCodes.ManagerNotFound);
			}

			if (logger.isDebugEnabled())
				logger.info("Got localBean " + managerName);

			return manager;

		} catch (Exception e) {
			logger.error("Error getting service " + managerName + e.toString(),
					e);
			throw new IntegerException(e, SystemErrorCodes.ManagerNotFound);
		} catch (Throwable e) {
			logger.error("Error getting service " + managerName + e.toString(),
					e);
			throw new IntegerException(e, SystemErrorCodes.ManagerNotFound);
		}

	}

	/**
	 * Lookup the manager in the remote server.
	 * 
	 * @param hostName
	 *            . Name of the remote host.
	 * @param managerName
	 *            . Lookup string for the manager to be looked up.
	 * @return manager. A reference to a manager
	 * @throws IntegerException
	 */
	@SuppressWarnings("unchecked")
	private static <T> T lookupRemoteBean(String hostName, String managerName)
			throws IntegerException {

		InitialContext ctx = null;
		try {

			final Properties env = new Properties();

			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jboss.naming.remote.client.InitialContextFactory");
			env.put(Context.PROVIDER_URL, "http-remoting://" + hostName);
			//
			env.put(Context.SECURITY_PRINCIPAL, "admin");
			env.put(Context.SECURITY_CREDENTIALS, "public");

			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			//
			env.put("jboss.naming.client.ejb.context", true);
			//
			ctx = new InitialContext(env);

			T manager = null;

			manager = (T) lookupBean(managerName, ctx);
			if (manager == null) {

				throw new IntegerException(null,
						SystemErrorCodes.ManagerNotFound);
			}

			if (logger.isDebugEnabled())
				logger.debug("Got bean " + managerName + " from host "
						+ hostName + " with context " + env.toString());

			return manager;

		} catch (Exception e) {
			logger.error("Error getting service " + managerName + " Error: "
					+ e.toString(), e);
			throw new IntegerException(e, SystemErrorCodes.ManagerNotFound);
		} catch (Throwable e) {
			logger.error("Error getting service " + managerName + " Error: "
					+ e.toString(), e);
			throw new IntegerException(e, SystemErrorCodes.ManagerNotFound);
		}

	}

	/**
	 * This method is used to lookup a bean (manager or service) in the given
	 * context. This will be used for both managers and services the lookup is
	 * the same.
	 * 
	 * @param beanName
	 *            . Name of the bean to lookup.
	 * @param ctx
	 *            . Context to lookup the bean in.
	 * @return bean. The bean will be either a manager or service.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T lookupBean(String managerName, InitialContext ctx) {
		T manager = null;

		try {

			if (logger.isDebugEnabled())
				logger.debug("Looking for manager " + managerName);

			manager = (T) ctx.lookup(managerName);

			if (logger.isDebugEnabled())
				logger.debug("Found manager " + managerName);

		} catch (NameNotFoundException ne) {
			logger.info("Manager " + managerName + " not found!! " + ne);
		} catch (NamingException e) {
			logger.info("Name " + managerName + " Not found! " + e);
		} catch (Throwable t) {
			logger.info("Throwable error Name " + managerName + " Not found! "
					+ t);
		}

		return manager;
	}

	/**
	 * Return the list of managers that are known to the system. This is the
	 * complete list of managers <b>NOT</b> just what is running on the local
	 * server.
	 * 
	 * @return list of the managers in the system.
	 */
	public static DistributedManager[] getManagers() {
		return managers;
	}

	/**
	 * Set the list of managers that are valid for the complete system
	 * <b>NOT</b> just what is valid on the local server.
	 * 
	 * @param managers
	 *            the managers to set
	 */
	public static void setManagers(DistributedManager[] distributeManagers) {
		managers = distributeManagers;
	}

	/**
	 * Return the list of services that are known to the system. This is the
	 * complete list of services <b>NOT</b> just what is running on the local
	 * server.
	 * 
	 * @return list of the managers in the system.
	 */
	public static DistributedService[] getServices() {
		return services;
	}

	/**
	 * Set the list of services that are valid for the complete system
	 * <b>NOT</b> just what is valid on the local server.
	 * 
	 * @param services
	 *            the managers to set
	 */
	public static void setServices(DistributedService[] distributedServers) {
		services = distributedServers;
	}

	/**
	 * Get the list of servers in the system.s
	 * 
	 * @return the servers
	 */
	public static IntegerServer[] getServers() {
		return servers;
	}

	/**
	 * 
	 * Set the list of servers in the system.s
	 * 
	 * @param servers
	 *            the servers to set
	 */
	public static void setServers(IntegerServer[] servers) {
		DistributionManager.servers = servers;
	}

	/**
	 * Return the IntegerServer for the given server ID.
	 * 
	 * @param serverId
	 *            . Server Id to get the IntegerServer object for.
	 * @return IntgerServer with the given ID.
	 */
	public static IntegerServer getIntegerServer(Long serverId) {
		for (IntegerServer server : getServers()) {
			if (server.getServerId().equals(serverId))
				return server;
		}

		return null;
	}
}
