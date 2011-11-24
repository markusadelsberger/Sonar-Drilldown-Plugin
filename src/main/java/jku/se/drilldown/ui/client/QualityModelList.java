package jku.se.drilldown.ui.client;

import java.util.List;

import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * NOT IN USE, so far 
 * 
 * 
 * @author Johannes
 *
 */
public class QualityModelList extends DrilldownComponentList<Resource>{

	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget createHeader() {
		return new Label("Quality Models");
	}

	@Override
	public void doLoadData() {

	    Sonar.getInstance().findAll(new ResourceQuery(), new AbstractListCallback<Resource>() {
			
			@Override
			protected void doOnResponse(List<Resource> resourceList) {
				Grid gridList = new Grid(resourceList.size(), gridColumnCount());
				gridList.setStyleName("spaced");
				setGrid(gridList);
							
				int row =0;
				for (Resource item : resourceList) 
				{
					 renderRow(item, row);
					 row++;
				}
				
				render(getGrid());	
			}
	    });
		
	}

	@Override
	public String getItemIdentifier(Resource item) {
		return item.getKey();
	}

	@Override
	public int gridColumnCount() {
		return 3;
	}

	@Override
	public void renderRow(Resource resource, int row) {
		int column = 0;
		renderIconCells(resource, row, column);
		column ++; 
		renderNameCell(resource, row, column);
		column ++; 
		renderUploadCell(resource, row, column);
		
	}

	private void renderUploadCell(Resource resource, int row, int column) {
		/*
		  if(QualityModelContainer.getInstance().findQualityModel(resource)== null)
		 
		{
			getGrid().setWidget(row, column, new FileUpload());
			getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		}
		else
		{
			getGrid().setWidget(row, column, new Label("uploaded"));
			getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		}
		*/
	}

	private void renderNameCell(Resource resource, int row, int column) {
		Anchor link = new Anchor(resource.getName());
		
		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		
	}

	private void renderIconCells(Resource resource, int row, int column) {
		getGrid().setWidget(row, column,new HTML("<div>" + Icons.forQualifier(resource.getQualifier()).getHTML() + "</div>"));
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
		
	}

}
