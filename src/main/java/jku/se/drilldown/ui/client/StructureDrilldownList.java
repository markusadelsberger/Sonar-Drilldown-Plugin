package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.List;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Johannes
 *
 */
public class StructureDrilldownList extends DrilldownComponentList<Resource> {

	private String pageID;
	private Resource resource;
	private String scope;
	private Measure selectedMeasure;
	private StructureDrilldownList next;
	private StructureDrilldownList prev;

	public StructureDrilldownList(Resource resource, String scope, ClickHandler clickHandler, String pageID) {
		super(clickHandler);
		
		this.resource=resource;
		this.pageID = pageID;
		this.scope=scope;
		
		this.selectedMeasure=null;
		this.next=null;
		this.prev=null;
	}
 
	@Override
	public Widget createHeader() {
		return new Label("");
	}
	
	@Override
	public int gridColumnCount() {
		return 4;
	}

	@Override
	public void renderRow(Resource item, int row) {
		renderIconCells(item, row);
		renderNameCell( item, row, 2);
		renderValueCell( item, row, 3);
	}
		
	@Override
	public void doLoadData() 
	{
		
		Sonar.getInstance().findAll(getQuery(), new AbstractListCallback<Resource>() {
			
			@Override
			protected void doOnResponse(List<Resource> resourceList) {
				setGrid(new Grid(resourceList.size(), gridColumnCount()));
				
				HashMap<String,Integer> hashmap= new HashMap<String,Integer>();
				
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
	
	private void renderIconCells(Resource resource, int row ) {
		if(resource.getQualifier().equals(Resource.QUALIFIER_MODULE)||resource.getQualifier().equals(Resource.QUALIFIER_PACKAGE))
		{
			getGrid().setWidget(row, 0, new HTML("<a id=\"zoom" + row + "\" href=\"" + Links.urlForResourcePage(resource.getKey(), pageID, null)+"\">" + Icons.get().zoom().getHTML() + "</a>"));
			getGrid().getCellFormatter().setStyleName(row, 0, getRowCssClass(row, false));
		}
			
		getGrid().setWidget(row, 1,new HTML("<div>" + Icons.forQualifier(resource.getQualifier()).getHTML() + "</div>"));
		getGrid().getCellFormatter().setStyleName(row, 1, getRowCssClass(row, false));
	}

	private void renderNameCell(final Resource resource, int row, int column) {
		Anchor link = new Anchor(resource.getName());
		
		// add resource object to link element
	    link.getElement().setPropertyObject("resourceObj", resource);
		
	    // register listener
	    if(getClickHandler() != null)
	    	link.addClickHandler(getClickHandler());

		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	private void renderValueCell(Resource resource, int row, int column) {
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
	
	private ResourceQuery getQuery()
	{
		Resource queryResource = getRootResource();
		
		ResourceQuery query = ResourceQuery.createForResource(queryResource, Metrics.VIOLATIONS)
	    		.setScopes(scope)
	    		.setDepth(-1);
		
		if(this.selectedMeasure!= null)
			query.setRules(selectedMeasure.getRuleKey());
		
		return query;
	}
	
	public Resource getRootResource()
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
	
	public void setSelectedMeasure(Measure selectedMeasure) {
		this.selectedMeasure = selectedMeasure;
	}
	
	public void addNext(StructureDrilldownList next)
	{
		this.next=next;	
	}
	
	public void addPrev(StructureDrilldownList prev)
	{
		this.prev=prev;
	}
	

}
