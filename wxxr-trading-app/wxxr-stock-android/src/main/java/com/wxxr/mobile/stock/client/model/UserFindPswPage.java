package com.wxxr.mobile.stock.client.model;

import android.os.SystemClock;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserFindPswCallBack;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;

@View(name = "userFindPswPage", withToolbar = true, description = "找回密码")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.psw_find_back_layout")
public abstract class UserFindPswPage extends PageBase {

	@Field(valueKey = "text", binding = "${callBack.phoneNum}")
	String mobileNum;

	@Bean(type = BindingType.Service)
	IUserLoginManagementService userService;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@Bean
	UserFindPswCallBack callBack = new UserFindPswCallBack();

	/**
	 * 发送密码到手机
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendMsg", navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }) })
	@NetworkConstraint
	@ExeGuard(title = "重置密码", message = "正在处理，请稍候...", silentPeriod = 200)
	String sendMsg(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			SystemClock.sleep(500);
			if (userService != null) {
				userService.resetPassword(callBack.getPhoneNum());
			}
			hide();
		}
		return null;
	}

}
