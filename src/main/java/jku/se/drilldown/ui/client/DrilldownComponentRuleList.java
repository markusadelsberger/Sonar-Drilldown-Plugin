//Abgeleitet von DrilldownComponentList, erweitert sie um zus�tzliche Spalte f�r Regeln
package jku.se.drilldown.ui.client;

import java.util.List;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;


public class DrilldownComponentRuleList extends DrilldownComponentList{

	public DrilldownComponentRuleList(Resource resource, String metrics, int depth, boolean excludeRules) {
		super(resource, metrics, depth, excludeRules);
	}
	
	
}