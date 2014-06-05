package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.client.widget.HvCheckListPanel;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.topology.CriticalityEnum;

/**
 * The Class FilterPanel.
 */
public class FilterPanel extends StackLayoutPanel {
	
	private Filter filter;
	
	private ListDataProvider<ID> providerProvider = new ListDataProvider<ID>();
	private ListDataProvider<CriticalityEnum> criticalityProvider = new ListDataProvider<CriticalityEnum>();
	private ListDataProvider<ID> locationProvider = new ListDataProvider<ID>();
	private ListDataProvider<ID> serviceProvider = new ListDataProvider<ID>();
	
	/**
	 * Instantiates a new filter panel.
	 *
	 * @param unit the unit
	 */
	public FilterPanel() {
		super(Unit.EM);	
	}
	
	public void update(Filter filter) {
		this.filter = filter;
		
		add(createTechnologyFilterPanel(filter.getTechnologies()), "Technology", 3);
		add(new HvCheckListPanel<ID>(providerProvider, filter.getProviders()), "Provider", 3);
		add(new HvCheckListPanel<CriticalityEnum>(criticalityProvider, filter.getCriticalities()), "Criticality", 3);
		add(new HvCheckListPanel<ID>(locationProvider, filter.getLocations()), "Location", 3);
		add(new HvCheckListPanel<ID>(serviceProvider, filter.getServices()), "Service", 3);
		add(getOrganizationFilterPanel(filter), "Organization", 3);
	}

	/** The technology filter panel. */
	private ScrollPanel technologyFilterPanel;
	
	/** The technology cell tree. */
	private CellTree technologyCellTree;
	
	/**
	 * Creates the technology filter panel.
	 *
	 * @return the widget
	 */
	private Widget createTechnologyFilterPanel(List<FilterNode> list) {
		if (technologyFilterPanel != null)
			return technologyFilterPanel;
		
		
		ID rootId = new ID(1L, "Technology", new IDType("Technology"));
		TechnologyDatabase.get().clear();
		TechnologyDatabase.get().generateTechnologyItems(rootId, list);
		
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
						filter.setTechnologies(techList);
						
						// call API
						Window.alert("Updating server ... under construction");
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		technologyCellTree = new CellTree(new TechnologyTreeViewModel(selectionModel, list), null, res);
		
		technologyCellTree.setAnimationEnabled(true);
		
		technologyFilterPanel = new ScrollPanel();
		technologyFilterPanel.add(technologyCellTree);
		return technologyFilterPanel;

	}
	
	/** The organization filter panel. */
	private VerticalPanel organizationFilterPanel;
	
	/**
	 * Gets the organization filter panel.
	 *
	 * @return the organization filter panel
	 */
	private Widget getOrganizationFilterPanel(Filter filter) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<FilterNode> ids = filter.getOrginizations();
//		for (FilterNode id : ids) {
//			filtersPanel.add(new CheckBox(id.getName()));
//		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/** The organization cell tree. */
	private CellTree organizationCellTree;
	
	/**
	 * Gets the organization cell tree.
	 *
	 * @return the organization cell tree
	 */
	private CellTree getOrganizationCellTree() {
		if (organizationCellTree == null) {
		      TreeViewModel model = new CustomTreeModel();
		      organizationCellTree = new CellTree(model, "Item 1");

		    }
		    return organizationCellTree;
	}
	
	/**
	 * The Class CustomTreeModel.
	 */
	private static class CustomTreeModel implements TreeViewModel {

		/* (non-Javadoc)
		 * @see com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
		 */
		@Override
	    public <T> NodeInfo<?> getNodeInfo(T value) {

	      // Create some data in a data provider. Use the parent value as a prefix for the next level.
	      ListDataProvider<String> dataProvider = new ListDataProvider<String>();
	      dataProvider.getList().add("Harvard University");
	      dataProvider.getList().add("Northeastern University");
	      dataProvider.getList().add("Boston University");

	      // Return a node info that pairs the data with a cell.
	      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	    }

		/* (non-Javadoc)
		 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
		 */
		@Override
	    public boolean isLeaf(Object value) {
	      // The maximum length of a value is ten characters.
	      return value.toString().length() > 20;
	    }
	  }

}
