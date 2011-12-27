package jku.se.drilldown;

import java.util.Arrays;
import java.util.List;

import jku.se.drilldown.sensor.BenchmarkSensor;
import jku.se.drilldown.sensor.DrilldownMetrics;

import org.sonar.api.SonarPlugin;

public final class BenchmarkPlugin extends SonarPlugin{

	@Override
	public List getExtensions() {
		return Arrays.asList(XMLTest.class, DrilldownMetrics.class); //BenchmarkViewer.class, , , BenchmarkSensor.class);
	}
	
	
}