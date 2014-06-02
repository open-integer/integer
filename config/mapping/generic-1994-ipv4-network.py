#!/usr/bin/env python
#
# mapping for the generic-1994-ipv4-network management group
# by Loren Jan Wilson, 2014-05-23

import integer

# Create Integer object to do the heavy lifting.
mapper = "generic-1994-ipv4-network"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "ipv4-network"
mobj_to_capability = {
    "ipAdEntAddr": "ipv4-network-address",
    "ipAdEntNetMask": "ipv4-network-mask",
    "ipAdEntIfIndex": "ipv4-network-ifIndex"
    "ipAdEntReasmMaxSize": "ipv4-network-reassembleSize"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# The ipv4-network-ifIndex value maps to the interface-index.
relation_type = "association"
match_capabilities = {
    "ipv4-network-ifIndex": "interface-index"
}
integer.build_relations(relation_type, match_capabilities)

# Send the output back to Integer.
integer.dump()
