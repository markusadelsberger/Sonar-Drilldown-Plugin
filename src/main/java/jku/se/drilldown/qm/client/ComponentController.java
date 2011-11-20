package jku.se.drilldown.qm.client;

/**
 * 
 * @author Johannes
 *
 */
public interface ComponentController {
	
	/**
	 * Method is invoked when something changed in a component. 
	 * As a consequence it synchronizes the other components. 
	 * 
	 * @param component Defines in which component the item changed. 
	 */
	public void onSelectedItemChanged(String component);
}
