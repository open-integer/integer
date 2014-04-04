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

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.SystemErrorCodes;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.BaseServiceInterface;


/**
 * @author David Taylor
 *
 */
public class DistributionManager {

	private static final Logger logger = LoggerFactory.getLogger(DistributionManager.class);
	 
	 
	public static <T extends BaseServiceInterface> T getService(ServiceTypeEnum type) throws IntegerException {
	
		return lookupLocalBean(getLocalServiceName(type));
	}
	

	private static String getLocalServiceName(ServiceTypeEnum serviceType) {
		StringBuffer b = new StringBuffer();
		
		b.append("java:module/");
		b.append(serviceType.getServiceClass().getSimpleName());
		
		return b.toString();
	}

	
	public static <T extends BaseManagerInterface> T getManager(ManagerTypeEnum managerType) throws IntegerException {
			
		return lookupLocalBean(getLocalManagerName(managerType));	
	}

	
	private static String getLocalManagerName(ManagerTypeEnum managerType) {
		StringBuffer b = new StringBuffer();
		
		b.append("java:module/");
		b.append(managerType.getBeanClass().getSimpleName());
		
		return b.toString();
	}


	@SuppressWarnings("unchecked")
	private static <T> T lookupLocalBean(String managerName) throws IntegerException {
		
		InitialContext ctx = null;
		try {
			
			final Properties env = new Properties();
			env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			ctx = new InitialContext(env);
			
			T manager = null;

			manager = (T) lookupBean( managerName, ctx);
			if (manager == null) {

				throw new IntegerException(null, SystemErrorCodes.ManagerNotFound);
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
	private static <T> T lookupBean(String managerName, InitialContext ctx) {
		T manager = null;

		try {

			if (logger.isDebugEnabled())
				logger.debug("Looking for manager " + managerName);

			manager = (T) ctx.lookup(managerName);
	
			if (logger.isDebugEnabled())
				logger.debug("Found manager " + managerName);
	
		} catch (NameNotFoundException ne) {
			logger.info("Manager "+  managerName + " not found!! " + ne);
		} catch (NamingException e) {
			logger.info("Name " + managerName + " Not found! " + e);
		} catch (Throwable t) {
			logger.info("Throwable error Name " + managerName + " Not found! " + t);
		}

		return manager;
	}

}
