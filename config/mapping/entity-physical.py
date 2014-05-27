#!/usr/bin/env python
#
# mapping for physical hardware coming from the entity-physical management group
# by Loren Jan Wilson, 2014-05-22

import integer

mapper = "entity-physical"

# Pull in the YAML and do sanity checking.
integer = integer.Integer(mapper)

# This mapper decides which types of mechanisms to make by looking at the
# "entPhysicalClass" value and deciding based on the value of that. So here is
# a dict we can use to make that decision.
class_to_type = {
    "chassis": "physical-chassis",
    "container": "physical-container",
    "powerSupply": "physical-powerSupply",
    "fan": "physical-fan",
    "sensor": "physical-sensor",
    "module": "physical-module",
    "port": "physical-port",
    "stack": "physical-stack",
    "everything else": "physical-hardware"
}

# Since we're going from one table to several different kinds of mechanism
# types which all inherit from one general "hardware" mechanism type, all of
# the capabilities are the same.
mobj_to_capability = {
    "entPhysicalIndex": "physical-index",
    "entPhysicalDescr": "physical-description",
    "entPhysicalVendorType": "physical-vendorType",
    "entPhysicalContainedIn": "physical-parentIndex",
    "entPhysicalClass": "physical-class",
    "entPhysicalParentRelPos": "physical-relativePosition",
    "entPhysicalName": "physical-name",
    "entPhysicalHardwareRev": "physical-hardwareRevision",
    "entPhysicalFirmwareRev": "physical-firmwareRevision",
    "entPhysicalSoftwareRev": "physical-softwareRevision",
    "entPhysicalSerialNum": "physical-serialNumber",
    "entPhysicalMfgName": "physical-vendor",
    "entPhysicalModelName": "physical-model",
    "entPhysicalIsFRU": "physical-fru",
}

# For each row, build a mechanism and append it to the mechanism list.
for row in integer.rows:
    # Build a mechanism to pass back for this row, which will include a
    # capabilities list.
    mechanism = {}
    mechanism["capabilities"] = {}

    # Iterate through the management objects for this row. Each row is a
    # dictionary, and each key is a management object name. 
    for mobj_name, mobj_value in row.items():
        # Make sure we know what capability this management object is. If we
        # don't, we'll complain to stderr and continue.
        try:
            capability_name = mobj_to_capability[mobj_name]
        except:
            print >> sys.stderr, "Mapper %s: Saw a management object we didn't recognize! (name: %s, value: %s)" % (mapper, mobj_name, mobj_value)
            continue

        # Add this capability to our mechanism's capabilities list.
        mechanism["capabilities"][capability_name] = mobj_value

        # Set the mechanism type based on the value of the physical-class capability.
        if capability_name == "physical-class":
            if mobj_value in class_to_type:
                mechanism["mechanismType"] = class_to_type[mobj_value]
            else:
                # If we don't have an entry in our class_to_type dictionary,
                # just use the most general type.
                mechanism["mechanismType"] = "physical-hardware"

    # Add the mechanism we just built to our list.
    integer.mechanisms.append(mechanism)

# Now go through the mechanisms we made and create a parent relation for each
# one if it exists.
for mechanism in integer.mechanisms:
    # Don't create a relation unless parentIndex exists and is not 0.
    try:
        if mechanism["capabilities"]["physical-parentIndex"] > 0:
            relation = {}
            relation["type"] = "parent"
            relation["source"] = { "physical-index": mechanism["capabilities"]["physical-index"] }
            relation["destination"] = { "physical-index": mechanism["capabilities"]["physical-parentIndex"] }
            integer.relations.append(relation)
    except:
        integer.logger.debug("[mapper %s] Not creating relation for a mechanism because it looks like there is no value for capability 'physical-parentIndex' for this mechanism", integer.mapper)
        continue

# Send the completed mechanisms and relations back to Integer.
integer.dump()

