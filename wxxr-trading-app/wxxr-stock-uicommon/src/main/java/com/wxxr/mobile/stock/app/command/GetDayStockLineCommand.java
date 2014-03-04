package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockLineBean;

@NetworkConstraint
public class GetDayStockLineCommand implements ICommand<List<StockLineBean>> {
    public final static String COMMAND_NAME = "GetDayStockLineCommand";

    private String code;
    private String market;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public Class<List<StockLineBean>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
    	if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("Invalid market or code");
		}
    }

}