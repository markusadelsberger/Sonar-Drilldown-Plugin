package jku.se.drilldown.client.ui.view;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

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
 * Additionally it displays items of components which are selected. 
 * 
 * @author Johannes
 *
 */
public class PathComponent extends DrilldownComponent implements ClickHandler{

	private Grid pathInformation;
	
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	private ViewComponents drilldownComponent;
	private String[] labels = {"Path: ","Any severty >> ","Any rule >> ", " ", " ","Any QM Node >> ", "Any Benchmark Quantil >> "};

	public PathComponent(DrilldownController drilldownController, ViewComponents drilldownComponent){
		super(drilldownController);
		
		this.drilldownController = drilldownController;
		this.drilldownModel=drilldownController.getModel();
		this.drilldownComponent = drilldownComponent;
		
		pathInformation = new Grid(1,5);
		
		initWidget(pathInformation);	
	}
	
	@Override
	public void onLoad() {
		loadData();
	}
	
	public void loadData()
	{
		pathInformation.setWidget(0, 0, new Label(labels[0]));
		
		reload(ViewComponents.INITIALIZE);
	}
	
	public void onClick(ClickEvent event) {			
		
		Element element = event.getRelativeElement();
		
		ViewComponents clearItem = (ViewComponents)element.getPropertyObject("clearItem");
		
		drilldownController.clearElement(clearItem);		
	}

	private void setElement(String label, int column, ViewComponents viewComponent){
		
		if(label == null) {
			if(column>(pathInformation.getColumnCount()-1)) {	
				label = labels[1];
			} else {
				label = labels[column];
			}
		}
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label(label));		
		
		if(viewComponent!=null) {
			Anchor link = new Anchor("Clear");
			link.getElement().setPropertyObject("clearItem", viewComponent);
			link.addClickHandler(this);
			panel.add(link);
		}
		
    	pathInformation.setWidget(0, column, panel);
		pathInformation.getCellFormatter().setStyleName(0, column, "even");
	}
	
	@Override
	public void reload(ViewComponents viewComponent)
	{
		String label = null;
		int labelIndex = 1;
		
		switch (drilldownComponent) {
			case SEVERETYDRILLDOWN:
				
				setElement(drilldownModel.getActiveElement("Severety"),1,ViewComponents.SEVERETYDRILLDOWN);
				break;
				
			case QMTREE: 
				
				if(drilldownModel.getActiveElement("qmtreeNode") == null) {
					labelIndex = 5;
				}
				
				setElement(drilldownModel.getActiveElement("qmtreeNode"),labelIndex,ViewComponents.QMTREE);
				break;
				
			case BENCHMARKDRILLDOWN:
					
				if(drilldownModel.getActiveElement("benchmark") == null) {
					labelIndex = 6;
				}
				
				setElement(drilldownModel.getActiveElement("benchmark"),labelIndex,ViewComponents.BENCHMARKDRILLDOWN);
				break;
			default: 
				break;
		}
		
	
		label = null;
		if(drilldownModel.getActiveMeasure()!=null) {
			label = drilldownModel.getActiveMeasure().getRuleName();
		}
			
		setElement(label,2,ViewComponents.RULEDRILLDOWN);
		
		label = null;
		if(drilldownModel.getSelectedItem(ViewComponents.MODULELIST)!=null) {
			label = drilldownModel.getSelectedItem(ViewComponents.MODULELIST).getName();
		}
			
		setElement(label,3,ViewComponents.MODULELIST);
		
		label = null;
		if(drilldownModel.getSelectedItem(ViewComponents.PACKAGELIST)!=null) {
			label = drilldownModel.getSelectedItem(ViewComponents.PACKAGELIST).getName();
		}
			
		setElement(label,4,ViewComponents.PACKAGELIST);
	}
}
