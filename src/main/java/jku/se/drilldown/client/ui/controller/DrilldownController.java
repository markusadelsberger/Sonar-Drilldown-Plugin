package jku.se.drilldown.client.ui.controller;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.DrilldownComponent;

/**
 * @author markus
 * Implements the Controller of the Drilldown MVC Principle
 */
public class DrilldownController implements IComponentController{

	private DrilldownModel drilldownModel;
	private List<DrilldownComponent> listenerList = new LinkedList<DrilldownComponent>();
	
	/**
	 * To instantiate the controller a model must be given; the controller must be instantiated before the views,
	 * as the views register with the view via the super-Constructor in DrilldownComponent
	 * @param drilldownModel A DrilldownModel-Object, if it isn't given the NullPointerException will be thrown
	 * @throws NullPointerException
	 */
	public DrilldownController (DrilldownModel drilldownModel) throws NullPointerException{
		if(drilldownModel == null) {
			throw new NullPointerException();
		}
		else {
			this.drilldownModel=drilldownModel;
		}
	}
	
	/**
	 * Adds the given DrilldownComponent to the list that is notified at a reload
	 * @param drilldownComponent
	 */
	public void addListener(DrilldownComponent drilldownComponent) {
		listenerList.add(drilldownComponent);
	}
	
	/**
	 * Is used to notify the controller that the data in the model has changed and 
	 * the views should be notified
	 * @param component The component that called the method;
	 */
	public void onSelectedItemChanged(ViewComponents component) {
		for(DrilldownComponent listener : listenerList){
			listener.reload(component);
		}
	}
	
	/**
	 * Resets the selected element
	 * @param component The component that called the method;
	 */
	public void clearElement(ViewComponents component){
		
		switch(component){
			case SEVERETYDRILLDOWN:
				drilldownModel.setActiveElement("Severety", null);
				drilldownModel.setActiveMeasures(null);
			break;
		
			case RULEDRILLDOWN:
				drilldownModel.setActiveMeasure(null);
			break;
		
			case MODULELIST:
				drilldownModel.setSelectedItem(ViewComponents.MODULELIST, null);
			break;
			
			case PACKAGELIST:
				drilldownModel.setSelectedItem(ViewComponents.PACKAGELIST, null);
			break;
			
			case QMTREE:
				drilldownModel.setActiveElement("qmtreeNode", null);
				drilldownModel.setActiveMeasures(null);
			break;
			
			case BENCHMARKDRILLDOWN:
				drilldownModel.setActiveElement("Benchmark", null);
				drilldownModel.setActiveMeasures(null);
			break;
			
			default: break;
		}
		
		onSelectedItemChanged(component);
	}

	/**
	 * Gets the used Model
	 * @return The Model in use;
	 */
	public DrilldownModel getModel(){
		return drilldownModel;
	}
}
