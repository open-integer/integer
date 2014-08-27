package edu.harvard.integer.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.SelectionAppearance;  
import com.smartgwt.client.types.TreeModelType;  
import com.smartgwt.client.widgets.events.DrawEvent;  
import com.smartgwt.client.widgets.events.DrawHandler;  
import com.smartgwt.client.widgets.layout.HLayout;  
import com.smartgwt.client.widgets.tree.Tree;  
import com.smartgwt.client.widgets.tree.TreeGrid;  
import com.smartgwt.client.widgets.tree.TreeNode;

import edu.harvard.integer.client.ui.FilterPanel;
import edu.harvard.integer.common.selection.FilterNode;

public class HvCheckBoxTree extends HLayout {
	
	public static int MIN_TREE_HEIGHT = 120;
	public static int MARGIN_BAR_WIDTH = 25;
	
	private TreeGrid filterTreeGrid = new TreeGrid();
	
	public HvCheckBoxTree(List<FilterNode> list) {
		
		Tree filterTree = new Tree();  
        filterTree.setModelType(TreeModelType.PARENT);  
        filterTree.setRootValue(-1);  
        filterTree.setNameProperty("Name");  
        filterTree.setIdField("ItemId");  
        filterTree.setParentIdField("ReportsTo");  
        filterTree.setOpenProperty("isOpen");  
        filterTree.setReportCollisions(false);
        filterTree.setData(convertToFilterTreeNodes(list));  
  
        int treeHeight = Window.getClientHeight() - 128 - FilterPanel.FILTER_ITEM_HEIGHT -40;
        if (treeHeight < MIN_TREE_HEIGHT)
        	treeHeight = MIN_TREE_HEIGHT;
        
        setSize("100%", treeHeight+"px");
        filterTreeGrid.setSize(FilterPanel.FILTER_PANEL_WIDTH-MARGIN_BAR_WIDTH+"px", treeHeight+"px");

        filterTreeGrid.setShowOpenIcons(false);  
        filterTreeGrid.setShowDropIcons(false); 
        
        filterTreeGrid.setData(filterTree);  
        filterTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);  
        filterTreeGrid.setShowSelectedStyle(false);  
        filterTreeGrid.setShowPartialSelection(true);  
        filterTreeGrid.setCascadeSelection(true);  
  
        filterTreeGrid.addDrawHandler(new DrawHandler() {  
            public void onDraw(DrawEvent event) {  
                // filterTreeGrid.getTree().openAll();  
            }  
        });   
  
        addMember(filterTreeGrid);   
  
        draw(); 
	}

	public class FilterTreeNode extends TreeNode {  
        public FilterTreeNode(String itemId, String reportsTo, String name) {  
            setAttribute("ItemId", itemId);  
            setAttribute("ReportsTo", reportsTo);  
            setAttribute("Name", name);   
            setAttribute("isOpen", false);  
        }  
    }
	
	private List<FilterTreeNode> getTreeNodeList(String parentId, List<FilterNode> list) {
		List<FilterTreeNode> filterTreeNodeList = new ArrayList<FilterTreeNode>();
		for (FilterNode filterNode : list) {
			FilterTreeNode filterTreeNode = new FilterTreeNode(filterNode.getItemId().getIdentifier().toString(), parentId, filterNode.getName());
			filterTreeNodeList.add(filterTreeNode);
			
			if (filterNode.getChildren() != null && !filterNode.getChildren().isEmpty()) {
				String id = filterNode.getItemId().getIdentifier().toString();
				filterTreeNodeList.addAll(getTreeNodeList(id, filterNode.getChildren()));
			}
		}
		return filterTreeNodeList;
	}

	private TreeNode[] convertToFilterTreeNodes(List<FilterNode> list) {
		List<FilterTreeNode> filterTreeNodeList = getTreeNodeList("0", list);
		TreeNode[] array = new TreeNode[filterTreeNodeList.size()];
		
		return filterTreeNodeList.toArray(array);
	}
	
	/**
	 * set TreeGrid size by given width and height
	 * 
	 * @param width
	 * @param height
	 */
	public void setTreeGridSize(int width, int height) {
		int treeHeight = height - FilterPanel.FILTER_ITEM_HEIGHT - 40;
        if (treeHeight < MIN_TREE_HEIGHT)
        	treeHeight = MIN_TREE_HEIGHT;
//        
//		if (height < MIN_TREE_HEIGHT + MARGIN_BAR_WIDTH)
//        	return;
        
        setSize("100%", treeHeight+"px");
		//filterTreeGrid.setWidth(width-MARGIN_BAR_WIDTH);  
        filterTreeGrid.setHeight(treeHeight); 
	}
}
