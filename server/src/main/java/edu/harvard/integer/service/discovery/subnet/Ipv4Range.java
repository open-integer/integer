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
package edu.harvard.integer.agent.serviceelement.discovery;


/**
 * The Class Ipv4Range is used to store the starting ipaddress and the ending ipaddress 
 * within a subnet for discovery.  It provides two methods for iterating, hasNext and next.
 * 
 * @author dchan
 */
final public class Ipv4Range {
	
	/** The start ipaddress */
	public final String startIp;
	
	/** The end ipaddress */
	public final String endIp;
	
	/** The current ipaddress. */
	private String currIp;
	
	/**
	 * Instantiates a new Ipv4Ranage instance.
	 *
	 * @param startIp the start ip
	 * @param endIp the end ip
	 */
	public Ipv4Range( String startIp, String endIp ) {
		
		this.startIp = startIp;
		this.endIp = endIp;
		currIp = startIp;
	}

	/**
	 * Gets the current ipaddress.
	 *
	 * @return the curr ip
	 */
	public String getCurrIp() {
		return currIp;
	}
	
	
	/**
	 * Checks for avaiable for next IP address.
	 *
	 * @return true, if avaiable.
	 */
	public synchronized boolean hasNext() {	
		
		if ( currIp == null ) 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Next ipaddress in the subnet.
	 *
	 * @return the string
	 */
	public synchronized  String next() 
	{
		String returnIp = currIp;
		if ( endIp.equals(currIp) ) {
			currIp = null;
		}
		
		if ( currIp != null ) {
			currIp = getNextIPV4Address(currIp);
		}
		return returnIp;
	}

	
	/**
	 * Reset the current ipaddress to start ipaddress
	 */
	public synchronized void startOver() {
		
		currIp = startIp;
	}
	
	/**
	 * Get the next IPAddress based on a given IP address.
	 * 
	 * @param ip
	 * @return  -- next IPAddress. 
	 */
	public static String getNextIPV4Address(String ip) {
	    String[] nums = ip.split("\\.");
	    int i = (Integer.parseInt(nums[0]) << 24 | Integer.parseInt(nums[2]) << 8
	          |  Integer.parseInt(nums[1]) << 16 | Integer.parseInt(nums[3])) + 1;

	    // If you wish to skip over .255 addresses.
	    if ((byte) i == -1) i++;

	    return String.format("%d.%d.%d.%d", i >>> 24 & 0xFF, i >> 16 & 0xFF,
	                                        i >>   8 & 0xFF, i >>  0 & 0xFF);
	}
}
