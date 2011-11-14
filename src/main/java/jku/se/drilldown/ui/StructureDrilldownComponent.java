package jku.se.drilldown.ui;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Component decomposes a resource object in its folder, packages and files.  
 * 
 * @author Johannes
 *
 */
public class StructureDrilldownComponent extends DrilldownComponent implements ClickHandler{
	
	private Panel verticalPanel;
	private Grid structurePanel;
	private Resource resource;
	
	final static String[] STRUCTURE = {Resource.SCOPE_SET, Resource.SCOPE_SPACE, Resource.SCOPE_ENTITY};
	
	private StructureDrilldownList moduleList;
	private StructureDrilldownList packageList;
	private StructureDrilldownList fileList;
	
	private String pageID; 
	
	/**
	 * 
	 * @param resource
	 * @param pageID The gwtID of the page that contains this element. for example "jku.se.drilldown.qm.page.QMDrilldownPage" 
	 */
	public StructureDrilldownComponent(Resource resource, String pageID){
		this.resource=resource;
		this.pageID= pageID;

		verticalPanel = new VerticalPanel();
		
		initWidget(verticalPanel);	
	}
		
	public Resource getResource() {
		 return resource;
	}

	@Override
	public void onLoad() {
		structurePanel = new Grid(1,4);
		verticalPanel.add(structurePanel);
		loadData();
	}
	
	private void loadData() {
		structurePanel.clear();
	
		boolean cont= false;
		
		final ClickHandler clickHandler = this;
				
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			moduleList = new StructureDrilldownList(resource, STRUCTURE[0], clickHandler, pageID);
			structurePanel.setWidget(0, 1, moduleList);
			
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || cont){
			packageList = new StructureDrilldownList(resource, STRUCTURE[1], clickHandler, pageID);
			structurePanel.setWidget(0, 2, packageList);
				
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || cont){
			fileList = new StructureDrilldownList(resource, STRUCTURE[2], null, pageID);
			structurePanel.setWidget(0, 3, fileList);
		}
		
		// TODO moduleList / packageList können null sein!
		moduleList.addNext(packageList);
		packageList.addNext(fileList);
		
		fileList.addPrev(packageList);
		packageList.addPrev(moduleList);
	}

	public void onClick(ClickEvent event) {
		
		Element element = event.getRelativeElement();
		
		Resource drillResource = (Resource)element.getPropertyObject("resourceObj");
		Measure drillMeasure = (Measure)element.getPropertyObject("measureObj");
	
		if(drillResource != null)
		{		
			if(drillResource.getQualifier().equals(Resource.QUALIFIER_MODULE))
			{		
				this.moduleList.setSelectedItem(drillResource);
				
				this.packageList.loadData();
				
			} 
			else if(drillResource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
			{								
				this.packageList.setSelectedItem(drillResource);
				
				this.fileList.loadData();
			}
		}
		
		if(drillMeasure != null)
		{				
			// TODO moduleList / packageList können null sein!
			this.moduleList.setSelectedMeasure(drillMeasure);
			this.packageList.setSelectedMeasure(drillMeasure);
			this.fileList.setSelectedMeasure(drillMeasure);
			
			this.moduleList.loadData();
		}
	}
}
 