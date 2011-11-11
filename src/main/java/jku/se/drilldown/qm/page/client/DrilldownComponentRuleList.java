//Abgeleitet von DrilldownComponentList, erweitert sie um zus�tzliche Spalte f�r Regeln
package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.wsclient.services.Resource;


public class DrilldownComponentRuleList extends DrilldownComponentList{

	public DrilldownComponentRuleList(List<Resource> resourceList, int key, String requestQUery) {
		super(resourceList, 0, requestQUery);
		// TODO Auto-generated constructor stub
	}
	
}