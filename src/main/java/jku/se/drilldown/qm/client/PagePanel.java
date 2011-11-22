package jku.se.drilldown.qm.client;


import jku.se.drilldown.qm.client.ui.PathComponent;
import jku.se.drilldown.qm.client.ui.RuleDrilldownComponent;
import jku.se.drilldown.qm.client.ui.StructureDrilldownComponent;

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
	    
	    PathComponent controller = new PathComponent();
	    StructureDrilldownComponent struct = new StructureDrilldownComponent(resource,"jku.se.drilldown.qm.page.QMDrilldownPage",controller);
	    RuleDrilldownComponent comp = new RuleDrilldownComponent(resource, controller);
	    controller.setStructureDrilldownComponent(struct);
	    
	    panel.add(comp);
	    panel.add(struct);
	    panel.add(controller);
	    return panel;
	}

}
