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
package edu.harvard.integer.service.discovery.snmp;

/**
 * This is an immutable class using for discover element along with element components
 * and build up their containment relationship.
 * 
 * @author dchan
 *
 */
final public class EntityPatternVB implements DevicePhisicalPattern {

	/** The element class used to specify what element class type. */
	private String elementClass;
	
	/** The element name. */
	private String elementName;
	
	/** The element parent relations position. */
	private String elementParentRelPos;
	
	/** Specify where element contained in. */
	private String elementContainedIn;
	
	/**
	 * Instantiates a new immutable EntityPatternVB class
	 *
	 * @param elementClass the element class
	 * @param elementName the element name
	 * @param elementParentRelPos the element parent relation position
	 * @param elementContainedIn the element contained in which parent.
	 */
	public EntityPatternVB( String elementClass, 
			                String elementName, 
			                String elementParentRelPos,
			                String elementContainedIn )
	{
		this.elementClass = elementClass;
		this.elementName = elementName;
		this.elementContainedIn = elementContainedIn;
		this.elementParentRelPos = elementParentRelPos;
	}
	
	/**
	 * Gets the element class.
	 *
	 * @return the element class
	 */
	public String getElementClass() {
		return elementClass;
	}

	/**
	 * Gets the element name.
	 *
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * Gets the element parent relation position.
	 *
	 * @return the element parent rel pos
	 */
	public String getElementParentRelPos() {
		return elementParentRelPos;
	}

	/**
	 * Gets the element contained in.
	 *
	 * @return the element contained in
	 */
	public String getElementContainedIn() {
		return elementContainedIn;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.access.snmp.DevicePhisicalPattern#getPattern()
	 */
	@Override
	public PatternE getPattern() {
		return PatternE.EntityPattern;
	}
}
