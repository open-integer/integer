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

package edu.harvard.integer.service.technology;

import java.util.List;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.technology.Mechanism;
import edu.harvard.integer.common.technology.Service;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.common.topology.Capability;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * 
 */
public interface TechnologyManagerInterface extends BaseManagerInterface {

	/**
	 * Update the service in the database. The identifier will be set when the
	 * service is returned.
	 * 
	 * @param service
	 * @return Service with the Identifier filled in
	 * @throws IntegerException
	 */
	Service updateService(Service service) throws IntegerException;

	/**
	 * Get the service identified by the given ID
	 * @param service
	 * @return Service for the given ID
	 * @throws IntegerException
	 */
	Service getServiceById(ID service) throws IntegerException;

	/**
	 * Get all the services that are in the database.
	 * @return List of Services found in the database.
	 * @throws IntegerException
	 */
	Service[] getAllServices() throws IntegerException;

	/**
	 * Update the technology in the database. The identifier will be set when the
	 * service is returned.
	 * @param technology
	 * @return
	 * @throws IntegerException
	 */
	Technology updateTechnology(Technology technology) throws IntegerException;

	/**
	 * Get the technology for the given ID
	 * @param service
	 * @return Technology for the given ID
	 * @throws IntegerException
	 */
	Technology getTechnologyById(ID technologyId) throws IntegerException;

	/**
	 * Get all the Technology objects in the database.
	 * 
	 * @return List of Technology objects found in the database.
	 * @throws IntegerException
	 */
	Technology[] getAllTechnologies() throws IntegerException;

	/**
	 * Update the Mechanism in the database. 
	 * 
	 * @param mechanism
	 * @return Mechanism with the identifier filled in.
	 * @throws IntegerException
	 */
	Mechanism updateMechanism(Mechanism mechanism) throws IntegerException;

	/**
	 * Get the list of Mechanism's objects for the given list of capabilities.
	 * 
	 * @param capabilites
	 * @return List of Mechanisms for the give list of capabilities.
	 */
	List<Mechanism> getMechanisms(List<Capability> capabilites);

	/**
	 * Get all the Mechanisms in the database.
	 * 
	 * @return List of Mechanism's found in the database.
	 * 
	 * @throws IntegerException
	 */
	Mechanism[] getAllMechanisms() throws IntegerException;

	/**
	 * Get the Mechanism for the given ID.
	 * 
	 * @param mechanismId
	 * @return Mechanism for the given ID
	 * 
	 * @throws IntegerException
	 */
	Mechanism getMechanismById(ID mechanismId) throws IntegerException;

	/**
	 * Return the list of top level technologies. 
	 * 
	 * @return
	 * @throws IntegerException
	 */
	Technology[] getTopLevelTechnology() throws IntegerException;

	/**
	 * Find the Technology with the given name. 
	 * 
	 * @param name. Name of the topology objec to get.
	 * 
	 * @return Technology object with the given name.
	 */
	Technology getTechnologyByName(String name) throws IntegerException;

}
