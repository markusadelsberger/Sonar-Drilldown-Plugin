package jku.se.drilldown.qm.client;

import java.util.HashMap;
import java.util.Map;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MeasuresList extends DrilldownComponentList<Measure> {

	private Resource resource;
	private ComponentController controller; 
	
	public MeasuresList(Resource resource, ComponentController controller) {
		super();
		
		this.resource=resource;
		this.controller=controller;
	}
	
	@Override
	public Widget createHeader() {
		return new Label("");
	}

	@Override
	public int gridColumnCount() {
		return 1;
	}

	@Override
	public void renderRow(Measure item, int row) {
		renderNameCell(item, row, 0);
	}

	private void renderNameCell(Measure measure, int row, int column) {
		Anchor link = new Anchor(measure.getRuleName());

		// add resource object to link element
	    link.getElement().setPropertyObject("measureObj", measure);
		
	    // register listener
    	link.addClickHandler(this);

		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	@Override
	public String getItemIdentifier(Measure item) {
		
		return item.getRuleKey();
	}

	@Override
	public void doLoadData() {
		ResourceQuery r_query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
	            .setDepth(0)
	            .setExcludeRules(false);
	    
	    Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {

	        @Override
	        protected void doOnResponse(Resource resource) {
	        	
	          if (resource==null || resource.getMeasures().isEmpty()) {
	            ;
	          } 
	          else 
	          {	  
	          	  setGrid(new Grid(resource.getMeasures().size(), gridColumnCount()));
					
				  Map<String,Integer> hashmap= new HashMap<String,Integer>();
					
				  int row = 0;
					
				  for (Measure item : resource.getMeasures()) 
				  {
					  renderRow(item, row);
					
					  hashmap.put(getItemIdentifier(item), new Integer(row));

					  row++;
				  }
					
				  setHashmap(hashmap);
					
				  if(containsSelectedItem())
					  selectRow(hashmap.get(getItemIdentifier(getSelectedItem())));
						
				  render(getGrid());	
	        	  
	          }
	        }
	   });
		
	}

	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		
		Measure selectedMeasure = (Measure)element.getPropertyObject("measureObj");

		if(selectedMeasure != null)
		{		
			this.setSelectedItem(selectedMeasure);
			controller.onSelectedItemChanged("rule");
		} 
	}
}
