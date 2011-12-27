package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.XMLExtractor;

import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkDrilldown extends DrilldownComponentList<List<Measure>>{

	private BenchmarkData benchmarkData;
	private DrilldownComponent benchmarkDrilldown = this;
	private DrilldownModel drilldownModel;
	private List q0;
	private List q1;
	private List q2;
	private List q3;
	private List q4;
	private List q5;
	private int linesOfCode = -1;
	
	
	public BenchmarkDrilldown(DrilldownController drilldownController){
		super(drilldownController);
		drilldownModel = getController().getModel();
	}
	
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
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
	public void renderRow(List<Measure> item, int row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		try{
			int i = 0;
			for(BenchmarkTool t : benchmarkData.getTools()){
				i++;
				i+=t.getDistribution().size();
			}
			this.setGrid(new Grid(i, 7));

			for(BenchmarkTool t : benchmarkData.getTools()){
				int j = 0;
				try{	
					for(Distribution d : t.getDistribution()){
						getGrid().setText(j,0,t.getName());
						getGrid().setText(j,1,d.getName());
						getGrid().setText(j,2,String.valueOf(d.getMin()));
						getGrid().setText(j,3,String.valueOf(d.getQ25()));
						getGrid().setText(j,4,String.valueOf(d.getMedian()));
						getGrid().setText(j,5,String.valueOf(d.getQ75()));
						getGrid().setText(j,6,String.valueOf(d.getMax()));
						j++;
					}
				}catch(Exception e){
					Window.alert("Reload, inner catch loop: "+e.getMessage());
				}
			}
		}catch(Exception e){
			Window.alert("Reload, outer catch loop: "+e.getMessage());
		}
		if(getGrid()!=null){
			render(getGrid());
		}else{
			Window.alert("Grid was null");
		}
		
	}
	
	@Override
	public void doLoadData(){
		
	}
	
	public void loadBenchmarkData(){
		//Load the Benchmarkdata
		if(getController()!=null){
			try{
				Resource resource = getController().getModel().getResource();
				ResourceQuery query = ResourceQuery.createForResource(resource, "benchmark", "ncloc");
				Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
					@Override
					protected void doOnResponse(Resource innerResource) {
						Measure benchmark = innerResource.getMeasure("benchmark");
						if(benchmark != null){
							benchmarkData = XMLExtractor.extract(benchmark.getData());
						}
						Measure loc = innerResource.getMeasure("ncloc");
						if(loc != null){
							linesOfCode=loc.getIntValue();
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
			
			
			q0 = new ArrayList();
			q1 = new ArrayList();
			q2 = new ArrayList();
			q3 = new ArrayList();
			q4 = new ArrayList();
			q5 = new ArrayList();
			
			
			for(Measure measure : measureList){
				String key = measure.getRuleKey();
				String tool="";
				String rule="";
				//The toolname and the rulename are saved in seperate Strings
				if(key==null){
					Window.alert("Key was null");
				}else{
					try{
						tool = key.substring(0, key.indexOf(":"));
						rule = key.substring(key.indexOf(":")+1, key.length());
						//Window.alert("Key: "+key+"\nTool: "+tool+"\nRule: "+rule);
					}catch(Exception e){
						Window.alert("Key: "+key+"\n"+e.toString());
					}
					
				}

				
				//All the benchmarkTools are looped to find the right one in the rule data
				try{
					//if(benchmarkData==null){
					//	Window.alert("BenchmarkData is null");
					//}
					for(BenchmarkTool benchmarkTool : benchmarkData.getTools()){
						if(benchmarkTool.getName().compareToIgnoreCase(tool)==0){
							//If so, all distributions are looped to find the correct rule
							try{
								for(Distribution distribution : benchmarkTool.getDistribution()){
									if(distribution.getName().compareToIgnoreCase(rule)==0){
										//The correct rule for a certain tool was found
										try{
											Window.alert("Tool "+benchmarkTool.getName()+" is "+tool);
											Window.alert("Rule "+distribution.getName()+" is "+rule);
										}catch(Exception e){
											Window.alert("3"+e.toString());
										}
									}
								}
							}catch(Exception e){
								Window.alert("2"+e.toString());
							}
							
						}
					}
				}catch(Exception e){
					Window.alert("1"+e.toString());
				}
				
			}
		}catch(Exception e){
			Window.alert("CombineData: "+e.toString());
		}
	}

}
