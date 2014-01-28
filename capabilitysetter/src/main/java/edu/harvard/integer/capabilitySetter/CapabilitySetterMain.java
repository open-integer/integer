package edu.harvard.integer.capabilitySetter;

import edu.harvard.integer.capabilitySetter.snmp.MibParserFactory;
import edu.harvard.integer.capabilitySetter.snmp.MibParserFactory.ParserProvider;
import edu.harvard.integer.capabilitySetter.snmp.MibParser;


public class CapabilitySetterMain {

public static String MibResourceDir = "mibs/ietf/";
	
	public static String IANA = "iana";	
	public static String MIBFILELOCATON = "mibFileLocation";
	public static String IETFMIB = "ietf";
	public static String VENDORMIB = "vendors";
	
	public static void main(String[] args) {

		System.setProperty(MIBFILELOCATON, "/Users/dchan/sandbox/integer/capabilitysetter/mibs");
		
		MibParser mibParser =  MibParserFactory.getModuleSource(ParserProvider.MIBBLE);
		try {
			mibParser.getLoadedSNMPObjects();
		} 
		catch (CapabilitySetterException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("After mib parser ... ");
	}

}
