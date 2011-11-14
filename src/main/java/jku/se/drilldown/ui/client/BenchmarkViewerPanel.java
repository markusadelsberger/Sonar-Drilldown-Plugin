package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;


import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page {
	private HorizontalPanel mainPanel;
	public static final int LIMIT = 5;
	private Panel drilldownGrid;
	private Panel ruleDrilldown;

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Drilldown"));
		
		mainPanel = new HorizontalPanel();
		Label l;
		//List<Measure> measureList = fetchMeasureList(resource, Metrics.VIOLATIONS, 0, false);
		try{
			ruleDrilldown = new DrilldownComponentList(resource, Metrics.VIOLATIONS, 0, false);
			l = new Label(ruleDrilldown.toString());
		}catch(Exception e){
			l = new Label("In BenchmarkViewerPanel: "+e.toString()+", "+e.getCause().toString());
		}
		if(ruleDrilldown!=null){
			panel.add(ruleDrilldown);
		}
		
		
		//drilldownGrid = new QuantilDrilldown(resource, Metrics.VIOLATIONS, -1, false);
		
		
		mainPanel.add(l);
		//mainPanel.add(l);
		panel.add(mainPanel);

		return panel;

	}
	
	public List<Measure> fetchMeasureList(Resource resource, String metrics, int depth, boolean excludeRules){
		final List<Measure> measureList = new LinkedList<Measure>();
		ResourceQuery r_query = ResourceQuery.createForResource(resource, metrics)
				.setDepth(depth)
				.setExcludeRules(excludeRules);
		
		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource resource) {
				if (resource!=null && !resource.getMeasures().isEmpty()) {
					for (Measure measure : resource.getMeasures()) {
						measureList.add(measure);
						//ruleDrilldownGrid.setText(i, 0, measure.getCharacteristicKey());
						//ruleDrilldownGrid.setText(i, 1, measure.getRuleName());
						//ruleDrilldownGrid.setText(i, 2, String.valueOf(measure.getValue()));
					}
				}
			}
		});
		return measureList;
	}
}