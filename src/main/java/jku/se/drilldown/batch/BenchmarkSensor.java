package jku.se.drilldown.batch;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;

import jku.se.drilldown.batch.DrilldownMetrics;

public class BenchmarkSensor implements Sensor{

	public boolean shouldExecuteOnProject(Project arg0) {
		return true;
	}
	
	public void analyse(Project project, SensorContext sensorContext) {
		try{
			String baseDir = project.getFileSystem().getBasedir().toString();
			String xml = BatchUtility.readFile(baseDir+"\\benchmarkdistributions.xml");
			Measure measure = new Measure(DrilldownMetrics.BENCHMARK,xml);
		    sensorContext.saveMeasure(measure);
		}catch(Exception e){
			//File was not found or 
		}
	}
}
