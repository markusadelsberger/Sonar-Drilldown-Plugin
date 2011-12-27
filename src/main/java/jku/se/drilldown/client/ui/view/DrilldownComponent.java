package jku.se.drilldown.client.ui.view;

import com.google.gwt.user.client.ui.Composite;

public abstract class DrilldownComponent extends Composite{
	
	/**
	 * Method is entry point to reload component which means to reload the data from the model and rerender it in the view.
	 * During the creation of the component a loading icon shows that the component is under work.  
	 */
	public abstract void reload();
	
}