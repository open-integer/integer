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

/**
 * Keys for the String properties in the system. The default value must be
 * specified for each key.
 * 
 * @author David Taylor
 * 
 */
public enum StringPropertyNames {
	MIBDir("MibDir", "../standalone/data/mibs"), BaseMibList(
			"BaseMibList",
			"RFC1065-SMI,RFC1213-MIB,SNMPv2-SMI,SNMPv2-MIB,IANAifType-MIB,IF-MIB,SNMP-FRAMEWORK-MIB,ENTITY-MIB,HOST-RESOURCES-MIB,CISCO-SMI,CISCO-ENTITY-VENDORTYPE-OID-MIB,CISCO-TC,CISCO-PRODUCTS-MIB,INET-ADDRESS-MIB,IP-MIB,CISCO-CEF-TC.my,CISCO-FIREWALL-TC.my,CISCO-IMAGE-TC.my,CISCO-IPSEC-TC.my,CISCO-ST-TC.my,CISCO-VIDEO-TC.my,DIFFSERV-DSCP-TC,HCNUM-TC"), ModuleName(
			"ModuleName", ""); // Use empty string for test cases. When
								// installed the real name will be used.

	private String fieldName = null;
	private String defaultValue = null;

	private StringPropertyNames(String fieldName, String defaultValue) {
		this.fieldName = fieldName;
		this.defaultValue = defaultValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}
