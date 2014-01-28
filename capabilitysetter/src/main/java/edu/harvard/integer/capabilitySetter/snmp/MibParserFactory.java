package edu.harvard.integer.capabilitySetter.snmp;

import edu.harvard.integer.capabilitySetter.snmp.moduleLoader.MibbleParser;

public class MibParserFactory {

	public enum ParserProvider {
	    MIBBLE   	
	}
	
	public static MibParser getModuleSource( ParserProvider provider ){
		
		switch (provider) {
		   
		   case MIBBLE:			
			   return MibbleParser.getInstance();

		   default:
			  break;
		}
		return null;
	}
}
