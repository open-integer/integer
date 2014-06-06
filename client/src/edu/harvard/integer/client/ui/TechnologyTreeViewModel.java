package edu.harvard.integer.client.ui;

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

import edu.harvard.integer.client.ui.TechnologyDatabase.Category;
import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.common.selection.FilterNode;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TechnologyTreeViewModel.
 */
public class TechnologyTreeViewModel implements TreeViewModel {

	/**
	 * The cell used to render categories.
	 */
	private static class CategoryCell extends AbstractCell<Category> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell
		 * .client.Cell.Context, java.lang.Object,
		 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(Context context, Category value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getDisplayName());
			}
		}
	}

	/** The category data provider. */
	private final ListDataProvider<Category> categoryDataProvider;
	
	private ListDataProvider<TechItem> dataProvider;

	/** The tech item cell. */
	private final Cell<TechItem> techItemCell;

	/** The selection manager. */
	private final DefaultSelectionEventManager<TechItem> selectionManager = DefaultSelectionEventManager
			.createCheckboxManager();

	/** The selection model. */
	private final SelectionModel<TechItem> selectionModel;

	/** The filter node list. */
	private List<FilterNode> filterNodeList;

	/**
	 * Instantiates a new technology tree view model.
	 *
	 * @param selectionModel            the selection model
	 * @param filterNodeList the filter node list
	 */
	public TechnologyTreeViewModel(ListDataProvider<TechItem> dataProvider, final SelectionModel<TechItem> selectionModel, List<FilterNode> filterNodeList) {
		this.dataProvider = dataProvider;
		this.selectionModel = selectionModel;
		this.filterNodeList = filterNodeList;

		// Create a data provider that provides categories.
		categoryDataProvider = new ListDataProvider<Category>();
		List<Category> categoryList = categoryDataProvider.getList();

		for (FilterNode filterNode : filterNodeList) {
			categoryList.add(new Category(filterNode.getItemId().getName()));
		}

		// Construct a composite cell for contacts that includes a checkbox.
		List<HasCell<TechItem, ?>> hasCells = new ArrayList<HasCell<TechItem, ?>>();
		hasCells.add(new HasCell<TechItem, Boolean>() {

			private CheckboxCell cell = new CheckboxCell(true, false);

			public Cell<Boolean> getCell() {
				return cell;
			}

			public FieldUpdater<TechItem, Boolean> getFieldUpdater() {
				return null;
			}

			public Boolean getValue(TechItem object) {
				return selectionModel.isSelected(object);
			}
		});
		hasCells.add(new HasCell<TechItem, SafeHtml>() {

			private SafeHtmlCell cell = new SafeHtmlCell();

			public Cell<SafeHtml> getCell() {
				return cell;
			}

			public FieldUpdater<TechItem, SafeHtml> getFieldUpdater() {
				return null;
			}

			public SafeHtml getValue(TechItem object) {
				return new SafeHtmlBuilder().appendEscaped(object.getName())
						.toSafeHtml();
			}
		});

		techItemCell = new CompositeCell<TechItem>(hasCells) {
			@Override
			public void render(Context context, TechItem value,
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
			protected <X> void render(Context context, TechItem value,
					SafeHtmlBuilder sb, HasCell<TechItem, X> hasCell) {
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
			return new DefaultNodeInfo<Category>(categoryDataProvider, new CategoryCell());
		} else if (value instanceof Category) {
			// Return the first letters of each first name.
			Category category = (Category) value;

			List<TechItem> techItems = queryTechItemsByCategory(category);

			ListDataProvider<TechItem> dataProvider = new ListDataProvider<TechItem>(
					techItems, TechItem.KEY_PROVIDER);
			return new DefaultNodeInfo<TechItem>(dataProvider, techItemCell,
					selectionModel, selectionManager, null);
		}

		// Unhandled type.
		String type = value.getClass().getName();
		throw new IllegalArgumentException("Unsupported object type: " + type);
	}
	
	public List<TechItem> queryTechItemsByCategory(Category category) {
		List<TechItem> matches = new ArrayList<TechItem>();
		for (TechItem item : dataProvider.getList()) {
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
		return value instanceof TechItem;
	}

}
