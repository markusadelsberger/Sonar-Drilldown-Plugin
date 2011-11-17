package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.List;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class QuantilDrilldown extends DrilldownComponentList<Resource> {

	private String pageID;
	private Resource resource;
	private String scope;
	private Measure selectedMeasure;
	private StructureDrilldownList next;
	private StructureDrilldownList prev;

	public QuantilDrilldown(Resource resource, String scope, ClickHandler clickHandler, String pageID) {
		super(clickHandler);

		this.resource=resource;
		this.pageID = pageID;
		this.scope=scope;

		this.selectedMeasure=null;
		this.next=null;
		this.prev=null;

	}
	
	@Override
	public Widget createHeader() {
		return new Label("");
	}


	@Override
	public int gridColumnCount() {
		return 4;
	}
	
	
	@Override
	public void renderRow(Resource item, int row) {
		//renderIconCells(item, row);
		//renderNameCell( item, row, 2);
		//renderValueCell( item, row, 3);
	}

	@Override
	public void doLoadData()
	{
		setGrid(new Grid(5,3));
		getGrid().setWidget(0, 0, new HTML(Icons.get().priorityBlocker().getHTML()));			
		getGrid().setWidget(1, 0, new HTML(Icons.get().priorityCritical().getHTML()));		
		getGrid().setWidget(2, 0, new HTML(Icons.get().priorityMajor().getHTML()));
		getGrid().setWidget(3, 0, new HTML(Icons.get().priorityMinor().getHTML()));
		getGrid().setWidget(4, 0, new HTML(Icons.get().priorityInfo().getHTML()));
	}
	
	protected void addMeasures(int row, int violations){
		getGrid().setText(row, 2, String.valueOf(violations));
	}
	
	protected void reloadFinished(){
		render(getGrid());
	}

	@Override
	public String getItemIdentifier(Resource item) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDrilldownAnchor(String name, int row, List<Measure> measures){
		Anchor a = new Anchor(name);
		a.getElement().setPropertyObject("assignedList", measures);
		if(getClickHandler() != null)
			a.addClickHandler(getClickHandler());
		getGrid().setWidget(row, 1, a);
	}
}