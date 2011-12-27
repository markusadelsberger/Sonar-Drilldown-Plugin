package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;
import jku.se.drilldown.client.ui.model.XMLExtractor;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkDrilldown extends DrilldownComponentList<List<Measure>>{

	private DrilldownModel drilldownModel;
	private DrilldownController drilldownController;
	
	
	public BenchmarkDrilldown(DrilldownController drilldownController){
		super();
		this.drilldownController=drilldownController;
		this.drilldownModel = drilldownController.getModel();
	}

	@Override
	public Widget createHeader() {
		return new Label("Benchmark Drilldown");
	}


	@Override
	public String getItemIdentifier(List<Measure> selectedItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int gridColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	@Override
	public void doLoadData(){
		this.setGrid(new Grid(6, 4));
		for(int i=0;i<=5;i++){
			addDrilldownAnchor("Q"+i, i);
		}
		
		getGrid().getRowFormatter().setStyleName(0, getRowCssClass(0, false));
		getGrid().getRowFormatter().setStyleName(1, getRowCssClass(1, false));
		getGrid().getRowFormatter().setStyleName(2, getRowCssClass(2, false));
		getGrid().getRowFormatter().setStyleName(3, getRowCssClass(3, false));
		getGrid().getRowFormatter().setStyleName(4, getRowCssClass(4, false));
		getGrid().getRowFormatter().setStyleName(5, getRowCssClass(5, false));
		
		getGrid().getColumnFormatter().setWidth(4, "70px");
	}
	
	@Override
	public void reload() {
		try{
			for(int i = 0; i<=5; i++){
				addMeasures(i, drilldownModel.getCount("q"+i));
				addGraph("q"+i, i);
			}
			render(getGrid());
		}catch(Exception e){
			Window.alert("Reload, outer catch loop: "+e.getMessage());
		}
	}
	
	public void loadBenchmarkData(){
		//Load the Benchmarkdata
		if(drilldownController!=null){
			try{
				Resource resource = drilldownController.getModel().getResource();
				ResourceQuery query = ResourceQuery.createForResource(resource, "benchmark", "ncloc");
				Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
					@Override
					protected void doOnResponse(Resource innerResource) {
						Measure benchmark = innerResource.getMeasure("benchmark");
						if(benchmark != null){
							drilldownModel.setBenchmarkData(XMLExtractor.extract(benchmark.getData()));
						}
						Measure loc = innerResource.getMeasure("ncloc");
						if(loc != null){
							drilldownModel.addCount("loc", loc.getIntValue());
						}
						
						combineData();
					}
				});
			}catch(Exception e){
				Window.alert("Outer try "+e.getMessage());
			}
			
		}else{
			Window.alert("Controller not set");
		}
	}
	
	public void combineData(){
		try{
			//Load the Rules
			//Window.alert("CombineData");
			List<Measure> measureList = new ArrayList();
			try{
				measureList.addAll(drilldownModel.getList("Blocker"));
				measureList.addAll(drilldownModel.getList("Critical"));
				measureList.addAll(drilldownModel.getList("Major"));
				measureList.addAll(drilldownModel.getList("Minor"));
				measureList.addAll(drilldownModel.getList("Info"));
			}catch(Exception e){
				Window.alert("Listen waren null: "+e.toString());
			}
			
			//Window.alert("benchmarkData: "+benchmarkData.getTools().isEmpty());
			//Window.alert("ncloc: "+linesOfCode);
			
			long start = System.currentTimeMillis();
			
			BenchmarkData benchmarkData = drilldownModel.getBenchmarkData();
			int linesOfCode = drilldownModel.getCount("loc");
			List<Measure> q0 = new ArrayList<Measure>();
			List<Measure> q1 = new ArrayList<Measure>();
			List<Measure> q2 = new ArrayList<Measure>();
			List<Measure> q3 = new ArrayList<Measure>();
			List<Measure> q4 = new ArrayList<Measure>();
			List<Measure> q5 = new ArrayList<Measure>();
			
			for(Measure measure : measureList){
				String key = measure.getRuleKey();
				
				//The toolname and the rulename are saved in seperate Strings
				String tool=key.substring(0, key.indexOf(":"));
				String rule=key.substring(key.indexOf(":")+1, key.length());
				
				//All the benchmarkTools are looped to find the right one in the rule data
				try{
					for(BenchmarkTool benchmarkTool : benchmarkData.getTools()){
						if(benchmarkTool.getName().compareToIgnoreCase(tool)==0){
							//If so, all distributions are looped to find the correct rule
							for(Distribution distribution : benchmarkTool.getDistribution()){
								if(distribution.getName().compareToIgnoreCase(rule)==0){
									//The correct rule for a certain tool was found
									float measureValue = measure.getIntValue()/linesOfCode;
									if(measureValue<distribution.getMin()){
										q0.add(measure);
									}else if(measureValue<distribution.getQ25()){
										q1.add(measure);
									}else if(measureValue<distribution.getMedian()){
										q2.add(measure);
									}else if(measureValue<distribution.getQ75()){
										q3.add(measure);
									}else if(measureValue<distribution.getMax()){
										q4.add(measure);
									}else{
										q5.add(measure);
									} 
									//Window.alert("Tool "+benchmarkTool.getName()+" is "+tool);
									//Window.alert("Rule "+distribution.getName()+" is "+rule);
								}
							}					
						}
					}
				}catch(Exception e){
					Window.alert(e.toString());
				}
			}
			drilldownModel.addCount("q0", q0.size());
			drilldownModel.addCount("q1", q1.size());
			drilldownModel.addCount("q2", q2.size());
			drilldownModel.addCount("q3", q3.size());
			drilldownModel.addCount("q4", q4.size());
			drilldownModel.addCount("q5", q5.size());
			drilldownModel.addCount("benchmark_total", q0.size()+q1.size()+q2.size()+q3.size()+q4.size()+q5.size());
			
			drilldownModel.addList("q0", q0);
			drilldownModel.addList("q1", q1);
			drilldownModel.addList("q2", q2);
			drilldownModel.addList("q3", q3);
			drilldownModel.addList("q4", q4);
			drilldownModel.addList("q5", q5);
			long end = System.currentTimeMillis();
			Window.alert("Time: "+(end-start));
		}catch(Exception e){
			Window.alert("CombineData: "+e.toString());
		}
		reload();
	}
	
	private double getGraphWidth(String severety){
		Integer totalCount = drilldownModel.getCount("benchmark_total");
		Integer severetyCount = drilldownModel.getCount(severety);
		if(severetyCount!=null && totalCount!=null){
			return (severetyCount.doubleValue()/totalCount.doubleValue())*100;
		}else{
			return -1D;
		}
	}
	
	@Override
	public void renderRow(List<Measure> item, int row) {
		// TODO Auto-generated method stub
		
	}
	
	private void addDrilldownAnchor(String name, int row){
		Anchor a = new Anchor(name);
		a.getElement().setId(name);
		a.addClickHandler(this);
		getGrid().setWidget(row, 0, a);
	}
	
	public void addMeasures(int row, int violations){
		if(getGrid()!=null){
			getGrid().setText(row, 1, String.valueOf(violations));
		}
	}
	
	private void addGraph(String severety, int row){
		if(getGrid()!=null){
			double width = getGraphWidth(severety);
			HTML bar = new HTML("<div class='barchart' style='width: 60px'><div style='width: "+String.valueOf(width)+"%;background-color:#777;'></div></div>");
			getGrid().setWidget(row, 2, bar);
		}
	}
	
	
	
	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		String severety = element.getInnerText();
		drilldownModel.setActiveElement("Severety", severety);
		drilldownController.onSelectedItemChanged(ViewComponents.SEVERETYDRILLDOWN);
	}

}
