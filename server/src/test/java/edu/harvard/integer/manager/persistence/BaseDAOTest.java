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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.MIBInfo;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.common.snmp.SNMPModule;
import edu.harvard.integer.service.persistance.dao.snmp.MIBInfoDAO;

/**
 * @author David Taylor
 *
 */
public class BaseDAOTest {

	@Test
	public void createCleanCopy() {
		
		Logger logger = LoggerFactory.getLogger(BaseDAOTest.class);
		
		MIBInfo mibInfo = new MIBInfo();
		mibInfo.setName("Name");
		mibInfo.setIdentifier(Long.valueOf(1));
		mibInfo.setVendor("Vendor");
		
		SNMPModule module = new SNMPModule();
		module.setName("ModuleName");
		module.setDescription("Module description");
		module.setLastUpdated(new Date());
		module.setOid("1.2.3.4.5.6.7.8.9");
		
		mibInfo.setModule(module);
		
		List<SNMP> scalors = new ArrayList<SNMP>();
		SNMP oid = new SNMP();
		oid.setName("OidName1");
		oid.setDescription("OID Description1");
		oid.setDisplayName("OID displayname1");
		oid.setOid("1.2.3.4.5.6.7.1");
		scalors.add(oid);
		
		oid.setName("OidName2");
		oid.setDescription("OID Description2");
		oid.setDisplayName("OID displayname2");
		oid.setOid("1.2.3.4.5.6.7.2");
		scalors.add(oid);
		
		mibInfo.setScalors(scalors);
		
		MIBInfoDAO dao = new MIBInfoDAO(null, LoggerFactory.getLogger(MIBInfoDAO.class));
		
		try {
			MIBInfo copy = dao.createCleanCopy(mibInfo);
			
			assert(copy.getName().equals(mibInfo.getName()));
			assert(copy.getIdentifier().equals(mibInfo.getIdentifier()));
			assert(copy.getVendor().equals(mibInfo.getVendor()));
			
			assert(copy.getModule().getName().equals(mibInfo.getModule().getName()));
			assert(copy.getModule().getDescription().equals(mibInfo.getModule().getDescription()));
			assert(copy.getModule().getOid().equals(mibInfo.getModule().getOid()));
			
			
			System.out.println("Module: " + mibInfo.getModule().getName() + " Last Update: " + mibInfo.getModule().getLastUpdated() 
					+ " OID: " + oid + " Desc: " + mibInfo.getModule().getDescription() + " Vendor: " + mibInfo.getVendor()
					+ " OID's " + Arrays.toString(mibInfo.getScalors().toArray()));
			

			System.out.println("Module: " + copy.getModule().getName() + " Last Update: " + copy.getModule().getLastUpdated() 
					+ " OID: " + oid + " Desc: " + copy.getModule().getDescription() + " Vendor: " + copy.getVendor()
					+ " OID's " + Arrays.toString(copy.getScalors().toArray()));
			
		} catch (IntegerException e) {
			
			e.printStackTrace();
			fail("Error in createCleanCopy! " + e.getMessage());
		} 
			
	}

	@Test
	public void exportSchema() {
		try {
			SchemaGenerator generator = new SchemaGenerator("edu.harvard.integer");
			generator.generate(SchemaGenerator.Dialect.MYSQL);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

}
