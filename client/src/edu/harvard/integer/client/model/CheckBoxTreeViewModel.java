package edu.harvard.integer.client.model;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import edu.harvard.integer.common.selection.FilterNode;

import java.util.ArrayList;
import java.util.List;

/**
 * The TechnologyTreeViewModel class is the model class for Technology CellTree.
 */
public class CheckBoxTreeViewModel implements TreeViewModel {

	/**
	 * The cell used to render categories.
	 */
	private static class RootNodeCell extends AbstractCell<RootNodeItem> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell
		 * .client.Cell.Context, java.lang.Object,
		 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(Context context, RootNodeItem value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getDisplayName());
			}
		}
	}
	
	/**
	 * The Class SubCategoryCell.
	 */
	private static class ChildNodeCell extends AbstractCell<ChildNodeItem> {

	    /* (non-Javadoc)
    	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
    	 */
    	@Override
	    public void render(Context context, ChildNodeItem value, SafeHtmlBuilder sb) {
	      if (value != null) {
	    	  sb.appendEscaped(value.getDisplayName());
	      }
	    }
	  }

	/** The root-node provider. */
	private final ListDataProvider<RootNodeItem> rootNodeProvider;
	
	/** The child-node provider. */
	private ListDataProvider<ChildNodeItem> childNodeProvider;
	
	/** The leave provider. */
	private ListDataProvider<LeaveItem> leaveProvider;

	/** The leave item cell. */
	private final Cell<LeaveItem> techItemCell;

	/** The selection manager. */
	private final DefaultSelectionEventManager<LeaveItem> selectionManager = DefaultSelectionEventManager
			.createCheckboxManager();

	/** The selection model. */
	private final SelectionModel<LeaveItem> selectionModel;

	/**
	 * Instantiates a new technology tree view model.
	 *
	 * @param techItemProvider the tech item provider
	 * @param subCategoryProvider the sub category provider
	 * @param selectionModel            the selection model
	 * @param filterNodeList the filter node list
	 */
	public CheckBoxTreeViewModel(ListDataProvider<LeaveItem> leaveProvider, 
			ListDataProvider<ChildNodeItem> childNodeProvider, 
			final SelectionModel<LeaveItem> selectionModel, 
			List<FilterNode> filterNodeList) {
		this.leaveProvider = leaveProvider;
		this.childNodeProvider = childNodeProvider;
		this.selectionModel = selectionModel;

		// Create a data provider that provides categories.
		rootNodeProvider = new ListDataProvider<RootNodeItem>();
		List<RootNodeItem> rootNodeList = rootNodeProvider.getList();

		for (FilterNode filterNode : filterNodeList) {
			rootNodeList.add(new RootNodeItem(filterNode.getItemId().getName()));
		}

		// Construct a composite cell for contacts that includes a checkbox.
		List<HasCell<LeaveItem, ?>> hasCells = new ArrayList<HasCell<LeaveItem, ?>>();
		hasCells.add(new HasCell<LeaveItem, Boolean>() {

			private CheckboxCell cell = new CheckboxCell(true, false);

			public Cell<Boolean> getCell() {
				return cell;
			}

			public FieldUpdater<LeaveItem, Boolean> getFieldUpdater() {
				return null;
			}

			public Boolean getValue(LeaveItem object) {
				return selectionModel.isSelected(object);
			}
		});
		hasCells.add(new HasCell<LeaveItem, SafeHtml>() {

			private SafeHtmlCell cell = new SafeHtmlCell();

			public Cell<SafeHtml> getCell() {
				return cell;
			}

			public FieldUpdater<LeaveItem, SafeHtml> getFieldUpdater() {
				return null;
			}

			public SafeHtml getValue(LeaveItem object) {
				return new SafeHtmlBuilder().appendEscaped(object.getName())
						.toSafeHtml();
			}
		});

		techItemCell = new CompositeCell<LeaveItem>(hasCells) {
			@Override
			public void render(Context context, LeaveItem value,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected Element getContainerElement(Element parent) {
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}

			@Override
			protected <X> void render(Context context, LeaveItem value,
					SafeHtmlBuilder sb, HasCell<LeaveItem, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</td>");
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
	 */
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			// Return top level categories.
			return new DefaultNodeInfo<RootNodeItem>(rootNodeProvider, new RootNodeCell());
		}
		else if (value instanceof RootNodeItem) {
			// Return the first letters of each first name.
			RootNodeItem category = (RootNodeItem) value;

			List<ChildNodeItem> counts = querySubCategoryByCategory(category);
			
			if (!counts.isEmpty())
				return new DefaultNodeInfo<ChildNodeItem>(new ListDataProvider<ChildNodeItem>(counts), new ChildNodeCell());
			
			List<LeaveItem> techItems = queryTechItemsByCategory(category);
			ListDataProvider<LeaveItem> technologyProvider = new ListDataProvider<LeaveItem>(
					techItems, LeaveItem.KEY_PROVIDER);
			return new DefaultNodeInfo<LeaveItem>(technologyProvider, techItemCell,
				selectionModel, selectionManager, null);
		}
		else if (value instanceof ChildNodeItem) {
			ChildNodeItem subCategory = (ChildNodeItem) value;

			List<LeaveItem> techItems = queryTechItemsByCategory(subCategory);

			ListDataProvider<LeaveItem> technologyProvider = new ListDataProvider<LeaveItem>(
					techItems, LeaveItem.KEY_PROVIDER);
			return new DefaultNodeInfo<LeaveItem>(technologyProvider, techItemCell,
				selectionModel, selectionManager, null);
		}

		// Unhandled type.
		String type = value.getClass().getName();
		throw new IllegalArgumentException("Unsupported object type: " + type);
	}
	
	/**
	 * Query sub category by category.
	 *
	 * @param category the category
	 * @return the list
	 */
	private List<ChildNodeItem> querySubCategoryByCategory(RootNodeItem category) {
		List<ChildNodeItem> matches = new ArrayList<ChildNodeItem>();
		for (ChildNodeItem item : childNodeProvider.getList()) {
			if (item.getParentName().equals(category.getDisplayName())) {
				matches.add(item);
			}
		}
		return matches;
	}

	/**
	 * Query tech items by category.
	 *
	 * @param category the category
	 * @return the list
	 */
	public List<LeaveItem> queryTechItemsByCategory(RootNodeItem category) {
		List<LeaveItem> matches = new ArrayList<LeaveItem>();
		for (LeaveItem item : leaveProvider.getList()) {
			if (item.getCategory().getDisplayName().equals(category.getDisplayName())) {
				matches.add(item);
			}
		}
		return matches;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object value) {
		return value instanceof LeaveItem;
	}

}
