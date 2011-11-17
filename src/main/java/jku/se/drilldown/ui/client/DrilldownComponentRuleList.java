package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.List;

import org.sonar.gwt.Links;
import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Icons;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.AbstractListCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class DrilldownComponentRuleList extends DrilldownComponentList<Resource> {

	private String pageID;
	private Resource resource;
	private String scope;
	private Measure selectedMeasure;
	private StructureDrilldownList next;
	private StructureDrilldownList prev;

	public DrilldownComponentRuleList(Resource resource, String scope, ClickHandler clickHandler, String pageID) {
		super(clickHandler);

		this.resource=resource;
		this.pageID = pageID;
		this.scope=scope;

		this.selectedMeasure=null;
		this.next=null;
		this.prev=null;
		
		this.setStyleName("scrollable");
		setGrid(new Grid(0, gridColumnCount()));
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
	public void renderRow(Resource item, int row) {
		//renderIconCells(item, row);
		//renderNameCell( item, row, 2);
		//renderValueCell( item, row, 3);
	}

	@Override
	public void doLoadData()
	{

		/*Sonar.getInstance().find(getQuery(), new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource resource) {
				List<Measure>measureList = resource.getMeasures();
				setGrid(new Grid(measureList.size(), gridColumnCount()));

				HashMap<String,Integer> hashmap= new HashMap<String,Integer>();

				int i = 0;
				for (Measure measure : measureList)
				{
					renderIconCells(measure, i);
					renderNameCell(measure, i, 2);
					renderValueCell(measure, i, 3);
					i++;
				}

				setHashmap(hashmap);

				if(containsSelectedItem())
					selectRow(hashmap.get(getItemIdentifier(getSelectedItem())));

				render(getGrid());

				if(next!=null)
					next.loadData();
			}

		});*/
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
		getGrid().setWidget(row, 0, new HTML("<a href=\"" + Links.urlForResourcePage(resource.getKey(), pageID, "rule="+measure.getRuleKey())+"\">" + getIcon(measure.getRuleSeverity()) + "</a>"));
		getGrid().getCellFormatter().setStyleName(row, 0, getRowCssClass(row, false));
	}

	private void renderNameCell(final Measure measure, int row, int column) {
		Anchor link = new Anchor(measure.getRuleName());

		// add resource object to link element
		link.setName(measure.getRuleKey());
		link.setTitle(measure.getRuleName());

		// register listener
		if(getClickHandler() != null)
			link.addClickHandler(getClickHandler());

		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	private void renderValueCell(Measure measure, int row, int column) {
		getGrid().setHTML(row, column, String.valueOf(measure.getIntValue()));
		getGrid().getCellFormatter().setStyleName(row, column,getRowCssClass(row, false));
	}

	@Override
	public String getItemIdentifier(Resource item) {
		return item.getKey();
	}

	@Override
	public Resource getSelectedItem()
	{
		if(containsSelectedItem())
			return super.getSelectedItem();
		else
			return null;
	}

	public Resource getRootResource()
	{
		if(prev==null)
		{
			return resource;
		}
		else if (prev.getSelectedItem()!= null)
		{
			return prev.getSelectedItem();
		}
		else
		{
			return prev.getRootResource();
		}
	}

	protected void addMeasures(List<Measure> measures){
		int gridCount = getGrid().getRowCount();
		getGrid().resizeRows(gridCount+measures.size());
		for (Measure measure : measures)
		{
			renderIconCells(measure, gridCount);
			renderNameCell(measure, gridCount, 2);
			renderValueCell(measure, gridCount, 3);
			gridCount++;
		}
	}
	
	protected void reloadBegin(){
		getGrid().resizeRows(0);
	}
	protected void reloadFinished(){
		render(getGrid());
	}
	
}