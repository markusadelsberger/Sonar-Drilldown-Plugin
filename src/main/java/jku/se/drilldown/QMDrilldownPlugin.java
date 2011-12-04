package jku.se.drilldown;

import java.util.List;

import org.sonar.api.SonarPlugin;
import org.sonar.wsclient.services.Plugin;

import com.google.common.collect.Lists;


public class QMDrilldownPlugin extends SonarPlugin {
	
	
	public String toString() {
		return getKey();
	}
	
	public List getExtensions() {
		List extensions = Lists.newLinkedList();
		  
		extensions.add(QMDrilldownPage.class);
	//	extensions.add(QMDrilldownConfig.class);

		return extensions;
	}
}