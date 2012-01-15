package jku.se.drilldown.client.ui.view;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.ViewComponents;

import com.google.gwt.user.client.ui.Composite;

public abstract class DrilldownComponent extends Composite{
	
	public DrilldownComponent(DrilldownController drilldownController){
		drilldownController.addListener(this);
	}
	
	/**
	 * Method is entry point to reload component. 
	 * This means to reload the data from the model and rerender it in the view.
	 * During the creation of the component a loading icon shows that the component is under work.  
	 */
	public abstract void reload(ViewComponents viewComponent);
	
}