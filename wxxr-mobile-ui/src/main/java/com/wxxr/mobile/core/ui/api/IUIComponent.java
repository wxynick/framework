/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Set;

import com.wxxr.mobile.core.microkernel.api.IAdaptable;

/**
 * @author neillin
 *
 */
public interface IUIComponent extends IAdaptable,Cloneable {
	String getName();
	
	<T> T getAttribute(AttributeKey<T> key);
	boolean hasAttribute(AttributeKey<?> key);
	 Set<AttributeKey<?>> getAttributeKeys();
	 <T> IUIComponent setAttribute(AttributeKey<T> key, T val);
	 <T> T removeAttribute(AttributeKey<T> key);
	 
	IUIContainer<?> getParent();
	boolean isSubsidiaryOf(IUIComponent component);
		
	boolean isInitialized();
	
	void setValueChangedCallback(IBindingValueChangedCallback cb);
	
	void handleInputEvent(InputEvent event);
}
