package edu.harvard.integer.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import edu.harvard.integer.client.model.LeaveItem;
import edu.harvard.integer.client.resources.Resources;
import edu.harvard.integer.client.widget.HvCheckBoxTree;
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
public class FilterPanel extends VerticalPanel {

	/** The Constant FILTER_PANEL_WIDTH. */
	public static final int FILTER_PANEL_WIDTH = 350;
	
	/** The Constant FILTER_PANEL_HEIGHT. */
	public static final int FILTER_PANEL_HEIGHT = SystemSplitViewPanel.CONTENT_HEIGHT-20;
	
	public static final int FILTER_ITEM_HEIGHT = 300;
	
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
	private HorizontalPanel titleToolBar = new HorizontalPanel();
	
	/** The selection panel. */
	private DecoratedStackPanel selectionPanel = new DecoratedStackPanel();
	
	/** The scroll panel. */
	private ScrollPanel scrollPanel = new ScrollPanel(this);
	
	private Image refreshImage = new Image(Resources.ICONS.refresh());
	
	/** The refresh button. */
	private PushButton refreshButton = new PushButton(refreshImage);
	
	private Label titleLabel = new Label("Filter Selections");
	
	private HvCheckBoxTree businessTree;
	private HvCheckBoxTree categoryTree;
	
	/**
	 * Create a new FilterPanel.
	 */
	public FilterPanel() {
		titleToolBar.add(titleLabel);
		titleToolBar.addStyleName("titleToolBar");

		refreshButton.setSize("16px", "16px");
		selectionPanel.addStyleName("filterPanel");

		int selectionHeight = Window.getClientHeight() - 128;
		selectionPanel.setSize("100%", selectionHeight+"px");
		scrollPanel.setSize("100%", selectionHeight+"px");
		
		add(titleToolBar);
		add(selectionPanel);
		
		// fill up all spaces
		setSize("100%", "100%");
	}
	
	
	public DecoratedStackPanel getSelectionPanel() {
		return selectionPanel;
	}
	
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	/**
	 * resize FilterPanel size by given width and height
	 */
	public void resetSize(int width, int height) {
		
		setHeight(height+"px");
		
		selectionPanel.setHeight(height-120+"px");
		scrollPanel.setHeight(height+"px");
		
		if (businessTree != null)
			businessTree.setTreeGridSize(width, height-15);
		
		if (categoryTree != null)
			categoryTree.setTreeGridSize(width, height-15);
	}

	/**
	 * Update method will refresh the filter panel with the given Filter object.
	 *
	 * @param filter the filter
	 */
	public void update(Filter filter) {
		if (filter.getServices() != null && !filter.getServices().isEmpty()) {
			businessTree = new HvCheckBoxTree(filter.getServices());
			selectionPanel.add(businessTree, "Business Services");
		}
		
		if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
			categoryTree = new HvCheckBoxTree(filter.getCategories());
			selectionPanel.add(categoryTree, "Categories");
		}
		
		if (filter.getEnvironmentLevel() != null && !filter.getEnvironmentLevel().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(environmentProvider, filter.getEnvironmentLevel()), "Environment");
		
		if (filter.getTechnologies() != null && !filter.getTechnologies().isEmpty())
			selectionPanel.add(new HvCheckBoxTree(filter.getTechnologies()), "Service Technologies");
		
		if (filter.getProviders() != null && !filter.getProviders().isEmpty())
			selectionPanel.add(new HvCheckListPanel<ID>(providerProvider, filter.getProviders()), "Provider");
		
		if (filter.getCriticalities() != null && !filter.getCriticalities().isEmpty())
			selectionPanel.add(new HvCheckListPanel<CriticalityEnum>(criticalityProvider, filter.getCriticalities()), "Criticality");
		
		if (filter.getLocations() != null && !filter.getLocations().isEmpty())
			selectionPanel.add(new HvCheckBoxTree(filter.getLocations()), "Location");
			//selectionPanel.add(new HvCheckListPanel<ID>(locationProvider, filter.getLocations()), "Location", 3);
		
		if (filter.getLinkTechnologies() != null && !filter.getLinkTechnologies().isEmpty())
			selectionPanel.add(new HvCheckBoxTree(filter.getLinkTechnologies()), "Technology Links");
		
		if (filter.getOrginizations() != null && !filter.getOrginizations().isEmpty())
			selectionPanel.add(new HvCheckBoxTree(filter.getOrginizations()), "Organization");
	}

}
