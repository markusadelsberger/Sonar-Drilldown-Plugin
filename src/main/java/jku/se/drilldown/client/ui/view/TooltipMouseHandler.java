package jku.se.drilldown.client.ui.view;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A TooltipMouseHandler reacts on mouse events and shows a tool tip 
 * when the mouse is over a widget. When the mouse drops out of the widget the tooltip is hidden.
 * 
 * @author Johannes 
 */
public class TooltipMouseHandler implements MouseOverHandler, MouseOutHandler {
	
	private static final String DEFAULT_TOOLTIP_STYLE = "tooltip";
	private static final int DEFAULT_OFFSET_X = 10;
	private static final int DEFAULT_OFFSET_Y = 25;

	private Tooltip tooltip;
	private String text;
	private String styleName;
	private int delay;
	private int offsetX = DEFAULT_OFFSET_X;
	private int offsetY = DEFAULT_OFFSET_Y;

	public TooltipMouseHandler(String text, int delay) {
		this(text, delay, DEFAULT_TOOLTIP_STYLE );
	}

	public TooltipMouseHandler(String text, int delay, String styleName) {
		this.text = text;
		this.delay = delay;
		this.styleName = styleName;
	}

	public String getStyleName() {
	    return styleName;
	}

	public void setStyleName(String styleName) {
	    this.styleName = styleName;
	}

	public int getOffsetX() {
	    return offsetX;
	}

	public void setOffsetX(int offsetX) {
	    this.offsetX = offsetX;
	}

	public int getOffsetY() {
	    return offsetY;
	}

	public void setOffsetY(int offsetY) {
	    this.offsetY = offsetY;
	}

	/**
	 * Method is triggered when mouse is over a widget and then it shows a tooltip.
	 */
	public void onMouseOver(MouseOverEvent event) {
		
		if (tooltip != null) {
			tooltip.hide();
	    }
		
	    tooltip = new Tooltip( (Widget)event.getSource(), offsetX, offsetY, text, delay, styleName);
	    tooltip.show();
	}

	/**
	 * Method is triggered when mouse drops out of the widget and then it hides the tooltip.
	 */
	public void onMouseOut(MouseOutEvent event) {
		
		if (tooltip != null) {
			tooltip.hide();
	    }	
	}
	
	/**
	 * Class is used to simulate a tooltip as a popup panel. 
	 * The tooltip displays information for a certain time. 
	 * 
	 * @author Johannes
	 * 
	 */
	private static class Tooltip extends PopupPanel {
	
		//defines the time the tooltip is visible
		private int delay;
	
	    public Tooltip(Widget sender, int offsetX, int offsetY, final String information, final int delay, final String styleName) {
	    	super(true);
	
	    	this.delay = delay;
	
	    	HTML contents = new HTML(information);
	    	add(contents);
	
	    	int left = sender.getAbsoluteLeft() + offsetX;
	    	int top = sender.getAbsoluteTop() + offsetY;
	
	    	setPopupPosition(left, top);
	    	setStyleName(styleName);
	    }
	
	    /**
	     * Shows tooltip for a certain time. 
	     * Time is defined by the delay. 
	     */
	    public void show() {
	    	super.show();
	    	
	    	Timer t = new Timer() {
	
		        public void run() {
		        	Tooltip.this.hide();
		        }
	
	    	};
	      
	    	t.schedule(delay);
	    }
	}
}
