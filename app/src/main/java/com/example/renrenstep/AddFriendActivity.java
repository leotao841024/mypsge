package com.example.renrenstep;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import tools.ToastManager;
import org.json.JSONObject;
import tools.FileUtils;
import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.Conversation.ConversationType;
import com.alibaba.wukong.im.Message;
import helper.BaseAsyncTask;
import bean.Customer;
import helper.BGHelper;
import helper.SPHelper;
import constant.Cons;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory; 
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener; 
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendActivity extends Activity implements OnClickListener {
	private RelativeLayout layouter;
	private TextView address, name, email, title;
	private view.CircleImageView img_left;
	private Customer user;
	private Conversation conversation;
	private Button addbtn;
	private Handler handler=new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_friend);
		initView();
		initData();
		initColor();
		initEvent();
		//alterRest();
	}
	
	void initView() {
		layouter = (RelativeLayout) findViewById(R.id.layouter);
		address = (TextView) findViewById(R.id.address);
		name = (TextView) findViewById(R.id.name);
		email = (TextView) findViewById(R.id.email);
		title = (TextView) findViewById(R.id.title);
		img_left = (view.CircleImageView) findViewById(R.id.img_left);
		addbtn=((Button) findViewById(R.id.addbtn));
	}

	void initData() {
		Intent intent = this.getIntent();
		user = (Customer) intent.getSerializableExtra("key");
		address.setText(user.getCity_alia());
		name.setText(user.getNc());
		title.setText(user.getNc());
		email.setText(user.getEmail());
		FileUtils filetools = new FileUtils("stepic");
		filetools.createSDDir();
		String basepath = filetools.getFilePath() + user.getAvatar();
		File file = new File(basepath);
		if (file.exists()) {
			img_left.setImageBitmap(BitmapFactory.decodeFile(basepath));
		}
	}

	void initColor() {
		String gender=SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
		layouter.setBackgroundResource(BGHelper.setBackground(this,gender));
		addbtn.setBackgroundResource(gender.equals("M")?R.drawable.btn_add_friend_blue:R.drawable.btn_add_friend_red);
	}
	
	void initEvent() {
		((LinearLayout) findViewById(R.id.covert_left_dao)).setOnClickListener(this);
		addbtn.setOnClickListener(this);
	}
	
	private Dialog mindialog;
	void alterRest() { 
		mindialog = new Dialog(this);
		mindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mindialog.setCanceledOnTouchOutside(false); 
		View view = LayoutInflater.from(this).inflate(R.layout.cutdown_pg,null); 
		mindialog.setContentView(view);
		WindowManager.LayoutParams lp = mindialog.getWindow().getAttributes();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); 
		Window dialogWindow = mindialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER); 
		mindialog.getWindow().setAttributes(lp);
		mindialog.show();  
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.covert_left_dao:
			finish();
			break;
		case R.id.addbtn:
			//sendApplayToNewFriend("123123");
			Intent intent=new Intent(this,NewFriendApplyActivity.class);
			intent.putExtra("key", user);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	
	void sendApplayToNewFriend(String arg0) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", user.getId());
		maps.put("words",arg0);
		new BaseAsyncTask(Cons.APPLYURL, maps, BaseAsyncTask.HttpType.Get, "", this) {
			@Override
			public void handler(String param) {
				// TODO Auto-generated method stub
				JSONObject jsonobj;
				if (param == null || param.equals("")|| !param.contains("status")) {
					ToastManager.show(AddFriendActivity.this, getResources().getString(R.string.wangluoyic), 2000);
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if (jsonobj.getInt("status") == 0) {
						ToastManager.show(AddFriendActivity.this,getResources().getString(R.string.addsuccess),2000);
						//creareConversation();
						//Intent mIntent = new Intent();  
				        //mIntent.putExtra("key", "ok");
				        // 设置结果，并进行传送  
				        //AddFriendActivity.this.setResult(resultCode, mIntent);  
						finish();
					}
				} catch (Exception ex) {
					ToastManager.show(AddFriendActivity.this, getResources().getString(R.string.wangluoyic), 2000);
				}
			}
		}.execute("");
	}
	
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
				Toast.makeText(AddFriendActivity.this,getResources().getString(R.string.senderror), Toast.LENGTH_LONG).show();
			}
		});
	} 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0&&resultCode==0&&data.getStringExtra("key").equals("ok")){
			//弹出层显示
			alterRest();
			handler.postDelayed(new Runnable() { 
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mindialog.dismiss();
				}
			}, 3000);
			//ToastManager.show(AddFriendActivity.this, getResources().getString(R.string.sendsccess),3000);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
//	void sendmsgToUser() {
//		Message msg = messageService.getTextMessage("createconvert");
////		convertService.createConversation(new ConverHandler(), "addmem", "",msg, ConversationType.UNKNOWN, (long) user.getId());
//	}
//
//	void sendMsgToUser() {
//		if (conversation != null) {
//			Message msg = messageService.getTextMessage("helloworld");
//			msg.sendTo(conversation, new Callback<Message>() {
//				@Override
//				public void onSuccess(Message arg0) {
//					ToastManager.show(AddFriendActivity.this, "发送消息成功", 2000);
//				}
//
//				@Override
//				public void onProgress(Message arg0, int arg1) {
//
//				}
//
//				@Override
//				public void onException(String arg0, String arg1) {
//					ToastManager.show(AddFriendActivity.this, "发送消息失败", 2000);
//				}
//			});
//		}
//	}

//	class ConverHandler implements ICreateConvert {
//		@Override
//		public void handlerSuccess(Conversation arg0) {
//			// TODO Auto-generated method stub
//			conversation = arg0;
//			//ToastManager.show(AddFriendActivity.this, arg0.conversationId()+ "", 6000);
//		}
//
//		@Override
//		public void handlerProcess(Conversation arg0, int arg1) {
//		}
//
//		@Override
//		public void handlerException(String arg0, String arg1) {
//			// TODO Auto-generated method stub
//			ToastManager.show(AddFriendActivity.this, arg0 + "|" + arg1, 2000);
//		}
//	}
}
