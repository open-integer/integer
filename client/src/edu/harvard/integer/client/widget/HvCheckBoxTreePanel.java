package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.harvard.integer.client.ui.TechnologyDatabase.SubCategory;
import edu.harvard.integer.client.ui.TechnologyTreeViewModel;
import edu.harvard.integer.client.ui.TechnologyDatabase.Category;
import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;

public class HvCheckBoxTreePanel extends SimplePanel {
	
	private ListDataProvider<TechItem> techItemProvider;
	private ListDataProvider<SubCategory> subCategoryProvider;
	
	private final ProvidesKey<TechItem> KEY_PROVIDER = new ProvidesKey<TechItem>() {
		@Override
		public Object getKey(TechItem item) {
			return item == null ? null : item.getId();
		}
	};

	public HvCheckBoxTreePanel(ListDataProvider<TechItem> techItemProvider, List<FilterNode> list) {
		FilterNode rootNode = list.get(0);
		this.techItemProvider = techItemProvider;
		subCategoryProvider = new ListDataProvider<SubCategory>();

		generateProviderItems(rootNode.getItemId(), rootNode.getChildren());
		
		final MultiSelectionModel<TechItem> selectionModel = new MultiSelectionModel<TechItem>(KEY_PROVIDER);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						List<TechItem> selected = new ArrayList<TechItem>(selectionModel.getSelectedSet());
						Collections.sort(selected);
						
						List<FilterNode> techList = new ArrayList<FilterNode>();
						for (TechItem item : selected) {
							FilterNode node = new FilterNode();
							ID id = new ID((long)item.getId(), item.getName(), new IDType("Technology"));
							node.setItemId(id);
							techList.add(node);
						}
						
						// get Filter
						//filter.setTechnologies(techList);
						
						// call API
						Window.alert("Updating server ... under construction");
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		CellTree cellTree = new CellTree(new TechnologyTreeViewModel(techItemProvider, subCategoryProvider, selectionModel, rootNode.getChildren()), null, res);
		
		cellTree.setAnimationEnabled(true);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setHeight("550px");
		scrollPanel.add(cellTree);
		
		setWidget(scrollPanel);
		
	}
	
	public void generateProviderItems(ID parentNode, List<FilterNode> techNodeList) {
		List<TechItem> techItemList = techItemProvider.getList();
		List<SubCategory> subCategoryList = subCategoryProvider.getList();
		
		for (FilterNode node : techNodeList) {
			if (node.getChildren() == null || node.getChildren().isEmpty())
				techItemList.add(createTechItem(node.getItemId().getIdentifier(), parentNode.getName(), node.getItemId().getName()));
			else {
				subCategoryList.add(new SubCategory(parentNode.getName(), node.getItemId().getName()));
				generateProviderItems(node.getItemId(), node.getChildren());
			}
		}
	}
	
	private static TechItem createTechItem(long id, String catName, String itemName) {
		Category category = new Category(catName);
		TechItem techItem = new TechItem((int)id, category, itemName);
		return techItem;
	}
	
}
