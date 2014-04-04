/*
 *  Copyright (c) 2014 Harvard University and the persons
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

package edu.harvard.integer.service.managementobject.snmp;

import java.util.List;

import javax.ejb.Local;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * 
 */
@Local
public interface SnmpManagerInterface extends BaseManagerInterface {

	/**
	 * This method will be called to import a MIB into the system. The MIB is
	 * passed in since the user will point to a MIB in the UI. The file will
	 * then be read in and sent to the server to be processed.
	 * 
	 * @param mibFile
	 *            - Contents of MIB to import
	 * @return TODO
	 */
	public MIBImportResult[] importMib(MIBImportInfo[] mibFile)
			throws IntegerException;

	/**
	 * Get the list of MIB's that have been imported into the system.
	 * 
	 * @return List<File>. The list of imported mibs.
	 * @throws IntegerException
	 */
	public MIBInfo[] getImportedMibs() throws IntegerException;

	public MIBInfo getMIBInfoByID(ID id) throws IntegerException;

	/**
	 * @param name
	 * @return
	 * @throws IntegerException
	 */
	List<SNMP> findByNameStartsWith(String name) throws IntegerException;

	/**
	 * Get the SNMP object specified by the string OID.
	 * 
	 * @param oid
	 *            . OID to get the SNMP object for.
	 * 
	 * @return SNMP object for the givne OID
	 * @throws IntegerException
	 */
	SNMP getSNMPByOid(String oid) throws IntegerException;

	/**
	 * Update the SNMP OID.
	 * 
	 * @param oid
	 * @return SNMP the updated OID. If this oid is new then the identifier will
	 *         be filled in after this call.
	 * @throws IntegerException
	 */
	SNMP updateSNMP(SNMP oid) throws IntegerException;
}