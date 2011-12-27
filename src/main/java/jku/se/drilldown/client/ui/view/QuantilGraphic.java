package jku.se.drilldown.client.ui.view;

import java.util.List;

import org.sonar.wsclient.services.Measure;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public class QuantilGraphic extends DrilldownComponent {
	
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	private Panel listPanel;
	private Panel data;
	private Grid grid;
	
	public QuantilGraphic(DrilldownController drilldownController){
		super();
		this.drilldownController=drilldownController;
		this.drilldownModel=drilldownController.getModel();
		listPanel = new VerticalPanel();
		initWidget(listPanel);
		data = new ScrollPanel();
		data.setStyleName("scrollable");
		listPanel.add(data);
		grid = new Grid(6, 2);
		grid.setText(0, 0, "Start");
		render(grid);
	}
	
	
	public void reload()
	{
		if(drilldownModel.getActiveMeasure()!=null){
			Distribution distribution = loadBenchmarkData(drilldownModel.getActiveMeasure().getRuleKey());
			
			grid.setText(0, 0, drilldownModel.getActiveMeasure().getRuleName());
			if(distribution!=null){
				grid.setText(1, 0, "Minimum");
				grid.setText(1, 1, String.valueOf(distribution.getMin()));
				
				grid.setText(2, 0, "Q25");
				grid.setText(2, 1, String.valueOf(distribution.getQ25()));
				
				grid.setText(3, 0, "Median");
				grid.setText(3, 1, String.valueOf(distribution.getMedian()));
				
				grid.setText(4, 0, "Q75");
				grid.setText(4, 1, String.valueOf(distribution.getQ75()));
				
				grid.setText(5, 0, "Maximum");
				grid.setText(5, 1, String.valueOf(distribution.getMax()));
				
				int linesOfCode = drilldownModel.getCount("loc");
				grid.setText(0, 1, String.valueOf((float)drilldownModel.getActiveMeasure().getIntValue()/(float)linesOfCode));
			}else{
				grid.setText(0, 1, "Distribution was null, RuleKey was "+drilldownModel.getActiveMeasure().getRuleKey());
			}
			
		}else{
			//TODO: Kein measure gesetzt, tabelle muss leer sein
		}
		render(grid);
		
	}
	
	private Distribution loadBenchmarkData(String measure){
		BenchmarkData benchmarkData = drilldownModel.getBenchmarkData();
		//The toolname and the rulename are saved in seperate Strings
		String tool=measure.substring(0, measure.indexOf(":"));
		String rule=measure.substring(measure.indexOf(":")+1, measure.length());
		for(BenchmarkTool benchmarkTool : benchmarkData.getTools()){
			if(benchmarkTool.getName().compareToIgnoreCase(tool)==0){
				for(Distribution benchmarkDistribution : benchmarkTool.getDistribution()){
					if(benchmarkDistribution.getName().compareToIgnoreCase(rule)==0){
						return benchmarkDistribution;
					}
				}
			}
		}
		return null;
	}
	
	protected void render(Widget widget) {
		data.clear();
		data.add(widget);
	}
}
