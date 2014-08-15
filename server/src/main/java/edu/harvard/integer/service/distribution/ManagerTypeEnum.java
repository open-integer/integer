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

import edu.harvard.integer.common.distribution.DistributedManagerInterface;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.BaseManagerInterface;
import edu.harvard.integer.service.discovery.DiscoveryManager;
import edu.harvard.integer.service.discovery.DiscoveryManagerLocalInterface;
import edu.harvard.integer.service.discovery.DiscoveryManagerRemoteInterface;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManager;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerLocalInterface;
import edu.harvard.integer.service.discovery.ServiceElementDiscoveryManagerRemoteInterface;
import edu.harvard.integer.service.event.EventManager;
import edu.harvard.integer.service.event.EventManagerLocalInterface;
import edu.harvard.integer.service.event.EventManagerRemoteInterface;
import edu.harvard.integer.service.inventory.InventoryManager;
import edu.harvard.integer.service.inventory.InventoryManagerLocalInterface;
import edu.harvard.integer.service.inventory.InventoryManagerRemoteInterface;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManager;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerLocalInterface;
import edu.harvard.integer.service.managementobject.ManagementObjectCapabilityManagerRemoteInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManager;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerLocalInterface;
import edu.harvard.integer.service.managementobject.snmp.SnmpManagerRemoteInterface;
import edu.harvard.integer.service.persistance.PersistenceManager;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.selection.SelectionManager;
import edu.harvard.integer.service.selection.SelectionManagerLocalInterface;
import edu.harvard.integer.service.selection.SelectionManagerRemoteInterface;
import edu.harvard.integer.service.technology.TechnologyManager;
import edu.harvard.integer.service.technology.TechnologyManagerLocalInterface;
import edu.harvard.integer.service.technology.TechnologyManagerRemoteInterface;
import edu.harvard.integer.service.topology.TopologyManager;
import edu.harvard.integer.service.topology.TopologyManagerLocalInterface;
import edu.harvard.integer.service.topology.TopologyManagerRemoteInterface;
import edu.harvard.integer.service.topology.device.ServiceElememtAccessManager;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerLocalInterface;
import edu.harvard.integer.service.topology.device.ServiceElementAccessManagerRemoteInterface;
import edu.harvard.integer.service.user.UserManager;
import edu.harvard.integer.service.user.UserManagerLocalInterface;
import edu.harvard.integer.service.user.UserManagerRemoteInterface;
import edu.harvard.integer.service.yaml.YamlManager;
import edu.harvard.integer.service.yaml.YamlManagerLocalInterface;
import edu.harvard.integer.service.yaml.YamlManagerRemoteInterface;


/**
 * Enumeration describing each of the managers.
 * <p>
 * This enumeration is used when asking the distribution manager
 * for an instance of of a mangers 
 * 
 * @author David Taylor
 *
 */
public enum ManagerTypeEnum implements DistributedManagerInterface {
	PersistenceManager(PersistenceManager.class,
			PersistenceManagerInterface.class,
			PersistenceManagerInterface.class),
			
	ServiceElementDiscoveryManager(ServiceElementDiscoveryManager.class,
			ServiceElementDiscoveryManagerLocalInterface.class,
			ServiceElementDiscoveryManagerRemoteInterface.class),
			
	SnmpManager(SnmpManager.class, SnmpManagerLocalInterface.class, SnmpManagerRemoteInterface.class),
	
    ServiceElementAccessManager(ServiceElememtAccessManager.class, ServiceElementAccessManagerLocalInterface.class,
    		ServiceElementAccessManagerRemoteInterface.class),
    
    ManagementObjectCapabilityManager(ManagementObjectCapabilityManager.class,
    		ManagementObjectCapabilityManagerLocalInterface.class,
    		ManagementObjectCapabilityManagerRemoteInterface.class),
    		
    StateManager(StateManager.class, StateManagerLocalInterface.class, StateManagerRemoteInterface.class),
    
    SelectionManager(SelectionManager.class, SelectionManagerLocalInterface.class, SelectionManagerRemoteInterface.class),
    
    TechnologyManager(TechnologyManager.class, TechnologyManagerLocalInterface.class, TechnologyManagerRemoteInterface.class),
    DiscoveryManager(DiscoveryManager.class, DiscoveryManagerLocalInterface.class, DiscoveryManagerRemoteInterface.class),
    EventManager(EventManager.class, EventManagerLocalInterface.class, EventManagerRemoteInterface.class),
    YamlManager(YamlManager.class, YamlManagerLocalInterface.class, YamlManagerRemoteInterface.class),
    TopologyManager(TopologyManager.class, TopologyManagerLocalInterface.class, TopologyManagerRemoteInterface.class),
    UserManager(UserManager.class, UserManagerLocalInterface.class, UserManagerRemoteInterface.class),
    InventoryManager(InventoryManager.class, InventoryManagerLocalInterface.class, InventoryManagerRemoteInterface.class);
	
	Class<? extends BaseManager> mgrClazz;
	Class<? extends BaseManagerInterface> remoteIntfClazz;
	Class<? extends BaseManagerInterface> localIntfClazz;
	
	private ManagerTypeEnum(Class<? extends BaseManager> mgrClazz,
			Class<? extends BaseManagerInterface> localIntfClazz,
			Class<? extends BaseManagerInterface> remoteIntfClazz) {

		this.mgrClazz = mgrClazz;
		this.localIntfClazz = localIntfClazz;
		this.remoteIntfClazz = remoteIntfClazz;
	}

	/**
	 * @return
	 */
	public Class<? extends BaseManager> getBeanClass() {
		
		return mgrClazz;
	}

	/**
	 * @return
	 */
	public Class<? extends BaseManagerInterface> getBeanLocalInterfaceClass() {
		
		return localIntfClazz;
	}
	
	/**
	 * @return
	 */
	public Class<? extends BaseManagerInterface> getBeanRemoteInterfaceClass() {
		
		return remoteIntfClazz;
	}
}
