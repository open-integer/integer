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

package edu.harvard.integer.common;

import java.io.Serializable;

import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

/**
 * Fake class used to force GWT to add classes to the whitelist. The white list
 * is used to say what classes can be serialized and sent to the client. The
 * inherited abstract classes do not get added to the whitelist.
 * 
 * @author David Taylor
 * 
 */
public class GWTWhitelist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BaseEntity baseEntity = null;

	@SuppressWarnings("unused")
	private ServiceElementManagementObject snmp = null;

	@SuppressWarnings("unused")
	private String classType = null;

	@SuppressWarnings("unused")
	private IDType idType = null;

	private ServiceElement[] serviceElementList;

	public GWTWhitelist() {
		super();
	}

	/**
	 * @return the baseEntity
	 */
	public BaseEntity getBaseEntity() {
		return baseEntity;
	}

	/**
	 * @param baseEntity
	 *            the baseEntity to set
	 */
	public void setBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

	/**
	 * @return the serviceElementList
	 */
	public ServiceElement[] getServiceElementList() {
		return serviceElementList;
	}

	/**
	 * @param serviceElementList
	 *            the serviceElementList to set
	 */
	public void setServiceElementList(ServiceElement[] serviceElementList) {
		this.serviceElementList = serviceElementList;
	}

}
