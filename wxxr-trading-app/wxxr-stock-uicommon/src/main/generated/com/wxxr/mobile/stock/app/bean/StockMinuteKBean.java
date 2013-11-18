/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;
import com.wxxr.mobile.core.bean.api.ListDecorator;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class StockMinuteKBean implements IBindableBean {
	
	private final PropertyChangeSupport emitter = new PropertyChangeSupport(this);
	private List<StockMinuteLineBean> list;
	private String date;
	private String close;

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
	 * @return the list
	 */
	public List<StockMinuteLineBean> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<StockMinuteLineBean> list) {
		List<StockMinuteLineBean> old = this.list;
		this.list = list;
		if(this.list != null){
            this.list = new ListDecorator<StockMinuteLineBean>("list", this.emitter,this.list);
        }
		this.emitter.firePropertyChange("list", old, this.list);
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		String old = this.date;
		this.date = date;
		this.emitter.firePropertyChange("date", old, this.date);
	}

	/**
	 * @return the close
	 */
	public String getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(String close) {
		String old = this.close;
		this.close = close;
		this.emitter.firePropertyChange("close", old, this.close);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "StockMinuteKBean ["+
                "list=" + this.list +
                " , date=" + this.date +
                " , close=" + this.close +
        "]";
    }	

}
