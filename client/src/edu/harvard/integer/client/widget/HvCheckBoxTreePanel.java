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

import edu.harvard.integer.client.ui.TechnologyDatabase;
import edu.harvard.integer.client.ui.TechnologyTreeViewModel;
import edu.harvard.integer.client.ui.TechnologyDatabase.Category;
import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;

public class HvCheckBoxTreePanel<D> extends SimplePanel {
	
	ListDataProvider<TechItem> dataProvider;
	
	public final ProvidesKey<D> KEY_PROVIDER = new ProvidesKey<D>() {
		@Override
		public Object getKey(D d) {
			Object key = null;
			
			if (d != null && d instanceof ID)
				key = ((ID)d).getIdentifier();
			else if (d != null && d instanceof Enum)
				key = ((Enum<?>)d).name();
			
			return key;
		}
	};

	public HvCheckBoxTreePanel(ListDataProvider<TechItem> dataProvider, List<FilterNode> list) {
		this.dataProvider = dataProvider;
		ID rootId = new ID(1L, "Technology", new IDType("Technology"));
		generateTechnologyItems(rootId, list);
		
		final MultiSelectionModel<TechItem> selectionModel = new MultiSelectionModel<TechItem>(
				TechnologyDatabase.TechItem.KEY_PROVIDER);
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
		
		CellTree cellTree = new CellTree(new TechnologyTreeViewModel(selectionModel, list), null, res);
		
		cellTree.setAnimationEnabled(true);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.add(cellTree);
		
		setWidget(scrollPanel);
		
	}
	
	public void generateTechnologyItems(ID parentNode, List<FilterNode> techNodeList) {
		List<TechItem> list = dataProvider.getList();
		
		for (FilterNode node : techNodeList) {
			list.add(createTechItem(node.getItemId().getIdentifier(), parentNode.getName(), node.getItemId().getName()));
			if (node.getChildren() != null)
				generateTechnologyItems(node.getItemId(), node.getChildren());
		}
	}
	
	private static TechItem createTechItem(long id, String catName, String itemName) {
		Category category = new Category(catName);
		TechItem techItem = new TechItem((int)id, category, itemName);
		return techItem;
	}
	
}
