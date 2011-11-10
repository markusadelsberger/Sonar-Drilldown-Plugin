package jku.se.drilldown.qm;

import java.util.List;

import jku.se.drilldown.qm.page.QMDrilldownPage;

import org.sonar.api.SonarPlugin;

import com.google.common.collect.Lists;

public class QMDrilldownPlugin extends SonarPlugin {
	
	
	public String toString() {
		return getKey();
	}

	public List getExtensions() {
		List extensions = Lists.newLinkedList();
		  
		extensions.add(QMDrilldownPage.class);

		return extensions;
	}
}