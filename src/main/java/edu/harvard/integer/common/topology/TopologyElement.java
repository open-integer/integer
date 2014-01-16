package edu.harvard.integer.common.topology;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import edu.harvard.integer.common.Address;
import edu.harvard.integer.common.BaseEntity;

/*
 * TopologyElement instances provide details for each ServiceElement about its addresses, 
 * address types and layer of the network hierarchy in which they exist. 
 */
@Entity
public class TopologyElement extends BaseEntity {

	@ManyToOne
	private Address address = null;
}
