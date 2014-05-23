#!/usr/bin/env python
#
# mapping for the cisco-cdp-cache management group
# by Loren Jan Wilson, 2014-05-23

import integer

# Instantiate Integer object.
mapper = "cisco-cdp-cache"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "cdp-cache"
mobj_to_capability = {
    "cdpCacheIfIndex": "cdp-cache-ifIndex",
    "cdpCacheDeviceIndex": "cdp-cache-deviceIndex",
    "cdpCacheAddressType": "cdp-cache-addressType",
    "cdpCacheAddress": "cdp-cache-address",
    "cdpCacheVersion": "cdp-cache-version",
    "cdpCacheDeviceId": "cdp-cache-deviceId",
    "cdpCacheDevicePort": "cdp-cache-devicePort",
    "cdpCachePlatform": "cdp-cache-platform"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Simple parent relation with capability-to-capability matching.
relation_type = "parent"
match_capabilities = {
    "cdp-cache-ifIndex": "cdp-interface-index"
}
integer.build_relations(relation_type, match_capabilities)

# Topology relation with multiple matching fields.
relation_type = "topology"
match_capabilities = {
    "cdp-cache-deviceId": "cdp-global-deviceId",
    "cdp-cache-devicePort": "cdp-interface-name"
}
integer.build_relations(relation_type, match_capabilities)

# Send the output back to Integer.
integer.dump()
