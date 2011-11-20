package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RuleDrilldownComponent extends DrilldownComponent implements ClickHandler {
	
	private Resource resource;
	private LayoutPanel mainPanel;
	private Panel rightPanel;
	private Panel leftPanel;
	
	private QuantilDrilldown quantilDrilldown;
	private DrilldownComponentRuleList drilldownComponentRuleList;
	
	
	public RuleDrilldownComponent(Resource resource){
		this.resource=resource;
		mainPanel= new LayoutPanel();
		
		rightPanel=new ScrollPanel();
		leftPanel=new HorizontalPanel();
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		
		mainPanel.setWidgetLeftWidth(leftPanel, 0, Style.Unit.PX, 100,  Style.Unit.PX);  // Left panel
		mainPanel.setWidgetLeftWidth(rightPanel, 0, Style.Unit.PX, 100,  Style.Unit.PCT);  // Left panel

		
		initWidget(mainPanel);
	}
	
	@Override
	public void onLoad(){
		final ClickHandler clickHandler = this;
		try{
			quantilDrilldown=new QuantilDrilldown(resource, Resource.SCOPE_ENTITY, clickHandler, "jku.se.drilldown.ui.BenchmarkViewer");
			drilldownComponentRuleList=new DrilldownComponentRuleList(resource, Resource.SCOPE_ENTITY, clickHandler, "jku.se.drilldown.ui.BenchmarkViewer");

			//loadRuleData();
			loadRuleDataForMetric(Metrics.VIOLATIONS);
			
			//quantilDrilldown.reloadFinished();
			//drilldownComponentRuleList.reloadFinished();
			
			leftPanel.add(quantilDrilldown);
			rightPanel.add(drilldownComponentRuleList);
		}catch(Exception e){
			Window.alert(e.toString());
		}
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		try{
			Element element = event.getRelativeElement();
			List<Measure> measureList = (List<Measure>)element.getPropertyObject("assignedList");
			drilldownComponentRuleList.reloadBegin();
			drilldownComponentRuleList.addMeasures(measureList);
			drilldownComponentRuleList.reloadFinished();
		}catch(Exception e){
			Window.alert(e.toString());
		}
		
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
				quantilDrilldown.addMeasures(0, blockerCount);
				quantilDrilldown.addDrilldownAnchor("Blocker", 0, blockerList);
				
				quantilDrilldown.addMeasures(1, criticalCount);
				quantilDrilldown.addDrilldownAnchor("Critical", 1, criticalList);
				
				quantilDrilldown.addMeasures(2, majorCount);
				quantilDrilldown.addDrilldownAnchor("Major", 2, majorList);
				
				quantilDrilldown.addMeasures(3, minorCount);
				quantilDrilldown.addDrilldownAnchor("Minor", 3, minorList);
				
				quantilDrilldown.addMeasures(4, infoCount);
				quantilDrilldown.addDrilldownAnchor("Info", 4, infoList);
				
				if(blockerCount>0){
					drilldownComponentRuleList.addMeasures(blockerList);
				}
				if(criticalCount>0){
					drilldownComponentRuleList.addMeasures(criticalList);
				}
				
				if(majorCount>0){
					drilldownComponentRuleList.addMeasures(majorList);
				}
				if(minorCount>0){
					drilldownComponentRuleList.addMeasures(minorList);
				}
				
				if(infoCount>0){
					drilldownComponentRuleList.addMeasures(infoList);
				}
				
				quantilDrilldown.reloadFinished();
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
