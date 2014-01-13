/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IServiceDecoratorBuilder;
import com.wxxr.mobile.core.microkernel.api.IServiceDelegateHolder;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.session.api.ISessionManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserAttributeBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.app.service.handler.GetClientInfoHandler;
import com.wxxr.mobile.stock.app.service.handler.GetClientInfoHandler.ReadClientInfoCommand;
import com.wxxr.mobile.stock.app.service.handler.GetPushMessageSettingHandler;
import com.wxxr.mobile.stock.app.service.handler.GetPushMessageSettingHandler.GetPushMessageSettingCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadAllUnreadMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadAllUnreadMessageHandler.ReadAllUnreadMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadPullMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadPullMessageHandler.ReadPullMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadRemindMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadRemindMessageHandler.ReadRemindMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.RefresUserInfoHandler;
import com.wxxr.mobile.stock.app.service.handler.RefresUserInfoHandler.RefreshUserInfoCommand;
import com.wxxr.mobile.stock.app.service.handler.RegisterHandher;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordHandler;
import com.wxxr.mobile.stock.app.service.handler.SubmitPushMesasgeHandler;
import com.wxxr.mobile.stock.app.service.handler.SubmitPushMesasgeHandler.SubmitPushMesasgeCommand;
import com.wxxr.mobile.stock.app.service.handler.SumitAuthHandler;
import com.wxxr.mobile.stock.app.service.handler.SumitAuthHandler.SubmitAuthCommand;
import com.wxxr.mobile.stock.app.service.handler.UpPwdHandler;
import com.wxxr.mobile.stock.app.service.handler.UpPwdHandler.UpPwdCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateAuthHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateAuthHandler.UpdateAuthCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateNickNameHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateNickNameHandler.UpdateNickNameCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateTokenHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateTokenHandler.UpdateTokenCommand;
import com.wxxr.mobile.stock.app.service.loader.GainBeanLoader;
import com.wxxr.mobile.stock.app.service.loader.GainPayDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.OtherPersonalHomePageLoader;
import com.wxxr.mobile.stock.app.service.loader.PersonalHomePageLoader;
import com.wxxr.mobile.stock.app.service.loader.PullMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.RemindMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.UNReadRemindingMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.UserAssetLoader;
import com.wxxr.mobile.stock.app.service.loader.UserAttributeLoader;
import com.wxxr.mobile.stock.app.service.loader.UserInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.VoucherLoader;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;

/**
 * @author neillin
 * 
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext> implements IUserManagementService, IStatefulService {
	
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.UserManagementServiceImpl");

	private UserBean otherUserInfo = new UserBean();
	private ScoreInfoBean myScoreInfo = new ScoreInfoBean();
	
	
	private TradeDetailListBean myTradeDetails = new TradeDetailListBean();
	/**
	 * 个人主页
	 */
	
	private UserAssetBean userAssetBean;
	
	private IReloadableEntityCache<String,UserAssetBean> userAssetBeanCache;
	
	
	private ClientInfoBean clientInfoBean;
	private VoucherBean voucherBean;
	private IReloadableEntityCache<String,VoucherBean> voucherBeanCache;
	

	private BindableListWrapper<RemindMessageBean> remindMessages;
	private GenericReloadableEntityCache<String, RemindMessageBean,MessageVO> remindMessagesCache;
	
	private BindableListWrapper<PullMessageBean> pullMessages;
	private GenericReloadableEntityCache<String, PullMessageBean, PullMessageVO> pullMessagesCache;

	
	private BindableListWrapper<UserAttributeBean> userAttrbutes;
	private IReloadableEntityCache<String, UserAttributeBean> userAttributeCache;
	
	private BindableListWrapper<GainPayDetailBean> gainPayDetails;
    private GenericReloadableEntityCache<Long, GainPayDetailBean, GainPayDetailsVO> gainPayDetail_cache;
	
    private GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>> personalHomePageBean_cache;
    private GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>> otherpersonalHomePageBean_cache;
    private GenericReloadableEntityCache<String,GainBean,List<GainBean>> gainBean_cache;

    private GenericReloadableEntityCache<String,GainBean,List<GainBean>> otherGainBean_cache;
	private BindableListWrapper<RemindMessageBean> unreadRemindMessages;
	private GenericReloadableEntityCache<String, RemindMessageBean,RemindMessageBean> unreadRemindMessagesCache;
    

	private UserBean myUserInfo;

	/*private IEventListener listener = new IEventListener() {
      @Override
      public void onEvent(IBroadcastEvent event) {
        if (event instanceof LogoutEvent) {
           clearCache();
        }
      }
   };
   protected void clearCache(){
      
   }*/
	//==============  module life cycle =================
