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
package edu.harvard.integer.agent.serviceelement.access;

import edu.harvard.integer.agent.serviceelement.AccessTypeEnum;

/**
 * The Class AccessUtil contains static methods used to access ip nodes.
 *
 * @author dchan
 */
public class AccessUtil {

	/**
	 * Gets the default port associated with each access type.
	 *
	 * @param access the access
	 * @return the default port
	 */
	public static int getDefaultPort( AccessTypeEnum access ) {
		
		switch (access) {
		    case NFS:
			    return 2049;

		    case PUPPET_CLIENT:
		    	return 8139;
		    	
		    case SNMPv1:
		    case SNMPv2c:
		    case SNMPv3:
		    	return 161;
		    	
		    case SNMPTRAP:
		    	return 162;
		    	
		    case PUPPET_MASTER:
		    	return 8140;
		    
		    case SSH:
		    	return 22;
		    	
		    case SYSLOG:
		    	return 514;
		    		
		    default:
			    break;
		}
		
		return -1;
	}
}
