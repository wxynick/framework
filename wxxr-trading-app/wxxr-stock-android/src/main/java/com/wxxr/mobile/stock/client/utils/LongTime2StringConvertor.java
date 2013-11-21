/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author dz
 *
 */
public class LongTime2StringConvertor implements IValueConvertor<Long, String> {

	private String format = "yyyy-MM-DD hh:mm:ss";
	@Override
	public void destroy() {
	}

	@Override
	public Class<Long> getSourceType() {
		return Long.class;
	}

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> map) {
		if(map.containsKey("format")){
			this.format = (String)map.get("format");
		}
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if(StringUtils.isBlank(s)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(s);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if(val == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date(val);
		return sdf.format(date);
	}

}
