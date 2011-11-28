package jku.se.drilldown.qm.client;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.qm.client.ui.DrilldownComponentRuleList;
import jku.se.drilldown.qm.client.ui.DrilldownController;
import jku.se.drilldown.qm.client.ui.DrilldownModel;
import jku.se.drilldown.qm.client.ui.PathComponent;
import jku.se.drilldown.qm.client.ui.SeveretyDrilldown;
import jku.se.drilldown.qm.client.ui.StructureDrilldownComponent;


import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

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
public class PagePanel extends Page{
	private HorizontalPanel mainPanel;
	private Panel rightPanel;
	private Panel leftPanel;
	private PathComponent pathComponent;
	private SeveretyDrilldown severetyDrilldown;
	private DrilldownComponentRuleList drilldownComponentRuleList;
	private StructureDrilldownComponent structureComponent;
	private Resource resource;
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	
	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel panel = new VerticalPanel();
		this.resource=resource;
		
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
			//structureComponent= new StructureDrilldownComponent(resource, "jku.se.drilldown.ui.BenchmarkViewer", drilldownController);
			structureComponent= new StructureDrilldownComponent(resource, "jku.se.drilldown.qm.QMDrilldownPage", drilldownController);
						
			drilldownController.setPathComponent(pathComponent);
			drilldownController.setRuleList(drilldownComponentRuleList);
			drilldownController.setStructureDrilldown(structureComponent);
			drilldownController.setResource(resource);
			drilldownController.setSeveretyDrilldown(severetyDrilldown);
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			leftPanel.add(severetyDrilldown);
			rightPanel.add(drilldownComponentRuleList);
			
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