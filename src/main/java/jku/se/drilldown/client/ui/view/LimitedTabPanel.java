package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class LimitedTabPanel extends Composite implements SelectionHandler<Integer>{
	
	private int visibleTabs; 
	private List<Widget> widgetList;
	private List<String> tabTextList;
	private int tabIndex;
	private int startListIndex;
	
	private TabPanel tabPanel; 
	
	private Label first = new Label("first");
	private Label last = new Label("last");
	
	private Label label;
	
	public LimitedTabPanel (int visibleTabs, Label label){
		super();
		
		this.label=label;
		this.visibleTabs=visibleTabs;
		
		widgetList = new ArrayList<Widget>();
		tabTextList = new ArrayList<String>();
		
		tabIndex=1;
		startListIndex=0;
		
		tabPanel= new TabPanel();
		tabPanel.add(first, "<<");
		tabPanel.add(last, ">>");

		tabPanel.addSelectionHandler(this);
		
		initWidget(tabPanel);
	}
	

	public void add(Widget widget, String tabText){
		widgetList.add(widget);
		tabTextList.add(tabText);
		
		
		if(widgetList.size() <= visibleTabs)
			rebuildTabPanel(startListIndex,widgetList.size());
		 
	}

	private void rebuildTabPanel(int startIndex, int count) {
		
		label.setText(startIndex+" "+count+" "+ widgetList.size());
		
		tabPanel.clear();
		
		//for(int i=0; i<tabPanel.getDeckPanel().getWidgetCount(); i++)
		//	tabPanel.remove(i);
				
		tabPanel.add(first, "<<");	
				
		for(int i =startIndex; i<startIndex+count; i++)
			tabPanel.add(widgetList.get(i), tabTextList.get(i));	
		
		tabPanel.add(last, ">>");
	
		
	}

	public void onSelection(SelectionEvent<Integer> event) {
		
		//first tab
		if(event.getSelectedItem()==0)
		{
			if(tabIndex == 1 && startListIndex>0)
			{
				startListIndex--;
				rebuildTabPanel(startListIndex,visibleTabs);
			}
			else if(tabIndex>1)
				tabIndex--;
	
		} 
		//last tab
		else if (event.getSelectedItem() == (visibleTabs +1))
		{
			
			if(tabIndex == visibleTabs && (widgetList.size()-(startListIndex+1))>visibleTabs)
			{
				startListIndex++;
				rebuildTabPanel(startListIndex,visibleTabs);
			} else if(tabIndex<(visibleTabs))
				tabIndex++;
		}
			
		tabPanel.selectTab(tabIndex);
		
	}


	public void selectTab(int i) {
		/*
		if(tabCount>0)
		{		
			tabIndex= i+1;
			tabPanel.selectTab(tabIndex);
		}
		*/
	}
}
