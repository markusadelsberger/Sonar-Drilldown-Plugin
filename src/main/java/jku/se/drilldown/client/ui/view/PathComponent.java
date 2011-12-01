package jku.se.drilldown.client.ui.view;

import jku.se.drilldown.client.ui.controller.DrilldownController;

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
	
	public void reload()
	{
		;
	}
}
