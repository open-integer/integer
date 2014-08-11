package edu.harvard.integer.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.selection.Selection;
import edu.harvard.integer.common.topology.Network;

/**
 * The Class SystemSplitViewPanel represents a split panel object of System
 * This is a subclass class extended from com.google.gwt.user.client.ui.SplitLayoutPanel
 * It contains filterPanel at west side and eastPanel at east side
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class SystemSplitViewPanel extends SplitLayoutPanel {
	
	/** The Constant CONTENT_WIDTH. */
	public static final int CONTENT_WIDTH = 1200;
	
	/** The Constant CONTENT_HEIGHT. */
	public static final int CONTENT_HEIGHT = 800;
	
	/** The Constant WESTPANEL_WIDTH. */
	public static final int WESTPANEL_WIDTH = 250;

	/** The east panel. */
	//public static DockPanel eastPanel = null;
	private SplitLayoutPanel eastPanel = new SplitLayoutPanel(MainClient.SPLITTER_SIZE);
	
	/** The contained tree view. */
	//public static ContainedTreeView containedTreeView = null;

	/** The network panel. */
	private NetworkPanel networkPanel = new NetworkPanel();
	
	/** The tab panel. */
	private static TabLayoutPanel tabPanel = new TabLayoutPanel(2.5, Unit.EM);

	/**
	 * Instantiates a new system split view panel.
	 */
	public SystemSplitViewPanel() {
		super(MainClient.SPLITTER_SIZE);
        
		// clean up tabPanel
		tabPanel.clear();
		
        setSize("100%", MainClient.WINDOW_HEIGHT+"px");
        
        // Event View
		EventView eventView = createEventView();
		
		eastPanel.setSize("100%", "100%");

	    // tabPanel
		tabPanel.setSize("100%", "100%");
	    tabPanel.setAnimationDuration(500);
	    tabPanel.add(networkPanel, "Network Map");

		eastPanel.addSouth(eventView, EventView.EVENT_VIEW_HEIGHT);
		eastPanel.add(tabPanel);
		eastPanel.setWidgetToggleDisplayAllowed(eventView, true);
		eastPanel.setWidgetHidden(eventView, false);
		
		final FilterPanel filterPanel = new FilterPanel();
		filterPanel.setSize("100%", SystemSplitViewPanel.CONTENT_HEIGHT+"px");
		
		addWest(filterPanel, WESTPANEL_WIDTH);
		setWidgetToggleDisplayAllowed(filterPanel, true);
		
		add(eastPanel);
		
		MainClient.integerService.getBlankSelection(new AsyncCallback<Selection>() {

			@Override
			public void onFailure(Throwable caught) {
				MainClient.statusPanel.showAlert("Failed to receive default filter and selection.");
			}

			@Override
			public void onSuccess(Selection result) {
				filterPanel.update(result.getFilters().get(0));			
			}
			
		});
		
	}

	
	/**
	 * Creates the filter view.
	 *
	 * @return the filter view
	 */
	private FilterView createFilterView() {
		String title = "Layer 3 Topology";
		String subTitle = "State - Campus Wide";
		final String[] headers = {"Views", "Filters", "Manager"};
		final FilterView filterView = new FilterView(title, subTitle, headers);
		/*integerService.getEvents(new AsyncCallback<List<Object>>() {

			@Override
			public void onSuccess(List<Object> result) {
				filterView.update(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});*/
		return filterView;
	}
	
	/**
	 * Creates the event view.
	 *
	 * @return the event view
	 */
	private EventView createEventView() {
		String title = "Events";
		final String[] headers = {"Type", "Severity", "Start Time", "Status", "Description"};
		final EventView eventView = new EventView(title, headers);
		/*integerService.getEvents(new AsyncCallback<List<Object>>() {

			@Override
			public void onSuccess(List<Object> result) {
				eventView.update(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});*/
		return eventView;
	}
	
	/**
	 * Show contained tree view.
	 *
	 * @param se the se
	 */
	/*public static void showContainedTreeView(final ServiceElement se) {
		SplitLayoutPanel containedSplitPanel = new SplitLayoutPanel(SPLITTER_SIZE);
		ServiceElementDetailsTabPanel detailsTabPanel = new ServiceElementDetailsTabPanel();
		final ContainedTreeView containedTreeView = new ContainedTreeView(title, headers, containedSplitPanel, detailsTabPanel);
		
	    
	    containedSplitPanel.addSouth(detailsTabPanel, 300);
	    containedSplitPanel.setWidgetHidden(detailsTabPanel, true);
	    containedSplitPanel.setWidgetToggleDisplayAllowed(detailsTabPanel, true);
	    containedSplitPanel.add(containedTreeView);
	    //tabPanel.add(containedSplitPanel, se.getName());
		//tabPanel.selectTab(containedSplitPanel);
		
		MainClient.integerService.getServiceElementByParentId(se.getID(), new AsyncCallback<ServiceElement[]>() {

			@Override
			public void onFailure(Throwable caught) {
				MainClient.statusPanel.showAlert("Failed to receive contained service elements of " + se.getName());
			}

			@Override
			public void onSuccess(ServiceElement[] serviceElements) {
				containedTreeView.updateTree(se.getName(), serviceElements);
			}
		});
	}*/


	public static void showServiceElementMap(Network network) {
		if (network.getServiceElements() == null || network.getServiceElements().isEmpty()) {
			MainClient.statusPanel.showAlert("No service element exists in network " + network.getName());
			return;
		}
			
		int seSize = network.getServiceElements().size();
		int netSize = network.getLowerNetworks().size();
		int linkSize = network.getInterDeviceLinks().size();
		String text = "showing subnet " + network.getName() + ": " + seSize + " service elements, " + netSize + " lower networks, " + linkSize + " inter device links.";
		MainClient.statusPanel.updateStatus(text);
		
		SubnetPanel subnetPanel = new SubnetPanel();
		subnetPanel.updateSubnet(network);
		tabPanel.add(subnetPanel, network.getName());
		tabPanel.selectTab(subnetPanel);
	}
	
}
