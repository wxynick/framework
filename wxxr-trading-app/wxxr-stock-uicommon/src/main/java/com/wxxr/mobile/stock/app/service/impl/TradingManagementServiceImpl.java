/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements ITradingManagementService {

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);
	//=========================beans =======================
	/**
	 * 排行榜列表
	 */
	private RankListBean rank = new RankListBean();
	/**
	 * 我的交易盘列表
	 */
	private TradingAccountListBean myTradingAccounts = new TradingAccountListBean();
	/**
	 * 成交详情
	 */
	private DealDetailBean dealDetailBean = new DealDetailBean();
	/**
	 * 清算详情
	 */
	private AuditDetailBean auditDetailBean = new AuditDetailBean();
	// =================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
	}

	@Override
	protected void startService() {		
		context.registerService(ITradingManagementService.class, this);
		List<TradingAccountBean> t0_list = myTradingAccounts.getT0TradingAccounts();
		
		if (t0_list==null) {
			t0_list = mockData(0);
			myTradingAccounts.setT0TradingAccounts(t0_list);
		}
		List<TradingAccountBean> t1_list = myTradingAccounts.getT1TradingAccountBeans();
		if (t1_list==null) {
			t1_list = mockData(1);
			myTradingAccounts.setT1TradingAccountBeans(t1_list);
		}
		timer.scheduleAtFixedRate(new TimerTask() {
			
			public void run() {
				// ====mock T日实盘，未结算，盈利
				TradingAccountBean	t0ta = new TradingAccountBean();
				seq.getAndDecrement();
				t0ta.setId(1l);
				t0ta.setInitCredit(100000);
				t0ta.setIncome(3000.01f);
				t0ta.setStockCode(String.format("000%d", seq.get()));
				t0ta.setStockName("无限新锐"+seq.get());
				t0ta.setType(1);// 实盘
				t0ta.setStatus(0);// 未结算
				myTradingAccounts.getT0TradingAccounts().add(t0ta);
			}
		}, 5000, 5000);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementService.class, this);
		timer.cancel();
	}

	// =================interface method =====================================
	private Timer timer = new Timer();
	private AtomicLong seq = new AtomicLong(0);
	public TradingAccountListBean getTradingAccountList() {
		if (context.getApplication().isInDebugMode()) {
			List<TradingAccountBean> ss_list = myTradingAccounts.getSuccessTradingAccountBeans();
			
			if (ss_list==null) {
				ss_list = mockData(0);
				myTradingAccounts.setT0TradingAccounts(ss_list);
			}
			List<TradingAccountBean> all_list = myTradingAccounts.getAllTradingAccounts();
			if (all_list==null) {
				all_list = mockData(0);
				myTradingAccounts.setT0TradingAccounts(all_list);
			}
			return myTradingAccounts;
		}
		
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					List<TradingAccInfoVO> vo = getService(IRestProxyService.class).getRestService(
							TradingResourse.class).getTradingAccountList();
					if (vo!=null) {
						
					}else{
						if (!context.getApplication().isInDebugMode()) {
							myTradingAccounts.getT0TradingAccounts();
						}
					}
					
					
				} catch (Throwable e) {
					log.error("fetch data error",e);
				}
			}
		}, 10, TimeUnit.SECONDS);

		return myTradingAccounts;
	}
	
	private TradingAccountBean fromVO(TradingAccInfoVO vo){
		if (vo==null) {
			return null;
		}
		TradingAccountBean bean = new TradingAccountBean();
		bean.setId(vo.getAcctID());
		bean.setIncome(vo.getTotalGain()/100.0f);
		bean.setStockName(vo.getMaxStockName());
		bean.setStockCode(vo.getMaxStockCode());
		bean.setStatus("CLOSED".equals(vo.getOver())?1:0);
		bean.setType(vo.getStatus()==1?0:1);
		//bean.setCreateDate(vo.getCreateDate());
		return bean;
	}
	@Override
	public TradingAccountBean getTradingAccount(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createTradingAccount(String type, String credit, String stops,
			String fee1, String freezing) throws StockAppBizException {
		return null;
	}

	@Override
	public RankListBean getTMegagameRank() throws StockAppBizException {
		rank.setTRankBeans(mockRankData("T"));
		return rank;
	}

	@Override
	public RankListBean getT1MegagameRank() throws StockAppBizException {
		rank.setT1RankBeans(mockRankData("T+1"));
		return rank;
	}

	@Override
	public RankListBean getRegularTicketRank()
			throws StockAppBizException {
		rank.setRegularTicketBeans(mockRegularTicketRank());
		return rank;
	}

	@Override
	public RankListBean getWeekRank() throws StockAppBizException {
		rank.setWeekRanKBeans(mockWeekRank());
		return rank;
	}

	// =================private method =======================================
	private List<TradingAccountBean> mockData(int type) {
		List<TradingAccountBean> list = new ArrayList<TradingAccountBean>();
		TradingAccountBean t0ta = null;
		switch (type) {
		case 0:
			// ====mock T日实盘，未结算，盈利
			t0ta = new TradingAccountBean();
			t0ta.setId(1l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(3000.01f);
			t0ta.setStockCode("600521");
			t0ta.setStockName("华海药业");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ====mock T日实盘，未结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(-3000.01f);
			t0ta.setStockCode("600521");
			t0ta.setStockName("华海药业");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// =====mock T日模拟盘，未结算，平盘
			t0ta = new TradingAccountBean();
			t0ta.setId(2l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(0);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(0);// 模拟盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			break;
		case 1:
			// ===== mock T+1日模拟盘，已结算
			t0ta = new TradingAccountBean();
			t0ta.setId(2l);
			t0ta.setInitCredit(100000);
			t0ta.setIncome(300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(0);// 模拟盘
			t0ta.setStatus(1);// 结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，盈利
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(-300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、未结算，平盘
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(0);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(0);// 未结算
			list.add(t0ta);
			// ==== mock T+1日实盘、已结算，亏损
			t0ta = new TradingAccountBean();
			t0ta.setId(1002l);
			t0ta.setInitCredit(50000);
			t0ta.setIncome(-300.01f);
			t0ta.setStockCode("601169");
			t0ta.setStockName("北京银行");
			t0ta.setType(1);// 实盘
			t0ta.setStatus(1);// 结算
			list.add(t0ta);
			break;
		default:
			break;
		}
		return list;
	}

	public List<MegagameRankBean> mockRankData(String t)
			throws StockAppBizException {
		List<MegagameRankBean> list = new ArrayList<MegagameRankBean>();
		for (int i = 0; i < 10; i++) {
			MegagameRankBean mr = new MegagameRankBean();
			mr.setAcctID(1);
			mr.setNickName("模拟用户" + i);
			mr.setGainRate("+10.01%");

			if (t.equals("T")) {
				mr.setRankSeq(i+1);
				mr.setStatus(1);
				mr.setMaxStockCode("600521");
				mr.setMaxStockMarket("华海药业");
			} else {
				mr.setRankSeq(i+1);
				mr.setStatus(0);
				mr.setMaxStockCode("600175");
				mr.setMaxStockMarket("美都控股");
			}

			list.add(mr);
		}
		return list;
	}

	public List<RegularTicketBean> mockRegularTicketRank()
			throws StockAppBizException {
		List<RegularTicketBean> list = new ArrayList<RegularTicketBean>();
		for (int i = 0; i < 10; i++) {
			RegularTicketBean mr = new RegularTicketBean();
			mr.setRankSeq(i+1);
			mr.setNickName("模拟用户" + i);
			mr.setRegular(1000 * (10 - i));
			mr.setGainCount(6 - i % 5);
			list.add(mr);
		}
		return list;
	}

	public List<WeekRankBean> mockWeekRank() throws StockAppBizException {
		List<WeekRankBean> list = new ArrayList<WeekRankBean>();
		for (int i = 0; i < 10; i++) {
			WeekRankBean mr = new WeekRankBean();
			mr.setRankSeq(i+1);
			mr.setDates("2013年11月04日-2013年11月08日");
			mr.setGainCount(6 - i % 5);
			mr.setNickName("模拟用户" + i);
			mr.setGainRate(String.format("+%s", 15 - i) + "%");
			list.add(mr);
		}
		return list;
	}

	public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
		UserCreateTradAccInfoBean info = new UserCreateTradAccInfoBean();
		info.setCapitalRate(0.05f);
		info.setCostRate(0.00399f);
		info.setDepositRate(0.05f);
		info.setMaxAmount(30000000l);
		info.setRateString("0.08;0.10,0.12;0.13,0.05;0.08");
		info.setVoucherCostRate(0.00399f);
		return info;
	}

	@Override
	public TradingAccountListBean getMyTradingAccountList()
			throws StockAppBizException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradingAccountListBean getOtherTradingAccountList(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DealDetailBean getDealDetail(String accId) {
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				
			}
		}, 0, TimeUnit.SECONDS);
		dealDetailBean.setFund("10万");
		dealDetailBean.setPlRisk(0.001f);
		dealDetailBean.setUserGain(1000);
		dealDetailBean.setImgUrl(new String[]{"#"});
		dealDetailBean.setTradingRecords(mockTradingRecord());
		return dealDetailBean;
	}
	private List<TradingRecordBean> mockTradingRecord(){
		List<TradingRecordBean> list = new ArrayList<TradingRecordBean>();
		TradingRecordBean t = new TradingRecordBean();
		t.setDate(System.currentTimeMillis());
		t.setBeDone(true);
		t.setDay(0);
		t.setCode("600521");
		t.setFee(3990);
		t.setPrice(1256);
		t.setTax(1230);
		t.setVol(3500);
		t.setAmount(12000000);
		t.setDescribe("买入成交");
		list.add(t);
		return list;
	}

	@Override
	public AuditDetailBean getAuditDetail(String accId) {
		auditDetailBean.setAccountPay("122.3");
		auditDetailBean.setBuyDay("2013-11-12");
		auditDetailBean.setDeadline("2013-11-13");
		auditDetailBean.setBuyAverage("39.9");
		auditDetailBean.setId("123");
		auditDetailBean.setFrozenAmount("0");
		auditDetailBean.setFund("10万");
		
		return auditDetailBean;
	}

}
