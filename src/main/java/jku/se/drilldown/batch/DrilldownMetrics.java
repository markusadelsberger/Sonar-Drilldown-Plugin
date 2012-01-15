package jku.se.drilldown.batch;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * DrilldownMetrics contains all metrics which are used, on the one hand, from sensors and decorators
 * and on the other hand, from the UI to get data. 
 * 
 * @author Johannes
 *
 */
public final class DrilldownMetrics implements Metrics {

	public static final Metric QMTREE = new Metric.Builder("qmtree", "QMTree", Metric.ValueType.DATA)
		.setDescription("QMTree is a metric which is used by the QMDrilldown Plugin.")
		.setDirection(Metric.DIRECTION_BETTER)
		.setQualitative(false)
		.setDomain(CoreMetrics.DOMAIN_GENERAL)
		.create();
	
	public static final Metric QMTREE_PROJECTKEY = new Metric.Builder("projectkey", "ProjectKey", Metric.ValueType.STRING)
		.setDescription("Key indicates the resource object that holds the QMTree.")
		.setDirection(Metric.DIRECTION_BETTER)
		.setQualitative(false)
		.setDomain(CoreMetrics.DOMAIN_GENERAL)
		.create();


	public static final Metric BENCHMARK = new Metric.Builder("benchmark", "Benchmark", Metric.ValueType.DATA)
	    .setDescription("Benchmark values")
	    .setDirection(Metric.DIRECTION_WORST)
	    .setQualitative(false)
	    .setDomain(CoreMetrics.DOMAIN_GENERAL)
	    .create();
	
	public static final Metric BENCHMARK_PROJECTKEY = new Metric.Builder("benchmark_projectkey", "ProjectKey", Metric.ValueType.STRING)
		.setDescription("Key indicates the resource object that holds the benchmark data.")
		.setDirection(Metric.DIRECTION_BETTER)
		.setQualitative(false)
		.setDomain(CoreMetrics.DOMAIN_GENERAL)
		.create();

	/**
	 * getMetrics() method is defined in the Metrics interface and is used by
 	 * Sonar to retrieve the list of new metrics
	 */
  	public List<Metric> getMetrics() {
  		return Arrays.asList(QMTREE, QMTREE_PROJECTKEY, BENCHMARK, BENCHMARK_PROJECTKEY);
  	}
}

