package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.ui.client.DrilldownComponentList;


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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page {

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
		try{
			panel.add(new RuleDrilldownComponent(resource));
		}catch (Exception e){
			panel.add(new Label("BenchmarkViewerPanel: "+e.toString()));
		}
		
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
					}
				}
			}
		});
		return measureList;
	}
}