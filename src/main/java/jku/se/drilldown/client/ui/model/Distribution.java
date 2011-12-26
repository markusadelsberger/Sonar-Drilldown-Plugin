package jku.se.drilldown.client.ui.model;

public class Distribution {
	private String tool;
	private String name;
	private float min;
	private float q25;
	private float median;
	private float q75;
	private float max;
	
	public Distribution(String tool, String name, float min, float q25, float median, float q75, float max){
		this.tool = tool;
		this.name=name;
		this.min=min;
		this.q25=q25;
		this.median=median;
		this.q75=q75;
		this.max=max;
	}
	

	public String getName(){
		return name;
	}
	
	public float getMin(){
		return min;
	}
	
	public float getQ25(){
		return q25;
	}
	
	public float getMedian(){
		return median;
	}
	
	public float getQ75(){
		return q75;
	}
	
	public float getMax(){
		return max;
	}


	public String getTool() {
		return tool;
	}
}
