package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userViewMorePage", withToolbar = true, description = "我的成功操作")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_view_more_layout")
public abstract class UserViewMorePage extends PageBase implements
		IModelUpdater {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	
	@Bean(type=BindingType.Pojo,express="${usrService.getMorePersonalRecords(0,15,false)}")
	BindableListWrapper<GainBean> myChallengeListBean;

	@Bean(type=BindingType.Pojo,express="${usrService.getMorePersonalRecords(0,15,true)}")
	BindableListWrapper<GainBean> myJoinListBean;
	
	
	@Bean(type=BindingType.Pojo,express="${usrService.getMoreOtherPersonal(userId,0,15,false)}")
	BindableListWrapper<GainBean> otherChallengeListBean;

	@Bean(type=BindingType.Pojo,express="${usrService.getMoreOtherPersonal(userId,0,15,true)}")
	BindableListWrapper<GainBean> otherJoinListBean;
	

	@Field(valueKey = "options", binding = "${userId!=null?(otherChallengeListBean!=null?otherChallengeListBean.data:null):(myChallengeListBean!=null?myChallengeListBean.data:null)}", 
				visibleWhen="${curItemId == 0}")
	List<GainBean> actualRecordList;

	@Field(valueKey = "options", binding = "${userId!=null?(otherJoinListBean!=null?otherJoinListBean.data:null):(myJoinListBean!=null?myJoinListBean.data:null)}",
			visibleWhen="${curItemId == 1}"
			)
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 0}"),
			@Attribute(name = "textColor", value = "${curItemId == 0?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean actualRecordBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean virtualRecordBtn;

	

	/** 其它用户ID */
	@Bean
	String userId = null;

	/** 用户--参赛交易盘每页初始条目 */
	@Bean
	int myHomeVLimit = 15;

	/** 用户--挑战交易盘每页初始条目 */
	@Bean
	int myHomeALimit = 15;

	/** 其它用户--挑战交易盘每页初始条目 */
	@Bean
	int otherHomeALimit = 15;

	/** 其它用户--参赛交易盘每页初始条目 */
	@Bean
	int otherHomeVLimit = 15;

	@SuppressWarnings("unused")
	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	/**
	 * 挑战交易盘交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showActualRecords(InputEvent event) {
		registerBean("curItemId", 0);
		if (StringUtils.isBlank(userId)) {
			// 用户自己的挑战交易记录
			if (usrService != null) {
				usrService.getMorePersonalRecords(0, 15, false);
			}
		} else {
			if (usrService != null) {
				usrService.getMoreOtherPersonal(userId, 0, 15, false);
			}
		}
		return null;
	}

	/**
	 * 参赛交易盘操作记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showVirtualRecords(InputEvent event) {
		registerBean("curItemId", 1);
		if (StringUtils.isBlank(userId)) {
			// 用户自己的参赛交易记录
			if (usrService != null) {
				usrService.getMorePersonalRecords(0, 15, true);
			}
		} else {
			if (usrService != null) {
				usrService.getMoreOtherPersonal(userId, 0, 15, true);
			}
		}
		return null;
	}

	@Command(commandName = "virtualRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "otherUserSellOutPage"),
			@Navigation(on="BuyIn",showPage="otherUserBuyInPage")
	})
	CommandResult virtualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean virtualBean = null;
			if (StringUtils.isBlank(userId)) {
				//本人
				if(myJoinListBean!=null) {
					List<GainBean> virtualList = myJoinListBean.getData();
					if (virtualList!=null && virtualList.size()>0) {
						virtualBean = virtualList.get(position);
					}
				}
			} else {
				if(otherJoinListBean!=null) {
					List<GainBean> otherVirtualList = otherJoinListBean.getData();
					if (otherVirtualList!=null && otherVirtualList.size()>0) {
						virtualBean = otherVirtualList.get(position);
					}
				}
			}
			
			CommandResult result = null;
			if (virtualBean != null) {
				/** 交易盘ID */
				Long accId = virtualBean.getTradingAccountId();
				String tradeStatus = virtualBean.getOver();
				Boolean isVirtual = virtualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accId", accId);
				map.put("isVirtual", isVirtual);
				map.put("userId", userId);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = virtualBean.getStatus();
					if (status == 0) {
						//进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						//进入买入界面
						result.setResult("BuyIn");
					}
				}
			}
			return result;
		}
		return null;
	}

	@Command(commandName = "actualRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "otherUserSellOutPage"),
			@Navigation(on="BuyIn",showPage="otherUserBuyInPage")
	})
	CommandResult actualRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean actualBean = null;
			if (StringUtils.isBlank(userId)) {
				//本人
				if(myChallengeListBean!=null) {
					List<GainBean> actualList = myChallengeListBean.getData();
					if (actualList!=null && actualList.size()>0) {
						actualBean = actualList.get(position);
					}
				}
			} else {
				if(otherChallengeListBean!=null) {
					List<GainBean> otherActualList = otherChallengeListBean.getData();
					if (otherActualList!=null && otherActualList.size()>0) {
						actualBean = otherActualList.get(position);
					}
				}
			}
			
			CommandResult result = null;
			if (actualBean != null) {
				/** 交易盘ID */
				Long accId = actualBean.getTradingAccountId();
				String tradeStatus = actualBean.getOver();
				Boolean isVirtual = actualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accId", accId);
				map.put("isVirtual", isVirtual);
				map.put("userId", userId);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = actualBean.getStatus();
					if (status == 0) {
						//进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						//进入买入界面
						result.setResult("BuyIn");
					}
				}
			}
			return result;
		}
		return null;
	}

	@Command
	String handleActualTopRefresh(InputEvent event) {
		showActualRecords(null);
		return null;
	}

	@Command
	String handleActualBottomRefresh(InputEvent event) {
		showActualRecords(null);
		return null;
	}
	
	

	@Command
	String handleVirtualBottomRefresh(InputEvent event) {
		showVirtualRecords(null);
		return null;
	}

	@Command
	String handleVirtualTopRefresh(InputEvent event) {
		showVirtualRecords(null);
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		Map<String, Object> map = (Map<String, Object>) value;

		String isVirtual = (String) map.get("isVirtual");
		if (isVirtual.equals("1")) {
			registerBean("curItemId", 1);
		} else if (isVirtual.equals("0")) {
			registerBean("curItemId", 0);
		}
		
		String uId = (String) map.get("userId");
		if (!StringUtils.isBlank(uId)) {
			this.userId = uId;
			registerBean("userId", uId);
		} else {
			userId = null;
			registerBean("userId", null);
		}
	}

	@OnUIDestroy
	protected void clearData() {
		this.userId = null;
	}
}
