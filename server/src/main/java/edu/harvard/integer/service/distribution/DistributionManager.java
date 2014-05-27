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

	public static <T extends BaseServiceInterface> T getService(
			ServiceTypeEnum type) throws IntegerException {

		String moduleName = IntegerProperties.getInstance().getProperty(
				StringPropertyNames.ModuleName);
		try {

			// if (logger.isDebugEnabled())
			logger.info("Lookup " + type + " module " + moduleName);

			String hostName = getHostNameForService(type);
			
			if (moduleName.length() > 1) {
				if (isLocalhost(type))
					return lookupLocalBean(hostName, getLocalServiceName(moduleName, type));
				else
					return lookupRemoteBean(getHostNameForService(type),
							getRemoteServiceName(moduleName, type));
				
			} else
				return lookupLocalBean("localhost",
						getLocalServiceName(type));
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
	
	public static String getHostName(Long serverId) {
		for (IntegerServer server : servers) {
			if (server.getServerId().equals(serverId)) {
				String hostUrl = server.getServerAddress().getAddress() + ":"
						+ server.getPort();
				logger.info("Using " + hostUrl + " for serverId " + serverId);
				return hostUrl;
			}
		}

		logger.error("Unable to find server for " + serverId);

		return "localhost";
	}

	/**
	 * @param type
	 * @return
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
	
	private static String getHostNameForManager(ManagerTypeEnum type) {
		if (managers == null) {
			logger.error("Manager list is empty! Has the server completed startup? Try loalhost");
			return "localhost";
		}

		for (DistributedManager manager : managers) {
			if (manager.getManagerType().equals(type.name()))
				return getHostName(manager.getServerId());
		}

		logger.error("Service " + type
				+ " Not found in Distribtued service cache!!");

		return "localhost";
	}

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

	private static String getLocalServiceName(ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
		b.append(serviceType.getServiceClass().getSimpleName());
		b.append("!");
		b.append(serviceType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

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

	private static String getRemoteServiceName(String module,
			ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
//		b.append(module);
//		b.append('/');

		b.append(serviceType.getServiceClass().getSimpleName());
		b.append("!");
		b.append(serviceType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}

	public static <T extends BaseManagerInterface> T getManager(
			ManagerTypeEnum managerType) throws IntegerException {

		for (DistributedManager manager : managers) {
			if (manager.getManagerType().equals(managerType.name()))
				return getManager(manager.getServerId(), managerType);
		}
	
		System.out.println("Manager not found for " + managerType + " Types " + managers);
		for (DistributedManager manager : managers) {
			System.out.println("Manager " + manager);
		}
		logger.error("Manager not found for " + managerType);
		return null;
	}

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
	
	private static String getRemoteManagerName(String moduleName,
			ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();

		//b.append("java:global/");
		b.append(moduleName);
		b.append('/');
		b.append(managerType.getBeanClass().getSimpleName());
		b.append("!");
		b.append(managerType.getBeanRemoteInterfaceClass().getName());

		return b.toString();
	}
	
	
	private static String getLocalManagerName(ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();

		b.append("java:module/");
		b.append(managerType.getBeanClass().getSimpleName());
		b.append("!");
		b.append(managerType.getBeanLocalInterfaceClass().getName());

		return b.toString();
	}


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

			// if (logger.isDebugEnabled())
			logger.info("Got bean " + managerName + " from host " + hostName
					+ " with context " + env.toString());

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
	 * @return the managers
	 */
	public static DistributedManager[] getManagers() {
		return managers;
	}

	/**
	 * @param managers
	 *            the managers to set
	 */
	public static void setManagers(DistributedManager[] distributeManagers) {
		managers = distributeManagers;
	}

	/**
	 * @return the services
	 */
	public static DistributedService[] getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public static void setServices(DistributedService[] distributedServers) {
		services = distributedServers;
	}

	/**
	 * @return the servers
	 */
	public static IntegerServer[] getServers() {
		return servers;
	}

	/**
	 * @param servers
	 *            the servers to set
	 */
	public static void setServers(IntegerServer[] servers) {
		DistributionManager.servers = servers;
	}
}
