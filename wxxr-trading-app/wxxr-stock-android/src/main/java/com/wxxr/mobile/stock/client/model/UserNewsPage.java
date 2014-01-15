package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "userNewsPage", withToolbar = true, description = "消息", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_news_center_page_layout")
public abstract class UserNewsPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${usrService.remindMessageBean}")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.getPullMessageBean(start,limit)}")
	BindableListWrapper<PullMessageBean> infoNoticeListBean;

	/** 账户&交易-数据 */
	@Field(valueKey = "options", upateAsync=true,binding = "${accountTradeListBean!=null?accountTradeListBean.getData(true):null}", visibleWhen = "${(curItemId==0)&&(accountTradeListBean.data!=null?(accountTradeListBean.data.size()>0?true:false):false)}")
	List<RemindMessageBean> accountTradeInfos;

	/** 当账户&交易数据为空时显示 */
	@Field(valueKey = "visible", binding = "${false}")
	boolean newsAccountNullVisible;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}") })
	boolean accountTrades;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}") })
	boolean infoNotices;

	/** 资讯&公告-数据 */
	@Field(valueKey = "options", binding = "${infoNoticeListBean!=null?infoNoticeListBean.getData(true):null}", upateAsync=true,visibleWhen = "${(curItemId==1)&&(infoNoticeListBean.data!=null?(infoNoticeListBean.data.size()>0?true:false):false)}")
	List<PullMessageBean> noticeInfos;

	@Field(valueKey = "visible", binding = "${false}")
	boolean noticeInfosNullVisible;

	@Bean
	int curItemId = 0;

	@Bean
	int start = 0;

	@Bean
	int limit = 20;
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(valueKey = "text", visibleWhen = "${curItemId == 1}", attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${infoNoticeListBean!=null&&infoNoticeListBean.data!=null&&infoNoticeListBean.data.size()>0?true:false}") })
	String noticeRefreshView;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initData() {
		usrService.readAllUnremindMessage();
	}

	@Command
	String showInfoNotices(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);

		if (usrService != null) {
			usrService.getPullMessageBean(0, infoNoticeListBean.getData()
					.size());
		}
		return null;
	}

	@Command
	String showAccountTrades(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", curItemId);
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			// accountTradeListBean.getData();
			if (usrService != null) {
				usrService.getRemindMessageBean();
			}
		}
		return null;
	}

	@Command
	String handleNoticeTopRefresh(InputEvent event) {
		if (event.getEventType().equals("TopRefresh")) {

			showInfoNotices(event);
		} else if (event.getEventType().equals("BottomRefresh")) {
			int completeLoadSize = 0;
			if (infoNoticeListBean != null)
				completeLoadSize += infoNoticeListBean.getData().size();
			start = completeLoadSize;
			if (usrService != null) {
				usrService.getPullMessageBean(start, limit);
			}
		}
		return null;
	}

	@Command
	String handleNoticeBottomRefresh(InputEvent event) {
		int completeLoadSize = 0;
		if (infoNoticeListBean != null)
			completeLoadSize += infoNoticeListBean.getData().size();
		start += completeLoadSize;
		if (usrService != null) {
			usrService.getPullMessageBean(start, limit);
		}
		return null;
	}

	@Command(navigations = { @Navigation(on = "*", showPage = "webPage") })
	CommandResult handleNoticesItemClick(InputEvent event) {

		int position = (Integer) event.getProperty("position");
		PullMessageBean message = null;
		if (infoNoticeListBean != null) {
			List<PullMessageBean> infoNoticesList = infoNoticeListBean
					.getData();
			message = infoNoticesList.get(position);

			if (usrService != null && message != null) {
				usrService.readPullMesage(message.getId());
			}
			CommandResult result = new CommandResult();
			result.setPayload(message.getArticleUrl());
			result.setResult("*");
			return result;
		}

		return null;
	}

	@Command(navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult handleNewsItemClick(InputEvent event) {
		int position = (Integer) event.getProperty("position");
		RemindMessageBean message = null;

		if (accountTradeListBean != null) {
			List<RemindMessageBean> remindMessageList = accountTradeListBean.getData();
			message =remindMessageList.get(position);
		}
		String accId = null;
		if(message != null)
			accId = message.getAttrs().get("acctID");
		TradingAccountBean accountBean = null;
		if (tradingService != null) {
			accountBean = tradingService.getTradingAccountInfo(accId);
		}

		CommandResult result = null;
		if (accountBean != null) {

			boolean isVirtual = accountBean.getVirtual();
			String tradeStatus = accountBean.getOver();

			result = new CommandResult();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ACCOUNT_ID_FLAG, accId);
			map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
			map.put("isSelf", true);
			result.setPayload(map);
			if ("CLOSED".equals(tradeStatus)) {
				// result.setPayload(map);
				result.setResult("operationDetails");
				// updateSelection(new AccidSelection(accId, isVirtual));
			}

			if ("UNCLOSE".equals(tradeStatus)) {

				int accountStatus = accountBean.getStatus();
				if (accountStatus == 0) {
					result.setResult("SellOut");
				} else if (accountStatus == 1) {
					result.setResult("BuyIn");
				}
				// result.setPayload(map);
			}
			updateSelection(new AccidSelection(String.valueOf(accId), isVirtual));
			return result;
		}
		return null;
	}
}
