package jku.se.drilldown.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;

/**
 * Decorator works when qualitymodel.xml is available in a project directory.
 * Stores the root project key in a sonar metric. 
 * 
 * @author Johannes
 */
public class QMDrilldownDecorator implements Decorator {

	private static Logger logger = LoggerFactory.getLogger("QMDrilldownDecoratorLogger"); 
	private static String logMarker = "QMDrilldownDecorator";
	
	/**
	 * Methods checks if the project has a qualitymodel.xml file. 
	 * If the file is absent the sensor will not be executed. 
	 */
	public boolean shouldExecuteOnProject(Project project) {
		return BatchUtility.checkFileExist(project, QMDrilldownSensor.fileName, logger, logMarker);
	}

	@SuppressWarnings("rawtypes")
	public void decorate( Resource resource, DecoratorContext context) {
    	
		//only for the resource objects: PROJECT, MODULE and PACKAGE it is necessary to store the project key
		if (Scopes.isHigherThan(resource, Scopes.FILE)) {
			Measure measure = new Measure(DrilldownMetrics.QMTREE_PROJECTKEY, context.getProject().getKey());
		    context.saveMeasure(measure);
		}
    }
}