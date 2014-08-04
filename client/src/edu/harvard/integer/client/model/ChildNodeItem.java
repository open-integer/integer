package edu.harvard.integer.client.model;

public class ChildNodeItem extends RootNodeItem {

	private String parentName;
	
	private int numChildren = 0;
	
	public ChildNodeItem(String parentName, String displayName) {
		super(displayName);
		this.parentName = parentName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the numChildren
	 */
	public int getNumChildren() {
		return numChildren;
	}

	/**
	 * @param numChildren the numChildren to set
	 */
	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}
	
	
}
