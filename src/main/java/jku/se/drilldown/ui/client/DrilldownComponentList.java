package jku.se.drilldown.ui.client;

import java.util.List;
import java.util.Map;

import org.sonar.gwt.ui.Loading;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Johannes
 * 
 */
public abstract class DrilldownComponentList<T> extends DrilldownComponent implements ClickHandler{

	private Panel listPanel;
	private Panel data;

	private List<T> itemList;

	private Grid grid;
	private T selectedItem;
	
	// map stores the id of an item as key and its row in the grid as value
	// map contains current list items displayed by the user interface
	private Map<String,Integer> hashmap;
	
	public DrilldownComponentList() {
		this.setItemList(itemList);
		this.selectedItem = null;
		
		listPanel = new VerticalPanel();
		initWidget(listPanel);
	}

	@Override
	public void onLoad() {
		listPanel.add(createHeader());
		data = new ScrollPanel();
		data.setStyleName("scrollable");
		listPanel.add(data);
		loadData();
	}

	public abstract Widget createHeader();

	/**
	 * Method is entry point to reload component.
	 * During the creation of the component a loading icon shows that the component is under work.  
	 */
	protected void loadData() {
		data.clear();
		data.add(new Loading());
		try{
			doLoadData();
		}catch(Exception e){
			Window.alert("Exception in loadData: "+e.toString());
		}
		
	}
	
	public abstract void doLoadData();

	/**
	 * Method returns the unique identifier of an list item. 
	 * The identifier is used as key value in the map. 
	 */
	public abstract String getItemIdentifier(T item);
	
	public abstract int gridColumnCount();
	
	public abstract void renderRow(T item, int row);
	
	public void selectRow(int row){
		for(int i=0; i<grid.getCellCount(row); i++)
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, true));
	}
	
	public void deselectRow(int row){
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
		
	/**
	 * Changing the selected item includes a update of the user interface. 
	 * In the case the selected item is not in the current list the row has not to be deselected. 
	 * 
	 * @param selectedItem The new selected item.
	 */
	public void setSelectedItem(T selectedItem)
	{
		if(this.selectedItem!=null)
			if(hashmap.get(getItemIdentifier(this.selectedItem))!=null)
				deselectRow(hashmap.get(getItemIdentifier(this.selectedItem)));
				
		this.selectedItem=selectedItem;
		
		if(this.selectedItem!=null)
			selectRow(hashmap.get(getItemIdentifier(this.selectedItem)));
	}
	
	public void setHashmap(Map<String,Integer> hashmap)
	{
		this.hashmap= hashmap;
	}
	
	public T getSelectedItem()
	{
		return this.selectedItem;
	}
	
	/**
	 * Checks if the selected item is in the current list which is presented as user interface. 
	 * 
	 * @return
	 */
	public boolean containsSelectedItem()
	{
		if(this.selectedItem!= null)
			if(hashmap.get(getItemIdentifier(this.selectedItem))!=null)
				return true;
		
		return false;
	}
}