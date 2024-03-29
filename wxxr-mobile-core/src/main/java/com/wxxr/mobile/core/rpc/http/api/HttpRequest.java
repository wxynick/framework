/**
 * 
 */
package com.wxxr.mobile.core.rpc.http.api;

import java.util.Map;

import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.rpc.api.Request;

/**
 * @author neillin
 *
 */
public interface HttpRequest extends Request<HttpResponse>,ICancellable {

	Map<String, String> getHeaders();
	
	String getHeader(String key);
	
	void setHeader(String key, String value);
	
	String getURI();
}
