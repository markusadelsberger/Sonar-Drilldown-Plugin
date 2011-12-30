package jku.se.drilldown;

import java.util.Arrays;
import java.util.List;

import jku.se.drilldown.batch.DrilldownMetrics;
import jku.se.drilldown.batch.QMDrilldownDecorator;
import jku.se.drilldown.batch.QMDrilldownSensor;

import org.sonar.api.SonarPlugin;

public class QMDrilldownPlugin extends SonarPlugin {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExtensions() {
		return Arrays.asList(	
			// Definitions
		    DrilldownMetrics.class,

		    // Batch
		    QMDrilldownSensor.class, QMDrilldownDecorator.class, 

		    // UI
		    QMDrilldownPage.class);
	}
}
