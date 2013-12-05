package com.wxxr.stock.restful.resource;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.BaseInfoListVO;
import com.wxxr.stock.restful.json.ComponentstocksListVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.json.PlateTaxisListVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;


public class StockResourceTest  extends TestCase{

	StockResource tradingResource;
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
		MockApplication app = new MockApplication(){
			ExecutorService executor = Executors.newFixedThreadPool(3);

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.rpc.impl.MockApplication#getExecutor()
			 */
			@Override
			public ExecutorService getExecutorService() {
				return executor;
			}
			
			@Override
			protected void initModules() {
				
			}

		};
		IKernelContext context = app.getContext();
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				return new IUserAuthCredential() {
					
					@Override
					public String getUserName() {
						return "13500001009";
					}
					
					@Override
					public String getAuthPassword() {
						return "404662";
					}

				};
			}
		});
		service.startup(context);
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
			@Override
			public KeyStore getTrustKeyStore() {
				// TODO Auto-generated method stub
				return null;
			}
						
			@Override
			public KeyStore getSiteKeyStore() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HostnameVerifier getHostnameVerifier() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		MockRestClient builder = new MockRestClient();
		builder.init(context);
		tradingResource=builder.getRestService(StockResource.class,"http://192.168.123.44:8480/mobilestock2");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	public void testGetAllStockInfo() throws Exception{
		BaseInfoListVO a = tradingResource.getAllStockInfo();
	}
	
	public void testGetQuotation()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		QuotationListVO a = tradingResource.getQuotation(list);
	}
	
	public void testGetQuotation1()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		QuotationListVO a = tradingResource.getQuotation("SH","600000");
		System.out.println(a.getList().get(0));
	}

	public void testGetMinuteline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		StockMinuteKVO a = tradingResource.getMinuteline(p);
		System.out.println(a);
	}
	   
	public void testGetDayline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = tradingResource.getDayline(p);
	}
	
	public void testGetFiveDayMinuteline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		List<StockMinuteKVO> a = tradingResource.getFiveDayMinuteline(p);
	}
	
	public void testGetStockHQListByBlockId()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47513L);
		p.setTaxis("risefallrate");
		List<StockQuotationVO> a = tradingResource.getStockHQListByBlockId(p);
	}
	
	public void testGetWeekline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = tradingResource.getWeekline(p);
	}
	public void testGetMonthline()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		LineListVO a = tradingResource.getMonthline(p);
	}
	public void testGetStocktaxis()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		StockTaxisListVO a = tradingResource.getStocktaxis(p);
	}
	public void testGetPlatetaxis()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		PlateTaxisListVO a = tradingResource.getPlatetaxis(p);
	}
	public void testGetRefenceBlockHQ()throws Exception{
		List<ParamVO> list = new ArrayList<ParamVO>();
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		list.add(p);
		PlateTaxisListVO a = tradingResource.getRefenceBlockHQ(list);
	}


    //public ComponentstocksListVO getComponentstocks(ParamVO paramVO) throws Exception ;
    public void testGetComponentstocks()throws Exception{
		ParamVO p = new ParamVO();
		p.setMarket("SH");
		p.setCode("600000");
		p.setStart(0L);
		p.setLimit(10L);
		ComponentstocksListVO a = tradingResource.getComponentstocks(p);
	}

    //public StockTaxisListVO getIndexPreview(TaxisVO vo) throws Exception;
    public void testGetIndexPreview()throws Exception{
		TaxisVO p = new TaxisVO();
		p.setOrderby("desc");
		p.setStart(0L);
		p.setLimit(10L);
		p.setBlockId(47503L);
		p.setTaxis("risefallrate");
		StockTaxisListVO a = tradingResource.getIndexPreview(p);
	}
}
