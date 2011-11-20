package jku.se.drilldown.qm.client;

import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigPanel extends Page{
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		
	    final VerticalPanel panel = new VerticalPanel();

	    panel.setWidth("100%");
	    
	    panel.add( new Label("Test"));
	    
	    
	    return panel;
	}
}
