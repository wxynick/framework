/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author dz
 * 
 */
public class ViewPagerIndexGroupFieldBinder implements IFieldBinder {

	private IWorkbenchRTContext mngContext;

	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.mngContext = mngCtx;
	}

	@Override
	public void destory() {
		this.mngContext = null;
	}

	@Override
	public IFieldBinding createBinding(IBindingContext context,
			String fieldName, Map<String, String> attrs) {
		return new ViewPagerIndexGroupFieldBinding(
				(IAndroidBindingContext) context, fieldName, attrs);
	}

}
