/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IInfoCenterManagementService {

	private static final Trace log = Trace.register(InfoCenterManagementServiceImpl.class);
	private SearchStockListBean stockListbean = new SearchStockListBean();
	//====================interface methods =====================
	@Override
	public SearchStockListBean searchStock(String keyword) {
		List<StockBaseInfoBean> list = new ArrayList<StockBaseInfoBean>();
		if(StringUtils.isEmpty(keyword)) {
			stockListbean.setSearchResult(list);
			return stockListbean;
		}
		List<StockBaseInfoBean> searchList = MockDataUtils.getAllMockDataForSearchStock();
		for(StockBaseInfoBean bean : searchList) {
			if(bean.toString().contains(keyword)) {
				list.add(bean);
			}
		}
		stockListbean.setSearchResult(list);
		return this.stockListbean;
	}


	
	// ====================module life cycle methods ==================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);

	}


	@Override
	protected void startService() {
		context.registerService(IInfoCenterManagementService.class, this);

	}


	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagementService.class, this);
	}

	@Override
	public StockMinuteKBean getMinuteline(String code, String market,
			Long start, Long limit, String date, Long startTime, Long endTime,
			int page) {
		// TODO Auto-generated method stub
		return null;
	}


}
