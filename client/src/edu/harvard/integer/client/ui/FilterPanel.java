package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import edu.harvard.integer.client.ui.TechnologyDatabase.TechItem;
import edu.harvard.integer.client.widget.CheckboxHeader;
import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.Filter;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.topology.CriticalityEnum;

/**
 * The Class FilterPanel.
 */
public class FilterPanel extends StackLayoutPanel {

	private Filter filter;
	/**
	 * Instantiates a new filter panel.
	 *
	 * @param unit the unit
	 */
	public FilterPanel(Filter filter) {
		super(Unit.EM);
		this.filter = filter;
		
		add(createTechnologyFilterPanel(filter.getTechnologies()), "Technology", 3);
		add(getProviderFilterPanel(filter), "Provider", 3);
		add(createCriticalityFilterPanel(filter), "Criticality", 3);
		add(createLocationFilterPanel(filter), "Location", 3);
		add(createServiceFilterPanel(filter), "Service", 3);
		add(getOrganizationFilterPanel(filter), "Organization", 3);
	}

	/** The technology filter panel. */
	private ScrollPanel technologyFilterPanel;
	
	/** The technology cell tree. */
	private CellTree technologyCellTree;
	
	/**
	 * Creates the technology filter panel.
	 *
	 * @return the widget
	 */
	private Widget createTechnologyFilterPanel(List<FilterNode> list) {
		if (technologyFilterPanel != null)
			return technologyFilterPanel;
		
		
		ID rootId = new ID(1L, "Technology", new IDType("Technology"));
		TechnologyDatabase.get().clear();
		TechnologyDatabase.get().generateTechnologyItems(rootId, list);
		
		final MultiSelectionModel<TechItem> selectionModel = new MultiSelectionModel<TechItem>(
				TechnologyDatabase.TechItem.KEY_PROVIDER);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						List<TechItem> selected = new ArrayList<TechItem>(selectionModel.getSelectedSet());
						Collections.sort(selected);
						
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		technologyCellTree = new CellTree(new TechnologyTreeViewModel(selectionModel, list), null, res);
		
		technologyCellTree.setAnimationEnabled(true);
		
		technologyFilterPanel = new ScrollPanel();
		technologyFilterPanel.add(technologyCellTree);
		return technologyFilterPanel;

	}
	
