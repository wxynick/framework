/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;

/**
 * @author neillin
 *
 */
public class GenericReloadableEntityCache<K,V,T> extends
		ReloadableEntityCacheImpl<K, V> {
	
	private IEntityLoader<K,V,T> entityLoader;
	private Map<String, Object> commandParameters;

	public GenericReloadableEntityCache(String name, int reloadInterval) {
		super(name, reloadInterval);
	}

	public GenericReloadableEntityCache(String name) {
		super(name);
	}
	
	@SuppressWarnings("unchecked")
	protected IEntityLoader<K,V,T> getEntityLoader() {
		if(this.entityLoader == null){
			this.entityLoader = KUtils.getService(IEntityLoaderRegistry.class).getEntityLoader(getEntityTypeName());
		}
		return this.entityLoader;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.ReloadableEntityCacheImpl#getReloadCommand()
	 */
	@Override
	protected ICommand<?> getReloadCommand() {
		return getEntityLoader().createCommand(getCommandParameters());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.ReloadableEntityCacheImpl#processReloadResult(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected boolean processReloadResult(Object result) {
		return getEntityLoader().handleCommandResult((T)result, this);
	}

	/**
	 * @param entityLoader the entityLoader to set
	 */
	public void setEntityLoader(IEntityLoader<K,V,T> entityLoader) {
		this.entityLoader = entityLoader;
	}

	/**
	 * @return the commandParameters
	 */
	public Map<String, Object> getCommandParameters() {
		return commandParameters;
	}

	/**
	 * @param commandParameters the commandParameters to set
	 */
	public void setCommandParameters(Map<String, Object> commandParameters) {
		this.commandParameters = commandParameters;
	}

}
