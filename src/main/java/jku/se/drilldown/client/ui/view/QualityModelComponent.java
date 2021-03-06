package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.QualityModelTreeNode;
import jku.se.drilldown.client.ui.model.ViewComponents;

import org.sonar.gwt.Metrics;
import org.sonar.gwt.ui.Loading;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * 
 * @author Johannes
 */
public class QualityModelComponent extends DrilldownComponent implements SelectionHandler<TreeItem>, OpenHandler<TreeItem> {

	private Panel qmoverview;
	private Panel data;

	private TreeItem selectedItem;

	private DrilldownController controller;
	private DrilldownModel model;

	private Map<String,Measure> hashmap;

	public QualityModelComponent(DrilldownController controller)
	{
		super(controller);

		this.controller=controller;
		this.model = controller.getModel();

		qmoverview = new VerticalPanel();
		initWidget(qmoverview);

		doLoadData();
	}

	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}

	@Override
	public void onLoad() {
		qmoverview.add(createHeader());

		data = new ScrollPanel();
		data.setStyleName("scrollable");
		data.setWidth("320px");

		qmoverview.add(data);

		loadData();
	}

	@Override
	public void reload(ViewComponents viewComponent){

		if(selectedItem!=null && model.getActiveElement("qmtreeNode")==null)
		{
			deselectNode((Grid)selectedItem.getWidget());
			selectedItem = null;
		}
	}

	private Widget createHeader() {
		return new Label("Quality Models");
	}

	private void doLoadData() {
		//Abfrage für 2.13
		ResourceQuery violationsQuery = ResourceQuery.createForResource(model.getResource(), Metrics.BLOCKER_VIOLATIONS, Metrics.CRITICAL_VIOLATIONS, Metrics.MAJOR_VIOLATIONS, Metrics.MINOR_VIOLATIONS, Metrics.INFO_VIOLATIONS)
				.setDepth(0)
				.setExcludeRules(false);

		Sonar.getInstance().find(violationsQuery, new AbstractCallback<Resource>() {
			@Override
			protected void doOnResponse(Resource resource) {
				if(resource!=null && !resource.getMeasures().isEmpty()){
					processInitialResponse(resource);  
				}else{
					//if Sonar 2.11 or lower is used, a different request is needed
					ResourceQuery violationsQuery = ResourceQuery.createForResource(model.getResource(), Metrics.VIOLATIONS)
							.setDepth(0)
							.setExcludeRules(false);
					Sonar.getInstance().find(violationsQuery, new AbstractCallback<Resource>() {
						@Override
						protected void doOnResponse(Resource resource) {
							if(resource!=null && !resource.getMeasures().isEmpty()){
								processInitialResponse(resource);
								model.setOlderVersion(true);
							}
						}
					});
				}
			}			
		});// Sonar.getInstance().find	
	}

	private void processInitialResponse(Resource resource){
		final LimitedTabPanel tabPanel = new LimitedTabPanel(3);
		final SelectionHandler<TreeItem> selectionHandler = this;
		final OpenHandler<TreeItem> openHandler = this;

		hashmap= new HashMap<String,Measure>();

		for (Measure item : resource.getMeasures()){ 		  			
			String key = item.getRuleKey();

			if(key.indexOf(':')>0) {
				key = key.substring(key.indexOf(':')+1,key.length());
			}

			hashmap.put(key, item);
		}
		
		model.addList("completeList", new ArrayList<Measure>(hashmap.values()));
		controller.onSelectedItemChanged(ViewComponents.INITIALIZE);	

		ResourceQuery projectKeyQuery = ResourceQuery.createForMetrics(resource.getKey(), "projectkey");
		Sonar.getInstance().find(projectKeyQuery, new AbstractCallback<Resource>() {

			@Override
			protected void doOnResponse(Resource result) {
				if (result==null) {
					data.clear(); 
					data.add(new Label("For the project no quality model is available."));
				} 
				else 
				{    	 
					Measure measure = result.getMeasure("projectkey");

					ResourceQuery qmtreeQuery = ResourceQuery.createForMetrics(measure.getData(), "qmtree");

					Sonar.getInstance().find(qmtreeQuery, new QMTreeCallbackHandler(tabPanel, selectionHandler, openHandler));
				}
			}
		});// Sonar.getInstance().find	
	}

	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem item = event.getSelectedItem();

		if(selectedItem!=null) {
			deselectNode((Grid)selectedItem.getWidget());
		}

		selectedItem = item;
		selectNode((Grid)selectedItem.getWidget());

		QualityModelTreeNode modelNode = (QualityModelTreeNode) item.getUserObject();
		List<QualityModelTreeNode> leaves = modelNode.getLeaves();

		List<Measure> selectedMeasures = new ArrayList<Measure>();

		for(QualityModelTreeNode leaf : leaves){
			Measure violation = hashmap.get(leaf.getNodeName());

			if(violation != null) {
				selectedMeasures.add(violation);
			}
		}

		model.setActiveElement("qmtreeNode", modelNode.getNodeName());
		model.setActiveMeasures(selectedMeasures);

		controller.onSelectedItemChanged(ViewComponents.QMTREE);

	}// onSelection


	public void onOpen(OpenEvent<TreeItem> event) {
		TreeItem item = event.getTarget();

		if(!((QualityModelTreeNode)item.getUserObject()).isCalculatedChildsValue())
		{
			for(int i=0; i<item.getChildCount(); i++)
			{
				TreeItem child = item.getChild(i);

				calculateViolations(child);
			}

			((QualityModelTreeNode)item.getUserObject()).setCalculatedChildsValue(true);	
		}

	}// onOpen

	private void renderTreeNode (TreeItem treeNode, String text, String value){
		Grid grid = new Grid(1, 2);
		//grid.setStyleName("spaced");

		Anchor link = new Anchor(text);			
		grid.setWidget(0, 0, link);
		grid.setWidget(0,1,new HTML("&nbsp "+value));

		treeNode.setWidget(grid);
	}

	private void selectNode(Grid grid){
		for(int i=0; i<grid.getCellCount(0); i++) {
			grid.getCellFormatter().setStyleName(0, i, "odd selected");
		}
	}

	private void deselectNode(Grid grid){
		for(int i=0; i<grid.getCellCount(0); i++) {
			grid.getCellFormatter().setStyleName(0, i, "odd");
		}
	}

	private void calculateViolations(TreeItem treeNode) {
		QualityModelTreeNode modelNode = (QualityModelTreeNode)treeNode.getUserObject();

		int violationCount = 0;
		for(QualityModelTreeNode leaf : modelNode.getLeaves())
		{
			Measure violation = hashmap.get(leaf.getNodeName());

			if(violation!= null) {
				violationCount+=violation.getIntValue();
			}
		}

		modelNode.setViolationCount(violationCount);

		renderTreeNode(treeNode, modelNode.getNodeName(),String.valueOf(violationCount));
	}// calculateViolations

	private class QMTreeCallbackHandler extends AbstractCallback<Resource>{

		private LimitedTabPanel tabPanel;
		private SelectionHandler<TreeItem> selectionHandler;
		private OpenHandler<TreeItem> openHandler;

		public QMTreeCallbackHandler(LimitedTabPanel tabPanel,
				SelectionHandler<TreeItem> selectionHandler,
				OpenHandler<TreeItem> openHandler) {

			this.tabPanel=tabPanel;
			this.selectionHandler=selectionHandler;
			this.openHandler=openHandler;		
		}

		@Override
		protected void doOnError(int errorCode, String errorMessage)
		{
			data.clear(); 
			data.add(new Label("For the project no quality model is available."));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void doOnResponse(Resource result) {

			if (result==null) {
				data.clear(); 
				data.add(new Label("For the project no quality model is available."));
			} 
			else 
			{    	 
				Measure measure = result.getMeasure("qmtree");

				JSONArray items = JSONParser.parse(measure.getData()).isArray(); 

				for(int i=0; i<items.size(); i++)
				{
					JSONObject jsonObj = items.get(i).isObject();
					QualityModelTreeNode modelNode = new QualityModelTreeNode(getNameFromJSONObject(jsonObj));

					JSONArray jsonChilds = jsonObj.get("childs").isArray();

					FlowPanel flowpanel = new FlowPanel();

					if(jsonChilds!=null)
					{
						for(int j=0; j<jsonChilds.size(); j++)
						{
							JSONObject jsonChild = jsonChilds.get(j).isObject();
							QualityModelTreeNode modelChild = new QualityModelTreeNode(getNameFromJSONObject(jsonChild));
							modelNode.addChild(modelChild);

							TreeItem treeNode = new TreeItem();
							renderTreeNode(treeNode,  getNameFromJSONObject(jsonChild),"");
							treeNode.setUserObject(modelChild);

							addNodesToTreeAndModel(jsonChild, modelChild, treeNode);

							calculateViolations(treeNode);

							Tree tree = new Tree();
							tree.addItem(treeNode);
							tree.addSelectionHandler(selectionHandler);
							tree.addOpenHandler(openHandler);

							flowpanel.add(tree);
						}
					}

					String qmName = getNameFromJSONObject(jsonObj);
					tabPanel.add(flowpanel, shortName(qmName),qmName);

				}// for

				//tabPanel.add(new Label("test1"), "001","Toolttip");
				//tabPanel.add(new Label("test2"), "002","Toolttip");
				//tabPanel.add(new Label("test3"), "003","Toolttip");



				data.clear(); 
				data.add(tabPanel);

				tabPanel.selectTab(0);

			}// if - else    
		}// doOnResponse

		private String getNameFromJSONObject(JSONObject jsonObj) {
			return jsonObj.get("name").isString().stringValue();
		}

		private String shortName(String stringValue) {

			return stringValue.substring(0, stringValue.indexOf(' '));
		}

		private void addNodesToTreeAndModel(JSONObject jsonObj,
				QualityModelTreeNode modelNode, TreeItem rootNode) {

			JSONArray childs = jsonObj.get("childs").isArray();

			if(childs!=null)
			{
				for(int j=0; j<childs.size(); j++)
				{
					JSONObject jsonChild = childs.get(j).isObject();
					QualityModelTreeNode modelChild = new QualityModelTreeNode(getNameFromJSONObject(jsonChild));
					modelNode.addChild(modelChild);

					if((jsonChild.get("childs")!=null) && (jsonChild.get("childs").isArray().size()!=0)) {
						TreeItem treeNode = new TreeItem();
						renderTreeNode(treeNode, getNameFromJSONObject(jsonChild),"");
						treeNode.setUserObject(modelChild);
						addNodesToTreeAndModel(jsonChild, modelChild, treeNode);

						rootNode.addItem(treeNode);
					}
				}
			}	
		}// addNodesToTreeAndModel
	}
}
