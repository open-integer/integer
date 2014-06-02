#!/usr/bin/env python
#
# mapping for the generic-ifmib-iftable management group
# by Loren Jan Wilson, 2014-06-02

import integer

# Create Integer object to do the heavy lifting.
mapper = "generic-ifmib-iftable"
integer = integer.Integer(mapper)

# The "interface-speed" item needs to be divided by 1000000 to store the speed
# as megabits, but we're not doing that here.
mechanism_type = "interface"
mobj_to_capability = {
    "ifIndex": "interface-index",
    "ifDescr": "interface-longname",
    "ifType": "interface-type",
    "ifMtu": "interface-mtu",
    "ifSpeed": "interface-speed",
    "ifPhysAddress": "interface-physicalAddress",
    "ifAdminStatus": "interface-adminStatus",
    "ifOperStatus": "interface-operationalStatus",
    "ifLastChange": "interface-lastChangeTime",
    "ifInOctets": "interface-inputOctets",
    "ifInUcastPkts": "interface-inputUnicastPackets",
    "ifInDiscards": "interface-inputDiscards",
    "ifInErrors": "interface-inputErrors",
    "ifInUnknownProtos": "interface-inputUnknownProtocols",
    "ifOutOctets": "interface-outputOctets",
    "ifOutUcastPkts": "interface-outputUnicastPackets",
    "ifOutErrors": "interface-outputErrors"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Send the output back to Integer.
integer.dump()
