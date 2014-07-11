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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.distribution.DistributedManager;
import edu.harvard.integer.common.distribution.DistributedService;
import edu.harvard.integer.common.distribution.IntegerServer;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.properties.IntegerProperties;
import edu.harvard.integer.common.properties.LongPropertyNames;
import edu.harvard.integer.server.IntegerApplication;
import edu.harvard.integer.service.BaseService;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedManagerDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.DistributedServiceDAO;
import edu.harvard.integer.service.persistance.dao.distribtued.IntegerServerDAO;

/**
 * The distribution service is responsible for where all the services and managers
 * are running as well as the state of each of the services, managers and servers 
 * in the integer system. 
 * <p>
 * The distribution service will run on each server so that all services and managers
 * on each server can find a local or remote manager or service.
 * 
 * @author David Taylor
 * 
 */
@Singleton
@Startup
@Path("/DistributionService")
public class DistributionService extends BaseService implements DistributionServiceInterface {

	@Inject
	private Logger logger;

	@Inject
	private PersistenceManagerInterface persistenceManager;

	/**
	 * 
	 */
	public DistributionService() {

	}

	/**
	 * Called after the DistributionService has been created to do the 
	 * Initialization of the Distribution service.
	 * <p>
	 * This is called by the container (wildfly)
	 */
	@PostConstruct
	public void init() {
		
		logger.info("DistributionService starting");

		// Register the application for RESTfull interface
		IntegerApplication.register(this);

		try {
			loadServers();
		} catch (IntegerException e) {
			logger.error("Error loading servers!! " + e.toString());
			e.printStackTrace();
		}

		try {
			loadManagers();
		} catch (IntegerException e) {
			logger.error("Error loading managers!! " + e.toString());

			e.printStackTrace();
		}

		try {
			loadServices();
		} catch (IntegerException e) {
			logger.error("Error loading services!! " + e.toString());
			e.printStackTrace();
		}

		try {
			showAllManagers();
		} catch (IntegerException e) {
			logger.error("Error checking all managers! " + e.toString());
			e.printStackTrace();
		} catch (Throwable e) { 
			logger.error("Error checking all managers! " + e.toString());
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.distribution.DistributionServiceInterface#getManagers()
	 */
	@Override
	public DistributedManager[] getManagers() throws IntegerException  {

		DistributedManagerDAO distributedManagerDAO = persistenceManager
				.getDistributedManagerDAO();
		DistributedManager[] managers = distributedManagerDAO.findAll();
	
		logger.info("Found " + managers.length + " managers " + managers);
		
		return managers;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.distribution.DistributionServiceInterface#getServices()
	 */
	@Override
	public DistributedService[] getServices() throws IntegerException {

		DistributedServiceDAO dao = persistenceManager
				.getDistributedServiceDAO();
		DistributedService[] services = dao.findAll();
		
		logger.info("Found " + services.length + " managers " + services);
		
		return services;
	}
	
	/**
	 * @throws IntegerException 
	 * 
	 */
	@Path(value="/AllManagers")
	@GET
	@Produces({ MediaType.TEXT_HTML })
	private String showAllManagers() throws IntegerException {
		StringBuffer b = new StringBuffer();
		
		b.append("My ServerID: ").append(getServerID());
		
		IntegerServer[] servers = DistributionManager.getServers();
		for (IntegerServer integerServer : servers) {
			String message = "Server " + integerServer.getName() + " ID: " + integerServer.getServerId() 
					+ " " + integerServer.getServerAddress().getAddress()
					+ " My ID: " + getServerID()
					+ " started " + integerServer.getLastStarted();
			
			b.append(message).append('\n');
			logger.info(message);
			
			
			for (DistributedManager distributedManager : getManagers()) {
				String managerMessage = "Manager " + distributedManager.getName() + " type " + distributedManager.getManagerType();
				logger.info(managerMessage);
				b.append("Manager " + distributedManager.getName() + " type " + distributedManager.getManagerType()).append('\n');
			}
		}
		
		return b.toString();
	}

	/**
	 * @throws IntegerException 
	 * 
	 */
	@Path(value="/AllManagers")
	@GET
	@Produces({ MediaType.TEXT_HTML })
	private String showAllServicess() throws IntegerException {
		StringBuffer b = new StringBuffer();
		
		b.append("My ServerID: ").append(getServerID());
		
		IntegerServer[] servers = DistributionManager.getServers();
		for (IntegerServer integerServer : servers) {
			b.append("\nServer " + integerServer.getName() + " ID: " + integerServer.getServerId() 
					+ " " + integerServer.getServerAddress()
					+ " started " + integerServer.getLastStarted());
			

			DistributedService[] services = DistributionManager.getServices();
			for (DistributedService service : services) {
				b.append("\nManager " + service.getName() + " type " + service.getService());
			}
		}
		
		return b.toString();
	}


	/**
	 * @return
	 */
	private Integer getServerPort() {
		// TODO Fix ME!!
		logger.warn("Need to get sever port from Wildfly");
		
	//	try {
			String property = System.getProperty("jboss.http.port");
			if (property != null) {
				logger.info("got http port " + property + " from System properties");
				
				return Integer.valueOf(property);
			}
			
			// TODO: Fix call to get web server port
			// return JMXConsole.getWebServerPort();
		
//		} catch (IntegerException e) {
//			logger.error("Error gettting webserver port!! " + e.toString());
//			e.printStackTrace();
//		}
		
		return Integer.valueOf(8080);
	}

	public Long getServerID() {
		try {
			Long id = IntegerProperties.getInstance().getLongProperty(
					LongPropertyNames.ServerId);
			return id;
		} catch (IntegerException e) {
			logger.error("Error loading IntegerProperties " + e.toString());

			e.printStackTrace();
		}

		return Long.valueOf(1);
	}

	private void loadManagers() throws IntegerException {
		DistributedManagerDAO distributedManagerDAO = persistenceManager
				.getDistributedManagerDAO();
		DistributedManager[] managers = distributedManagerDAO.findAll();

		if (managers.length == 0) {
			logger.warn("Create managers on 'localhost'");

			for (ManagerTypeEnum managerType : ManagerTypeEnum.values()) {
				DistributedManager manager = new DistributedManager();
				manager.setName(managerType.name());
				manager.setManagerType(managerType.name());
				manager.setServerId(getServerID());
				distributedManagerDAO.update(manager);
				
				logger.info("Added Manager " + manager.getManagerType() +  " ID: " + manager.getID().toDebugString());
			}

			managers = distributedManagerDAO.findAll();
		}

		logger.info("Have " + managers.length + " DistributeManagers");
		
		DistributionManager.setManagers(managers);
	}

	private void loadServices() throws IntegerException {
		DistributedServiceDAO dao = persistenceManager
				.getDistributedServiceDAO();
		DistributedService[] services = dao.findAll();

		if (services.length == 0) {
			logger.warn("Create service on 'localhost'");

			for (ServiceTypeEnum serviceType : ServiceTypeEnum.values()) {
				DistributedService service = new DistributedService();
				service.setName(serviceType.name());
				service.setService(serviceType.name());
				service.setServerId(getServerID());
				dao.update(service);
			}

			services = dao.findAll();
		}

		logger.info("Have " + services.length + " DistributeServices");
		DistributionManager.setServices(services);
	}

	private void loadServers() throws IntegerException {
		IntegerServerDAO dao = persistenceManager.getIntegerServerDAO();
		IntegerServer[] servers = dao.findAll();

		if (servers.length == 0) {
			addServer(dao);
			servers = dao.findAll();
		} else {
			boolean foundIt = false;
			for (IntegerServer integerServer : servers) {
				if (integerServer.getServerId().equals(getServerID())) {
					foundIt = true;
					break;
				}
			}
			
			if ( ! foundIt ) {
				addServer(dao);
				servers = dao.findAll();
			}
		}

		logger.info("Have " + servers.length + " IntegerServers");
		
		DistributionManager.setServers(servers);
	}
	
	private void addServer(IntegerServerDAO dao) throws IntegerException {

		try {
			Address address = new Address(Inet4Address.getLocalHost()
					.getHostAddress(), "255.255.255.0");

			IntegerServer integerServer = dao.findByAddress(address);
			if (integerServer == null) {
				integerServer = new IntegerServer();
				integerServer.setServerAddress(address);
				integerServer.setServerId(getServerID());
				integerServer.setPort(getServerPort());
				dao.update(integerServer);
				
				logger.info("Created IntegerServer " + integerServer.getID());
			}
		} catch (UnknownHostException e) {
			logger.error("Unable to resolve hostname of this localhost!!! Can not create IntegerServer entry!");

			e.printStackTrace();
		}
		
	}
}
