/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import static com.wxxr.mobile.core.ui.common.ModelUtils.*;
import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IMenuHandler;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public class MenuBase extends UIComponent implements IMenu {

	private LinkedList<String> commandIds = new LinkedList<String>();
	private IListDataProvider provider;
	
	public MenuBase(){
	}
	
	public MenuBase(String name){
		super(name);
	}
	
	public MenuBase(String[] cmdIds){
		if(cmdIds != null){
			for (String id : cmdIds) {
				this.commandIds.add(id);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String)
	 */
	@Override
	public IUICommand getCommand(String name) {
		return getParent().getChild(name, IUICommand.class);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IMenu#getCommandIds()
	 */
	@Override
	public String[] getCommandIds() {
		return this.commandIds != null ? this.commandIds.toArray(new String[this.commandIds.size()]) : new String[0];
	}
	

	@Override
	public IMenu addCommand(String cmdId) {
		if(!this.commandIds.contains(cmdId)){
			this.commandIds.add(cmdId);
		}
		return this;
	}

	@Override
	public IMenu removeCommand(String cmdId) {
		this.commandIds.remove(cmdId);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#getAdaptor(java.lang.Class)
	 */
	@Override
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IListDataProvider.class){
			if(this.provider == null){
				this.provider = new IListDataProvider() {
					
					private LinkedList<String> items = new LinkedList<String>();
					@Override
					public Object getItemId(Object item) {
						
						return item != null ? (long)((IUICommand)item).getName().hashCode() : 0L;
					}
					
					@Override
					public int getItemCounts() {
						updateItems();
						return items.size();
					}
					
					@Override
					public Object getItem(int i) {
						return getCommand(items.get(i));
					}

					@Override
					public boolean isItemEnabled(Object item) {
						IUICommand cmd = (IUICommand)item;
						Boolean enabled = cmd.getAttribute(AttributeKeys.enabled);
						return (enabled == null)||(enabled.booleanValue() == true);
					}

					@Override
					public boolean updateDataIfNeccessary() {
						updateItems();
						return true;
					}

					protected void updateItems() {
						items.clear();
						for (String id : commandIds) {
							IUICommand cmd = getCommand(id);
							if((cmd != null)&&((cmd.getAttribute(AttributeKeys.visible) == null)||(cmd.getAttribute(AttributeKeys.visible) == true))){
								items.add(id);
							}
						}
					}

				};
			}
			return clazz.cast(this.provider);
		}
		return super.getAdaptor(clazz);
	}

	protected void handleItemClick(IUICommand command,InputEvent event) {
		 event.addProperty("ItemClicked", command);
		 event.setTargetCommand(null);
		 command.handleInputEvent(event);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#handleInputEvent(com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public void handleInputEvent(InputEvent event) {
		String cmdName = event.getTargetCommand();
		if("handleItemClick".equals(cmdName)){
			int position = (Integer)event.getProperty("position");
			handleItemClick((IUICommand)this.provider.getItem(position),event);
			event.setTargetCommand(cmdName);
		}
		super.handleInputEvent(event);
	}
	


	@Override
	public void show() {
		showOrHideMenu(true);
	}

	/**
	 * 
	 */
	protected void showOrHideMenu(boolean show) {
		IMenuHandler handler = getMenuHandler();
		if(handler != null){
			if(show){
				handler.showMenu();
			}else{
				handler.hideMenu();
			}
		}
	}

	/**
	 * @return
	 */
	protected IMenuHandler getMenuHandler() {
		IView v = getView(getParent());
		IViewBinding binding = v != null ? (IViewBinding)v.getBinding() : null;
		IMenuHandler handler = binding != null ? binding.getMenuHandler(getName()) : null;
		if(handler == null){
			IPage p = getPage(getParent());
			binding = p != null ? (IViewBinding)p.getBinding() : null;
			handler = binding != null ? binding.getMenuHandler(getName()) : null;
		}
		return handler;
	}

	@Override
	public void hide() {
		showOrHideMenu(false);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IMenu#isOnShow()
	 */
	@Override
	public boolean isOnShow() {
		IMenuHandler handler = getMenuHandler();
		return handler != null ? handler.isMenuOnShow() : false;
	}

}
