package jku.se.drilldown;

import java.util.Arrays;
import java.util.List;

import jku.se.drilldown.client.BenchmarkViewer;

import org.sonar.api.SonarPlugin;

public final class BenchmarkPlugin extends SonarPlugin{

	@Override
	public List getExtensions() {
		return Arrays.asList(BenchmarkViewer.class);
	}
	
	
}