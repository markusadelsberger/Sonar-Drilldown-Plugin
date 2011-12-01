package jku.se.drilldown.client;


import jku.se.drilldown.client.ui.QualityModelList;

import org.sonar.gwt.ui.Page;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigPanel extends Page{
	
 
	
	@Override
	protected Widget doOnModuleLoad() {
		
	    final VerticalPanel panel = new VerticalPanel();
       
	    
	    panel.add(new QualityModelList());
	   
	    panel.add(new Label("2"));
	    
	    return panel;
	}
}