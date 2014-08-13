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

import java.util.List;

import org.apache.commons.net.util.SubnetUtils;

import edu.harvard.integer.common.topology.Subnet;

/**
 * This class contains static methods using for subnet discovery.
 * 
 * @author dchan
 *
 */
public class SubnetUtil {

	/**
	 * Check if discoverNet in a passing subnet list.s
	 * 
	 * @param discoverNet
	 * @param subnets
	 * @return
	 */
	public static boolean isSubnetInList( DiscoverNet discoverNet, List<Subnet> subnets ) {
		
		for ( Subnet subnet : subnets ) {
			
		    if ( discoverNet.getNetmask().equals("255.255.255.255") ) {
			
		    	/**
		    	 * If the discoverNet mask is 255.255.255.255, it is considering associated to an IP address.
		    	 * Not a subnet.  If the "subnet" is also an IPAddress, check if they equal or not,
		    	 * if they are equal, return true;
		    	 */
		    	String subMask = subnet.getAddress().getMask();
		    	if ( subMask.equals("255.255.255.255") ) {
		    		
		    		if ( subnet.getAddress().getAddress().equals(discoverNet.getIpAddress()) ) {
		    			return true;
		    		}
		    	}	
		    	else {
		    		
		    		/**
		    		 * if the subnet is not associated with an IPAddress, check if discoveNet is within the net or not.
		    		 */
		    		SubnetUtils utils = new SubnetUtils(subnet.getAddress().getAddress(), subMask);
			    	long checkIpl = DiscoverNet.ipToInt(discoverNet.getIpAddress());
			    	
			    	if ( DiscoverNet.ipToInt(utils.getInfo().getLowAddress()) <= checkIpl 
			    			         && checkIpl >= DiscoverNet.ipToInt(utils.getInfo().getHighAddress()) ) {
			    		return true;
			    	}
		    	}
		    }
		    else {
		    	
		    	String subMask = subnet.getAddress().getMask();
		    	if ( subMask.equals("255.255.255.255") ) {
		    		
		    		/**
		    		 * if the subnet is associated with an IPAddress, check if both IPAddress are equal or not.
		    		 */
		    		if( discoverNet.getIpAddress().equals(subnet.getAddress().getAddress()) ) {
		    			return true;
		    		}
		    	}
		    	else if ( subMask.equals(discoverNet.getNetmask()) ) {
		    		
		    		/**
		    		 * If both mask are equal, check if their network address is equal or not.
		    		 */
		    		SubnetUtils utils = new SubnetUtils(subnet.getAddress().getAddress(), subMask);
		    		if ( utils.getInfo().getNetworkAddress().equals(discoverNet.getNetworkAddress())) {
		    			return true;
		    		}
		    		
		    	}
		    }
		}
		return false;
	}
}
