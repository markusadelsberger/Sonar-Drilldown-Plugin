package jku.se.drilldown.qm.client.ui;

import org.sonar.gwt.ui.Loading;
import org.spqr.base.core.resources.model.IQualityModel;
import org.spqr.base.internal.core.files.SpqrQualityModelFiles;
import org.spqr.base.internal.core.providers.SimpleDomainProvider;
import org.spqr.base.internal.core.providers.SimpleSupplementProvider;
import org.spqr.base.internal.core.resources.QualityModelElementList;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


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

		QualityModelElementList modelList = loadQualitymodels();
		
		for (IQualityModel qmmodel : modelList.getAllQualityModels() )
		{
			TreeItem firstLevel = new TreeItem(qmmodel.getBaseModelName());
			
			tree.addItem(firstLevel);
		}
	
        qmoverview.add(tree);
	}

	private QualityModelElementList loadQualitymodels() {
		
		final QualityModelElementList modelList = new QualityModelElementList(SimpleDomainProvider.getInstance(), SimpleSupplementProvider.getInstance());
		
		try {
			RequestBuilder sendRequest = new RequestBuilder(RequestBuilder.GET, "http://localhost:9000/static/qualitymodel/qualitymodel.xml");
			
			sendRequest.sendRequest("", new RequestCallback() {

				public void onResponseReceived(Request request, Response response) {
					String result=response.getText();
					
					try {
						SpqrQualityModelFiles.loadQualityModel(result.toString(), modelList);
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
		*/

		return modelList;
	}
}
