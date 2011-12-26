package jku.se.drilldown.client.ui.view;

import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.BenchmarkData;
import jku.se.drilldown.client.ui.model.BenchmarkTool;
import jku.se.drilldown.client.ui.model.Distribution;
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

	private BenchmarkData benchmarkData = new BenchmarkData();
	private DrilldownComponent benchmarkDrilldown = this;
	
	public BenchmarkDrilldown(DrilldownController drilldownController){
		super(drilldownController);
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
			//Daten sind vorhanden, laden ist irgendwie falsch
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
		if(getController()!=null){
			try{
				Resource resource = getController().getModel().getResource();
				ResourceQuery query = ResourceQuery.createForResource(resource, "benchmark");
				Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
					@Override
					protected void doOnResponse(Resource innerResource) {
						try{
							Measure measure = innerResource.getMeasure("benchmark");
							if(measure != null){
								benchmarkData = XMLExtractor.extract(benchmarkData, measure.getData());
							}
							benchmarkDrilldown.reload();
						}catch(Exception e){
							Window.alert("Inner try " + e.toString());
						}
						
						
					}
				});
			}catch(Exception e){
				Window.alert("Outer try "+e.getMessage());
			}
		}else{
			Window.alert("Controller not set");
		}
		
	}

}
