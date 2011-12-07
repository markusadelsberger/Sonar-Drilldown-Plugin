package jku.se.drilldown.client.ui;

import org.sonar.gwt.ui.Page;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.XMLParser;

public class XMLTestPanel extends Page{

	@Override
	protected Widget doOnResourceLoad(Resource resource) {
		VerticalPanel output = new VerticalPanel();
		try{
			String xml = 
				"<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"+
						"<benchmarkdistributions date=\"2008-10-07 11:04\" findingsmode=\"0\" namemode=\"original\">" +
						"<criterions projectversions=\"all\">" +
							"<projectlist>" +
								"<project name=\"Ant\"/>" +
								"<project name=\"ArgoUML\"/>" +
								"<project name=\"Azureus\"/>" +
								"<project name=\"FreeMind\"/>" +
							"</projectlist>" +
						"</criterions>" +
					"</benchmarkdistributions>";
        
			//Parse the XML into a DOM
			Document benchmarkDom = XMLParser.parse(xml);
        
			//Fetch the metadata
			Node metaData = benchmarkDom.getElementsByTagName("benchmarkdistributions").item(0);
			String date = ((Element)metaData).getAttribute("date");
			output.add(new Label(date));
			
			//Fetch projects
			NodeList projects = benchmarkDom.getElementsByTagName("project");
			
			output.add(new Label("Projects: "));
			for(int i = 0; i < projects.getLength();i++){
				output.add(new Label("Project #"+String.valueOf(i)+": "+((Element)projects.item(i)).getAttribute("name")));
			}
			
		}catch(Exception e){
			output.add(new Label(e.getMessage()));
		}
		return output;
		
	}
}
