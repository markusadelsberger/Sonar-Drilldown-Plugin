package jku.se.drilldown;

import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;

@NavigationSection(NavigationSection.RESOURCE)
public class BenchmarkPage extends GwtPage{

	public String getTitle() {
		return "Benchmark";
	}

	public String getGwtId() {
		return "jku.se.drilldown.BenchmarkPage";
	}
}
