package jku.se.drilldown.batch;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.spqr.base.core.resources.model.IQualityModel;
import org.spqr.base.core.resources.model.IQualityModelElement;
import org.spqr.base.core.resources.model.IQualityModelList;
import org.spqr.base.internal.core.files.SpqrQualityModelFiles;

public class QMDrilldownSensor implements Sensor {

	public boolean shouldExecuteOnProject(Project project) {
		// This sensor is executed on any type of projects
		return true;
	}

	public void analyse(Project project, SensorContext context) {
  
		JSONArray jArray = new JSONArray();

	    File file = new File("c:\\qualitymodel.xml");
	    
	    try {
			
	    	IQualityModelList modelList = SpqrQualityModelFiles.loadQualityModel(file);
				
		  	for (IQualityModel model : modelList.getAllQualityModels() )
			{
				
				JSONObject node = new JSONObject();
				node.put("name", model.getBaseModelName());
				node.put("childs", structureQMModel(model));
				
				jArray.put(node);
			}
				    
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
	    
	    Measure measure = new Measure(DrilldownMetrics.QMTREE, jArray.toString());
	    context.saveMeasure(measure);
	    
	    System.out.println("save qmtree for project: "+project.getName());

	}

	private JSONArray structureQMModel(IQualityModelElement modelElement) throws JSONException {
			
		JSONArray jArray = new JSONArray();

		for (IQualityModelElement child : modelElement.getChildren() )
		{
			JSONObject node = new JSONObject();
			
			if(child.getChildren().length>0)
			{
				node.put("name", child.getFullName());
				node.put("childs", structureQMModel(child));
			}
			else
			{
				node.put("name", child.getName());
				node.put("childs", new JSONArray());
			}
			
			jArray.put(node);
		}

		return jArray;
	}
}
