package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class BuyStockClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	
	public BuyStockClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final View v = (View)context.getUIControl();
		if(v != null)
			v.setEnabled(false);
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_SOURCE_VIEW, context.getViewModel());
		IAsyncCallback cb = new IAsyncCallback() {
			
			@Override
			public void success(Object result) {
				if(v != null)
					v.setEnabled(true);
			}
			
			@Override
			public void failed(Object cause) {
				if(v != null)
					v.setEnabled(true);
			}
		};
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

}
