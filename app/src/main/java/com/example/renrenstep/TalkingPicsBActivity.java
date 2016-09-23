package com.example.renrenstep; 
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationChangeListener;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageContent;
import com.alibaba.wukong.im.MessageListener;
import com.alibaba.wukong.im.MessageService;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import comm.CommHelper;
import helper.SPHelper;
import view.EmojiconTextView;

public class TalkingPicsBActivity extends Activity {
	private static final int ALBUM_RQUEST = 100;
	private Message message;
	private Button btn_face;
	private EmojiconTextView textView;
	private ImageView imageMsg;
	private ConversationService conversationService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talking_demo);
		initView();
	    initWkLogion();
		//initConversationChangeListner();
		//registerReceiver();

	}

	ConversationChangeListener conversationChange = new ConversationChangeListener(){
		public void onStatusChanged(List<Conversation> list) {

		};
	};

	private void initView() {
		btn_face = (Button) findViewById(R.id.btn_face);
		imageMsg = (ImageView) findViewById(R.id.imageMsg);
		textView = (EmojiconTextView) findViewById(R.id.txtEmojicon);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void initWkLogion( ) {
		long oldopenId = AuthService.getInstance().latestAuthInfo().getOpenId();
		if (oldopenId != 0) {
			IMEngine.getIMService(AuthService.class).autoLogin(oldopenId);
			registerReceiver();
		} else {
			AuthService authService = IMEngine.getIMService(AuthService.class);
			Random random = new Random();
			String nonce = random.nextInt(6) + "";// 随机数
			long timestamp = System.currentTimeMillis() / 1000;// 时间戳
			String mid = SPHelper.getBaseMsg(this, "mid", "");
			long openId = mid.equals("") ? 0 : Long.parseLong(mid);// 用户标识
			String appSecret = "SrmZ15rCH9d5CcqoxACQhVX35P3K3gO0";
			String appToken = "SAjSeMlnXXlA2SiPGCfOCGmfo6hDsNpN";
			String signature = getSignature(appToken, appSecret, openId, nonce,timestamp);
			ALoginParam params = buildLoginParam(openId, nonce, timestamp,signature);
			authService.login(params, new Callback<AuthInfo>() {
				@Override
				public void onSuccess(AuthInfo arg0) {
					registerReceiver();
				}

				@Override
				public void onProgress(AuthInfo arg0, int arg1) {

				}

				@Override
				public void onException(String arg0, String arg1) {

				}
			});
		}
	}

	ALoginParam buildLoginParam(long openId, String nonce, long timestamp,
								String signature) {
		ALoginParam param = new ALoginParam();
		param.domain = "ZouKu";
		param.openId = openId;// 用户id
		param.nonce = nonce;// 随机数
		param.timestamp = timestamp;// 时间戳 System.currentTimeMillis() / 1000
		param.signature = signature;
		return param;
	}
	String getSignature(String appToken, String appSecret, long openId,
						String nonce, long timestamp) {
		List<String> slist = new ArrayList<String>();
		slist.add(appToken);
		slist.add(appSecret);
		slist.add("" + openId);
		slist.add(nonce);
		slist.add("" + timestamp);
		Collections.sort(slist);
		StringBuilder builder = new StringBuilder();
		for (String item : slist) {
			builder.append(item);
		}
		return new String(CommHelper.sha256Hex(builder.toString()));
	}

	private void registerReceiver() {
		IMEngine.getIMService(MessageService.class).addMessageListener(mMessageListener);
	}


	private MessageListener mMessageListener = new MessageListener() {

		@Override
		public void onAdded(List<Message> list, DataType dataType) {
			Log.e("MessageListener", "onAdded");

				message = list.get(0);
				/*MessageContent.ImageContent msgContent = (MessageContent.ImageContent) message.messageContent();
				String remoteUrl = msgContent.url();

				if (TextUtils.isEmpty(remoteUrl)) Log.e("MessageListener","bitmap is null");
				else{
					Glide.with(TalkingPicsBActivity.this).load(remoteUrl).into(imageMsg);
				}*/
			MessageContent.TextContent msgContent = (MessageContent.TextContent) message.messageContent();
			textView.setText(msgContent.text()+"-----extension:"+message.extension("extension"));

		}

		@Override
		public void onRemoved(List<Message> list) {
		}

		@Override
		public void onChanged(List<Message> list) {
			Log.e("MessageListener", "onChanged");
			/*message = list.get(0);
			MessageContent.ImageContent msgContent = (MessageContent.ImageContent) message.messageContent();
			String remoteUrl = msgContent.url();
			if (TextUtils.isEmpty(remoteUrl)) Log.e("MessageListener", "bitmap is null");
			else {
				Glide.with(TalkingPicsBActivity.this).load(remoteUrl).into(imageMsg);
			}*/
		}

	};
}
