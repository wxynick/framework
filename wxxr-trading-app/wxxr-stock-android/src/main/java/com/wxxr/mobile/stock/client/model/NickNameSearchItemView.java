package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="NickNameSearchItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.nick_name_search_layout_item")
public abstract class NickNameSearchItemView extends ViewBase implements IModelUpdater {

	
	@Field(valueKey="text")
	String nickName;
	
	@Override
	public void updateModel(Object value) {
	
	}
}
