/**
 * @author markus
 * Implements the Controller of the Drilldown MVC Principle
 * After the creation the setter methods must be called
 */
package jku.se.drilldown.client.ui.controller;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.model.DrilldownModel;
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
	
	public void onSelectedItemChanged(String component) {
		
		if(component.equals("severety")){
			String activeElement = drilldownModel.getActiveElement("Severety");
			
			drilldownModel.setActiveMeasure(null);
			
			pathComponent.setElement("Any rule >> ", 2, null);
			pathComponent.setElement(activeElement, 1, "severety");
						
			structureDrilldown.reload();
			ruleList.reload();
		} 
		else if (component.equals("rule"))
		{
			Measure selectedMeasure = drilldownModel.getActiveMeasure();
			
			pathComponent.setElement(selectedMeasure.getRuleName(), 2, "rule");
			
			structureDrilldown.reload();
		} 
		else if(component.equals("structure"))
		{
			Resource selectedModule = structureDrilldown.getSelectedModule();
			if(selectedModule != null)
			{
				pathComponent.setElement(selectedModule.getName(), 3, "module");
			}
			
			Resource selectedPackage = structureDrilldown.getSelectedPackage();
			if (selectedPackage != null)
			{
				pathComponent.setElement(selectedPackage.getName(), 4, "package");
			}
		}	
	}
	
	/**
	 * Resets the selected element, depending on the given String
	 * @param element rule: resets the rule and deletes it from the pathcomponent
	 * @param element module: resets the module and deletes it from the pathcomponent
	 * @param element package: resets the package and deletes it from the pathcomponent
	 * @param element severety: resets the severety, deletes it from the pathcomponent and reloads the ruleList
	 */
	
	public void clearElement(String element){
	
 		if (element.equals("severety"))
		{
			drilldownModel.setActiveElement("Severety", null);
			ruleList.reload();
			structureDrilldown.reload();
			pathComponent.setElement("Severety >> ", 1, null);
		}
		else if(element.equals("rule"))
		{
			drilldownModel.setActiveMeasure(null);
			structureDrilldown.reload();
			pathComponent.setElement("Any rule >> ", 2, null);
		} 
		else if (element.equals("module"))
		{
			structureDrilldown.clearSelectedModule();
			pathComponent.setElement(" ", 3, null);
		}
		else if (element.equals("package"))
		{
			structureDrilldown.clearSelectedPackage();
			pathComponent.setElement(" ", 4, null);
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
