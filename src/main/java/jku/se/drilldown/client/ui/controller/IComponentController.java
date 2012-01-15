package jku.se.drilldown.client.ui.controller;

import jku.se.drilldown.client.ui.model.ViewComponents;

/**
 * Interface defines methods a drilldown controller has to implement. 
 * 
 * @author Johannes
 */
public interface IComponentController {
	
	/**
	 * Is used to notify the controller that the data in the model has changed and 
	 * the views should be notified
	 * 
	 * @param component The component that called the method;
	 */
	public void onSelectedItemChanged(ViewComponents component);
	
	/**
	 * Resets the selected element
	 * @param component The component that called the method;
	 */
	public void clearElement(ViewComponents component);
}
