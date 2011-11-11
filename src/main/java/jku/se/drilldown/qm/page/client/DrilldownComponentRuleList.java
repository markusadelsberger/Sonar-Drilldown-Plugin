//Abgeleitet von DrilldownComponentList, erweitert sie um zus�tzliche Spalte f�r Regeln
package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.wsclient.services.Resource;

import com.google.gwt.event.dom.client.ClickHandler;


public class DrilldownComponentRuleList extends DrilldownComponentList{

	public DrilldownComponentRuleList(List<Resource> resourceList, int key, ClickHandler clickHandler) {
		super(resourceList, 0, clickHandler);
		// TODO Auto-generated constructor stub
	}
	
}