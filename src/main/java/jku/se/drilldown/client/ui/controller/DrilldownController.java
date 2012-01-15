package jku.se.drilldown.client.ui.controller;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.DrilldownComponent;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * @author markus
 * Implements the Controller of the Drilldown MVC Principle
 */
public class DrilldownController implements IComponentController{

	private DrilldownModel drilldownModel;
	private List<DrilldownComponent> listenerList = new LinkedList<DrilldownComponent>();
	
	/**
	 * To instantiate the controller a model must be given; the controller must be instantiated before the views,
	 * as the views register with the view via the super-Constructor in DrilldownComponent
	 * @param drilldownModel A DrilldownModel-Object, if it isn't given the NullPointerException will be thrown
	 * @throws NullPointerException
	 */
	public DrilldownController (DrilldownModel drilldownModel) throws NullPointerException{
		if(drilldownModel == null) {
			throw new NullPointerException();
		}
		else {
			this.drilldownModel=drilldownModel;
		}
	}
	
	/**
	 * Adds the given DrilldownComponent to the list that is notified at a reload
	 * @param drilldownComponent
	 */
	public void addListener(DrilldownComponent drilldownComponent) {
		listenerList.add(drilldownComponent);
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
	 * @param component The component that called the method;
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
			
			default: break;
		}
		
		onSelectedItemChanged(component);
	}

	/**
	 * Gets the used Model
	 * @return The Model in use;
	 */
	public DrilldownModel getModel(){
		return drilldownModel;
	}
	
	/**
	 * Loads the Ruledata for a given String and saves it into the Model
	 * @param metric The Metric Name from the org.sonar.gwt.Metrics Interface
	 */
	public void loadRuleDataForMetric(String... metric){
		ResourceQuery query = ResourceQuery.createForResource(drilldownModel.getResource(), metric).setDepth(0).setExcludeRules(false);
		Sonar.getInstance().find(query, new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				//the response from the query came back, the measures are saved in measureList
				List<Measure>measureList = resource.getMeasures();
				
				/*a list for every severety is created and a count; the count represents the number of violations of the
				* rules, not the nuber of violated rules
				*/
				
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
				
				//depending on the severety the measure is added to the appropriate list
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
				
				//the lists are added to the model, so that they can be loaded later on, same goes for the counts
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
				
				//the views are reloaded
				onSelectedItemChanged(ViewComponents.INITIALIZE);
			}

		});
	}
}
