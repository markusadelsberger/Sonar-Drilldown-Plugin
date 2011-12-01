package jku.se.drilldown.client.ui.view;

import java.util.HashMap;
import java.util.List;

import jku.se.drilldown.client.ui.controller.ComponentController;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The component lists the items from a resource according to the defined scope. 
 * Moreover the component is linked with its previous and next component. 
 * So it can get a selected item from the previous list or reload the next list component when its state changes.  
 * 
 * @author Johannes
 *
 */
public class StructureDrilldownList extends DrilldownComponentList<Resource>{

	private String pageID;
	private Resource resource;
	private String scope;
	
	private List<Measure> selectedMeasures;
	
	// child list
	private StructureDrilldownList next;
	// parent list
	private StructureDrilldownList prev;
	
	private ComponentController controller;

	public StructureDrilldownList(Resource resource, String scope, String pageID, ComponentController controller) {
		super();
		
		this.resource=resource;
		this.pageID = pageID;
		this.scope=scope;
		
		this.selectedMeasures=null;
		this.next=null;
		this.prev=null;
		
		this.controller=controller;
	}
 
	@Override
	public Widget createHeader() {
		return new Label("");
	}
	
	@Override
	public int gridColumnCount() {
		if(scope.equals(Resource.SCOPE_ENTITY))
			return 3;
		else 
			return 4;
	}

	@Override
	public void renderRow(Resource item, int row) {
		renderValueCell( item, row, renderNameCell( item, row, renderIconCells(item, row, 0)));
	}
		
	@Override
	public void doLoadData() 
	{
		
		Sonar.getInstance().findAll(getQuery(), new AbstractListCallback<Resource>() {
			
			@Override
			protected void doOnResponse(List<Resource> resourceList) {
				Grid gridList = new Grid(resourceList.size(), gridColumnCount());
				gridList.setStyleName("spaced");
				setGrid(gridList);
				
				HashMap<String,Integer> hashmap= new HashMap<String,Integer>();
			
				if(scope.equals(Resource.SCOPE_SET) && !resourceList.isEmpty())
					resourceList.remove(0);
									
				int row = 0;
				for (Resource item : resourceList) 
				{
					renderRow(item, row);
					hashmap.put(getItemIdentifier(item), new Integer(row));

					row++;
				}
				
				setHashmap(hashmap);
				
				if(containsSelectedItem())
					selectRow(hashmap.get(getItemIdentifier(getSelectedItem())));
						
				render(getGrid());	
				
				if(next!=null)
					next.loadData();	
			}

		});
	}
	
	/**
	 * 
	 * @param resource Contains the corresponding information.
	 * @param row The row of the grid. 
	 * @param column The column of the grid.
	 * @return The next column in which a widget can be presented.
	 */
	private int renderIconCells(Resource resource, int row, int column ) {
		
		if(resource.getQualifier().equals(Resource.QUALIFIER_MODULE)||resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
		{
			getGrid().setWidget(row, column, new HTML("<a id=\"zoom" + row + "\" href=\"" + Links.urlForResourcePage(resource.getKey(), pageID, null)+"\">" + Icons.get().zoom().getHTML() + "</a>"));
			getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
			
			column++;
		}
			
		getGrid().setWidget(row, column,new HTML("<div>" + Icons.forQualifier(resource.getQualifier()).getHTML() + "</div>"));
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		
		column++;
		
		return column;
	}

	private int renderNameCell(final Resource resource, int row, int column) {
		Anchor link = new Anchor(resource.getName());
		
		// add resource object to link element
	    link.getElement().setPropertyObject("resourceObj", resource);
		

    	if(resource.getQualifier().equals(Resource.QUALIFIER_MODULE)||resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
		{
    	    // register listener to the component
        	link.addClickHandler(this);
		}
    	else
    	{
    		link.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                	if(selectedMeasures != null)
                		Links.openMeasurePopup(resource.getKey(), selectedMeasures.get(0).getRuleKey());
                	else
                		Links.openResourcePopup(resource.getKey());
                }
              });
    	}
    	
		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		
		column++;
		
		return column;
	}

	private void renderValueCell(Resource resource, int row, int column) {
		
		if((this.selectedMeasures!=null) && (this.selectedMeasures.size()>0))
		{
			int sumOfValues=0;
			
			for(Measure measure : resource.getMeasures())
				sumOfValues += Integer.parseInt(measure.getFormattedValue());

			getGrid().setHTML(row, column, ""+sumOfValues);
		}
		else
			getGrid().setHTML(row, column, resource.getMeasureValue(Metrics.VIOLATIONS).toString());
		
		getGrid().getCellFormatter().setStyleName(row, column,getRowCssClass(row, false));
	}

	@Override
	public String getItemIdentifier(Resource item) {
		return item.getKey();
	}
	
	@Override
	public Resource getSelectedItem()
	{
		if(containsSelectedItem())
			return super.getSelectedItem();
		else
			return null;
	}
	
	/**
	 * Builds a resource query in consideration of the scope and selected measures. 
	 * 
	 * @return A resource query specified for the component's scope. 
	 */
	private ResourceQuery getQuery()
	{
		Resource queryResource = getRootResource();
		
		ResourceQuery query = ResourceQuery.createForResource(queryResource, Metrics.VIOLATIONS)
	    		.setScopes(scope)
	    		.setDepth(-1);
		
		if(this.selectedMeasures!= null)
		{
			String[] selectedRuleKeys = new String[this.selectedMeasures.size()];
			
			int i =0;
			for(Measure measure : selectedMeasures)
			{
				selectedRuleKeys[i]=measure.getRuleKey();
				i++;
			}
					
			query.setRules(selectedRuleKeys);
		}
		
		return query;
	}
	
	/**
	 * Methods works recursive in the case the previous component is set and has no selected item.  
	 * 
	 * @return
	 */
	private Resource getRootResource()
	{
		if(prev==null)
		{
			return resource;
		} 
		else if (prev.getSelectedItem()!= null)
		{
			return prev.getSelectedItem();
		} 
		else
		{
			return prev.getRootResource();
		}
	}
	
	public void setSelectedMeasures(List<Measure> selectedMeasures) {
		this.selectedMeasures = selectedMeasures;
	}
	
	
	public void setNext(StructureDrilldownList next)
	{
		this.next=next;	
	}
	
	public void setPrev(StructureDrilldownList prev)
	{
		this.prev=prev;
	}

	/**
	 *  Click handler to react on clicks from the list component.
	 * 
	 *  Sets the selected item and invokes its child component to refresh. 
	 */
	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		
		Resource drillResource = (Resource)element.getPropertyObject("resourceObj");
		
	
		if(drillResource != null)
		{		
			if(drillResource.getQualifier().equals(Resource.QUALIFIER_MODULE)||drillResource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
			{				
				this.setSelectedItem(drillResource);
				this.next.loadData();
				
				this.controller.onSelectedItemChanged("structure");
			} 
		}	
	}
}
