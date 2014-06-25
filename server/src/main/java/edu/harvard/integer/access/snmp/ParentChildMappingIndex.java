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
package edu.harvard.integer.access.snmp;

import edu.harvard.integer.common.discovery.RelationMappingTypeEnum;

/**
 * The Class ParentChildMappingIndex contains the instance OID information
 * for parent and child containment relationship.
 *
 * @author dchan
 */
public class ParentChildMappingIndex {

	/** The parent index. */
	private final String parentIndex;
	
	/** The child index. */
	private String childIndex;
	
	/** The mapping type. */
	private final RelationMappingTypeEnum mappingType;
	

	/**
	 * Instantiates a new parent child mapping index.
	 *
	 * @param parentIndex the parent index
	 * @param type the type
	 */
	public ParentChildMappingIndex( String parentIndex, RelationMappingTypeEnum type ) {
		
		this.parentIndex = parentIndex;
		this.mappingType = type;
	}
	
	/**
	 * Gets the child index.
	 *
	 * @return the child index
	 */
	public String getChildIndex() {
		return childIndex;
	}

	/**
	 * Sets the child index.
	 *
	 * @param childIndex the new child index
	 */
	public void setChildIndex(String childIndex) {
		this.childIndex = childIndex;
	}

	/**
	 * Gets the parent index.
	 *
	 * @return the parent index
	 */
	public String getParentIndex() {
		return parentIndex;
	}

	/**
	 * Gets the mapping type.
	 *
	 * @return the mapping type
	 */
	public RelationMappingTypeEnum getMappingType() {
		return mappingType;
	}

}
