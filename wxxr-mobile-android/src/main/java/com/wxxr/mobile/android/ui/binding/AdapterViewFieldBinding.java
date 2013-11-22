/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 * 
 */
public class AdapterViewFieldBinding extends BasicFieldBinding {
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String LIST_FOOTER_VIEW_ID = "footerViewId";
	public static final String LIST_HEADER_VIEW_ID = "headerViewId";
	private GenericListAdapter listAdapter;
	private IListDataProvider provider;
	private IViewBinding headerBinding;
	private IViewBinding footerBinding;
	private IView headerView,footerView;
	private View headItemView, footerItemView;
	
	public AdapterViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		String footerViewId = getBindingAttrs().get(LIST_FOOTER_VIEW_ID);
		String headerViewId = getBindingAttrs().get(LIST_HEADER_VIEW_ID);
		ListView list = getUIControl() instanceof ListView ? (ListView)getUIControl() : null;
		if(list != null){
			if (headerViewId != null
					&& list.getHeaderViewsCount() == 0) {
				IViewDescriptor v = ctx.getWorkbenchManager().getViewDescriptor(headerViewId);
				View view = createUI(v,ctx.getWorkbenchManager());
				headerBinding = (IViewBinding) view.getTag();
				list.addHeaderView(view);
				headItemView = view;
				headerView = ctx.getWorkbenchManager().getWorkbench().createNInitializedView(headerBinding.getBindingViewId());
			}
			if (footerViewId != null
					&& list.getFooterViewsCount() == 0) {
				IViewDescriptor v = ctx.getWorkbenchManager().getViewDescriptor(footerViewId);
				View view = createUI(v,ctx.getWorkbenchManager());
				footerBinding = (IViewBinding) view.getTag();
				list.addFooterView(view);
				footerItemView = view;
				footerView = ctx.getWorkbenchManager().getWorkbench().createNInitializedView(footerBinding.getBindingViewId());
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr
	 * .mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void activate(IView model) {
		super.activate(model);
		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		provider = comp.getAdaptor(IListDataProvider.class);
		if (provider == null) {
			provider = createAdaptorFromValue(comp);
		}
		this.listAdapter = new GenericListAdapter(getWorkbenchContext(),
				getAndroidBindingContext(), provider,
				itemViewId);
		if((headerBinding != null)&&(headerView != null)){
			model.addChild(headerView);
			headerBinding.activate(headerView);
		}
		if((footerBinding != null)&&(footerView != null)){
			model.addChild(footerView);
			footerBinding.activate(footerView);
		}
		this.provider.updateDataIfNeccessary();
		setupAdapter(listAdapter);
	}

	protected View createUI(IViewDescriptor v,final IWorkbenchManager mgr) {
		IBindingDescriptor bDesc = v
				.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = mgr.getViewBinder();
		binding = vBinder.createBinding(new IAndroidBindingContext() {

			@Override
			public Context getUIContext() {
				return getAndroidBindingContext().getUIContext();
			}

			@Override
			public View getBindingControl() {
				return null;
			}

			@Override
			public IWorkbenchManager getWorkbenchManager() {
				return mgr;
			}

			@Override
			public boolean isOnShow() {
				return getAndroidBindingContext().isOnShow();
			}
		}, bDesc);
		View view = (View) binding.getUIControl();
		view.setTag(binding);
		return view;

	}

	protected void setupAdapter(ListAdapter adapter) {
		((AbsListView) getUIControl()).setAdapter(adapter);
	}
	
	protected Object[] getListData(IUIComponent comp){
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			return result != null ? result.toArray() : null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				return ((List<Object>)val).toArray();
			}else if((val != null)&&val.getClass().isArray()){
				return (Object[])val;
			}
		}
		return null;
	}

	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IListDataProvider createAdaptorFromValue(final IUIComponent comp) {
		return new IListDataProvider() {
			Object[]  data = null;
			@Override
			public Object getItemId(Object item) {
				return null;
			}

			@Override
			public int getItemCounts() {
				return data != null ? data.length : 0;
			}

			@Override
			public Object getItem(int i) {
				return data[i];
			}

			@Override
			public boolean isItemEnabled(Object item) {
				return true;
			}

			@Override
			public boolean updateDataIfNeccessary() {
				data = getListData(comp);
				return true;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if (this.listAdapter != null) {
			setupAdapter(null);
			this.listAdapter.destroy();
			this.listAdapter = null;
		}
		super.deactivate();
		if(headerBinding != null) {
			headerBinding.deactivate();
			if(headerView.getParent() != null){
				headerView.getParent().removeChild(headerView);
			}
		}
		if(footerBinding != null) {
			footerBinding.deactivate();
			if(footerView.getParent() != null){
				footerView.getParent().removeChild(footerView);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.ValueChangedEvent[])
	 */
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.listAdapter != null){
			this.listAdapter.notifyDataSetChanged();
		}
		super.notifyDataChanged(events);
		if(headerBinding != null) {
			headerBinding.notifyDataChanged(events);
		}
		if(footerBinding != null) {
			footerBinding.notifyDataChanged(events);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#refresh()
	 */
	@Override
	public void refresh() {
		if(this.listAdapter != null){
			this.listAdapter.notifyDataSetChanged();
		}
		super.refresh();
		if(headerBinding != null) {
			headerBinding.refresh();
		}
		if(footerBinding != null) {
			footerBinding.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#handleValueChangedCallback(com.wxxr.mobile.core.ui.common.UIComponent, com.wxxr.mobile.core.ui.api.AttributeKey<?>[])
	 */
	@Override
	protected void updateUI(boolean recursive) {
		if((this.provider != null)&&this.provider.updateDataIfNeccessary()&&(this.listAdapter != null)){
			this.listAdapter.notifyDataSetChanged();
		}
		super.updateUI(recursive);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#destroy()
	 */
	@Override
	public void destroy() {
		ListView list = getUIControl() instanceof ListView ? (ListView)getUIControl() : null;
		if((list != null)&&(headItemView != null)){
			list.removeHeaderView(headItemView);
			headItemView = null;
		}
		if(headerBinding != null){
			headerBinding.destroy();
			headerBinding = null;
		}
		if(headerView != null){
			headerView.destroy();
			headerView = null;
		}
		if((list != null)&&(footerItemView != null)){
			list.removeFooterView(footerItemView);
			footerItemView = null;
		}
		if(footerBinding != null){
			footerBinding.destroy();
			footerBinding = null;
		}
		if(footerView != null){
			footerView.destroy();
			footerView = null;
		}
		super.destroy();
	}

}
