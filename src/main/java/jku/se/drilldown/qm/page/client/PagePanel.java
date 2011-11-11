package jku.se.drilldown.qm.page.client;

import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import jku.se.drilldown.qm.page.client.I18nConstants;

public class PagePanel extends Page{
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		
	    final VerticalPanel panel = new VerticalPanel();

	    panel.setWidth("100%");
	    
	    panel.add( new Label(I18nConstants.INSTANCE.sample()));
	    panel.add( new StructureDrilldownComponent(resource));
	    
	    return panel;
	}

}
