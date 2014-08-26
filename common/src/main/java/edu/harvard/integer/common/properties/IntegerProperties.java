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

package edu.harvard.integer.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;

/**
 * Utility to load Integer properties. The properties are specified in the
 * XXXXPropertyNames enum. Ex IntegerPropertyNames.
 * 
 * @author David Taylor
 * 
 * 
 */
public class IntegerProperties {

	private static final String PROPERTIES_FILENAME = "integer.properties";
	private Properties settings = null;
	private static IntegerProperties me = null;
	private static long lastLoaded = 0;
	private static long checkInterval = 0;

	private static Logger logger = LoggerFactory
			.getLogger(IntegerProperties.class);

	private IntegerProperties() {

	}

	/**
	 * Get the instance of the IntegerProperties. The properties will be re-read
	 * from disk if the last load was more than the CheckInterval seconds ago.
	 * 
	 * @return
	 * @throws IntegerException
	 */
	public static IntegerProperties getInstance() throws IntegerException {

		if (me != null
				&& (System.currentTimeMillis() - lastLoaded) > checkInterval) {
			me.loadSettings();
		} else {
			if (me != null)
				return me;

			synchronized (IntegerProperties.class) {
				if (me != null)
					return me;

				me = new IntegerProperties();
				me.loadSettings();
			}
		}

		return me;

	}

	private void loadSettings() throws IntegerException {
		settings = new Properties();
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(PROPERTIES_FILENAME);

			if (resourceAsStream == null) {
				logger.error("Unable to load properties from "
						+ PROPERTIES_FILENAME);
				return;

			}
			settings.load(resourceAsStream);

			logger.info("Loaded properties " + settings.toString());


			lastLoaded = System.currentTimeMillis();

			checkInterval = getIntProperty(IntegerPropertyNames.SystemPropertyCheckInterval);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (resourceAsStream != null)
				try {
					resourceAsStream.close();
				} catch (IOException e) {
					logger.error("Error closing property file " + PROPERTIES_FILENAME + " Error " + e.toString());
					e.printStackTrace();
				}
		}
	}

	/**
	 * Load the String property specified. If the property is not in the
	 * property file then the default value will be returned.
	 * 
	 * @param key
	 *            StringPropertyName to get the value for.
	 * @return value of the property.
	 */
	public String getProperty(StringPropertyNames key) {
		if (settings == null) {
			return key.getDefaultValue();
		} else {
			String value = settings.getProperty(key.getFieldName());
			if (value != null)
				return value;
			else
				return key.getDefaultValue();
		}
	}

	public <T extends EnumPropertyNameInterface> T getEnumProperty (EnumPropertyNameInterface<T> propName) {
		
		if (settings == null) {
			return propName.getDefaultValue();
		} else {
			String value = settings.getProperty(propName.getPropertyName());
			if (value != null) {
				for (int i = 0; i < propName.getClass().getEnumConstants().length; i++) {
					if ( propName.getClass().getEnumConstants()[i].getName().equals(value)) {
						return (T) propName.getClass().getEnumConstants()[i];
					}
				}
				
			}
			
			return propName.getDefaultValue();
		}
	}
	
	/**
	 * Load the Integer type property specified. If the property is not in the
	 * property file then the default value will be returned.
	 * 
	 * @param key
	 *            IntegerPropertyName to get the value for.
	 * @return value of the property.
	 */
	public Integer getIntProperty(IntegerPropertyNames key) {
		String strValue = null;

		if (settings == null) {
			return key.getDefaultValue();
		} else {
			strValue = settings.getProperty(key.getFieldName());
		}

		Integer value = null;

		try {

			if (strValue == null)
				value = key.getDefaultValue();
			else {
				strValue = strValue.trim();
				value = Integer.parseInt(strValue);
			}

			if (key.getMinValue() != null && value < key.getMinValue())
				value = key.getMinValue().intValue();
			if (key.getMaxValue() != null && value > key.getMaxValue())
				value = key.getMaxValue().intValue();

		} catch (NumberFormatException e) {
			logger.error("Error getting property for " + key + " Value "
					+ strValue + " is not an integer!!");

			value = key.getDefaultValue().intValue();
		}

		return value;

	}

	/**
	 * Load the Long type property specified. If the property is not in the
	 * property file then the default value will be returned.
	 * 
	 * @param key
	 *            IntegerPropertyName to get the value for.
	 * @return value of the property.
	 */
	public Long getLongProperty(LongPropertyNames key) {
		String strValue = null;

		if (settings == null) {
			return key.getDefaultValue();
		} else {
			strValue = settings.getProperty(key.getFieldName());
		}

		Long value = null;

		try {

			if (strValue == null)
				value = key.getDefaultValue();
			else {
				strValue = strValue.trim();
				value = Long.parseLong(strValue);
			}

			if (key.getMinValue() != null && value < key.getMinValue())
				value = key.getMinValue();
			if (key.getMaxValue() != null && value > key.getMaxValue())
				value = key.getMaxValue();

		} catch (NumberFormatException e) {
			logger.error("Error getting property for " + key + " Value "
					+ strValue + " is not an integer!!");

			value = key.getDefaultValue();
		}

		return value;

	}

	/**
	 * Get a list of all the keys that are in the property file the last time
	 * the property file was read. The data will be at most
	 * IntegerPropertyNames.SystemPropertyCheckInterval seconds old.
	 * 
	 * @return Set of keys to the properties.
	 */
	public Set<Object> getKeys() {
		if (settings == null) {
			return null;
		} else {
			return settings.keySet();
		}
	}
}
