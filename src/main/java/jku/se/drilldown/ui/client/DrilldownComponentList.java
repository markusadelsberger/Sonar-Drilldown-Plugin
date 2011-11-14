//Abgeleitet von DrilldownComponent, stellt alle Listen dar
package jku.se.drilldown.ui.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;


import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DrilldownComponentList extends DrilldownComponent implements IsWidget{
	public Grid drilldownGrid;
	public Label l;
	public DrilldownComponentList(Resource resource, String metrics, int depth, boolean excludeRules){
		this.add(new Label("Begin"));
		//List<Measure> measureList = fetchMeasureList(resource, metrics, depth, excludeRules);
		ResourceQuery r_query = ResourceQuery.createForResource(resource, metrics)
				.setDepth(depth)
				.setExcludeRules(excludeRules);
		drilldownGrid = new Grid();
		try{
			Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {
				@Override
				protected void doOnResponse(Resource resource) {
					if (resource!=null && !resource.getMeasures().isEmpty()) {
						int i=0;
						drilldownGrid.resize(resource.getMeasures().size(), 3);
						for (Measure measure : resource.getMeasures()) {
							//measureList.add(measure);
							drilldownGrid.setText(i, 0, measure.getCharacteristicKey());
							drilldownGrid.setText(i, 1, measure.getRuleName());
							drilldownGrid.setText(i, 2, String.valueOf(measure.getValue()));
							i++;
						}
					}else{
						l = new Label("Empty");
					}
				}
			});
			this.add(drilldownGrid);
		}catch(Exception e){
			this.add(new Label("In DrilldownComponentList: "+e.toString()));
		}
		if(l!=null){
			this.add(l);
		}else{
			l=new Label("l was null");
			this.add(l);
		}
		
		
	}
	
	public Widget asWidget(){
		return this;
	}
	
	/*
	public DrilldownComponentList(Resource resource, String metrics, int depth, boolean excludeRules){
		drilldownGrid=new Grid();
		final List<Measure> measureList = new LinkedList<Measure>();
		ResourceQuery r_query = ResourceQuery.createForResource(resource, metrics)
				.setDepth(depth)
				.setExcludeRules(excludeRules);
		
		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource resource) {
				if (resource!=null && !resource.getMeasures().isEmpty()) {
					int i=0;
					drilldownGrid.resize(resource.getMeasures().size(), 3);
					for (Measure measure : resource.getMeasures()) {
						//measureList.add(measure);
						drilldownGrid.setText(i, 0, measure.getCharacteristicKey());
						drilldownGrid.setText(i, 1, measure.getRuleName());
						drilldownGrid.setText(i, 2, String.valueOf(measure.getValue()));
						i++;
					}
				}
			}
		});
		return measureList;
	}*/

	
}