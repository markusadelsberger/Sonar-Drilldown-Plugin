package jku.se.drilldown.qm.client;


import jku.se.drilldown.qm.client.ui.QualityModelList;

import org.sonar.gwt.ui.Page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigPanel extends Page{
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	 
	
	@Override
	protected Widget doOnModuleLoad() {
		
	    final VerticalPanel panel = new VerticalPanel();
       
	    
	    panel.add(new QualityModelList());
	    
	    greetingService.greetServer("hallo", new AsyncCallback<String>(){

			public void onFailure(Throwable caught) {
				panel.add(new Label("fail"+caught.getMessage()));
				
			}

			public void onSuccess(String result) {
				panel.add(new Label(result));
				
			}
	    	
	    });
	    panel.add(new Label("2"));
	    
	    return panel;
	}
}