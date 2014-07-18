use Config;

--
-- The IntegerServer table is used to say what server the managers are used on.
-- The address field MUST be changed to be the server address of the for each
-- server. The FE is the first entry serverId 1
--         THe core server is the second entry serverId 2
--
-- The serverId MUST also be set in the integer.properties file that is located 
-- in directory <wildfly>/modules/system/layers/base/edu/harvard/integer/settings/main/integer.properties
-- on EACH server. 
-- 
INSERT INTO `IntegerServer` (`identifier`, `classType`, `name`, `lastStarted`, `port`, `address`, `serverId`)
VALUES
    (1, 'edu.harvard.integer.common.distribution.IntegerServer', NULL, NULL, 80, 'front_end_ip_address', 1),
	(2, 'edu.harvard.integer.common.distribution.IntegerServer', NULL, NULL, 80, 'core_ip_address', 2);

