package jku.se.drilldown.client.ui;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;
import jku.se.drilldown.client.ui.view.PathComponent;
import jku.se.drilldown.client.ui.view.QualityModelComponent;
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
 * QMViewerPanel represents UI of the QMDrilldown. 
 * 
 * @author Johannes
 */ 
public class QMViewerPanel extends Page{

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		Panel panel = new VerticalPanel();
		
		try{
			HorizontalPanel mainPanel= new HorizontalPanel();
			Panel rightPanel=new HorizontalPanel();
			Panel leftPanel=new HorizontalPanel();

			// at the beginning controller has to be initialized, because UI-components need controller. 
			DrilldownController drilldownController = new DrilldownController(new DrilldownModel(resource));
			
			// creates components
			PathComponent pathComponent = new PathComponent(drilldownController,ViewComponents.QMTREE);
			QualityModelComponent qmComponent = new QualityModelComponent(drilldownController);
			DrilldownComponentRuleList ruleListComponent=new DrilldownComponentRuleList(drilldownController);
			StructureDrilldownComponent structureComponent= new StructureDrilldownComponent(drilldownController, "jku.se.drilldown.QMDrilldownPage");
			
			drilldownController.loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			leftPanel.add(qmComponent);

			rightPanel.add(ruleListComponent);
			rightPanel.setWidth("100%");
			
			mainPanel.add(leftPanel);
			mainPanel.add(rightPanel);
			mainPanel.setWidth("100%");
			mainPanel.setCellWidth(leftPanel, "230px");
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