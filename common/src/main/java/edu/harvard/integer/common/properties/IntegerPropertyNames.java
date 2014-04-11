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
 * @author David Taylor
 * 
 *         This holds the keys for the Integer properties used. The values must
 *         have a default, min and max value. The units are also specified. 
 */
public enum IntegerPropertyNames {
	ServerId("ServerID", 1, 1, 1000, UnitsEnum.NA),
	SystemPropertyCheckInterval("SystemPropertyCheckInterval", 30, 1, 10000, UnitsEnum.Minute);

	private String fieldName = null;
	private Integer defaultValue = null;
	private Integer minValue = null;
	private Integer maxValue = null;
	private UnitsEnum units = null;

	/**
	 * @param fieldName
	 * @param defaultValue
	 * @param minValue
	 * @param maxValue
	 * @param units
	 */
	private IntegerPropertyNames(String fieldName, Integer defaultValue,
			Integer minValue, Integer maxValue, UnitsEnum units) {
		this.fieldName = fieldName;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.units = units;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the defaultValue
	 */
	public Integer getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the minValue
	 */
	public Integer getMinValue() {
		return minValue;
	}

	/**
	 * @return the maxValue
	 */
	public Integer getMaxValue() {
		return maxValue;
	}

	/**
	 * @return the units
	 */
	public UnitsEnum getUnits() {
		return units;
	}

}
