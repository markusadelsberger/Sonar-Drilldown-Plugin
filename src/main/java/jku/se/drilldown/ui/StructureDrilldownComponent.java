package jku.se.drilldown.ui;

import java.util.List;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	private Panel structurePanel;
	private Resource resource;
	
	final static String[] STRUCTURE = {Resource.SCOPE_SET, Resource.SCOPE_SPACE, Resource.SCOPE_ENTITY};
	
	private DrilldownComponentList<Resource> moduleList;
	private DrilldownComponentList<Resource> packageList;
	private DrilldownComponentList<Resource> fileList;
	
	private Measure selectedMeasure;
	
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
		structurePanel = new HorizontalPanel();
		verticalPanel.add(structurePanel);
		loadData();
	}
	
	private void loadData() {
		structurePanel.clear();
	
		boolean cont= false;
		
		final ClickHandler clickHandler = this;
		
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			ResourceQuery query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
		    		.setScopes(STRUCTURE[0])
		    		.setDepth(-1);
			
			Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
				@Override
				protected void doOnResponse(List<Resource> resources) {
					resources.remove(0);
					
					moduleList = new StructureDrilldownList(resources, clickHandler, pageID);
					structurePanel.add(moduleList);
				}
			});
			
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_MODULE) || cont){
			ResourceQuery query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
			    	.setScopes(STRUCTURE[1])
			    	.setDepth(-1);
			
			Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
					
				@Override
				protected void doOnResponse(List<Resource> resources) {
					packageList = new StructureDrilldownList(resources, clickHandler, pageID);
					structurePanel.add(packageList);
				}
			});
				
			cont =true;
		} 
		
		if (resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE) || cont){
			ResourceQuery query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
			    	.setScopes(STRUCTURE[2])
			    	.setDepth(-1);
				
			Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
					
				@Override
				protected void doOnResponse(List<Resource> resources) {
					fileList = new StructureDrilldownList(resources, null, pageID);
					structurePanel.add(fileList);
				}
			});
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
				this.moduleList.setSelectedItem(drillResource);
				
				updatePackageList(this.moduleList.getSelectedItem());
				
				if(((StructureDrilldownList)packageList).getSelectedItem()!= null)
					updateFileList(((StructureDrilldownList)packageList).getSelectedItem());
				else
					updateFileList(((StructureDrilldownList)moduleList).getSelectedItem());
				
			} 
			else if(drillResource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
			{								
				this.packageList.setSelectedItem(drillResource);
				
				updateFileList(this.packageList.getSelectedItem());
			}
		}
		
		if(drillMeasure != null)
		{		
			
			this.selectedMeasure=drillMeasure;
			
			//TODO: moduleList/packageListe können null sein
			updateModuleList(resource);
			
			if(((StructureDrilldownList)moduleList).getSelectedItem()!= null)
				updatePackageList(((StructureDrilldownList)moduleList).getSelectedItem());
			else
				updatePackageList(resource);
			
			if(((StructureDrilldownList)packageList).getSelectedItem()!= null)
				updateFileList(((StructureDrilldownList)packageList).getSelectedItem());
			else if(((StructureDrilldownList)moduleList).getSelectedItem()!= null)
				updateFileList(((StructureDrilldownList)moduleList).getSelectedItem());
			else
				updateFileList(resource);
		}
	}
	
	private void updateModuleList(final Resource drillResource)
	{
		ResourceQuery query = ResourceQuery.createForResource(drillResource, Metrics.VIOLATIONS)
		    	.setScopes(STRUCTURE[0])
		    	.setDepth(-1);
		
		if(this.selectedMeasure!= null)
			query.setRules(selectedMeasure.getRuleKey());
			
		Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
			@Override
			protected void doOnResponse(List<Resource> resources) {
				resources.remove(0);
				moduleList.setItemList(resources);
				moduleList.loadData();
			}
		});
	}
	
	private void updatePackageList(final Resource drillResource)
	{
		ResourceQuery query = ResourceQuery.createForResource(drillResource, Metrics.VIOLATIONS)
		    	.setScopes(STRUCTURE[1])
		    	.setDepth(-1);
		
		if(this.selectedMeasure!= null)
			query.setRules(selectedMeasure.getRuleKey());
			
		Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
			@Override
			protected void doOnResponse(List<Resource> resources) {
				packageList.setItemList(resources);
				packageList.loadData();
			}
		});
	}
	
	private void updateFileList(final Resource drillResource)
	{
		ResourceQuery query = ResourceQuery.createForResource(drillResource, Metrics.VIOLATIONS)
		    	.setScopes(STRUCTURE[2])
		    	.setDepth(-1);
		
		if(this.selectedMeasure!= null)
			query.setRules(selectedMeasure.getRuleKey());
		
		Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
			@Override
			protected void doOnResponse(List<Resource> resources) {
				fileList.setItemList(resources);
				fileList.loadData();
			}
		});
	}
}
 