package app;

import android.app.Application;
import android.content.Intent;

import com.alibaba.wukong.WKConstants;
import com.alibaba.wukong.WKManager;
import com.alibaba.wukong.im.IMEngine;

import helper.DatabaseHelper;
import service.TalkingMessageService;

public class MyApplication extends Application {
	private static MyApplication instance; 

	public static MyApplication getInstance() {
		return instance;
	} 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this; 
		init();
		initAliWkong();
		startTalkingService();
	}
	
	void initAliWkong(){
		// SDK 默认链接的环境为线上环境，如果要切换到沙箱
		WKManager.setEnvironment(WKConstants.Environment.SANDBOX); 
		// 是否启用悟空保存用户信息（如果设置false只保存openId）setUserAvailable
		IMEngine.setUserAvailable(false);
		// IM初始化
		IMEngine.launch(this);
	}
	
	private void init() {
	//	String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
	//	background = BGHelper.setBackground(this, sex);
		DatabaseHelper database=new DatabaseHelper(this); 
		CrashHandler crashHandler = CrashHandler.getInstance();  
	    crashHandler.init(getApplicationContext());  
	}
	public void startTalkingService(){
		Intent talkingservice = new Intent(this, TalkingMessageService.class);
		startService(talkingservice);
	}



}
