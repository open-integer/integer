package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.client.widget.HvCheckBoxTreePanel;
import edu.harvard.integer.client.widget.HvCheckListPanel;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.topology.CategoryTypeEnum;
import edu.harvard.integer.common.topology.CriticalityEnum;

/**
 * The Class FilterPanel.
 */
public class FilterPanel extends DockPanel {
	
	private ListDataProvider<CategoryTypeEnum> categoryProvider = new ListDataProvider<CategoryTypeEnum>();
	private ListDataProvider<TechItem> technologyProvider = new ListDataProvider<TechItem>();
	private ListDataProvider<ID> providerProvider = new ListDataProvider<ID>();
	private ListDataProvider<CriticalityEnum> criticalityProvider = new ListDataProvider<CriticalityEnum>();
	private ListDataProvider<ID> locationProvider = new ListDataProvider<ID>();
	private ListDataProvider<ID> serviceProvider = new ListDataProvider<ID>();
	private ListDataProvider<TechItem> organizationProvider = new ListDataProvider<TechItem>();
	
	private SimplePanel titlePanel = new SimplePanel();
	private StackLayoutPanel selectionPanel = new StackLayoutPanel(Unit.EM);
	private SimplePanel actionPanel = new SimplePanel();
	private Button refreshButton = new Button("Refresh");
	
	/**
	 * Instantiates a new filter panel.
	 *
	 * @param unit the unit
	 */
	public FilterPanel() {
		titlePanel.add(new HTML("Narrow Your Selections"));
		titlePanel.setStyleName("titlePanel");
		
		actionPanel.add(refreshButton);
		
		selectionPanel.setSize("100%", "700px");
		
		add(titlePanel, DockPanel.NORTH);
		add(selectionPanel, DockPanel.CENTER);
		add(actionPanel, DockPanel.SOUTH);
	}
	
	public void update(Filter filter) {
		if (filter.getServices() != null && !filter.getServices().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(serviceProvider, filter.getServices()), "Business Services", 3);
		
		if (filter.getCategories() != null && !filter.getCategories().isEmpty())
			selectionPanel.add(new HvCheckListPanel<CategoryTypeEnum>(categoryProvider, filter.getCategories()), "Categories", 3);
		
		if (filter.getTechnologies() != null && !filter.getTechnologies().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(technologyProvider, filter.getTechnologies()), "Service Technologies", 3);
		
		if (filter.getProviders() != null && !filter.getProviders().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(providerProvider, filter.getProviders()), "Provider", 3);
		
		if (filter.getCriticalities() != null && !filter.getCriticalities().isEmpty())
			selectionPanel.add(new HvCheckListPanel<CriticalityEnum>(criticalityProvider, filter.getCriticalities()), "Criticality", 3);
		
		if (filter.getLocations() != null && !filter.getLocations().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(locationProvider, filter.getLocations()), "Location", 3);
		
		if (filter.getOrginizations() != null && !filter.getOrginizations().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(organizationProvider, filter.getOrginizations()), "Organization", 3);
	}

}
