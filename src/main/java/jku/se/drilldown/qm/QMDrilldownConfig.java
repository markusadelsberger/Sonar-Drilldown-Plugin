package jku.se.drilldown.qm;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;
import org.sonar.api.web.UserRole;

@UserRole(UserRole.ADMIN)
@NavigationSection(NavigationSection.CONFIGURATION)
public class QMDrilldownConfig extends GwtPage {

	public String getGwtId() {
		 return "jku.se.drilldown.qm.QMDrilldownConfig";
	}

	public String getTitle() {
		 return "QM Drilldown Configuration";
	}
}
