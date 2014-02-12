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

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

/**
 * @author David Taylor
 * 
 *         Identifies an organization. Organizations have
 *         hierarchies/containment. That is why this element can contain more
 *         Organizations.
 */
@Entity
public class Orginization extends BaseEntity {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1L;

	private String orginizationType = null;

	/**
	 * This is a list of the id of the Id class instances associated with
	 * location objects for this organization. Generally an organization will
	 * have several locations each with a different 'type'. For example, support
	 * center, development center, business office, etc.
	 */
	@ElementCollection
	@OrderColumn(name="idx")
	private List<ID> locations = null;
	
	/**
	 * list of orinizations that this orginization belongs to.
	 */
	@ElementCollection
	@OrderColumn(name="idx")
	private List<ID> orinizations = null;

	/**
	 * @return the type
	 */
	public String getType() {
		return orginizationType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.orginizationType = type;
	}

	/**
	 * @return the orinizations
	 */
	public List<ID> getOrinizations() {
		return orinizations;
	}

	/**
	 * @param orinizations
	 *            the orinizations to set
	 */
	public void setOrinizations(List<ID> orinizations) {
		this.orinizations = orinizations;
	}

}
