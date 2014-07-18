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

import org.snmp4j.mp.SnmpConstants;

import edu.harvard.integer.access.Authentication;

/**
 * The Class SnmpAuthentication is the base class for SNMP access control.
 * It implements getProrityBySnmpVersion to defined discovery priority based on version.
 *
 * @author dchan
 */
public abstract class SnmpAuthentication implements Authentication, Comparable<SnmpAuthentication>{

	/**
	 * Gets the snmp version.
	 *
	 * @return the snmp version
	 */
	abstract public int getSnmpVersion();
	
	/**
	 * SNMP timeout for million second. 
	 */
	private int timeOut = 5000;
	
	/**
	 * SNMP try out count.
	 */
	private int tryCount = 2;
	
	
	/**
	 * Gets the time out.
	 *
	 * @return the time out
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Sets the time out.
	 *
	 * @param timeOut the new time out
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Gets the try count.
	 *
	 * @return the try count
	 */
	public int getTryCount() {
		return tryCount;
	}

	/**
	 * Sets the try count.
	 *
	 * @param tryCount the new try count
	 */
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override 
    public int compareTo(SnmpAuthentication o) {
		
		int p1 = getProrityBySnmpVersion(getSnmpVersion());
		int p2 = getProrityBySnmpVersion(o.getSnmpVersion());
		
		return p1 - p2;
    }
	
	
	/**
	 * Get priority based on snmp version. 
	 * The highest priority is v2c, the sencond one is v3 and the third one it v1. 
	 *
	 * @param snmpVersion the snmp version
	 * @return the prority by snmp version
	 */
	private int getProrityBySnmpVersion( int snmpVersion ) {
		
		switch (snmpVersion ) {
		
		   case SnmpConstants.version2c:
			  return 3;
			  
		   case SnmpConstants.version1:
			   return 1;
			   
		   case SnmpConstants.version3:
			   return 2;

		   default:
			   return 1;
		}
	}

}
