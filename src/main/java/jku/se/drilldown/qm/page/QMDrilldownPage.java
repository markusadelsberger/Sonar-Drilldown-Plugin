package jku.se.drilldown.qm.page;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;

@NavigationSection(NavigationSection.RESOURCE)
public class QMDrilldownPage extends GwtPage {
	
	public String getGwtId() {
	    return "jku.se.drilldown.qm.page.QMDrilldownPage";
	}

	public String getTitle() {
	    return "QM Drilldown";
	}
	  
}
