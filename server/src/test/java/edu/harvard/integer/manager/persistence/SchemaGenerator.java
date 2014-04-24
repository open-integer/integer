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

package edu.harvard.integer.manager.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


/**
 * 
 * 
 */
public class SchemaGenerator {
	
	private Configuration cfg; 
	
	@SuppressWarnings("unchecked")
	public SchemaGenerator(String packageName) throws Exception {
		
		cfg = new Configuration();
		
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		
		for (Class<Object> clazz : getClasses(packageName)) {
			cfg.addAnnotatedClass(clazz);
		}
	}

	/**
	 * Method that actually creates the file.
	 * 
	 * @param dbDialect
	 *            to use
	 */
	void generate(Dialect dialect) {
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

		SchemaExport export = new SchemaExport(cfg);
		
		export.setDelimiter(";");
		
		export.setOutputFile("build/create_" + dialect.name().toLowerCase() + ".sql");
		export.execute(true, false, false, true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();

		SchemaGenerator gen = new SchemaGenerator("edu.harvard.integer");
		gen.generate(Dialect.MYSQL);
		
	}

	/**
	 * Utility method used to fetch Class list based on a package name.
	 * 
	 * @param packageName
	 *            (should be the package containing your annotated beans.
	 */
	@SuppressWarnings("rawtypes")
	private List<Class> getClasses(String packageName) throws Exception {
		List<Class> classes = new ArrayList<Class>();
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			
			directory = new File( "../common/src/main/java/" + packageName.replace('.', '/'));
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(packageName + " (" + directory
					+ ") does not appear to be a valid package");
		}

		if (directory.exists()) {
			addFiles(packageName, directory, classes);
		} else {
			throw new ClassNotFoundException(directory.getAbsolutePath()
					+ " is not a valid package");
		}

		return classes;
	}

	private void addFiles(String packageName, File directory,
			@SuppressWarnings("rawtypes") List<Class> classes) {
		
		String[] files = directory.list();
		
		if (files == null) {
			System.out.println("Dir is empty " + directory);
			return;
		}
		
		for (int i = 0; i < files.length; i++) {
			

			if (files[i].endsWith(".java")) {
				// removes the .class extension
				try {
					System.out.println("add class " + files[i]);
					classes.add(Class.forName(packageName + '.'
							+ files[i].substring(0, files[i].length() - 5)));
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
			} else {
				
				File subDir = new File(directory.getAbsoluteFile() + "/" + files[i]);
				if (subDir.exists())
					addFiles(packageName + "." + files[i], subDir, classes);
			}
		}
	}

	/**
	 * Holds the classnames of hibernate dialects for easy reference.
	 */
	public static enum Dialect {
		ORACLE("org.hibernate.dialect.Oracle10gDialect"), MYSQL(
				"org.hibernate.dialect.MySQLDialect"), HSQL(
				"org.hibernate.dialect.HSQLDialect");

		private String dialectClass;

		private Dialect(String dialectClass) {
			this.dialectClass = dialectClass;
		}

		public String getDialectClass() {
			return dialectClass;
		}
	}
}