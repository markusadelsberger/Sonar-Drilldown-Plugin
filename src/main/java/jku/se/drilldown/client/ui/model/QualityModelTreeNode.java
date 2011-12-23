package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;

public class QualityModelTreeNode {

	private String nodeName;
	private int value;
	
	private List<QualityModelTreeNode> childs;
	
	private boolean calculatedChildsValue;
	
	public QualityModelTreeNode (String nodeName)
	{
		this.nodeName=nodeName;
		childs=new ArrayList<QualityModelTreeNode>();
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

	public List<QualityModelTreeNode> getChilds() {
		return childs;
	}

	public void addChild(QualityModelTreeNode child) {
		this.childs.add(child);
	}
	
	public Set<QualityModelTreeNode> getLeaves(Label label) {
		
		String output="";
		
		List<QualityModelTreeNode> leaves = getLeavesFromNode(this);
		
		Collections.sort(leaves, new Comparator<QualityModelTreeNode>() {
		    
			public int compare(QualityModelTreeNode o1, QualityModelTreeNode o2) {
		        return o1.getNodeName().compareToIgnoreCase(o2.getNodeName());
		    }
		
		});
		

		if(leaves.size()>0)
		{
			Set<String> set = new HashSet<String>();
			List<QualityModelTreeNode> newList = new ArrayList<QualityModelTreeNode>();
			
			for (Iterator<QualityModelTreeNode> iter = leaves.iterator();    iter.hasNext(); ) {
				QualityModelTreeNode element = iter.next();
				if (set.add(element.getNodeName()))
					newList.add(element);
			}
			    
			leaves.clear();
			leaves.addAll(newList);
		}
		
		label.setText(output);
		
		return new HashSet<QualityModelTreeNode>(leaves);
	
	}

	private List<QualityModelTreeNode> getLeavesFromNode(QualityModelTreeNode node ) {
		
		List<QualityModelTreeNode> leaves = new ArrayList<QualityModelTreeNode>();
		
		if(node.childs.size()==0){
			leaves.add(node);
		} else {
			for(QualityModelTreeNode child : node.childs){	
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
		
		if ( !(o instanceof QualityModelTreeNode) ) return false;
		
		QualityModelTreeNode node = (QualityModelTreeNode)o;
		
		if(getNodeName().equalsIgnoreCase(node.getNodeName()))
			return true;
		else
			return false;

	}
}
