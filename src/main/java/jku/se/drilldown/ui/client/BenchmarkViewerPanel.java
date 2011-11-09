package jku.se.drilldown.ui.client;

import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page {
	protected Widget doOnResourceLoad(Resource resource) {
		final VerticalPanel panel = new VerticalPanel();
		Label label = new Label("Hello World");
		panel.add(label);
		return panel;
		
	}
}