/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.javax.ws.rs.NotAuthorizedException;
import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.api.UsernamePasswordCredential;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.LoginFailedException;
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
import com.wxxr.mobile.stock.app.service.handler.RegisterHandher.UserRegisterCommand;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordHandler;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordHandler.RestPasswordCommand;
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
import com.wxxr.mobile.stock.app.service.loader.VoucherLoader;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;

/**
 * @author neillin
 * 
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IUserManagementService, IUserAuthManager,IUserIdentityManager {
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.UserManagementServiceImpl");
	private static final String KEY_USERNAME = "U";
	private static final String KEY_PASSWORD = "P";
	private static final String KEY_UPDATE_DATE = "UD";
	private IPreferenceManager prefManager;
	private UsernamePasswordCredential usernamePasswordCredential4Login;
	// ==================beans =============================
	private UserBean myUserInfo ;
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
	private GenericReloadableEntityCache<Long, PullMessageBean, PullMessageVO> pullMessagesCache;

	
	private BindableListWrapper<UserAttributeBean> userAttrbutes;
	private IReloadableEntityCache<String, UserAttributeBean> userAttributeCache;
	
	private BindableListWrapper<GainPayDetailBean> gainPayDetails;
    private GenericReloadableEntityCache<Long, GainPayDetailBean, GainPayDetailsVO> gainPayDetail_cache;
	
    private GenericReloadableEntityCache<String,PersonalHomePageBean,List> personalHomePageBean_cache;
    private GenericReloadableEntityCache<String,PersonalHomePageBean,List> otherpersonalHomePageBean_cache;
    private GenericReloadableEntityCache<String,GainBean,List> gainBean_cache;

    private GenericReloadableEntityCache<String,GainBean,List> otherGainBean_cache;
	private BindableListWrapper<RemindMessageBean> unreadRemindMessages;
	private GenericReloadableEntityCache<String, RemindMessageBean,RemindMessageBean> unreadRemindMessagesCache;
    
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
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
	    addRequiredService(IPreferenceManager.class);

	}

	@Override
	protected void startService() {
	    
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("userAssetBean", new UserAssetLoader());
		registry.registerEntityLoader("clientInfo", new UserAssetLoader());
		registry.registerEntityLoader("voucherBean", new VoucherLoader());
		registry.registerEntityLoader("remindMessageBean", new RemindMessageLoader());
		registry.registerEntityLoader("pullMessageBean", new PullMessageLoader());
		registry.registerEntityLoader("userAttributesBean", new UserAttributeLoader());
        registry.registerEntityLoader("gainPayDetailBean", new GainPayDetailLoader());
        registry.registerEntityLoader("unreadRemindingMsg", new UNReadRemindingMessageLoader());
        
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
		
		personalHomePageBean_cache=new GenericReloadableEntityCache<String,PersonalHomePageBean,List>("personalHomePageBean");
	    otherpersonalHomePageBean_cache=new GenericReloadableEntityCache<String,PersonalHomePageBean,List>("otherpersonalHomePageBean");
	    gainBean_cache  =new GenericReloadableEntityCache<String,GainBean,List> ("gainBean");
	    otherGainBean_cache = new GenericReloadableEntityCache<String, GainBean, List>("gainBean");
        registry.registerEntityLoader("personalHomePageBean", new PersonalHomePageLoader());
        registry.registerEntityLoader("otherpersonalHomePageBean", new OtherPersonalHomePageLoader());
        registry.registerEntityLoader("gainBean", new GainBeanLoader());
       // context.getService(IEventRouter.class).registerEventListener(LogoutEvent.class, listener);
		context.registerService(IUserManagementService.class, this);
		context.registerService(IUserAuthManager.class, this);
		context.registerService(IUserIdentityManager.class, this);
		updateToken();
		restoreUserBean();
	}

	@Override
	protected void stopService() {
	   // context.getService(IEventRouter.class).unregisterEventListener(LogoutEvent.class, listener);
		context.unregisterService(IUserIdentityManager.class, this);
		context.unregisterService(IUserManagementService.class, this);
		context.unregisterService(IUserAuthManager.class, this);
	}

	protected IPreferenceManager getPrefManager() {
		if (this.prefManager == null) {
			this.prefManager = context.getService(IPreferenceManager.class);
		}
		return this.prefManager;
	}

	@Override
	public IUserAuthCredential getAuthCredential(String host, String realm) {
		IPreferenceManager mgr = getPrefManager();
		if (!mgr.hasPreference(getModuleName())
				|| mgr.getPreference(getModuleName()).get(KEY_USERNAME) == null) {
			if (usernamePasswordCredential4Login != null) {
				return usernamePasswordCredential4Login;
			}
//			
//			
			IDialog dialog = getService(IWorkbenchManager.class).getWorkbench().createDialog("userLoginPage",null );
			dialog.show();
//			
		}
		Dictionary<String, String> d = mgr.getPreference(getModuleName());
		String userName = null;
        String passwd = null;
		if (d!=null) {
		   userName = d.get(KEY_USERNAME);
		   passwd = d.get(KEY_PASSWORD);
        }
		return new UsernamePasswordCredential(userName, passwd);
	}

	@Override
	public UserBean getMyUserInfo() {
		return myUserInfo;
	}

	@Override
	public void register(final String phoneNumber) throws StockAppBizException {
		UserRegisterCommand cmd=new UserRegisterCommand();
		cmd.setUserName(phoneNumber);
		try{
			Future<SimpleResultVo> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
				try {
					SimpleResultVo vo=future.get(30,TimeUnit.SECONDS);
					if(vo.getResult()!=0){
						throw new StockAppBizException(vo.getMessage());
					}
				} catch (Exception e) {
					throw new StockAppBizException("系统错误");
				}
			}catch(CommandException e){
				throw new StockAppBizException(e.getMessage());
			}

	}

	@Override
	public void login(final String userId, final String pwd) throws LoginFailedException {
		Future<?> future = context.getExecutor().submit(new Runnable() {
			@Override
			public void run() {
                usernamePasswordCredential4Login = new UsernamePasswordCredential(
                        userId, pwd);
				try {
					UserVO vo = context.getService(IRestProxyService.class).getRestService(StockUserResource.class).getUser();
					if (vo!=null){
    					myUserInfo = new UserBean();
    					myUserInfo.setNickName(vo.getNickName());
    					myUserInfo.setUsername(vo.getUserName());
    					myUserInfo.setPhoneNumber(vo.getMoblie());
    					myUserInfo.setUserPic(vo.getIcon());
    					saveUserBean(myUserInfo);
    					
    					context.setAttribute("currentUser", myUserInfo);
    					 //根据用户密码登录成功
    	                Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName());
    	                if(pref == null){
    	                    pref= new Hashtable<String, String>();
    	                    getPrefManager().newPreference(getModuleName(), pref);
    	                }else{
    	                    pref = DictionaryUtils.clone(pref);
    	                }
    	                pref.put(KEY_USERNAME, userId);
    	                pref.put(KEY_PASSWORD, pwd);
    	                pref.put(KEY_UPDATE_DATE, String.valueOf(System.currentTimeMillis()));
    	                getPrefManager().putPreference(getModuleName(), pref);
					}
					
				} catch (NotAuthorizedException e) {
					log.warn("Failed to login user due to invalid user name and/or password",e);
					usernamePasswordCredential4Login = null;
					throw new LoginFailedException("用户名或密码错误");
				} catch (Throwable e) {
					log.warn("Failed to login user due to unexpected exception",e);
					usernamePasswordCredential4Login = null;
					throw new LoginFailedException("登录失败，请稍后再试...");
				}
				
			}
		});
		if (future != null) {
			try {
				future.get(20, TimeUnit.SECONDS);
			} catch (ExecutionException e) {
				throw (StockAppBizException)e.getCause();
			} catch(Throwable e){
				log.warn("连接超时",e);
				throw new LoginFailedException("登录超时，请稍后再试...");
			}
		}

	}

	@Override
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
	public void logout(){
	   myUserInfo=null;
       getPrefManager().putPreference(getModuleName(), new Hashtable<String, String>());
       HttpRpcService httpService = context.getService(HttpRpcService.class);
       if (httpService != null) {
           httpService.resetHttpClientContext();
       }
       
       context.removeAttribute("currentUser");
      // getService(IEventRouter.class).routeEvent(new LogoutEvent());
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
	    String key="PersonalHomePageBean";
	    if (personalHomePageBean_cache.getEntity(key)==null){
	        PersonalHomePageBean b=new PersonalHomePageBean();
	        personalHomePageBean_cache.putEntity(key,b);
        }
        this.personalHomePageBean_cache.forceReload(null,false);
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
			return o2.getCloseTime().compareTo(o1.getCloseTime());
		}
	};
	
	private static long formatDate2Long(String time) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
		Date date = formatter.parse(time);
		return date.getTime();
	}
	
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
				remindMessagesCache=new GenericReloadableEntityCache<String, RemindMessageBean, MessageVO>("remindMessageBean",60);
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
				pullMessagesCache=new GenericReloadableEntityCache<Long, PullMessageBean, PullMessageVO>("pullMessageBean",60);
				pullMessagesCache.setCommandParameters(params);
			}
			pullMessages=pullMessagesCache.getEntities(null, new Comparator<PullMessageBean>() {

				@Override
				public int compare(PullMessageBean lhs, PullMessageBean rhs) {
					long c=lhs.getCreateDate().compareTo(lhs.getCreateDate());
					return c>=0?1:-1;
				}
				
			});
		}

		pullMessagesCache.doReloadIfNeccessay(params);
		return pullMessages;
	}

	@Override
	public boolean isUserAuthenticated() {
		return myUserInfo != null;
	}

	@Override
	public String getUserId() {
		return myUserInfo.getPhoneNumber();
	}

	@Override
	public boolean usrHasRoles(String... roles) {
		return false;
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
		RefreshUserInfoCommand cmd=new RefreshUserInfoCommand();
		try{
			Future<UserVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
				try {
					UserVO vo=future.get(30,TimeUnit.SECONDS);
					myUserInfo.setNickName(vo.getNickName());
					myUserInfo.setUsername(vo.getUserName());
					myUserInfo.setPhoneNumber(vo.getMoblie());
					myUserInfo.setUserPic(vo.getIcon());
					saveUserBean(myUserInfo);
				} catch (Exception e) {
					throw new StockAppBizException("系统错误");
				}
			}catch(CommandException e){
				throw new StockAppBizException(e.getMessage());
			}
		return null;
	}

	@Override
	public void resetPassword(String userName) {
		RestPasswordCommand command=new RestPasswordCommand();
		command.setUserName(userName);
		try{
			Future<Void> future=context.getService(ICommandExecutor.class).submitCommand(command);
				try {
					future.get(30,TimeUnit.SECONDS);
				} catch (Exception e) {
					throw new StockAppBizException("系统错误");
				}
			}catch(CommandException e){
				throw new StockAppBizException(e.getMessage());
			}
	}
	
	protected void updateToken() {
		UpdateTokenCommand command=new UpdateTokenCommand();
		try{
			Future<TokenVO> future=context.getService(ICommandExecutor.class).submitCommand(command);
		}catch(Throwable e){
			log.warn("updatToken error",e);
		}
	}
	

    private static final String KEY_NICKNAME = "NickName";
    private static final String KEY_PHONENUMBER = "PhoneNumber";
    private static final String KEY_USERPIC = "UserPic";
	private void saveUserBean(UserBean b){
	    Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName());
        if(pref == null){
            pref= new Hashtable<String, String>();
            getPrefManager().newPreference(getModuleName(), pref);
        }else{
            pref = DictionaryUtils.clone(pref);
        }
        if (b.getNickName()!=null){
            pref.put(KEY_NICKNAME, b.getNickName());
        }
        if (b.getUsername()!=null){
            pref.put(KEY_USERNAME, b.getUsername());
        }
        if (b.getPhoneNumber()!=null){
            pref.put(KEY_PHONENUMBER,b.getPhoneNumber());
        }
        if (b.getUserPic()!=null){
            pref.put(KEY_USERPIC, b.getUserPic());
        }
        getPrefManager().putPreference(getModuleName(), pref);

	}
	private void restoreUserBean(){
	    IPreferenceManager mgr = getPrefManager();
        Dictionary<String, String> d = mgr.getPreference(getModuleName());
        String nickName = d != null ? d.get(KEY_NICKNAME) : null;
        String user_name = d != null ? d.get(KEY_USERNAME) : null;
        String pn = d != null ? d.get(KEY_PHONENUMBER) : null;
        String icon = d != null ? d.get(KEY_USERPIC) : null;

        if(user_name != null && this.myUserInfo==null){
            myUserInfo=new UserBean();
            myUserInfo.setNickName(nickName);
            myUserInfo.setUsername(user_name);
            myUserInfo.setPhoneNumber(pn);
            myUserInfo.setUserPic(icon);
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

  
	
}
