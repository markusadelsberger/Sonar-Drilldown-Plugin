package jku.se.drilldown.client.ui.view;


import org.sonar.gwt.ui.Loading;
/*
import org.spqr.base.core.resources.model.IQualityModel;
import org.spqr.base.internal.core.files.SpqrQualityModelFiles;
import org.spqr.base.internal.core.providers.SimpleDomainProvider;
import org.spqr.base.internal.core.providers.SimpleSupplementProvider;
import org.spqr.base.internal.core.resources.QualityModelElementList;
*/
   
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
 
/**
 * For use import sqpr.jar
 * 
 * Add following entry into pom.xml 
 * 
 * <dependency>
 *		<groupId>org.spqr.base</groupId>
 *		<artifactId>spqr</artifactId>
 *		<version>1.0</version>
 *		<scope>compile</scope>
 *	</dependency> 
 * 
 * Store file sqpr-1.0.jar under: /User/.m2/repository/org/spqr/base/sqpr/1.0/sqpr-1.0.jar
 * 
 * 
 * @author Johannes
 *
 */
public class QualityModelOverview extends DrilldownComponent {

	private Panel qmoverview;
	private Panel data;
	
	private Tree tree; 
	
	public QualityModelOverview()
	{
		qmoverview = new VerticalPanel();
        initWidget(qmoverview);
        doLoadData();
		
	}

	@Override
	public void onLoad() {
		qmoverview.add(createHeader());
		data = new VerticalPanel();
		qmoverview.add(data);
		loadData();
	}

	public void reload()
	{
		loadData();
	}
	
	protected void loadData() {
		data.clear();
		data.add(new Loading());
		doLoadData();
	}

	private Widget createHeader() {
		return null;
	}


	private void doLoadData() {
		tree = new Tree();

		loadQualitymodels();
		/*
		QualityModelElementList modelList = loadQualitymodels();
		
		for (IQualityModel qmmodel : modelList.getAllQualityModels() )
		{
			TreeItem firstLevel = new TreeItem(qmmodel.getBaseModelName());
			
			tree.addItem(firstLevel);
		}
*/	
        qmoverview.add(tree);
	}

	// private QualityModelElementList loadQualitymodels() {
	private void loadQualitymodels() {
		
		//final QualityModelElementList modelList = new QualityModelElementList(SimpleDomainProvider.getInstance(), SimpleSupplementProvider.getInstance());
		
		try {
			RequestBuilder sendRequest = new RequestBuilder(RequestBuilder.GET, "http://localhost:9000/static/qualitymodel/qualitymodel.xml");
			
			sendRequest.sendRequest("", new RequestCallback() {

				public void onResponseReceived(Request request, Response response) {
					String result=response.getText();
					qmoverview.add(new Label(result));
					try {
			//			SpqrQualityModelFiles.loadQualityModel(result.toString(), modelList);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				public void onError(Request request, Throwable exception) {
					
					
				}
				  
				});
			
			
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try
		{
			URL url = new URL("http://localhost:9000/static/qualitymodel/qualitymodel.xml");
	        URLConnection urlConnection = url.openConnection();
	        
	        DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
	       
			String line;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder buffer = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			
			reader.close();
			dis.close();
			
			SpqrQualityModelFiles.loadQualityModel(buffer.toString(), modelList);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// TODO Auto-generated catch block
		}
		
		
		return modelList;
*/
	}
}
