#!/usr/bin/env python
#
# mapping for the cisco-cdp-global management group
# by Loren Jan Wilson, 2014-05-23

import integer

# Create Integer object to do the heavy lifting.
mapper = "cisco-cdp-global"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "cdp-global"
mobj_to_capability = {
    "cdpGlobalRun": "cdp-global-run",
    "cdpGlobalDeviceId": "cdp-global-deviceId"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Send the output back to Integer.
integer.dump()
