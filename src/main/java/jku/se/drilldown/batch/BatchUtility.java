package jku.se.drilldown.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
	
	public static String readFile(String fileName) throws IOException{
		File xmlFile = new File(fileName);
		StringBuilder contents = new StringBuilder();
		BufferedReader input =  new BufferedReader(new FileReader(xmlFile));
		try {
			String line = null;
			while (( line = input.readLine()) != null){
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		}
		finally {
			input.close();
		}
		return contents.toString();
	}
}
