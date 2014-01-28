package edu.harvard.integer.capabilitySetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.common.snmp.SNMPTable;

public class SNMPModuleCache {

	private SNMPModule module;
	private List<SNMPTable> tbllist = new ArrayList<>();
	private List<SNMP> scalelist = new ArrayList<>();
	
	private Map<String, SNMP> attMap;
	
	public SNMPModuleCache( SNMPModule module ) 
	{
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

	
	public String getName() 
	{
		return module.getName();
	}
	
}
