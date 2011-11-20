package jku.se.drilldown.qm.client;

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
	private Resource resource;
	
	final static String[] STRUCTURE = {Resource.SCOPE_SET, Resource.SCOPE_SPACE, Resource.SCOPE_ENTITY};
	
	private StructureDrilldownList moduleList;
	private StructureDrilldownList packageList;
	private StructureDrilldownList fileList;
	
	private String pageID; 
	
	private ComponentController controller;
	/**
	 * 
	 * @param resource The selected resource object on the sonar platform. 
	 * @param pageID The gwtID of the page that contains this element. for example "jku.se.drilldown.qm.page.QMDrilldownPage" 
	 */
	public StructureDrilldownComponent(Resource resource, String pageID, ComponentController controller){
		this.resource=resource;
		this.pageID= pageID;
		
		this.controller=controller;
		
		this.moduleList = null;
		this.packageList = null;
		this.fileList = null;
		
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);	
	}
		
	public Resource getResource() {
		 return resource;
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
	
		boolean parentExists= false;
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			moduleList = new StructureDrilldownList(resource, STRUCTURE[0], pageID, controller);
			structurePanel.setWidget(0, 0, moduleList);
			
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || parentExists){
			packageList = new StructureDrilldownList(resource, STRUCTURE[1], pageID, controller);
			structurePanel.setWidget(0, 1, packageList);
			
			if(parentExists)
				packageList.setPrev(moduleList);
			
			parentExists =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || parentExists){
			fileList = new StructureDrilldownList(resource, STRUCTURE[2], pageID, controller);
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

	public void reloadLists(Measure selectedMeasure)
	{
		StructureDrilldownList startReloadingComp = null;
		
		if(moduleList!= null)
		{
			moduleList.setSelectedMeasure(selectedMeasure);
			startReloadingComp = moduleList;
		}
		
		if(packageList!= null)
		{
			this.packageList.setSelectedMeasure(selectedMeasure);
			if(startReloadingComp==null)
				startReloadingComp= packageList;
		}
	
		if(fileList!= null)
		{
			this.fileList.setSelectedMeasure(selectedMeasure);
			if(startReloadingComp==null)
				startReloadingComp= fileList;
		}
		
		if(startReloadingComp != null)
			startReloadingComp.loadData();
	}

	
	public void setSelectedModule(Resource selectedModule)
	{
		if(moduleList.containsSelectedItem())
		{
			moduleList.setSelectedItem(selectedModule);
			packageList.loadData();
		}
		else
			moduleList.setSelectedItem(selectedModule);
	}
	
	public Resource getSelectedModule()
	{
		return this.moduleList.getSelectedItem();
	}
	
	public void setSelectedPackage(Resource selectedPackage)
	{
		if(packageList.containsSelectedItem())
		{
			packageList.setSelectedItem(selectedPackage);	
			fileList.loadData();
		}
		else
			packageList.setSelectedItem(selectedPackage);	
	}
	
	public Resource getSelectedPackage()
	{
		return this.packageList.getSelectedItem();
	}
}
 