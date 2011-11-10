package jku.se.drilldown.ui.client;

import java.util.LinkedList;
import java.util.List;


import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.DefaultSourcePanel;
import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkViewerPanel extends Page {
	private VerticalPanel measure_pan;
	private HorizontalPanel mainPanel;
	public static final int LIMIT = 5;
	private Grid drilldownGrid;
	private Grid ruleDrilldownGrid;

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		final VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Drilldown"));

		measure_pan = new VerticalPanel();
		mainPanel = new HorizontalPanel();
		ruleDrilldownGrid = new Grid();
		drilldownGrid = new Grid(5, 4);
		
		ruleDrilldownGrid.setStyleName("spacedIcon");
		drilldownGrid.setStyleName("spacedIcon");
		
		drilldownGrid.getRowFormatter().addStyleName(0, "even");
		drilldownGrid.setText(0, 0, "[]");
		drilldownGrid.setText(0, 1, "Blocker");
		drilldownGrid.setText(0, 2, "0");
		drilldownGrid.setText(0, 3, "|");

		drilldownGrid.getRowFormatter().addStyleName(1, "odd");
		drilldownGrid.setText(1, 0, "[]");
		drilldownGrid.setText(1, 1, "Critical");
		drilldownGrid.setText(1, 2, "173");
		drilldownGrid.setText(1, 3, "|");
		
		drilldownGrid.getRowFormatter().addStyleName(2, "even");
		drilldownGrid.setText(2, 0, "[]");
		drilldownGrid.setText(2, 1, "Major");
		drilldownGrid.setText(2, 2, "8.281");
		drilldownGrid.setText(2, 3, "||||||||||");
		
		drilldownGrid.getRowFormatter().addStyleName(3, "odd");
		drilldownGrid.setText(3, 0, "[]");
		drilldownGrid.setText(3, 1, "Minor");
		drilldownGrid.setText(3, 2, "2.344");
		drilldownGrid.setText(3, 3, "|||||||");
		
		drilldownGrid.getRowFormatter().addStyleName(4, "even");
		drilldownGrid.setText(4, 0, "[]");
		drilldownGrid.setText(4, 1, "Info");
		drilldownGrid.setText(4, 2, "145");
		drilldownGrid.setText(4, 3, "|");
		
		ResourceQuery r_query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
				.setDepth(0)
				.setExcludeRules(false);
		
		
		Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				if (resource!=null && !resource.getMeasures().isEmpty()) {
					ruleDrilldownGrid.resize(resource.getMeasures().size(), 4);
					int i = 0;
					for (Measure measure : resource.getMeasures()) {
						ruleDrilldownGrid.setText(i, 0, measure.getCharacteristicKey());
						ruleDrilldownGrid.setText(i, 1, measure.getRuleName());
						ruleDrilldownGrid.setText(i, 2, String.valueOf(measure.getValue()));
						i++;
					}
				}
			}
		});
		
		mainPanel.add(drilldownGrid);
		mainPanel.add(ruleDrilldownGrid);
		
		final ListBox widget1 = new ListBox();
		widget1.setVisibleItemCount(8);

		ViolationQuery query = ViolationQuery.createForResource(resource);
		Sonar.getInstance().findAll(query, new AbstractListCallback<Violation>() {

			@Override
			protected void doOnResponse(List<Violation> result) {
				for(Violation violation : result )
				{
					widget1.addItem(violation.getRuleKey()+" "+violation.getMessage()+" "+violation.getResourceKey());
				}
			}



		});

		measure_pan.add(widget1);


		panel.add(mainPanel);
		panel.add(measure_pan);

		return panel;

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