package jku.se.drilldown.qm.client;

import org.sonar.wsclient.services.Resource;

public class QualityModelContainer {

	private static QualityModelContainer instance = null;
	
	private QualityModelContainer() {}
	
	public static QualityModelContainer getInstance() {
		if (instance == null)
			instance = new QualityModelContainer();
	
		return instance;

	}
	
	public Object findQualityModel(Resource resource)
	{
		return null;
	}
}
