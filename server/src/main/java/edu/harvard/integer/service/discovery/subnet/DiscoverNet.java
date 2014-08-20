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
package edu.harvard.integer.service.discovery.subnet;

import java.io.Serializable;

import org.apache.commons.net.util.SubnetUtils;

/**
 * The Class DiscoverNet is used to specify which net or node for discovery
 * or need to be excluded.  There are two lists for discovery.  One contains the nodes and
 * another one contains net. The discover engine will start the discover on nodes
 * first.
 *
 * @author dchan
 */
public class DiscoverNet implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private SubnetUtils utils;
	
	/**
	 * Starting Subnet IP address in long format
	 */
	private long startIpi;
	
	/**
	 * Ending Subnet IP address in long format.
	 */
	private long endIpi;
	
	/**
	 * Radius count down to keep track for next subnet discovery.
	 * If this value is equal to 0, no more next subnet discovery.
	 */
	private int radiusCountDown = 0;


	/**
	 * Boolean to indicate this subnet being discvoer or not.
	 */
	private boolean discoverYet = false;
	



	/**
	 * 
	 * @param ipnet
	 * @param mask
	 * @param radiusCount
	 */
	public DiscoverNet( String ipnet, String mask, int radiusCount ) {
		
		/**
		 * 255.255.255.255 is special case.  In this case it only point to one address.
		 */
		if ( mask.equalsIgnoreCase("255.255.255.255")) {
			
			startIpi = ipToInt(ipnet);
			endIpi = ipToInt(ipnet);
			
		}
		else {
			utils = new SubnetUtils(ipnet, mask);
			startIpi = utils.getInfo().asInteger( utils.getInfo().getLowAddress() );
			endIpi = utils.getInfo().asInteger( utils.getInfo().getHighAddress() );
		}
		
		this.radiusCountDown = radiusCount;
	}
	
	
	/**
	 * 
	 * @param cidr
	 * @param radiusCount
	 */
	public DiscoverNet( String cidr, int radiusCount ) 
	{
		utils = new SubnetUtils(cidr);
		startIpi = utils.getInfo().asInteger( utils.getInfo().getLowAddress() );
		endIpi = utils.getInfo().asInteger( utils.getInfo().getHighAddress() );
		
		this.radiusCountDown = radiusCount;
	}
	

	/**
	 * Gets the network.
	 *
	 * @return the network
	 */
	public String getIpAddress() {
		
		if ( utils == null ) {
			return longToIp(startIpi);
		}
		return utils.getInfo().getAddress();
	}
	
	
	
	/**
	 * Gets the netmask.
	 *
	 * @return the netmask Specify the netmask on the network. 
	 */
	public String getNetmask() {
		
		if ( utils != null )
		     return utils.getInfo().getNetmask();
		
		return "255.255.255.255";
	}
	
	
	/**
	 * 
	 * @param remoteAddr
	 * @return
	 */
	public boolean isInRange( String remoteAddr )  {
		
		if ( utils != null ) {
			 int address = utils.getInfo().asInteger( remoteAddr );
			 return startIpi <= address && address <= endIpi;
		}
		
		if ( remoteAddr.equals(longToIp(startIpi) ) )  {
			return true;
		}
		return false;
	   
	}
	
	
	public int getIpInteger( String addr )
	{
		if ( utils != null )
		   return utils.getInfo().asInteger( addr );
		
		return (int) startIpi;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public long getStartIpi() {
		return startIpi;
	}

	/**
	 * 
	 * @return
	 */
	public long getEndIpi() {
		return endIpi;
	}
	
	
	public String getStartIp() {
		
		if ( utils == null ) {
			return longToIp(startIpi);
		}
		return utils.getInfo().getLowAddress();
	}
	
	public String getEndIp() {
		
		if ( utils == null ) {
			return longToIp(startIpi);
		}
		return utils.getInfo().getHighAddress();
	}
	
	public String getNetworkAddress() {
		
		if ( utils == null ) {
			return longToIp(startIpi);
		}
		return utils.getInfo().getNetworkAddress();
	}
	
	
	/**
	 * '
	 * @param ipAddress
	 * @return
	 */
	public static long ipToInt(String ipAddress) {
		 
		String[] ipAddressInArray = ipAddress.split("\\.");
	 
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
	 
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);
	 
		}
	 
		return result;
	}
	
	/**
	 * Get CIDR of the network.  If the mask is "255.255.255.255" 
	 * return null.
	 * @return
	 */
	public String getCidr() {
		
		if ( utils != null ) {
			return utils.getInfo().getCidrSignature();					
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public static String longToIp(long i) {
		 
		return ((i >> 24) & 0xFF) + 
                   "." + ((i >> 16) & 0xFF) + 
                   "." + ((i >> 8) & 0xFF) + 
                   "." + (i & 0xFF);
 
	}
	
	
	
	public int radiusCountDownDecrease() {
		if ( radiusCountDown > 0 ) {
			radiusCountDown--;
		}
		return radiusCountDown;
	}
	
	
	public int getRadiusCountDown() {
		return radiusCountDown;
	}
	

	public boolean isDiscoverYet() {
		return discoverYet;
	}


	public void setDiscoverYet(boolean discoverYet) {
		this.discoverYet = discoverYet;
	}

}
