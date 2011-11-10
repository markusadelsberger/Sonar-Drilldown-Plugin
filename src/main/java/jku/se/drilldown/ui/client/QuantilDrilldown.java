package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.user.client.ui.Grid;

public class QuantilDrilldown extends DrilldownComponent {
	private Grid grid;
	public QuantilDrilldown(Resource resource){
		List<Measure> measures = fetchMeasures(resource);
		if(!measures.isEmpty()){
			grid = new Grid(measures.size(),3);
			int i = 0;
			for(Measure m : measures){
				grid.setText(i, 0, "[]");
				grid.setText(i, 1, "<a href=\""+m.getRuleName()+"\">"+m.getRuleName()+"</a>");
				grid.setText(i, 2, String.valueOf(m.getValue()));
				i++;
			}
		}
	}
	
	private List<Measure> fetchMeasures(Resource resource){
		final List<Measure> measureList = new LinkedList<Measure>();
		ResourceQuery r_query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
				.setDepth(0)
				.setExcludeRules(false);

		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				if (resource==null || resource.getMeasures().isEmpty()) {
					;
				} else {
					//TODO: Vorläufige implementierung
					for (Measure measure : resource.getMeasures()) {
						measureList.add(measure);
					}
				}
			}
		});
		return measureList;
	}
}
