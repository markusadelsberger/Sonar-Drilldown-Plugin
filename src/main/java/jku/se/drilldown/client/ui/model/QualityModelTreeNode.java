package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Model represents a node element in a tree. 
 * Therefore, it stores a list of further QualityModelTreeNodes.
 * 
 * @author Johannes
 */
public class QualityModelTreeNode {

	private String nodeName;
	private int violationCount;
	
	//list of further nodes, to create the tree structure
	private List<QualityModelTreeNode> children;
	
	private boolean calculatedChildsValue;
	
	public QualityModelTreeNode (String nodeName) {
		this.nodeName=nodeName;
		
		children=new ArrayList<QualityModelTreeNode>();
		calculatedChildsValue=false;
	}
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getViolationCount() {
		return violationCount;
	}

	public void setViolationCount(int violationCount) {
		this.violationCount = violationCount;
	}

	public List<QualityModelTreeNode> getChilds() {
		return children;
	}

	public boolean isCalculatedChildsValue() {
		return calculatedChildsValue;
	}

	public void setCalculatedChildsValue(boolean calculatedChildsValue) {
		this.calculatedChildsValue = calculatedChildsValue;
	}
	
	public void addChild(QualityModelTreeNode child) {
		this.children.add(child);
	}
	
	/**
	 * Method returns nodes at the lowest level based on the current object.  
	 * This means it provides all nodes which have no element in the children list. 
	 * 
	 * @return List of nodes, that has no further nodes. 
	 */
	public List<QualityModelTreeNode> getLeaves() {
		
		List<QualityModelTreeNode> leaves = getLeavesFromNodeRekursive(this);
		
		// sorts list by node name
		Collections.sort(leaves, new Comparator<QualityModelTreeNode>() {
		    
			public int compare(QualityModelTreeNode o1, QualityModelTreeNode o2) {
		        return o1.getNodeName().compareToIgnoreCase(o2.getNodeName());
		    }
		
		});
		
		// removes duplicate nodes
		if(leaves.size()>0) {
			
			Set<String> set = new HashSet<String>();
			List<QualityModelTreeNode> newList = new ArrayList<QualityModelTreeNode>();
			
			for (Iterator<QualityModelTreeNode> iter = leaves.iterator();    iter.hasNext(); ) {
				QualityModelTreeNode element = iter.next();
				
				if (set.add(element.getNodeName())) {
					newList.add(element);
				}
			}
			    
			leaves.clear();
			leaves.addAll(newList);
		}
		
		return leaves;
	}

	/**
	 * Method iterates thru the tree by creating sub trees. Therefore a recursive algorithm is used. 
	 * It provides all leaves based on a node element. 
	 * A leaf is a node without further nodes. 
	 *  
	 * @param node The root element of a tree or rather subtree. 
	 * @return List of leaves from the node. 
	 */
	private List<QualityModelTreeNode> getLeavesFromNodeRekursive(QualityModelTreeNode node ) {
		
		List<QualityModelTreeNode> leaves = new ArrayList<QualityModelTreeNode>();
		
		if(node.children.size()==0){
			leaves.add(node);
		} else {
			for(QualityModelTreeNode child : node.children){	
				leaves.addAll(getLeavesFromNodeRekursive(child));
			}
		}

		return leaves;
	}
}
