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

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.Layer;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * A selection manager handles individual requests from users of the Integer
 * system (as in the case of reporting) to create, delete, modify and show
 * selections
 * 
 * @author David Taylor
 *
 */
public interface SelectionManagerInterface extends BaseManagerInterface {

	/**
	 * Update/save the selection. 
	 * @param selection. The Selection to save
	 * @return Selection. The updated seletion. 
	 * @throws IntegerException
	 */
	Selection updateSelection(Selection selection) throws IntegerException;

	/**
	 * Get all selections found in the database.
	 * @return Selection[] of all selections in the database.
	 * @throws IntegerException
	 */
	Selection[] getAllSeletions() throws IntegerException;

	/**
	 * Get the selection with the given ID.
	 * @param selectionId. ID of the selectiont to get.
	 * @return Selection for the given ID.
	 * @throws IntegerException
	 */
	Selection getSelectionById(ID selectionId) throws IntegerException;

	/**
	 * Update/save the filter. 
	 * @param filter. Filter to save/update
	 * @return Filter. The updated filter.
	 * @throws IntegerException
	 */
	Filter updateFilter(Filter filter) throws IntegerException;

	/**
	 * Get all the Filter's in the database.
	 * @return Filter[] of all filters found in the database.
	 * @throws IntegerException
	 */
	Filter[] getAllFilters() throws IntegerException;

	/**
	 * Get the filter with the given ID.
	 * @param filterId. ID of the filter to get.
	 * @return Filter with the given ID.
	 * @throws IntegerException
	 */
	Filter getFilterById(ID filterId) throws IntegerException;

	/**
	 * Update/save the Layer in the database.
	 * @param layer. Layer to update
	 * @return Layer. The updated layer.
	 * @throws IntegerException
	 */
	Layer updateLayer(Layer layer) throws IntegerException;

	/**
	 * Get all Layer's in the database.
	 * @return Layer[] of all Layer's found in the database.
	 * @throws IntegerException
	 */
	Layer[] getAllLayers() throws IntegerException;

	/**
	 * Get the Layer with the given ID.
	 * @param layerId. ID of the Layer to get.
	 * @return Layer with the given ID.
	 * @throws IntegerException
	 */
	Layer getLayerById(ID layerId) throws IntegerException;

	/**
	 * Create a selection with the Technology, Orginization and Link Technologies filled in
	 * with the complete tree.
	 * 
	 * @return A "Blank" Selection.
	 * @throws IntegerException
	 */
	Selection getBlankSelection() throws IntegerException;

}
