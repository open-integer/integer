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

package edu.harvard.integer.service.persistance.dao.discovery;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;

import edu.harvard.integer.common.discovery.VendorIdentifier;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.snmp.SNMP;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * @author David Taylor
 *
 */
public class VendorIdentifierDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public VendorIdentifierDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, VendorIdentifier.class);

	}

	public VendorIdentifier findByVendorOid(String identifier) throws IntegerException {
		return findByStringField(identifier, "vendorOid", VendorIdentifier.class);
	}
	
	public VendorIdentifier findByVendorSubtypeId(String identifier) throws IntegerException {
		return findByStringField(identifier, "vendorSubtypeId", VendorIdentifier.class);
	}
	
	/**
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public List<VendorIdentifier> findByOidSubtree(String name) {

		Query createQuery = getEntityManager().createQuery("select vid from VendorIdentifier vid where vendorSubtypeId like :name");
		createQuery.setParameter("name", name + ".%");
		
		List<VendorIdentifier> resultList = createQuery.getResultList();

		if (resultList.size() > 0) {
			if (getLogger().isDebugEnabled())
				getLogger().debug(
						"Found OID " + resultList.get(0).getIdentifier() + " "
								+ resultList.get(0).getVendorSubtypeId() + " "
								+ resultList.get(0).getName());

			return resultList;
		} else
			return null;

	}

	/**
	 * @param vendorSubTypeName
	 * @return
	 */
	public VendorIdentifier findByVendorSubtypeName(String vendorSubTypeName) {
		// TODO Auto-generated method stub
		return findByStringField(vendorSubTypeName, "vendorSubtypeName", VendorIdentifier.class);
	}
}
