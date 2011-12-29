package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class LimitedTabPanel extends Composite implements SelectionHandler<Integer>{
	
	private int visibleTabs; 
	private List<Widget> widgetList;
	private List<String> tabTextList;
	private int startListIndex;
	
	private int selectedTabIndex;
	private TabPanel tabPanel; 
	
	private Label first = new Label("first");
	private Label last = new Label("last");
	
	public LimitedTabPanel (int visibleTabs){
		super();
		
		if(visibleTabs>1)
			this.visibleTabs=visibleTabs;
		else
			this.visibleTabs=1;
		
		widgetList = new ArrayList<Widget>();
		tabTextList = new ArrayList<String>();
		
		selectedTabIndex=0;
		startListIndex=0;
		
		tabPanel= new TabPanel();
		tabPanel.addSelectionHandler(this);
		
		initWidget(tabPanel);
	}
	
	public void add(Widget widget, String tabText){
		widgetList.add(widget);
		tabTextList.add(tabText);
		
		if(widgetList.size() > visibleTabs)
		{
			rebuildTabPanel(startListIndex, visibleTabs);
		}
		else
			tabPanel.add(widget, tabText);
		 
	}

	private void rebuildTabPanel(int startIndex, int count) {
		
		tabPanel.clear();
		
		//if(startIndex!=0)
			tabPanel.add(first, "("+startIndex+") <<");	
				
		for(int i =startIndex; i<startIndex+count; i++)
			tabPanel.add(widgetList.get(i), tabTextList.get(i));	
		
		//if((widgetList.size()-startIndex-count)!=0)
			tabPanel.add(last, ">> ("+(widgetList.size()-startIndex-count)+")");
	
	}

	public void onSelection(SelectionEvent<Integer> event) {
		
		if(tabPanel.getWidget(event.getSelectedItem())==first)
		{
			if(selectedTabIndex == 1 && startListIndex>0)
			{
				startListIndex--;
				rebuildTabPanel(startListIndex,visibleTabs);
			}
			else if(selectedTabIndex > 1)
				selectedTabIndex--;
	
		} 
		else if (tabPanel.getWidget(event.getSelectedItem())==last)
		{
			
			if(selectedTabIndex == visibleTabs && (widgetList.size()-startListIndex-visibleTabs)>0)
			{
				startListIndex++;
				rebuildTabPanel(startListIndex,visibleTabs);
			} 
			else if(selectedTabIndex < visibleTabs)
				selectedTabIndex++;
		}
		else
			selectedTabIndex = event.getSelectedItem();
		
		tabPanel.selectTab(selectedTabIndex);
		
	}
	
	public void selectTab(int index) {

		if(index>=0 && index<widgetList.size()){
		
			if(widgetList.size() > visibleTabs)
			{		
				selectedTabIndex= index+1;
			}
			else
				selectedTabIndex=index;
			
			tabPanel.selectTab(selectedTabIndex);
		}
	}
}
