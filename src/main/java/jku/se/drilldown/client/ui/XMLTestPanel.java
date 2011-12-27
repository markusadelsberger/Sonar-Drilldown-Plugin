package jku.se.drilldown.client.ui;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.view.BenchmarkDrilldown;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class XMLTestPanel extends Page{
	DrilldownController drilldownController;
	DrilldownModel drilldownModel;
	BenchmarkDrilldown benchmarkDrilldown;
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel verticalPanel = new VerticalPanel();
		try{
			drilldownController = new DrilldownController();
			drilldownModel = new DrilldownModel();
			drilldownModel.setResource(resource);
			drilldownController.setModel(drilldownModel);
			drilldownController.setResource(resource);
			benchmarkDrilldown = new BenchmarkDrilldown(drilldownController);
			drilldownController.setBenchmarkDrilldown(benchmarkDrilldown);
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);

			verticalPanel.add(benchmarkDrilldown);
		}catch(Exception e){
			verticalPanel.add(new Label(e.toString()));
		}
		return verticalPanel;
	}
	
	
	
}