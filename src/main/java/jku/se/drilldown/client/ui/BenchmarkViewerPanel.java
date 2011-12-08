package jku.se.drilldown.client.ui;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;
import jku.se.drilldown.client.ui.view.PathComponent;
import jku.se.drilldown.client.ui.view.SeveretyDrilldown;
import jku.se.drilldown.client.ui.view.StructureDrilldownComponent;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author markus
 */
public class BenchmarkViewerPanel extends Page {
	private HorizontalPanel mainPanel;
	private Panel rightPanel;
	private Panel leftPanel;
	private PathComponent pathComponent;
	private SeveretyDrilldown severetyDrilldown;
	private DrilldownComponentRuleList drilldownComponentRuleList;
	private StructureDrilldownComponent structureComponent;
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
		
		try{
			mainPanel= new HorizontalPanel();
			rightPanel=new HorizontalPanel();
			leftPanel=new HorizontalPanel();
			
			drilldownController = new DrilldownController();
			drilldownModel = new DrilldownModel();
			drilldownController.setModel(drilldownModel);
			
			pathComponent = new PathComponent(drilldownController);
			severetyDrilldown=new SeveretyDrilldown(drilldownController);
			drilldownComponentRuleList=new DrilldownComponentRuleList(drilldownController);
			drilldownComponentRuleList.setWidth("100%");
			structureComponent= new StructureDrilldownComponent(drilldownController, resource, "jku.se.drilldown.BenchmarkViewer");
			
			drilldownController.setPathComponent(pathComponent);
			drilldownController.setRuleList(drilldownComponentRuleList);
			drilldownController.setStructureDrilldown(structureComponent);
			drilldownController.setResource(resource);
			drilldownController.setSeveretyDrilldown(severetyDrilldown);
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			leftPanel.add(severetyDrilldown);
			rightPanel.add(drilldownComponentRuleList);
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
		}catch (Exception e){
			panel.add(new Label("BenchmarkViewerPanel: "+e.toString()));
		}
		
		return panel;
	}
}