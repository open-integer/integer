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

import org.apache.commons.net.util.SubnetUtils;

/**
 * The Class DiscoverNet is used to specify which net or node for discovery
 * or need to be excluded.  There are two lists for discovery.  One contains the nodes and
 * another one contains net. The discover engine will start the discover on nodes
 * first.
 *
 * @author dchan
 */
public class DiscoverNet {

	private SubnetUtils utils;
	
	private int startIpi;
	private int endIpi;
	


	public DiscoverNet( String ipnet, String mask ) {
		
		utils = new SubnetUtils(ipnet, mask);
		startIpi = utils.getInfo().asInteger( utils.getInfo().getLowAddress() );
		endIpi = utils.getInfo().asInteger( utils.getInfo().getHighAddress() );
	}
	
	public DiscoverNet( String cidr ) 
	{
		utils = new SubnetUtils(cidr);
	}
	

	/**
	 * Gets the network.
	 *
	 * @return the network
	 */
	public String getNetwork() {
		return utils.getInfo().getAddress();
	}
	
	
	
	/**
	 * Gets the netmask.
	 *
	 * @return the netmask Specify the netmask on the network. 
	 */
	public String getNetmask() {
		return utils.getInfo().getNetmask();
	}
	
	
	/**
	 * 
	 * @param remoteAddr
	 * @return
	 */
	public boolean isInRange( String remoteAddr )  {
		
	    int address = utils.getInfo().asInteger( remoteAddr );
	    return startIpi <= address && address <= endIpi;
	}
	
	
	public int getIpInteger( String addr )
	{
		return utils.getInfo().asInteger( addr );
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getStartIpi() {
		return startIpi;
	}

	/**
	 * 
	 * @return
	 */
	public int getEndIpi() {
		return endIpi;
	}
	
	
	public String getStartIp() {
		return utils.getInfo().getLowAddress();
	}
	
	public String getEndIp() {
		return utils.getInfo().getHighAddress();
	}
}
