package jku.se.drilldown.qm.client;


import jku.se.drilldown.qm.client.ui.QualityModelList;

import org.sonar.gwt.ui.Page;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigPanel extends Page{
	
	
	@Override
	protected Widget doOnModuleLoad() {
		
	    final VerticalPanel panel = new VerticalPanel();
       
	    panel.add(new QualityModelList());
	    
	    return panel;
	}
}