# Integer common functions for mappers written in Python.
# by Loren Jan Wilson, 2014-05-23

# Instantiate this class.
# Pull in a set of YAML management group values from Integer.
# Perform a sanity check on the YAML we got to make sure everything we expect is there.
# Pass mechanisms and relations back to Integer in YAML.

import sys
import logging
import yaml

class Integer:
    def __init__(self, mapper):
        # Keep track of the mapper that instantiated this class.
        self.mapper = mapper
        # We load via stdin a collected management group in YAML format. This
        # shows up as a list of rows, each of which has an id and a list of
        # management objects.  
        self.management_group = yaml.load(sys.stdin)
        # Load rows into self.rows for easy access.
        self.parse()
        # Mappers that instantiate this object will add mechanisms and
        # relations as necessary. Each mechanism includes a mechanismType and
        # a mapping of atoms-to-capabilities.
        self.mechanisms = []
        self.relations = []
        # Provide a logging object.
        log_format = '%(levelname)s %(asctime)s %(message)s'
        logging.basicConfig(level=logging.WARNING, format=log_format)
        self.logger = logging.getLogger("integer")


    def parse(self):
        # Check to make sure we were passed the correct management group and
        # some rows, and assign them to easy self variables when we find them.
        # First, make sure this group has a name we can see.
        try:
            self.name = self.management_group["name"]
        except:
            self.logger.error("[mapper %s] no management group name present in input YAML.. is this a parsing issue?", self.mapper)
            raise
        # Make sure the name matches the mapper that called us.
        if self.name != self.mapper:
            self.logger.error("[mapper %s] called on the wrong management group (%s), this shouldn't happen!", self.mapper, self.management_group["name"])
            raise
        # Access the rows, and complain if we can't.
        try:
            self.rows = self.management_group["rows"]
        except:
            self.logger.error("[mapper %s] no rows present in input YAML.. is this a parsing issue?", self.mapper)
            raise

    def dump(self):
        # Build a dictionary with our mechanisms and relations, and print it
        # back to Integer as YAML.
        output = { "mechanisms": self.mechanisms, "relations": self.relations }
        print(yaml.dump(output, default_flow_style=False, width=9000))

        # We might need to switch to canonical format depending on how the Java
        # YAML import and the Python YAML export work with the kinds of values
        # we see in the wild.  If we do, just comment out the above print line
        # and uncomment this one:

        #print(yaml.dump(output, canonical=True, width=9000))

    def build_mechanisms(self, mechanism_type, mobj_to_capability):
        # Given a mechanism type and a management-objects-to-capabilities
        # dictionary, make mechanisms from the management group rows we loaded.
        for row in self.rows:
            # Build a mechanism for this row, including a capabilities list and
            # a mechanism type.
            mechanism = {}
            mechanism["capabilities"] = {}
            mechanism["mechanismType"] = mechanism_type
            # Iterate through the management objects for this row.
            for mobj_name, mobj_value in row.items():
                # Make sure we know what capability this management object is.
                # If we don't, we'll complain to stderr and continue.
                try:
                    capability_name = mobj_to_capability[mobj_name]
                except:
                    self.logger.warning("[mapper %s] Saw a management object we didn't recognize! (name: %s, value: %s)", self.mapper, mobj_name, mobj_value)
                    continue
                # Add this capability to our mechanism's capabilities list.
                mechanism["capabilities"][capability_name] = mobj_value
            # Add the mechanism to our list.
            self.mechanisms.append(mechanism)

    def build_relations(self, relation_type, relation_match):
        # Given a relation type and a capability-to-capability matching
        # dictionary, make mechanisms from the management group rows we loaded.
        for mechanism in self.mechanisms:
            # Go through the mechanisms that exist and create relations for each.
            try:
                # Build a relation with source and destination match conditions.
                relation = {}
                relation["type"] = relation_type
                relation["source"] = {}
                relation["destination"] = {}
                # For each match condition, flesh out the relation.
                for src_cap, dest_cap in relation_match.items():
                    # The source and destination capabilities will share the
                    # same value. If your management group is more complex than
                    # this, don't use this function and instead create these
                    # manually in your mapping code.
                    try:
                        shared_value = mechanism["capabilities"][src_cap]
                        relation["source"][src_cap] = shared_value
                        relation["destination"][dest_cap] = shared_value
                    except KeyError:
                        self.logger.warning("[mapper %s] Couldn't find a capability '%s' for this entry, so can't create relation!", self.mapper, src_cap)
                        continue

                # Append this relation to our list.
                self.relations.append(relation)
            except Exception, ex:
                self.logger.exception("[mapper %s] Couldn't create relation for unknown reason!", self.mapper)
                continue

