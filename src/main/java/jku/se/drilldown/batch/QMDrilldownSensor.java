package jku.se.drilldown.batch;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.spqr.base.core.resources.model.IQualityModel;
import org.spqr.base.core.resources.model.IQualityModelElement;
import org.spqr.base.core.resources.model.IQualityModelList;
import org.spqr.base.internal.core.files.SpqrQualityModelFiles;

public class QMDrilldownSensor implements Sensor {

	public static String fileName = "qualitymodel.xml";
	
	private static Logger logger = LoggerFactory.getLogger("QMDrilldownSensorLogger"); 
	private static String logMarker = "QMDrilldownSensor";
	
	/**
	 * Methods checks if the project has a qualitymodel.xml. 
	 * If the project does not have one the sensor will not be executed. 
	 */
	public boolean shouldExecuteOnProject(Project project) {
		
		return BatchUtility.checkFileExist(project, fileName, logger, logMarker);
	}

	public void analyse(Project project, SensorContext context) {
		
		//Calendar starttime = Calendar.getInstance();
 	
	    JSONArray jArray = new JSONArray();
		
		String baseDir = project.getFileSystem().getBasedir().toString();
	    File file = new File(baseDir+"\\qualitymodel.xml");
	    
	    try {
			
	    	IQualityModelList modelList = SpqrQualityModelFiles.loadQualityModel(file);
				
		  	for (IQualityModel model : modelList.getAllQualityModels() )
			{
				
				JSONObject node = new JSONObject();
				node.put("name", model.getBaseModelName());
				node.put("childs", iterateQMTreeRekursive(model));
				
				jArray.put(node);
			}
				    
	    } catch (Exception e) {
	    	 logger.info(logMarker+" sqpr.jar caused exception");
	    	 logger.error(logMarker+" Exception: "+e.getMessage());
	    }
	    
	    Measure measure = new Measure(DrilldownMetrics.QMTREE, jArray.toString());
	    context.saveMeasure(measure);
	    
	    //Calendar endtime = Calendar.getInstance();
	    	        
	    //logger.info(logMarker+" done: "+ (endtime.getTimeInMillis() - starttime.getTimeInMillis() )+" ms"); 

	}

	/**
	 * Method iterates thru the quality model tree. 
	 * Therefore a recursive algorithm is used. 
	 * 
	 * @param modelElement The current tree node
	 * @return A JSON array which includes the name of the current node and its child nodes. 
	 * @throws JSONException
	 */
	private JSONArray iterateQMTreeRekursive(IQualityModelElement treeNode) throws JSONException {
			
		JSONArray jArray = new JSONArray();

		for (IQualityModelElement child : treeNode.getChildren() ) {
			JSONObject node = new JSONObject();
			
			if(child.getChildren().length>0) {
				node.put("name", child.getFullName());
				node.put("childs", iterateQMTreeRekursive(child));
			}
			else {
				node.put("name", child.getName());
				node.put("childs", new JSONArray());
			}
			
			jArray.put(node);
		}

		return jArray;
	}
}
