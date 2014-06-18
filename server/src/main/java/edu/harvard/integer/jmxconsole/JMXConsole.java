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

package edu.harvard.integer.jmxconsole;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;

/**
 * @author David Taylor
 * 
 */
public class JMXConsole {

	private static final Logger logger = LoggerFactory
			.getLogger(JMXConsole.class);

	public static Object getAttribute(String mbeanName, String attribute)
			throws IntegerException {

		String host = "localhost";
		int port = 8080; // management-web port
		String urlString = System.getProperty("jmx.service.url",
				"service:jmx:http-remoting-jmx://" + host + ":" + port);
		JMXServiceURL serviceURL = null;
		try {
			serviceURL = new JMXServiceURL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JMXConnector jmxConnector = null;
		MBeanServerConnection connection = null;
		try {
			jmxConnector = JMXConnectorFactory.connect(serviceURL, null);

			connection = jmxConnector.getMBeanServerConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectName objName = null;
		try {
			objName = new ObjectName(mbeanName);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object obj = null;
		try {
			obj = connection.getAttribute(objName, attribute);
		} catch (AttributeNotFoundException | InstanceNotFoundException
				| MBeanException | ReflectionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (logger.isDebugEnabled())
			logger.debug("Bean: " + mbeanName + " Attribute: " + attribute
					+ " = " + obj);

		try {
			if (jmxConnector != null)
				jmxConnector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;

	}
	
	public static Integer getWebServerPort() throws IntegerException {
		return (Integer) getAttribute(
				"jboss.ws:service=ServerConfig", "WebServicePort");
	}
	
}
