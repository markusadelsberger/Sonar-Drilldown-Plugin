package jku.se.drilldown.client.ui.controller;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.BenchmarkDrilldown;
import jku.se.drilldown.client.ui.view.DrilldownComponent;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;


/**
 * @author markus
 * Implements the Controller of the Drilldown MVC Principle
 * After the creation the setter methods must be called
 */
public class DrilldownController implements IComponentController{

	private DrilldownModel drilldownModel;
	private List<DrilldownComponent> listenerList = new LinkedList<DrilldownComponent>();
	
	private Resource resource;
	
	public void setModel(DrilldownModel drilldownModel){
		this.drilldownModel=drilldownModel;
	}
	
	public void setResource(Resource resource){
		this.resource=resource;
	}
	
	public void addListener(DrilldownComponent drilldownComponent){
		listenerList.add(drilldownComponent);
	}
	
	public void removeListener(DrilldownComponent drilldownComponent){
		listenerList.remove(drilldownComponent);
	}
	
	/**
	 * Is used to notify the controller that the data in the model has changed and 
	 * the views should be notified
	 * @param component The component that called the method;
	 */
	public void onSelectedItemChanged(ViewComponents component) {
		for(DrilldownComponent listener : listenerList){
			listener.reload(component);
		}
	}
	
	/**
	 * Resets the selected element
	 * @param component The component that called the method
	*/
	public void clearElement(ViewComponents component){
		
		switch(component){
			case SEVERETYDRILLDOWN:
				drilldownModel.setActiveElement("Severety", null);
				drilldownModel.setActiveMeasures(null);
			break;
		
			case RULEDRILLDOWN:
				drilldownModel.setActiveMeasure(null);
			break;
		
			case MODULELIST:
				drilldownModel.setSelectedItem(ViewComponents.MODULELIST, null);
			break;
			
			case PACKAGELIST:
				drilldownModel.setSelectedItem(ViewComponents.PACKAGELIST, null);
			break;
			
			case QMTREE:
				drilldownModel.setActiveElement("qmtreeNode", null);
				drilldownModel.setActiveMeasures(null);
			break;
			
			case BENCHMARKDRILLDOWN:
				drilldownModel.setActiveElement("Benchmark", null);
				drilldownModel.setActiveMeasures(null);
			break;
		}
		
		onSelectedItemChanged(component);
	}

	/**
	 * Gets the used Model
	 * @return The Model in use, if null is returned the method setModel(DrilldownModel drilldownModel) was not called correctly
	 */
	public DrilldownModel getModel(){
		return drilldownModel;
	}
	
	/**
	 * Loads the Ruledata for a given String and saves it into the Model; after loading the Severety List and the Rule List are reloaded
	 * @param metric The Metric Name from the org.sonar.gwt.Metrics Interface
	 */
	public void loadRuleDataForMetric(String... metric){
		ResourceQuery query = ResourceQuery.createForResource(resource, metric).setDepth(0).setExcludeRules(false);
		Sonar.getInstance().find(query, new AbstractCallback<Resource>() {

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
				
				onSelectedItemChanged(ViewComponents.INITIALIZE);
			}

		});
	}
}
