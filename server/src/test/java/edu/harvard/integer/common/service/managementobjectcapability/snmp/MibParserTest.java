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

package edu.harvard.integer.common.service.managementobjectcapability.snmp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.common.snmp.MIBImportResult;
import edu.harvard.integer.server.parser.mibparser.MibParser;
import edu.harvard.integer.server.parser.mibparser.MibParserFactory;

/**
 * @author David Taylor
 *
 */
public class MibParserTest {

	@Test
	public void loadCiscoProductMib() {
		
		MIBImportResult result = importMib("CISCO-PRODUCTS-MIB.my");
		
		System.out.println("Got " + result.getObjectIdentifiers().size() + " OIDS");
		
	}
	
	public MIBImportResult importMib(String mibName) {
		
		try {
			File mibFile = null;
			mibFile = new File("mibs/" + mibName);

			if (mibFile.exists())
				System.out.println("Found rfc");
			else {
				System.out.println("rfc not found! PATH: "
						+ mibFile.getAbsolutePath());

				fail("rfc not found! PATH: " + mibFile.getAbsolutePath());
			}

			String content = null;
			try {
				content = new String(Files.readAllBytes(mibFile.toPath()));

			} catch (IOException e) {

				e.printStackTrace();
				fail("Error loading MIB: " + e.getMessage());
			}

			MIBImportInfo importInfo = new MIBImportInfo();
			importInfo.setFileName(mibFile.getName());
			importInfo.setMib(content);


			System.out.println("Load Product MIB " + importInfo.getFileName() + " Size "
					+ importInfo.getMib().length());

			MibParser mibParser = MibParserFactory
					.getParserSource(MibParserFactory.ParserProvider.MIBBLE);
			MIBImportResult[] results = mibParser.importMIB(
					new MIBImportInfo[] { importInfo }, true);
			
			
			return results[0];
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		
		return null;
		
	}

}
