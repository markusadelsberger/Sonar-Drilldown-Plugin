package jku.se.drilldown.qm.client.ui;

import java.util.List;

import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class QuantilDrilldown extends DrilldownComponentList<List<Measure>> {

	private PathComponent controller;
	private List<Measure> selectedItem;

	public QuantilDrilldown(Resource resource, String scope, ClickHandler clickHandler, String pageID, PathComponent controller) {

		this.controller=controller;
		controller.setSeveretyDrilldownList(this);
	}
	
	@Override
	public Widget createHeader() {
		return new Label("");
	}


	@Override
	public int gridColumnCount() {
		return 4;
	}
	
	
	@Override
	public void renderRow(List<Measure> item, int row) {
		//renderIconCells(item, row);
		//renderNameCell( item, row, 2);
		//renderValueCell( item, row, 3);
	}

	@Override
	public void doLoadData()
	{
		setGrid(new Grid(5,3));
		getGrid().setWidget(0, 0, new HTML(Icons.get().priorityBlocker().getHTML()));			
		getGrid().setWidget(1, 0, new HTML(Icons.get().priorityCritical().getHTML()));		
		getGrid().setWidget(2, 0, new HTML(Icons.get().priorityMajor().getHTML()));
		getGrid().setWidget(3, 0, new HTML(Icons.get().priorityMinor().getHTML()));
		getGrid().setWidget(4, 0, new HTML(Icons.get().priorityInfo().getHTML()));
		
	}
	
	protected void addMeasures(int row, int violations){
		getGrid().setText(row, 2, String.valueOf(violations));
	}
	
	protected void reloadFinished(){
		render(getGrid());
	}

	@Override
	public String getItemIdentifier(List<Measure> item) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDrilldownAnchor(String name, int row, List<Measure> measures){
		Anchor a = new Anchor(name);
		a.getElement().setPropertyObject("assignedList", measures);
		a.addClickHandler(this);
		getGrid().setWidget(row, 1, a);
	}
	
	@Override
	public List<Measure> getSelectedItem(){
		return selectedItem;
	}
	public void setSelectedItem(List<Measure> item){
		this.selectedItem=item;
	}

	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		setSelectedItem((List<Measure>)element.getPropertyObject("assignedList"));
		controller.onSelectedItemChanged("severety");
	}
}