package jku.se.drilldown.client.ui;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.BenchmarkDrilldown;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;
import jku.se.drilldown.client.ui.view.PathComponent;
import jku.se.drilldown.client.ui.view.QuantilGraphic;
import jku.se.drilldown.client.ui.view.StructureDrilldownComponent;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page{

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
			
		try{
			HorizontalPanel mainPanel= new HorizontalPanel();
			Panel rightPanel=new VerticalPanel();
			Panel leftPanel=new HorizontalPanel();
			
			DrilldownController drilldownController = new DrilldownController(new DrilldownModel(resource));
				
			PathComponent pathComponent = new PathComponent(drilldownController, ViewComponents.BENCHMARKDRILLDOWN);
			
			BenchmarkDrilldown benchmarkDrilldown=new BenchmarkDrilldown(drilldownController);
			benchmarkDrilldown.setWidth("175px");
			
			DrilldownComponentRuleList drilldownComponentRuleList=new DrilldownComponentRuleList(drilldownController);
			drilldownComponentRuleList.setWidth("100%");
			
			StructureDrilldownComponent structureComponent= new StructureDrilldownComponent(drilldownController, "jku.se.drilldown.BenchmarkPage");
			
			QuantilGraphic quantilGraphic = new QuantilGraphic(drilldownController);
			quantilGraphic.setWidth("100%");
			quantilGraphic.setHeight("170px");
			
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
				
			leftPanel.add(benchmarkDrilldown);
			leftPanel.setWidth("175px");
			rightPanel.add(drilldownComponentRuleList);
			rightPanel.add(quantilGraphic);
			rightPanel.setWidth("100%");
			
			mainPanel.add(leftPanel);
			mainPanel.add(rightPanel);
			mainPanel.setWidth("100%");
			mainPanel.setCellWidth(leftPanel, "200px");
			mainPanel.setCellWidth(rightPanel, "100%");
			panel.add(mainPanel);

			panel.add(structureComponent);
			panel.add(pathComponent);
			panel.setWidth("100%");
		
		} catch(NullPointerException e) {
			panel.add(new Label("DrilldownController initialized with null. \n Error message: "+e.getMessage()));
		}	
		
		return panel;
	}	
}