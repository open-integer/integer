package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;

/**
 * The Class TechnologyDatabase.
 */
public class TechnologyDatabase {
	/**
	 * A technology category.
	 */
	public static class Category {

		/** The display name. */
		private final String displayName;

		/**
		 * Instantiates a new category.
		 *
		 * @param displayName the display name
		 */
		public Category(String displayName) {
			this.displayName = displayName;
		}

		/**
		 * Gets the display name.
		 *
		 * @return the display name
		 */
		public String getDisplayName() {
			return displayName;
		}
		
	}

	/**
	 * A specific technology item.
	 */
	public static class TechItem implements Comparable<TechItem> {

		/**
		 * The key provider that provides the unique ID of a techitem.
		 */
		public static final ProvidesKey<TechItem> KEY_PROVIDER = new ProvidesKey<TechItem>() {
			@Override
			public Object getKey(TechItem item) {
				return item == null ? null : item.getId();
			}
		};
		
		/** The id. */
		private int id;

		/** The name. */
		private String name;
		
		/** The category. */
		private Category category;

		/**
		 * Instantiates a new tech item.
		 *
		 * @param category the category
		 * @param name the name
		 */
		public TechItem(int id, Category category, String name) {
			setId(id);
			setCategory(category);
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
		 * Gets the category.
		 *
		 * @return the category
		 */
		public Category getCategory() {
			return category;
		}

		/**
		 * Sets the category.
		 *
		 * @param category the new category
		 */
		public void setCategory(Category category) {
			assert category != null : "category cannot be null";
			this.category = category;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			assert category != null : "name cannot be null";
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
		public int compareTo(TechItem o) {
			return (o == null || o.name == null) ? -1 : -o.name.compareTo(name);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {
			if (o instanceof TechItem) {
				return id == ((TechItem) o).id;
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

	/**
	 * The constants used in this Content Widget.
	 */
	static interface DatabaseConstants extends Constants {
		
		/**
		 * Technology database categories.
		 *
		 * @return the string[]
		 */
		@DefaultStringArrayValue({ "Load Balancers", "Routers", "Servers" })
		String[] technologyDatabaseCategories();
	}

	/**
	 * The singleton instance of the database.
	 */
	private static TechnologyDatabase instance;

	/**
	 * Get the singleton instance of the contact database.
	 * 
	 * @return the singleton instance
	 */
	public static TechnologyDatabase get() {
		if (instance == null) {
			instance = new TechnologyDatabase();
		}
		return instance;
	}

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private static ListDataProvider<TechItem> dataProvider = new ListDataProvider<TechItem>();

	/** The categories. */
	//private final Category[] categories;

	/**
	 * Construct a new contact database.
	 */
	private TechnologyDatabase() {
		// Initialize the categories.
//		DatabaseConstants constants = GWT.create(DatabaseConstants.class);
//		String[] catNames = constants.technologyDatabaseCategories();
//		categories = new Category[catNames.length];
//		for (int i = 0; i < catNames.length; i++) {
//			categories[i] = new Category(catNames[i]);
//		}

	}

	/**
	 * Add a new contact.
	 * 
	 * @param techItem
	 *            the item to add.
	 */
	public void addContact(TechItem techItem) {
		List<TechItem> techItemList = dataProvider.getList();
		// Remove the contact first so we don't add a duplicate.
		techItemList.remove(techItem);
		techItemList.add(techItem);
	}

	/**
	 * Add a display to the database. The current range of interest of the
	 * display will be populated with data.
	 * 
	 * @param display
	 *            a {@Link HasData}.
	 */
	public void addDataDisplay(HasData<TechItem> display) {
		dataProvider.addDataDisplay(display);
	}

	/**
	 * Generate the specified number of contacts and add them to the data
	 * provider.
	 */
	public static void generateTechnologyItems(ID parentNode, List<FilterNode> techNodeList) {
		List<TechItem> list = dataProvider.getList();
		
		for (FilterNode node : techNodeList) {
			list.add(createTechItem(node.getItemId().getIdentifier(), parentNode.getName(), node.getItemId().getName()));
			if (node.getChildren() != null)
				generateTechnologyItems(node.getItemId(), node.getChildren());
		}
	}
	
	public static void clear() {
		dataProvider.getList().clear();
	}
	

	/**
	 * Creates the tech item.
	 *
	 * @param catName the cat name
	 * @param itemName the item name
	 * @return the tech item
	 */
	private static TechItem createTechItem(long id, String catName, String itemName) {
		Category category = new Category(catName);
		TechItem techItem = new TechItem((int)id, category, itemName);
		return techItem;
	}

	/**
	 * Gets the data provider.
	 *
	 * @return the data provider
	 */
	public ListDataProvider<TechItem> getDataProvider() {
		return dataProvider;
	}

	/**
	 * Get the categories in the database.
	 * 
	 * @return the categories in the database
	 */
//	public Category[] queryCategories() {
//		return categories;
//	}

	/**
	 * Query all techItems for the specified category.
	 * 
	 * @param category
	 *            the category
	 * @return the list of techItem in the category
	 */
	public List<TechItem> queryTechItemsByCategory(ListDataProvider<TechItem> dataProvider, Category category) {
		List<TechItem> matches = new ArrayList<TechItem>();
		for (TechItem item : dataProvider.getList()) {
			if (item.getCategory().getDisplayName().equals(category.getDisplayName())) {
				matches.add(item);
			}
		}
		return matches;
	}
	
	public List<TechItem> queryAllCategory() {
		List<TechItem> matches = new ArrayList<TechItem>();
		
		return matches;
	}

}
