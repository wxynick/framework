/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class PullMessageBean implements IBindableBean {
	
	private final PropertyChangeSupport emitter = new PropertyChangeSupport(this);
	private String message;
	private Long id;
	private String phone;
	private String title;
	private String createDate;
	private String articleUrl;

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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		String old = this.message;
		this.message = message;
		this.emitter.firePropertyChange("message", old, this.message);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Long old = this.id;
		this.id = id;
		this.emitter.firePropertyChange("id", old, this.id);
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		String old = this.phone;
		this.phone = phone;
		this.emitter.firePropertyChange("phone", old, this.phone);
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
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		String old = this.createDate;
		this.createDate = createDate;
		this.emitter.firePropertyChange("createDate", old, this.createDate);
	}

	/**
	 * @return the articleUrl
	 */
	public String getArticleUrl() {
		return articleUrl;
	}

	/**
	 * @param articleUrl the articleUrl to set
	 */
	public void setArticleUrl(String articleUrl) {
		String old = this.articleUrl;
		this.articleUrl = articleUrl;
		this.emitter.firePropertyChange("articleUrl", old, this.articleUrl);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "PullMessageBean ["+
                "message=" + this.message +
                " , id=" + this.id +
                " , phone=" + this.phone +
                " , title=" + this.title +
                " , createDate=" + this.createDate +
                " , articleUrl=" + this.articleUrl +
        "]";
    }	

}
