/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.common.topology.ServiceElementType;

/**
 * The Class CapabilityView.
 */
public class ContaineeTreeView extends ScrollPanel {
	
	private Tree tree = new Tree();
	private ServiceElement selectedServiceElement;
	private long selectedTimestamp;
	
	/**
	 * Instantiates a new capability view.
	 *
	 * @param title the title
	 * @param headers the headers
	 */
	public ContaineeTreeView(String title, String[] headers) {
		tree.setTitle(title);
	    tree.setAnimationEnabled(true);
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				final TreeItem treeItem = event.getSelectedItem();
				
				selectedServiceElement = (ServiceElement)treeItem.getUserObject();
				SystemSplitViewPanel.detailsTabPanel.update(selectedServiceElement);
				
				selectedTimestamp = System.currentTimeMillis();
				
				MainClient.integerService.getServiceElementByParentId(selectedServiceElement.getID(), new AsyncCallback<ServiceElement[]>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ServiceElement[] serviceElements) {
						SystemSplitViewPanel.containedSplitPanel.setWidgetHidden(SystemSplitViewPanel.detailsTabPanel, false);
						
						if (serviceElements == null || serviceElements.length == 0)
							return;

						for (ServiceElement se : serviceElements) {
							TreeItem item = new TreeItem();
							item.setText(se.getName());
							if (se.getHasChildren())
								item.setText(se.getName() + " (*)");
							item.setUserObject(se);
							treeItem.addItem(item);
						}
						
						MainClient.integerService.getServiceElementTypeById(
								selectedServiceElement.getServiceElementTypeId(), 
								new AsyncCallback<ServiceElementType>() {

									@Override
									public void onFailure(Throwable caught) {
									}

									@Override
									public void onSuccess(
											ServiceElementType serviceElementType) {
										SystemSplitViewPanel.detailsTabPanel.update(serviceElementType);
									}
									
								});
					}
				});
			}
			
		});
		
	    setWidget(tree);
	}

	/**
	 * Update.
	 *
	 * @param result the result
	 */
	public void updateTree(String name, ServiceElement[] elements) {

		tree.removeItems();
	    tree.setAnimationEnabled(true);
	    TreeItem root = new TreeItem();
	    root.setText(name);
	    
	    for (ServiceElement se : elements) {
	    	TreeItem item = new TreeItem();
	    	item.setUserObject(se);
	    	item.setText(se.getName());
	    	root.addItem(item);
	    }
	    
	    root.setState(true);
	    tree.addItem(root);

	}

	public ServiceElement getSelectedServiceElement() {
		return selectedServiceElement;
	}
	
	public long getSelectedTimestamp() {
		return selectedTimestamp;
	}

}
