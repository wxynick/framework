/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;
import com.wxxr.mobile.core.ui.common.ListDecorator;

/**
 * Generated by Bindable Bean generator
 *
 */
public class LineListBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private List<StockLineBean> week_list;
	private List<StockLineBean> day_list;
	private List<StockLineBean> month_list;

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	@Override
	public boolean hasPropertyChangeListener(IPropertyChangeListener listener) {
		return this.emitter.hasPropertyChangeListener(listener);
	}	


	/**
	 * @return the week_list
	 */
	public List<StockLineBean> getWeek_list() {
		return week_list;
	}

	/**
	 * @param week_list the week_list to set
	 */
	public void setWeek_list(List<StockLineBean> week_list) {
		List<StockLineBean> old = this.week_list;
		this.week_list = week_list;
		if(this.week_list != null){
            this.week_list = new ListDecorator<StockLineBean>("week_list", this.emitter,this.week_list);
        }
		this.emitter.firePropertyChange("week_list", old, this.week_list);
	}

	/**
	 * @return the day_list
	 */
	public List<StockLineBean> getDay_list() {
		return day_list;
	}

	/**
	 * @param day_list the day_list to set
	 */
	public void setDay_list(List<StockLineBean> day_list) {
		List<StockLineBean> old = this.day_list;
		this.day_list = day_list;
		if(this.day_list != null){
            this.day_list = new ListDecorator<StockLineBean>("day_list", this.emitter,this.day_list);
        }
		this.emitter.firePropertyChange("day_list", old, this.day_list);
	}

	/**
	 * @return the month_list
	 */
	public List<StockLineBean> getMonth_list() {
		return month_list;
	}

	/**
	 * @param month_list the month_list to set
	 */
	public void setMonth_list(List<StockLineBean> month_list) {
		List<StockLineBean> old = this.month_list;
		this.month_list = month_list;
		if(this.month_list != null){
            this.month_list = new ListDecorator<StockLineBean>("month_list", this.emitter,this.month_list);
        }
		this.emitter.firePropertyChange("month_list", old, this.month_list);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "LineListBean ["+
                "week_list=" + this.week_list +
                " , day_list=" + this.day_list +
                " , month_list=" + this.month_list +
        "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((day_list == null) ? 0 : day_list.hashCode());
		result = prime * result
				+ ((month_list == null) ? 0 : month_list.hashCode());
		result = prime * result
				+ ((week_list == null) ? 0 : week_list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineListBean other = (LineListBean) obj;
		if (day_list == null) {
			if (other.day_list != null)
				return false;
		} else if (!day_list.equals(other.day_list))
			return false;
		if (month_list == null) {
			if (other.month_list != null)
				return false;
		} else if (!month_list.equals(other.month_list))
			return false;
		if (week_list == null) {
			if (other.week_list != null)
				return false;
		} else if (!week_list.equals(other.week_list))
			return false;
		return true;
	}	

}
