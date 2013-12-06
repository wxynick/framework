/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;
import com.wxxr.mobile.core.ui.common.ListDecorator;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class DealDetailBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private List<TradingRecordBean> tradingRecords;
	private String[] imgUrl;
	private float totalGain;
	private float userGain;
	private String fund;
	private String plRisk;

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
	 * @return the tradingRecords
	 */
	public List<TradingRecordBean> getTradingRecords() {
		return tradingRecords;
	}

	/**
	 * @param tradingRecords the tradingRecords to set
	 */
	public void setTradingRecords(List<TradingRecordBean> tradingRecords) {
		List<TradingRecordBean> old = this.tradingRecords;
		this.tradingRecords = tradingRecords;
		if(this.tradingRecords != null){
            this.tradingRecords = new ListDecorator<TradingRecordBean>("tradingRecords", this.emitter,this.tradingRecords);
        }
		this.emitter.firePropertyChange("tradingRecords", old, this.tradingRecords);
	}

	/**
	 * @return the imgUrl
	 */
	public String[] getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String[] imgUrl) {
		String[] old = this.imgUrl;
		this.imgUrl = imgUrl;
		this.emitter.firePropertyChange("imgUrl", old, this.imgUrl);
	}

	/**
	 * @return the totalGain
	 */
	public float getTotalGain() {
		return totalGain;
	}

	/**
	 * @param totalGain the totalGain to set
	 */
	public void setTotalGain(float totalGain) {
		float old = this.totalGain;
		this.totalGain = totalGain;
		this.emitter.firePropertyChange("totalGain", old, this.totalGain);
	}

	/**
	 * @return the userGain
	 */
	public float getUserGain() {
		return userGain;
	}

	/**
	 * @param userGain the userGain to set
	 */
	public void setUserGain(float userGain) {
		float old = this.userGain;
		this.userGain = userGain;
		this.emitter.firePropertyChange("userGain", old, this.userGain);
	}

	/**
	 * @return the fund
	 */
	public String getFund() {
		return fund;
	}

	/**
	 * @param fund the fund to set
	 */
	public void setFund(String fund) {
		String old = this.fund;
		this.fund = fund;
		this.emitter.firePropertyChange("fund", old, this.fund);
	}

	/**
	 * @return the plRisk
	 */
	public String getPlRisk() {
		return plRisk;
	}

	/**
	 * @param plRisk the plRisk to set
	 */
	public void setPlRisk(String plRisk) {
	    String old = this.plRisk;
		this.plRisk = plRisk;
		this.emitter.firePropertyChange("plRisk", old, this.plRisk);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "DealDetailBean ["+
                "tradingRecords=" + this.tradingRecords +
                " , imgUrl=" + this.imgUrl +
                " , totalGain=" + this.totalGain +
                " , userGain=" + this.userGain +
                " , fund=" + this.fund +
                " , plRisk=" + this.plRisk +
        "]";
    }	

}
