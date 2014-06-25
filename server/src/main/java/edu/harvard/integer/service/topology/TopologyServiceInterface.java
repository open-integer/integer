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

package edu.harvard.integer.service.topology;

import edu.harvard.integer.service.BaseServiceInterface;

/**
 * The DiscoveryService will start an instance of the TopologyManager based on
 * DiscoveryRules. Even in the case of a rule that is not determining network
 * topology, the TopologyManager is used to calculate the hosts to discover on a
 * network.
 * <p>
 * When the topology information is required, the ToplogyManager uses the
 * ServiceElementDiscoveryManager to collect required details for systems so
 * that it knows what types of objects to retrieve from the network systems to
 * get the topology data.
 * <p>
 * It is responsible for using data collected from the environment alone or in
 * combination with user input, or exclusively with user input to create Path
 * instances. These instances describe the connection information at different
 * layers of the environment. In the same way that it collects topology data
 * from service elements for Layer 2/3 topology, it also uses information
 * obtainable from topology modifying service elements like firewalls, load
 * balancers and site selectors to retrieve information about those functions so
 * that the system can show how traffic is impacted by these topology modifiers.
 * <p>
 * Note that the TopologyManager can also be called by the DiscoveryService to
 * integrate topologies across discovery seeds. This means that if, for example,
 * to seeds ran that terminated with adjacencies not connected because of
 * 'radius' restrictions, the topology manager could stitch them together
 * afterwards.
 * @author David Taylor
 *
 */
public interface TopologyServiceInterface extends BaseServiceInterface {
	
	
}
