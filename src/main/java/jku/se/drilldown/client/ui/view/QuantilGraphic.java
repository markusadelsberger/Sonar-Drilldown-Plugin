package jku.se.drilldown.client.ui.view;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class QuantilGraphic extends DrilldownComponent {
	
	private DrilldownModel drilldownModel;
	private Panel listPanel;
	private Panel data;
	private Grid grid;
	private HorizontalPanel horizontalPanel;
	private VerticalPanel leftPanel;
	private VerticalPanel rightPanel;
	
	public QuantilGraphic(DrilldownController drilldownController){
		super(drilldownController);

		this.drilldownModel=drilldownController.getModel();
		listPanel = new VerticalPanel();
		initWidget(listPanel);
		
		listPanel.add(new Label("Benchmark Quantil Graphic"));
		data = new ScrollPanel();
		data.setStyleName("scrollable");
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
		leftPanel=new VerticalPanel();
		rightPanel=new VerticalPanel();
		
		leftPanel.add(grid);
		
		horizontalPanel.add(leftPanel);
		horizontalPanel.add(rightPanel);
		render(horizontalPanel);
	}
	
	@Override
	public void reload(ViewComponents viewComponent)
	{
		switch(viewComponent){
			case BENCHMARKDRILLDOWN:
			case RULEDRILLDOWN:
				
				horizontalPanel=new HorizontalPanel();
				leftPanel=new VerticalPanel();
				rightPanel=new VerticalPanel();
				
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
						
						int pos = -3;
						//check if value is smaller than min or bigger than max, if so the getScale method reacts differently
						if(value<min){
							pos=-2;
						}else if(value>max){
							pos=-1;
						}else{
							//get the position of the line on the scale
							pos = getLinePos(q25, median, q75, max, value);
						}
								
						NumberFormat format = NumberFormat.getScientificFormat();
		
						//set the data in the grid
						grid.setText(0, 1, String.valueOf(format.format(value)));
						grid.setText(1, 1, String.valueOf(format.format(min)));
						grid.setText(2, 1, String.valueOf(format.format(q25)));
						grid.setText(3, 1, String.valueOf(format.format(median)));
						grid.setText(4, 1, String.valueOf(format.format(q75)));
						grid.setText(5, 1, String.valueOf(format.format(max)));
				
						
						rightPanel.add(new Label(drilldownModel.getActiveMeasure().getRuleName()));
						rightPanel.add(getScale(pos, min, q25, median, q75, max, value));
					}else{
						grid.setText(0, 1, "");
						grid.setText(1, 1, "");
						grid.setText(2, 1, "");
						grid.setText(3, 1, "");
						grid.setText(4, 1, "");
						grid.setText(5, 1, "");
					}
				}else{
					grid.setText(0, 1, "");
					grid.setText(1, 1, "");
					grid.setText(2, 1, "");
					grid.setText(3, 1, "");
					grid.setText(4, 1, "");
					grid.setText(5, 1, "");
				}
			
				leftPanel.add(grid);
				horizontalPanel.add(leftPanel);
				horizontalPanel.add(rightPanel);
				render(horizontalPanel);
				
				break; //RULEDRILLDOWN
				
			default: break;
		}
		
	}
	
	private Distribution loadBenchmarkData(String measure){
		BenchmarkData benchmarkData = drilldownModel.getBenchmarkData();
		//The toolname and the rulename are saved in seperate Strings
		
		String tool=measure.substring(0, measure.indexOf(':'));
		String rule=measure.substring(measure.indexOf(':')+1, measure.length());
		
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
	
	private HTML getScale(int position, float min, float q25, float median, float q75, float max, float value){
		int x = 50;
		int y1 = 20;
		int y2 = 100;
		
		NumberFormat format = NumberFormat.getScientificFormat();
				
		String data = "<text x=\""+(x)+"\" y=\""+(y2+15)+"\" style=\"text-anchor: middle;\">"+format.format(min).toString()+"</text>"+
					"<text x=\""+(x+100)+"\" y=\""+(y2+15)+"\" style=\"text-anchor: middle;\">"+format.format(q25).toString()+"</text>"+
					"<text x=\""+(x+200)+"\" y=\""+(y2+15)+"\" style=\"text-anchor: middle;\">"+format.format(median).toString()+"</text>"+
					"<text x=\""+(x+300)+"\" y=\""+(y2+15)+"\" style=\"text-anchor: middle;\">"+format.format(q75).toString()+"</text>"+
					"<text x=\""+(x+400)+"\" y=\""+(y2+15)+"\" style=\"text-anchor: middle;\">"+format.format(max).toString()+"</text>";
					
		if(position>-1){
			data +=	"<!-- value line -->"+
					"<text x=\""+(position+x)+"\" y=\"15\"  style=\"text-anchor: middle;\">"+format.format(value).toString()+"</text>"+
					"<line x1=\""+(position+x)+"\" y1=\""+y1+"\" x2=\""+(position+x)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: green;\"/>";	
		}else if(position==-2){
			data += "<!-- value below min -->"+
					"<line x1=\"20\" y1=\""+y1+"\" x2=\"20\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: red;\"/>" +
					"<line x1=\"20\" y1=\""+((y1+y2)/2)+"\" x2=\"50\" y2=\""+((y1+y2)/2)+"\" style=\"stroke-width=10; stroke: red; stroke-dasharray:2,2;\"/>" +
					"<text x=\"20\" y=\"15\" style=\"text-anchor: middle;\">"+format.format(value).toString()+"</text>";
		}else if(position==-1){
			data += "<!-- value above max -->"+
					"<line x1=\""+(x+420)+"\" y1=\""+y1+"\" x2=\""+(x+420)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: red;\"/>" +
					"<line x1=\""+(x+420)+"\" y1=\""+((y1+y2)/2)+"\" x2=\""+(x+400)+"\" y2=\""+((y1+y2)/2)+"\" style=\"stroke-width=10; stroke: red; stroke-dasharray:2,2;\"/>" +
					"<text x=\"470\" y=\"15\" style=\"text-anchor: middle;\">"+format.format(value).toString()+"</text>";
		}else {
			data = "";
		}
		
		HTML scale = new HTML(getSVG(data, x, y1, y2));
		
		return scale;
	}
	
	public int getLinePos(float q25, float median, float q75, float max, float value){
		if(value<q25){
			return (int) ((value/q25)*100);
		}else if(value<median){
			return (int) ((value/median)*100)+100;
		}else if(value<q75){
			return (int) ((value/q75)*100)+200;
		}else if(value<max){
			return (int) ((value/max)*100)+300;
		}
		else return -3;
	}
	
	private String getRowCssClass(int row, boolean selected) {
		return row % 2 == 0 ? "even" + getRowCssSelected(selected) : "odd" + getRowCssSelected(selected);
	}
	
	private String getRowCssSelected(boolean selected) {
		return selected ? " selected" : "";
	}
	
	private String getSVG(String svg, int x, int y1, int y2){
		return "<svg width=\"520px\" height=\"120px\" viewBox=\"0 0 520 120\">" +
				"<!-- min line -->"+
				"<line x1=\""+(x)+"\" y1=\""+y1+"\" x2=\""+x+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- q25 line -->"+
				"<line x1=\""+(x+100)+"\" y1=\""+y1+"\" x2=\""+(x+100)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- median line -->"+
				"<line x1=\""+(x+200)+"\" y1=\""+y1+"\" x2=\""+(x+200)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- q75 line -->"+
				"<line x1=\""+(x+300)+"\" y1=\""+y1+"\" x2=\""+(x+300)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- maximum line -->"+
				"<line x1=\""+(x+400)+"\" y1=\""+y1+"\" x2=\""+(x+400)+"\" y2=\""+y2+"\" style=\"stroke-width=10; stroke: black;\"/>" +
				"<!-- vertical -->"+
				"<line x1=\""+x+"\" y1=\""+((y1+y2)/2)+"\" x2=\""+(x+400)+"\" y2=\""+((y1+y2)/2)+"\" style=\"stroke-width=10; stroke: black;\"/>"+
				svg+
		"</svg>";
	}
}
