/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * @author neillin
 * 
 */
@View(name = "infoCenter", description = "行情中心",provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.price_center_page_layout")
public abstract class InfoCenterView extends ViewBase {
	
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStocktaxis(orderBy,direction,0,20)}")
	BindableListWrapper<StockTaxisBean> stockTaxis;
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('000001','SH')}")
	StockQuotationBean shBean;
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('399001','SZ')}")
	StockQuotationBean szBean;
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('399005','SZ')}")
	StockQuotationBean zxBean;
	
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000f"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000f"),
			@Parameter(name="format",value="%+.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor1;
	
	@Convertor(params={
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	/**-------上证指数 上海*/

	//箭头
	@Field(valueKey="text",enableWhen="${(shBean!=null && shBean.newprice > shBean.close)?true:false}",visibleWhen="${(shBean!=null && shBean.newprice != shBean.close)?true:false}")
	String shType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${shBean!=null?shBean.risefallrate:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sh_risefallrate;
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${shBean!=null?shBean.market:null}")
	String sh_market;
	// 最新
	@Field(valueKey="text",binding="${shBean!=null?shBean.newprice:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sh_newprice;
	// 涨跌额
	@Field(valueKey="text",binding="${shBean!=null?shBean.change:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String sh_change;
	
	/**-----------深圳成指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(szBean!=null && szBean.newprice > szBean.close)?true:false}",visibleWhen="${(szBean!=null && szBean.newprice != szBean.close)?true:false}",upateAsync=true)
		String szType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${szBean!=null?szBean.risefallrate:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sz_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${szBean!=null?szBean.market:null}")
	String sz_market;
	
	// 最新
	@Field(valueKey="text",binding="${szBean!=null?szBean.newprice:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sz_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${szBean!=null?szBean.change:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String sz_change;
	
	// 股票列表
	@Field(valueKey = "options",binding="${stockTaxis.data}",upateAsync=true)
	List<StockTaxisBean> stockInfos;
	
	/**-----------深圳中小版指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(zxBean!=null && zxBean.newprice > zxBean.close)?true:false}",visibleWhen="${(zxBean!=null && zxBean.newprice != zxBean.close)?true:false}",upateAsync=true)
		String zxType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.risefallrate:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String zx_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.market:null}")
	String zx_market;
	
	// 最新
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.newprice:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String zx_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.change:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String zx_change;
	

	@Bean
	String orderBy = "risefallrate";
	
	@Bean
	String direction = "desc";
	
	@Field(valueKey="text",enableWhen="${direction == 'asc'}",visibleWhen="${orderBy == 'newprice'}")
	String isNewPrice;
	
	@Field(valueKey="text",enableWhen="${direction == 'asc'}",visibleWhen="${orderBy == 'risefallrate'}")
	String isRisefallrate;

	@Menu(items={"right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="right",label="搜索",icon="resourceId:drawable/find_button_style")
			},navigations = { @Navigation(on = "*", showPage = "GeGuStockPage")}
	)
	String toolbarClickedLeft(InputEvent event) {
		return "";
	}
	
	/**
	 * 事件处理-单击深证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SZ_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleSZClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(szBean!=null){
				if(szBean.getCode()!=null && szBean.getMarket()!=null){
					temp.put("code", szBean.getCode());
					temp.put("market", szBean.getMarket());
					temp.put("name", "深圳成指");
					updateSelection(new StockSelection(szBean.getMarket(),szBean.getCode(),"深圳成指"));
					result.setPayload(temp);
					result.setResult("SZ_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}
	/**
	 * 事件处理 -单击上证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SH_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleSHClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(shBean!=null){
				if(shBean.getCode()!=null && shBean.getMarket()!=null){
					temp.put("code", shBean.getCode());
					temp.put("market", shBean.getMarket());
					temp.put("name", "上证指数");
					updateSelection(new StockSelection(shBean.getMarket(),shBean.getCode(),"上证指数"));
					result.setPayload(temp);
					result.setResult("SH_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * 事件处理 -单击中小版指 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "ZX_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleZXClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(zxBean!=null){
				if(zxBean.getCode()!=null && zxBean.getMarket()!=null){
					temp.put("code", zxBean.getCode());
					temp.put("market", zxBean.getMarket());
					temp.put("name", "中小版指");
					updateSelection(new StockSelection(zxBean.getMarket(),zxBean.getCode(),"中小版指"));
					result.setPayload(temp);
					result.setResult("ZX_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}	
	/**
	 * 事件处理- 单击涨跌幅标题（股票列表排序-按涨跌幅）
	 * 
	 * */
	@Command
	@ExeGuard(title="加载数据",message="正在从服务器端查询，下载数据，请稍候...",silentPeriod=500)
	String orderByRisefallrate(InputEvent event){
		if("risefallrate".equals(this.orderBy)){
			this.direction = "desc".equals(this.direction) ? "asc" : "desc";
		}else{
			this.orderBy = "risefallrate";
			this.direction = "desc";
		}
		registerBean("orderBy", this.orderBy);
		registerBean("direction", this.direction);
		this.infoCenterService.reloadStocktaxis(this.orderBy, this.direction, 0, 20);
		return null;
	}
	/**
	 * 事件处理- 单击当前价标题（股票列表排序-按当前价）
	 * 
	 * */
	@Command
	@ExeGuard(title="加载数据",message="正在从服务器端查询，下载数据，请稍候...",silentPeriod=500)
	String orderByNewPrice(InputEvent event){
		if("newprice".equals(this.orderBy)){
			this.direction = "desc".equals(this.direction) ? "asc" : "desc";
		}else{
			this.orderBy = "newprice";
			this.direction = "desc";
		}
		registerBean("orderBy", this.orderBy);
		registerBean("direction", this.direction);
		this.infoCenterService.reloadStocktaxis(this.orderBy, this.direction, 0, 20);
		return null;
	}
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "true")})
	String acctRefreshView;
	
	@Command
	String handleRefresh(InputEvent event){
		if(event.getEventType().equals("TopRefresh")){
			this.infoCenterService.getStockQuotation("000001","SH");
			this.infoCenterService.getStockQuotation("399001","SZ");
			this.infoCenterService.reloadStocktaxis(this.orderBy, this.direction, 0, 20);
		}else if(event.getEventType().equals("BottomRefresh")){
			if(stockTaxis!=null && this.stockTaxis.getData()!=null){
				this.infoCenterService.reloadStocktaxis(this.orderBy, this.direction, this.stockTaxis.getData().size(), 20);
			} 
		}
		return null;
	}
	
//	@Command
//	String handleBottomRefresh(InputEvent event){
//		this.infoCenterService.reloadStocktaxis(this.orderBy, this.direction, this.stockTaxis.getData().size(), 10);
//		return null;
//	}
	
	/**
	 * 点击列表跳转个股界面
	 * */
	@Command(navigations={
			@Navigation(on = "GeGuStockPage",showPage="GeGuStockPage")
	})
	CommandResult gotoPageItemClick(InputEvent event){
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			int position = (Integer) event.getProperty("position");
			CommandResult result = new CommandResult();
			if(stockTaxis!=null){
				List<StockTaxisBean> taxis = stockTaxis.getData();
				if(taxis!=null && taxis.size()>0){
					StockTaxisBean stockTaxis = taxis.get(position);
					String code = stockTaxis.getCode();
					String name = stockTaxis.getName();
					String market = stockTaxis.getMarket();
					if(code!=null && market!=null){
						map.put("code", code);
						map.put("name", name);
						map.put("market", market);
						result.setPayload(map);
						updateSelection(new StockSelection(market,code,name));
						result.setResult("GeGuStockPage");
						return result;
					}
				}
			}
		}
		return null;
	}
}
