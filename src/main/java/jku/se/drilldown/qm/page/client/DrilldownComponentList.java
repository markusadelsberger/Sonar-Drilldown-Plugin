package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.gwt.ui.Loading;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
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
public class DrilldownComponentList extends DrilldownComponent {

	private Panel listPanel;
	private Panel data;

	private List<Resource> resourceList;

	private ClickHandler clickHandler;
	
	private Grid grid;
	private int selectedRow;
	
	public DrilldownComponentList(List<Resource> resourceList, int selectedResourceId, ClickHandler clickHandler) {
		this.resourceList = resourceList;
		this.clickHandler = clickHandler;
		this.selectedRow =0;
		
		listPanel = new VerticalPanel();
		initWidget(listPanel);
	}

	public void setResourceList (List<Resource> resourceList)
	{
		this.resourceList=resourceList;
	}
	
	public void setSelectedRow(int selectedRow)
	{
		this.deselectRow(this.selectedRow);
		this.selectedRow=selectedRow;
		this.selectRow(this.selectedRow);
	}
	
	@Override
	public void onLoad() {
		listPanel.add(createHeader(null));
		data = new ScrollPanel();
		listPanel.add(data);
		loadData();
	}

	public Widget createHeader(String headerTitle) {
		return new Label(headerTitle);
	}

	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}
	
	public void doLoadData() {
		grid = new Grid(resourceList.size(), 4);

		int row = 0;

		for (Resource resource : resourceList) {
			renderIconCells(resource, row);
			renderNameCell( resource, row, 2);
			renderValueCell( resource, row, 3);
			row++;
		}
		render(grid);
	}

	private void selectRow(int row){
		for(int i=0; i<grid.getCellCount(row);i++)
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, true));
	}
	
	private void deselectRow(int row){
		for(int i=0; i<grid.getCellCount(row);i++)
			grid.getCellFormatter().setStyleName(row, i, getRowCssClass(row, false));
	}
	
	protected void render(Widget widget) {
		data.clear();
		data.add(widget);
	}

	protected void renderIconCells(Resource resource, int row ) {
		if(resource.getQualifier().equals(Resource.QUALIFIER_MODULE)||resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
		{
			grid.setWidget(row, 0, new HTML("<a id=\"zoom" + row + "\" href=\"" + Links.urlForResourcePage(resource.getKey(), "jku.se.drilldown.qm.page.QMDrilldownPage", null)+"\">" + Icons.get().zoom().getHTML() + "</a>"));
			grid.getCellFormatter().setStyleName(row, 0, getRowCssClass(row, false));
		}
			
		grid.setWidget(row, 1,new HTML("<div>" + Icons.forQualifier(resource.getQualifier()).getHTML() + "</div>"));
		grid.getCellFormatter().setStyleName(row, 1, getRowCssClass(row, false));
	}

	protected void renderNameCell(final Resource resource, int row, int column) {
		Anchor link = new Anchor(resource.getName());

	    link.getElement().setPropertyObject("resourceObj", resource);
	    link.getElement().setAttribute("gridRow", ""+row);
		
	    if(clickHandler != null)
	    	link.addClickHandler(clickHandler);

		grid.setWidget(row, column, link);
		grid.getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	protected void renderValueCell(Resource resource, int row, int column) {
		grid.setHTML(row, column, resource.getMeasureValue(Metrics.VIOLATIONS).toString());
		grid.getCellFormatter().setStyleName(row, column,getRowCssClass(row, false));
	}

	protected String getRowCssClass(int row, boolean selected) {
		return row % 2 == 0 ? "even" + getRowCssSelected(selected) : "odd" + getRowCssSelected(selected);
	}

	private String getRowCssSelected(boolean selected) {
		return selected ? " selected" : "";
	}
}