package jku.se.drilldown.client.ui;

import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.XMLExtractor;
import jku.se.drilldown.client.ui.view.BenchmarkDrilldown;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.XMLParser;

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
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			benchmarkDrilldown = new BenchmarkDrilldown(drilldownController);
			
			
			verticalPanel.add(benchmarkDrilldown);
		}catch(Exception e){
			verticalPanel.add(new Label(e.toString()));
		}
		return verticalPanel;
	}
	
	
	
}