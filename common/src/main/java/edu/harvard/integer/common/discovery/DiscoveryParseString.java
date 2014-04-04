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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.exception.DiscoveryErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;

/**
 * @author David Taylor
 * 
 */
@Entity
public class DiscoveryParseString extends BaseEntity {

	private static transient Logger logger = LoggerFactory
			.getLogger(DiscoveryParseString.class);

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany
	@OrderColumn(name = "idx")
	private List<DiscoveryParseElement> parseStrings = null;

	/**
	 * @return the parseStrings
	 */
	public List<DiscoveryParseElement> getParseStrings() {
		return parseStrings;
	}

	/**
	 * @param parseStrings
	 *            the parseStrings to set
	 */
	public void setParseStrings(List<DiscoveryParseElement> parseStrings) {
		this.parseStrings = parseStrings;
	}

	/**
	 * Parse the element out of the given string. The ordered list of elements
	 * in getParseString() are used to parse the string. ex. For the sysDescr:
	 * 				Cisco IOS Software, s72033_rp Software (s72033_rp-ADVIPSERVICESK9_WAN-M),
	 * 				Version 12.2(33)SXI4a, RELEASE SOFTWARE (fc2)Technical Support:
	 * 				http://www.cisco.com/techsupportCopyright (c) 1986-2010 by Cisco Systems,
	 * 				Inc.Compiled Fri 16-Jul-10 19:51 by p
	 * 
	 *  and the DiscoveryParseElements
	 *  		DiscoveryParseElementTypeEnum.Firmware, "Software,"
	 *  		DiscoveryParseElementTypeEnum.Software, "Version"
	 *  
	 *  Then
	 *  	parseElement(DiscoveryParseTypeEnum.Firmware, sysDescr) ==> "s72033_rp"
	 *  	parseElement(DiscoveryParseTypeEnum.Software, sysDescr) ==> "12.2(33)SIX4a" 
	 * 
	 * @param element
	 * @param sysDescr
	 * @return
	 * @throws IntegerException
	 */
	public String parseElement(DiscoveryParseElementTypeEnum element,
			String sysDescr) throws IntegerException {
		if (getParseStrings() == null || getParseStrings().size() == 0)
			throw new IntegerException(null, DiscoveryErrorCodes.NoParseStrings);

		if (sysDescr == null)
			throw new IntegerException(null,
					DiscoveryErrorCodes.NoStringToParse);

		String[] parts = sysDescr.split(" ");

		if (parts == null || parts.length == 0)
			throw new IntegerException(null,
					DiscoveryErrorCodes.NoStringToParseable);

		if (logger.isDebugEnabled())
			logger.debug("Have " + parts.length + " Parts to SysDescr");

		int index = 0;
		for (int elementIndex = 0; elementIndex < getParseStrings().size(); elementIndex++) {
			DiscoveryParseElement parseElement = getParseStrings().get(
					elementIndex);

			boolean foundPart = false;
			while (index < parts.length) {
				if (parseElement.getParseElement().equals(parts[index])) {

					if (logger.isDebugEnabled())
						logger.debug("Found part for " + parseElement.getName()
								+ " = " + parts[index + 1]);
					foundPart = true;

					if (parseElement.getParseElementType().equals(element))
						return parts[index + 1];

					break;
				} else if (logger.isDebugEnabled())
					logger.debug("Skip " + parts[index]);

				index++;
			}

			if (!foundPart) {
				logger.error("Part " + parseElement.getName() + " NOT FOUND!!");
				throw new IntegerException(null,
						DiscoveryErrorCodes.ParseElementNoFound);
			}
		}

		throw new IntegerException(null,
				DiscoveryErrorCodes.ParseElementNoFound);
	}
}
