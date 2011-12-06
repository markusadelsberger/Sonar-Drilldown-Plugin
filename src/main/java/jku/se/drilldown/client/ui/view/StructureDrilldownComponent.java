package jku.se.drilldown.client.ui.view;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Component decomposes a resource object in its folders, packages and files.  
 * 
 * @author Johannes
 *
 */
public class StructureDrilldownComponent extends DrilldownComponent{
	
	private Panel verticalPanel;
	private Grid structurePanel;
	
	final static String[] STRUCTURE = {Resource.SCOPE_SET, Resource.SCOPE_SPACE, Resource.SCOPE_ENTITY};
	
	private StructureDrilldownList moduleList;
	private StructureDrilldownList packageList;
	private StructureDrilldownList fileList;
	
	private String pageID; 
	
	private DrilldownController controller;
	private DrilldownModel model;
	/**
	 * 
	 * @param resource The selected resource object on the sonar platform. 
	 * @param pageID The gwtID of the page that contains this element. for example "jku.se.drilldown.qm.page.QMDrilldownPage" 
	 */
	public StructureDrilldownComponent(DrilldownController controller, Resource resource, String pageID){
		this.pageID= pageID;
		
		this.controller=controller;
		model=controller.getModel();
		model.setResource(resource);
		
		moduleList = null;
		packageList = null;
		fileList = null;
				
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);	
	}
		
	@Override
	public void onLoad() {
		structurePanel = new Grid(1,3);
		verticalPanel.add(structurePanel);
		loadData();
	}
	
	/**
	 * Method is the entry point to reload the component. 
	 * It removes all items and creates them new. 
	 * 
	 * Additionally the method interlinks the list components together.
	 */
	private void loadData() {
		structurePanel.clear();
	
		Resource resource = model.getResource();
		
		boolean parentExists= false;
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			moduleList = new StructureDrilldownList(controller, STRUCTURE[0], pageID);
			structurePanel.setWidget(0, 0, moduleList);
			
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || parentExists){
			packageList = new StructureDrilldownList(controller, STRUCTURE[1], pageID);
			structurePanel.setWidget(0, 1, packageList);
			
			if(parentExists)
				packageList.setPrev(moduleList);
			
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || parentExists){
			fileList = new StructureDrilldownList(controller, STRUCTURE[2], pageID);
			structurePanel.setWidget(0, 2, fileList);
			
			if(parentExists)
				fileList.setPrev(packageList);	
		}
		
		if(packageList!=null)
		{
			packageList.setNext(fileList);
			
			if(moduleList!=null)
				moduleList.setNext(packageList);
		}
	}

	private void setSelectedRule(Measure selectedMeasure)
	{
		if(selectedMeasure!=null)
		{
			List<Measure> selectedMeasures = new ArrayList<Measure>();
			selectedMeasures.add(selectedMeasure);
			
			reloadLists(selectedMeasures);
		}
		else
			reloadLists(null);
	}
	
	private void setSelectedRules(List<Measure> selectedMeasures)
	{
		reloadLists(selectedMeasures);
	}
	
	private void reloadLists(List<Measure> selectedMeasures)
	{
		StructureDrilldownList startReloadingComp = null;
		
		if(moduleList!= null)
		{
			moduleList.setSelectedMeasures(selectedMeasures);
			startReloadingComp = moduleList;
		}
		
		if(packageList!= null)
		{
			this.packageList.setSelectedMeasures(selectedMeasures);
			if(startReloadingComp==null)
				startReloadingComp= packageList;
		}
	
		if(fileList!= null)
		{
			this.fileList.setSelectedMeasures(selectedMeasures);
			if(startReloadingComp==null)
				startReloadingComp= fileList;
		}
		
		if(startReloadingComp != null)
			startReloadingComp.loadData();
	}
	
	
	public Resource getSelectedModule()
	{
		return model.getSelectedItem(moduleList);
	}
	
	public void clearSelectedModule() {
		moduleList.deselectRow(getSelectedModule());
			
		model.setSelectedItem(moduleList, null);
		packageList.loadData();
	}
	
	public Resource getSelectedPackage()
	{
		return model.getSelectedItem(packageList);
	}
	
	public void clearSelectedPackage() {
		packageList.deselectRow(getSelectedPackage());
		
		model.setSelectedItem(packageList, null);
		fileList.loadData();
	}

	public void reload() {
		
		Measure selectedMeasure = model.getActiveMeasure();
		
		if(selectedMeasure == null)
		{
			String activeElement = model.getActiveElement("Severety");
			
			if(activeElement!=null){
				setSelectedRules(model.getList(activeElement));
			}
		}
		else
		{
			setSelectedRule(selectedMeasure);
		}
	}


}
 