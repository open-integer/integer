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
import edu.harvard.integer.client.model.RootNodeItem;
import edu.harvard.integer.client.model.ChildNodeItem;
import edu.harvard.integer.client.model.LeaveItem;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.technology.Technology;

/**
 * The Class HvCheckBoxTreePanel.
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
	 * Instantiates a new hv check box tree panel.
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
	 * @param techNodeList the tech node list
	 */
	public void generateProviderItems(ID parentNode, List<FilterNode> techNodeList) {
		List<LeaveItem> techItemList = techItemProvider.getList();
		List<ChildNodeItem> subCategoryList = subCategoryProvider.getList();
		
		for (FilterNode node : techNodeList) {
			if (node.getChildren() == null || node.getChildren().isEmpty())
				techItemList.add(createTechItem(node.getItemId().getIdentifier(), parentNode.getName(), node.getItemId().getName()));
			else {
				subCategoryList.add(new ChildNodeItem(parentNode.getName(), node.getItemId().getName()));
				generateProviderItems(node.getItemId(), node.getChildren());
			}
		}
	}
	
	/**
	 * Creates the tech item.
	 *
	 * @param id the id
	 * @param catName the cat name
	 * @param itemName the item name
	 * @return the tech item
	 */
	private static LeaveItem createTechItem(long id, String catName, String itemName) {
		RootNodeItem category = new RootNodeItem(catName);
		LeaveItem techItem = new LeaveItem((int)id, category, itemName);
		return techItem;
	}
	
}
