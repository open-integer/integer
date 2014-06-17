package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
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
public class FilterPanel extends StackLayoutPanel {
	
	private ListDataProvider<CategoryTypeEnum> categoryProvider = new ListDataProvider<CategoryTypeEnum>();
	private ListDataProvider<TechItem> technologyProvider = new ListDataProvider<TechItem>();
	private ListDataProvider<ID> providerProvider = new ListDataProvider<ID>();
	private ListDataProvider<CriticalityEnum> criticalityProvider = new ListDataProvider<CriticalityEnum>();
	private ListDataProvider<ID> locationProvider = new ListDataProvider<ID>();
	private ListDataProvider<ID> serviceProvider = new ListDataProvider<ID>();
	private ListDataProvider<TechItem> organizationProvider = new ListDataProvider<TechItem>();
	
	/**
	 * Instantiates a new filter panel.
	 *
	 * @param unit the unit
	 */
	public FilterPanel() {
		super(Unit.EM);	
	}
	
	public void update(Filter filter) {
		if (filter.getServices() != null && !filter.getServices().isEmpty())
			add(new HvCheckListPanel<ID>(serviceProvider, filter.getServices()), "Business Services", 3);
		
		if (filter.getCategories() != null && !filter.getCategories().isEmpty())
			add(new HvCheckListPanel<CategoryTypeEnum>(categoryProvider, filter.getCategories()), "Categories", 3);
		
		if (filter.getTechnologies() != null && !filter.getTechnologies().isEmpty())
			add(new HvCheckBoxTreePanel(technologyProvider, filter.getTechnologies()), "Service Technologies", 3);
		
		if (filter.getProviders() != null && !filter.getProviders().isEmpty())
			add(new HvCheckListPanel<ID>(providerProvider, filter.getProviders()), "Provider", 3);
		
		if (filter.getCriticalities() != null && !filter.getCriticalities().isEmpty())
			add(new HvCheckListPanel<CriticalityEnum>(criticalityProvider, filter.getCriticalities()), "Criticality", 3);
		
		if (filter.getLocations() != null && !filter.getLocations().isEmpty())
			add(new HvCheckListPanel<ID>(locationProvider, filter.getLocations()), "Location", 3);
		
		if (filter.getOrginizations() != null && !filter.getOrginizations().isEmpty())
			add(new HvCheckBoxTreePanel(organizationProvider, filter.getOrginizations()), "Organization", 3);
	}

}
