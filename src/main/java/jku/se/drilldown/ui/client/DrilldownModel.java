/**
 * @author markus
 * The model part of the MVC Principle of the Drilldown. Three kinds of Data can be saved, Measures, the Name of Active Elements
 * and the count of Measures. These are saved internally under a String.
 */
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
	
	/**
	 * Adds a list to the model
	 * @param name The name under which the List is saved; this has to be unique, if addList is called a second time with the same name the list is replaced
	 * @param measureList The list that should be saved
	 */
	public void addList(String name, List measureList){
		measureMap.put(name.toLowerCase(), measureList);
	}
	
	/**
	 * Fetches the list that was previously saved
	 * @param name The name the list was saved under; this value is case insensitive
	 * @return Returns the requested list or null if the list was not found
	 */
	public List getList(String name){
		return measureMap.get(name.toLowerCase());
	}
	
	/**
	 * Sets the active element of a list; is intended to be used with the same listname that was given in addList()
	 * @param listName Name of the list the element belongs to
	 * @param elementName Name of the element
	 */
	public void setActiveElement(String listName, String elementName){
		activeMap.put(listName.toLowerCase(), elementName);
	}
	
	/**
	 * Fetches the active Element that was previously saved
	 * @param listName The name of the list the active element is wanted for; this value is case insensitive
	 * @return The name of the element or null if no element was found
	 */
	public String getActiveElement(String listName){
		return activeMap.get(listName.toLowerCase());
	}
	
	/**
	 * Adds a count of elements to a given list
	 * @param listName Name of the list
	 * @param count Amount of elements to be saved
	 */
	public void addCount(String listName, Integer count){
		countMap.put(listName.toLowerCase(), count);
	}
	
	/**
	 * Fetches the previously saved count
	 * @param listName The list of which the amount of elements is needed
	 * @return The amount as Integer or null if the value was not found
	 */
	public Integer getCount(String listName){
		return countMap.get(listName.toLowerCase());
	}
	
	/**
	 * Sets the active measure
	 * @param measure The active measure
	 */
	public void setActiveMeasure(Measure measure){
		activeMeasure=measure;
	}
	
	/**
	 * Fetches the active measure
	 * @return The measure or null if it was not set or if it was reset
	 */
	public Measure getActiveMeasure(){
		return activeMeasure;
	}
}