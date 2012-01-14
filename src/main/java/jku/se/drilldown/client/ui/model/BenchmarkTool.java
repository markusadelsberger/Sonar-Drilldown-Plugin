package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkTool {
	private String name="default name";
	private String version="default version";
	private List<Distribution> distribution;
	
	/**
	 * Constructs a benchmark tool, name and version are saved if they are given
	 * @param name The name of the tool, if null is given, "default name" is set as name
	 * @param version The version of the tool, if null is given, "default version" is set as name
	 */
	public BenchmarkTool(String name, String version){
		if(name!=null) { this.name=name; }
		if(version!=null) { this.version=version; }
		
		distribution=new ArrayList<Distribution>();
	}
	
	/**
	 * @return Name of the tool; if "default name" is returned, no name attribute was found whilst parsing
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return Version of the tool used; if "default version" is returned, no version attribute was found whilst parsing
	 */
	public String getVersion(){
		return version;
	}
	
	/**
	 * @return If no Distribution-List is set, an empty ArrayList is returned
	 */
	public List<Distribution> getDistribution() {
		return distribution;
	}

	public void setDistribution(List<Distribution> distribution) {
		this.distribution = distribution;
	}
}
