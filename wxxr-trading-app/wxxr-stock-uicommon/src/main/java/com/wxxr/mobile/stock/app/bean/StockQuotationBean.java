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
public class StockQuotationBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private Long risefallrate;
	private Long change;
	private Long szjs;
	private Long buysum;
	private Long secuvolume;
	private Long capital;
	private Long close;
	private Long profitrate;
	private Long open;
	private Long sellsum;
	private String datetime;
	private Long sellvolume2;
	private Long sellvolume3;
	private Long buyprice2;
	private Long sellvolume1;
	private Long buyprice1;
	private Long marketvalue;
	private Long status;
	private String code;
	private Long averageprice;
	private Long sellvolume4;
	private Long sellvolume5;
	private Long handrate;
	private Long ppjs;
	private String market;
	private Long sellprice5;
	private Long xdjs;
	private Long sellprice3;
	private Long buyvolume4;
	private Long newprice;
	private Long sellprice4;
	private Long buyvolume5;
	private Long lb;
	private Long secuamount;
	private Long high;
	private Long sellprice1;
	private Long low;
	private Long buyprice3;
	private Long buyvolume2;
	private Long sellprice2;
	private Long buyprice4;
	private Long buyvolume3;
	private Long buyprice5;
	private Long buyvolume1;
	
	private Integer power;

	private String stockName;
	
	private boolean added;
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
	 * @return the risefallrate
	 */
	public Long getRisefallrate() {
		return risefallrate;
	}

	/**
	 * @param risefallrate the risefallrate to set
	 */
	public void setRisefallrate(Long risefallrate) {
		Long old = this.risefallrate;
		this.risefallrate = risefallrate;
		this.emitter.firePropertyChange("risefallrate", old, this.risefallrate);
	}

	/**
	 * @return the change
	 */
	public Long getChange() {
		return change;
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(Long change) {
		Long old = this.change;
		this.change = change;
		this.emitter.firePropertyChange("change", old, this.change);
	}

	/**
	 * @return the szjs
	 */
	public Long getSzjs() {
		return szjs;
	}

	/**
	 * @param szjs the szjs to set
	 */
	public void setSzjs(Long szjs) {
		Long old = this.szjs;
		this.szjs = szjs;
		this.emitter.firePropertyChange("szjs", old, this.szjs);
	}

	/**
	 * @return the buysum
	 */
	public Long getBuysum() {
		return buysum;
	}

	/**
	 * @param buysum the buysum to set
	 */
	public void setBuysum(Long buysum) {
		Long old = this.buysum;
		this.buysum = buysum;
		this.emitter.firePropertyChange("buysum", old, this.buysum);
	}

	/**
	 * @return the secuvolume
	 */
	public Long getSecuvolume() {
		return secuvolume;
	}

	/**
	 * @param secuvolume the secuvolume to set
	 */
	public void setSecuvolume(Long secuvolume) {
		Long old = this.secuvolume;
		this.secuvolume = secuvolume;
		this.emitter.firePropertyChange("secuvolume", old, this.secuvolume);
	}

	/**
	 * @return the capital
	 */
	public Long getCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	public void setCapital(Long capital) {
		Long old = this.capital;
		this.capital = capital;
		this.emitter.firePropertyChange("capital", old, this.capital);
	}

	/**
	 * @return the close
	 */
	public Long getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(Long close) {
		Long old = this.close;
		this.close = close;
		this.emitter.firePropertyChange("close", old, this.close);
	}

	/**
	 * @return the profitrate
	 */
	public Long getProfitrate() {
		return profitrate;
	}

	/**
	 * @param profitrate the profitrate to set
	 */
	public void setProfitrate(Long profitrate) {
		Long old = this.profitrate;
		this.profitrate = profitrate;
		this.emitter.firePropertyChange("profitrate", old, this.profitrate);
	}

	/**
	 * @return the open
	 */
	public Long getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(Long open) {
		Long old = this.open;
		this.open = open;
		this.emitter.firePropertyChange("open", old, this.open);
	}

	/**
	 * @return the sellsum
	 */
	public Long getSellsum() {
		return sellsum;
	}

	/**
	 * @param sellsum the sellsum to set
	 */
	public void setSellsum(Long sellsum) {
		Long old = this.sellsum;
		this.sellsum = sellsum;
		this.emitter.firePropertyChange("sellsum", old, this.sellsum);
	}

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(String datetime) {
		String old = this.datetime;
		this.datetime = datetime;
		this.emitter.firePropertyChange("datetime", old, this.datetime);
	}

	/**
	 * @return the sellvolume2
	 */
	public Long getSellvolume2() {
		return sellvolume2;
	}

	/**
	 * @param sellvolume2 the sellvolume2 to set
	 */
	public void setSellvolume2(Long sellvolume2) {
		Long old = this.sellvolume2;
		this.sellvolume2 = sellvolume2;
		this.emitter.firePropertyChange("sellvolume2", old, this.sellvolume2);
	}

	/**
	 * @return the sellvolume3
	 */
	public Long getSellvolume3() {
		return sellvolume3;
	}

	/**
	 * @param sellvolume3 the sellvolume3 to set
	 */
	public void setSellvolume3(Long sellvolume3) {
		Long old = this.sellvolume3;
		this.sellvolume3 = sellvolume3;
		this.emitter.firePropertyChange("sellvolume3", old, this.sellvolume3);
	}

	/**
	 * @return the buyprice2
	 */
	public Long getBuyprice2() {
		return buyprice2;
	}

	/**
	 * @param buyprice2 the buyprice2 to set
	 */
	public void setBuyprice2(Long buyprice2) {
		Long old = this.buyprice2;
		this.buyprice2 = buyprice2;
		this.emitter.firePropertyChange("buyprice2", old, this.buyprice2);
	}

	/**
	 * @return the sellvolume1
	 */
	public Long getSellvolume1() {
		return sellvolume1;
	}

	/**
	 * @param sellvolume1 the sellvolume1 to set
	 */
	public void setSellvolume1(Long sellvolume1) {
		Long old = this.sellvolume1;
		this.sellvolume1 = sellvolume1;
		this.emitter.firePropertyChange("sellvolume1", old, this.sellvolume1);
	}

	/**
	 * @return the buyprice1
	 */
	public Long getBuyprice1() {
		return buyprice1;
	}

	/**
	 * @param buyprice1 the buyprice1 to set
	 */
	public void setBuyprice1(Long buyprice1) {
		Long old = this.buyprice1;
		this.buyprice1 = buyprice1;
		this.emitter.firePropertyChange("buyprice1", old, this.buyprice1);
	}

	/**
	 * @return the marketvalue
	 */
	public Long getMarketvalue() {
		return marketvalue;
	}

	/**
	 * @param marketvalue the marketvalue to set
	 */
	public void setMarketvalue(Long marketvalue) {
		Long old = this.marketvalue;
		this.marketvalue = marketvalue;
		this.emitter.firePropertyChange("marketvalue", old, this.marketvalue);
	}

	/**
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		Long old = this.status;
		this.status = status;
		this.emitter.firePropertyChange("status", old, this.status);
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
	 * @return the averageprice
	 */
	public Long getAverageprice() {
		return averageprice;
	}

	/**
	 * @param averageprice the averageprice to set
	 */
	public void setAverageprice(Long averageprice) {
		Long old = this.averageprice;
		this.averageprice = averageprice;
		this.emitter.firePropertyChange("averageprice", old, this.averageprice);
	}

	/**
	 * @return the sellvolume4
	 */
	public Long getSellvolume4() {
		return sellvolume4;
	}

	/**
	 * @param sellvolume4 the sellvolume4 to set
	 */
	public void setSellvolume4(Long sellvolume4) {
		Long old = this.sellvolume4;
		this.sellvolume4 = sellvolume4;
		this.emitter.firePropertyChange("sellvolume4", old, this.sellvolume4);
	}

	/**
	 * @return the sellvolume5
	 */
	public Long getSellvolume5() {
		return sellvolume5;
	}

	/**
	 * @param sellvolume5 the sellvolume5 to set
	 */
	public void setSellvolume5(Long sellvolume5) {
		Long old = this.sellvolume5;
		this.sellvolume5 = sellvolume5;
		this.emitter.firePropertyChange("sellvolume5", old, this.sellvolume5);
	}

	/**
	 * @return the handrate
	 */
	public Long getHandrate() {
		return handrate;
	}

	/**
	 * @param handrate the handrate to set
	 */
	public void setHandrate(Long handrate) {
		Long old = this.handrate;
		this.handrate = handrate;
		this.emitter.firePropertyChange("handrate", old, this.handrate);
	}

	/**
	 * @return the ppjs
	 */
	public Long getPpjs() {
		return ppjs;
	}

	/**
	 * @param ppjs the ppjs to set
	 */
	public void setPpjs(Long ppjs) {
		Long old = this.ppjs;
		this.ppjs = ppjs;
		this.emitter.firePropertyChange("ppjs", old, this.ppjs);
	}

	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}

	/**
	 * @param market the market to set
	 */
	public void setMarket(String market) {
		String old = this.market;
		this.market = market;
		this.emitter.firePropertyChange("market", old, this.market);
	}

	/**
	 * @return the sellprice5
	 */
	public Long getSellprice5() {
		return sellprice5;
	}

	/**
	 * @param sellprice5 the sellprice5 to set
	 */
	public void setSellprice5(Long sellprice5) {
		Long old = this.sellprice5;
		this.sellprice5 = sellprice5;
		this.emitter.firePropertyChange("sellprice5", old, this.sellprice5);
	}

	/**
	 * @return the xdjs
	 */
	public Long getXdjs() {
		return xdjs;
	}

	/**
	 * @param xdjs the xdjs to set
	 */
	public void setXdjs(Long xdjs) {
		Long old = this.xdjs;
		this.xdjs = xdjs;
		this.emitter.firePropertyChange("xdjs", old, this.xdjs);
	}

	/**
	 * @return the sellprice3
	 */
	public Long getSellprice3() {
		return sellprice3;
	}

	/**
	 * @param sellprice3 the sellprice3 to set
	 */
	public void setSellprice3(Long sellprice3) {
		Long old = this.sellprice3;
		this.sellprice3 = sellprice3;
		this.emitter.firePropertyChange("sellprice3", old, this.sellprice3);
	}

	/**
	 * @return the buyvolume4
	 */
	public Long getBuyvolume4() {
		return buyvolume4;
	}

	/**
	 * @param buyvolume4 the buyvolume4 to set
	 */
	public void setBuyvolume4(Long buyvolume4) {
		Long old = this.buyvolume4;
		this.buyvolume4 = buyvolume4;
		this.emitter.firePropertyChange("buyvolume4", old, this.buyvolume4);
	}

	/**
	 * @return the newprice
	 */
	public Long getNewprice() {
		return newprice;
	}

	/**
	 * @param newprice the newprice to set
	 */
	public void setNewprice(Long newprice) {
		Long old = this.newprice;
		this.newprice = newprice;
		this.emitter.firePropertyChange("newprice", old, this.newprice);
	}

	/**
	 * @return the sellprice4
	 */
	public Long getSellprice4() {
		return sellprice4;
	}

	/**
	 * @param sellprice4 the sellprice4 to set
	 */
	public void setSellprice4(Long sellprice4) {
		Long old = this.sellprice4;
		this.sellprice4 = sellprice4;
		this.emitter.firePropertyChange("sellprice4", old, this.sellprice4);
	}

	/**
	 * @return the buyvolume5
	 */
	public Long getBuyvolume5() {
		return buyvolume5;
	}

	/**
	 * @param buyvolume5 the buyvolume5 to set
	 */
	public void setBuyvolume5(Long buyvolume5) {
		Long old = this.buyvolume5;
		this.buyvolume5 = buyvolume5;
		this.emitter.firePropertyChange("buyvolume5", old, this.buyvolume5);
	}

	/**
	 * @return the lb
	 */
	public Long getLb() {
		return lb;
	}

	/**
	 * @param lb the lb to set
	 */
	public void setLb(Long lb) {
		Long old = this.lb;
		this.lb = lb;
		this.emitter.firePropertyChange("lb", old, this.lb);
	}

	/**
	 * @return the secuamount
	 */
	public Long getSecuamount() {
		return secuamount;
	}

	/**
	 * @param secuamount the secuamount to set
	 */
	public void setSecuamount(Long secuamount) {
		Long old = this.secuamount;
		this.secuamount = secuamount;
		this.emitter.firePropertyChange("secuamount", old, this.secuamount);
	}

	/**
	 * @return the high
	 */
	public Long getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(Long high) {
		Long old = this.high;
		this.high = high;
		this.emitter.firePropertyChange("high", old, this.high);
	}

	/**
	 * @return the sellprice1
	 */
	public Long getSellprice1() {
		return sellprice1;
	}

	/**
	 * @param sellprice1 the sellprice1 to set
	 */
	public void setSellprice1(Long sellprice1) {
		Long old = this.sellprice1;
		this.sellprice1 = sellprice1;
		this.emitter.firePropertyChange("sellprice1", old, this.sellprice1);
	}

	/**
	 * @return the low
	 */
	public Long getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(Long low) {
		Long old = this.low;
		this.low = low;
		this.emitter.firePropertyChange("low", old, this.low);
	}

	/**
	 * @return the buyprice3
	 */
	public Long getBuyprice3() {
		return buyprice3;
	}

	/**
	 * @param buyprice3 the buyprice3 to set
	 */
	public void setBuyprice3(Long buyprice3) {
		Long old = this.buyprice3;
		this.buyprice3 = buyprice3;
		this.emitter.firePropertyChange("buyprice3", old, this.buyprice3);
	}

	/**
	 * @return the buyvolume2
	 */
	public Long getBuyvolume2() {
		return buyvolume2;
	}

	/**
	 * @param buyvolume2 the buyvolume2 to set
	 */
	public void setBuyvolume2(Long buyvolume2) {
		Long old = this.buyvolume2;
		this.buyvolume2 = buyvolume2;
		this.emitter.firePropertyChange("buyvolume2", old, this.buyvolume2);
	}

	/**
	 * @return the sellprice2
	 */
	public Long getSellprice2() {
		return sellprice2;
	}

	/**
	 * @param sellprice2 the sellprice2 to set
	 */
	public void setSellprice2(Long sellprice2) {
		Long old = this.sellprice2;
		this.sellprice2 = sellprice2;
		this.emitter.firePropertyChange("sellprice2", old, this.sellprice2);
	}

	/**
	 * @return the buyprice4
	 */
	public Long getBuyprice4() {
		return buyprice4;
	}

	/**
	 * @param buyprice4 the buyprice4 to set
	 */
	public void setBuyprice4(Long buyprice4) {
		Long old = this.buyprice4;
		this.buyprice4 = buyprice4;
		this.emitter.firePropertyChange("buyprice4", old, this.buyprice4);
	}

	/**
	 * @return the buyvolume3
	 */
	public Long getBuyvolume3() {
		return buyvolume3;
	}

	/**
	 * @param buyvolume3 the buyvolume3 to set
	 */
	public void setBuyvolume3(Long buyvolume3) {
		Long old = this.buyvolume3;
		this.buyvolume3 = buyvolume3;
		this.emitter.firePropertyChange("buyvolume3", old, this.buyvolume3);
	}

	/**
	 * @return the buyprice5
	 */
	public Long getBuyprice5() {
		return buyprice5;
	}

	/**
	 * @param buyprice5 the buyprice5 to set
	 */
	public void setBuyprice5(Long buyprice5) {
		Long old = this.buyprice5;
		this.buyprice5 = buyprice5;
		this.emitter.firePropertyChange("buyprice5", old, this.buyprice5);
	}

	/**
	 * @return the buyvolume1
	 */
	public Long getBuyvolume1() {
		return buyvolume1;
	}

	/**
	 * @param buyvolume1 the buyvolume1 to set
	 */
	public void setBuyvolume1(Long buyvolume1) {
		Long old = this.buyvolume1;
		this.buyvolume1 = buyvolume1;
		this.emitter.firePropertyChange("buyvolume1", old, this.buyvolume1);
	}

    public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		String old = this.stockName;
		this.stockName = stockName;
		this.emitter.firePropertyChange("stockName", old, this.stockName);
	}

	

	public boolean getAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		boolean old = this.added;
		this.added = added;
		this.emitter.firePropertyChange("added", old, this.added);
	}

	@Override
	public String toString() {
		return "StockQuotationBean [risefallrate=" + risefallrate + ", change="
				+ change + ", szjs=" + szjs + ", buysum=" + buysum
				+ ", secuvolume=" + secuvolume + ", capital=" + capital
				+ ", close=" + close + ", profitrate=" + profitrate + ", open="
				+ open + ", sellsum=" + sellsum + ", datetime=" + datetime
				+ ", sellvolume2=" + sellvolume2 + ", sellvolume3="
				+ sellvolume3 + ", buyprice2=" + buyprice2 + ", sellvolume1="
				+ sellvolume1 + ", buyprice1=" + buyprice1 + ", marketvalue="
				+ marketvalue + ", status=" + status + ", code=" + code
				+ ", averageprice=" + averageprice + ", sellvolume4="
				+ sellvolume4 + ", sellvolume5=" + sellvolume5 + ", handrate="
				+ handrate + ", ppjs=" + ppjs + ", market=" + market
				+ ", sellprice5=" + sellprice5 + ", xdjs=" + xdjs
				+ ", sellprice3=" + sellprice3 + ", buyvolume4=" + buyvolume4
				+ ", newprice=" + newprice + ", sellprice4=" + sellprice4
				+ ", buyvolume5=" + buyvolume5 + ", lb=" + lb + ", secuamount="
				+ secuamount + ", high=" + high + ", sellprice1=" + sellprice1
				+ ", low=" + low + ", buyprice3=" + buyprice3 + ", buyvolume2="
				+ buyvolume2 + ", sellprice2=" + sellprice2 + ", buyprice4="
				+ buyprice4 + ", buyvolume3=" + buyvolume3 + ", buyprice5="
				+ buyprice5 + ", buyvolume1=" + buyvolume1 + ", power=" + power
				+ ", stockName=" + stockName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (added ? 1231 : 1237);
		result = prime * result
				+ ((averageprice == null) ? 0 : averageprice.hashCode());
		result = prime * result
				+ ((buyprice1 == null) ? 0 : buyprice1.hashCode());
		result = prime * result
				+ ((buyprice2 == null) ? 0 : buyprice2.hashCode());
		result = prime * result
				+ ((buyprice3 == null) ? 0 : buyprice3.hashCode());
		result = prime * result
				+ ((buyprice4 == null) ? 0 : buyprice4.hashCode());
		result = prime * result
				+ ((buyprice5 == null) ? 0 : buyprice5.hashCode());
		result = prime * result + ((buysum == null) ? 0 : buysum.hashCode());
		result = prime * result
				+ ((buyvolume1 == null) ? 0 : buyvolume1.hashCode());
		result = prime * result
				+ ((buyvolume2 == null) ? 0 : buyvolume2.hashCode());
		result = prime * result
				+ ((buyvolume3 == null) ? 0 : buyvolume3.hashCode());
		result = prime * result
				+ ((buyvolume4 == null) ? 0 : buyvolume4.hashCode());
		result = prime * result
				+ ((buyvolume5 == null) ? 0 : buyvolume5.hashCode());
		result = prime * result + ((capital == null) ? 0 : capital.hashCode());
		result = prime * result + ((change == null) ? 0 : change.hashCode());
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ ((datetime == null) ? 0 : datetime.hashCode());
		result = prime * result
				+ ((handrate == null) ? 0 : handrate.hashCode());
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((lb == null) ? 0 : lb.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		result = prime * result
				+ ((marketvalue == null) ? 0 : marketvalue.hashCode());
		result = prime * result
				+ ((newprice == null) ? 0 : newprice.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((power == null) ? 0 : power.hashCode());
		result = prime * result + ((ppjs == null) ? 0 : ppjs.hashCode());
		result = prime * result
				+ ((profitrate == null) ? 0 : profitrate.hashCode());
		result = prime * result
				+ ((risefallrate == null) ? 0 : risefallrate.hashCode());
		result = prime * result
				+ ((secuamount == null) ? 0 : secuamount.hashCode());
		result = prime * result
				+ ((secuvolume == null) ? 0 : secuvolume.hashCode());
		result = prime * result
				+ ((sellprice1 == null) ? 0 : sellprice1.hashCode());
		result = prime * result
				+ ((sellprice2 == null) ? 0 : sellprice2.hashCode());
		result = prime * result
				+ ((sellprice3 == null) ? 0 : sellprice3.hashCode());
		result = prime * result
				+ ((sellprice4 == null) ? 0 : sellprice4.hashCode());
		result = prime * result
				+ ((sellprice5 == null) ? 0 : sellprice5.hashCode());
		result = prime * result + ((sellsum == null) ? 0 : sellsum.hashCode());
		result = prime * result
				+ ((sellvolume1 == null) ? 0 : sellvolume1.hashCode());
		result = prime * result
				+ ((sellvolume2 == null) ? 0 : sellvolume2.hashCode());
		result = prime * result
				+ ((sellvolume3 == null) ? 0 : sellvolume3.hashCode());
		result = prime * result
				+ ((sellvolume4 == null) ? 0 : sellvolume4.hashCode());
		result = prime * result
				+ ((sellvolume5 == null) ? 0 : sellvolume5.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((stockName == null) ? 0 : stockName.hashCode());
		result = prime * result + ((szjs == null) ? 0 : szjs.hashCode());
		result = prime * result + ((xdjs == null) ? 0 : xdjs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockQuotationBean other = (StockQuotationBean) obj;
		if (added != other.added)
			return false;
		if (averageprice == null) {
			if (other.averageprice != null)
				return false;
		} else if (!averageprice.equals(other.averageprice))
			return false;
		if (buyprice1 == null) {
			if (other.buyprice1 != null)
				return false;
		} else if (!buyprice1.equals(other.buyprice1))
			return false;
		if (buyprice2 == null) {
			if (other.buyprice2 != null)
				return false;
		} else if (!buyprice2.equals(other.buyprice2))
			return false;
		if (buyprice3 == null) {
			if (other.buyprice3 != null)
				return false;
		} else if (!buyprice3.equals(other.buyprice3))
			return false;
		if (buyprice4 == null) {
			if (other.buyprice4 != null)
				return false;
		} else if (!buyprice4.equals(other.buyprice4))
			return false;
		if (buyprice5 == null) {
			if (other.buyprice5 != null)
				return false;
		} else if (!buyprice5.equals(other.buyprice5))
			return false;
		if (buysum == null) {
			if (other.buysum != null)
				return false;
		} else if (!buysum.equals(other.buysum))
			return false;
		if (buyvolume1 == null) {
			if (other.buyvolume1 != null)
				return false;
		} else if (!buyvolume1.equals(other.buyvolume1))
			return false;
		if (buyvolume2 == null) {
			if (other.buyvolume2 != null)
				return false;
		} else if (!buyvolume2.equals(other.buyvolume2))
			return false;
		if (buyvolume3 == null) {
			if (other.buyvolume3 != null)
				return false;
		} else if (!buyvolume3.equals(other.buyvolume3))
			return false;
		if (buyvolume4 == null) {
			if (other.buyvolume4 != null)
				return false;
		} else if (!buyvolume4.equals(other.buyvolume4))
			return false;
		if (buyvolume5 == null) {
			if (other.buyvolume5 != null)
				return false;
		} else if (!buyvolume5.equals(other.buyvolume5))
			return false;
		if (capital == null) {
			if (other.capital != null)
				return false;
		} else if (!capital.equals(other.capital))
			return false;
		if (change == null) {
			if (other.change != null)
				return false;
		} else if (!change.equals(other.change))
			return false;
		if (close == null) {
			if (other.close != null)
				return false;
		} else if (!close.equals(other.close))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (datetime == null) {
			if (other.datetime != null)
				return false;
		} else if (!datetime.equals(other.datetime))
			return false;
		if (handrate == null) {
			if (other.handrate != null)
				return false;
		} else if (!handrate.equals(other.handrate))
			return false;
		if (high == null) {
			if (other.high != null)
				return false;
		} else if (!high.equals(other.high))
			return false;
		if (lb == null) {
			if (other.lb != null)
				return false;
		} else if (!lb.equals(other.lb))
			return false;
		if (low == null) {
			if (other.low != null)
				return false;
		} else if (!low.equals(other.low))
			return false;
		if (market == null) {
			if (other.market != null)
				return false;
		} else if (!market.equals(other.market))
			return false;
		if (marketvalue == null) {
			if (other.marketvalue != null)
				return false;
		} else if (!marketvalue.equals(other.marketvalue))
			return false;
		if (newprice == null) {
			if (other.newprice != null)
				return false;
		} else if (!newprice.equals(other.newprice))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (power == null) {
			if (other.power != null)
				return false;
		} else if (!power.equals(other.power))
			return false;
		if (ppjs == null) {
			if (other.ppjs != null)
				return false;
		} else if (!ppjs.equals(other.ppjs))
			return false;
		if (profitrate == null) {
			if (other.profitrate != null)
				return false;
		} else if (!profitrate.equals(other.profitrate))
			return false;
		if (risefallrate == null) {
			if (other.risefallrate != null)
				return false;
		} else if (!risefallrate.equals(other.risefallrate))
			return false;
		if (secuamount == null) {
			if (other.secuamount != null)
				return false;
		} else if (!secuamount.equals(other.secuamount))
			return false;
		if (secuvolume == null) {
			if (other.secuvolume != null)
				return false;
		} else if (!secuvolume.equals(other.secuvolume))
			return false;
		if (sellprice1 == null) {
			if (other.sellprice1 != null)
				return false;
		} else if (!sellprice1.equals(other.sellprice1))
			return false;
		if (sellprice2 == null) {
			if (other.sellprice2 != null)
				return false;
		} else if (!sellprice2.equals(other.sellprice2))
			return false;
		if (sellprice3 == null) {
			if (other.sellprice3 != null)
				return false;
		} else if (!sellprice3.equals(other.sellprice3))
			return false;
		if (sellprice4 == null) {
			if (other.sellprice4 != null)
				return false;
		} else if (!sellprice4.equals(other.sellprice4))
			return false;
		if (sellprice5 == null) {
			if (other.sellprice5 != null)
				return false;
		} else if (!sellprice5.equals(other.sellprice5))
			return false;
		if (sellsum == null) {
			if (other.sellsum != null)
				return false;
		} else if (!sellsum.equals(other.sellsum))
			return false;
		if (sellvolume1 == null) {
			if (other.sellvolume1 != null)
				return false;
		} else if (!sellvolume1.equals(other.sellvolume1))
			return false;
		if (sellvolume2 == null) {
			if (other.sellvolume2 != null)
				return false;
		} else if (!sellvolume2.equals(other.sellvolume2))
			return false;
		if (sellvolume3 == null) {
			if (other.sellvolume3 != null)
				return false;
		} else if (!sellvolume3.equals(other.sellvolume3))
			return false;
		if (sellvolume4 == null) {
			if (other.sellvolume4 != null)
				return false;
		} else if (!sellvolume4.equals(other.sellvolume4))
			return false;
		if (sellvolume5 == null) {
			if (other.sellvolume5 != null)
				return false;
		} else if (!sellvolume5.equals(other.sellvolume5))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (stockName == null) {
			if (other.stockName != null)
				return false;
		} else if (!stockName.equals(other.stockName))
			return false;
		if (szjs == null) {
			if (other.szjs != null)
				return false;
		} else if (!szjs.equals(other.szjs))
			return false;
		if (xdjs == null) {
			if (other.xdjs != null)
				return false;
		} else if (!xdjs.equals(other.xdjs))
			return false;
		return true;
	}

	

}
