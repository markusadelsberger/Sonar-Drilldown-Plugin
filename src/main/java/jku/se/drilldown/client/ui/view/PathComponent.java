package jku.se.drilldown.client.ui.view;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The PathComponent is the controller of different DrilldownComponents. 
 * If for example a selected item of a DrilldownComponentList changes this controller will be triggered 
 * and it refresh the other components. 
 * 
 * Additionally it displays selected items of components. 
 * 
 * @author Johannes
 *
 */
public class PathComponent extends DrilldownComponent implements ClickHandler{

	private Grid pathInformation;
	
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	private String[] labels = {"Path: ","Any severty >> ","Any rule >> ", " ", " "};

	public PathComponent(DrilldownController drilldownController)
	{
		this.drilldownController = drilldownController;
		this.drilldownModel=drilldownController.getModel();
		pathInformation = new Grid(1,5);
		
		initWidget(pathInformation);	
	}
	
	@Override
	public void onLoad() {
		loadData();
	}
	
	public void loadData()
	{
		for(int i =0;i<5;i++){
			pathInformation.setWidget(0, i, new Label(labels[i]));
		}
	}
	
	public void onClick(ClickEvent event) {			
		Element element = event.getRelativeElement();
		
		ViewComponents clearItem = (ViewComponents)element.getPropertyObject("clearItem");
		
		drilldownController.clearElement(clearItem);		
	}

	private void setElement(String label, int column, ViewComponents category){
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label(label));
		
		if(category!=null){
			Anchor link = new Anchor("Clear");
			link.getElement().setPropertyObject("clearItem", category);
			link.addClickHandler(this);
			panel.add(link);
		}
		
    	pathInformation.setWidget(0, column, panel);
	}
	
	public void reload()
	{
		String severety = drilldownModel.getActiveElement("Severety");
		if(severety!=null){
			this.setElement(severety, 1, ViewComponents.SEVERETYDRILLDOWN);
		}else{
			this.setElement(labels[1], 1, null);
		}
		
		Measure activeMeasure = drilldownModel.getActiveMeasure();
		if(activeMeasure!=null){
			this.setElement(activeMeasure.getRuleName(), 2, ViewComponents.RULEDRILLDOWN);
		}else{
			this.setElement(labels[2], 2, null);
		}
		
		Resource selectedModule = drilldownModel.getSelectedItem(ViewComponents.MODULELIST);
		if(selectedModule!=null){
			this.setElement(selectedModule.getName(), 3, ViewComponents.MODULELIST);
		}else{
			this.setElement(labels[3], 3, null);
		}
		
		Resource selectedPackage = drilldownModel.getSelectedItem(ViewComponents.PACKAGELIST);
		if(selectedPackage!=null){
			this.setElement(selectedPackage.getName(), 4, ViewComponents.PACKAGELIST);
		}else{
			this.setElement(labels[4], 4, null);
		}
	}
}
