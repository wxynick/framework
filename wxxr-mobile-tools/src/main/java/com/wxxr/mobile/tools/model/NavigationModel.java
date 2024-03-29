/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neillin
 *
 */
public class NavigationModel {
	private String result, toPage, toView, message,toDialog;
	private boolean closeCurrentView,keepMenuOpen;
	private Map<String, Parameter> params;
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @return the toPage
	 */
	public String getToPage() {
		return toPage;
	}
	/**
	 * @return the toView
	 */
	public String getToView() {
		return toView;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the params
	 */
	public List<Parameter> getParams() {
		return params != null ? new ArrayList<Parameter>(params.values()) : null;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @param toPage the toPage to set
	 */
	public void setToPage(String toPage) {
		this.toPage = toPage;
	}
	/**
	 * @param toView the toView to set
	 */
	public void setToView(String toView) {
		this.toView = toView;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addParameter(Parameter p){
		if(this.params == null){
			this.params = new HashMap<String, Parameter>();
		}
		this.params.put(p.getName(), p);
	}
	/**
	 * @return the toDialog
	 */
	public String getToDialog() {
		return toDialog;
	}
	/**
	 * @param toDialog the toDialog to set
	 */
	public void setToDialog(String toDialog) {
		this.toDialog = toDialog;
	}
	/**
	 * @return the closeCurrentView
	 */
	public boolean getCloseCurrentView() {
		return closeCurrentView;
	}
	/**
	 * @param closeCurrentView the closeCurrentView to set
	 */
	public void setCloseCurrentView(boolean closeCurrentView) {
		this.closeCurrentView = closeCurrentView;
	}
	/**
	 * @return the keepMenuOpen
	 */
	public boolean isKeepMenuOpen() {
		return keepMenuOpen;
	}
	/**
	 * @param keepMenuOpen the keepMenuOpen to set
	 */
	public void setKeepMenuOpen(boolean keepMenuOpen) {
		this.keepMenuOpen = keepMenuOpen;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NavigationModel [result=" + result + ", toPage=" + toPage
				+ ", toView=" + toView + ", message=" + message + ", toDialog="
				+ toDialog + ", closeCurrentView=" + closeCurrentView
				+ ", keepMenuOpen=" + keepMenuOpen + ", params=" + params + "]";
	}
	
	
}
