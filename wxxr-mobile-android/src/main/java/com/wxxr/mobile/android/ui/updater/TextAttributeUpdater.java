/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class TextAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(TextAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof TextView)){
			return;
		}
		TextView tv = (TextView)control;
		String val = (String)value;
		if((val != null)&&(attrType == AttributeKeys.text)){
			try {
				if(RUtils.isResourceIdURI(val)){
					tv.setText(RUtils.getInstance().getResourceIdByURI(val));
				}else{
					int selection = val.length();
					selection = tv.getSelectionStart();
					tv.setText(val);
					if(control instanceof EditText) {
						((EditText)tv).setSelection(selection);
					}
				}
			} catch (Exception e) {
				log.error("Failed to set image for field :"+field.getName(), e);
			}
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		TextView tv = (TextView)control;
		String val = tv.getText().toString();
		IWritable writer = field.getAdaptor(IWritable.class);
		if(writer != null){
			writer.setValue(val);
		}
		return attrType.getValueType().cast(val);
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof TextView;
	}

}
