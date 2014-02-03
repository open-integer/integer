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
package edu.harvard.integer.service.managementobject.provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBImportInfo;
import edu.harvard.integer.service.managementobject.provider.snmp.MibParser;
import edu.harvard.integer.service.managementobject.provider.snmp.MibParserFactory;
import edu.harvard.integer.service.managementobject.provider.snmp.MibParserFactory.ParserProvider;

/**
 * This Class is the main class for doing parser SNMP MIB using CLI. It provides
 * the following functionalities:
 * 
 * Import MIB into the repository -- Can be imported all Modules in a directory
 * or a file. Verify any error on the current repository. Check what MODULEs
 * containing in the repository.
 * 
 * That repository contains two directories:
 * 
 * ietf -- Contains all common MIBs. vendors -- Contians all vendor MIBs.
 * 
 * 
 * @author dchan
 */
public class ServiceProviderMain {

	/**
	 * The Enum ParserCmdE.
	 */
	public enum ParserCmdE {

 		ImportDir, 
        ImportFile, 
        Verify, 
        GetCommonInfo, 
        GetVendorInfo,
	}

	/** The repository local directory location. */
	public static String MIBFILELOCATON = "mibFileLocation";

	/**  Specify common MIB directory. */
	public static String IETFMIB = "ietf";

	/**  Specify vendors MIB directory. */
	public static String VENDORMIB = "vendors";

	/** CLI cmd. */
	private ParserCmdE cmd;
	
	/** The install directory to hold MIB modules. */
	private String installDir;
	
	/** The import directory to import MIB modules. */
	private String importDir;
	
	/** The import file. */
	private String importFile;

	/**
	 * Gets the usage to display help.
	 *
	 * @return the usage
	 */
	private String getUsage() {

		StringBuffer helpBuf = new StringBuffer();
		helpBuf.append("\nMib parser utility. \n\n");
		helpBuf.append("Usage: \n");
		helpBuf.append("  parserutil ...\n\n");

		helpBuf.append("\n");
		helpBuf.append("--help\n");

		return helpBuf.toString();
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *         <p>
	 *            the arguments contain following options: <br>
	 *               -impDir xxx yyy --- Import all modules in a directory "xxx" import modules
	 *                                   directory. "yyy" output modules location. <br>
	 *               -impFile xxx yyy --- Import a file into a repository. "xxx" file to be imported.
	 *                                    The repository directory.   <br>
	 *               -verify xxx --- "xxx" the directory contains SNMP module files to be verified.  <br>
	 *               -getComm xxx ---  Check Common Module in the repository xxx.  <br>
	 *               -getVen xxx --- Check Vendors Modules int the repository xxx   <br>
	 *           </p>
	 * 
	 */
	public static void main(String[] args) {

		ServiceProviderMain service = new ServiceProviderMain();
		for (int i = 0; i < args.length; i++) {
			if (args[i].trim().equalsIgnoreCase("-impdir")) {
				service.cmd = ParserCmdE.ImportDir;
				service.importDir = args[++i];
				service.installDir = args[++i];
			} else if (args[i].trim().equalsIgnoreCase("-verify")) {

				service.cmd = ParserCmdE.Verify;
				service.installDir = args[++i];
			}
		}

		if ( service.cmd == null ) {
			System.out.println(service.getUsage());
		}
		switch (service.cmd) {

		case GetCommonInfo:
			break;

		case GetVendorInfo:
			break;

		case ImportDir:
			service.doImportDir();
			break;

		case ImportFile:
			break;

		case Verify:
			break;

		default:
			System.out.println(service.getUsage());
		}
	}

	/**
	 * Do import dir.
	 */
	private void doImportDir() {

		List<MIBImportInfo> importMibs = new ArrayList<>();
		String commonDir = importDir + "/ietf";
		File dirf = new File(commonDir);
		File[] fs = dirf.listFiles();
		
		importFiles(importMibs, fs, true);
		
		String vendorDir = importDir + "/vendors";
		dirf = new File(vendorDir);
		fs = dirf.listFiles();
		importFiles(importMibs, fs, false);
		
		System.setProperty(MIBFILELOCATON, installDir);
		try {
			MibParser mibParser = MibParserFactory.getParserSource(ParserProvider.MIBBLE);
			mibParser.importMIB(importMibs.toArray(new MIBImportInfo[importMibs.size()]), true);
		} catch (IntegerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	/**
	 * Import files into importMibs for fs.
	 *
	 * @param importMibs the import mibs
	 * @param fs the fs
	 * @param isCommon if 
	 */
	private void importFiles(List<MIBImportInfo> importMibs, File[] fs, boolean isCommon) {

		for (File f : fs) {

			if (f.isFile()) {
				try {

					String content = getFileContents(f);
					MIBImportInfo minfo = new MIBImportInfo();
					minfo.setMib(content);

					minfo.setStandardMib(isCommon);
					minfo.setFileName(f.getName());
					importMibs.add(minfo);
				} 
				catch (Exception e) {}
			}
		}
	}

	/**
	 * Gets the file contents.
	 * 
	 * @param file
	 *            the file
	 * @return the file contents
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	static String getFileContents(File file) throws IOException {

		String p = file.getAbsolutePath();
		String content = new String(Files.readAllBytes(Paths.get(p)));
		return content;
	}
}
