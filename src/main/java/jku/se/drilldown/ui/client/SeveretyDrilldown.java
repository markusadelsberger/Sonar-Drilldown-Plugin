package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

	public SeveretyDrilldown(DrilldownController controller) {
		super();
		this.controller=controller;
		drilldownModel=controller.getModel();
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
	}
	
	public void addMeasures(int row, int violations){
		getGrid().setText(row, 2, String.valueOf(violations));
	}
	
	public void reloadFinished(){
		render(getGrid());
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

	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		String severety = element.getInnerText();
		drilldownModel.setActiveElement("Severety", severety);
		controller.onSelectedItemChanged("severety");
	}
	
	public void reload(){
		addMeasures(0, drilldownModel.getCount("Blocker"));
		addMeasures(1, drilldownModel.getCount("Critical"));
		addMeasures(2, drilldownModel.getCount("Major"));
		addMeasures(3, drilldownModel.getCount("Minor"));
		addMeasures(4, drilldownModel.getCount("Info"));
		
		addDrilldownAnchor("Blocker", 0);
		addDrilldownAnchor("Critical", 1);
		addDrilldownAnchor("Major", 2);
		addDrilldownAnchor("Minor", 3);
		addDrilldownAnchor("Info", 4);
	}
}