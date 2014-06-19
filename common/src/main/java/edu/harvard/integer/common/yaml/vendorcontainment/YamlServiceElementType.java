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
public class YamlServiceElementType {
	
	private String name = null;
	private String fieldReplaceableUnit = null;
	private String description = null;
	private String defaultNameCabability = null;
	private String category = null;
	private List<String> uniqueIdentifierCapabilities = null;
	private List<String> attributes = null;
	
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
	 * @return the fieldReplaceableUnit
	 */
	public String getFieldReplaceableUnit() {
		return fieldReplaceableUnit;
	}

	/**
	 * @param fieldReplaceableUnit
	 *            the fieldReplaceableUnit to set
	 */
	public void setFieldReplaceableUnit(String fieldReplaceableUnit) {
		this.fieldReplaceableUnit = fieldReplaceableUnit;
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
	 * @return the defaultNameCabability
	 */
	public String getDefaultNameCabability() {
		return defaultNameCabability;
	}

	/**
	 * @param defaultNameCabability
	 *            the defaultNameCabability to set
	 */
	public void setDefaultNameCabability(String defaultNameCabability) {
		this.defaultNameCabability = defaultNameCabability;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the uniqueIdentifierCapabilities
	 */
	public List<String> getUniqueIdentifierCapabilities() {
		return uniqueIdentifierCapabilities;
	}

	/**
	 * @param uniqueIdentifierCapabilities
	 *            the uniqueIdentifierCapabilities to set
	 */
	public void setUniqueIdentifierCapabilities(
			List<String> uniqueIdentifierCapabilities) {
		this.uniqueIdentifierCapabilities = uniqueIdentifierCapabilities;
	}

	/**
	 * @return the attributes
	 */
	public List<String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

}
