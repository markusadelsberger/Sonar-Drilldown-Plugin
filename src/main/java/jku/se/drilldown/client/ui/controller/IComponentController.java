package jku.se.drilldown.client.ui.controller;

/**
 * 
 * @author Johannes
 *
 */
public interface IComponentController {
	
	/**
	 * Method is invoked when something changed in a component. 
	 * As a consequence it synchronizes the other components. 
	 * 
	 * @param component Defines in which component the item changed. 
	 */
	public void onSelectedItemChanged(String component);
}
