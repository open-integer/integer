package edu.harvard.integer.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
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

/**
 * The Class FilterPanel.
 */
public class FilterPanel extends StackLayoutPanel {

	/**
	 * Instantiates a new filter panel.
	 *
	 * @param unit the unit
	 */
	public FilterPanel(Unit unit) {
		super(unit);
		
		add(createTechnologyFilterPanel(), "Technology", 3);
		add(getAbsolutePanel(), "Provider", 3);
		add(createCriticalityFiltersItem(), "Criticality", 3);
		add(createLocationFiltersItem(), "Location", 3);
		add(createServiceFiltersItem(), "Service", 3);
		add(getOrganizationFilterPanel(), "Organization", 3);
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
	private Widget createTechnologyFilterPanel() {
		if (technologyFilterPanel != null)
			return technologyFilterPanel;
		
		final MultiSelectionModel<TechItem> selectionModel = new MultiSelectionModel<TechItem>(
				TechnologyDatabase.TechItem.KEY_PROVIDER);
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						StringBuilder sb = new StringBuilder();
						boolean first = true;
						List<TechItem> selected = new ArrayList<TechItem>(
								selectionModel.getSelectedSet());
						Collections.sort(selected);
						for (TechItem value : selected) {
							if (first) {
								first = false;
							} else {
								sb.append(", ");
							}
							sb.append(value.getName());
						}
					}
				});

		CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
		
		technologyCellTree = new CellTree(new TechnologyTreeViewModel(selectionModel), null, res);
		
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
	private Widget createCriticalityFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("1"));
		filtersPanel.add(new CheckBox("2"));
		filtersPanel.add(new CheckBox("3"));
		
		return new SimplePanel(filtersPanel);
	}
	
	/**
	 * Creates the location filters item.
	 *
	 * @return the widget
	 */
	private Widget createLocationFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Cambridge"));
		filtersPanel.add(new CheckBox("Boston"));
		filtersPanel.add(new CheckBox("New York"));
		
		return new SimplePanel(filtersPanel);
	}
	
	/**
	 * Creates the service filters item.
	 *
	 * @return the widget
	 */
	private Widget createServiceFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Internet"));
		filtersPanel.add(new CheckBox("Cable TV"));
		filtersPanel.add(new CheckBox("Wireless"));
		
		return new SimplePanel(filtersPanel);
	}
	
	/** The organization filter panel. */
	private VerticalPanel organizationFilterPanel;
	
	/**
	 * Gets the organization filter panel.
	 *
	 * @return the organization filter panel
	 */
	private VerticalPanel getOrganizationFilterPanel() {
		if (organizationFilterPanel == null) {
			organizationFilterPanel = new VerticalPanel();
			organizationFilterPanel.setSize("612px", "482px");
			organizationFilterPanel.add(getOrganizationCellTree());
		}
		return organizationFilterPanel;
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
	private AbsolutePanel getAbsolutePanel() {
		if (absolutePanel == null) {
			absolutePanel = new AbsolutePanel();
			absolutePanel.setSize("612px", "482px");
			absolutePanel.add(getCellTree(), 0, 0);
			absolutePanel.add(getBtnAdd(), 265, 428);
			absolutePanel.add(getBtnRemove(), 336, 428);
		}
		return absolutePanel;
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
