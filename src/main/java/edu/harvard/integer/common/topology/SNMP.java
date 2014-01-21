/*
 *  Copyright (c) 2013 Harvard University and the persons
 *  identified as authors of the code.  All rights reserved. 
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 * 	.    Redistributions of source code must retain the above copyright
 * 		 notice, this list of conditions and the following disclaimer.
 * 
 * 	.    Redistributions in binary form must reproduce the above copyright
 * 		 notice, this list of conditions and the following disclaimer in the
 * 		 documentation and/or other materials provided with the distribution.
 * 
 * 	.    Neither the name of Harvard University, nor the names of specific
 * 		 contributors, may be used to endorse or promote products derived from
 * 		 this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *      
 */
package edu.harvard.integer.common.topology;

import javax.persistence.Entity;
/**
 * @author David Taylor
 *
 */
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
