package jku.se.drilldown.ui.client;

import java.util.HashMap;
import java.util.List;

import org.sonar.wsclient.services.Measure;

public class DrilldownModel {
	private HashMap<String, List> measureMap;
	private HashMap<String, String> activeMap;
	private HashMap<String, Integer> countMap;
	private Measure activeMeasure;
	
	public DrilldownModel(){
		measureMap=new HashMap<String, List>();
		activeMap=new HashMap<String, String>();
		countMap= new HashMap<String, Integer>();
		activeMeasure = null;
	}
	
	public void addList(String name, List measureList){
		measureMap.put(name, measureList);
	}
	
	public List getList(String name){
		return measureMap.get(name);
	}
	
	public void setActiveElement(String listName, String elementName){
		activeMap.put(listName, elementName);
	}
	
	public String getActiveElement(String listName){
		return activeMap.get(listName);
	}
	
	public void addCount(String listName, Integer count){
		countMap.put(listName, count);
	}
	
	public Integer getCount(String listName){
		return countMap.get(listName);
	}
	
	public void setActiveMeasure(Measure measure){
		activeMeasure=measure;
	}
	
	public Measure getActiveMeasure(){
		return activeMeasure;
	}
}