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
public class StockBasicMarketInfoBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private String currentTime;
	private float highestPrice;
	private String name;
	private float currentPrice;
	private float lastDayPrice;
	private String code;
	private float todayInitPrice;
	private float lowestPrice;
	private String pyCode;

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
	 * @return the currentTime
	 */
	public String getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(String currentTime) {
		String old = this.currentTime;
		this.currentTime = currentTime;
		this.emitter.firePropertyChange("currentTime", old, this.currentTime);
	}

	/**
	 * @return the highestPrice
	 */
	public float getHighestPrice() {
		return highestPrice;
	}

	/**
	 * @param highestPrice the highestPrice to set
	 */
	public void setHighestPrice(float highestPrice) {
		float old = this.highestPrice;
		this.highestPrice = highestPrice;
		this.emitter.firePropertyChange("highestPrice", old, this.highestPrice);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		this.emitter.firePropertyChange("name", old, this.name);
	}

	/**
	 * @return the currentPrice
	 */
	public float getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(float currentPrice) {
		float old = this.currentPrice;
		this.currentPrice = currentPrice;
		this.emitter.firePropertyChange("currentPrice", old, this.currentPrice);
	}

	/**
	 * @return the lastDayPrice
	 */
	public float getLastDayPrice() {
		return lastDayPrice;
	}

	/**
	 * @param lastDayPrice the lastDayPrice to set
	 */
	public void setLastDayPrice(float lastDayPrice) {
		float old = this.lastDayPrice;
		this.lastDayPrice = lastDayPrice;
		this.emitter.firePropertyChange("lastDayPrice", old, this.lastDayPrice);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		String old = this.code;
		this.code = code;
		this.emitter.firePropertyChange("code", old, this.code);
	}

	/**
	 * @return the todayInitPrice
	 */
	public float getTodayInitPrice() {
		return todayInitPrice;
	}

	/**
	 * @param todayInitPrice the todayInitPrice to set
	 */
	public void setTodayInitPrice(float todayInitPrice) {
		float old = this.todayInitPrice;
		this.todayInitPrice = todayInitPrice;
		this.emitter.firePropertyChange("todayInitPrice", old, this.todayInitPrice);
	}

	/**
	 * @return the lowestPrice
	 */
	public float getLowestPrice() {
		return lowestPrice;
	}

	/**
	 * @param lowestPrice the lowestPrice to set
	 */
	public void setLowestPrice(float lowestPrice) {
		float old = this.lowestPrice;
		this.lowestPrice = lowestPrice;
		this.emitter.firePropertyChange("lowestPrice", old, this.lowestPrice);
	}

	/**
	 * @return the pyCode
	 */
	public String getPyCode() {
		return pyCode;
	}

	/**
	 * @param pyCode the pyCode to set
	 */
	public void setPyCode(String pyCode) {
		String old = this.pyCode;
		this.pyCode = pyCode;
		this.emitter.firePropertyChange("pyCode", old, this.pyCode);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "StockBasicMarketInfoBean ["+
                "currentTime=" + this.currentTime +
                " , highestPrice=" + this.highestPrice +
                " , name=" + this.name +
                " , currentPrice=" + this.currentPrice +
                " , lastDayPrice=" + this.lastDayPrice +
                " , code=" + this.code +
                " , todayInitPrice=" + this.todayInitPrice +
                " , lowestPrice=" + this.lowestPrice +
                " , pyCode=" + this.pyCode +
        "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + Float.floatToIntBits(currentPrice);
		result = prime * result
				+ ((currentTime == null) ? 0 : currentTime.hashCode());
		result = prime * result + Float.floatToIntBits(highestPrice);
		result = prime * result + Float.floatToIntBits(lastDayPrice);
		result = prime * result + Float.floatToIntBits(lowestPrice);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pyCode == null) ? 0 : pyCode.hashCode());
		result = prime * result + Float.floatToIntBits(todayInitPrice);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockBasicMarketInfoBean other = (StockBasicMarketInfoBean) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (Float.floatToIntBits(currentPrice) != Float
				.floatToIntBits(other.currentPrice))
			return false;
		if (currentTime == null) {
			if (other.currentTime != null)
				return false;
		} else if (!currentTime.equals(other.currentTime))
			return false;
		if (Float.floatToIntBits(highestPrice) != Float
				.floatToIntBits(other.highestPrice))
			return false;
		if (Float.floatToIntBits(lastDayPrice) != Float
				.floatToIntBits(other.lastDayPrice))
			return false;
		if (Float.floatToIntBits(lowestPrice) != Float
				.floatToIntBits(other.lowestPrice))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pyCode == null) {
			if (other.pyCode != null)
				return false;
		} else if (!pyCode.equals(other.pyCode))
			return false;
		if (Float.floatToIntBits(todayInitPrice) != Float
				.floatToIntBits(other.todayInitPrice))
			return false;
		return true;
	}	

}
