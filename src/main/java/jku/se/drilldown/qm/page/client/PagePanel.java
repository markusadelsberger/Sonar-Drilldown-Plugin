package jku.se.drilldown.qm.page.client;


import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Johannes
 *
 */ 
public class PagePanel extends Page{
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		
	    final VerticalPanel panel = new VerticalPanel();

	    panel.setWidth("100%");
	    
	    panel.add( new Label(I18nConstants.INSTANCE.sample()));
	    
	    PathComponent controller = new PathComponent();
	    MeasuresList ruleList = new MeasuresList(resource,controller);
	    StructureDrilldownComponent struct = new StructureDrilldownComponent(resource,"jku.se.drilldown.qm.page.QMDrilldownPage",controller);
	       
	    
	    controller.setRuleList(ruleList);
	    controller.setStructureDrilldownComponent(struct);
	    
	    panel.add(ruleList);
	    panel.add(struct);
	    panel.add(controller);
	    
	    return panel;
	}

}
