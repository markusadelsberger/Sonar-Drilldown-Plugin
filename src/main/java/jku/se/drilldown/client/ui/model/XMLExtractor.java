package jku.se.drilldown.client.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XMLExtractor {
	public static BenchmarkData extract (String xml){
		BenchmarkData benchmarkData = new BenchmarkData();
		try{			
			//Parse the XML into a DOM
			Document benchmarkDom = XMLParser.parse(xml);
        
			//Fetch the metadata
			NodeList metaData = benchmarkDom.getElementsByTagName("benchmarkdistributions");
			if(metaData!=null && metaData.getLength()>0){
				Node dateNode = metaData.item(0);
				String date = ((Element)dateNode).getAttribute("date");
				benchmarkData.setDate(date);
			}
			
			//Fetch projects, put them into a list and save them in the benchmarkData
			NodeList projects = benchmarkDom.getElementsByTagName("project");
			
			if(projects!=null && projects.getLength()>0){
				List<String> projectList = new ArrayList<String>(projects.getLength());
				for(int i = 0; i < projects.getLength();i++){
					projectList.add(((Element)projects.item(i)).getAttribute("name"));
				}
				benchmarkData.setProjects(projectList);
			}
			
			//Fetch the tools that were used
			NodeList tools = benchmarkDom.getElementsByTagName("tool");
			
			if(tools!=null && tools.getLength()>0){
				List<BenchmarkTool> toolList = new ArrayList<BenchmarkTool>(tools.getLength());
				
				for(int i = 0; i < tools.getLength();i++){
					//Create the BenchmarkTool Object, the DistributionList is created after that
					String name = ((Element)tools.item(i)).getAttribute("name");
					String version = ((Element)tools.item(i)).getAttribute("version");
					BenchmarkTool tool = new BenchmarkTool(name, version);
					
					//Get all the children of the tool node -> all distributions, loop through them, get their data, and save it
					NodeList distribution = ((Element)tools.item(i)).getChildNodes();
					
					if(distribution!=null && distribution.getLength()>0){
						List<Distribution> distributionList = new ArrayList<Distribution>(distribution.getLength());
						
						for(int j = 0;j<distribution.getLength();j++) {
							try { 
								
								float min = Float.parseFloat(((Element)distribution.item(j)).getAttribute("min"));
								float q25 = Float.parseFloat(((Element)distribution.item(j)).getAttribute("q25"));
								float median = Float.parseFloat(((Element)distribution.item(j)).getAttribute("median"));
								float q75 = Float.parseFloat(((Element)distribution.item(j)).getAttribute("q75"));					
								float max = Float.parseFloat(((Element)distribution.item(j)).getAttribute("max"));
								String distributionName = ((Element)distribution.item(j)).getAttribute("name");
								
								Distribution distributionObj = new Distribution(name, distributionName, min, q25, median, q75, max);
								distributionList.add(distributionObj);
								
							} catch(NumberFormatException e) {
								//String was not a number
								benchmarkData.addError("NumberFormatException: "+e.getMessage()+"\n\ti was: "+String.valueOf(i)+"\n\tj was "+String.valueOf(j));
							} catch(NullPointerException e) {
								//String was null, meaning the attribute was not found
								benchmarkData.addError("NullPointerException: "+e.getMessage()+"\n\ti was: "+String.valueOf(i)+"\n\tj was "+String.valueOf(j));
							} catch(ClassCastException e) {
								//can not cast distribution.item to Element 
								benchmarkData.addError("ClassCastException: "+e.getMessage()+"\n\ti was: "+String.valueOf(i)+"\n\tj was "+String.valueOf(j));
							}
						}
						//the found distributions are added to the tool
						tool.setDistribution(distributionList);
						
					} else {
						benchmarkData.addError("No Distribution Found\n\ti was: "+String.valueOf(i));
					}
					//if the list was created, it is added to the tool, and the tool is added to the toollist
					toolList.add(tool);
				}
				//the toolList is added to the benchmarkData
				benchmarkData.addTools(toolList);
			} else {
				if (tools==null) {
					benchmarkData.addError("Tools was null");
				} else {
					benchmarkData.addError("Toolcount was "+String.valueOf(tools.getLength()));
				}				
			}
			
		} catch(NullPointerException e) {
			benchmarkData.addError("NullPointerException XML Extractor outside try: "+e.getMessage());
		} catch(ClassCastException e) {
			benchmarkData.addError("ClassCastException XML Extractor outside try: "+e.getMessage());
		}
		
		return benchmarkData;
	}
}
