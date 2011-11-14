package jku.se.drilldown.ui.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;


import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

//Abstrakte Überklasse
public abstract class DrilldownComponent extends Panel{

	public List<Measure> fetchMeasureList(Resource resource, String metrics, int depth, boolean excludeRules){
		final List<Measure> measureList = new LinkedList<Measure>();
		ResourceQuery r_query = ResourceQuery.createForResource(resource, metrics)
				.setDepth(depth)
				.setExcludeRules(excludeRules);
		
		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource resource) {
				if (resource!=null && !resource.getMeasures().isEmpty()) {
					for (Measure measure : resource.getMeasures()) {
						measureList.add(measure);
						//ruleDrilldownGrid.setText(i, 0, measure.getCharacteristicKey());
						//ruleDrilldownGrid.setText(i, 1, measure.getRuleName());
						//ruleDrilldownGrid.setText(i, 2, String.valueOf(measure.getValue()));
					}
				}
			}
		});
		return measureList;
	}
	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget child) {
		// TODO Auto-generated method stub
		return false;
	}
}