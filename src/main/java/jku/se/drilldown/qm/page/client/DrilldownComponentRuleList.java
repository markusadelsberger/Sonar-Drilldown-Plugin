//Abgeleitet von DrilldownComponentList, erweitert sie um zus�tzliche Spalte f�r Regeln
package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;


public class DrilldownComponentRuleList extends DrilldownComponentList{

	public DrilldownComponentRuleList(List<Resource> resourceList, ClickHandler clickHandler) {
		super(resourceList, clickHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Widget createHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doLoadData() {
		// TODO Auto-generated method stub
		
	}
	
}