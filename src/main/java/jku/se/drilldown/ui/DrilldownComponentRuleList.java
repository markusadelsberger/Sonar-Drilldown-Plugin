//Abgeleitet von DrilldownComponentList, erweitert sie um zus�tzliche Spalte f�r Regeln
package jku.se.drilldown.ui;

import java.util.List;

import org.sonar.wsclient.services.Measure;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;


public class DrilldownComponentRuleList extends DrilldownComponentList<Measure>{

	public DrilldownComponentRuleList(List<Measure> measureList, ClickHandler clickHandler) {
		super(measureList, clickHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Widget createHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int gridColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renderRow(Measure item, int row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getItemIdentifier(Measure item) {
		return item.getRuleKey();
	}

	
}