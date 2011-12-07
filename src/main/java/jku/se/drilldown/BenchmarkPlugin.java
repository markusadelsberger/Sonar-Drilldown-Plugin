package jku.se.drilldown;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

public final class BenchmarkPlugin extends SonarPlugin{

	@Override
	public List getExtensions() {
		return Arrays.asList(BenchmarkViewer.class, XMLTest.class);
	}
	
	
}