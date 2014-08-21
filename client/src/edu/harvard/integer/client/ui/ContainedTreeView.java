/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class ContainedTreeView represents a contained tree view object of Integer.
 * This is a subclass class extended from com.google.gwt.user.client.ui.ScrollPanel.
 * 
 * @author  Joel Huang
 * @version 1.0, May 2014
 */
public class ContainedTreeView extends ScrollPanel {
	
	/** The fake child node. */
	public static String FAKE_CHILD_NODE = "Fake Child";
	
	/** The tree. */
	private Tree tree = new Tree();
	
	/** The selected service element. */
	private ServiceElement selectedServiceElement;
	
	/** The contained split panel contains tree and detailsTabPanel */
	private SplitLayoutPanel containedSplitPanel;
	
	/** The detailsTabPanel of service element. */
	private ServiceElementDetailsTabPanel detailsTabPanel;
	
	/** The selected timestamp. */
	private long selectedTimestamp;

	/**
	 * Create a new ContainedTreeView.
	 *
	 * @param title the title
	 * @param detailsTabPanel the details tab panel
	 */
	public ContainedTreeView(String title, final SplitLayoutPanel containedSplitPanel, final ServiceElementDetailsTabPanel detailsTabPanel) {
		this.containedSplitPanel = containedSplitPanel;
		this.detailsTabPanel = detailsTabPanel;
		
		tree.setTitle(title);
	    tree.setAnimationEnabled(true);
	    
	    tree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				final TreeItem treeItem = event.getTarget();
				
				selectedServiceElement = (ServiceElement)treeItem.getUserObject();
				
				// get first child of the treeItem
				TreeItem firstChild = treeItem.getChild(0);
				
				// remove first child if it is a fake child
				if (firstChild != null && firstChild.getText().equals(FAKE_CHILD_NODE))
					treeItem.removeItem(firstChild);
				else
					return;
				
				MainClient.integerService.getServiceElementByParentId(selectedServiceElement.getID(), new AsyncCallback<ServiceElement[]>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ServiceElement[] serviceElements) {
						containedSplitPanel.setWidgetHidden(detailsTabPanel, false);
						
						if (serviceElements == null || serviceElements.length == 0)
							return;

						for (ServiceElement se : serviceElements) {
							TreeItem item = new TreeItem();
							String title = getServiceElementTitle(se);
							item.setText(title);
							item.setUserObject(se);
							
							if (se.getHasChildren())
								item.addTextItem(FAKE_CHILD_NODE);
							treeItem.addItem(item);
						}
					}
				});
			}
	    	
	    });
	    
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				final TreeItem treeItem = event.getSelectedItem();
				
				selectedServiceElement = (ServiceElement)treeItem.getUserObject();
				
				if (selectedServiceElement != null)
					detailsTabPanel.update(selectedServiceElement);
				
			}
			
		});
		
	    setWidget(tree);
	}

	/**
	 * Update method will refresh the contained tree view of the object by object's name and the list of service element objects.
	 *
	 * @param  the name
	 * @param elements the elements
	 */
	public void updateTree(ServiceElement serviceElement, ServiceElement[] elements) {
		containedSplitPanel.setWidgetHidden(detailsTabPanel, false);
		
		tree.removeItems();
	    tree.setAnimationEnabled(true);
	    TreeItem root = new TreeItem();
	    root.setUserObject(serviceElement);
	    root.setText(serviceElement.getName());
	    
	    for (ServiceElement se : elements) {
	    	TreeItem item = new TreeItem();
	    	String title = getServiceElementTitle(se);
	    	item.setUserObject(se);
	    	item.setText(title);
	    	
	    	if (se.getHasChildren())
				item.addTextItem(FAKE_CHILD_NODE);
	    	
	    	root.addItem(item);
	    }
	    
	    root.setState(true);
	    tree.addItem(root);
	    
	    // select root initially
	    tree.setSelectedItem(root);

	}

	/**
	 * Gets the selected service element.
	 *
	 * @return the selected service element
	 */
	public ServiceElement getSelectedServiceElement() {
		return selectedServiceElement;
	}
	
	/**
	 * Gets the selected timestamp.
	 *
	 * @return the selected timestamp
	 */
	public long getSelectedTimestamp() {
		return selectedTimestamp;
	}
	
	private String getServiceElementTitle(ServiceElement se) {
		StringBuffer title = new StringBuffer();
		title.append(se.getName());
		
		title.append(" (").append(se.getDescription());
		if (se.getCategory() != null)
			title.append(" - ").append(se.getCategory().getName());
		title.append(")");
		
		return title.toString();
	}

}
