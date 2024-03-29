/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IView extends IUIContainer<IUIComponent>,IBindable<IView>{
	String getName();
		
	boolean isActive();
	
	void show();
	
	void hide();
	
	List<ValidationError> getErrors();
	
	ISelectionProvider getSelectionProvider();
	
	IView setProperty(String name, Object value);
	
	Object getProperty(String name);
	
	String[] getPropertyNames();
	
	boolean hasProperty(String name);
	
	IView clearProperties();
	
	void processStartupExceptions();

}
