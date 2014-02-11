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

package edu.harvard.integer.common.snmp;

import java.util.List;

/**
 * 
 * @author David Taylor
 * 
 *         Hold the result of a MIB import.
 */
public class MIBImportResult extends MIBImportInfo {

	/**
	 * The SNMP module. It can be null if the imported MIB does not pass parser.
	 */
	private SNMPModule module;

	private List<SNMPModuleHistory> history = null;

	/**
	 * SNMP table list in the module. It can be null if table is not available
	 */
	private List<SNMPTable> snmpTable;

	/**
	 * SNMP scalar list in the module. It can be null if table is not available
	 */
	private List<SNMP> snmpScalars;

	private String[] errors = null;

	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
	public String[] getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 * 
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	public SNMPModule getModule() {
		return module;
	}

	public void setModule(SNMPModule module) {
		this.module = module;
	}

	/**
	 * @return the snmpTable
	 */
	public List<SNMPTable> getSnmpTable() {
		return snmpTable;
	}

	/**
	 * @param snmpTable
	 *            the snmpTable to set
	 */
	public void setSnmpTable(List<SNMPTable> snmpTable) {
		this.snmpTable = snmpTable;
	}

	/**
	 * @return the snmpScalars
	 */
	public List<SNMP> getSnmpScalars() {
		return snmpScalars;
	}

	/**
	 * @param snmpScalars
	 *            the snmpScalars to set
	 */
	public void setSnmpScalars(List<SNMP> snmpScalars) {
		this.snmpScalars = snmpScalars;
	}

	/**
	 * @return the history
	 */
	public List<SNMPModuleHistory> getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(List<SNMPModuleHistory> history) {
		this.history = history;
	}

}
