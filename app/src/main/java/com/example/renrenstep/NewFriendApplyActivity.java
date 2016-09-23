package com.example.renrenstep;

import helper.BGHelper;
import helper.SPHelper;

import java.util.HashMap;
import java.util.Map;

import tools.ToastManager;

import org.json.JSONObject;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.Conversation.ConversationType;

import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;
import bean.Customer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewFriendApplyActivity extends Activity implements OnClickListener {
	private EditText edit_txt;
	private Customer user;
	private Conversation conversation;
	private int  resultCode = 0;
	private RelativeLayout layouter;
	private Dialog dialoging;
	private String gender="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_friend_apply);
		initView();
		initData();
		initEvent();
		initColor();
	}
	
	void initView(){
		layouter = (RelativeLayout) findViewById(R.id.layouter);
		edit_txt=(EditText)findViewById(R.id.edit_txt);
	}
	
	void initData(){
		Intent intent = this.getIntent();
		user = (Customer) intent.getSerializableExtra("key");
		gender=SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
	}
	
	void initEvent(){
		((LinearLayout)findViewById(R.id.covert_left_dao)).setOnClickListener(this);
		((TextView)findViewById(R.id.cleartxt)).setOnClickListener(this); 
		((LinearLayout)findViewById(R.id.send_msg)).setOnClickListener(this); 
	}
	
	void initColor(){
		layouter.setBackgroundResource(BGHelper.setBackground(this, SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man))));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.covert_left_dao:
//			Intent mIntent = new Intent();
//	        mIntent.putExtra("key", "no");  
//	        NewFriendApplyActivity.this.setResult(resultCode, mIntent);
			finishApplay(); 
			break; 
		case R.id.cleartxt:
			edit_txt.setText("");
			break;
		case R.id.send_msg:
			dialoging=CommHelper.createLoadingDialog(this, "", gender); 
			dialoging.show();
			sendApplayToNewFriend(edit_txt.getText().toString());
			break;
		default:
			break;
		}
	}
	void sendApplayToNewFriend(String arg0) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", user.getId());
		maps.put("words",arg0);
		new BaseAsyncTask(Cons.APPLYURL, maps, BaseAsyncTask.HttpType.Post, "", this) {
			@Override
			public void handler(String param) {
				// TODO Auto-generated method stub
				dialoging.dismiss();
				JSONObject jsonobj;
				if (param == null || param.equals("")|| !param.contains("status")) {
					ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.wangluoyic), 2000);
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if (jsonobj.getInt("status") == 0) {
						Intent mIntent = new Intent();
				        mIntent.putExtra("key", "ok");
				        NewFriendApplyActivity.this.setResult(resultCode, mIntent);  
						finish();
					}else if(jsonobj.getInt("status") == 1){
						ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.isyourself), 2000);
					}else if(jsonobj.getInt("status") == 2){
						ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.wasyourfriend), 2000);
					}else if(jsonobj.getInt("status") == 3){
						ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.hasaddtheman), 2000);
					}
				} catch (Exception ex) {
					ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.wangluoyic), 2000);
				}
			}
		}.execute("");
	}
	
	/*void sendApplayToNewFriend(String arg0) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", user.getId());
		maps.put("words",arg0);
		new BaseAsyncTask(Cons.APPLYURL, maps, HttpType.Get, "", this) {
			@Override
			public void handler(String param) {
				// TODO Auto-generated method stub
				JSONObject jsonobj;
				if (param == null || param.equals("")|| !param.contains("status")) {
					ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.wangluoyic), 2000);
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if (jsonobj.getInt("status") == 0) {
						ToastManager.show(NewFriendApplyActivity.this,getResources().getString(R.string.addsuccess),2000);
						creareConversation();
						Intent mIntent = new Intent();  
				        mIntent.putExtra("key", "ok");
				        // 设置结果，并进行传送  
				        NewFriendApplyActivity.this.setResult(resultCode, mIntent);  
						finish();
					}
				} catch (Exception ex) {
					ToastManager.show(NewFriendApplyActivity.this, getResources().getString(R.string.wangluoyic), 2000);
				}
			}
		}.execute("");
	}*/
	
	void creareConversation() {
		Message alikongmsg = IMEngine.getIMService(MessageBuilder.class).buildTextMessage("add"); // 系统消息
		IMEngine.getIMService(ConversationService.class).createConversation(
				new Callback<Conversation>() {
					@Override
					public void onSuccess(Conversation arg0) {
						// TODO Auto-generated method stub
						//Toast.makeText(NewFriendApplyActivity.this,"成功1:" + arg0.conversationId(), 3000).show();
						conversation = arg0;
						sendMsgToFriend();
					} 
					@Override
					public void onProgress(Conversation arg0, int arg1) {
						// TODO Auto-generated method stub
					} 
					@Override
					public void onException(String arg0, String arg1) {
						// TODO Auto-generated method stub
					}
				}, Cons.ADDSINGAL, "", alikongmsg, ConversationType.UNKNOWN,(long) user.getId());
	}
	
	void sendMsgToFriend() {
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage("add");
		message.sendTo(conversation, new Callback<Message>() {
			@Override
			public void onSuccess(Message arg0) {
				//Toast.makeText(NewFriendApplyActivity.this,getResources().getString(R.string.sendsccess), 4000).show();
			}
			
			@Override
			public void onProgress(Message arg0, int arg1) { // TODO

			}
			
			@Override
			public void onException(String arg0, String arg1) {
				Toast.makeText(NewFriendApplyActivity.this,getResources().getString(R.string.senderror), 4000).show();
			}
		});
	}
	void finishApplay(){
		Intent mIntent = new Intent();  
        mIntent.putExtra("key", "no");  
        NewFriendApplyActivity.this.setResult(resultCode, mIntent);  
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==event.KEYCODE_BACK){
			finishApplay();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
