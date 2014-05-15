/*
 * 
 */
package edu.harvard.integer.client.ui;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class CapabilityView.
 */
public class ContaineeTreeView extends ScrollPanel {
	
	private Tree tree = new Tree();
	
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
				
				ServiceElement se = (ServiceElement)treeItem.getUserObject();
				
				MainClient.integerService.getServiceElementByParentId(se.getID(), new AsyncCallback<ServiceElement[]>() {

					@Override
					public void onFailure(Throwable caught) {
						/*for (int i = 0; i < 3; i++) {
							ServiceElement se = new ServiceElement();
							se.setName("fake son-" + i);
							TreeItem item = new TreeItem();
							item.setText(se.getName());
							item.setUserObject(se);
							treeItem.addItem(item);
						}*/
					}

					@Override
					public void onSuccess(ServiceElement[] serviceElements) {	
						for (ServiceElement se : serviceElements) {
							TreeItem item = new TreeItem();
							item.setText(se.getName());
							item.setUserObject(se);
							treeItem.addItem(item);
						}
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
}
