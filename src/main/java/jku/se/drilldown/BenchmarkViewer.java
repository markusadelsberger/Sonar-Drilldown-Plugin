package jku.se.drilldown;


import org.sonar.api.web.GwtPage;
import org.sonar.api.web.NavigationSection;

@NavigationSection(NavigationSection.RESOURCE)
public class BenchmarkViewer extends GwtPage{

	@Override
	public String getTitle() {
		return "Benchmark Drilldown";
	}

	@Override
	public String getGwtId() {
		return "jku.se.drilldown.BenchmarkViewer";
	}

}