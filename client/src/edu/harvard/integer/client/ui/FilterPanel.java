package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.harvard.integer.client.model.LeaveItem;
import edu.harvard.integer.client.widget.HvCheckBoxTreePanel;
import edu.harvard.integer.client.widget.HvCheckListPanel;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.topology.CriticalityEnum;

/**
 * The Class FilterPanel represents a filter panel object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.DockPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class FilterPanel extends DockPanel {

	public static final int FILTER_PANEL_HEIGHT = SystemSplitViewPanel.CONTENT_HEIGHT;
	
	/** The category provider. */
	private ListDataProvider<LeaveItem> categoryProvider = new ListDataProvider<LeaveItem>();
	
	private ListDataProvider<ID> environmentProvider = new ListDataProvider<ID>();
	
	/** The technology provider. */
	private ListDataProvider<LeaveItem> technologyProvider = new ListDataProvider<LeaveItem>();
	
	/** The provider provider. */
	private ListDataProvider<ID> providerProvider = new ListDataProvider<ID>();
	
	/** The criticality provider. */
	private ListDataProvider<CriticalityEnum> criticalityProvider = new ListDataProvider<CriticalityEnum>();
	
	/** The location provider. */
	private ListDataProvider<LeaveItem> locationProvider = new ListDataProvider<LeaveItem>();
	
	/** The service provider. */
	private ListDataProvider<LeaveItem> serviceProvider = new ListDataProvider<LeaveItem>();
	
	/** The technology link provider. */
	private ListDataProvider<LeaveItem> technologyLinkProvider = new ListDataProvider<LeaveItem>();
	
	/** The organization provider. */
	private ListDataProvider<LeaveItem> organizationProvider = new ListDataProvider<LeaveItem>();
	
	/** The title panel. */
	private SimplePanel titlePanel = new SimplePanel();
	
	/** The selection panel. */
	private DecoratedStackPanel selectionPanel = new DecoratedStackPanel();
	
	private ScrollPanel scrollPanel = new ScrollPanel(this);
	
	/** The action panel. */
	private SimplePanel actionPanel = new SimplePanel();
	
	/** The refresh button. */
	private Button refreshButton = new Button("Refresh");
	
	/**
	 * Create a new FilterPanel.
	 */
	public FilterPanel() {
		titlePanel.add(new HTML("Narrow Your Selections"));
		titlePanel.setStyleName("titlePanel");
		
		actionPanel.add(refreshButton);
		
		selectionPanel.addStyleName("filterPanel");
		scrollPanel.setSize("100%", FILTER_PANEL_HEIGHT+"px");
		
		add(titlePanel, DockPanel.NORTH);
		add(actionPanel, DockPanel.SOUTH);
		add(selectionPanel, DockPanel.CENTER);
		
		setSize("100%", "100%");
	}
	
	
	public DecoratedStackPanel getSelectionPanel() {
		return selectionPanel;
	}
	
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	public void resetHeight(int height) {
		setHeight((height-5)+"px");
		selectionPanel.setHeight((height-70)+"px");
		scrollPanel.setHeight(height+"px");
	}

	/**
	 * Update method will refresh the filter panel with the given Filter object.
	 *
	 * @param filter the filter
	 */
	public void update(Filter filter) {
		if (filter.getServices() != null && !filter.getServices().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(serviceProvider, filter.getServices()), "Business Services");
		
		if (filter.getCategories() != null && !filter.getCategories().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(categoryProvider, filter.getCategories()), "Categories");
		
		if (filter.getEnvironmentLevel() != null && !filter.getEnvironmentLevel().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(environmentProvider, filter.getEnvironmentLevel()), "Environment");
		
		if (filter.getTechnologies() != null && !filter.getTechnologies().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(technologyProvider, filter.getTechnologies()), "Service Technologies");
		
		if (filter.getProviders() != null && !filter.getProviders().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(providerProvider, filter.getProviders()), "Provider");
		
		if (filter.getCriticalities() != null && !filter.getCriticalities().isEmpty())
			selectionPanel.add(new HvCheckListPanel<CriticalityEnum>(criticalityProvider, filter.getCriticalities()), "Criticality");
		
		if (filter.getLocations() != null && !filter.getLocations().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(locationProvider, filter.getLocations()), "Location");
			//selectionPanel.add(new HvCheckListPanel<ID>(locationProvider, filter.getLocations()), "Location", 3);
		
		if (filter.getLinkTechnologies() != null && !filter.getLinkTechnologies().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(technologyLinkProvider, filter.getLinkTechnologies()), "Technology Links");
		
		if (filter.getOrginizations() != null && !filter.getOrginizations().isEmpty())
			selectionPanel.add(new HvCheckBoxTreePanel(organizationProvider, filter.getOrginizations()), "Organization");
	}

}
