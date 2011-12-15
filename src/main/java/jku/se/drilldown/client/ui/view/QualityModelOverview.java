package jku.se.drilldown.client.ui.view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.Node;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

 
/**
 * For use import sqpr.jar
 * 
 * Add following entry into pom.xml 
 * 
 * <dependency>
 *		<groupId>org.spqr.base</groupId>
 *		<artifactId>spqr</artifactId>
 *		<version>1.0</version>
 *		<scope>compile</scope>
 *	</dependency> 
 * 
 * Store file sqpr-1.0.jar under: /User/.m2/repository/org/spqr/base/sqpr/1.0/sqpr-1.0.jar
 * 
 * 
 * @author Johannes
 *
 */
public class QualityModelOverview extends DrilldownComponent implements SelectionHandler<TreeItem>, OpenHandler<TreeItem> {

	
	private Panel qmoverview;
	private Panel data;
	
	private Resource resource;
	private TreeItem selectedItem;
	private Label label;
	
	private DrilldownController controller;
	private DrilldownModel model;
	
	private Map<String,Measure> hashmap;
	
	public QualityModelOverview(DrilldownController controller, Resource resource)
	{
		this.resource=resource;
		
		this.controller=controller;
		this.model = controller.getModel();
	
		qmoverview = new VerticalPanel();
        initWidget(qmoverview);
        
        doLoadData();
	}

	@Override
	public void onLoad() {
		qmoverview.add(createHeader());
		
		data = new ScrollPanel();
		data.setStyleName("scrollable");
		qmoverview.add(data);
		
    	label = new Label();
    	qmoverview.add(label);
		
		loadData();
	}

