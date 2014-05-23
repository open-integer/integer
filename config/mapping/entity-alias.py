#!/usr/bin/env python
#
# mapping for the entity-alias management group
# by Loren Jan Wilson, 2014-05-22

import os
import sys
import yaml
import integer

mapper = "entity-alias"
integer = integer.Integer(mapper)

# For each row, we create a relation from a physical mechanism of unknown type
# to another mechanism of unknown type. Because the value is a row pointer, we
# can't know in advance which kind of mechanism might be referenced. Currently
# we've only seen this implemented as pointers to the ifTable, so that's the
# only kind of relation this mapper knows how to make at the moment.

object_name_to_capability = { 
    "ifIndex": "interface-index"
}

for row in integer.rows:
    # We get back three management objects... a physical index, a logical
    # index, and a mapping identifier. If the logical index is not 0, we pass
    # over this entry, because we don't use the logical table at this time.
    if row["entAliasLogicalIndexOrZero"] != 0:
        continue

    # Parse the mapping identifier. It should be something like "ifIndex.3".
    object_name, dest_index_string = row["entAliasMappingIdentifier"].split('.')
    # I'm not totally sure whether we want an integer or string for each index.
    dest_index = int(dest_index_string)
    # Move on unless we recognize this object name.
    if object_name not in object_name_to_capability:
        continue

    # Build the relation with the single match condition expressed by this row.
    relation = {}
    relation["type"] = "equality"
    relation["source"] = { "physical-index": row["entPhysicalIndex"] }
    relation["destination"] = { object_name_to_capability[object_name]: dest_index }
    integer.relations.append(relation)

integer.dump()

