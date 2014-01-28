package edu.harvard.integer.capabilitySetter;

import edu.harvard.integer.common.snmp.MIBImportInfo;

public class ModuleLoadingLog {

	public enum ErrorTypeE {
		
		MissingModuleError, SyntaxError, OtherError
	}
	
	private ErrorTypeE err;
	private String errDescription;
	private MIBImportInfo targetMib;
	private String internalName;
	
	public ModuleLoadingLog( MIBImportInfo mibInfo ) {
		
		this.targetMib = mibInfo;
	}
	
	
	public ErrorTypeE getErr() {
		return err;
	}

	public void setErr(ErrorTypeE err) {
		this.err = err;
	}

	public String getErrDescription() {
		return errDescription;
	}

	public void setErrDescription(String errDescription) {
		this.errDescription = errDescription;
	}

	public MIBImportInfo getTargetMib() {
		return targetMib;
	}


	public String getInternalName() {
		return internalName;
	}


	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	
	
	
}
