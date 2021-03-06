package jku.se.drilldown.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;


public class BenchmarkDecorator implements Decorator {

	private static Logger logger = LoggerFactory.getLogger("BenchmarkDecoratorLogger"); 
	private static String logMarker = "BenchmarkDecorator";

	public boolean shouldExecuteOnProject(Project project) {
		//If a benchmark file is available, the decorator should be executed
		return BatchUtility.checkFileExist(project, BenchmarkSensor.fileName, logger, logMarker);
	}

	@SuppressWarnings("rawtypes")
	public void decorate( Resource resource, DecoratorContext context) {
		//The projectkey is saved for each resource higher than files, so a drilldown for other resource objects is possible
		if (Scopes.isHigherThan(resource, Scopes.FILE)) {
			Measure measure = new Measure(DrilldownMetrics.BENCHMARK_PROJECTKEY, context.getProject().getKey());
			context.saveMeasure(measure);
		}
	}
}