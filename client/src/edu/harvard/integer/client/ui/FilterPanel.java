package edu.harvard.integer.client.ui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class FilterPanel extends StackLayoutPanel {

	public FilterPanel(Unit unit) {
		super(unit);
		
		add(createTechnologyFiltersItem(), "Technology", 3);
		add(getAbsolutePanel(), "Provider", 3);
		add(createCriticalityFiltersItem(), "Criticality", 3);
		add(createLocationFiltersItem(), "Location", 3);
		add(createServiceFiltersItem(), "Service", 3);
		add(createOrganizationFiltersItem(), "Organization", 3);
	}

	private Widget createTechnologyFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Load Balancers"));
		filtersPanel.add(new CheckBox("Router"));
		filtersPanel.add(new CheckBox("Server"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private Widget createProviderFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Cisco"));
		filtersPanel.add(new CheckBox("Juniper"));
		filtersPanel.add(new CheckBox("Lucent"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private Widget createCriticalityFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("1"));
		filtersPanel.add(new CheckBox("2"));
		filtersPanel.add(new CheckBox("3"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private Widget createLocationFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Cambridge"));
		filtersPanel.add(new CheckBox("Boston"));
		filtersPanel.add(new CheckBox("New York"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private Widget createServiceFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("Internet"));
		filtersPanel.add(new CheckBox("Cable TV"));
		filtersPanel.add(new CheckBox("Wireless"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private Widget createOrganizationFiltersItem() {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new CheckBox("NBC"));
		filtersPanel.add(new CheckBox("CNN"));
		filtersPanel.add(new CheckBox("GOV"));
		
		return new SimplePanel(filtersPanel);
	}
	
	private AbsolutePanel absolutePanel;
	private CellTree cellTree;
	private Button btnAdd;
	private Button btnRemove;
	private MyTreeModel myTreeModel;
	private SingleSelectionModel<MyNode> selectionModelCellTree = null;

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
	
	private CellTree getCellTree() {
	    if (cellTree == null) {
	      myTreeModel = new MyTreeModel();
	      cellTree = new CellTree(myTreeModel, null);
	      cellTree.setSize("285px", "401px");
	    }
	    return cellTree;
	  }

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

	  public class MyNode {
	    private String name;
	    private ArrayList<MyNode> childs; //nodes childrens
	    private MyNode parent; //track internal parent
	    private MyCell cell; //for refresh - reference to visual component

	    public MyNode(String name) {
	      super();
	      parent = null;
	      this.name = name;
	      childs = new ArrayList<MyNode>();
	    }

	    public void addSubMenu(MyNode m) {
	      m.parent = this;
	      childs.add(m);
	    }

	    public void removeMenu(MyNode m) {

	      m.getParent().childs.remove(m);
	    }

	    public boolean hasChildrens() {
	      return childs.size()>0;
	    }

	    public ArrayList<MyNode> getList() {
	      return childs;
	    }

	    public MyNode getParent() {
	      return parent;
	    }

	    public void setCell(MyCell cell) {
	      this.cell = cell;
	    }

	    public void refresh() {
	      if(parent!=null) {
	        parent.refresh();
	      }
	      if (cell!=null) {
	        cell.refresh(); //refresh tree
	      }
	    }

	    public String getName() {
	      return name;
	    }

	    public void setName(String name) {
	      this.name = name;
	    }
	  }

	  public class MyTreeModel implements TreeViewModel {
	    private MyNode officialRoot; //default not dynamic
	    private MyNode studentRoot; //default not dynamic
	    private MyNode testRoot; //default not dynamic
	    private MyNode root;

	    public MyNode getRoot() { // to set CellTree root
	      return root;
	    }

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
	    public void addNew(MyNode myparent, String name) {
	      myparent.addSubMenu(new MyNode(name));
	      myparent.refresh(); //HERE refresh tree
	    }
	    public void remove(MyNode objToRemove) {

	      objToRemove.removeMenu(objToRemove);
	      objToRemove.refresh();
	    }

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

	  public class MyCell extends AbstractCell<MyNode> {
	    ListDataProvider<MyNode> dataProvider; //for refresh

	    public MyCell(ListDataProvider<MyNode> dataProvider) {
	      super();
	      this.dataProvider = dataProvider;
	    }
	    public void refresh() {
	      dataProvider.refresh();
	    }

	    @Override
	    public void render(Context context, MyNode value, SafeHtmlBuilder sb) {
	      if (value == null) {
	        return;
	      }
	      sb.appendEscaped(value.getName());
	    }
	  }


}
