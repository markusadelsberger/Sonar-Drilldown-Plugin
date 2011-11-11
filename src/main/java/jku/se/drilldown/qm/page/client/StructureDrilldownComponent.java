package jku.se.drilldown.qm.page.client;

import java.util.ArrayList;
import java.util.List;

import org.sonar.gwt.Configuration;
import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Component decomposes a resource object in its folder, packages and files.  
 * 
 * @author Johannes
 *
 */
public class StructureDrilldownComponent extends DrilldownComponent{
	
	private Panel verticalPanel;
	private Panel structurePanel;
	private Resource resource;
	
	final static String[] STRUCTURE = {Resource.SCOPE_SET, Resource.SCOPE_SPACE, Resource.SCOPE_ENTITY};
	
	private int selectedProject = 0;
	private int selectedModule = 0;
	
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
		
		selectedProject=0;
		if(Configuration.getRequestParameter(Resource.QUALIFIER_PROJECT) != null)
			selectedProject = Integer.parseInt(Configuration.getRequestParameter(Resource.QUALIFIER_PROJECT));
		
		selectedModule=0;
		if(Configuration.getRequestParameter(Resource.QUALIFIER_MODULE)!= null)
			selectedModule = Integer.parseInt(Configuration.getRequestParameter(Resource.QUALIFIER_MODULE));
		
		boolean cont= false;
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_PROJECT)){
			ResourceQuery query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
		    		.setScopes(STRUCTURE[0])
		    		.setDepth(-1);
			
			Sonar.getInstance().findAll(query, new AbstractListCallback<Resource>() {
				
				@Override
				protected void doOnResponse(List<Resource> resources) {
					structurePanel.add(new DrilldownComponentList(resources, selectedProject, Resource.QUALIFIER_PROJECT ));
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
					if(selectedProject == 0)
						structurePanel.add(new DrilldownComponentList(resources, selectedModule, Resource.QUALIFIER_MODULE));
					else
						structurePanel.add(new DrilldownComponentList(resources, selectedModule, Resource.QUALIFIER_PROJECT+"="+selectedProject+"&"+Resource.QUALIFIER_MODULE));
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
					structurePanel.add(new DrilldownComponentList(resources, 0, null));
				}
			});
		}
	}
}
 