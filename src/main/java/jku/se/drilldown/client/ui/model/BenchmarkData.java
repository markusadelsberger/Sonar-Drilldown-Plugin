package jku.se.drilldown.client.ui.model;

import java.util.LinkedList;
import java.util.List;

public class BenchmarkData {
	//the date in the benchmarkfile
	private String date;
	
	//the projects that were used as baseline
	private List<String> projects;
	
	//the tools that were used to for checking
	private List<BenchmarkTool> tools;
	
	//any errors that occured whilst parsing the xml file
	private List<String> errors;
	
	public BenchmarkData(){
		projects = new LinkedList<String>();
		tools = new LinkedList<BenchmarkTool>();
		errors = new LinkedList<String>();
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<String> getProjects() {
		return projects;
	}
	public void setProjects(List<String> projects) {
		this.projects = projects;
	}
	public List<BenchmarkTool> getTools() {
		return tools;
	}
	public void addTools(List<BenchmarkTool> tools) {
		this.tools.addAll(tools);
	}
	
	public void addError(String error){
		errors.add(error);
	}
	
	public List<String> getErrors(){
		return errors;
	}
	
}
