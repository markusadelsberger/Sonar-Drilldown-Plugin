package jku.se.drilldown.batch;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;

import jku.se.drilldown.batch.DrilldownMetrics;

public class BenchmarkSensor implements Sensor{

	protected static String fileName = "benchmarkdistributions.xml";
	private static Logger logger = LoggerFactory.getLogger("BenchmarkSensorLogger"); 
	private static String logMarker = "BenchmarkSensor";
	
	public boolean shouldExecuteOnProject(Project project) {
		return BatchUtility.checkFileExist(project, fileName, logger, logMarker);
	}
	
	public void analyse(Project project, SensorContext sensorContext) {
		try{
			String baseDir = project.getFileSystem().getBasedir().toString();
			String xml = BatchUtility.readFile(baseDir+"\\"+fileName);
			Measure measure = new Measure(DrilldownMetrics.BENCHMARK,xml);
			sensorContext.saveMeasure(measure);
		} catch (IOException e) {
			logger.error(logMarker+" Error: "+e.getMessage());
		}
	}
}
