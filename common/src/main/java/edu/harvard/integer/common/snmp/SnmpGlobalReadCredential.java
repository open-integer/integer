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

package edu.harvard.integer.common.snmp;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.topology.Credential;

/**
 * This is a global set of SnmpCredentials the system will use ABSENT an
 * SnmpCredentials object associated with an ipToplogySeed. It is expected that
 * these Global credentials will be used by other parts of the system as they
 * are implemented (if needed - most likely we would not be communicating with a
 * system we had not discovered or are in the process of discovering. For
 * example the data collection system and perhaps the configuration system.
 * <p/>
 * There can be only one GlobalSnmpCredentials instance per TopologyManager
 * instance.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class SnmpGlobalReadCredential extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The ordered list of community strings to try when doing the SNMP-based
	 * discovery. After a system has been discovered we will have a community
	 * string to use and this will be the default for this device until it is
	 * changed - probably via user interface.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<Credential> credentials = null;

	/**
	 * An ordered listing of alternate ports (other than 161, the default) that
	 * can be used for SNMP-based discovery. Users can change the default if
	 * needed.
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn(name = "idx")
	private List<Integer> alternatePorts = null;

	/**
	 * @return the credentials
	 */
	public List<Credential> getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the alternatePorts
	 */
	public List<Integer> getAlternatePorts() {
		return alternatePorts;
	}

	/**
	 * @param alternatePorts the alternatePorts to set
	 */
	public void setAlternatePorts(List<Integer> alternatePorts) {
		this.alternatePorts = alternatePorts;
	}
}
