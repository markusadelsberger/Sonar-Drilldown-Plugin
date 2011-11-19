package jku.se.drilldown.qm;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;

@NavigationSection(NavigationSection.RESOURCE)
public class QMDrilldownPage extends GwtPage {
	
	//public static final String GWT_ID = "jku.se.drilldown.qm.page.QMDrilldownPage";
	
	public String getGwtId() {
	    return "jku.se.drilldown.qm.page.QMDrilldownPage";
	}

	public String getTitle() {
	    return "QM Drilldown";
	}
	  
}
