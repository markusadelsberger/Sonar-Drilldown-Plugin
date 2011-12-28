package jku.se.drilldown;

import java.util.Arrays;
import java.util.List;

import jku.se.drilldown.batch.BenchmarkSensor;
import jku.se.drilldown.batch.DrilldownMetrics;

import org.sonar.api.SonarPlugin;

public final class BenchmarkPlugin extends SonarPlugin{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExtensions() {
		return Arrays.asList(
				//UI
				XMLTest.class, 
				//BenchmarkViewer.class,
				
				
				//Metric
				DrilldownMetrics.class,  
				
				//Batch
				BenchmarkSensor.class
				
			);
	}

}