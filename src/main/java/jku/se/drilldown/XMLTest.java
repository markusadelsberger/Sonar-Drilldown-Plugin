package jku.se.drilldown;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;

@NavigationSection(NavigationSection.RESOURCE)
public class XMLTest extends GwtPage{

	
	@Override
	public String getTitle() {
		return "XML Test";
	}

	@Override
	public String getGwtId() {
		return "jku.se.drilldown.XMLTest";
	}
}
