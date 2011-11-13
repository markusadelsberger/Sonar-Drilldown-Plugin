package jku.se.drilldown.qm.page.client;

import java.util.List;

import org.sonar.wsclient.services.Measure;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MeasuresList extends DrilldownComponentList<Measure> {

	
	public MeasuresList(List<Measure> resourceList, ClickHandler clickHandler) {
		super(resourceList, clickHandler);
	}
	
	@Override
	public Widget createHeader() {
		return new Label("");
	}

	@Override
	public int gridColumnCount() {
		return 1;
	}

	@Override
	public void renderRow(Measure item, int row) {
		renderNameCell(item, row, 0);
	}

	private void renderNameCell(Measure measure, int row, int column) {
		Anchor link = new Anchor(measure.getRuleName());

		// add resource object to link element
	    link.getElement().setPropertyObject("measureObj", measure);
		
	    // register listener
	    if(getClickHandler() != null)
	    	link.addClickHandler(getClickHandler());

		getGrid().setWidget(row, column, link);
		getGrid().getCellFormatter().setStyleName(row, column, getRowCssClass(row, false));
	}

	@Override
	public String getItemIdentifier(Measure item) {
		
		return item.getRuleKey();
	}
}
