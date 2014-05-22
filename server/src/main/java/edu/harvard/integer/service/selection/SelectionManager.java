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

package edu.harvard.integer.service.selection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.selection.SelectionDAO;

/**
 * @author David Taylor
 * 
 *         A selection manager handles individual requests from users of the
 *         Integer system (as in the case of reporting) to create, delete,
 *         modify and show selections
 */
@Stateless
public class SelectionManager extends BaseManager implements SelectionManagerLocalInterface, SelectionManagerRemoteInterface {

	@Inject
	private PersistenceManagerInterface persistenceManager;
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#updateSelection(edu.harvard.integer.common.selection.Selection)
	 */
	@Override
	public Selection updateSelection(Selection selection) throws IntegerException {
		SelectionDAO selectionDAO = persistenceManager.getSelectionDAO();
		
		return selectionDAO.update(selection);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getAllSeletions()
	 */
	@Override
	public Selection[] getAllSeletions() throws IntegerException {
		SelectionDAO selectionDAO = persistenceManager.getSelectionDAO();
		
		return selectionDAO.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getSelectionById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Selection getSelectionById(ID selectionId) throws IntegerException {
		SelectionDAO selectionDAO = persistenceManager.getSelectionDAO();
		
		return selectionDAO.findById(selectionId);
	}
}
