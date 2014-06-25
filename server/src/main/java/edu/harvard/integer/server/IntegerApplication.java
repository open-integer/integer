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

package edu.harvard.integer.server;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;

/**
 * This class is used to register the RESTfull call's to the server.
 * This also defines the base context for all calls to the server
 *  
 * @author David Taylor
 * 
 */
@ApplicationPath("/Integer")
public class IntegerApplication extends Application {

	@Inject
	private Logger logger;

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	private static IntegerApplication me = null;

	public IntegerApplication() {
	}

	public static IntegerApplication getInstance() {
		if (me != null)
			return me;

		synchronized (IntegerApplication.class) {
			if (me != null)
				return me;

			me = new IntegerApplication();
			return me;
		}
	}

	private Logger getLogger() {
		return logger;
	}

	/**
	 * Get the index page for all calls to the server.
	 * 
	 * @return HTML string that can display a index page
	 */
	public String getIndexPage() {
		StringBuffer b = new StringBuffer();
		b.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
				.append('\n');
		b.append("<html>").append('\n');
		b.append("<head>").append('\n');
		b.append(
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append('\n');
		b.append("<title>NMS Services</title>").append('\n');
		b.append("</head>").append('\n');
		b.append("<body>").append('\n');

		b.append("  <ul>").append('\n');

		for (Class<?> clazz : classes) {

			Path classPath = clazz.getAnnotation(Path.class);
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				Path fieldPath = method.getAnnotation(Path.class);
				if (fieldPath != null) {

					if (fieldPath.value().contains("index"))
						continue;

					b.append("<li><a href=\"..");
					b.append(classPath.value());
					b.append(fieldPath.value()).append("\">");
					b.append(fieldPath.value().substring(1));
					b.append("</a></li>");
					logger.info("Add Rest path " + clazz.getSimpleName() + "/"
							+ fieldPath.toString());
				}
			}
		}

		b.append("  </ul>").append('\n');

		b.append("</body>").append('\n');
		b.append("</html>").append('\n');
		return b.toString();
	}

	/**
	 * Register a manager with the Application. The manager will then
	 * be registered in the index and be available for calls to its
	 * restfull interfaces.
	 * 
	 * @param object
	 */
	public static void register(Object object) {
		Path annotation = object.getClass().getAnnotation(Path.class);
		if (annotation != null) {
			boolean foundOne = false;
			Method[] methods = object.getClass().getMethods();
			for (Method method : methods) {
				Path fieldPath = method.getAnnotation(Path.class);
				if (fieldPath != null) {
					foundOne = true;
					if (getInstance().getLogger() != null)
						getInstance().getLogger().info(
								"Add Rest path " + annotation.toString() + "/"
										+ fieldPath.toString());
					else
						System.out.println("NULL LOGGER!!! Add Rest path "
								+ annotation.toString() + "/"
								+ fieldPath.toString());
				}
			}

			if (foundOne == true) {
				getInstance().singletons.add(object);
				getInstance().classes.add(object.getClass());
			}
		}

	}

	@Override
	public Set<Class<?>> getClasses() {
		return getInstance().classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return getInstance().singletons;
	}

}
