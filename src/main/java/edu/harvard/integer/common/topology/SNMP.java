package edu.harvard.integer.common.topology;

import javax.persistence.Entity;

@Entity
public class SNMP extends ServiceElementManagementObject {

	/*
	 * Description of the object as found in the MIB Module. In some cases, this
	 * description can be quite long.
	 */
	public String description = null;

	/*
	 * This defines the read, read/write, or some objects (bad objects) are
	 * write only. This is a function of the object definition, not the access
	 * policy.
	 */
	public MaxAccess maxAccess = null;


	/*
	 * The fully specified OID of the object (minus the instance data). For
	 * tables this would include the table glue.
	 */
	public String oid = null;

	/*
	 * From the object definition.
	 */
	public String textualConvetion = null;

	/*
	 * In some cases this information is not appropriate (orther than say
	 * string). In others degrees, or other information may be useful. In some
	 * cases this may come from or be equal to the Textual convention
	 * information.
	 */
	public String units = null;

	/*
	 * The value of an instance of this object. The context of this value is
	 * dependent on the context in which it is viewed. It can be contained in a
	 * list that represent the configured or operational state of a service
	 * element as it is retrieved from a device - that is the real world value
	 * of the object at a specific instant of time.
	 * 
	 * This value is blank in the portion of the system where objects are
	 * defined.
	 */
	//public Object value = null;

}
