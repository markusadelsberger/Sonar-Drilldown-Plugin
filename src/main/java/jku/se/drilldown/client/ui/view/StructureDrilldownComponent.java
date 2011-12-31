package jku.se.drilldown.client.ui.view;


import java.util.ArrayList;
import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

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
	 * @param resource The selected resource object on the sonar platform. 
	 * @param pageID The gwtID of the page that contains this element. for example "jku.se.drilldown.qm.page.QMDrilldownPage" 
	 */
	public StructureDrilldownComponent(DrilldownController controller, Resource resource, String pageID){
		super(controller);
		this.pageID= pageID;
		
		this.controller=controller;
		
		model=controller.getModel();
		model.setResource(resource);
		
		moduleList = null;
		packageList = null;
		fileList = null;
					
		verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		
		initWidget(verticalPanel);	
	}
		
	@Override
	public void onLoad() {
		loadData();

		verticalPanel.add(structurePanel);
	}
	
	/**
	 * Method is the entry point to reload the component. 
	 * It removes all items and creates them new. 
	 * 
	 * Additionally the method interlinks the list components together.
	 */
	private void loadData() {
	
		Resource resource = model.getResource();
		
		boolean parentExists= false;
		
		int listPosition=0;
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			moduleList = new StructureDrilldownList(controller, STRUCTURE[0], pageID, ViewComponents.MODULELIST);
			
			structurePanel = new Grid(1,3);
			structurePanel.setWidth("100%");
			
			moduleList.setSize(verticalPanel.getOffsetWidth()/structurePanel.getColumnCount());
			
			structurePanel.setWidget(0, listPosition, moduleList);
			
			
			listPosition++;
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || parentExists){
				
			packageList = new StructureDrilldownList(controller, STRUCTURE[1], pageID, ViewComponents.PACKAGELIST);
			
			if(parentExists)
			{
				packageList.setPrev(moduleList);		
			}
			else
			{
				structurePanel = new Grid(1,2);
				structurePanel.setWidth("100%");
			}
			
			packageList.setSize(verticalPanel.getOffsetWidth()/structurePanel.getColumnCount());
			
			structurePanel.setWidget(0, listPosition, packageList);

			listPosition++;
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || parentExists){
			
			fileList = new StructureDrilldownList(controller, STRUCTURE[2], pageID, ViewComponents.FILELIST);
			
			if(parentExists)
			{	
				fileList.setPrev(packageList);		
			}
			else
			{
				structurePanel = new Grid(1,1);
				structurePanel.setWidth("100%");
			}
			
			fileList.setSize(verticalPanel.getOffsetWidth()/structurePanel.getColumnCount());
			
			structurePanel.setWidget(0, listPosition, fileList);
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
			packageList.setSelectedMeasures(selectedMeasures);
			if(startReloadingComp==null)
				startReloadingComp= packageList;
		}
	
		if(fileList!= null)
		{
			fileList.setSelectedMeasures(selectedMeasures);
			if(startReloadingComp==null)
				startReloadingComp= fileList;
		}
		
		if(startReloadingComp != null)
			startReloadingComp.reload();
	}
	
	public Resource getSelectedModule(){
		return model.getSelectedItem(moduleList.getListType());
	}
	
	public void clearSelectedModule() {
		moduleList.deselectRow(getSelectedModule());
			
		model.setSelectedItem(moduleList.getListType(), null);
		packageList.reload();
	}
	
	public Resource getSelectedPackage(){
		return model.getSelectedItem(packageList.getListType());
	}
	
	public void clearSelectedPackage() {
		packageList.deselectRow(getSelectedPackage());
		
		model.setSelectedItem(packageList.getListType(), null);
		fileList.reload();
	}

	@Override
	public void reload(ViewComponents viewComponent) {
		switch(viewComponent){
			case QMTREE:
			case BENCHMARKDRILLDOWN:
			case SEVERETYDRILLDOWN:
			case RULEDRILLDOWN:
				Measure selectedMeasure = model.getActiveMeasure();
				
				if(selectedMeasure == null) {
					setSelectedRules(model.getActiveMeasures());
				}
				else {
					setSelectedRule(selectedMeasure);
				}
		}
		
	}
}
 