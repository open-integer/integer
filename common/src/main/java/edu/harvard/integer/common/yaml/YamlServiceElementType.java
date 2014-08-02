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

package edu.harvard.integer.common.yaml;

import java.util.List;

/**
 * @author David Taylor
 * 
 */
public class YamlServiceElementType {

	private String vendor = null;
	private String name = null;
	private String description = null;
	
	/**
	 * Extend Servce Element Type.
	 */
	private String extendServiceElementType;
	
	private YamlAccessMethod accessMethod = null;

	private List<YamlManagementObject> managementObjects = null;
 	private List<YamlServiceElementTypeTranslate>  serviceElementTypeTranslates = null;
 	
 	private List<YamlServiceElementAssociationType> associations;
	

	public List<YamlServiceElementAssociationType> getAssociations() {
		return associations;
	}

	public void setAssociations(List<YamlServiceElementAssociationType> associations) {
		this.associations = associations;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public List<YamlServiceElementTypeTranslate> getServiceElementTypeTranslates() {
		return serviceElementTypeTranslates;
	}

	public void setServiceElementTypeTranslates(
			List<YamlServiceElementTypeTranslate> serviceElementTypeTranslates) {
		this.serviceElementTypeTranslates = serviceElementTypeTranslates;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the accessMethod
	 */
	public YamlAccessMethod getAccessMethod() {
		return accessMethod;
	}

	/**
	 * @param accessMethod
	 *            the accessMethod to set
	 */
	public void setAccessMethod(YamlAccessMethod accessMethod) {
		this.accessMethod = accessMethod;
	}

	/**
	 * @return the managementObjects
	 */
	public List<YamlManagementObject> getManagementObjects() {
		return managementObjects;
	}

	/**
	 * @param managementObjects
	 *            the managementObjects to set
	 */
	public void setManagementObjects(
			List<YamlManagementObject> managementObjects) {
		this.managementObjects = managementObjects;
	}
	

	public String getExtendServiceElementType() {
		return extendServiceElementType;
	}

	public void setExtendServiceElementType(String extendServiceElementType) {
		this.extendServiceElementType = extendServiceElementType;
	}

}