	public void reload(){
		;
	}
	
	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}

	private Widget createHeader() {
		return new Label("QM-Models");
	}

	private void doLoadData() {
      
	    final TabPanel tabPanel = new TabPanel();
	    
	    final SelectionHandler<TreeItem> selectionHandler = this;
	    final OpenHandler<TreeItem> openHandler = this;
	    
	    
	    ResourceQuery r_query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
	    	.setDepth(0)
	    	.setExcludeRules(false);

	    	Sonar.getInstance().find(r_query, new AbstractCallback<Resource>() {

	    		@Override
	    		protected void doOnResponse(Resource resource) {

	    			if (resource==null || resource.getMeasures().isEmpty()) {
	    				;
	    			} else {
	    				hashmap= new HashMap<String,Measure>();

	    		  		for (Measure item : resource.getMeasures())
	    		  		{
	    		  		 
	    		  			String key = item.getRuleKey();
	    		  			
	    		  			if(key.indexOf(":")>0)
	    		  				key = key.substring(key.indexOf(":")+1,key.length());
	    		  			    		  			
	    		  			hashmap.put(key, item);
	    		   		}
	    			}
	    			   			
	    			ResourceQuery query = ResourceQuery.createForResource(resource, "qmtree");
	    			Sonar.getInstance().find(query, new AbstractCallback<Resource>() {
	    	
	    				@Override
	    				protected void doOnResponse(Resource result) {
				    		Measure measure = result.getMeasure("qmtree");
				    		
				    		if (measure==null || measure.getData()==null) {
				    			;
				    		} 
				    		else 
				    		{
				          
				    			JSONArray items = JSONParser.parse(measure.getData()).isArray(); 
				          
				    			for(int i=0; i<items.size(); i++)
				    			{
				    				JSONObject jsonObj = items.get(i).isObject();
				    				Node modelNode = new Node(jsonObj.get("name").isString().stringValue());
				    					        	  
				    				JSONArray jsonChilds = jsonObj.get("childs").isArray();
				    			
				    				FlowPanel flowpanel = new FlowPanel();
				    				
				    				if(jsonChilds!=null)
				    					for(int j=0; j<jsonChilds.size(); j++)
				    					{
				    						JSONObject jsonChild = jsonChilds.get(j).isObject();
				    						Node modelChild = new Node(jsonChild.get("name").isString().stringValue());
				    						modelNode.addChild(modelChild);
				    						
				    						TreeItem treeNode = new TreeItem(jsonChild.get("name").isString().stringValue());
				    						treeNode.setUserObject(modelChild);
				  	        		  
				    						addNodesToTreeAndModel(jsonChild, modelChild, treeNode);
				  	        		  
				    						calculateViolations(treeNode);
				    						
				    						Tree tree = new Tree();
				    						tree.addItem(treeNode);
				    						tree.addSelectionHandler(selectionHandler);
				    						tree.addOpenHandler(openHandler);
				    						
				    						flowpanel.add(tree);
				    					}
				    				
				    				tabPanel.add(flowpanel, ""+jsonObj.get("name").isString().stringValue());
				    			}
				    			
				    		}// if - else
				    		
				    	    data.clear(); 
				    	    data.add(tabPanel);
			    	    
	    				}// doOnResponse

						private void addNodesToTreeAndModel(JSONObject jsonObj,
	    						Node modelNode, TreeItem rootNode) {
	    					
	    					JSONArray childs = jsonObj.get("childs").isArray();
	    		        	  
	    					if(childs!=null)
	    			        	for(int j=0; j<childs.size(); j++)
	    			        	{
	    			        		  JSONObject jsonChild = childs.get(j).isObject();
	    			        		  Node modelChild = new Node(jsonChild.get("name").isString().stringValue());
	    			        		  modelNode.addChild(modelChild);
	    			        		  
	    			        		  TreeItem treeNode = new TreeItem(jsonChild.get("name").isString().stringValue());
	    			        		  treeNode.setUserObject(modelChild);
	    			        		  
	    			        		  addNodesToTreeAndModel(jsonChild, modelChild, treeNode);
	    			        		  
	    			        		  rootNode.addItem(treeNode);
	    			        	}
	    					
	    				}
	    				
	    				
	    			}); // find 
	    		}// doOnResponse	
	    });// Sonar.getInstance().find
	
	}

	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem item = event.getSelectedItem();
		    
		if(selectedItem!=null)
			selectedItem.setStyleName("");

		selectedItem = item;
		selectedItem.setStyleName("even selected");
		
		Node modelNode = (Node) item.getUserObject();
		List<Node> leaves = modelNode.getLeaves();
		
		List<Measure> selectedMeasures = new ArrayList<Measure>();
		
		for(Node leaf : leaves){
			Measure violation = hashmap.get(leaf.getNodeName());
			
			if(violation != null)
				selectedMeasures.add(violation);
		}
		
		model.addList("qmtreeList", selectedMeasures);
		controller.onSelectedItemChanged(ViewComponents.QMTREE);
		
		/*
		ResourceQuery query = ResourceQuery.createForResource(resource, Metrics.VIOLATIONS)
				.setDepth(0)
				.setExcludeRules(false);
		
		String[] selectedRuleKeys = new String[selectedMeasures.size()];
		
		int i =0;
		for(Measure measure : selectedMeasures){
			selectedRuleKeys[i]=measure.getRuleKey();
			i++;
		}
				
		query.setRules(selectedRuleKeys);
		
		
		
		Sonar.getInstance().find(query, new AbstractCallback<Resource>() {

    		@Override
    		protected void doOnResponse(Resource resource) {

    			if (resource==null || resource.getMeasures().isEmpty()) {
    				;
    			} else {

    				String txt = "";
    				
    		  		for (Measure item : resource.getMeasures())
    		  		{
    		  			txt += item.getRuleName();
    		   		}
    		  		
    		  		label.setText(txt);
    			}
    		}
		});
		*/
		
	}// onSelection

	
	public void onOpen(OpenEvent<TreeItem> event) {
		TreeItem item = event.getTarget();

		if(!((Node)item.getUserObject()).isCalculatedChildsValue())
		{
			for(int i=0; i<item.getChildCount(); i++)
			{
				TreeItem child = item.getChild(i);
				
				calculateViolations(child);
			}
			
			((Node)item.getUserObject()).setCalculatedChildsValue(true);	
		}

	}// onOpen
	
	private void calculateViolations(TreeItem treeNode) {
		Node modelNode = (Node)treeNode.getUserObject();
		
		int sumOfViolations = 0;
		for(Node leaf : modelNode.getLeaves())
		{
			Measure violation = hashmap.get(leaf.getNodeName());
			
			if(violation!= null)
				sumOfViolations+=violation.getIntValue();
		}
		
		modelNode.setValue(sumOfViolations);
		
		treeNode.setText(treeNode.getText()+" "+sumOfViolations);
		
	}// calculateViolations

}
