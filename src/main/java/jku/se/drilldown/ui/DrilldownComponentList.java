package jku.se.drilldown.ui;

import java.util.List;

import org.sonar.gwt.ui.Loading;
import org.sonar.wsclient.services.Resource;

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
public abstract class DrilldownComponentList extends DrilldownComponent {

	private Panel listPanel;
	private Panel data;

	private List<Resource> resourceList;

	private ClickHandler clickHandler;
	
	private Grid grid;
	private int selectedRow;
	
	public DrilldownComponentList(List<Resource> resourceList, ClickHandler clickHandler) {
		this.setResourceList(resourceList);
		this.setClickHandler(clickHandler);
		this.selectedRow =0;
		
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
	
	public abstract void doLoadData();

	private void selectRow(int row){
		for(int i=0; i<getGrid().getCellCount(row);i++)
			getGrid().getCellFormatter().setStyleName(row, i, getRowCssClass(row, true));
	}
	
	private void deselectRow(int row){
		for(int i=0; i<getGrid().getCellCount(row);i++)
			getGrid().getCellFormatter().setStyleName(row, i, getRowCssClass(row, false));
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

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList (List<Resource> resourceList)
	{
		this.resourceList=resourceList;
	}
	
	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public void setSelectedRow(int selectedRow)
	{
		this.deselectRow(this.selectedRow);
		this.selectedRow=selectedRow;
		this.selectRow(this.selectedRow);
	}
}