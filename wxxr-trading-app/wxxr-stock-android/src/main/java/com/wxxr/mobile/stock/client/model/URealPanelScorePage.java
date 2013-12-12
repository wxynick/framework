package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name="uRealPanelScorePage", withToolbar=true, description="实盘积分明细")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.actual_panel_integral_detail_layout")
public abstract class URealPanelScorePage extends PageBase{

	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getVoucherDetails(0,10)}")
	VoucherDetailsBean voucherDetailsBean;
	
	@Field(valueKey="options", binding="${voucherDetailsBean!=null?voucherDetailsBean.list:null}",attributes = {@Attribute(name = "enablePullDownRefresh", value="true"),
			  @Attribute(name = "enablePullUpRefresh", value="false")})
	List<GainPayDetailBean> actualScores;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command
	String refreshTopData(InputEvent event) {
		return null;
	}
}
