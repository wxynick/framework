/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.ComponentValueChangedEvent;
import com.wxxr.mobile.core.ui.api.IUIComponent;


/**
 * @author neillin
 *
 */
public class ComponentValueChangedEventImpl implements ComponentValueChangedEvent {
	
	private static SimpleDateFormat fmt = new SimpleDateFormat("yy/MM/dd HH:mm:ss sss");


	private final IUIComponent source;
	private long timestamp = System.currentTimeMillis();
	private String date;
	private List<AttributeKey<?>> keys;
	
	public ComponentValueChangedEventImpl(IUIComponent src,AttributeKey<?>...keys){
		this.source = src;
		this.keys = Arrays.asList(keys);
		synchronized(fmt){
			this.date = fmt.format(new Date(this.timestamp));
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getSource()
	 */
	public Object getSource() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#needSyncProcessed()
	 */
	public boolean needSyncProcessed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getTimestamp()
	 */
	public Long getTimestamp() {
		return this.timestamp;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ValueChangedEvent#getComponent()
	 */
	public IUIComponent getComponent() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ValueChangedEvent#getChangedAttributes()
	 */
	public List<AttributeKey<?>> getChangedAttributes() {
		return this.keys;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentValueChangedEventImpl [source=" + source + ", timestamp="
				+ date + ", keys=" + keys + "]";
	}
	@Override
	public String getSourceName() {
		return this.source.getName();
	}

}
