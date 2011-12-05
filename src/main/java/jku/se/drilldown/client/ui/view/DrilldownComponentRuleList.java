package jku.se.drilldown.client.ui.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.services.Measure;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class DrilldownComponentRuleList extends DrilldownComponentList<Measure> {

	private DrilldownController controller;
	private DrilldownModel drilldownModel;

	public DrilldownComponentRuleList(DrilldownController controller) {
		super();
		this.controller=controller;
		controller.setRuleList(this);
		Grid grid = new Grid(0, gridColumnCount());
		grid.setStyleName("spaced");
		setGrid(grid);
		drilldownModel=controller.getModel();
	}

	@Override
	public Widget createHeader() {
		return new Label("Rule Drilldown");
	}

	@Override
	public int gridColumnCount() {
		return 4;
	}

	@Override
	public void doLoadData()
	{

	}
	
	private String getIcon(String severity){
		if(severity.compareTo("BLOCKER")==0){
			return Icons.get().priorityBlocker().getHTML();
		}else if(severity.compareTo("CRITICAL")==0){
			return Icons.get().priorityCritical().getHTML();
		}else if(severity.compareTo("MAJOR")==0){
			return Icons.get().priorityMajor().getHTML();
		}else if(severity.compareTo("MINOR")==0){
			return Icons.get().priorityMinor().getHTML();
		}else if(severity.compareTo("INFO")==0){
			return Icons.get().priorityInfo().getHTML();
		}else{
			return null;
		}
	}
	
	private void renderIconCells(Measure measure, int row ) {
		getGrid().setWidget(row, 0, new HTML(getIcon(measure.getRuleSeverity())));
	}

	private void renderNameCell(final Measure measure, int row, int column) {
		Anchor link = new Anchor(measure.getRuleName());

		link.setName(measure.getRuleKey());
		link.setTitle(measure.getRuleName());
		link.getElement().setPropertyObject("measure", measure);
		link.addClickHandler(this);

		getGrid().setWidget(row, column, link);
	}

	private void renderValueCell(Measure measure, int row, int column) {
		getGrid().setHTML(row, column, String.valueOf(measure.getIntValue()));
	}

	@Override
	public void renderRow(Measure item, int row) {
		renderIconCells(item, row);
		renderNameCell( item, row, 1);
		renderValueCell( item, row, 2);
		renderBarCell(item, row, 3);
		getGrid().getRowFormatter().setStyleName(row, getRowCssClass(row, false));
	}

	private void renderBarCell(Measure item, int row, int column) {
		String severety = item.getRuleSeverity();
		double width = Math.round(getGraphWidth(severety, item));
		HTML bar = new HTML("<div class='barchart' style='width: 60px'><div style='width: "+String.valueOf(width)+"%;background-color:#777;'></div></div>");
		getGrid().setWidget(row, 3, bar);
	}
	
	private double getGraphWidth(String severety, Measure item){
		Integer severetyCount = drilldownModel.getCount(severety);
		Integer measureCount = item.getIntValue();
		if(severetyCount!=null && measureCount!=null){
			return (measureCount.doubleValue()/severetyCount.doubleValue())*100;
		}else{
			return -1D;
		}
	}
	
	
	@Override
	public String getItemIdentifier(Measure item) {
		return item.getRuleKey();
	}

	public Measure getSelectedItem(){
		return drilldownModel.getActiveMeasure();
	}
	
	public void addMeasures(List<Measure> measures){
		int row = getGrid().getRowCount();
		getGrid().resizeRows(row+measures.size());

		Map<String, Integer> hashmap= new HashMap<String,Integer>();

		for (Measure measure : measures)
		{
			renderRow(measure, row);
			hashmap.put(getItemIdentifier(measure), new Integer(row));
			row++;
		}

		this.setHashmap(hashmap);

		if(containsSelectedItem(drilldownModel.getActiveMeasure()))
			selectRow(hashmap.get(getItemIdentifier(getSelectedItem())));
	}
	
	protected void reloadBegin(){
		getGrid().resizeRows(0);
	}
	
	public void reloadFinished(){
		render(getGrid());
	}

	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		Measure selectedMeasure = (Measure)element.getPropertyObject("measure");
		if(selectedMeasure != null)
		{
			drilldownModel.setActiveMeasure(selectedMeasure);

			controller.onSelectedItemChanged(ViewComponents.RULEDRILLDOWN);
		} 
	}
	/**
	 * Reloads the rules from the model and rerenders the grid
	 */
	public void reload(){
		String activeElement = drilldownModel.getActiveElement("Severety");
		if(activeElement!=null){
			List<Measure> measureList = drilldownModel.getList(activeElement);
			reloadBegin();
			addMeasures(measureList);
			reloadFinished();
		}else{
			List<Measure> measureList = new LinkedList<Measure>();
			measureList.addAll(drilldownModel.getList("Blocker"));
			measureList.addAll(drilldownModel.getList("Critical"));
			measureList.addAll(drilldownModel.getList("Major"));
			measureList.addAll(drilldownModel.getList("Minor"));
			measureList.addAll(drilldownModel.getList("Info"));
			reloadBegin();
			addMeasures(measureList);
			reloadFinished();
		}
	}
	
}