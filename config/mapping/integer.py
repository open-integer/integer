# Integer common functions for mappers written in Python.
# by Loren Jan Wilson, 2014-05-23

# Instantiate this class.
# Pull in a set of YAML management group values from Integer.
# Perform a sanity check on the YAML we got to make sure everything we expect is there.
# Pass mechanisms and relations back to Integer in YAML.

import sys
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

    def parse(self):
        # Check to make sure we were passed the correct management group and
        # some rows, and assign them to easy self variables when we find them.
        # First, make sure this group has a name we can see.
        try:
            self.name = self.management_group["name"]
        except:
            print >> sys.stderr, "Fatal error: mapper %s, no management group name present in input YAML.. is this a parsing issue?" % (self.mapper)
            raise
        # Make sure the name matches the mapper that called us.
        if self.name != self.mapper:
            print >> sys.stderr, "Fatal error: mapper %s called on the wrong management group (%s), this shouldn't happen!" % (self.mapper, self.management_group["name"])
            raise
        # Access the rows, and complain if we can't.
        try:
            self.rows = self.management_group["rows"]
        except:
            print >> sys.stderr, "Fatal error: mapper %s, no rows present in input YAML.. is this a parsing issue?" % (self.mapper)
            raise

    def dump(self):
        # We build a dictionary with our mechanisms and relations, and print it
        # back to Integer as YAML.
        output = { "mechanisms": self.mechanisms, "relations": self.relations }
        #print(yaml.dump([output], canonical=True))
        #print(yaml.dump([output], width=9000))
        print(yaml.dump([output], default_flow_style=False, width=1000))

