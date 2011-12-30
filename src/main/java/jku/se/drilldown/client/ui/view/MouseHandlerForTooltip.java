package jku.se.drilldown.client.ui.view;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class MouseHandlerForTooltip implements MouseOverHandler, MouseOutHandler {
	
	private static final String DEFAULT_TOOLTIP_STYLE = "tooltip";
	private static final int DEFAULT_OFFSET_X = 10;
	private static final int DEFAULT_OFFSET_Y = 25;
	
	public void onMouseOver(MouseOverEvent event) {
		
		if (tooltip != null) {
			tooltip.hide();
	    }
	    tooltip = new Tooltip( (Widget)event.getSource(), offsetX, offsetY, text, delay, styleName);
	    tooltip.show();
	}

	public void onMouseOut(MouseOutEvent event) {
		
		if (tooltip != null) {
			tooltip.hide();
	    }	
	}

	private Tooltip tooltip;
	private String text;
	private String styleName;
	private int delay;
	private int offsetX = DEFAULT_OFFSET_X;
	private int offsetY = DEFAULT_OFFSET_Y;

	public MouseHandlerForTooltip(String text, int delay) {
		this(text, delay, DEFAULT_TOOLTIP_STYLE );
	}

	public MouseHandlerForTooltip(String text, int delay, String styleName) {
		
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

	private static class Tooltip extends PopupPanel {
	
		private int delay;
	
	    public Tooltip(Widget sender, int offsetX, int offsetY, final String text, final int delay, final String styleName) {
	    	super(true);
	
	    	this.delay = delay;
	
	    	HTML contents = new HTML(text);
	    	add(contents);
	
	    	int left = sender.getAbsoluteLeft() + offsetX;
	    	int top = sender.getAbsoluteTop() + offsetY;
	
	    	setPopupPosition(left, top);
	    	setStyleName(styleName);
	    }
	
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
