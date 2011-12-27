package jku.se.drilldown.client.ui.view;

import java.util.List;

import org.sonar.wsclient.services.Measure;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
	private HorizontalPanel horizontalPanel;
	private VerticalPanel verticalPanel;
	
	public QuantilGraphic(DrilldownController drilldownController){
		super();
		this.drilldownController=drilldownController;
		this.drilldownModel=drilldownController.getModel();
		listPanel = new VerticalPanel();
		initWidget(listPanel);
		listPanel.add(new Label("Benchmark Quantil Graphic"));
		data = new ScrollPanel();
	
		listPanel.add(data);
		
		doOnLoad();
	}
	
	private void doOnLoad(){
		grid = new Grid(6, 2);
		
		grid.setStyleName("spaced");
		
		grid.setText(0, 0, "Your Value");
		grid.setText(1, 0, "Minimum");
		grid.setText(2, 0, "Q25");
		grid.setText(3, 0, "Median");
		grid.setText(4, 0, "Q75");
		grid.setText(5, 0, "Maximum");
		
		grid.getRowFormatter().setStyleName(0, getRowCssClass(0, false));
		grid.getRowFormatter().setStyleName(1, getRowCssClass(1, false));
		grid.getRowFormatter().setStyleName(2, getRowCssClass(2, false));
		grid.getRowFormatter().setStyleName(3, getRowCssClass(3, false));
		grid.getRowFormatter().setStyleName(4, getRowCssClass(4, false));
		grid.getRowFormatter().setStyleName(5, getRowCssClass(5, false));
		
		grid.setWidth("200px");
		
		horizontalPanel=new HorizontalPanel();
		verticalPanel=new VerticalPanel();
		
		verticalPanel.add(grid);
		
		horizontalPanel.add(verticalPanel);
		render(horizontalPanel);
	}
	
	
	public void reload()
	{
		horizontalPanel=new HorizontalPanel();
		verticalPanel=new VerticalPanel();
		if(drilldownModel.getActiveMeasure()!=null){
			Distribution distribution = loadBenchmarkData(drilldownModel.getActiveMeasure().getRuleKey());
			
			if(distribution!=null){
				//fetch the values
				float min = distribution.getMin();
				float q25 = distribution.getQ25();
				float median = distribution.getMedian();
				float q75 = distribution.getQ75();
				float max = distribution.getMax();
				//value of the chosen distribution
				float value = (float)drilldownModel.getActiveMeasure().getIntValue()/(float)drilldownModel.getCount("loc");
				
				//get the position of the line on the scale
				int pos = getLinePos(min, q25, median, q75, max, value);
				//Window.alert(String.valueOf(pos));
				
				NumberFormat format = NumberFormat.getScientificFormat();

				//set the data in the grid
				grid.setText(0, 1, String.valueOf(format.format(value)));
				grid.setText(1, 1, String.valueOf(format.format(min)));
				grid.setText(2, 1, String.valueOf(format.format(q25)));
				grid.setText(3, 1, String.valueOf(format.format(median)));
				grid.setText(4, 1, String.valueOf(format.format(q75)));
				grid.setText(5, 1, String.valueOf(format.format(max)));
				
				verticalPanel.add(grid);
				
				horizontalPanel.add(verticalPanel);
				horizontalPanel.add(getScale(pos));
				
			}
		}else{
			grid.setText(0, 1, "");
			grid.setText(1, 1, "");
			grid.setText(2, 1, "");
			grid.setText(3, 1, "");
			grid.setText(4, 1, "");
			grid.setText(5, 1, "");
			
			verticalPanel.add(new Label(drilldownModel.getActiveMeasure().getRuleName()));
			verticalPanel.add(grid);
			
			horizontalPanel.add(verticalPanel);
			horizontalPanel.add(getScale(null));
		}

		render(horizontalPanel);
		
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
	
	private HTML getScale(Integer value){
		String blackLines = 
				"<!-- min line -->"+
				"<line x1=\"10\" y1=\"10\" x2=\"10\" y2=\"90\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- q25 line -->"+
				"<line x1=\"110\" y1=\"10\" x2=\"110\" y2=\"90\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- median line -->"+
				"<line x1=\"210\" y1=\"10\" x2=\"210\" y2=\"90\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- q75 line -->"+
				"<line x1=\"310\" y1=\"10\" x2=\"310\" y2=\"90\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- maximum line -->"+
				"<line x1=\"410\" y1=\"10\" x2=\"410\" y2=\"90\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- vertical -->"+
				"<line x1=\"10\" y1=\"50\" x2=\"410\" y2=\"50\" style=\"stroke-width=10; stroke: black;\"/>";
		String svg = "";
		if(value!=null){
			svg = 
					"<svg width=\"520px\" height=\"100px\" viewBox=\"0 0 520 100\">" +
							blackLines +
							"<!-- value line -->"+
							"<line x1=\""+value+"\" y1=\"10\" x2=\""+value+"\" y2=\"90\" style=\"stroke-width=10; stroke: green;\"/>" +
					"</svg>";
		}else{
			svg = 
					"<svg width=\"520px\" height=\"100px\" viewBox=\"0 0 520 100\">" +
							blackLines +
					"</svg>";
		}
		
		HTML scale = new HTML(svg);
		
		return scale;
	}
	
	public int getLinePos(float min, float q25, float median, float q75, float max, float value){
		
		if(value<min){
			return (int)((value/min)*100)+10;
		}else if(value<q25){
			return (int) ((value/q25)*100)+10;
		}else if(value<median){
			return (int) ((value/median)*100)+110;
		}else if(value<q75){
			return (int) ((value/q75)*100)+210;
		}else{
			return (int) ((value/max)*100)+310;
		}
	}
	
	protected String getRowCssClass(int row, boolean selected) {
		return row % 2 == 0 ? "even" + getRowCssSelected(selected) : "odd" + getRowCssSelected(selected);
	}
	
	private String getRowCssSelected(boolean selected) {
		return selected ? " selected" : "";
	}
}
