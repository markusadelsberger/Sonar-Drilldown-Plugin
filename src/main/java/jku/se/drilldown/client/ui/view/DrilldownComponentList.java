package jku.se.drilldown.client.ui.view;

import java.util.Map;

import jku.se.drilldown.client.ui.controller.DrilldownController;

import org.sonar.gwt.ui.Loading;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all list components. 
 * Implements basic renderer functions 
 * 
 * @author Johannes
 */
public abstract class DrilldownComponentList<T> extends DrilldownComponent implements ClickHandler{

	private Panel data;
	private Grid grid;
	
	// map stores the id of an item as key and its row in the grid as value
	// map contains current list items displayed by the user interface
	private Map<String,Integer> hashmap;
		
	public DrilldownComponentList(DrilldownController drilldownController) {
		super(drilldownController);
		
		Panel content = new VerticalPanel();
		content.setWidth("100%");
		
		content.add(createHeader());
		
		data = new ScrollPanel();
		data.setStyleName("scrollable");
					
		content.add(data);
		
		initWidget(content);
	}

	@Override
	public void onLoad() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}

	/**
	 * Defines the header of the component. 
	 * @return
	 */
	public abstract Widget createHeader();
	
	/**
	 * Initial loading of the list component. 
	 */
	public abstract void doLoadData();

	/**
	 * Method returns the unique identifier of a list item. 
	 * The identifier is used as key value in the map. 
	 * @param item Item from which the identifier will be returned.  
	 * @return Unique identifier of the item
	 */
	public abstract String getItemIdentifier(T item);
	
	/**
	 * Defines the number of columns in the grid. 
	 * @return Number of columns
	 */
	public abstract int gridColumnCount();
	
	/**
	 * Displays the item's values in a grid's row. 
	 * @param item Item to present
	 * @param row Defines the row of the grid. 
	 */
	public abstract void renderRow(T item, int row);
	
	/**
	 * Selects row with a blue background color.
	 * @param row Index of selected row;
	 */
	public void selectRow(int row){
		for(int i=0; i<grid.getCellCount(row); i++) {
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, true));
		}
	}
	
	/**
	 * Deselect row and set default style.
	 * @param row Index of deselected row;
	 */
	public void deselectRow(int row){
		for(int i=0; i<grid.getCellCount(row); i++) {
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, false));
		}
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

	public void setHashmap(Map<String,Integer> hashmap){
		this.hashmap= hashmap;
	}
	
	public Map<String,Integer> getHashmap(){
		return this.hashmap;
	}
	
	/**
	 * Defines the width of the list component
	 * @param size Size in pixel; 
	 */
	public void setWidth(int width) {
		this.data.setWidth(width+"px");
	}
	
	/**
	 * Checks if the item is in the current list which is presented in the UI. 
	 * @param item Look for this item in the current list.  
	 * @return True, the item is in the current list. False, the item is not displayed. 
	 */
	public boolean containsSelectedItem(T item){
		
		if(item!= null && hashmap.get(getItemIdentifier(item))!=null) {
				return true;
		}
		
		return false;
	}
}