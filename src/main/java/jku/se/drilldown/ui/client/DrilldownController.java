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
	private QuantilDrilldown quantilDrilldown;
	private PathComponent pathComponent;
	
	public void setStructureDrilldown(StructureDrilldownComponent structureDrilldown){
		this.structureDrilldown = structureDrilldown;
	}
	
	public void setRuleList(DrilldownComponentRuleList ruleList){
		this.ruleList = ruleList;
	}
	
	public void setSeveretyDrilldown(QuantilDrilldown quantilDrilldown){
		this.quantilDrilldown = quantilDrilldown;
	}
	
	public void setPathComponent(PathComponent pathComponent){
		this.pathComponent = pathComponent;
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
			List<Measure> measureList = quantilDrilldown.getSelectedItem();
			ruleList.reloadBegin();
			ruleList.addMeasures(measureList);
			ruleList.reloadFinished();
			
			String severety;
			if(quantilDrilldown.getSelectedSeverety()!=null){
				severety = quantilDrilldown.getSelectedSeverety();
			}else{
				severety = "";
			}
			pathComponent.setElement(severety, 1, "severety");
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
			quantilDrilldown.setSelectedItem("All");
			structureDrilldown.setSelectedPackage(null);
			ruleList.reloadBegin();
			ruleList.addMeasures(quantilDrilldown.getSelectedItem());
			ruleList.reloadFinished();
			pathComponent.setElement(" ", 1, null);
		}
	}

}
