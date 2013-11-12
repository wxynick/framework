/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;

/**
 * @author neillin
 *
 */
public class AndroidPageNavigator implements IAndroidPageNavigator {
	private static final Trace log = Trace.register(AndroidPageNavigator.class);
	
	private static final int PAGE_ACTIVITY_CREATE = 1;
	private static final int PAGE_ACTIVITY_SHOW = 2;
	private static final int PAGE_ACTIVITY_HIDE = 3;
	private static final int PAGE_ACTIVITY_DESTROY = 4;
	
	private Map<String, IBindableActivity> activeActivities = new HashMap<String, IBindableActivity>();
	private Map<String, List<IPageCallback>> callbacks = new HashMap<String, List<IPageCallback>>();
	private String currentPageId;
	private final IWorkbenchRTContext context;
	
	public AndroidPageNavigator(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	
	protected Activity getCurrentActivity(){
		if(currentPageId != null){
			IBindableActivity activity = this.activeActivities.get(currentPageId);
			if(activity != null){
				return activity.getActivity();
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#showPage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void showPage(IPage page,Map<String, Object> params,IPageCallback cb) {
		Application app = AppUtils.getFramework().getAndroidApplication();
		Activity activity = getCurrentActivity();
		String pageId = page.getName();
		Class<?> activityClass = getActivityClass(pageId);
		IModelUpdater updater = page.getAdaptor(IModelUpdater.class);
		if(updater != null){
			updater.updateModel(params);
		}
		Intent intent;
		if(activity != null) {
			intent = new Intent(activity, activityClass);
		}else{
			intent = new Intent(app, activityClass);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		initShowPageIntent(page,intent);
		if(params != null){
			for (Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				Object val = entry.getValue();
				if(PARAM_KEY_INTENT_FLAG.equals(key)){
					intent.addFlags(Integer.parseInt((String)val));
				}else{
					add2Intent(intent,key,val);
				}
			}
		}
		if(cb != null){
			List<IPageCallback> cbs = this.callbacks.get(pageId);
			if(cbs == null){
				cbs = new LinkedList<IPageCallback>();
				this.callbacks.put(pageId, cbs);
			}
			if(!cbs.contains(cb)){
				cbs.add(cb);
			}
		}
		if(activity != null) {
			activity.startActivity(intent);
		} else {
			app.startActivity(intent);
		}
	}
	
	protected void add2Intent(Intent intent, String key, Object val){
		if(val.getClass().isArray()){
			Class<?> componentType = val.getClass().getComponentType();
			if(componentType == Byte.TYPE){
				intent.putExtra(key, (byte[])val);
			}else if(componentType == Character.TYPE){
				intent.putExtra(key, (char[])val);
			}else if(componentType == Short.TYPE){
				intent.putExtra(key, (short[])val);
			}else if(componentType == Integer.TYPE){
				intent.putExtra(key, (int[])val);
			}else if(componentType == Long.TYPE){
				intent.putExtra(key, (long[])val);
			}else if(componentType == Boolean.TYPE){
				intent.putExtra(key, (boolean[])val);
			}else if(componentType == Float.TYPE){
				intent.putExtra(key, (float[])val);
			}else if(componentType == Double.TYPE){
				intent.putExtra(key, (double[])val);
			}else if(Serializable.class.isAssignableFrom(componentType)){
				intent.putExtra(key, (Serializable[])val);
			}else if(Parcelable.class.isAssignableFrom(componentType)){
				intent.putExtra(key, (Parcelable[])val);
			}else{
				throw new IllegalArgumentException("Invalid navigation parameter, key :"+key+", value :"+val);
			}
		}else{
			if(val instanceof Byte){
				intent.putExtra(key, ((Byte)val).byteValue());
			}else if(val instanceof Short){
				intent.putExtra(key, ((Short)val).shortValue());
			}else if(val instanceof Integer){
				intent.putExtra(key, ((Integer)val).intValue());
			}else if(val instanceof Long){
				intent.putExtra(key, ((Long)val).longValue());
			}else if(val instanceof Float){
				intent.putExtra(key, ((Float)val).floatValue());
			}else if(val instanceof Double){
				intent.putExtra(key, ((Double)val).doubleValue());
			}else if(val instanceof String){
				intent.putExtra(key, (String)val);
			}else if(val instanceof Boolean){
				intent.putExtra(key, ((Boolean)val).booleanValue());
			}else if(val instanceof Serializable){
				intent.putExtra(key, (Serializable)val);
			}else if(val instanceof Parcelable){
				intent.putExtra(key, (Parcelable)val);
			}else{
				throw new IllegalArgumentException("Invalid navigation parameter, key :"+key+", value :"+val);
			}
		}
	}

	protected Class<?> getActivityClass(String pageName) {
		IPageDescriptor desc = getManager().getPageDescriptor(pageName);
		IAndroidBindingDescriptor bdesc = (IAndroidBindingDescriptor)desc.getBindingDescriptor(TargetUISystem.ANDROID);
		return bdesc.getTargetClass();
	}
	
	protected void initShowPageIntent(IPage page, Intent intent){

	}
	
	protected IWorkbenchManager getManager(){
		return this.context.getWorkbenchManager();
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#hidePage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void hidePage(IPage page) {
		IBindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			activity.getActivity().finish();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#getCurrentActivePage()
	 */
	@Override
	public IPage getCurrentActivePage() {
		return this.currentPageId != null ? getManager().getWorkbench().getPage(currentPageId): null;
	}


	protected void notifyPageCallbacks(IPage page, int activity){
		List<IPageCallback> cbs = this.callbacks.get(page.getName());
		if(cbs != null){
			for (IPageCallback cb : cbs) {
				switch(activity){
				case PAGE_ACTIVITY_CREATE:
					cb.onCreate(page);
					break;
				case PAGE_ACTIVITY_DESTROY:
					cb.onDestroy(page);
					break;
				case PAGE_ACTIVITY_HIDE:
					cb.onHide(page);
					break;
				case PAGE_ACTIVITY_SHOW:
					cb.onShow(page);
					break;
				}
			}
		}
	}
	
	protected void removePageCallback(String pageId,IPageCallback cb) {
		List<IPageCallback> cbs = this.callbacks.get(pageId);
		if(cbs != null){
			cbs.remove(cb);
		}
	}
	@Override
	public void onPageCreate(IPage page, IBindableActivity activity) {
		if(log.isInfoEnabled()){
			log.info("Page :"+page.getName()+" is created !");
		}
		this.activeActivities.put(page.getName(), activity);
		notifyPageCallbacks(page,PAGE_ACTIVITY_CREATE);
	}

	@Override
	public void onPageShow(IPage page) {
		if(log.isInfoEnabled()){
			log.info("Page :"+page.getName()+" is on show !");
		}
		this.currentPageId = page.getName();
		notifyPageCallbacks(page,PAGE_ACTIVITY_SHOW);
	}

	@Override
	public void onPageHide(IPage page) {
		if(log.isInfoEnabled()){
			log.info("Page :"+page.getName()+" is hidden !");
		}
		if(page.getName().equals(currentPageId)){
			this.currentPageId = null;
		}
		notifyPageCallbacks(page,PAGE_ACTIVITY_HIDE);

	}

	@Override
	public void onPageDetroy(IPage page) {
		if(log.isInfoEnabled()){
			log.info("Page :"+page.getName()+" is destroyed !");
		}
		this.activeActivities.remove(page.getName());
		notifyPageCallbacks(page,PAGE_ACTIVITY_DESTROY);
	}

	@Override
	public IBindableActivity getOnShowActivity() {
		return this.currentPageId != null ? this.activeActivities.get(currentPageId) : null;
	}

	@Override
	public void showView(final IView view, final boolean add2BackStack) {
		IPage page = getPage(view);
		IBindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			hideOrShowView(view,add2BackStack, true);
		}else{
			 IPageCallback cb = new IPageCallback() {
				
				@Override
				public void onShow(IPage page) {
					removePageCallback(page.getName(), this);
					AppUtils.getFramework().runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							hideOrShowView(view, add2BackStack,true);							
						}
					});
					
				}
				
				@Override
				public void onHide(IPage page) {
					
				}
				
				@Override
				public void onDestroy(IPage page) {
					
				}
				
				@Override
				public void onCreate(IPage page) {
					
				}
			};
			showPage(page, null, cb);
		}
	}

	@Override
	public void hideView(IView view) {
		hideOrShowView(view,false,false);		
	}

	/**
	 * @param view
	 */
	protected void hideOrShowView(IView view,boolean add2BackStack, boolean show) {
		IPage page = getPage(view);
		final IViewGroup vg = (IViewGroup)view.getParent();
		IBindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			IViewDescriptor vDesc = context.getWorkbenchManager().getViewDescriptor(view.getName());
			IAndroidBindingDescriptor bDesc = (IAndroidBindingDescriptor)vDesc.getBindingDescriptor(TargetUISystem.ANDROID);
			IFieldBinding binding = activity.getViewBinding().getFieldBinding(vg.getName());
			if(binding == null){
				log.warn("Cannot found binding for view :"+view.getName());
				return;
			}
			ViewGroup vgControl = (ViewGroup)binding.getUIControl();
			if(bDesc.getBindingType() == AndroidBindingType.VIEW){
			}else if(bDesc.getBindingType() == AndroidBindingType.FRAGMENT){
				if(activity.getActivity() instanceof BindableFragmentActivity){
					if(show){
						showFragement((BindableFragmentActivity)activity.getActivity(),vgControl,view.getName(),bDesc,true);
					}else{
						hideFragement((BindableFragmentActivity)activity.getActivity(),view.getName());
					}
				}
			}
		}
	}
	
	private void hideFragement(BindableFragmentActivity activity,String viewId) {
		BindableFragment fragment = activity.getFragment(viewId);
		if(fragment != null){
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.hide(fragment);
			transaction.commit();
		}
	}

	private void showFragement(BindableFragmentActivity activity,ViewGroup vgControl,String viewId, IAndroidBindingDescriptor bDesc,boolean add2Backstack) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		try {
			BindableFragment fragment = (BindableFragment)bDesc.getTargetClass().newInstance();
			transaction.replace(vgControl.getId(),fragment);
			if(add2Backstack){
				transaction.addToBackStack(null);
			}
			transaction.commitAllowingStateLoss();
			activity.addFragment(fragment);
		}catch(Throwable t){
			throw new RuntimeException("Failed to show framment for view :"+viewId, t);
		}
	}


	protected IPage getPage(IView view) {
		IUIContainer<?> v = view ; 
		while(v != null){
			if(v instanceof IPage){
				return (IPage)v;
			}
			v = v.getParent();
		}
		return null;
	}

	@Override
	public IBindableActivity getPageActivity(IPage page) {
		return this.activeActivities.get(page.getName());
	}

}