//	@Override
//	protected void initServiceDependency() {
//		addRequiredService(IRestProxyService.class);
//		addRequiredService(IEntityLoaderRegistry.class);
//		addRequiredService(ICommandExecutor.class);
//	    addRequiredService(IPreferenceManager.class);
//
//	}


	public void startService() {
	    
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("userAssetBean", new UserAssetLoader());
		registry.registerEntityLoader("clientInfo", new UserAssetLoader());
		registry.registerEntityLoader("voucherBean", new VoucherLoader());
		registry.registerEntityLoader("remindMessageBean", new RemindMessageLoader());
		registry.registerEntityLoader("pullMessageBean", new PullMessageLoader());
		registry.registerEntityLoader("userAttributesBean", new UserAttributeLoader());
        registry.registerEntityLoader("gainPayDetailBean", new GainPayDetailLoader());
        registry.registerEntityLoader("unreadRemindingMsg", new UNReadRemindingMessageLoader());
        
        registry.registerEntityLoader("myUserInfo", new UserInfoLoader());

		context.getService(ICommandExecutor.class).registerCommandHandler(UpPwdHandler.COMMAND_NAME, new UpPwdHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(UpdateAuthHandler.COMMAND_NAME, new UpdateAuthHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(SumitAuthHandler.COMMAND_NAME, new SumitAuthHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(SubmitPushMesasgeHandler.COMMAND_NAME, new SubmitPushMesasgeHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(GetPushMessageSettingHandler.COMMAND_NAME, new GetPushMessageSettingHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(UpdateNickNameHandler.COMMAND_NAME, new UpdateNickNameHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(RefresUserInfoHandler.COMMAND_NAME, new RefresUserInfoHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(RestPasswordHandler.COMMAND_NAME, new RestPasswordHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(UpdateTokenHandler.COMMAND_NAME, new UpdateTokenHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(RegisterHandher.COMMAND_NAME, new RegisterHandher());
		context.getService(ICommandExecutor.class).registerCommandHandler(ReadRemindMessageHandler.COMMAND_NAME, new ReadRemindMessageHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(ReadAllUnreadMessageHandler.COMMAND_NAME, new ReadAllUnreadMessageHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(ReadPullMessageHandler.COMMAND_NAME, new ReadPullMessageHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(GetClientInfoHandler.COMMAND_NAME, new GetClientInfoHandler());
		
		
	   
	    gainBean_cache  =new GenericReloadableEntityCache<String,GainBean,List<GainBean>> ("gainBean");
	    otherGainBean_cache = new GenericReloadableEntityCache<String, GainBean, List<GainBean>>("gainBean");
        registry.registerEntityLoader("personalHomePageBean", new PersonalHomePageLoader());
        registry.registerEntityLoader("otherpersonalHomePageBean", new OtherPersonalHomePageLoader());
        registry.registerEntityLoader("gainBean", new GainBeanLoader());
       // context.getService(IEventRouter.class).registerEventListener(LogoutEvent.class, listener);
		context.registerService(IUserManagementService.class, this);
	
		updateToken();
	}



	public void stopService() {
		context.unregisterService(IUserManagementService.class, this);

		userAssetBeanCache=null;
		voucherBeanCache=null;
		remindMessagesCache=null;
		pullMessagesCache=null;
		userAttributeCache=null;
		gainPayDetail_cache=null;
		personalHomePageBean_cache=null;
		otherpersonalHomePageBean_cache=null;
		gainBean_cache=null;
		otherGainBean_cache=null;
		unreadRemindMessagesCache=null;
	}



	@Override
	public UserBean getMyUserInfo() {
		if(myUserInfo==null){
			myUserInfo = getService(IUserLoginManagementService.class).getMyUserInfo();
		}
		return myUserInfo;
	}




	public String getModuleName() {
		return "UserManagementService";
	}

	@Override
	public void pushMessageSetting(boolean on) {
		SubmitPushMesasgeCommand command=new SubmitPushMesasgeCommand();
		command.setBinding(on);
		Future<Boolean> future=context.getService(ICommandExecutor.class).submitCommand(command);
		try {
			future.get(30,TimeUnit.SECONDS);
		} catch (Exception e) {
			new StockAppBizException("系统错误");
		}
	}

	@Override
	public boolean getPushMessageSetting() {
		GetPushMessageSettingCommand cmd=new GetPushMessageSettingCommand();
		Future<SimpleResultVo> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
		try {
			SimpleResultVo result=future.get(30,TimeUnit.SECONDS);
			return result.getResult()==1;
		} catch (Exception e) {
			new StockAppBizException("系统错误");
		}
		return false;
	}

	public void setRegRulesReaded(boolean isRead) {

	}



	public ScoreInfoBean fetchUserScoreInfo(String userId) {

		ScoreInfoBean entity = new ScoreInfoBean();

		entity.setBalance("200");

		List<ScoreBean> scores = new ArrayList<ScoreBean>();
		for (int i = 0; i < 9; i++) {
			ScoreBean score = new ScoreBean();
			score.setCatagory("推荐好友奖励");
			score.setAmount(200f);
			score.setDate("2011-10-12");
			scores.add(score);
		}

		entity.setScores(scores);
		return entity;
	}

	@Override
	public  void updatePassword(String oldPwd,String newPwd,String newPwd2) throws StockAppBizException {
		
		UpPwdCommand cmd=new UpPwdCommand();
		cmd.setOldPwd(oldPwd);
		cmd.setNewPwd(newPwd);
		cmd.setNewPwd2(newPwd2);
		try {
			Future<ResultBaseVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);

			ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
			if(vo.getResulttype()!=1){
				throw new StockAppBizException(vo.getResultInfo());
			}
		}catch(StockAppBizException e){
			throw e;
		}
		catch(CommandException e){
			throw new StockAppBizException(e.getMessage());
		}catch (Exception e) {
			throw new StockAppBizException("系统错误");
		}
	}




	@Override
	public UserBean getUserInfoById(String userId) {
		context.invokeLater(new Runnable() {
			public void run() {
				
				
			}
		}, 1, TimeUnit.SECONDS);
		return otherUserInfo;
	}

	public void switchBankCard(String bankName,
			String bankAddr, String bankNum) {
		UpdateAuthCommand cmd=new UpdateAuthCommand();
		cmd.setBankName(bankName);
		cmd.setBankNum(bankNum);
		cmd.setBankAddr(bankAddr);
		try{
		Future<ResultBaseVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
			try {
				ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
				if(vo.getResulttype()!=1){
					throw new StockAppBizException(vo.getResultInfo());
				}
			} catch (Exception e) {
				throw new StockAppBizException("系统错误");
			}
		}catch(CommandException e){
			throw new StockAppBizException(e.getMessage());
		}
	}

	public void withDrawCashAuth(String accountName, String bankName,
			String bankAddr, String bankNum) {
		SubmitAuthCommand cmd=new SubmitAuthCommand();
		cmd.setAccountName(accountName);
		cmd.setBankName(bankName);
		cmd.setBankNum(bankNum);
		cmd.setBankAddr(bankAddr);
		try{
			Future<ResultBaseVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
			try {
				ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
				if(vo.getResulttype()!=1){
					throw new StockAppBizException(vo.getResultInfo());
				}
			} catch (Exception e) {
				new StockAppBizException("系统错误");
			}
		}catch(CommandException e){
			throw new StockAppBizException(e.getMessage());
		}
		
	}

	public AuthInfo getUserAuthInfo() {
		if(userAttrbutes==null){
			if(userAttributeCache==null){
				userAttributeCache=new GenericReloadableEntityCache<String, UserAttributeBean, UserAttributeVO>("userAttributesBean");
			}
			userAttrbutes=userAttributeCache.getEntities(new IEntityFilter<UserAttributeBean>() {
				@Override
				public boolean doFilter(UserAttributeBean entity) {
					return "BANK_POSITION".equals(entity.getName())||"BANK_NUM".equals(entity.getName())||
							"ACCT_NAME".equals(entity.getName())||"ACCT_BANK".equals(entity.getName());
				}
			}, null);
		}
		userAttributeCache.forceReload(true);
		if(userAttrbutes.getData()==null ||userAttrbutes.getData().size()==0){
			return null;
		}
		
		AuthInfo authinfo=new AuthInfo();
		for(UserAttributeBean bean:userAttrbutes.getData()){
			if("BANK_POSITION".equals(bean.getName())){
				authinfo.setBankAddr(bean.getValue());
			}else if("BANK_NUM".equals(bean.getName())){
				authinfo.setBankNum(bean.getValue());
			}else if("ACCT_NAME".equals(bean.getName())){
				authinfo.setAccountName(bean.getValue());
			}else if("ACCT_BANK".equals(bean.getName())){
				authinfo.setBankName(bean.getValue());
			}
			
		}
		return authinfo;
	}

	public String getUserAuthMobileNum(String userId) {

		return null;
	}



	@Override
	public ScoreInfoBean getMyUserScoreInfo() {
		if (context.getApplication().isInDebugMode()) {
			myScoreInfo = MockDataUtils.mockScoreInfo();
			return myScoreInfo;
		}
		return null;
	}

	@Override
	public TradeDetailListBean getMyTradeDetailInfo() {
		if (context.getApplication().isInDebugMode()) {
			myTradeDetails = MockDataUtils.mockTradeDetails();
			return myTradeDetails;
		}
		//getRestService(TradingResourse.class).get
		return null;
	}

	@Override
	public PersonalHomePageBean getOtherPersonalHomePage(final String userId) {
		if (otherpersonalHomePageBean_cache==null) {
			 otherpersonalHomePageBean_cache=new GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>>("otherpersonalHomePageBean");
		}
	    String key=userId;
        if (otherpersonalHomePageBean_cache.getEntity(key)==null){
            PersonalHomePageBean b=new PersonalHomePageBean();
            otherpersonalHomePageBean_cache.putEntity(key,b);
        }
        Map<String, Object> p=new HashMap<String, Object>(); 
        p.put("userId", userId);
        this.otherpersonalHomePageBean_cache.forceReload(p,false);
        return otherpersonalHomePageBean_cache.getEntity(key);
	}
	
	
	@Override
	public PersonalHomePageBean getMyPersonalHomePage() {
        this.personalHomePageBean_cache.forceReload(null,true);
        if (personalHomePageBean_cache==null) {
			personalHomePageBean_cache=new GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>>("personalHomePageBean");
		}
	    String key="PersonalHomePageBean";
	    if (personalHomePageBean_cache.getEntity(key)==null){
	        PersonalHomePageBean b=new PersonalHomePageBean();
	        personalHomePageBean_cache.putEntity(key,b);
        }
        return personalHomePageBean_cache.getEntity(key);
	}

    public BindableListWrapper<GainBean> getMorePersonalRecords(int start, int limit,final boolean virtual) {
        BindableListWrapper<GainBean> gainBeans = gainBean_cache.getEntities(new IEntityFilter<GainBean>(){
            @Override
            public boolean doFilter(GainBean entity) {
                if ( StringUtils.isBlank(entity.getUserId()) && entity.getVirtual()==virtual){
                    return true;
                }
                return false;
            }
            
        }, viewMoreComparator);
     
      Map<String, Object> p=new HashMap<String, Object>(); 
      p.put("virtual", virtual);
      p.put("start", start);
      p.put("limit", limit);
      gainBean_cache.forceReload(p,false);
      gainBean_cache.setCommandParameters(p);
     return gainBeans;
    }
    
    private static Comparator<GainBean> viewMoreComparator = new Comparator<GainBean>() {

		@Override
		public int compare(GainBean o1, GainBean o2) {
			
			
			if (o2!=null&&o1!=null) {
				return (int)(o2.getTradingAccountId()-o1.getTradingAccountId());
			}
			return 0;
			//return o2.getCloseTime().compareTo(o1.getCloseTime());
		}
	};
	

	
    public BindableListWrapper<GainBean> getMoreOtherPersonal(final String userId, int start,int limit, final boolean virtual) {
        BindableListWrapper<GainBean> gainBeans = otherGainBean_cache.getEntities(new IEntityFilter<GainBean>(){
            @Override
            public boolean doFilter(GainBean entity) {
                if ( entity.getUserId().equals(userId) && entity.getVirtual()==virtual ){
                    return true;
                }
                return false;
            }
            
        }, viewMoreComparator);
      Map<String, Object> p=new HashMap<String, Object>(); 
      p.put("virtual", virtual);
      p.put("start", start);
      p.put("limit", limit);
      p.put("userId", userId);
      otherGainBean_cache.forceReload(p,false);
      otherGainBean_cache.setCommandParameters(p);
     return gainBeans;
    }
	@Override
	public UserAssetBean getUserAssetBean() {
		if(userAssetBean==null){
			if(userAssetBeanCache==null){
				userAssetBeanCache=new GenericReloadableEntityCache<String, UserAssetBean, UserAssetVO>("userAssetBean");
			}
			userAssetBean=userAssetBeanCache.getEntity(UserAssetBean.class.getCanonicalName());
			if(userAssetBean==null){
				userAssetBean=new UserAssetBean();
				userAssetBeanCache.putEntity(UserAssetBean.class.getCanonicalName(), userAssetBean);
			}
		}
		userAssetBeanCache.doReloadIfNeccessay();
		return userAssetBean;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getVoucherBean()
	 */
	@Override
	public VoucherBean getVoucherBean() {
		if(voucherBean==null){
			if(voucherBeanCache==null){
				voucherBeanCache=new GenericReloadableEntityCache<String, VoucherBean, ActivityUserVo>("voucherBean");
			}
			voucherBean=voucherBeanCache.getEntity(VoucherBean.class.getCanonicalName());
			if(voucherBean==null){
				voucherBean=new VoucherBean();
				voucherBeanCache.putEntity(VoucherBean.class.getCanonicalName(), voucherBean);
			}
		}
		voucherBeanCache.doReloadIfNeccessay();
		return voucherBean;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getRemindMessageBean()
	 */
	@Override
	public BindableListWrapper<RemindMessageBean> getRemindMessageBean() {
		if(remindMessages==null){
			if(remindMessagesCache==null){
				remindMessagesCache=new GenericReloadableEntityCache<String, RemindMessageBean, MessageVO>("remindMessageBean",300);
			}
			remindMessages=remindMessagesCache.getEntities(null, new Comparator<RemindMessageBean>() {
				
				@Override
				public int compare(RemindMessageBean lhs, RemindMessageBean rhs) {
					long c=rhs.getCreatedDate().compareTo(lhs.getCreatedDate());
					if(c==0){
						c=((String)rhs.getAttrs().get("time")).compareTo((String)lhs.getAttrs().get("time"));
					}
					return c>=0?1:-1;
				}
			});
		}
		remindMessagesCache.doReloadIfNeccessay();
		return remindMessages;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getPullMessageBean()
	 */
	@Override
	public BindableListWrapper<PullMessageBean> getPullMessageBean(int start,int limit) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		if(pullMessages==null){
			if(pullMessagesCache==null){
				pullMessagesCache=new GenericReloadableEntityCache<String, PullMessageBean, PullMessageVO>("pullMessageBean");
			}
			pullMessages=pullMessagesCache.getEntities(null, new Comparator<PullMessageBean>() {

				@Override
				public int compare(PullMessageBean lhs, PullMessageBean rhs) {
					long c=rhs.getCreateDate().compareTo(lhs.getCreateDate());
					return c>=0?1:-1;
				}
				
			});
		}

		pullMessagesCache.doReloadIfNeccessay(params);
		return pullMessages;
	}

	

	
	private static final Comparator<GainPayDetailBean> gainPayDetailComparator = new Comparator<GainPayDetailBean>() {

		@Override
		public int compare(GainPayDetailBean o1, GainPayDetailBean o2) {
			
			return (int)(o2.getTime() - o1.getTime());
		}
	};
	
    @Override
    public BindableListWrapper<GainPayDetailBean> getGPDetails(int start, int limit) {
        if(gainPayDetails==null){
            if(gainPayDetails==null){
                gainPayDetail_cache=new GenericReloadableEntityCache<Long, GainPayDetailBean, GainPayDetailsVO>("gainPayDetailBean");
            }
            gainPayDetails = gainPayDetail_cache.getEntities(null, gainPayDetailComparator);
        }
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("start", start);
        params.put("limit", limit);
        gainPayDetail_cache.doReloadIfNeccessay(params);
        return gainPayDetails;
    }

	@Override
	public void updateNickName(String nickName) {
		UpdateNickNameCommand cmd=new UpdateNickNameCommand();
		cmd.setNickName(nickName);
		try{
		Future<ResultBaseVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
			try {
				ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
				if(vo.getResulttype()!=1){
					throw new StockAppBizException(vo.getResultInfo());
				}
			} catch (Exception e) {
				throw new StockAppBizException("系统错误");
			}
		}catch(CommandException e){
			throw new StockAppBizException(e.getMessage());
		}
	}

	@Override
	public UserBean refreshUserInfo() {
		if(myUserInfo==null){
			myUserInfo=new UserBean();
		}
		RefresUserInfoHandler.RefreshUserInfoCommand command=new RefreshUserInfoCommand();
		Future<UserVO> future=context.getService(ICommandExecutor.class).submitCommand(command);
		try {
			UserVO userVO=future.get(30,TimeUnit.SECONDS);
			myUserInfo.setNickName(userVO.getNickName());
			myUserInfo.setUsername(userVO.getUserName());
			myUserInfo.setPhoneNumber(userVO.getMoblie());
		} catch (Exception e) {
			new StockAppBizException("系统错误");
		}
		return myUserInfo;
	}


	
	protected void updateToken() {
		UpdateTokenCommand command=new UpdateTokenCommand();
		try{
			Future<TokenVO> future=context.getService(ICommandExecutor.class).submitCommand(command);
		}catch(Throwable e){
			log.warn("updatToken error",e);
		}
	}
	



	@Override
	public void readRemindMessage(String read) {
		ReadRemindMessageCommand command=new ReadRemindMessageCommand();
		command.setId(read);
		try{
			Future<Void> future=context.getService(ICommandExecutor.class).submitCommand(command);
		}catch(Throwable e){
			log.warn("updatToken error",e);
		}
	}

	@Override
	public  BindableListWrapper<RemindMessageBean> getUnreadRemindMessages() {
		if(unreadRemindMessages==null){
			if(unreadRemindMessagesCache==null){
				unreadRemindMessagesCache=new GenericReloadableEntityCache<String, RemindMessageBean, RemindMessageBean>("unreadRemindingMsg",60);
			}
			unreadRemindMessages=unreadRemindMessagesCache.getEntities(null, new Comparator<RemindMessageBean>() {
				
				@Override
				public int compare(RemindMessageBean lhs, RemindMessageBean rhs) {
					long c=rhs.getCreatedDate().compareTo(lhs.getCreatedDate());
					if(c==0){
						c=((String)rhs.getAttrs().get("time")).compareTo((String)lhs.getAttrs().get("time"));
					}
					return c>=0?1:-1;
				}
			});
		}
		unreadRemindMessagesCache.doReloadIfNeccessay();
		return unreadRemindMessages;
	}

	@Override
	public void readAllUnremindMessage() {
		ReadAllUnreadMessageCommand command=new ReadAllUnreadMessageCommand();
		try{
			context.getService(ICommandExecutor.class).submitCommand(command);
		}catch(Throwable e){
			log.warn("updatToken error",e);
		}
	}
	 @Override
	   public ClientInfoBean getClientInfo() {	   
	      if (clientInfoBean==null) {
	         clientInfoBean = new ClientInfoBean();
	      }
	      ReadClientInfoCommand cmd=new ReadClientInfoCommand();
	      try{
	          Future<ClientInfoVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
	              try {
	                  ClientInfoVO vo=future.get(3,TimeUnit.SECONDS);
	                  if (vo!=null) {
	                     clientInfoBean.setStatus(vo.getStatus());
	                     clientInfoBean.setDescription(vo.getDescription());
	                     clientInfoBean.setUrl(vo.getUrl());
	                     clientInfoBean.setVersion(vo.getVersion());
	                  }
	              } catch (Exception e) {
	                 return clientInfoBean;
	                  //throw new StockAppBizException("系统错误");
	              }
	          }catch(CommandException e){
	             return clientInfoBean;
	              //throw new StockAppBizException(e.getMessage());
	          }
	      return clientInfoBean;
	   }
	@Override
	public void readPullMesage(long id) {
		ReadPullMessageCommand command=new ReadPullMessageCommand();
		command.setId(id);
		try{
			Future<Void> future=context.getService(ICommandExecutor.class).submitCommand(command);
			future.get(30,TimeUnit.SECONDS);
		}catch(Throwable e){
			log.warn("updatToken error",e);
		}
	}


	protected <S> S getService(Class<S> clazz) {
		return this.context.getService(clazz);
	}



	@Override
	public void destroy(Object serviceHandler) {
		
	}



	@Override
	public IServiceDecoratorBuilder getDecoratorBuilder() {
		return new IServiceDecoratorBuilder() {
			
			@Override
			public <T> T createServiceDecorator(Class<T> clazz,
					final IServiceDelegateHolder<T> holder) {
				if(clazz == IUserManagementService.class){
					return clazz.cast(new IUserManagementService() {
						
						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyUserInfo()
						 */
						public UserBean getMyUserInfo() {
							return ((IUserManagementService)holder.getDelegate()).getMyUserInfo();
						}

						/**
						 * @param userId
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserInfoById(java.lang.String)
						 */
						public UserBean getUserInfoById(String userId) {
							return ((IUserManagementService)holder.getDelegate()).getUserInfoById(userId);
						}

						/**
						 * @param oldPwd
						 * @param newPwd
						 * @param newPwd2
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#updatePassword(java.lang.String, java.lang.String, java.lang.String)
						 */
						public void updatePassword(String oldPwd,
								String newPwd, String newPwd2)
								throws StockAppBizException {
							((IUserManagementService)holder.getDelegate()).updatePassword(oldPwd, newPwd, newPwd2);
						}

						/**
						 * @param on
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#pushMessageSetting(boolean)
						 */
						public void pushMessageSetting(boolean on) {
							((IUserManagementService)holder.getDelegate()).pushMessageSetting(on);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getPushMessageSetting()
						 */
						public boolean getPushMessageSetting() {
							return ((IUserManagementService)holder.getDelegate()).getPushMessageSetting();
						}

						/**
						 * @param isRead
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#setRegRulesReaded(boolean)
						 */
						public void setRegRulesReaded(boolean isRead) {
							((IUserManagementService)holder.getDelegate()).setRegRulesReaded(isRead);
						}

						/**
						 * @param userId
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#fetchUserScoreInfo(java.lang.String)
						 */
						public ScoreInfoBean fetchUserScoreInfo(String userId) {
							return ((IUserManagementService)holder.getDelegate()).fetchUserScoreInfo(userId);
						}

						/**
						 * @param bankName
						 * @param bankAddr
						 * @param bankNum
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#switchBankCard(java.lang.String, java.lang.String, java.lang.String)
						 */
						public void switchBankCard(String bankName,
								String bankAddr, String bankNum) {
							((IUserManagementService)holder.getDelegate()).switchBankCard(bankName, bankAddr, bankNum);
						}

						/**
						 * @param accountName
						 * @param bankName
						 * @param bankAddr
						 * @param bankNum
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#withDrawCashAuth(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
						 */
						public void withDrawCashAuth(String accountName,
								String bankName, String bankAddr, String bankNum) {
							((IUserManagementService)holder.getDelegate()).withDrawCashAuth(accountName, bankName,
									bankAddr, bankNum);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserAuthInfo()
						 */
						public AuthInfo getUserAuthInfo() {
							return ((IUserManagementService)holder.getDelegate()).getUserAuthInfo();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyUserScoreInfo()
						 */
						public ScoreInfoBean getMyUserScoreInfo() {
							return ((IUserManagementService)holder.getDelegate()).getMyUserScoreInfo();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyTradeDetailInfo()
						 */
						public TradeDetailListBean getMyTradeDetailInfo() {
							return ((IUserManagementService)holder.getDelegate()).getMyTradeDetailInfo();
						}

						/**
						 * @param userId
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getOtherPersonalHomePage(java.lang.String)
						 */
						public PersonalHomePageBean getOtherPersonalHomePage(
								String userId) {
							return ((IUserManagementService)holder.getDelegate()).getOtherPersonalHomePage(userId);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyPersonalHomePage()
						 */
						public PersonalHomePageBean getMyPersonalHomePage() {
							return ((IUserManagementService)holder.getDelegate()).getMyPersonalHomePage();
						}

						/**
						 * @param start
						 * @param limit
						 * @param virtual
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMorePersonalRecords(int, int, boolean)
						 */
						public BindableListWrapper<GainBean> getMorePersonalRecords(
								int start, int limit, boolean virtual) {
							return ((IUserManagementService)holder.getDelegate()).getMorePersonalRecords(start,
									limit, virtual);
						}

						/**
						 * @param userId
						 * @param start
						 * @param limit
						 * @param virtual
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMoreOtherPersonal(java.lang.String, int, int, boolean)
						 */
						public BindableListWrapper<GainBean> getMoreOtherPersonal(
								String userId, int start, int limit,
								boolean virtual) {
							return ((IUserManagementService)holder.getDelegate()).getMoreOtherPersonal(userId, start,
									limit, virtual);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserAssetBean()
						 */
						public UserAssetBean getUserAssetBean() {
							return ((IUserManagementService)holder.getDelegate()).getUserAssetBean();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getVoucherBean()
						 */
						public VoucherBean getVoucherBean() {
							return ((IUserManagementService)holder.getDelegate()).getVoucherBean();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getRemindMessageBean()
						 */
						public BindableListWrapper<RemindMessageBean> getRemindMessageBean() {
							return ((IUserManagementService)holder.getDelegate()).getRemindMessageBean();
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getPullMessageBean(int, int)
						 */
						public BindableListWrapper<PullMessageBean> getPullMessageBean(
								int start, int limit) {
							return ((IUserManagementService)holder.getDelegate()).getPullMessageBean(start, limit);
						}

						/**
						 * @param nickName
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#updateNickName(java.lang.String)
						 */
						public void updateNickName(String nickName) {
							((IUserManagementService)holder.getDelegate()).updateNickName(nickName);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getGPDetails(int, int)
						 */
						public BindableListWrapper<GainPayDetailBean> getGPDetails(
								int start, int limit) {
							return ((IUserManagementService)holder.getDelegate()).getGPDetails(start, limit);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#refreshUserInfo()
						 */
						public UserBean refreshUserInfo() {
							return ((IUserManagementService)holder.getDelegate()).refreshUserInfo();
						}

						/**
						 * @param id
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readRemindMessage(java.lang.String)
						 */
						public void readRemindMessage(String id) {
							((IUserManagementService)holder.getDelegate()).readRemindMessage(id);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUnreadRemindMessages()
						 */
						public BindableListWrapper<RemindMessageBean> getUnreadRemindMessages() {
							return ((IUserManagementService)holder.getDelegate()).getUnreadRemindMessages();
						}

						/**
						 * 
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readAllUnremindMessage()
						 */
						public void readAllUnremindMessage() {
							((IUserManagementService)holder.getDelegate()).readAllUnremindMessage();
						}

						/**
						 * @param id
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readPullMesage(long)
						 */
						public void readPullMesage(long id) {
							((IUserManagementService)holder.getDelegate()).readPullMesage(id);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getClientInfo()
						 */
						public ClientInfoBean getClientInfo() {
							return ((IUserManagementService)holder.getDelegate()).getClientInfo();
						}
						
					});
				}
				throw new IllegalArgumentException("Invalid service class :"+clazz);
			}
		};
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()  {
		try {
			UserManagementServiceImpl impl = (UserManagementServiceImpl)super.clone();
			impl.otherUserInfo = new UserBean();
			impl.myScoreInfo = new ScoreInfoBean();
			impl.myTradeDetails = new TradeDetailListBean();
			return impl;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("SHOULD NOT HAPPEN !");
		}
	}



	@Override
	protected void initServiceDependency() {
		addRequiredService(ISessionManager.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
	    addRequiredService(IPreferenceManager.class);
	    addRequiredService(IUserIdentityManager.class);
	    addRequiredService(IUserLoginManagementService.class);
	}



	
	
}
