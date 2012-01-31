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

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BenchmarkDrilldown extends DrilldownComponentList<List<Measure>>{

	private DrilldownModel drilldownModel;
	private DrilldownController drilldownController;
	private List<String> errors = null;


	public BenchmarkDrilldown(DrilldownController drilldownController){
		super(drilldownController);
		this.drilldownController=drilldownController;
		this.drilldownModel = drilldownController.getModel();
	}

	@Override
	public Widget createHeader() {
		return new Label("Benchmark Drilldown");
	}

	@Override
	public String getItemIdentifier(List<Measure> selectedItem) {
		return null;
	}

	@Override
	public int gridColumnCount() {
		return 4;
	}

	@Override
	public void doLoadData(){
		this.setGrid(new Grid(6, 4));
		getGrid().setStyleName("spaced");

		for(int i=0;i<=5;i++){
			addDrilldownAnchor("Q"+i, i);
			getGrid().getRowFormatter().setStyleName(i, getRowCssClass(i, false));
		}
		loadBenchmarkData();
	}

	@Override
	public void reload(ViewComponents viewComponent) {
			switch(viewComponent){
				case INITIALIZE:
					//if valid data is found, the count and graph is added to the BenchmarkDrilldown
					if(drilldownModel.getCount("benchmark_total")!=null && drilldownModel.getCount("benchmark_total")>0){
						for(int i = 0; i<=5; i++){
							addMeasures(i, drilldownModel.getCount("q"+i));
							addGraph("q"+i, i);
						}
						render(getGrid());
					}else{
						if(errors!=null){
							this.setGrid(new Grid(errors.size()+1, 1));
							getGrid().setText(0, 0, "Errors found in benchmarkData:");
							int i = 1;
							for(String s : errors){
								getGrid().setText(i, 0, s);
								i++;
							}
							render(getGrid());
						}else{
							render(new Label("No Benchmarkdata found."));
						}
					}
					break;	
				default: break;
			}
	}
	
	private void loadBenchmarkData(){
		ResourceQuery query = ResourceQuery.createForMetrics("jku.se.drilldown:sonar-drilldown-plugin", "benchmark", "ncloc");
		Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource result) {
				drilldownModel.setResource(result);
				Measure benchmark = result.getMeasure("benchmark");
				Measure loc = result.getMeasure("ncloc");
				if(benchmark != null && loc!=null){
					drilldownModel.setBenchmarkData(XMLExtractor.extract(benchmark.getData()));
					drilldownModel.addCount("loc", loc.getIntValue());
					combineData();
				}else{
					reload(ViewComponents.INITIALIZE);
				}
			}
		});	
	}

	private void combineData(){
		ResourceQuery query = ResourceQuery.createForResource(drilldownModel.getResource(), Metrics.VIOLATIONS).setDepth(0).setExcludeRules(false);
		Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource resource) {
				//the response from the query came back, the measures are saved in measureList
				List<Measure>measureList = resource.getMeasures();
				if(measureList!=null && measureList.size()>0){
					try{
						//Load the Rules
						BenchmarkData benchmarkData = drilldownModel.getBenchmarkData();
						Integer loc = drilldownModel.getCount("loc");
						if(benchmarkData!=null && loc!=null && benchmarkData.getErrors().size()==0){
							int linesOfCode = loc.intValue();
							List<Measure> q0 = new ArrayList<Measure>();
							List<Measure> q1 = new ArrayList<Measure>();
							List<Measure> q2 = new ArrayList<Measure>();
							List<Measure> q3 = new ArrayList<Measure>();
							List<Measure> q4 = new ArrayList<Measure>();
							List<Measure> q5 = new ArrayList<Measure>();
							List<Measure> completeList = new ArrayList<Measure>();

							for(Measure measure : measureList){
								String key = measure.getRuleKey();

								//The toolname and the rulename are saved in seperate Strings
								String tool=key.substring(0, key.indexOf(':'));
								String rule=key.substring(key.indexOf(':')+1, key.length());

								//All the benchmarkTools are looped to find the right one in the rule data
								for(BenchmarkTool benchmarkTool : benchmarkData.getTools()){
									if(benchmarkTool.getName().compareToIgnoreCase(tool)==0){
										//If so, all distributions are looped to find the correct rule
										for(Distribution distribution : benchmarkTool.getDistribution()){
											if(distribution.getName().compareToIgnoreCase(rule)==0){
												//The correct rule for a certain tool was found
												Integer measureIntValue = measure.getIntValue();
												completeList.add(measure);
												/*If a valid value is found, the quantil is calculated by 
												 * making it relative to the lines of code
												 */
												if(measureIntValue!=null){
													float measureValue = measureIntValue.floatValue()/(float)linesOfCode;
													if(measureValue<distribution.getMin()){
														q0.add(measure);
													} else if(measureValue<distribution.getQ25()){
														q1.add(measure);
													} else if(measureValue<distribution.getMedian()){
														q2.add(measure);
													} else if(measureValue<distribution.getQ75()){
														q3.add(measure);
													} else if(measureValue<distribution.getMax()){
														q4.add(measure);
													} else {
														q5.add(measure);
													}
												}
											}
										}					
									}
								}
							}
							//the count of the quantils is the number of violations, not the number of elements in the list
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
							drilldownModel.addList("completeList", completeList);
						}else{
							errors=benchmarkData.getErrors();
						}
					}catch(NullPointerException e){
						errors.add("NullPointerException whilst combining data, a Violation list could not be loaded; Error: "+e.getMessage());
					}catch(IndexOutOfBoundsException e){
						errors.add("IndexOutOfBoundsException whilst combining data, a rulename was incorrect; Error: "+e.getMessage());
					}
				}
				drilldownController.onSelectedItemChanged(ViewComponents.INITIALIZE);
			}
		});
	}

	private double getGraphWidth(String category){
		Integer totalCount = drilldownModel.getCount("benchmark_total");
		Integer severetyCount = drilldownModel.getCount(category);
		if(severetyCount!=null && totalCount!=null){
			return (severetyCount.doubleValue()/totalCount.doubleValue())*100;
		}else{
			return -1D;
		}
	}

	@Override
	public void renderRow(List<Measure> item, int row) {
		//not applicable for this class
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
		String benchmark = element.getInnerText();
		drilldownModel.setActiveElement("Benchmark", benchmark);
		drilldownModel.setActiveMeasure(null);
		drilldownModel.setActiveMeasures(drilldownModel.getList(benchmark));
		drilldownController.onSelectedItemChanged(ViewComponents.BENCHMARKDRILLDOWN);
	}

}
