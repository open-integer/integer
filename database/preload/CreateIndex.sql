 ALTER TABLE `Capability` ADD INDEX `CapabilityNameIdx` (`name`);
 ALTER TABLE `ServiceElementManagementObject` ADD INDEX `SEMO_nameIdx` (`name`);
 ALTER TABLE `VendorIdentifier` ADD INDEX `VendorIdIDx` (`vendorSubtypeId`);
 ALTER TABLE `ServiceElementType` ADD INDEX `SETNameIdx` (`name`);

