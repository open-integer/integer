package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.harvard.integer.client.model.CheckBoxTreeViewModel;
import edu.harvard.integer.client.model.ChildNodeItem;
import edu.harvard.integer.client.model.LeaveItem;
import edu.harvard.integer.client.model.RootNodeItem;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.technology.Technology;

/**
 * The Class HvCheckBoxTreePanel represents a panel showing a list of CheckBox in tree.
 * This is a subclass class extended from com.google.gwt.user.client.ui.SimplePanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class HvCheckBoxTreePanel extends SimplePanel {
	
	/** The tech item provider. */
	private ListDataProvider<LeaveItem> techItemProvider;
	
	/** The sub category provider. */
	private ListDataProvider<ChildNodeItem> subCategoryProvider;
	
	/** The key provider. */
	private final ProvidesKey<LeaveItem> KEY_PROVIDER = new ProvidesKey<LeaveItem>() {
		@Override
		public Object getKey(LeaveItem item) {
			return item == null ? null : item.getId();
		}
	};

	/**
	 * Creates a new HvCheckBoxTreePanel instance.
	 *
	 * @param techItemProvider the tech item provider
	 * @param list the list
	 */
	public HvCheckBoxTreePanel(ListDataProvider<LeaveItem> techItemProvider, List<FilterNode> list) {
		this.techItemProvider = techItemProvider;
		subCategoryProvider = new ListDataProvider<ChildNodeItem>();

		ID rootId = new ID(1L, "Technology", new IDType(Technology.class.getName()));
		generateProviderItems(rootId, list);
		
		final MultiSelectionModel<LeaveItem> selectionModel = new MultiSelectionModel<LeaveItem>(KEY_PROVIDER);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						List<LeaveItem> selected = new ArrayList<LeaveItem>(selectionModel.getSelectedSet());
						Collections.sort(selected);
						
						List<FilterNode> techList = new ArrayList<FilterNode>();
						for (LeaveItem item : selected) {
							FilterNode node = new FilterNode();
							ID id = new ID((long)item.getId(), item.getName(), new IDType(Technology.class.getName()));
							node.setItemId(id);
							techList.add(node);
						}
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		CellTree cellTree = new CellTree(new CheckBoxTreeViewModel(techItemProvider, subCategoryProvider, selectionModel, list), null, res);
		
		cellTree.setAnimationEnabled(true);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setHeight("550px");
		scrollPanel.add(cellTree);
		
		setWidget(scrollPanel);
		
	}
	
	/**
	 * Generate provider items.
	 *
	 * @param parentNode the parent node
	 * @param childNodeList the child node list
	 */
	public void generateProviderItems(ID parentNode, List<FilterNode> childNodeList) {
		List<LeaveItem> techItemList = techItemProvider.getList();
		List<ChildNodeItem> subCategoryList = subCategoryProvider.getList();
		
		for (FilterNode node : childNodeList) {
			if (node.getChildren() == null) // || node.getChildren().isEmpty())
				techItemList.add(createLeaveItem(node.getItemId().getIdentifier(), parentNode.getName(), node.getItemId().getName()));
			else {
				ChildNodeItem childNode = new ChildNodeItem(parentNode.getName(), node.getItemId().getName());
				childNode.setNumChildren(node.getChildren().size());
				subCategoryList.add(childNode);
				generateProviderItems(node.getItemId(), node.getChildren());
			}
		}
	}
	
	/**
	 * Creates the leave item.
	 *
	 * @param id the id
	 * @param parentNodeName the cat name
	 * @param leaveName the item name
	 * @return the tech item
	 */
	private static LeaveItem createLeaveItem(long id, String parentNodeName, String leaveName) {
		RootNodeItem parentNode = new RootNodeItem(parentNodeName);
		LeaveItem leaveItem = new LeaveItem((int)id, parentNode, leaveName);
		return leaveItem;
	}
	
}
