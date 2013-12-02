/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisListBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.loader.StockMinuteKLoader;
import com.wxxr.mobile.stock.app.service.loader.StockQuotationLoader;
import com.wxxr.mobile.stock.app.service.loader.StockTaxisVOLoader;
import com.wxxr.mobile.stock.sync.model.StockBaseInfo;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.resource.StockResource;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements
		IInfoCenterManagementService {

	private static final Trace log = Trace
			.register(InfoCenterManagementServiceImpl.class);
	private SearchStockListBean stockListbean = new SearchStockListBean();
	private StockQuotationBean stockQuotationbean = new StockQuotationBean();
	protected LineListBean lineListBean = new LineListBean();
	
	// ====================module life cycle methods ==================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
        addRequiredService(IEntityLoaderRegistry.class);

	}

	@Override
	protected void startService() {
	    
		context.registerService(IInfoCenterManagementService.class, this);
        IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
        stockQuotationBean_cache=new GenericReloadableEntityCache<String, StockQuotationBean, List>("StockQuotation",30);
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockQuotation", new StockQuotationLoader());
        
        
        stockMinuteKBean_cache=new GenericReloadableEntityCache<String, StockMinuteKBean, List>("StockMinuteK");
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockMinuteK", new StockMinuteKLoader());
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("StockTaxisVO", new StockTaxisVOLoader());
	}

	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagementService.class, this);
	}

	// ====================interface methods =====================
	@Override
	public SearchStockListBean searchStock(final String keyword) {
		List<StockBaseInfo> ret = getService(IStockInfoSyncService.class).getStockInfos(new IEntityFilter<StockBaseInfo>() {
			public boolean doFilter(StockBaseInfo entity) {
				if (StringUtils.isBlank(keyword)) {
					return false;
				}
				return entity.getAbbr().startsWith(keyword)||entity.getCode().startsWith(keyword);
			}
		});
		
		stockListbean.setSearchResult(ret);
		return this.stockListbean;
	}

	private <T> T getRestService(Class<T> restResouce) {
		return context.getService(IRestProxyService.class).getRestService(
				restResouce);
	}
	//分钟线
	@Override
	public StockMinuteKBean getMinuteline(Map<String, String> params) {
	   String market= params.get("market");
	   String code= params.get("code");
	   if (StringUtils.isBlank(market) || StringUtils.isBlank(code) ){
	       return null;
	   }
	    String mc=market+code;
        if (stockMinuteKBean_cache.getEntity(mc)==null){
            StockMinuteKBean b=new StockMinuteKBean();
            b.setMarket(market);
            b.setCode(code);
            stockMinuteKBean_cache.putEntity(mc,b);
        }
        Map<String, Object> p=new HashMap<String, Object>(); 
        p.put("code", code);
        p.put("market", market);
        this.stockMinuteKBean_cache.forceReload(p,false);
        return stockMinuteKBean_cache.getEntity(mc);
	}
	//K线
	@Override
	public LineListBean getDayline(String code, String market) {
		return null;
	}
	
	//行情信息
    private GenericReloadableEntityCache<String,StockQuotationBean,List> stockQuotationBean_cache;
    //分钟线信息
    private GenericReloadableEntityCache<String,StockMinuteKBean,List> stockMinuteKBean_cache;
	//查询股票行情
    private GenericReloadableEntityCache<String,StockTaxisVO,StockTaxisVO> stockTaxisVO_cache;
    
	@Override
    public StockQuotationBean getStockQuotation(String code, String market) {
	    String mc=market+code;
	    if (stockQuotationBean_cache.getEntity(market+code)==null){
	        StockQuotationBean b=new StockQuotationBean();
            stockQuotationBean_cache.putEntity(mc,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("code", code);
        params.put("market", market);

        this.stockQuotationBean_cache.forceReload(params,false);
        return stockQuotationBean_cache.getEntity(mc);
       
    }
//=====================beans =====================
	private StockTaxisListBean stockList = new StockTaxisListBean();
	private QuotationListBean quotationListBean = new QuotationListBean();
	@Override
	public BindableListWrapper<StockTaxisVO> getStocktaxis(final String taxis, final String orderby,
			long start, long limit) {
		if(stockTaxisVO_cache == null){
			this.stockTaxisVO_cache = new GenericReloadableEntityCache<String, StockTaxisVO, StockTaxisVO>("StockTaxisVO");
		}
		BindableListWrapper<StockTaxisVO> result = this.stockTaxisVO_cache.getEntities(null, new Comparator<StockTaxisVO>() {
			
			@Override
			public int compare(StockTaxisVO o1, StockTaxisVO o2) {
				Long v1 = null;
				Long v2 = null;
				if("newprice".equals(taxis)){
					v1 = o1.getNewprice();
					v2 = o2.getNewprice();
				}else if("risefallrate".equals(taxis)){
					v1 = o1.getRisefallrate();
					v2 = o2.getRisefallrate();
				}
				if("desc".equals(orderby)){
					return v1.compareTo(v2);
				}else{
					return v2.compareTo(v1);
				}
			}
		});
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taxis",taxis);
		params.put("orderby", orderby);
		params.put("start", (long)start);
		params.put("limit", (long)limit);
		this.stockTaxisVO_cache.doReloadIfNeccessay(params);
		return result;
	}

	@Override
	public QuotationListBean getQuotations() {
		Future<QuotationListVO> future = context.getExecutor().submit(new Callable<QuotationListVO>() {
			public QuotationListVO call() throws Exception {
				QuotationListVO vo = getRestService(StockResource.class).getQuotation(null);
				return vo;
			}
		});
		try {
			QuotationListVO volist = future.get();
		}catch (Exception e) {
			log.warn("Failed to fetch quotation",e);
		}
		return quotationListBean;
	}
	
	

	@Override
	public BindableListWrapper<List<StockMinuteKBean>> getFiveDayMinuteline(
			String code, String market) {
		
		return null;
	}

}
