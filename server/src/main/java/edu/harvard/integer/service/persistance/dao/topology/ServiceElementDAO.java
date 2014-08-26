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

package edu.harvard.integer.service.persistance.dao.topology;

import javax.persistence.EntityManager;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.managementobject.ManagementObjectValue;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementFields;

/**
 * The DAO is responsible for persisting the ServiceElement. All queries will be
 * done in this class.
 * 
 * @author David Taylor
 * 
 */
public class ServiceElementDAO extends ServiceElementFieldsDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public ServiceElementDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, ServiceElement.class);
	}

	private ServiceElement[] castToServiceElement(ServiceElementFields[] serviceElementFields) {
		ServiceElement[] serviceElements = new ServiceElement[serviceElementFields.length];
		
		for (int i = 0; i < serviceElementFields.length; i++) {
			serviceElements[i] = (ServiceElement) serviceElementFields[i];
		}
		
		return serviceElements;
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.topology.ServiceElementFieldsDAO#findTopLevelServiceElements()
	 */
	@Override
	public ServiceElement[] findTopLevelServiceElements() {
		
		return castToServiceElement(super.findTopLevelServiceElements());
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.topology.ServiceElementFieldsDAO#findByParentId(edu.harvard.integer.common.ID)
	 */
	@Override
	public ServiceElementFields[] findByParentId(ID parent) {
		
		return castToServiceElement(super.findByParentId(parent));
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.topology.ServiceElementFieldsDAO#findByParentIdAndName(edu.harvard.integer.common.ID, java.lang.String)
	 */
	@Override
	public ServiceElementFields[] findByParentIdAndName(ID parent, String name) {
		
		return castToServiceElement(super.findByParentIdAndName(parent, name));
	}

	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.topology.ServiceElementFieldsDAO#findBySelection(edu.harvard.integer.common.selection.Selection)
	 */
	@Override
	public ServiceElementFields[] findBySelection(Selection selection) {
		
		return castToServiceElement(super.findBySelection(selection));
	}


}
