package jku.se.drilldown.qm.page.client;

import java.util.List;

import jku.se.drilldown.qm.page.QMDrilldownPage;

import org.sonar.gwt.Configuration;
import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.gwt.ui.Loading;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
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
	private int selectedResourceId;
	
	private String requestQuery;

	public DrilldownComponentList(List<Resource> resourceList, int selectedResourceKey, String requestQuery) {
		this.resourceList = resourceList;
		this.selectedResourceId = selectedResourceKey;
		this.requestQuery=requestQuery;
		listPanel = new VerticalPanel();
		initWidget(listPanel);
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

	public void doLoadData() {
		final Grid grid = new Grid(resourceList.size(), 3);

		int row = 0;

		for (Resource resource : resourceList) {
			renderIconCell(grid, resource, row, 0);
			renderNameCell(grid, resource, row, 1);
			renderValueCell(grid, resource, row, 2);
			row++;
		}
		render(grid);
	}

	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}

	protected void render(Widget widget) {
		data.clear();
		data.add(widget);
	}

	protected void renderIconCell(Grid grid, Resource resource, int row, int column) {
		grid.setWidget(row, 0,new HTML("<div>" + Icons.forQualifier(resource.getQualifier()).getHTML() + "</div>"));
		grid.getCellFormatter().setStyleName(row, 0, getRowCssClass(row, resource.getId()));
	}

	protected void renderNameCell(Grid grid, final Resource resource, int row,
			int column) {
		Anchor link = new Anchor(resource.getName());

		link.getElement().setAttribute("title", resource.getName(true));
	    link.getElement().setAttribute("rel", resource.getName(true));
		
		link.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
		
				if(requestQuery != null)
					Window.Location.assign(Links.urlForResourcePage(Configuration.getResourceId(), "jku.se.drilldown.qm.page.QMDrilldownPage", requestQuery+"="+resource.getId()));
				else
					;
				//Window.Location.assign(Links.baseUrl() + "/plugins/resource/" + Configuration.getResourceId() + "?page=jku.se.drilldown.qm.page.QMDrilldownPage&depId="+resource.getId());	
			}
		});

		grid.setWidget(row, column, link);
		grid.getCellFormatter().setStyleName(row, column, getRowCssClass(row, resource.getId()));
	}

	protected void renderValueCell(Grid grid, Resource resource, int row, int column) {
		grid.setHTML(row, column, resource.getMeasureValue(Metrics.VIOLATIONS).toString());
		grid.getCellFormatter().setStyleName(row, column,getRowCssClass(row, resource.getId()));
	}

	protected String getRowCssClass(int row, int resourcekey) {
		return row % 2 == 0 ? "even" + ifRowSelected(resourcekey) : "odd" + ifRowSelected(resourcekey);
	}

	private String ifRowSelected(int key) {
		return key == selectedResourceId ? " selected" : "";
	}
}