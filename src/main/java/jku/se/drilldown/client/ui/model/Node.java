package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String nodeName;
	private int value;
	
	private List<Node> childs;
	
	private boolean calculatedChildsValue;
	
	
	public Node (String nodeName)
	{
		this.nodeName=nodeName;
		childs=new ArrayList<Node>();
		calculatedChildsValue=false;
	}
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<Node> getChilds() {
		return childs;
	}

	public void addChild(Node child) {
		this.childs.add(child);
	}
	
	public List<Node> getLeaves() {
		
		return getLeavesFromNode(this);
	
	}

	private List<Node> getLeavesFromNode(Node node ) {
		
		List<Node> leaves = new ArrayList<Node>();
		
		if(node.childs.size()==0){
			leaves.add(node);
		} else {
			for(Node child : node.childs){
				leaves.addAll(getLeavesFromNode(child));
			}
		}
		
		return leaves;
	}
	
	public boolean isCalculatedChildsValue() {
		return calculatedChildsValue;
	}

	public void setCalculatedChildsValue(boolean calculatedChildsValue) {
		this.calculatedChildsValue = calculatedChildsValue;
	}

}
