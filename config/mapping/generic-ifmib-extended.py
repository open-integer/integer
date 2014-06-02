#!/usr/bin/env python
#
# mapping for the generic-ifmib-extended management group
# by Loren Jan Wilson, 2014-06-02

import integer

# Create Integer object to do the heavy lifting.
mapper = "generic-ifmib-extended"
integer = integer.Integer(mapper)

# This mapping will overwrite some items from the ifmib-iftable mapping with
# 64-bit values if they're available. Also, in this case, ifHighSpeed is in
# megabits by default, so we don't need to divide it by 1000000.
mechanism_type = "interface"
mobj_to_capability = {
    "ifName": "interface-name",
    "ifHCInOctets": "interface-inputOctets",
    "ifHCInUcastPkts": "interface-inputUnicastPackets",
    "ifHCInMulticastPkts": "interface-inputMulticastPackets",
    "ifHCInBroadcastPkts": "interface-inputBroadcastPackets",
    "ifHCOutOctets": "interface-outputOctets",
    "ifHCOutUcastPkts": "interface-outputUnicastPackets",
    "ifHCOutMulticastPkts": "interface-outputMulticastPackets",
    "ifHCOutBroadcastPkts": "interface-outputBroadcastPackets",
    "ifLinkUpDownTrapEnable": "interface-linkUpDownTrapEnable",
    "ifHighSpeed": "interface-speed",
    "ifPromiscuousMode": "interface-promiscuousMode",
    "ifConnectorPresent": "interface-connectorPresent",
    "ifAlias": "interface-description"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Send the output back to Integer.
integer.dump()
