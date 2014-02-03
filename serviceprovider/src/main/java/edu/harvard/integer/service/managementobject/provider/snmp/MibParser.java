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
package edu.harvard.integer.service.managementobject.provider.snmp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.common.snmp.SNMP;

/**
 * @author dchan
 *
 */
public interface MibParser {

	/**
	 * 
	 * @return List of common Modules supported in the repository'
	 * 
	 * @throws IntegerException
	 */
	public List<String>  getSupportCommonModuleNames() throws IntegerException;
	
	/**
	 * 
	 * @return list of vendor modules supported in the repository.
	 * 
	 * @throws CapabilitySetterException
	 */
	public List<String>  getSupportVendorModuleNames() throws IntegerException;
	
	/**
	 * Load a module file into the system.
	 * 
	 * @param moduleFile  Loaded file.
	 * 
	 * @throws CapabilitySetterException
	 */
	public void loadModule( File moduleFile) throws IntegerException;
	
	/**
	 * Import mib module into the system.  
	 * If a module in the list is already exist, skip it and use the one in the repository. 
	 * This method will install modules which are successfully loaded in the repository.
	 * The return contains an error log list and the one being loaded.
	 * 
	 * @param mibinfos  Import MIB modules.
	 * @return -- Return error log if any and modules which are successfully loaded.
	 * 
	 * @throws CapabilitySetterException
	 */
	public MIBImportResult[] importMIB( MIBImportInfo[] mibinfos, boolean replaceExist ) throws IntegerException;
	
	
	/**
	 * Return all loaded SNMP modules.  The key is an OID for a SNMP table or a SNMP object.
	 * @return
	 * @throws CapabilitySetterException
	 */
	public HashMap<String, SNMP>  getLoadedSNMPObjects() throws IntegerException;
	
}
