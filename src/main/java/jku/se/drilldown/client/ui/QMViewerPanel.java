package jku.se.drilldown.client.ui;

import java.util.ArrayList;
import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;
import jku.se.drilldown.client.ui.view.PathComponent;
import jku.se.drilldown.client.ui.view.QualityModelComponent;
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
 * 
 * @author Johannes
 *
 */ 
public class QMViewerPanel extends Page{
	private HorizontalPanel mainPanel;
	private Panel rightPanel;
	private Panel leftPanel;
	private PathComponent pathComponent;
	private SeveretyDrilldown severetyDrilldown;
	private DrilldownComponentRuleList drilldownComponentRuleList;
	private StructureDrilldownComponent structureComponent;
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	private QualityModelComponent qmComponent;
	
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
		
		List<ViewComponents> components = new ArrayList<ViewComponents>();
		components.add(ViewComponents.RULEDRILLDOWN);
		components.add(ViewComponents.MODULELIST);
		components.add(ViewComponents.PACKAGELIST);
		components.add(ViewComponents.QMTREE);
		pathComponent = new PathComponent(drilldownController,components);

		severetyDrilldown=new SeveretyDrilldown(drilldownController);
		qmComponent = new QualityModelComponent(drilldownController, resource);
		drilldownComponentRuleList=new DrilldownComponentRuleList(drilldownController);

		structureComponent= new StructureDrilldownComponent(drilldownController, resource, "jku.se.drilldown.QMDrilldownPage");
					
		drilldownController.setPathComponent(pathComponent);
		drilldownController.setRuleList(drilldownComponentRuleList);
		drilldownController.setStructureDrilldown(structureComponent);
		drilldownController.setQMComponent(qmComponent);
		drilldownController.setResource(resource);
		drilldownController.setSeveretyDrilldown(severetyDrilldown);
		drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
		
		leftPanel.add(severetyDrilldown);
		leftPanel.add(qmComponent);
		
		rightPanel.add(drilldownComponentRuleList);
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.setWidth("100%");
		mainPanel.setCellWidth(leftPanel, "400px");
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