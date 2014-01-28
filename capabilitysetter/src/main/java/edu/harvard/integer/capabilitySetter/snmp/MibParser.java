package edu.harvard.integer.capabilitySetter.snmp;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import edu.harvard.integer.capabilitySetter.CapabilitySetterException;
import edu.harvard.integer.capabilitySetter.ModuleLoadingLog;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.SNMP;


public interface MibParser {

	public List<String>  getSupportCommonModuleNames() throws CapabilitySetterException;
	public List<String>  getSupportModuleNames() throws CapabilitySetterException;
	public void loadModule( File moduleFile) throws CapabilitySetterException;
	
	public List<ModuleLoadingLog> importMIB( List<MIBImportInfo> mibinfos ) throws CapabilitySetterException;
	public HashMap<String, SNMP>  getLoadedSNMPObjects() throws CapabilitySetterException;
	
}
