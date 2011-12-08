/**
 * @author markus
 * Implements the Controller of the Drilldown MVC Principle
 * After the creation the setter methods must be called
 */
package jku.se.drilldown.client.ui.controller;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.view.DrilldownComponentRuleList;
import jku.se.drilldown.client.ui.view.PathComponent;
import jku.se.drilldown.client.ui.view.SeveretyDrilldown;
import jku.se.drilldown.client.ui.view.StructureDrilldownComponent;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;


public class DrilldownController implements IComponentController{

	private StructureDrilldownComponent structureDrilldown;
	private DrilldownComponentRuleList ruleList;
	private PathComponent pathComponent;
	private DrilldownModel drilldownModel;
	private SeveretyDrilldown severetyDrilldown;
	private Resource resource;
	
	public void setStructureDrilldown(StructureDrilldownComponent structureDrilldown){
		this.structureDrilldown = structureDrilldown;
	}
	
	public void setRuleList(DrilldownComponentRuleList ruleList){
		this.ruleList = ruleList;
	}
	
	public void setPathComponent(PathComponent pathComponent){
		this.pathComponent = pathComponent;
	}
	
	public void setModel(DrilldownModel drilldownModel){
		this.drilldownModel=drilldownModel;
	}
	
	public void setSeveretyDrilldown(SeveretyDrilldown severetyDrilldown){
		this.severetyDrilldown=severetyDrilldown;
	}
	
	public void setResource(Resource resource){
		this.resource=resource;
	}
	
	/**
	 * Is used to notify the controller that the data in the model has changed and 
	 * the views should be notified
	 * @param component The component that called the method;
	 */
	public void onSelectedItemChanged(ViewComponents component) {
		switch(component){
			case SEVERETYDRILLDOWN:
				ruleList.reload();
				pathComponent.reload();
				structureDrilldown.reload();
				break;
			
			case RULEDRILLDOWN: 
				pathComponent.reload();
				structureDrilldown.reload();
				break;
			
			case PACKAGELIST:
			case FILELIST:
			case MODULELIST: pathComponent.reload(); 
				break;		

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
				
				ruleList.reload();
				structureDrilldown.reload();
				pathComponent.reload();
			break;
		
			case RULEDRILLDOWN:
				drilldownModel.setActiveMeasure(null);
				
				structureDrilldown.reload();
				ruleList.reload();
				pathComponent.reload();
			break;
		
			case MODULELIST:
				drilldownModel.setSelectedItem(ViewComponents.MODULELIST, null);
				
				structureDrilldown.reload();
				pathComponent.reload();
			break;
			
			case PACKAGELIST:
				drilldownModel.setSelectedItem(ViewComponents.PACKAGELIST, null);
				
				structureDrilldown.reload();
				pathComponent.reload();
			break;
		}
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
	public void loadRuleDataForMetric(final String metric){
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
				ruleList.reload();
				ruleList.reloadFinished();
				
			}

		});
	}
		
	private ResourceQuery getQuery(String metric)
	{
		ResourceQuery query = ResourceQuery.createForResource(resource, metric).setDepth(0).setExcludeRules(false);
		return query;
	}
}
