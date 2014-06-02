#!/usr/bin/env python
#
# mapping for the generic-ifmib-global management group
# by Loren Jan Wilson, 2014-06-02

import integer

# Create Integer object to do the heavy lifting.
mapper = "generic-ifmib-global"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "interface-global"
mobj_to_capability = {
    "ifNumber": "interface-global-number",
    "ifTableLastChange": "interface-global-lastChangeTime"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Send the output back to Integer.
integer.dump()
