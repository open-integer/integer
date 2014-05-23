#!/usr/bin/env python
#
# mapping for the cisco-cdp-interface management group
# by Loren Jan Wilson, 2014-05-23

import integer

# Create Integer object to do the heavy lifting.
mapper = "cisco-cdp-interface"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "cdp-interface"
mobj_to_capability = {
    "cdpInterfaceIfIndex": "cdp-interface-index",
    "cdpInterfaceEnable": "cdp-interface-enable",
    "cdpInterfaceName": "cdp-interface-name"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Simple relations with capability-to-capability matching.
relation_type = "equality"
match_capabilities = {
    "cdp-interface-index": "interface-index"
}
integer.build_relations(relation_type, match_capabilities)

# Send the output back to Integer.
integer.dump()
