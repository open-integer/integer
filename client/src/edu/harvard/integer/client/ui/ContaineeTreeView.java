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
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.harvard.integer.client.MainClient;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.ServiceElement;

/**
 * The Class CapabilityView.
 */
public class ContaineeTreeView extends VerticalPanel {
	
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
			public void onSelection(SelectionEvent event) {
				final TreeItem selectedItem = tree.getSelectedItem();
				ID id = new ID();
				id.setName(selectedItem.getText());
				MainClient.integerService.getServiceElementByParentId(id, new AsyncCallback<ServiceElement[]>() {

					@Override
					public void onFailure(Throwable caught) {
						for (int i = 0; i < 3; i++) {
							selectedItem.addTextItem("grand son-"+i);
						}
					}

					@Override
					public void onSuccess(ServiceElement[] serviceElements) {
						if (serviceElements == null || serviceElements.length == 0) {
							for (int i = 0; i < 4; i++) {
								selectedItem.addTextItem("grand child-"+i);
							}
						}
						
						for (ServiceElement se : serviceElements) {
							selectedItem.addTextItem(se.getName());
						}
					}
				});
			}
			
		});
		
	    ScrollPanel staticTreeWrapper = new ScrollPanel(tree);
	    staticTreeWrapper.setSize("250px", "500px");
	    
	    add(tree);
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
	    
	    for (ServiceElement element : elements) {
	    	root.addTextItem(element.getName());
	    }
	    
	    root.setState(true);
	    tree.addItem(root);

	}
}
