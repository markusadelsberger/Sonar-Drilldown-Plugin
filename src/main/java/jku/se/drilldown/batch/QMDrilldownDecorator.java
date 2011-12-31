package jku.se.drilldown.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;


public class QMDrilldownDecorator implements Decorator {

	private static Logger logger = LoggerFactory.getLogger("QMDrilldownSensorLogger"); 
	private static String logMarker = "QMDrilldownDecorator";
	
	public boolean shouldExecuteOnProject(Project project) {
		
		return BatchUtility.checkQMFileExist(project, logger, logMarker);
	}

	@SuppressWarnings("rawtypes")
	public void decorate( Resource resource, DecoratorContext context) {
    		
		if (Scopes.isHigherThan(resource, Scopes.FILE)) {
			Measure measure = new Measure(DrilldownMetrics.QMTREE_PROJECTKEY, context.getProject().getKey());
		    context.saveMeasure(measure);
		}
    }
}