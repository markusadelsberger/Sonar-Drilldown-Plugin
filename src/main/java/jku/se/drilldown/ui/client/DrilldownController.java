package jku.se.drilldown.ui.client;

import java.util.List;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class DrilldownController implements ComponentController{

	private StructureDrilldownComponent structureDrilldown;
	private DrilldownComponentRuleList ruleList;
	private PathComponent pathComponent;
	private DrilldownModel drilldownModel;
	
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
	
	@Override
	public void onSelectedItemChanged(String component) {
		if (component.equals("rule"))
		{
			Measure selectedMeasure = ruleList.getSelectedItem();
			structureDrilldown.reloadLists(selectedMeasure);
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
				pathComponent.setElement(selectedPackage.getName(), 3, "package");
			}
		}
		else if(component.equals("severety")){
			String severety = drilldownModel.getActiveElement("Severety");
			pathComponent.setElement(severety, 1, "severety");
			drilldownModel.setActiveElement("Severety", severety);
			ruleList.reload();
		}
	}
	
	public void clearElement(String element){
		if(element.equals("rule"))
		{
			ruleList.setSelectedItem(null);
			structureDrilldown.reloadLists(null);
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
		else if (element.equals("severety"))
		{
			drilldownModel.setActiveElement("Severety", null);
			structureDrilldown.setSelectedPackage(null);
			ruleList.reload();
			pathComponent.setElement(" ", 1, null);
		}
	}
	
	public DrilldownModel getModel(){
		return drilldownModel;
	}
}
