//Abgeleitet von DrilldownComponent, stellt alle Listen dar
package jku.se.drilldown.ui;

import java.util.List;

import org.sonar.wsclient.services.Measure;


import com.google.gwt.user.client.ui.Grid;

public class DrilldownComponentList extends DrilldownComponent{
	private Grid grid;
	public DrilldownComponentList(List<Measure> measures){
		if(!measures.isEmpty()){
			grid = new Grid(measures.size(),3);
			int i = 0;
			for(Measure m : measures){
				grid.setText(i, 0, "[]");
				grid.setText(i, 1, "<a href=\""+m.getRuleName()+"\">"+m.getRuleName()+"</a>");
				grid.setText(i, 2, String.valueOf(m.getValue()));
				i++;
			}
		}
	}
}