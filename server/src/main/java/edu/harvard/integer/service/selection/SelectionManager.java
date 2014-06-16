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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.selection.Layer;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.technology.Technology;
import edu.harvard.integer.service.BaseManager;
import edu.harvard.integer.service.distribution.ManagerTypeEnum;
import edu.harvard.integer.service.persistance.PersistenceManagerInterface;
import edu.harvard.integer.service.persistance.dao.selection.FilterDAO;
import edu.harvard.integer.service.persistance.dao.selection.LayerDAO;
import edu.harvard.integer.service.persistance.dao.selection.SelectionDAO;
import edu.harvard.integer.service.persistance.dao.technology.TechnologyDAO;

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
	
	public SelectionManager() {
		super(ManagerTypeEnum.SelectionManager);
	}
	
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
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getBlankSelection()
	 */
	@Override
	public Selection getBlankSelection() throws IntegerException {
		TechnologyDAO dao = persistenceManager.getTechnologyDAO();
		Technology[] technologies = dao.findTopLevel();
		
		List<FilterNode> nodes = new ArrayList<FilterNode>();
		
		// TODO: remove once real data is available.
		if (technologies == null || technologies.length == 0) {
			nodes.addAll(getTechnologyTree());
		} 
		
		for (Technology technology : technologies) {
			FilterNode node = new FilterNode();
			node.setItemId(technology.getID());
			node.setName(technology.getName());
			
			node.setChildren(findChildren(dao, technology.getID()));
			
			nodes.add(node);
		}
		
		Filter filter = new Filter();
		filter.setCreated(new Date());
		filter.setTechnologies(nodes);
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(filter);
		
		Selection selection = new Selection();
		selection.setFilters(filters);
		
		return selection;
	}
	

	private List<FilterNode> getTechnologyTree() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();

		root.setItemId(new ID(Long.valueOf(1), "Routers", new IDType(Technology.class)));
		root.setChildren(getRoutersLevel1());

		nodes.add(root);

		root = new FilterNode();

		root.setItemId(new ID(Long.valueOf(1), "Routers", new IDType(Technology.class)));
		root.setChildren(getServersLevel1());
		nodes.add(root);

		return nodes;
	}
	

	private List<FilterNode> getServersLevel1() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(1), "Server1", new IDType(Technology.class)));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(2), "Server2", new IDType(Technology.class)));
		nodes.add(root);

		root = new FilterNode();
		nodes.add(root);

		return nodes;
	}


	private List<FilterNode> getRoutersLevel1() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(1), "Router1", new IDType(Technology.class)));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(2), "Router2", new IDType(Technology.class)));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(3), "Router3", new IDType(Technology.class)));
		nodes.add(root);

		return nodes;
	}
	
	
	private List<FilterNode> findChildren(TechnologyDAO dao, ID parentId) throws IntegerException {
		Technology[] children = dao.findByParentId(parentId);
		
		List<FilterNode> nodes = new ArrayList<FilterNode>();
		for (Technology technology : children) {
			FilterNode node = new FilterNode();
			node.setItemId(technology.getID());
			node.setName(technology.getName());
			node.setChildren(findChildren(dao, technology.getID()));
			
			nodes.add(node);
		}
		
		return nodes;
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
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#updateFilter(edu.harvard.integer.common.selection.Filter)
	 */
	@Override
	public Filter updateFilter(Filter filter) throws IntegerException {
		FilterDAO dao = persistenceManager.getFilterDAO();
		
		return dao.update(filter);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getAllFilters()
	 */
	@Override
	public Filter[] getAllFilters() throws IntegerException {
		FilterDAO dao = persistenceManager.getFilterDAO();
		
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getFilterById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Filter getFilterById(ID filterId) throws IntegerException {
		FilterDAO dao = persistenceManager.getFilterDAO();
		
		return dao.findById(filterId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#updateLayer(edu.harvard.integer.common.selection.Layer)
	 */
	@Override
	public Layer updateLayer(Layer layer) throws IntegerException {
		LayerDAO dao = persistenceManager.getLayerDAO();
		
		return dao.update(layer);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getAllLayers()
	 */
	@Override
	public Layer[] getAllLayers() throws IntegerException {
		LayerDAO dao = persistenceManager.getLayerDAO();
		return dao.findAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.harvard.integer.service.selection.SelectionManagerInterface#getLayerById(edu.harvard.integer.common.ID)
	 */
	@Override
	public Layer getLayerById(ID layerId) throws IntegerException {
		LayerDAO dao = persistenceManager.getLayerDAO();
		
		return dao.findById(layerId);
	}
}