	/**
	 * Creates the criticality filters item.
	 *
	 * @return the widget
	 */
	private Widget createCriticalityFilterPanel(Filter filter) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<CriticalityEnum> ids = filter.getCriticalities();
		for (CriticalityEnum id : ids) {
			filtersPanel.add(new CheckBox(id.name()));
		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/**
	 * Creates the location filters item.
	 *
	 * @return the widget
	 */
	private Widget createLocationFilterPanel(Filter filter) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<ID> ids = filter.getLocations();
		for (ID id : ids) {
			filtersPanel.add(new CheckBox(id.getName()));
		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/**
	 * Creates the service filters item.
	 *
	 * @return the widget
	 */
	private Widget createServiceFilterPanel(Filter filter) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<ID> ids = filter.getServices();
		for (ID id : ids) {
			filtersPanel.add(new CheckBox(id.getName()));
		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/** The organization filter panel. */
	private VerticalPanel organizationFilterPanel;
	
	/**
	 * Gets the organization filter panel.
	 *
	 * @return the organization filter panel
	 */
	private Widget getOrganizationFilterPanel(Filter filter) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<FilterNode> ids = filter.getOrginizations();
//		for (FilterNode id : ids) {
//			filtersPanel.add(new CheckBox(id.getName()));
//		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/** The organization cell tree. */
	private CellTree organizationCellTree;
	
	/**
	 * Gets the organization cell tree.
	 *
	 * @return the organization cell tree
	 */
	private CellTree getOrganizationCellTree() {
		if (organizationCellTree == null) {
		      TreeViewModel model = new CustomTreeModel();
		      organizationCellTree = new CellTree(model, "Item 1");

		    }
		    return organizationCellTree;
	}
	
	/**
	 * The Class CustomTreeModel.
	 */
	private static class CustomTreeModel implements TreeViewModel {

		/* (non-Javadoc)
		 * @see com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
		 */
		@Override
	    public <T> NodeInfo<?> getNodeInfo(T value) {

	      // Create some data in a data provider. Use the parent value as a prefix for the next level.
	      ListDataProvider<String> dataProvider = new ListDataProvider<String>();
	      dataProvider.getList().add("Harvard University");
	      dataProvider.getList().add("Northeastern University");
	      dataProvider.getList().add("Boston University");

	      // Return a node info that pairs the data with a cell.
	      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
	    }

		/* (non-Javadoc)
		 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
		 */
		@Override
	    public boolean isLeaf(Object value) {
	      // The maximum length of a value is ten characters.
	      return value.toString().length() > 20;
	    }
	  }

	
	/** The absolute panel. */
	private AbsolutePanel absolutePanel;
	
	/** The cell tree. */
	private CellTree cellTree;
	
	/** The btn add. */
	private Button btnAdd;
	
	/** The btn remove. */
	private Button btnRemove;
	
	/** The my tree model. */
	private MyTreeModel myTreeModel;
	
	/** The selection model cell tree. */
	private SingleSelectionModel<MyNode> selectionModelCellTree = null;

	/**
	 * Gets the absolute panel.
	 *
	 * @return the absolute panel
	 */
	CellTable providerTable = new CellTable();
	private Widget getProviderFilterPanel(Filter filter) {
		// Add the first column:
	    /*TextColumn<String> column = new TextColumn<String>() {
	        @Override
	        public String getValue(final String object) {
	            return object;
	        }
	    };
	    providerTable.addColumn(column, SafeHtmlUtils.fromSafeConstant("Provider Name"));

	    // the checkbox column for selecting the lease
	    Column<String, Boolean> checkColumn = new Column<String, Boolean>(
	            new CheckboxCell(true, false)) {
	        @Override
	        public Boolean getValue(final String object) {
	            return selectionModel.isSelected(object);
	        }
	    };

	    CheckboxHeader selectAll = new CheckboxHeader();
	    selectAll.setSelectAllHandler(new SelectHandler());
	    providerTable.addColumn(checkColumn, selectAll);*/
	    
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		List<ID> ids = filter.getProviders();
		for (ID id : ids) {
			CheckBox checkbox = new CheckBox(id.getName());
			filtersPanel.add(checkbox);
		}
		
		return new SimplePanel(filtersPanel);
	}
	
	/**
	 * Gets the cell tree.
	 *
	 * @return the cell tree
	 */
	private CellTree getCellTree() {
	    if (cellTree == null) {
	      myTreeModel = new MyTreeModel();
	      cellTree = new CellTree(myTreeModel, null);
	      cellTree.setSize("285px", "401px");
	    }
	    return cellTree;
	  }

	  /**
  	 * Gets the btn add.
  	 *
  	 * @return the btn add
  	 */
  	private Button getBtnAdd() {
	    if (btnAdd == null) {
	      btnAdd = new Button("Add");
	      btnAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MyNode node =   selectionModelCellTree.getSelectedObject();
		        if(node != null)
		            myTreeModel.addNew(node, "Bla");
			}
	      });
	    }
	    return btnAdd;
	  }

