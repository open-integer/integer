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

package edu.harvard.integer.service.distribution;

import javax.ejb.Remote;

import edu.harvard.integer.common.distribution.DistributedManager;
import edu.harvard.integer.common.distribution.DistributedService;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.BaseServiceInterface;

/**
 * The distribution service is responsible for where all the services and managers
 * are running as well as the state of each of the services, managers and servers 
 * in the integer system. 
 * <p>
 * The distribution service will run on each server so that all services and managers
 * on each server can find a local or remote manager or service.

 * @author David Taylor
 *
 */
@Remote
public interface DistributionServiceInterface extends BaseServiceInterface {

	/**
	 * Get the list of managers in the system.
	 * @return List of DistributedManger's.
	 * @throws IntegerException
	 */
	DistributedManager[] getManagers() throws IntegerException;

	/**
	 * Get the list of services in the system.
	 * @return List of DistribtuedService's 
	 * 
	 * @throws IntegerException
	 */
	DistributedService[] getServices() throws IntegerException;

}
