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
		getGrid().setText(0, 1, "Blocker");	
		//getGrid().setWidget(0, 1, getAnchor("Blocker", "resourceObj", resource));	
		
		getGrid().setWidget(1, 0, new HTML(Icons.get().priorityCritical().getHTML()));
		getGrid().setText(1, 1, "Critical");	
		//getGrid().setWidget(1, 1, getAnchor("Critical", "resourceObj", resource));	
		
		getGrid().setWidget(2, 0, new HTML(Icons.get().priorityMajor().getHTML()));
		getGrid().setText(2, 1, "Major");	
		//getGrid().setWidget(2, 1, getAnchor("Major", "resourceObj", resource));	

		getGrid().setWidget(3, 0, new HTML(Icons.get().priorityMinor().getHTML()));
		getGrid().setText(3, 1, "Minor");	
		//getGrid().setWidget(3, 1, getAnchor("Minor", "resourceObj", resource));	

		getGrid().setWidget(4, 0, new HTML(Icons.get().priorityInfo().getHTML()));
		getGrid().setText(4, 1, "Info");	
		
		//render(getGrid());
		
		//getGrid().setWidget(4, 1, getAnchor("Info", "resourceObj", resource));
		
		/*
		Sonar.getInstance().find(getQuery(), new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				List<Measure>measureList = resource.getMeasures();
				setGrid(new Grid(measureList.size(), gridColumnCount()));

				HashMap<String,Integer> hashmap= new HashMap<String,Integer>();

				int i = 0;
				for (Measure measure : measureList)
				{
					getGrid().setText(0, i, measure.getRuleSeverity());
					i++;
				}

				setHashmap(hashmap);

				if(containsSelectedItem())
					selectRow(hashmap.get(getItemIdentifier(getSelectedItem())));

				render(getGrid());

				if(next!=null)
					next.loadData();
			}

		});*/
	}
	
	private Anchor getAnchor(String title, String resourceName, Resource resource){
		Anchor link = new Anchor(title);
		// add resource object to link element
		link.getElement().setPropertyObject("resourceObj", resource);

		// register listener
		if(getClickHandler() != null)
			link.addClickHandler(getClickHandler());
		
		return link;
		
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

}