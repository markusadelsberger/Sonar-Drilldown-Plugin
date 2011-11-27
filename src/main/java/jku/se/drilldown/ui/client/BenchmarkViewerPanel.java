package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;

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

public class BenchmarkViewerPanel extends Page {
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
			
			leftPanel.setWidth("100px");
			rightPanel.setWidth("100%");
			
			drilldownController = new DrilldownController();
			drilldownModel = new DrilldownModel();
			drilldownController.setModel(drilldownModel);
			
			pathComponent = new PathComponent(drilldownController);
			severetyDrilldown=new SeveretyDrilldown(drilldownController);
			drilldownComponentRuleList=new DrilldownComponentRuleList(drilldownController);
			structureComponent= new StructureDrilldownComponent(resource, "jku.se.drilldown.ui.BenchmarkViewer", drilldownController);
			
			
			drilldownController.setPathComponent(pathComponent);
			drilldownController.setRuleList(drilldownComponentRuleList);
			drilldownController.setStructureDrilldown(structureComponent);
			
			
			loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			leftPanel.add(severetyDrilldown);
			rightPanel.add(drilldownComponentRuleList);
			
			mainPanel.add(leftPanel);
			mainPanel.add(rightPanel);
			
			panel.add(mainPanel);
			
			panel.add(structureComponent);
			panel.add(pathComponent);
		}catch (Exception e){
			panel.add(new Label("BenchmarkViewerPanel: "+e.toString()));
		}
		
		return panel;

	}
	
	
	
	private void loadRuleDataForMetric(final String metric){
		Sonar.getInstance().find(getQuery(metric), new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				List<Measure>measureList = resource.getMeasures();
				
				List<Measure>blockerList = new LinkedList<Measure>();
				int blockerCount=0;
				
				List<Measure>criticalList = new LinkedList<Measure>();
				int criticalCount=0;
				
				List<Measure>majorList = new LinkedList<Measure>();
				int majorCount=0;
				
				List<Measure>minorList = new LinkedList<Measure>();
				int minorCount=0;
				
				List<Measure>infoList = new LinkedList<Measure>();
				int infoCount=0;
				
				for(Measure measure : measureList){
					String metric = measure.getRuleSeverity();
					if(metric.compareTo("BLOCKER")==0){
						blockerList.add(measure);
						blockerCount+=measure.getIntValue();
					}else if(metric.compareTo("CRITICAL")==0){
						criticalList.add(measure);
						criticalCount+=measure.getIntValue();
					}else if(metric.compareTo("MAJOR")==0){
						majorList.add(measure);
						majorCount+=measure.getIntValue();
					}else if(metric.compareTo("MINOR")==0){
						minorList.add(measure);
						minorCount+=measure.getIntValue();
					}else if(metric.compareTo("INFO")==0){
						infoList.add(measure);
						infoCount+=measure.getIntValue();
					}
				}
				
				drilldownModel.addList("Blocker", blockerList);
				drilldownModel.addList("Critical", criticalList);
				drilldownModel.addList("Major", majorList);
				drilldownModel.addList("Minor", minorList);
				drilldownModel.addList("Info", infoList);
				
				drilldownModel.addCount("Blocker", blockerCount);
				drilldownModel.addCount("Critical", criticalCount);
				drilldownModel.addCount("Major", majorCount);
				drilldownModel.addCount("Minor", minorCount);
				drilldownModel.addCount("Info", infoCount);
				drilldownModel.addCount("SeveretyTotal", blockerCount+criticalCount+majorCount+minorCount+infoCount);
				
				severetyDrilldown.reload();
				drilldownComponentRuleList.reload();
				drilldownComponentRuleList.reloadFinished();
				
			}

		});
	}
	
	private ResourceQuery getQuery(String metric)
	{
		ResourceQuery query = ResourceQuery.createForResource(resource, metric).setDepth(0).setExcludeRules(false);
		return query;
	}
}