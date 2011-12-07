package jku.se.drilldown.client.ui.view;

import com.google.gwt.user.client.ui.Composite;

public abstract class DrilldownComponent extends Composite{
	
	 /**
	 * Should be used to reload the data from the model and rerender it in the view.
	 * Will be called by the Controller if the Data has changed.
	 */
	public abstract void reload();
	
}