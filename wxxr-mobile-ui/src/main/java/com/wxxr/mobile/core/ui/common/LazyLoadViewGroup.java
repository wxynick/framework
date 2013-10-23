/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;

/**
 * @author neillin
 *
 */
public class LazyLoadViewGroup extends ViewGroupBase {
	private final String[] viewIds;
	private boolean dynamic;
	
	public LazyLoadViewGroup(String grpId, String[] vids){
		this.viewIds = vids;
		setName(grpId);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#getViewIds()
	 */
	@Override
	public String[] getViewIds() {
		return this.viewIds;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#getView(java.lang.String)
	 */
	@Override
	public IView getView(String name) {
		if((this.dynamic == false)&&(!hasView(name))){
			return null;
		}
		IView v = super.getView(name);
		if(v == null){
			IViewDescriptor vDesc = getUIContext().getWorkbenchManager().getViewDescriptor(name);
			v = vDesc.createPresentationModel(getUIContext());
			v.init(getUIContext());
			addChild(v);
		}
		return v;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#hasView(java.lang.String)
	 */
	@Override
	public boolean hasView(String name) {
		for (String vid : this.viewIds) {
			if(vid.equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the dynamic
	 */
	protected boolean isDynamic() {
		return dynamic;
	}

	/**
	 * @param dynamic the dynamic to set
	 */
	protected void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

}
