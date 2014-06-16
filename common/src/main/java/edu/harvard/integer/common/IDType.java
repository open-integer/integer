/*
 *  Copyright (c) 2013 Harvard University and the persons
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
package edu.harvard.integer.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * This class encapsulates the type of object. This is part of the ID for an
 * object. This is used to identify the type of object an ID represents. This
 * also has the database table name the object is stored in.
 * 
 * @author David Taylor
 * 
 */

@Embeddable
public class IDType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<? extends BaseEntity> classType = null;

	private transient String tableName = null;

	// Default constructor to make Hibernate happy.
	public IDType() {
		super();
	}
	
	public IDType(Class<? extends BaseEntity> clazz) {
		this.classType = clazz;
		this.tableName = clazz.getSimpleName();
	}

	public IDType(Class<? extends BaseEntity> clazz, String tableName) {
		this.classType = clazz;
		this.tableName = tableName;
	}

//	public String getClassName() {
//		return classType.getName();
//	}

	public Class<? extends BaseEntity> getClassType() {
		return classType;
	}

	public void setClassType(Class<? extends BaseEntity> classType) {
		this.classType = classType;
	}
	
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String name) {
		this.tableName = name;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (classType != null)
			return classType.getName();
		else
			return "";
	}

}
