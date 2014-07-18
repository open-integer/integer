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
 * Base class for IPV4 and IPV6 address.
 * 
 * @author David Taylor
 * 
 * 
 */
@Embeddable
public class Address implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private String address = null;

	private String mask = null;

	public Address() {
		this.address = null;
	}

	public Address(String address, String mask) {
		this.address = address;
		this.mask = mask;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * @param mask
	 *            the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

	public static Long dottedIPToLong(String address) {
		String[] parts = address.split("\\.");

		Long result;
		int token_a = Integer.valueOf(parts[0]);
		int token_b = Integer.valueOf(parts[1]);
		int token_c = Integer.valueOf(parts[2]);
		int token_d = Integer.valueOf(parts[3]);


		long a = (long) (token_a * (Math.pow(2, 24)));
		long b = (long) (token_b * (Math.pow(2, 16)));
		long c = (long) (token_c * (Math.pow(2, 8)));
		long d = new Long(token_d).longValue();
	
		result = Long.valueOf(a + b + c + d);

		return result;
	}

	public static String longToDottedIPString(Long integer_ip) {
		long ip = integer_ip.longValue();

		int inta = (int) ((ip >> 24) & 0xFF);
		int intb = (int) ((ip >> 16) & 0xFF);
		int intc = (int) ((ip >> 8) & 0xFF);
		int intd = (int) ((ip) & 0xFF);

		StringBuffer b = new StringBuffer();
		b.append(inta);
		b.append(".");

		b.append(intb);
		b.append(".");

		b.append(intc);
		b.append(".");

		b.append(intd);

		return b.toString();
	}

	public static String getSubNet(Address address) {
		return getSubNet(address.getAddress(), address.getMask());
	}
	
	public static String getSubNet(String address, String mask) {
		if (mask == null)
			mask = "255.255.255.0";
		
		Long theIP = dottedIPToLong(address);
		Long theMask = dottedIPToLong(mask);
		long baseAddress = theIP.intValue() & theMask.intValue();

		return longToDottedIPString(Long.valueOf(baseAddress));
	}
}
