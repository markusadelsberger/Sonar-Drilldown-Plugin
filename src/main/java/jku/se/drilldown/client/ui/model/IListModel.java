package jku.se.drilldown.client.ui.model;

import jku.se.drilldown.client.ui.view.StructureDrilldownList;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

public interface IListModel {

	public Resource getResource();
	
	public void setResource(Resource resource);
	
	public Resource getSelectedItem(StructureDrilldownList drilldownComponentList);
	
	public void setSelectedItem(StructureDrilldownList drilldownComponentList, Resource resource);
	
	public Measure getActiveMeasure();
	
	public String getActiveElement(String element);
}
