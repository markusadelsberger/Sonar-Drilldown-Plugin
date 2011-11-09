package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.ui.DrilldownComponentRuleList;
import jku.se.drilldown.ui.QuantilDrilldown;
import jku.se.drilldown.ui.QuantilGraphic;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page {
	protected Widget doOnResourceLoad(Resource resource) {
		final VerticalPanel panel = new VerticalPanel();
		
		//Create the 3 Objects needed in the Benchmark Drilldown
		//2011-11-09: Maven compains about missing sources -> currently not used
		//QuantilDrilldown quantilDrilldown = new QuantilDrilldown();
		//DrilldownComponentRuleList ruleList = new DrilldownComponentRuleList(fetchMeasures(resource));
		//QuantilGraphic quantilGraphic = new QuantilGraphic();
		Label quantilDrilldown = new Label("quantilDrilldown");
		Label ruleList = new Label("ruleList");
		Label quantilGraphic = new Label("quantilGraphic");
		
		
		//Create the right VerticalPanel with the ruleList and the quantilGraphic
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(ruleList);
		verticalPanel.add(quantilGraphic);
		
		//Create the HorizontalSplitPanel (Left = quantilDrilldown, Right = ruleList & quantilGraphic
		HorizontalPanel horizontalSplitPanel = new HorizontalPanel();
		horizontalSplitPanel.add(quantilDrilldown);
		horizontalSplitPanel.add(verticalPanel);
		
		panel.add(horizontalSplitPanel);
		return panel;
		
	}
	
	private List<Measure> fetchMeasures(Resource resource){
		final List<Measure> measureList = new LinkedList<Measure>();
		ResourceQuery r_query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
				.setDepth(0)
				.setExcludeRules(false);

		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				if (resource==null || resource.getMeasures().isEmpty()) {
					;
				} else {
					//TODO: Vorläufige implementierung
					for (Measure measure : resource.getMeasures()) {
						measureList.add(measure);
					}
				}
			}
		});
		return measureList;
	}
}