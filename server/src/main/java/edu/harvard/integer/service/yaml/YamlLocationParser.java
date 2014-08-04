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

package edu.harvard.integer.service.yaml;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.user.Location;
import edu.harvard.integer.common.yaml.YamlLocation;
import edu.harvard.integer.service.distribution.DistributionManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.topology.TopologyManagerInterface;

/**
 * @author David Taylor
 * 
 */
public class YamlLocationParser {

	private YamlLocation[] yamlLocations = null;
	
	private Location[] allLocations = null;
	
	/**
	 * @param yamlLocations
	 */
	public YamlLocationParser(YamlLocation[] yamlLocations) {
		this.yamlLocations = yamlLocations;
	}

	public String parse() throws IntegerException {

		if (yamlLocations == null)
			return "NoData";

		if (yamlLocations.length == 0)
			return "NoData";
		
		
		TopologyManagerInterface topologyManager = DistributionManager.getManager(ManagerTypeEnum.TopologyManager);
		allLocations = topologyManager.getAllLocations();
		
		for (YamlLocation location : yamlLocations) {
			Location dbLocation = getLocationByName(location.getBuildingName());
			
			if (dbLocation.getIdentifier() == null) {
				dbLocation.setName(location.getBuildingName());
			}
			
			dbLocation.setAddress1(location.getAddr1());
			dbLocation.setAddress2(location.getAddr2());
			dbLocation.setCity(location.getCity());
			dbLocation.setState(location.getStateProvence());
			dbLocation.setPostalCode(location.getPostalCode());
			dbLocation.setOther(location.getBuildingID());
			
			topologyManager.updateLocation(dbLocation);
		}
		
		return "success";
	}
	
	private Location getLocationByName(String name) {
		for (Location location : allLocations) {
			if (location.getName().equals(name))
				return location;
		}
		
		return new Location();
	}
}
