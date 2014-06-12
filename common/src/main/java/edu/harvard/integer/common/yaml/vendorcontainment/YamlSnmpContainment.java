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

package edu.harvard.integer.common.yaml.vendorcontainment;

import java.util.List;

/**
 * @author David Taylor
 * 
 */
public class YamlSnmpContainment {

	private String containmentType = null;

	private List<YamlSnmpLevelOID> snmpLevels = null;

	private YamlServiceElementType serviceElementType = null;

	/**
	 * @return the containmentType
	 */
	public String getContainmentType() {
		return containmentType;
	}

	/**
	 * @param containmentType
	 *            the containmentType to set
	 */
	public void setContainmentType(String containmentType) {
		this.containmentType = containmentType;
	}

	/**
	 * @return the snmpLevels
	 */
	public List<YamlSnmpLevelOID> getSnmpLevels() {
		return snmpLevels;
	}

	/**
	 * @param snmpLevels
	 *            the snmpLevels to set
	 */
	public void setSnmpLevels(List<YamlSnmpLevelOID> snmpLevels) {
		this.snmpLevels = snmpLevels;
	}

	/**
	 * @return the serviceElementType
	 */
	public YamlServiceElementType getServiceElementType() {
		return serviceElementType;
	}

	/**
	 * @param serviceElementType
	 *            the serviceElementType to set
	 */
	public void setServiceElementType(YamlServiceElementType serviceElementType) {
		this.serviceElementType = serviceElementType;
	}

}
