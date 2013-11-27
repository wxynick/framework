/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.UIComponent;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.client.binding.ArticleBodyFieldBinder;
import com.wxxr.mobile.stock.client.binding.BuyStockViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.HideProgressEventBinder;
import com.wxxr.mobile.stock.client.binding.KlineFieldBinder;
import com.wxxr.mobile.stock.client.binding.PageSwiperViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.RefreshEventBinder;
import com.wxxr.mobile.stock.client.binding.RefreshListViewAdapterBinder;
import com.wxxr.mobile.stock.client.binding.RefreshViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.TextChangedEventBinder;
import com.wxxr.mobile.stock.client.binding.TextSpinnerViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.ToolbarTextAttributeUpdater;
import com.wxxr.mobile.stock.client.view.DeclarativePModelProvider;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView;
import com.wxxr.mobile.stock.client.widget.ArticleBodyViewKeys;
import com.wxxr.mobile.stock.client.widget.BuyStockDetailInputView;
import com.wxxr.mobile.stock.client.widget.BuyStockViewKeys;
import com.wxxr.mobile.stock.client.widget.KLineView;
import com.wxxr.mobile.stock.client.widget.PageSwiperView;
import com.wxxr.mobile.stock.client.widget.Pull2RefreshViewKeys;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView;
import com.wxxr.mobile.stock.client.widget.TextSpinnerView;

/**
 * @author neillin
 *
 */
public class WorkbenchManagerModule extends AbstractWorkbenchManagerModule<IStockAppContext> {

	@Override
	protected void initFieldBinders(IFieldBinderManager mgr) {
		mgr.registerFieldBinder(UIComponent.class,PageSwiperView.class, new PageSwiperViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, KLineView.class, new KlineFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, TextSpinnerView.class, new TextSpinnerViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, PullToRefreshView.class, new RefreshViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, PullToRefreshListView.class, new RefreshListViewAdapterBinder());
		mgr.registerFieldBinder(UIComponent.class, ArticleBodyView.class, new ArticleBodyFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, BuyStockDetailInputView.class, new BuyStockViewFieldBinder());
	}

	@Override
	protected void initEventBinders(IEventBinderManager mgr) {
		mgr.registerFieldBinder("TopRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder("BottomRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder(InputEvent.EVENT_TYPE_TEXT_CHANGED, new TextChangedEventBinder());
		mgr.registerFieldBinder("HideDialog", new HideProgressEventBinder());
	}

	@Override
	protected void initAttributeUpdaters(IFieldAttributeManager mgr) {
		Pull2RefreshViewKeys.registerKeys(mgr);
		ArticleBodyViewKeys.registerKeys(mgr);
		BuyStockViewKeys.registerKeys(mgr);
		mgr.registerAttributeUpdater("text", new ToolbarTextAttributeUpdater());
		//mgr.registerAttributeUpdater("label", new EditTextAttributeUpdater());
	}

	@Override
	protected void initPresentationModels(IWorkbenchRTContext context) {
		DeclarativePModelProvider.updatePModel(context);
	}

}
