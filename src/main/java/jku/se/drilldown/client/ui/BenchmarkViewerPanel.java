package jku.se.drilldown.client.ui;

import java.util.ArrayList;
import java.util.List;

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
	private DrilldownController drilldownController;
	private BenchmarkDrilldown benchmarkDrilldown;
	private HorizontalPanel mainPanel;
	private Panel rightPanel;
	private Panel leftPanel;
	private PathComponent pathComponent;
	private DrilldownComponentRuleList drilldownComponentRuleList;
	private StructureDrilldownComponent structureComponent;
	private QuantilGraphic quantilGraphic;

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
			
		try{
			mainPanel= new HorizontalPanel();
			rightPanel=new VerticalPanel();
			leftPanel=new HorizontalPanel();
			
			drilldownController = new DrilldownController(new DrilldownModel(resource));
				
			pathComponent = new PathComponent(drilldownController, ViewComponents.BENCHMARKDRILLDOWN);
			
			benchmarkDrilldown=new BenchmarkDrilldown(drilldownController);
			benchmarkDrilldown.setWidth("175px");
			
			drilldownComponentRuleList=new DrilldownComponentRuleList(drilldownController);
			drilldownComponentRuleList.setWidth("100%");
			
			structureComponent= new StructureDrilldownComponent(drilldownController, "jku.se.drilldown.BenchmarkPage");
			
			quantilGraphic = new QuantilGraphic(drilldownController);
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