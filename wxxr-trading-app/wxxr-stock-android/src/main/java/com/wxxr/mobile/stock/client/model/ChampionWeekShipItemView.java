package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;

@View(name = "ChampionWeekShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_week_ship_page_layout_item")
public abstract class ChampionWeekShipItemView extends ViewBase implements
		IModelUpdater {
	
	@Bean
	WeekRankBean weekRank;
	
	@Field(valueKey = "text", binding="${weekRank!=null?weekRank.rankSeq:'--'}")
	String rankSeq;
	
	@Field(valueKey = "text", binding="${weekRank!=null?weekRank.nickName:'--'}")
	String nickName;
	
	@Field(valueKey = "text", binding="${weekRank!=null?weekRank.gainCount:'--'}${'个正收益'}")
	String gainCount;

	@Field(valueKey = "text", binding = "${weekRank!=null?weekRank.gainRate:'--'}", attributes={
			@Attribute(name = "textColor", value = "${weekRank.totalGain>0?'resourceId:color/red':(weekRank.gainRates==0?'resourceId:color/gray':'resourceId:color/green')}")
			})
	String gainRate;

	@Override
	public void updateModel(Object value) {
		if (value instanceof WeekRankBean) {
			weekRank = (WeekRankBean)value;
			registerBean("weekRank",value);
		}
	}
//	
//	/**
//	 * 用户点击
//	 * 
//	 * @param event
//	 * @return
//	 */
//	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
//	CommandResult handlerUserClicked(InputEvent event) {
//		CommandResult result = new CommandResult();
//
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put(Constants.KEY_USER_ID_FLAG, weekRank.getUserId());
//		map.put(Constants.KEY_USER_NAME_FLAG, weekRank.getNickName());
//		result.setPayload(map);
//		result.setResult("");
//		return result;
//	}
}
