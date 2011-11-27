package jku.se.drilldown.ui.client;


import java.util.List;

import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.services.Measure;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class SeveretyDrilldown extends DrilldownComponentList<List<Measure>> {

	private DrilldownController controller;
	private DrilldownModel drilldownModel;
	private String[] severeties;

	public SeveretyDrilldown(DrilldownController controller) {
		super();
		this.controller=controller;
		drilldownModel=controller.getModel();
		severeties = new String[5];
		severeties[0]="Blocker";
		severeties[1]="Critical";
		severeties[2]="Major";
		severeties[3]="Minor";
		severeties[4]="Info";
	}
	
	@Override
	public Widget createHeader() {
		return new Label("Severety");
	}


	@Override
	public int gridColumnCount() {
		return 4;
	}
	
	
	@Override
	public void renderRow(List<Measure> item, int row) {
		//renderIconCells(item, row);
		//renderNameCell( item, row, 2);
		//renderValueCell( item, row, 3);
	}

	@Override
	public void doLoadData()
	{
		setGrid(new Grid(5,gridColumnCount()));
		getGrid().setWidget(0, 0, new HTML(Icons.get().priorityBlocker().getHTML()));	
		getGrid().setWidget(1, 0, new HTML(Icons.get().priorityCritical().getHTML()));		
		getGrid().setWidget(2, 0, new HTML(Icons.get().priorityMajor().getHTML()));
		getGrid().setWidget(3, 0, new HTML(Icons.get().priorityMinor().getHTML()));
		getGrid().setWidget(4, 0, new HTML(Icons.get().priorityInfo().getHTML()));
		
		getGrid().getRowFormatter().setStyleName(0, getRowCssClass(0, false));
		getGrid().getRowFormatter().setStyleName(1, getRowCssClass(1, false));
		getGrid().getRowFormatter().setStyleName(2, getRowCssClass(2, false));
		getGrid().getRowFormatter().setStyleName(3, getRowCssClass(3, false));
		getGrid().getRowFormatter().setStyleName(4, getRowCssClass(4, false));
		
		getGrid().getColumnFormatter().setWidth(4, "70px");
	}
	
	public void addMeasures(int row, int violations){
		getGrid().setText(row, 2, String.valueOf(violations));
	}

	@Override
	public String getItemIdentifier(List<Measure> item) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDrilldownAnchor(String name, int row){
		Anchor a = new Anchor(name);
		a.getElement().setId(name);
		a.addClickHandler(this);
		getGrid().setWidget(row, 1, a);
	}
	
	public double getGraphWidth(String severety){
		Integer totalCount = drilldownModel.getCount("SeveretyTotal");
		Integer severetyCount = drilldownModel.getCount(severety);
		if(severetyCount!=null && totalCount!=null){
			return (severetyCount.doubleValue()/totalCount.doubleValue())*100;
		}else{
			return -1D;
		}
	}
	
	public void addGraph(String severety, int row){
		double width = getGraphWidth(severety);
		HTML bar = new HTML("<div class='barchart' style='width: 60px'><div style='width: "+String.valueOf(width)+"%;background-color:#777;'></div></div>");
		getGrid().setWidget(row, 3, bar);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		String severety = element.getInnerText();
		drilldownModel.setActiveElement("Severety", severety);
		controller.onSelectedItemChanged("severety");
	}
	
	public void reload(){
		for(int i=0;i<5;i++){
			addMeasures(i, drilldownModel.getCount(severeties[i]));
			addDrilldownAnchor(severeties[i], i);
			addGraph(severeties[i], i);
		}
		render(getGrid());
	}
}