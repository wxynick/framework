/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;

/**
 * @author neillin
 *
 */
public class SimpleNavigationDescriptor implements INavigationDescriptor {
	
	private String result,message,toView,toPage;
	private Map<String, String> params;


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.INavigationDescriptor#getParameters()
	 */
	public Map<String, String> getParameters() {
		return this.params != null ? Collections.unmodifiableMap(this.params) : null;
	}


	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the toView
	 */
	public String getToView() {
		return toView;
	}


	/**
	 * @return the toPage
	 */
	public String getToPage() {
		return toPage;
	}


	/**
	 * @param result the result to set
	 */
	public SimpleNavigationDescriptor setResult(String result) {
		this.result = result;
		return this;
	}


	/**
	 * @param message the message to set
	 */
	public SimpleNavigationDescriptor setMessage(String message) {
		this.message = message;
		return this;
	}


	/**
	 * @param toView the toView to set
	 */
	public SimpleNavigationDescriptor setToView(String toView) {
		this.toView = toView;
		return this;
	}


	/**
	 * @param toPage the toPage to set
	 */
	public SimpleNavigationDescriptor setToPage(String toPage) {
		this.toPage = toPage;
		return this;
	}
	
	public SimpleNavigationDescriptor addParameter(String name, String value){
		if(this.params == null){
			this.params = new HashMap<String, String>();
		}
		this.params.put(name, value);
		return this;
	}
	
	public SimpleNavigationDescriptor removeParameter(String name){
		if(this.params != null){
			this.params.remove(name);
		}
		return this;
	}

}