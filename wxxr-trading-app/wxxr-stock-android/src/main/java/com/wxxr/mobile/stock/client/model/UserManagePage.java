package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="userManagePage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_manage_layout")
public abstract class UserManagePage extends PageBase {

	
	
	
	
	@Command(
			commandName = "modifyPsw", 
			description = "To Modify Password UI", 
			navigations = { 
					@Navigation(
							on = "OK", 
							showPage = "userAlterPswPage"
							) 
					}
			)
	String modifyPsw(InputEvent event) {
		return "OK";
	}
	
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}

}
