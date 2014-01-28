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
package edu.harvard.integer.capabilitySetter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.harvard.integer.capabilitySetter.snmp.MibParserFactory;
import edu.harvard.integer.capabilitySetter.snmp.MibParserFactory.ParserProvider;
import edu.harvard.integer.capabilitySetter.snmp.MibParser;
import edu.harvard.integer.common.snmp.MIBImportInfo;


/**
 * @author dchan
 *
 */
public class CapabilitySetterMain {

public static String MibResourceDir = "mibs/ietf/";
	
	public static String IANA = "iana";	
	public static String MIBFILELOCATON = "mibFileLocation";
	public static String IETFMIB = "ietf";
	public static String VENDORMIB = "vendors";
	
	public static void main(String[] args)  {

		List<MIBImportInfo>  importMibs = new ArrayList<>();
		String readMibs = "/Users/dchan/sandbox/integer/capabilitysetter/testfinal/ietf";
		File dirf = new File(readMibs);
		
		File[] fs = dirf.listFiles();
		for ( File f : fs ) {
			
			if ( f.isFile() ) {
				
				try {
										
				    String content = getFileContents(f);
				    MIBImportInfo minfo = new MIBImportInfo();
				    minfo.setMib(content);
				    
				    minfo.setFileName(f.getName());
				    importMibs.add(minfo);
				}
				catch ( Exception e) 
				{
					
				}
			}
		}
		System.setProperty(MIBFILELOCATON, "/Users/dchan/sandbox/integer/capabilitysetter/testmibs");
		MibParser mibParser =  MibParserFactory.getModuleSource(ParserProvider.MIBBLE);
		try {
			mibParser.importMIB(importMibs);
		} 
		catch (CapabilitySetterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		
		try {
			mibParser.getLoadedSNMPObjects();
		} 
		catch (CapabilitySetterException e) {
			e.printStackTrace();
		}
		*/		
		System.out.println("After mib parser ... ");
	}

	static String getFileContents(File file)  throws IOException {
		
		String p = file.getAbsolutePath();
		String content = new String(Files.readAllBytes(Paths.get(p)));		
		return content;
     }
}
