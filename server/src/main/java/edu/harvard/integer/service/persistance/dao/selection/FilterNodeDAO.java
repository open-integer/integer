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

package edu.harvard.integer.service.persistance.dao.selection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.service.persistance.dao.BaseDAO;

/**
 * The DAO is responsible for persisting the FilterNode. All
 * queries will be done in this class. 
 * 
 * @author David Taylor
 *
 */
public class FilterNodeDAO extends BaseDAO {

	/**
	 * @param entityManger
	 * @param logger
	 * @param clazz
	 */
	public FilterNodeDAO(EntityManager entityManger, Logger logger) {
		super(entityManger, logger, FilterNode.class);
		
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.persistance.dao.BaseDAO#preSave(edu.harvard.integer.common.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> void preSave(T entity)
			throws IntegerException {
		
		FilterNode filterNode = (FilterNode) entity;
		if (filterNode.getChildren() != null) {
			List<FilterNode> dbNodes = new ArrayList<FilterNode>();
			
			for (FilterNode node : filterNode.getChildren()) {
				dbNodes.add(update(node));
			}
			
			filterNode.setChildren(dbNodes);
		}
		
		super.preSave(entity);
	}
	
	public StringBuffer getWhere(FilterNode filterNode,  StringBuffer b) throws IntegerException {
		
		if (filterNode.getSelected().booleanValue()) {
			b = getWhereAll(filterNode, b);
		} else {
			if (filterNode.getChildren() != null) {
				for (FilterNode childNode : filterNode.getChildren()) {
					b = getWhere(childNode, b);
				}
			}
				
		}
		
		return b;
	}
	
	public StringBuffer getWhereAll(FilterNode filterNode, StringBuffer b) throws IntegerException {
		
		b.append(getMyWhere(filterNode, b));
		if (filterNode.getChildren() != null) {
			for (FilterNode childNode : filterNode.getChildren()) {
				b = getWhereAll(childNode, b);
			}
		}
		
		return b;
	}
	
	private StringBuffer getMyWhere(FilterNode filterNode, StringBuffer b) throws IntegerException {
		
		if (b.length() > 1) 
			b.append(",\n\t");
		
		b.append(getTableForID(filterNode.getItemId())).append(" ").append(getAlias(filterNode.getItemId()));
		
		return b;
	}

	/**
	 * @param itemId
	 * @return
	 */
	private String getAlias(ID itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param itemId
	 * @return
	 */
	private String getTableForID(ID itemId) {
		
		return null;
	}
}
