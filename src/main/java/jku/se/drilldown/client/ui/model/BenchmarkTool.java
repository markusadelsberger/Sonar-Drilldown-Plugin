package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkTool {
	private String name="default name";
	private String version="default version";
	private List<Distribution> distribution;
	
	public BenchmarkTool(String name, String version){
		
		if(name!=null) { this.name=name; }
		if(version!=null) { this.version=version; }
		
		distribution=new ArrayList<Distribution>();
	}

	public String getName(){
		return name;
	}
	
	public String getVersion(){
		return version;
	}
	
	public List<Distribution> getDistribution() {
		return distribution;
	}

	public void setDistribution(List<Distribution> distribution) {
		this.distribution = distribution;
	}
}
