package jku.se.drilldown.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.sonar.api.resources.Project;

/**
 * BatchUtility is implemented as utility class and provides methods to sensors and decorators. 
 * @author Johannes
 */
public final class BatchUtility {

	private BatchUtility () {
		; //this class is a utility class, instantiation do not make sense. 
	}
	
	/**
	 * Method verifies if a specific file in a project directory is available. 
	 *  
	 * @param project The project that contains the file. 
	 * @param fileName The name of the file. 
	 * @param logger To show an output message. 
	 * @param logMarker Identifies the log message.   
	 *  
	 * @return True, the file is in the project directory. False, file is absent. 
	 */
	public static boolean checkFileExist(Project project, String fileName, Logger logger, String logMarker) {
		String baseDir = project.getFileSystem().getBasedir().toString();
		File file = new File(baseDir+"\\"+fileName);
		
		if(file.exists()){
			return true;
		} else {
			logger.info(logMarker+": "+fileName+" not available"); 
			logger.warn(logMarker+" not executed");
			return false;
		}	
	}
	
	/**
	 * Reads the content of a file. 
	 * 
	 * @param fileName The name of the file. 
	 * @return The content of the file as string.
	 * @throws IOException
	 */
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
