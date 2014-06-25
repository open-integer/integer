package edu.harvard.integer.client.model;

public class ChildNodeItem extends RootNodeItem {

	private String parentName;
	
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
}
