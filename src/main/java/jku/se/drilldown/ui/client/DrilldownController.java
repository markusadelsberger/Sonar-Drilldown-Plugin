package jku.se.drilldown.ui.client;

import java.util.List;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

public class DrilldownController implements ComponentController{

	private StructureDrilldownComponent structureDrilldown;
	private DrilldownComponentRuleList ruleList;
	private SeveretyDrilldown severetyDrilldown;
	private PathComponent pathComponent;
	
	public void setStructureDrilldown(StructureDrilldownComponent structureDrilldown){
		this.structureDrilldown = structureDrilldown;
	}
	
	public void setRuleList(DrilldownComponentRuleList ruleList){
		this.ruleList = ruleList;
	}
	
	public void setSeveretyDrilldown(SeveretyDrilldown severetyDrilldown){
		this.severetyDrilldown = severetyDrilldown;
	}
	
	public void setPathComponent(PathComponent pathComponent){
		this.pathComponent = pathComponent;
	}
	
	public void onSelectedItemChanged(String component) {
		
		if(component.equals("severety")){
			List<Measure> measureList = severetyDrilldown.getSelectedItem();
			
			ruleList.reloadBegin();
			ruleList.addMeasures(measureList);
			ruleList.reloadFinished();
			
			structureDrilldown.setSelectedRules(measureList);
			
			String severety;
			if(severetyDrilldown.getSelectedSeverety()!=null){
				severety = severetyDrilldown.getSelectedSeverety();
			} else {
				severety = "";
			}
			pathComponent.setElement(severety, 1, "severety");
		} 
		else if (component.equals("rule"))
		{
			Measure selectedMeasure = ruleList.getSelectedItem();
			
			structureDrilldown.setSelectedRule(selectedMeasure);
			pathComponent.setElement(selectedMeasure.getRuleName(), 2, "rule");
		} 
		else if(component.equals("structure"))
		{
			Resource selectedModule = structureDrilldown.getSelectedModule();
			Resource selectedPackage = structureDrilldown.getSelectedPackage();
			
			if(selectedModule != null)
			{
				pathComponent.setElement(selectedModule.getName(), 3, "module");
			}
			
			if (selectedPackage != null)
			{
				pathComponent.setElement(selectedPackage.getName(), 4, "package");
			}
		}
	}
	
	public void clearElement(String element){
		if (element.equals("severety"))
		{
			severetyDrilldown.setSelectedItem("All");
			structureDrilldown.setSelectedRules(null);
			ruleList.reloadBegin();
			ruleList.addMeasures(severetyDrilldown.getSelectedItem());
			ruleList.reloadFinished();
			pathComponent.setElement(" ", 1, null);
		} 
		else if(element.equals("rule"))
		{
			ruleList.setSelectedItem(null);
			structureDrilldown.setSelectedRule(null);
			pathComponent.setElement("Any rule >> ", 2, null);
		} 
		else if (element.equals("module"))
		{
			structureDrilldown.setSelectedModule(null);
			pathComponent.setElement(" ", 3, null);
		}
		else if (element.equals("package"))
		{
			structureDrilldown.setSelectedPackage(null);
			pathComponent.setElement(" ", 4, null);
		}
	}
}
