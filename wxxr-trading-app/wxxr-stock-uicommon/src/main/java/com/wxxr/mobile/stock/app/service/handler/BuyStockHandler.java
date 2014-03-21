package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.command.BuyStockCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class BuyStockHandler extends BasicCommandHandler<StockResultVO,BuyStockCommand> {
	private static final Trace log = Trace.getLogger(BuyStockHandler.class);
    @Override
    public void execute(final BuyStockCommand cmd, final IAsyncCallback<StockResultVO> callback) {
    	getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).buyStock(cmd.getAcctID(), cmd.getMarket(), cmd.getCode(), Long.valueOf(cmd.getPrice()), Long.valueOf(cmd.getAmount())).
    	onResult(new IAsyncCallback<StockResultVO>() {

 			/* (non-Javadoc)
			 * @see com.wxxr.mobile.stock.trade.command.BasicCommandHandler.DelegateCallback#success(java.lang.Object)
			 */
			@Override
			public void success(StockResultVO result) {
                if (result.getSuccOrNot() == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed buy Stock, caused by "
                                + result.getCause());
                    }
                    callback.failed(new StockAppBizException(result.getCause()));
                }else{
                	callback.success(result);
                }
			}

			@Override
			public void failed(Throwable cause) {
				callback.failed(cause);
			}

			@Override
			public void cancelled() {
				callback.cancelled();
			}

			@Override
			public void setCancellable(ICancellable cancellable) {
				callback.setCancellable(cancellable);
			}
    	});
    }

}
