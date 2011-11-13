package jku.se.drilldown.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.gwt.ui.Loading;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Johannes
 * 
 */
public abstract class DrilldownComponentList<T> extends DrilldownComponent {

	private Panel listPanel;
	private Panel data;

	private List<T> itemList;

	private ClickHandler clickHandler;
	
	private Grid grid;
	private T selectedItem;
	// hashmap stores the id of an item as key and its row in the grid as value
	private Map<String,Integer> hashmap;
	
	public DrilldownComponentList(List<T> itemList, ClickHandler clickHandler) {
		this.setItemList(itemList);
		this.setClickHandler(clickHandler);
		this.selectedItem = null;
		
		listPanel = new VerticalPanel();
		initWidget(listPanel);
	}

	@Override
	public void onLoad() {
		listPanel.add(createHeader());
		data = new ScrollPanel();
		listPanel.add(data);
		loadData();
	}

	public abstract Widget createHeader();

	/**
	 * Method is entry point to reload component. 
	 */
	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}
	
	public void doLoadData()
	{
		this.grid = new Grid(itemList.size(), gridColumnCount());
		
		this.hashmap= new HashMap<String,Integer>();
		
		int row = 0;
		
		for (T item : this.itemList) {
			renderRow(item, row);
		
			hashmap.put(getItemIdentifier(item), new Integer(row));

			row++;
		}
		
		if(containsSelectedItem())
			this.selectRow(hashmap.get(getItemIdentifier(this.selectedItem)));
				
	
		render(grid);	
	}

	public abstract String getItemIdentifier(T item);
	
	public abstract int gridColumnCount();
	
	public abstract void renderRow(T item, int row);
	
	private void selectRow(int row){
		for(int i=0; i<grid.getCellCount(row); i++)
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, true));
	}
	
	private void deselectRow(int row){
		for(int i=0; i<grid.getCellCount(row); i++)
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, false));
	}
	
	protected void render(Widget widget) {
		data.clear();
		data.add(widget);
	}

	protected String getRowCssClass(int row, boolean selected) {
		return row % 2 == 0 ? "even" + getRowCssSelected(selected) : "odd" + getRowCssSelected(selected);
	}

	private String getRowCssSelected(boolean selected) {
		return selected ? " selected" : "";
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public List<T> getItemList() {
		return itemList;
	}

	public void setItemList (List<T> resourceList){
		this.itemList=resourceList;
	}
	
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public void setSelectedItem(T selectedItem)
	{
		if(this.selectedItem!=null)
			if(hashmap.get(getItemIdentifier(this.selectedItem))!=null)
				deselectRow(hashmap.get(getItemIdentifier(this.selectedItem)));
				
		this.selectedItem=selectedItem;
				
		selectRow(hashmap.get(getItemIdentifier(this.selectedItem)));
	}
	
	public T getSelectedItem()
	{
		return this.selectedItem;
	}
	
	public boolean containsSelectedItem()
	{
		if(this.selectedItem!= null)
			if(hashmap.get(getItemIdentifier(this.selectedItem))!=null)
				return true;
		
		return false;
	
	}
}