#!/usr/bin/env python
#
# mapping for the generic-1994-ipv4-global management group
# by Loren Jan Wilson, 2014-05-23

import integer

# Create Integer object to do the heavy lifting.
mapper = "generic-1994-ipv4-global"
integer = integer.Integer(mapper)

# Simple mechanisms with one-to-one management object and capability mapping.
mechanism_type = "ipv4-network"
mobj_to_capability = {
    "ipForwarding": "ipv4-global-forwarding",
    "ipDefaultTTL": "ipv4-global-defaultTTL",
    "ipInReceives": "ipv4-global-inputReceives",
    "ipInHdrErrors": "ipv4-global-inputHeaderErrors",
    "ipInUnknownProtos": "ipv4-global-unknownProtocols",
    "ipInDiscards": "ipv4-global-inputDiscards",
    "ipInDelivers": "ipv4-global-inputDeliveries",
    "ipOutRequests": "ipv4-global-outputRequests",
    "ipOutDiscards": "ipv4-global-outputDiscards",
    "ipOutNoRoutes": "ipv4-global-outputNoRoutes",
    "ipReasmTimeout": "ipv4-global-reassemblyTimeout",
    "ipReasmReqds": "ipv4-global-reassemblyRequired",
    "ipReasmOKs": "ipv4-global-reassemblyOk",
    "ipReasmFails": "ipv4-global-reassemblyFails",
    "ipFragOKs": "ipv4-global-outputFragmentOk",
    "ipFragFails": "ipv4-global-outputFragmentFails",
    "ipFragCreates": "ipv4-global-outputFragments"
}
integer.build_mechanisms(mechanism_type, mobj_to_capability)

# Send the output back to Integer.
integer.dump()
