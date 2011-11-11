package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
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
	
	private DrilldownComponentList moduleList;
	private DrilldownComponentList packageList;
	private DrilldownComponentList fileList;
	
	public StructureDrilldownComponent(Resource resource){
		this.resource=resource;
		
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
	
	protected void loadData() {
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
					
					moduleList = new DrilldownComponentList(resources, 0, clickHandler);
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
					packageList = new DrilldownComponentList(resources, 0, clickHandler);
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
					fileList = new DrilldownComponentList(resources, 0, null);
					structurePanel.add(fileList);
				}
			});
		}
	}

	public void onClick(ClickEvent event) {
		
		Element element = event.getRelativeElement();
		Resource drillResource = (Resource) element.getPropertyObject("resourceObj");
		
		if(drillResource != null)
		{
			int row = Integer.parseInt(element.getAttribute("gridRow"));
			
			if(drillResource.getQualifier().equals(Resource.QUALIFIER_MODULE))
			{				
				this.moduleList.setSelectedRow(row);
				
				updatePackageList(drillResource);
				updateFileList(drillResource);
			} 
			else if(drillResource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
			{								
				this.packageList.setSelectedRow(row);
				
				updateFileList(drillResource);
			}
		}
	}
	
	private void updatePackageList(Resource drillResource)
	{
		ResourceQuery query = ResourceQuery.createForResource(drillResource, Metrics.VIOLATIONS)
		    	.setScopes(STRUCTURE[1])
		    	.setDepth(-1);
			
		Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
			@Override
			protected void doOnResponse(List<Resource> resources) {
				packageList.setResourceList(resources);
				packageList.loadData();
			}
		});
	}
	
	private void updateFileList(Resource drillResource)
	{
		ResourceQuery query = ResourceQuery.createForResource(drillResource, Metrics.VIOLATIONS)
		    	.setScopes(STRUCTURE[2])
		    	.setDepth(-1);
			
		Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
			@Override
			protected void doOnResponse(List<Resource> resources) {
				fileList.setResourceList(resources);
				fileList.loadData();
			}
		});
	}
}
 