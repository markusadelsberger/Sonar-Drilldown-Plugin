package jku.se.drilldown.client.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * LimitedTabPanel extends the functionality of a normal TabPanel because it limits the number of displayed tab.
 * To iterate thru all tabs a next and previous tab is offered. 
 * 
 * @author Johannes
 *
 */
public class LimitedTabPanel extends Composite implements SelectionHandler<Integer>{
	
	private int visibleTabs; 
	private List<TabItem> tabItemList;
	private int startListIndex;
	
	private int selectedTabIndex;
	private TabPanel tabPanel; 
	
	private Label first = new Label("first");
	private Label last = new Label("last");
	
	public LimitedTabPanel (int visibleTabs, Label label){
		super();
	
		
		if(visibleTabs>1)
			this.visibleTabs=visibleTabs;
		else
			this.visibleTabs=1;
		
		tabItemList = new ArrayList<TabItem>();
		
		selectedTabIndex=0;
		startListIndex=0;
		
		tabPanel= new TabPanel();
		tabPanel.addSelectionHandler(this);
		
		initWidget(tabPanel);
	}
	
	/**
	 * Adds an element to the tab panel. 
	 * An tab element needs a widget, a text and a tooltip. 
	 * 
	 * @param widget Represents the content of the tab element.
	 * @param tabText Text which is displayed at the top of the tab panel. 
	 * @param toolTip Tooltip which is shown when mouse moves over the text. 
	 */
	public void add(Widget widget, String tabText, String toolTip){
		tabItemList.add(new TabItem(widget,tabText,toolTip));
		
		if(tabItemList.size() > visibleTabs)
		{
			rebuildTabPanel(startListIndex, visibleTabs);
		}
		else
			addTabToTabPanel(widget, tabText, toolTip);
	}

	private void addTabToTabPanel(Widget content, String tabText, String toolTip)
	{
		Label label = new Label(tabText);
		
		TooltipMouseHandler mouseHandler = new TooltipMouseHandler(toolTip,5000);
		
		label.addMouseOverHandler(mouseHandler);
		label.addMouseOutHandler(mouseHandler);

		tabPanel.add(content,label);
	}
	
	private void rebuildTabPanel(int startIndex, int count) {
		
		tabPanel.clear();
		
		//if(startIndex!=0)
			addTabToTabPanel(first, "("+startIndex+") <<","previous Quality Model");	
				
		for(int i =startIndex; i<startIndex+count; i++)
			addTabToTabPanel(tabItemList.get(i).getWidget(), tabItemList.get(i).getText(),tabItemList.get(i).getToolTip());	
		
		//if((widgetList.size()-startIndex-count)!=0)
			addTabToTabPanel(last, ">> ("+(tabItemList.size()-startIndex-count)+")","next Quality Model");
	
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
			
			if(selectedTabIndex == visibleTabs && (tabItemList.size()-startListIndex-visibleTabs)>0)
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

		if(index>=0 && index<tabItemList.size()){
		
			if(tabItemList.size() > visibleTabs)
			{		
				selectedTabIndex= index+1;
			}
			else
				selectedTabIndex=index;
			
			tabPanel.selectTab(selectedTabIndex);
		}
	}
	
	
	private class TabItem{
		private Widget widget;
		private String text;
		private String toolTip;
		
		public TabItem(Widget widget, String text, String toolTip){
			this.widget=widget;
			this.text=text;
			this.toolTip=toolTip;
		}
		
		public Widget getWidget() {
			return widget;
		}

		public String getText() {
			return text;
		}

		public String getToolTip() {
			return toolTip;
		}
	}
}
