package jku.se.drilldown.client.ui.view;

import java.util.List;

import jku.se.drilldown.client.ui.controller.DrilldownController;
import jku.se.drilldown.client.ui.model.DrilldownModel;
import jku.se.drilldown.client.ui.model.ViewComponents;

import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The PathComponent is the controller of different DrilldownComponents. 
 * If for example a selected item of a DrilldownComponentList changes this controller will be triggered 
 * and it refresh the other components. 
 * 
 * Additionally it displays selected items of components. 
 * 
 * @author Johannes
 *
 */
public class PathComponent extends DrilldownComponent implements ClickHandler{

	private Grid pathInformation;
	
	private DrilldownController drilldownController;
	private DrilldownModel drilldownModel;
	private String[] labels = {"Path: ","Any severty >> ","Any rule >> ", " ", " ","Any QM Node >> ", "Any Benchmark Quantil >> "};
	
	private List<ViewComponents> viewComponents;

	public PathComponent(DrilldownController drilldownController, List<ViewComponents> viewComponents){
		super(drilldownController);
		this.drilldownController = drilldownController;
		this.drilldownModel=drilldownController.getModel();
		pathInformation = new Grid(1,5);
		//pathInformation.setStyleName("spaced");
		
		this.viewComponents=viewComponents;
		
		initWidget(pathInformation);	
	}
	
	@Override
	public void onLoad() {
		loadData();
	}
	
	public void loadData()
	{
		pathInformation.setWidget(0, 0, new Label(labels[0]));
		
		reload(ViewComponents.INITIALIZE);
	}
	
	public void onClick(ClickEvent event) {			
		
		Element element = event.getRelativeElement();
		
		ViewComponents clearItem = (ViewComponents)element.getPropertyObject("clearItem");
		drilldownController.clearElement(clearItem);		
	}

	private void setElement(String label, int column, ViewComponents viewComponent){
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(new Label(label));
		
		if(viewComponent!=null){
			Anchor link = new Anchor("Clear");
			link.getElement().setPropertyObject("clearItem", viewComponent);
			link.addClickHandler(this);
			panel.add(link);
		}
		
    	pathInformation.setWidget(0, column, panel);
		pathInformation.getCellFormatter().setStyleName(0, column, "even");
	}
	
	@Override
	public void reload(ViewComponents viewComponent)
	{
		switch(viewComponent){
			case INITIALIZE:
			case QMTREE:
			case BENCHMARKDRILLDOWN:
			case SEVERETYDRILLDOWN:
			case RULEDRILLDOWN:
			case PACKAGELIST:
			case FILELIST:
			case MODULELIST:
		
			for(ViewComponents component: viewComponents)
			{
				int position = 0;
				int label=0;
				String pattern = null;
				
				switch(component)
				{
					case SEVERETYDRILLDOWN:
						position=1;
						label=1;
						String severety = drilldownModel.getActiveElement("Severety");
						
						if(severety!=null)
							pattern= severety;
					break;
					
					case RULEDRILLDOWN: 
						position=2;
						label=2;
						Measure activeMeasure = drilldownModel.getActiveMeasure();
					
						if(activeMeasure!=null){
							pattern=activeMeasure.getRuleName();
						}
					break;
					
					case MODULELIST: 
						position=3;
						label=3;
						Resource selectedModule = drilldownModel.getSelectedItem(ViewComponents.MODULELIST);
					
						if(selectedModule!=null) {
							pattern=selectedModule.getName();
						}
					break;
					
					case PACKAGELIST: 
						position=4;
						label=4;
						Resource selectedPackage = drilldownModel.getSelectedItem(ViewComponents.PACKAGELIST);
					
						if(selectedPackage!=null)
							pattern=selectedPackage.getName();
					break;
						
					case QMTREE: 
						position=1;
						label=5;
						
						String qmtreeNodeName = drilldownModel.getActiveElement("qmtreeNode");
						
						if(qmtreeNodeName!=null)
							pattern= qmtreeNodeName;
						
					break;
					
					case BENCHMARKDRILLDOWN:
						position=1;
						label=6;
						
						String benchmarkQuantil = drilldownModel.getActiveElement("benchmark");
						
						if(benchmarkQuantil!=null)
							pattern=benchmarkQuantil;
				}
				
				if(pattern!=null)
					setElement(pattern, position, component);
				else
					setElement(labels[label], position, null);
	
			}
		}
	}
}
