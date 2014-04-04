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

package edu.harvard.integer.common.discovery;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;

/**
 * @author David Taylor
 *
 */
public class DiscoveryParseStringTest {

	private static Logger logger = LoggerFactory.getLogger(DiscoveryParseStringTest.class);

	@Before
	public void setupLogger() {
		BasicConfigurator.configure();
		LogManager.getRootLogger().setLevel(Level.INFO);
	}
	
	@Test
	public void parseSysDescr() {
		StringBuffer b = new StringBuffer();
		b.append("walk -v2c -c integer 10.240.127.121 system");
		b.append("SNMPv2-MIB::sysDescr.0 = STRING: Cisco IOS Software, s72033_rp Software (s72033_rp-ADVIPSERVICESK9_WAN-M), Version 12.2(33)SXI4a, RELEASE SOFTWARE (fc2)");
		b.append("Technical Support: http://www.cisco.com/techsupport");
		b.append("Copyright (c) 1986-2010 by Cisco Systems, Inc.");
		b.append("Compiled Fri 16-Jul-10 19:51 by p");
		String sysDescr = b.toString();
		
		DiscoveryParseString parseString = new DiscoveryParseString();
		parseString.setName("Cisco SysDescr parse string");
		
		List<DiscoveryParseElement> elements = new ArrayList<DiscoveryParseElement>();
		
		DiscoveryParseElement element = new DiscoveryParseElement();
		element.setParseElementType(DiscoveryParseElementTypeEnum.FirmwareVersion);
		element.setParseElement("Software,");
		element.setName("Firmware");
		elements.add(element);
		
		element = new DiscoveryParseElement();
		element.setParseElementType(DiscoveryParseElementTypeEnum.SoftwareVersion);
		element.setParseElement("Version");
		element.setName("Software");
		elements.add(element);
		
		parseString.setParseStrings(elements);
		
		logger.info("Parse SysDescr: " + sysDescr);
		
		try {
			String firmwareVersion = parseString.parseElement(DiscoveryParseElementTypeEnum.FirmwareVersion, sysDescr);
			logger.info("Found Firmware version " + firmwareVersion);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			String softwareVersion = parseString.parseElement(DiscoveryParseElementTypeEnum.SoftwareVersion, sysDescr);
			logger.info("Found Software version " + softwareVersion);
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
}
