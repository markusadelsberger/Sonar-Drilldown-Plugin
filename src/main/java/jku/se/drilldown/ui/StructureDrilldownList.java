package jku.se.drilldown.ui;

import java.util.List;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
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
	
	public StructureDrilldownList(List<Resource> resourceList, ClickHandler clickHandler, String pageID) {
		super(resourceList, clickHandler);
		
		this.pageID = pageID;
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
}
