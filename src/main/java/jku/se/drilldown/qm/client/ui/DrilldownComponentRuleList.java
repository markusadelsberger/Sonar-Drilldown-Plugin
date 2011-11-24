package jku.se.drilldown.qm.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private PathComponent controller;

	public DrilldownComponentRuleList(PathComponent controller) {
		super();
		this.controller=controller;
		controller.setRuleList(this);
		Grid grid = new Grid(0, gridColumnCount());
		grid.setStyleName("spaced");
		setGrid(grid);
	}

	@Override
	public Widget createHeader() {
		return new Label("Rule Drilldown");
	}

	@Override
	public int gridColumnCount() {
		return 3;
	}
	
	
	@Override
	public void renderRow(Measure item, int row) {
		//renderIconCells(item, row);
		//renderNameCell( item, row, 2);
		//renderValueCell( item, row, 3);
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
		getGrid().getCellFormatter().setStyleName(row, 0, getRowCssClass(row, false));
	}

	private void renderNameCell(final Measure measure, int row, int column) {
		Anchor link = new Anchor(measure.getRuleName());

		link.setName(measure.getRuleKey());
		link.setTitle(measure.getRuleName());
		link.getElement().setPropertyObject("measure", measure);
		link.addClickHandler(this);

		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	private void renderValueCell(Measure measure, int row, int column) {
		getGrid().setHTML(row, column, String.valueOf(measure.getIntValue()));
		getGrid().getCellFormatter().setStyleName(row, column,getRowCssClass(row, false));
	}

	@Override
	public String getItemIdentifier(Measure item) {
		return item.getRuleKey();
	}

	public void addMeasures(List<Measure> measures){
		int row = getGrid().getRowCount();
		getGrid().resizeRows(row+measures.size());		
		
		Map<String, Integer> hashmap= new HashMap<String,Integer>();
		
		for (Measure measure : measures)
		{
			renderIconCells(measure, row);
			renderNameCell(measure, row, 1);
			renderValueCell(measure, row, 2);
			
			hashmap.put(getItemIdentifier(measure), new Integer(row));
			
			row++;
		}
		
		this.setHashmap(hashmap);
		
		if(containsSelectedItem())
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
			this.setSelectedItem(selectedMeasure);
			controller.onSelectedItemChanged("rule");
		} 	
	}
}