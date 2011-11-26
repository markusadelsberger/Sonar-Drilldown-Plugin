package jku.se.drilldown.qm.client.ui;

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
	
	public PathComponent(DrilldownController drilldownController)
	{
		this.drilldownController = drilldownController;
		pathInformation = new Grid(1,5);
		
		initWidget(pathInformation);	
	}
	
	@Override
	public void onLoad() {
		loadData();
	}
	
	public void loadData()
	{
		pathInformation.setWidget(0, 0, new Label("Path: "));
		pathInformation.setWidget(0, 1, new Label("Any severty >> "));
		pathInformation.setWidget(0, 2, new Label("Any rule >> "));
		pathInformation.setWidget(0, 3, new Label(" "));
		pathInformation.setWidget(0, 4, new Label(" "));
	}
	
	public void onClick(ClickEvent event) {			
		Element element = event.getRelativeElement();
		
		String clearItem = element.getAttribute("clearItem");
		
		drilldownController.clearElement(clearItem);		
	}

	/*
	public void onSelectedItemChanged(String component) {
		
		if (component.equals("rule"))
		{
			Measure selectedMeasure = ruleList.getSelectedItem();
			//structureDrilldown.reloadLists(selectedMeasure);
			
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(new Label(selectedMeasure.getRuleName()));
			
			Anchor link = new Anchor("Clear");
			link.getElement().setAttribute("clearItem", "rule");
			link.addClickHandler(this);
			panel.add(link);
			
	    	pathInformation.setWidget(0, 2, panel);
		} 
		else if(component.equals("structure"))
		{
			Resource selectedModule = structureDrilldown.getSelectedModule();
			Resource selectedPackage = structureDrilldown.getSelectedPackage();
			
			if(selectedModule != null)
			{
				HorizontalPanel panel = new HorizontalPanel();
				panel.add(new Label(selectedModule.getName()));
				
				Anchor link = new Anchor("Clear");
				link.getElement().setAttribute("clearItem", "module");
				link.addClickHandler(this);
				panel.add(link);
				
		    	pathInformation.setWidget(0, 3, panel);
			}
			
			if (selectedPackage != null)
			{
				HorizontalPanel panel = new HorizontalPanel();
				panel.add(new Label(selectedPackage.getName()));
				
				Anchor link = new Anchor("Clear");
				link.getElement().setAttribute("clearItem", "package");
				link.addClickHandler(this);
				panel.add(link);
				
		    	pathInformation.setWidget(0, 4, panel);
			}
		}
		else if(component.equals("severety")){
			List<Measure> measureList = severetyDrilldown.getSelectedItem();
			ruleList.reloadBegin();
			ruleList.addMeasures(measureList);
			ruleList.reloadFinished();
			
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(new Label("Severity"));
			
			Anchor link = new Anchor("Clear");
			link.getElement().setAttribute("clearItem", "rule");
			link.addClickHandler(this);
			panel.add(link);
			
	    	pathInformation.setWidget(0, 1, panel);
			
		}
	}
	*/
	
	public void setElement(String label, int column, String category){
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label(label));
		
		if(category!=null){
			Anchor link = new Anchor("Clear");
			link.getElement().setAttribute("clearItem", category);
			link.addClickHandler(this);
			panel.add(link);
		}
		
    	pathInformation.setWidget(0, column, panel);
	}
}
