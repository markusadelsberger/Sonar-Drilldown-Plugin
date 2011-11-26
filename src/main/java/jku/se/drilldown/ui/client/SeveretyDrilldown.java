package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.services.Measure;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class SeveretyDrilldown extends DrilldownComponentList<List<Measure>> {

	private ComponentController controller;
	private List<Measure> selectedItem;
	private String severety;
	private HashMap<String, List<Measure>> hashmap;

	public SeveretyDrilldown(ComponentController controller) {
		super();
		this.controller=controller;
		selectedItem=null;
		severety=null;
		hashmap = new HashMap<String, List<Measure>>();
	}
	
	@Override
	public Widget createHeader() {
		return new Label("Severety");
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
		setGrid(new Grid(5,gridColumnCount()));
		getGrid().setWidget(0, 0, new HTML(Icons.get().priorityBlocker().getHTML()));	
		getGrid().setWidget(1, 0, new HTML(Icons.get().priorityCritical().getHTML()));		
		getGrid().setWidget(2, 0, new HTML(Icons.get().priorityMajor().getHTML()));
		getGrid().setWidget(3, 0, new HTML(Icons.get().priorityMinor().getHTML()));
		getGrid().setWidget(4, 0, new HTML(Icons.get().priorityInfo().getHTML()));
		
		getGrid().getRowFormatter().setStyleName(0, getRowCssClass(0, false));
		getGrid().getRowFormatter().setStyleName(1, getRowCssClass(1, false));
		getGrid().getRowFormatter().setStyleName(2, getRowCssClass(2, false));
		getGrid().getRowFormatter().setStyleName(3, getRowCssClass(3, false));
		getGrid().getRowFormatter().setStyleName(4, getRowCssClass(4, false));
		
	}
	
	public void addMeasures(int row, int violations){
		getGrid().setText(row, 2, String.valueOf(violations));
	}
	
	public void reloadFinished(){
		render(getGrid());
	}

	@Override
	public String getItemIdentifier(List<Measure> item) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDrilldownAnchor(String name, int row, List<Measure> measures){
		Anchor a = new Anchor(name);
		a.getElement().setId(name);
		a.addClickHandler(this);
		getGrid().setWidget(row, 1, a);
		hashmap.put(name, measures);
	}
	
	@Override
	public List<Measure> getSelectedItem(){
		if(selectedItem!=null){
			return selectedItem;
		}else{
			Set<String> keyset = hashmap.keySet();
			List<Measure> measureList = new LinkedList<Measure>();
			for(String s : keyset){
				measureList.addAll(hashmap.get(s));
			}
			return measureList;
		}
		
	}
	
	public void setSelectedItem(String name){
		this.selectedItem=hashmap.get(name);
	}
	
	public String getSelectedSeverety(){
		return severety;
	}

	public void onClick(ClickEvent event) {
		Element element = event.getRelativeElement();
		severety = element.getInnerText();
		setSelectedItem(element.getId());
		controller.onSelectedItemChanged("severety");
	}

}