/**
 * 
 */
package com.wxxr.mobile.core.rpc.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface RpcService<T extends Request<? extends Response>> {
	T createRequest(String endpointUrl,Map<String, Object> params);
}
