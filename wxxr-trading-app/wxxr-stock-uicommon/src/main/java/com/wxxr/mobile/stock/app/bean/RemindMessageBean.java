/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.Map;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class RemindMessageBean implements IBindableBean {
	
	private final PropertyChangeSupport emitter = new PropertyChangeSupport(this);
	private String content;
	private String id;
	private String title;
	private Map<String,String> attrs;
	private String acctId;
	private String type;
	private String createdDate;

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(propertyName, listener);
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		String old = this.content;
		this.content = content;
		this.emitter.firePropertyChange("content", old, this.content);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		String old = this.id;
		this.id = id;
		this.emitter.firePropertyChange("id", old, this.id);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		String old = this.title;
		this.title = title;
		this.emitter.firePropertyChange("title", old, this.title);
	}

	/**
	 * @return the attrs
	 */
	public Map<String,String> getAttrs() {
		return attrs;
	}

	/**
	 * @param attrs the attrs to set
	 */
	public void setAttrs(Map<String,String> attrs) {
		Map<String,String> old = this.attrs;
		this.attrs = attrs;
		this.emitter.firePropertyChange("attrs", old, this.attrs);
	}

	/**
	 * @return the acctId
	 */
	public String getAcctId() {
		return acctId;
	}

	/**
	 * @param acctId the acctId to set
	 */
	public void setAcctId(String acctId) {
		String old = this.acctId;
		this.acctId = acctId;
		this.emitter.firePropertyChange("acctId", old, this.acctId);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		String old = this.type;
		this.type = type;
		this.emitter.firePropertyChange("type", old, this.type);
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		String old = this.createdDate;
		this.createdDate = createdDate;
		this.emitter.firePropertyChange("createdDate", old, this.createdDate);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "RemindMessageBean ["+
                "content=" + this.content +
                " , id=" + this.id +
                " , title=" + this.title +
                " , attrs=" + this.attrs +
                " , acctId=" + this.acctId +
                " , type=" + this.type +
                " , createdDate=" + this.createdDate +
        "]";
    }	

}
