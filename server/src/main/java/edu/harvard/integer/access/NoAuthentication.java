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
package edu.harvard.integer.access;


/**
 * The Class NoAuthentication is used for accessing IP nodes without 
 * any authentication needed.  For example ICMP would not need any authentication.
 *
 * @author dchan
 */
public class NoAuthentication implements Authentication {

	/** The access type. */
	final private AccessTypeEnum accessType;
	
	/**
	 * Instantiates a new no authentication.
	 *
	 * @param accessType the access type
	 */
	public NoAuthentication( AccessTypeEnum accessType ) {
		this.accessType = accessType;
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.agent.serviceelement.Authentication#getAccessType()
	 */
	@Override
	public AccessTypeEnum getAccessType() {
		
		return accessType;
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.access.Authentication#isSame(edu.harvard.integer.access.Authentication)
	 */
	@Override
	public boolean isSame(Authentication auth) {
		
		if ( accessType == auth.getAccessType()) 
		{
			return true;
		}
		return false;
	}

}
