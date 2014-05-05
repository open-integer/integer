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
package edu.harvard.integer.server.parser.mibparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPModuleHistory;
import edu.harvard.integer.common.snmp.SNMPTable;

/**
 * This class contains all application needed information. It is contains Module
 * information and tables and scale if any on that module.
 * 
 * @author dchan
 * 
 */
public class SNMPModuleCache {

	/**
	 * Contain module information.
	 */
	private SNMPModule module;
	
	private List<SNMPModuleHistory> history = null;

	/**
	 * SNMP tables contained by the module.
	 */
	private List<SNMPTable> tbllist = new ArrayList<>();

	/**
	 * Any scale contained by the module.
	 */
	private List<SNMP> scalelist = new ArrayList<>();
	
	private List<SNMP> objectIdentifiers = new ArrayList<SNMP>();

	/**
	 * A map for fast searching based on OID.
	 */
	private Map<String, SNMP> attMap = new ConcurrentHashMap<String, SNMP>();

	public SNMPModuleCache(SNMPModule module) {
		this.module = module;
	}

	public SNMPModule getModule() {
		return module;
	}

	public Map<String, SNMP> getAttMap() {
		return attMap;
	}

	public List<SNMPTable> getTbllist() {
		return tbllist;
	}

	public List<SNMP> getScalelist() {
		return scalelist;
	}

	public String getName() {
		return module.getName();
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

	/**
	 * @return the objectIdentifiers
	 */
	public List<SNMP> getObjectIdentifiers() {
		return objectIdentifiers;
	}

	/**
	 * @param objectIdentifiers the objectIdentifiers to set
	 */
	public void setObjectIdentifiers(List<SNMP> objectIdentifiers) {
		this.objectIdentifiers = objectIdentifiers;
	}

}
