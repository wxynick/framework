package com.wxxr.mobile.stock.client.bean;


public class WeekRankVO{


	private String nickName;//昵称
	private int gainCount;//正收益个数1
	private Long totalGain;//总盈亏3
	private String gainRate;//总盈亏率2
	private int gainRates;
	private String dates;//周期时间
	private String uesrId;//用户id
	
	
	
	public String getUesrId() {
		return uesrId;
	}
	public void setUesrId(String uesrId) {
		this.uesrId = uesrId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getGainCount() {
		return gainCount;
	}
	public void setGainCount(int gainCount) {
		this.gainCount = gainCount;
	}
	
	public Long getTotalGain() {
		return totalGain;
	}
	public void setTotalGain(Long totalGain) {
		this.totalGain = totalGain;
	}
	
	public String getGainRate() {
		return gainRate;
	}
	public void setGainRate(String gainRate) {
		this.gainRate = gainRate;
	}
	
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	
	
	public int getGainRates() {
		return gainRates;
	}
	public void setGainRates(int gainRates) {
		this.gainRates = gainRates;
	}
	@Override
	public String toString() {
		return "WeekRankVO [nickName=" + nickName + ", gainCount=" + gainCount
				+ ", totalGain=" + totalGain + ", gainRate=" + gainRate
				+ ", gainRates=" + gainRates + ", dates=" + dates + "]";
	}
	
	
}
