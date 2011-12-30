package jku.se.drilldown.client.ui.model;

import java.util.LinkedList;
import java.util.List;

public class BenchmarkData {
	private String date;
	private List<String> projects;
	private List<BenchmarkTool> tools;
	private List<String> errors;
	public BenchmarkData(){
		projects=new LinkedList<String>();
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
