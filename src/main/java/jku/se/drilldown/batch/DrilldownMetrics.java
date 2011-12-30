package jku.se.drilldown.batch;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public final class DrilldownMetrics implements Metrics {

	public static final Metric BENCHMARK = new Metric.Builder("benchmark", "Benchmark", Metric.ValueType.DATA)
    .setDescription("Benchmark values")
    .setDirection(Metric.DIRECTION_WORST)
    .setQualitative(false)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();
	
  
  // getMetrics() method is defined in the Metrics interface and is used by
  // Sonar to retrieve the list of new metrics
  public List<Metric> getMetrics() {
    return Arrays.asList(BENCHMARK);
  }
}
