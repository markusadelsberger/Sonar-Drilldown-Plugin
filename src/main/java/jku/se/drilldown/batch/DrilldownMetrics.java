package jku.se.drilldown.batch;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public final class DrilldownMetrics implements Metrics {

	public static final Metric QMTREE = new Metric.Builder("qmtree", "QMTree", Metric.ValueType.DATA)
		.setDescription("QMTree")
		.setDirection(Metric.DIRECTION_BETTER)
		.setQualitative(false)
		.setDomain(CoreMetrics.DOMAIN_GENERAL)
		.create();

  	public List<Metric> getMetrics() {
  		return Arrays.asList(QMTREE);
  	}
}
