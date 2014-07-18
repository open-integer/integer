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

package edu.harvard.integer.service.persistance.dao.topology.vendortemplate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.discovery.VendorContainmentSelector;
import edu.harvard.integer.common.discovery.VendorSignature;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * The DAO is responsible for persisting the VendorContainmentSelector. All
 * queries will be done in this class.
 * 
 * @author David Taylor
 * 
 */
public class VendorContainmentSelectorDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public VendorContainmentSelectorDAO(EntityManager entityManger,
			Logger logger) {
		super(entityManger, logger, VendorContainmentSelector.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard
	 * .integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {

		VendorContainmentSelector selector = (VendorContainmentSelector) entity;
		
		if (selector.getSignatures() != null) {
			VendorSignatureDAO dao = new VendorSignatureDAO(getEntityManager(), getLogger());

			List<VendorSignature> dbSignatures = new ArrayList<VendorSignature>();
			for (VendorSignature signature : selector.getSignatures()) {
				dbSignatures.add(dao.update(signature));
			}

			selector.setSignatures(dbSignatures);
		}
		
		super.preSave(entity);
	}

	/**
	 * @param selector
	 * @return
	 * @throws IntegerException
	 */
	@SuppressWarnings({ "rawtypes" })
	public VendorContainmentSelector[] findBySelector(
			VendorContainmentSelector selector) throws IntegerException {

		StringBuffer queryBuffer = new StringBuffer(); 
				
		queryBuffer.append("Select vc.* from VendorContainmentSelector vc ").append('\n');
		
		if (selector.getSignatures() != null) {
			int i = 0;	
			for (@SuppressWarnings("unused") VendorSignature signature : selector.getSignatures()) {

				queryBuffer.append(" join VendorContainmentSelector_VendorSignature vcss").append(i);
				queryBuffer.append(" on (vcss").append(i).append(".VendorContainmentSelector_identifier = vc.identifier)").append('\n');

				queryBuffer.append(" join VendorSignature vs").append(i).append(" on (vs").append(i).append(".identifier = vcss").append(i).append(".signatures_identifier)").append('\n');

				queryBuffer.append(" join SignatureValueOperator vco").append(i);
				queryBuffer.append(" on (vco").append(i).append(".identifier = vs").append(i).append(".valueOperator_identifier)").append('\n');

				i++;
			}

			boolean addedOne = false;
			i = 0;
			for (@SuppressWarnings("unused") VendorSignature signature : selector.getSignatures()) {
				if (addedOne)
					queryBuffer.append(" and ");
				else {
					queryBuffer.append(" where ");
					addedOne = true;
				}

				queryBuffer.append("lower(vco").append(i).append(".value) = :value").append(i++);
			}
		}
		
		getLogger().info("findBySelector query " + queryBuffer.toString());
		
		Query nativeQuery = getEntityManager().createNativeQuery(queryBuffer.toString(), VendorContainmentSelector.class);
		
		if (selector.getSignatures() != null) {
			int i = 0;
			for (VendorSignature signature : selector.getSignatures()) {
				getLogger().info("value" + i + " = " + signature.getValueOperator().getValue());
				nativeQuery.setParameter("value" + i++, signature.getValueOperator().getValue().toLowerCase());
			}
		}
		
		List results = nativeQuery.getResultList();
		List<VendorContainmentSelector> selectors = new ArrayList<VendorContainmentSelector>();
		getLogger().info("Got " + results.size() + " VendorContainmentSelector's");
		
		for (Object object : results) {
			if (object instanceof VendorContainmentSelector)
				selectors.add((VendorContainmentSelector) object);
			else if (object.getClass().isArray()) {
				for (Object obj : (Object[]) object) {
					if (obj instanceof VendorContainmentSelector)
						selectors.add((VendorContainmentSelector) obj);
					else
						getLogger().error("Did not get VendorContainmentSelector!! 1 Got " + obj.getClass().getName());
				}
			}
			else
				getLogger().error("Did not get VendorContainmentSelector!! Got " + object.getClass().getName());
		}
		
		return (VendorContainmentSelector[]) selectors
				.toArray(new VendorContainmentSelector[selectors.size()]);
	
	}

}
