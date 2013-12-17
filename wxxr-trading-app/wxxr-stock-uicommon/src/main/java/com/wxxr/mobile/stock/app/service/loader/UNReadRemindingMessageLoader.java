/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class UNReadRemindingMessageLoader extends AbstractEntityLoader<String, RemindMessageBean, RemindMessageBean>{

	public static final String COMMAND_NAME = "UnReadRemindingMessagesCommand";

	@Override
	public ICommand<List<RemindMessageBean>> createCommand(Map<String, Object> params) {
		UnReadRemindingMessagesCommand command=new UnReadRemindingMessagesCommand();
		return command;
	}

	@Override
	public boolean handleCommandResult(ICommand<?> cmd, List<RemindMessageBean> result,
			IReloadableEntityCache<String, RemindMessageBean> cache) {
		cache.clear();
		if(result!=null){
			for(RemindMessageBean bean:result){
				cache.putEntity(bean.getId(), bean);
			}
		}
		return true;
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	protected List<RemindMessageBean> executeCommand(ICommand<List<RemindMessageBean>> command)
			throws Exception {
		RemindMessageInfoDao dao=cmdCtx.getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
		List<RemindMessageInfo> list=dao.queryRaw(" where read=0 ");
		if(list!=null ){
			for(RemindMessageBean entity:remindMessages){
				RemindMessageBean bean=new RemindMessageBean();
				bean.setAcctId(entity.getAcctId());
				bean.setAcctId(entity.getId());
//					entity.setAttrs(entity.getAttrs().toString());
				bean.setContent(entity.getContent());
				bean.setCreatedDate(entity.getCreatedDate());
				bean.setTitle(entity.getAttrs().get("title"));
				bean.setType(entity.getType());
				remindMessages.add(bean);
			}
		}
		return  remindMessages;
	}

	private static class UnReadRemindingMessagesCommand implements ICommand<List<RemindMessageBean>>{

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<List<RemindMessageBean>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		@Override
		public void validate() {
			
		}
		
	}
}