	  /**
  	 * Gets the btn remove.
  	 *
  	 * @return the btn remove
  	 */
  	private Button getBtnRemove() {
	    if (btnRemove == null) {
	      btnRemove = new Button("Remove");
	      btnRemove.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	          MyNode node = selectionModelCellTree.getSelectedObject();
	          if(node != null)
	            myTreeModel.remove(node);
	        }
	      });
	    }
	    return btnRemove;
	  }

	  /**
  	 * The Class MyNode.
  	 */
  	public class MyNode {
	    
    	/** The name. */
    	private String name;
	    
    	/** The childs. */
    	private ArrayList<MyNode> childs; //nodes childrens
	    
    	/** The parent. */
    	private MyNode parent; //track internal parent
	    
    	/** The cell. */
    	private MyCell cell; //for refresh - reference to visual component

	    /**
    	 * Instantiates a new my node.
    	 *
    	 * @param name the name
    	 */
    	public MyNode(String name) {
	      super();
	      parent = null;
	      this.name = name;
	      childs = new ArrayList<MyNode>();
	    }

	    /**
    	 * Adds the sub menu.
    	 *
    	 * @param m the m
    	 */
    	public void addSubMenu(MyNode m) {
	      m.parent = this;
	      childs.add(m);
	    }

	    /**
    	 * Removes the menu.
    	 *
    	 * @param m the m
    	 */
    	public void removeMenu(MyNode m) {

	      m.getParent().childs.remove(m);
	    }

	    /**
    	 * Checks for childrens.
    	 *
    	 * @return true, if successful
    	 */
    	public boolean hasChildrens() {
	      return childs.size()>0;
	    }

	    /**
    	 * Gets the list.
    	 *
    	 * @return the list
    	 */
    	public ArrayList<MyNode> getList() {
	      return childs;
	    }

	    /**
    	 * Gets the parent.
    	 *
    	 * @return the parent
    	 */
    	public MyNode getParent() {
	      return parent;
	    }

	    /**
    	 * Sets the cell.
    	 *
    	 * @param cell the new cell
    	 */
    	public void setCell(MyCell cell) {
	      this.cell = cell;
	    }

	    /**
    	 * Refresh.
    	 */
    	public void refresh() {
	      if(parent!=null) {
	        parent.refresh();
	      }
	      if (cell!=null) {
	        cell.refresh(); //refresh tree
	      }
	    }

	    /**
    	 * Gets the name.
    	 *
    	 * @return the name
    	 */
    	public String getName() {
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
	  }

	  /**
  	 * The Class MyTreeModel.
  	 */
  	public class MyTreeModel implements TreeViewModel {
	    
    	/** The official root. */
    	private MyNode officialRoot; //default not dynamic
	    
    	/** The student root. */
    	private MyNode studentRoot; //default not dynamic
	    
    	/** The test root. */
    	private MyNode testRoot; //default not dynamic
	    
    	/** The root. */
    	private MyNode root;

	    /**
    	 * Gets the root.
    	 *
    	 * @return the root
    	 */
    	public MyNode getRoot() { // to set CellTree root
	      return root;
	    }

	    /**
    	 * Instantiates a new my tree model.
    	 */
    	public MyTreeModel() {
	      selectionModelCellTree = new SingleSelectionModel<MyNode>();
	      root = new MyNode("root");
	      // Default items
	      officialRoot = new MyNode("Cisco"); //some basic static data
	      studentRoot = new MyNode("Lucent");
	      testRoot = new MyNode("IBM");
	      root.addSubMenu(officialRoot);
	      root.addSubMenu(studentRoot);
	      root.addSubMenu(testRoot);
	    }

	    //example of add add logic
	    /**
    	 * Adds the new.
    	 *
    	 * @param myparent the myparent
    	 * @param name the name
    	 */
    	public void addNew(MyNode myparent, String name) {
	      myparent.addSubMenu(new MyNode(name));
	      myparent.refresh(); //HERE refresh tree
	    }
	    
    	/**
    	 * Removes the.
    	 *
    	 * @param objToRemove the obj to remove
    	 */
    	public void remove(MyNode objToRemove) {

	      objToRemove.removeMenu(objToRemove);
	      objToRemove.refresh();
	    }

	    /* (non-Javadoc)
    	 * @see com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
    	 */
    	@Override
	    public <T> NodeInfo<?> getNodeInfo(T value) {
	      ListDataProvider<MyNode> dataProvider;
	      MyNode myValue = null;
	      if (value == null) { // root is not set
	        dataProvider = new ListDataProvider<MyNode>(root.getList());
	      } else {
	        myValue = (MyNode) value;
	        dataProvider = new ListDataProvider<MyNode>(myValue.getList());
	      }
	      MyCell cell = new MyCell(dataProvider); //HERE Add reference
	      if (myValue != null)
	        myValue.setCell(cell);
	      return new DefaultNodeInfo<MyNode>(dataProvider, cell, selectionModelCellTree, null);
	    }

	    /* (non-Javadoc)
    	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
    	 */
    	@Override
	    public boolean isLeaf(Object value) {
	      if (value instanceof MyNode) {
	        MyNode t = (MyNode) value;
	        if (!t.hasChildrens())
	          return true;
	        return false;
	      }
	      return false;
	    }
	  }

	  /**
  	 * The Class MyCell.
  	 */
  	public class MyCell extends AbstractCell<MyNode> {
	    
    	/** The data provider. */
    	ListDataProvider<MyNode> dataProvider; //for refresh

	    /**
    	 * Instantiates a new my cell.
    	 *
    	 * @param dataProvider the data provider
    	 */
    	public MyCell(ListDataProvider<MyNode> dataProvider) {
	      super();
	      this.dataProvider = dataProvider;
	    }
	    
    	/**
    	 * Refresh.
    	 */
    	public void refresh() {
	      dataProvider.refresh();
	    }

	    /* (non-Javadoc)
    	 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
    	 */
    	@Override
	    public void render(Context context, MyNode value, SafeHtmlBuilder sb) {
	      if (value == null) {
	        return;
	      }
	      sb.appendEscaped(value.getName());
	    }
	  }


}
