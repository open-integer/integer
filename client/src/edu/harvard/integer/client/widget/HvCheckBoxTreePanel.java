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

import edu.harvard.integer.client.ui.TechnologyDatabase.SubCategory;
import edu.harvard.integer.client.ui.TechnologyTreeViewModel;
import edu.harvard.integer.client.ui.TechnologyDatabase.Category;
import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.technology.Technology;

/**
 * The Class HvCheckBoxTreePanel.
 */
public class HvCheckBoxTreePanel extends SimplePanel {
	
	/** The tech item provider. */
	private ListDataProvider<TechItem> techItemProvider;
	
	/** The sub category provider. */
	private ListDataProvider<SubCategory> subCategoryProvider;
	
	/** The key provider. */
	private final ProvidesKey<TechItem> KEY_PROVIDER = new ProvidesKey<TechItem>() {
		@Override
		public Object getKey(TechItem item) {
			return item == null ? null : item.getId();
		}
	};

	/**
	 * Instantiates a new hv check box tree panel.
	 *
	 * @param techItemProvider the tech item provider
	 * @param list the list
	 */
	public HvCheckBoxTreePanel(ListDataProvider<TechItem> techItemProvider, List<FilterNode> list) {
		this.techItemProvider = techItemProvider;
		subCategoryProvider = new ListDataProvider<SubCategory>();

		ID rootId = new ID(1L, "Technology", new IDType(Technology.class.getName()));
		generateProviderItems(rootId, list);
		
		final MultiSelectionModel<TechItem> selectionModel = new MultiSelectionModel<TechItem>(KEY_PROVIDER);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						List<TechItem> selected = new ArrayList<TechItem>(selectionModel.getSelectedSet());
						Collections.sort(selected);
						
						List<FilterNode> techList = new ArrayList<FilterNode>();
						for (TechItem item : selected) {
							FilterNode node = new FilterNode();
							ID id = new ID((long)item.getId(), item.getName(), new IDType(Technology.class.getName()));
							node.setItemId(id);
							techList.add(node);
						}
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		CellTree cellTree = new CellTree(new TechnologyTreeViewModel(techItemProvider, subCategoryProvider, selectionModel, list), null, res);
		
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
	
	/**
	 * Creates the tech item.
	 *
	 * @param id the id
	 * @param catName the cat name
	 * @param itemName the item name
	 * @return the tech item
	 */
	private static TechItem createTechItem(long id, String catName, String itemName) {
		Category category = new Category(catName);
		TechItem techItem = new TechItem((int)id, category, itemName);
		return techItem;
	}
	
}
