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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Level;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.TestUtil;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.selection.Selection;

/**
 * @author David Taylor
 * 
 */
@RunWith(Arquillian.class)
public class SelectionManagerTest {

	@Inject
	private SelectionManagerInterface selectionManager;

	private Logger logger = LoggerFactory.getLogger(SelectionManagerTest.class);

	@Deployment
	public static Archive<?> createTestArchive() {
		return TestUtil
				.createTestArchive("ServiceElementDiscoveryManagerTest.war");
	}

	@Before
	public void setUpLogger() {
		org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
	}

	@Test
	public void updateSelection() {

		try {
			selectionManager.updateSelection(createNewSelection());
		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getAllSelections() {
		updateSelection();

		try {
			Selection[] selections = selectionManager.getAllSeletions();

			assert (selections != null);

			assert (selections.length > 0);

		} catch (IntegerException e) {

			e.printStackTrace();
			fail(e.toString());
		}
	}

	private Selection createNewSelection() {
		Selection selection = null;

		selection = new Selection();
		selection.setCreated(new Date());
		selection.setDescription("My Description");
		selection.setLastModifyed(new Date());

		Filter filter = new Filter();
		filter.setCreated(new Date());
		filter.setServices(TestUtil.createIdList(5, ID.class, "Service"));

		filter.setTechnologies(getTechnologyTree());

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(filter);
		selection.setFilters(filters);

		logger.info("Create Selection " + selection.getFilters().size()
				+ " TopLevel Technology's");
		for (FilterNode filterNode : selection.getFilters().get(0)
				.getTechnologies()) {
			showFilterNode("TopLevel", filterNode);
		}

		return selection;
	}

	private List<FilterNode> getTechnologyTree() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();

		root.setItemId(new ID(Long.valueOf(1), "Routers", new IDType(
				"Technology")));
		root.setChildren(getRoutersLevel1());

		nodes.add(root);

		root = new FilterNode();

		root.setItemId(new ID(Long.valueOf(1), "Routers", new IDType(
				"Technology")));
		root.setChildren(getServersLevel1());
		nodes.add(root);

		return nodes;
	}

	private List<FilterNode> getRoutersLevel1() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(1), "Router1", new IDType(
				"Technology")));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(2), "Router2", new IDType(
				"Technology")));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(3), "Router3", new IDType(
				"Technology")));
		nodes.add(root);

		return nodes;
	}

	private List<FilterNode> getServersLevel1() {
		List<FilterNode> nodes = new ArrayList<FilterNode>();

		FilterNode root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(1), "Server1", new IDType(
				"Technology")));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(2), "Server2", new IDType(
				"Technology")));
		nodes.add(root);

		root = new FilterNode();
		root.setItemId(new ID(Long.valueOf(3), "Server3", new IDType(
				"Technology")));
		nodes.add(root);

		return nodes;
	}

	@Test
	public void getBlankSelection() {

		updateSelection();

		try {

			Selection blankSelection = selectionManager.getBlankSelection();

			assert (blankSelection != null);
			assert (blankSelection.getFilters() != null);
			assert (blankSelection.getFilters().size() > 0);

			Filter filter = blankSelection.getFilters().get(0);
			List<FilterNode> technologies = filter.getTechnologies();
			assert (technologies != null);
			//assert (technologies.size() > 0);

			logger.info("Found " + technologies.size()
					+ " TopLevel Technology's");
			for (FilterNode filterNode : technologies) {
				showFilterNode("TopLevel", filterNode);
			}

		} catch (IntegerException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	private void showFilterNode(String name, FilterNode node) {
		logger.info(name + " Node " + node.getName() + " ID "
				+ node.getItemId().toDebugString());

		if (node.getChildren() != null) {
			for (FilterNode filterNode : node.getChildren()) {
				showFilterNode(name + "::" + node.getName(), filterNode);
			}
		}
	}
}
