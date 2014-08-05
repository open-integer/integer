use Config;

--
-- The serverId column must match the serverId column from the IntegerServer table that the
-- manger is to be run on. For the FE server all the managers will be on the core server. 
--
INSERT INTO `DistributedManager` (`identifier`, `classType`, `name`, `managerType`, `serverId`)
VALUES
	(1, 'edu.harvard.integer.common.distribution.DistributedManager', 'PersistenceManager', 'PersistenceManager', 2),
	(2, 'edu.harvard.integer.common.distribution.DistributedManager', 'ServiceElementDiscoveryManager', 'ServiceElementDiscoveryManager', 2),
	(3, 'edu.harvard.integer.common.distribution.DistributedManager', 'SnmpManager', 'SnmpManager', 2),
	(4, 'edu.harvard.integer.common.distribution.DistributedManager', 'ServiceElementAccessManager', 'ServiceElementAccessManager', 2),
	(5, 'edu.harvard.integer.common.distribution.DistributedManager', 'ManagementObjectCapabilityManager', 'ManagementObjectCapabilityManager', 2),
	(6, 'edu.harvard.integer.common.distribution.DistributedManager', 'StateManager', 'StateManager', 2),
	(7, 'edu.harvard.integer.common.distribution.DistributedManager', 'SelectionManager', 'SelectionManager', 2),
	(8, 'edu.harvard.integer.common.distribution.DistributedManager', 'TechnologyManager', 'TechnologyManager', 2),
	(9, 'edu.harvard.integer.common.distribution.DistributedManager', 'DiscoveryManager', 'DiscoveryManager', 2),
	(10, 'edu.harvard.integer.common.distribution.DistributedManager', 'EventManager', 'EventManager', 2),
	(11, 'edu.harvard.integer.common.distribution.DistributedManager', 'YamlManager', 'YamlManager', 2),
	(12, 'edu.harvard.integer.common.distribution.DistributedManager', 'TopologyManager', 'TopologyManager', 2),
	(13, 'edu.harvard.integer.common.distribution.DistributedManager', 'UserManager', 'UserManager', 2);


