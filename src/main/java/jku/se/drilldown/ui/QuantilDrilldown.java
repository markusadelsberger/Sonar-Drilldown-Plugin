package jku.se.drilldown.ui;

import java.util.LinkedList;
import java.util.List;

import jku.se.drilldown.ui.client.DrilldownComponent;

import org.sonar.gwt.Metrics;
import org.sonar.wsclient.gwt.AbstractCallback;
import org.sonar.wsclient.gwt.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.gwt.user.client.ui.Grid;

public class QuantilDrilldown extends DrilldownComponent {
	public QuantilDrilldown(Resource resource, String metrics, int depth, boolean excludeRules){
		/*this.resize(5, 4);
		this.setStyleName("spacedIcon");
		
		this.getRowFormatter().addStyleName(0, "even");
		this.setText(0, 0, "[]");
		this.setText(0, 1, "Blocker");
		this.setText(0, 2, "0");
		this.setText(0, 3, "|");

		this.getRowFormatter().addStyleName(1, "odd");
		this.setText(1, 0, "[]");
		this.setText(1, 1, "Critical");
		this.setText(1, 2, "173");
		this.setText(1, 3, "|");
		
		this.getRowFormatter().addStyleName(2, "even");
		this.setText(2, 0, "[]");
		this.setText(2, 1, "Major");
		this.setText(2, 2, "8.281");
		this.setText(2, 3, "||||||||||");
		
		this.getRowFormatter().addStyleName(3, "odd");
		this.setText(3, 0, "[]");
		this.setText(3, 1, "Minor");
		this.setText(3, 2, "2.344");
		this.setText(3, 3, "|||||||");
		
		this.getRowFormatter().addStyleName(4, "even");
		this.setText(4, 0, "[]");
		this.setText(4, 1, "Info");
		this.setText(4, 2, "145");
		this.setText(4, 3, "|");

		*/
	}
}
