package jku.se.drilldown.qm.config;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;
import org.sonar.api.web.UserRole;

@UserRole(UserRole.ADMIN)
public class QMDrilldownConfig extends GwtPage {

	public String getGwtId() {
		 return "jku.se.drilldown.qm.config.QMDrilldownConfig";
	}

	public String getTitle() {
		 return "QM Drilldown Configuration";
	}
}
