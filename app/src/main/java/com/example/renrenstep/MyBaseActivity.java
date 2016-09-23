package com.example.renrenstep;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.ApplayCustomer;
import bean.Customer;
import helper.BaseAsyncTask;
import constant.Cons;
import db.DBhelper;
import helper.SPHelper;
import manager.TalkAuthService;
import receiver.BrocastReviverManager;
import tools.ToastManager;

public class MyBaseActivity extends FragmentActivity {
	public interface IDownPicHandler {
		void handler();
		void failed();
	}
	public DBhelper db = DBhelper.instance();
	private  BrocastReviverManager receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		receiver=new BrocastReviverManager(this);
		super.onCreate(savedInstanceState);
	}

	protected void registBrocaster(){
		receiver.setCallback(new BrocastReviverManager.ReciverCallback() {
			@Override
			public void handler(Intent intent) {
				receiveMsgHander(intent);
			}
		});
		receiver.addFilter(Cons.RECIVE_MSG_ACTION);
		receiver.begin();
	}

	protected void unregistBrocaster(){
		receiver.end();
	}

	protected void initWkLogion(final TalkAuthService.ILoginCallbak plogion) {
		TalkAuthService.initWkLogion(plogion);
	}

	public void getOneFriend(final String mid, final String typ,final IDownPicHandler handler) {
		final String userid=SPHelper.getBaseMsg(MyBaseActivity.this, "mid", "0");
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", mid);
		new BaseAsyncTask(Cons.GETAPPLAYMANURL, maps, BaseAsyncTask.HttpType.Get, "", MyBaseActivity.this) {
			@Override
			public void handler(String param) {
				JSONObject jsonobj;
				if (param == null || param.equals("")|| !param.contains("status")) {
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if (jsonobj.getInt("status") == 0) {
						ApplayCustomer applycus = new ApplayCustomer();
						Gson gson = new Gson();
						applycus = gson.fromJson(param, applycus.getClass());
						Customer item = applycus.getItem();
						if (typ.equals("new")) {
							item.setState("UNREAD");
						}
						if(hasfirend(mid,typ)){
							ContentValues contents= new ContentValues(); 
								contents.put("mid", item.getId());
								contents.put("nc", filterStr(item.getNc()));
								contents.put("email",filterStr(item.getEmail()));
								contents.put("address", filterStr(item.getCity_alia()));
								contents.put("acvtor", filterStr(item.getAvatar()));
								contents.put("state", filterStr(item.getState()));
								contents.put("words", filterStr(item.getWords()));//remark,idcd,typ
								contents.put("remark", filterStr(item.getRemark()));
								contents.put("idcd", userid);
								contents.put("typ", typ);
								db.open();
								db.insert("apm_sys_friend", contents);
								db.close();
						}
						String actor = item.getAvatar();
						List<String> pics = new ArrayList<String>();
						pics.add(actor);
						if(handler!=null){
							handler.handler();
						}
					}
				} catch (Exception ex) {
					ToastManager.show(MyBaseActivity.this, getResources().getString(R.string.wangluoyic), 2000);
				}
			}
		}.execute("");
	}
	
	String filterStr(String pitem) {
		if (pitem == null) {
			return pitem;
		}
		return pitem;
	}
	
	boolean hasfirend(String mid,String typ) {
		final String userid=SPHelper.getBaseMsg(MyBaseActivity.this, "mid", "0");
		String sql="select mid from apm_sys_friend where idcd='"+userid+"' and typ='"+typ+"' and mid='"+mid+"'";
		db.open();
		Cursor cursor= db.query(sql);
		int cur = cursor.getCount();
		boolean notfriend= cur == 0;
		cursor.close();
		db.close();
		return notfriend;
	}
	
	public Dialog getDialog(View view){
		Dialog alterDialog = new Dialog(this);
		alterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alterDialog.setCanceledOnTouchOutside(false);
		alterDialog.setContentView(view);
		WindowManager windowManager = this.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = alterDialog.getWindow().getAttributes();
		lp.width = display.getWidth()-70; // 设置宽度
		alterDialog.getWindow().setAttributes(lp);
		return alterDialog;
	}

	
	public void receiveMsgHander(Intent intent){}

	public void checkUserMsgChange(String key,String value,String pmsg){
		if(ischangedStr(key,value)){
			sendUsermsgChangeMsg(pmsg);
		}
	}
	
	public boolean ischangedStr(String key,String value){
		String old= SPHelper.getDetailMsg(this, key, "");
		return !old.equals(value);
	}
	
	public void sendUsermsgChangeMsg(final String pmsg){
		IMEngine.getIMService(ConversationService.class).listConversations(new Callback<List<Conversation>>() {
			@Override
			public void onSuccess(List<Conversation> arg0) { 
				sendConversations(arg0,pmsg);
			}
			
			@Override
			public void onProgress(List<Conversation> arg0, int arg1) { 
				
			}
			
			@Override
			public void onException(String arg0, String arg1) { 
				
			}
		},Integer.MAX_VALUE, Conversation.ConversationType.GROUP|Conversation.ConversationType.CHAT);
	}
	
	void sendConversations(List<Conversation> conversations,final String pmsg){
		for(final Conversation item:conversations){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Message msg= IMEngine.getIMService(MessageBuilder.class).buildTextMessage(pmsg);
					msg.sendTo(item, new Callback<Message>() {
						@Override
						public void onSuccess(Message arg0) {
						}
						@Override
						public void onProgress(Message arg0, int arg1) { 
						}
						
						@Override
						public void onException(String arg0, String arg1) { 
						}
					});
				}
			}, 2000);
		}
	} 
}