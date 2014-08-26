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

package edu.harvard.integer.common;

import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import edu.harvard.integer.common.snmp.SNMP;

/**
 * @author David Taylor
 *
 */
public class TestUtil {

	/**
	 * 
	 */
	public TestUtil() {

	}

	public static List<ID> createIdList(int numToCreate, Class<? extends BaseEntity> clazz, String name) {
		List<ID> ids = new ArrayList<ID>();
		for (int i = 0; i < 10; i++) 
			ids.add(new ID(Long.valueOf(i), name + i, new IDType(clazz.getName())));

		return ids;
	}
	
	public static SNMP createOid(String name, String oidString) {
		SNMP oid = new SNMP();
		oid.setName(name);
		oid.setDisplayName(name);
		oid.setOid(oidString);

		return oid;
	}
	
	public static Archive<?> createTestArchive(String warName) {
		return ShrinkWrap
				.create(WebArchive.class, warName)
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addPackages(true, "org.apache.commons")
				.addPackages(true, "org.snmp4j")
				.addPackages(true, "uk.co.westhawk.snmp")
				.addPackages(true, "org.jboss")
				.addPackages(true, "org.wildfly")
				.addPackages(true, "org.xnio")
				.addPackages(true, "org.slf4j.logger")
				.addPackages(true, "org/jasypt")
				.addPackages(true, "com.fasterxml.jackson")
				.addPackages(true, "org.yaml")
				.addPackages(true, "edu.uci.ics.jung")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-ds.xml");
	}
	
	public static Archive<?> createTestMySqlArchive(String warName) {
		return ShrinkWrap
				.create(WebArchive.class, warName)
				.addPackages(true, "edu.harvard.integer")
				.addPackages(true, "net.percederberg")
				.addPackages(true, "org.apache.commons")
				.addPackages(true, "org.snmp4j")
				.addPackages(true, "uk.co.westhawk.snmp")
				.addPackages(true, "org.jboss")
				.addPackages(true, "org.wildfly")
				.addPackages(true, "org.xnio")
				.addPackages(true, "org.slf4j.logger")
				.addPackages(true, "org/jasypt")
				.addPackages(true, "com.fasterxml.jackson")
				.addPackages(true, "org.yaml")
				.addPackages(true, "edu.uci.ics.jung")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test data source
				.addAsWebInfResource("test-mysql-ds.xml");
	}
	
	
	
}
