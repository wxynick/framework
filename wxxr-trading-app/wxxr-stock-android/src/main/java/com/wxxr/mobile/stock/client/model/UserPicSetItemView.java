package com.wxxr.mobile.stock.client.model;

import android.R.bool;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.android.ui.binding.GenericListAdapter;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserPicBean;
import com.wxxr.mobile.stock.client.biz.UserIcon;

@View(name="imageItemForPic")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_pic_set_item_layout")
public abstract class UserPicSetItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="imageURI")
	String userIcon;
	
	@Field(valueKey="visible")
	boolean picChecked;
	
	DataField<String> userIconField;
	DataField<Boolean> picCheckedField;
	
	

	@Override
	public void updateModel(Object value) {
		
		if (value instanceof UserIcon) {
			UserIcon userPic = (UserIcon) value;
			this.userIcon = userPic.getImageURI();
			this.userIconField.setValue(userPic.getImageURI());
			
			this.picChecked = userPic.isPicChecked();
			this.picCheckedField.setValue(userPic.isPicChecked());
			
		}
	}
}
