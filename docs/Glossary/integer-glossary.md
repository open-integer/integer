Integer glossary
================

the things we are polling and manipulating 
------------------------------------------

### service element 

A **service element** is a device which we can usually access via one or more
management interfaces. A network switch is a service element, and the ports
inside a switch linecard are also service elements. A fiber patch panel is a
service element too, although it might not have a management interface. 

One management interface does not necessarily equal one service
element&mdash;there can be one management interface that allows us to perform
operations on a number of service elements, such as an API to Amazon EC2, or a
network switch fabric with a shared virtual managment address. 

Service elements can be related to each other, which is normally accomplished
through a hierarchical parenting relationship (a switch "has-a" linecard which
"has-a" port) but which could also look more like a heterarchy (e.g. the
connected-and-equal members of a network switch stack).

### signature

Each service element has a **signature** associated with it which tells us what
type of service element it is. Classifying a service element by type allows us
to make inferences about what we can collect and manage on that device. The
signature of a service element is a collection of **management atoms** which
allow us to classify a service element in the most specific possible way.

For a network device, we will usually use some collection of hardware and
software features to decide on that device's type, including IOS version and
device family. 

In order to build a signature, the management atoms we use must be part of a
generic **management group** which could be polled on any device. This is to
prevent a catch-22&mdash;we need access to the atoms to decide what kind of
service element it is, so we can't be expected to know the device's type before
we collect those atoms.

### characteristics

In order to collect and configure the **management objects** associated with
service elements, we use specific **management groups** that are associated
with the service element's **characteristics**. A characteristic is a
generalization that we make based on a service element's signature. It can be
thought of as a "tag" which can dynamically be associated with a service
element once that device's signature is known.

These characteristics can be defined as generally or specifically as necessary,
from "network switch" to "Cisco switch" to "Cisco 7604 running IOS
12.2(33)SXI4a with the advanced IP services image and redundant Supervisor 720
cards". Associating management groups with general characteristics allows
Integer to discover a network without having to define hundreds of specific
signatures beforehand.


methods we use to poll and manipulate each thing
------------------------------------------------

### management object

A management object is a thing which can be accessed or manipulated on a
device. This includes SNMP OIDs, CLI commands, REST API calls, and so forth.
When polling or manipulating a management object, there may be some output, and
that output may include one or more values that we care about.  A value that we
care about is called a **management atom**. 

### management atom 

A management atom is one value along with an associated concept to tell us what
this value means. Example atoms are the MTU of an interface, an OSPF router ID,
or a VLAN number. 

One management object may provide access to an arbitrary number of management
atoms, such as the CLI command "show ip interface brief" on a Cisco router, or
an SNMP OID which lets us get multiple indexed entries as part of a table.

A management atom's value is stored in Integer's technology representation as
an abstract **capability**.

### management group

A management group is a grouping of management objects.  We group management
objects so that we can associate the groups with particular
**characteristics**, which allows us to infer the methods we should use to
manipulate a general or specific kind of service element. 

A management group can have a parent management group, in which case it
inherits management objects from its parent. This allows us to define new
management groups based on old ones that represent slight changes that come
along with new versions of code, for example.


the underlying technologies we collect, represent, and configure
----------------------------------------------------------------

### technology

A technology is a concept which represents the structure and/or configuration
of service elements, independent of vendor-specific implementation details. 

Often, a technology is something which can be configured onto a set of service
elements that connect those service elements using a common protocol. For
example, OSPF is a technology, and we represent that technology in a consistent
way across all service elements which have OSPF configured, even though OSPF
may be configured using different-looking configuration for each device. 

Technologies can be related to other technologies, and we currently
conceptualize this as a hierarchical **technology tree** using an "is-a"
relationship where, for example, the "dynamic link bonding protocols"
technology may have "LACP" and "PAgP" beneath it on the tree. 

### mechanism type

A technology can have multiple **mechanism types** associated with it which act
together to create the technology's structure. For example, a configured OSPF
technology will have multiple mechanism types including an OSPF process, an
OSPF area, and an OSPF interface. Some technologies are simple, with only one
mechanism type, and other technologies are more complex. 

Each mechanism type has specific **capabilities** associated with it, and it is
assumed that those capabilities are partially defined by the mechanism type
that they are associated with. (For example, the "speed" capability on an
interface mechanism will mean something different from the "speed" capability
on a fan mechanism.)

### mechanism

Each mechanism is an instantiation of a specific mechanism type, and is a
component of a specific technology configuration. For example, the OSPF
interface "192.168.92.1" on a particular router is a mechanism of type "OSPF
interface", and as such, it has certain capabilities associated with it. 

### capability 

Each attribute of a mechanism is called a capability. A capability ties each
abstract technology mechanism to a real-world service element through the
management atom that represents that capability. In other words, management
objects provide access to management atoms, and each atom represents a
capability of a particular mechanism. 

An example of a capability would be the router ID associated with an OSPF
process mechanism, or the MTU of an interface mechanism, or the remote
interface name associated with a CDP cache entry mechanism.


getting from management objects to technologies (coming soon)
----------------------------------------------------------------

### translation

### mapping

### relation



