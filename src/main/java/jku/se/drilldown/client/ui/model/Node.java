package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public Set<Node> getLeaves() {
		
		List<Node> leaves = getLeavesFromNode(this);
		
		Collections.sort(leaves, new Comparator<Node>() {
		    
			public int compare(Node o1, Node o2) {
		        return o1.getNodeName().compareToIgnoreCase(o2.getNodeName());
		    }
		
		});
		
		if(leaves.size()>0)
		{
			Node curr = leaves.get(0);
			for (int i =1; i<leaves.size(); i++)
			{
				Node next =leaves.get(i);
				
				if(curr.equals(next))
				{
					leaves.remove(i);
					i++;
				}
				else
					curr = next;
			}
				
		}
		
		return new HashSet<Node>(leaves);
	
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
	
	@Override
	public boolean equals(Object o){
		
		if ( !(o instanceof Node) ) return false;
		
		Node node = (Node)o;
		
		if(this.getNodeName().equalsIgnoreCase(node.getNodeName()))
			return true;
		else
			return false;

	}

}
