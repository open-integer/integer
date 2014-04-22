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

package edu.harvard.integer.common.topology;

import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;

/**
 * @author David Taylor
 * 
 *         Some systems have difficulty processing snmp messages or snmp
 *         messages over a certain rate. In those cases, this object allows the
 *         system to tailor its behavior to service element types.
 */
@Entity
public class SnmpServiceElementTypeOverride extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Maximum rate per second to send messages to a serviceElement of this
	 * Type.
	 */
	private Integer maxRate = null;

	/**
	 * Number of retries for messages for which there has been no response.
	 */
	private Integer retries = null;

	/**
	 * SNMP Timeout value.
	 */
	private Integer timeout = null;

	/**
	 * The maximum number of outstanding messages for an instance of this
	 * serviceElementType.
	 */
	private Integer maxOutstanding = null;

	/**
	 * 
	 */
	public SnmpServiceElementTypeOverride() {

	}

	/**
	 * @return the maxRate
	 */
	public Integer getMaxRate() {
		return maxRate;
	}

	/**
	 * @param maxRate
	 *            the maxRate to set
	 */
	public void setMaxRate(Integer maxRate) {
		this.maxRate = maxRate;
	}

	/**
	 * @return the retries
	 */
	public Integer getRetries() {
		return retries;
	}

	/**
	 * @param retries
	 *            the retries to set
	 */
	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	/**
	 * @return the timeout
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the maxOutstanding
	 */
	public Integer getMaxOutstanding() {
		return maxOutstanding;
	}

	/**
	 * @param maxOutstanding
	 *            the maxOutstanding to set
	 */
	public void setMaxOutstanding(Integer maxOutstanding) {
		this.maxOutstanding = maxOutstanding;
	}

}
