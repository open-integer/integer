package edu.harvard.integer.client.model;

import com.google.gwt.view.client.ProvidesKey;

public class LeaveItem implements Comparable<LeaveItem> {

	/**
	 * The key provider that provides the unique ID of a techitem.
	 */
	public static final ProvidesKey<LeaveItem> KEY_PROVIDER = new ProvidesKey<LeaveItem>() {
		@Override
		public Object getKey(LeaveItem item) {
			return item == null ? null : item.getId();
		}
	};
	
	/** The id. */
	private int id;

	/** The display name. */
	private String name;
	
	/** The parentNode. */
	private RootNodeItem parentNode;

	/**
	 * Instantiates a new LeaveItem.
	 *
	 * @param parentNode the parentNode
	 * @param name the name
	 */
	public LeaveItem(int id, RootNodeItem parentNode, String name) {
		setId(id);
		setCategory(parentNode);
		setName(name);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the parentNode.
	 *
	 * @return the parentNode
	 */
	public RootNodeItem getCategory() {
		return parentNode;
	}

	/**
	 * Sets the parentNode.
	 *
	 * @param parentNode the new parentNode
	 */
	public void setCategory(RootNodeItem parentNode) {
		assert parentNode != null : "parentNode cannot be null";
		this.parentNode = parentNode;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		assert parentNode != null : "name cannot be null";
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(LeaveItem o) {
		return (o == null || o.name == null) ? -1 : -o.name.compareTo(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof LeaveItem) {
			return id == ((LeaveItem) o).id;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id;
	}
}
