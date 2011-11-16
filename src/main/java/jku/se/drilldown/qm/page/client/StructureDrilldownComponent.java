package jku.se.drilldown.qm.page.client;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
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
	
	private MeasuresList measureList;
	
	private String pageID; 
	
	/**
	 * 
	 * @param resource
	 * @param pageID The gwtID of the page that contains this element. for example "jku.se.drilldown.qm.page.QMDrilldownPage" 
	 */
	public StructureDrilldownComponent(Resource resource, String pageID){
		this.resource=resource;
		this.pageID= pageID;
		
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
		structurePanel = new Grid(1,5);
		verticalPanel.add(structurePanel);
		loadData();
	}
	
	private void loadData() {
		structurePanel.clear();
	
		boolean cont= false;
		
		final ClickHandler clickHandler = this;
		
		measureList = new MeasuresList(resource, clickHandler);
		structurePanel.setWidget(0, 0, measureList);
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			moduleList = new StructureDrilldownList(resource, STRUCTURE[0], clickHandler, pageID);
			structurePanel.setWidget(0, 1, moduleList);
			
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || cont){
			packageList = new StructureDrilldownList(resource, STRUCTURE[1], clickHandler, pageID);
			structurePanel.setWidget(0, 2, packageList);
			
			if(cont)
				packageList.addPrev(moduleList);
			
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || cont){
			fileList = new StructureDrilldownList(resource, STRUCTURE[2], null, pageID);
			structurePanel.setWidget(0, 3, fileList);
			
			if(cont)
				fileList.addPrev(packageList);	
		}
		
		if(packageList!=null)
		{
			packageList.addNext(fileList);
			
			if(moduleList!=null)
				moduleList.addNext(packageList);
		}
	}

	public void onClick(ClickEvent event) {
		
		Element element = event.getRelativeElement();
		
		Resource drillResource = (Resource)element.getPropertyObject("resourceObj");
		Measure drillMeasure = (Measure)element.getPropertyObject("measureObj");
	
		if(drillResource != null)
		{		
			if(drillResource.getQualifier().equals(Resource.QUALIFIER_MODULE))
			{		
				moduleList.setSelectedItem(drillResource);
				
				packageList.loadData();
				
			} 
			else if(drillResource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
			{								
				packageList.setSelectedItem(drillResource);
							
				fileList.loadData();
			}
		}
		
		if(drillMeasure != null)
		{		

			measureList.setSelectedItem(drillMeasure);
			
			StructureDrilldownList startReloadingComp = null;
			
			if(moduleList!= null)
			{
				moduleList.setSelectedMeasure(drillMeasure);
				startReloadingComp = moduleList;
			}
			
			if(packageList!= null)
			{
				this.packageList.setSelectedMeasure(drillMeasure);
				if(startReloadingComp==null)
					startReloadingComp= packageList;
			}

			if(fileList!= null)
			{
				this.fileList.setSelectedMeasure(drillMeasure);
				if(startReloadingComp==null)
					startReloadingComp= fileList;
			}
			
			if(startReloadingComp != null)
				startReloadingComp.loadData();
		}
	}
}
 