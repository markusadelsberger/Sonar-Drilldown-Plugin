package jku.se.drilldown.batch;

import java.io.File;

import org.slf4j.Logger;
import org.sonar.api.resources.Project;

public class BatchUtility {

	public static boolean checkQMFileExist(Project project, Logger logger, String logMarker)
	{
		String baseDir = project.getFileSystem().getBasedir().toString();
		File file = new File(baseDir+"\\qualitymodel.xml");
		
		if(file.exists()){
			// logger.info(logMarker+" scan ..."); 
			return true;
		}
		else{
			logger.info(logMarker+": qualitymodel.xml not available"); 
			logger.warn(logMarker+" not executed");
			return false;
		}
		
	}
}
