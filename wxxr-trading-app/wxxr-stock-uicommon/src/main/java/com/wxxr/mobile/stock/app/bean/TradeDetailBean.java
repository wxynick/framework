/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class TradeDetailBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private String tradeCatagory;
	private String tradeDate;
	private float tradeAmount;

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



	/**
	 * @return the tradeCatagory
	 */
	public String getTradeCatagory() {
		return tradeCatagory;
	}

	/**
	 * @param tradeCatagory the tradeCatagory to set
	 */
	public void setTradeCatagory(String tradeCatagory) {
		String old = this.tradeCatagory;
		this.tradeCatagory = tradeCatagory;
		this.emitter.firePropertyChange("tradeCatagory", old, this.tradeCatagory);
	}

	/**
	 * @return the tradeDate
	 */
	public String getTradeDate() {
		return tradeDate;
	}

	/**
	 * @param tradeDate the tradeDate to set
	 */
	public void setTradeDate(String tradeDate) {
		String old = this.tradeDate;
		this.tradeDate = tradeDate;
		this.emitter.firePropertyChange("tradeDate", old, this.tradeDate);
	}

	/**
	 * @return the tradeAmount
	 */
	public float getTradeAmount() {
		return tradeAmount;
	}

	/**
	 * @param tradeAmount the tradeAmount to set
	 */
	public void setTradeAmount(float tradeAmount) {
		float old = this.tradeAmount;
		this.tradeAmount = tradeAmount;
		this.emitter.firePropertyChange("tradeAmount", old, this.tradeAmount);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "TradeDetailBean ["+
                "tradeCatagory=" + this.tradeCatagory +
                " , tradeDate=" + this.tradeDate +
                " , tradeAmount=" + this.tradeAmount +
        "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(tradeAmount);
		result = prime * result
				+ ((tradeCatagory == null) ? 0 : tradeCatagory.hashCode());
		result = prime * result
				+ ((tradeDate == null) ? 0 : tradeDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeDetailBean other = (TradeDetailBean) obj;
		if (Float.floatToIntBits(tradeAmount) != Float
				.floatToIntBits(other.tradeAmount))
			return false;
		if (tradeCatagory == null) {
			if (other.tradeCatagory != null)
				return false;
		} else if (!tradeCatagory.equals(other.tradeCatagory))
			return false;
		if (tradeDate == null) {
			if (other.tradeDate != null)
				return false;
		} else if (!tradeDate.equals(other.tradeDate))
			return false;
		return true;
	}	

}
