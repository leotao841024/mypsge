package com.example.renrenstep; 
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.alibaba.wukong.Callback;
import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.MessageContent;
import com.alibaba.wukong.im.MessageListener;
import com.alibaba.wukong.im.MessageService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import adapter.TalkingPicAdapter;
import comm.CommHelper;
import helper.SPHelper;
import tools.FileUtils;
import view.Emojicon;
import view.EmojiconEditText;
import view.EmojiconGridFragment;
import view.EmojiconTextView;
import view.EmojiconsFragment;

public class TalkingPicsActivity extends FragmentActivity implements OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
	private static final int ALBUM_RQUEST = 100;
	private Message message;
	private Button btn_face,btn_photo,btnMap,btn_send;
	private Conversation currentConversation;
	private ListView talkingListView;
	private TalkingPicAdapter adapter;
	private List<Message> messageList;
	private ImageView imageMsg;
	private FileUtils fileUtils;
	private Bitmap bitmap;
	private LinearLayout ll_options;
	EmojiconEditText mEditEmojicon;
	EmojiconTextView mTxtEmojicon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talking_demo);
		initView();
		initWkLogion();
		initConversation();
		registerReceiver();

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.emojicons, EmojiconsFragment.newInstance(false))
				.commit();

	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		EmojiconsFragment.input(mEditEmojicon, emojicon);
	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		EmojiconsFragment.backspace(mEditEmojicon);
	}

	private void initView() {
		btn_face = (Button) findViewById(R.id.btn_face);
		btn_face.setOnClickListener(this);
		btn_photo = (Button) findViewById(R.id.btn_photo);
		btn_photo.setOnClickListener(this);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);

		talkingListView = (ListView) findViewById(R.id.mlist);
		messageList = new ArrayList<Message>();
		imageMsg = (ImageView) findViewById(R.id.imageMsg);
		btnMap = (Button) findViewById(R.id.btn_map);
		btnMap.setOnClickListener(this);
		ll_options = (LinearLayout) findViewById(R.id.ll_options);

		mEditEmojicon = (EmojiconEditText) findViewById(R.id.content);
		mTxtEmojicon = (EmojiconTextView) findViewById(R.id.txtEmojicon);


		fileUtils =  new FileUtils("renrenstep");
	}

	private void initConversation() {
		/*String mid = SPHelper.getBaseMsg(this, "mid", "");
		long openId = Long.parseLong(mid);*/
		IMEngine.getIMService(ConversationService.class).createConversation(new Callback<Conversation>() {
			@Override
			public void onSuccess(Conversation conversation) {
				currentConversation = conversation;
				Log.e("createConversation", conversation.getPeerId() + "");
			}

			@Override
			public void onException(String s, String s1) {
				Log.e("createConversation", s + "---" + s1);
			}

			@Override
			public void onProgress(Conversation conversation, int i) {
				//168921  117434
			}
		}, "testPic", null, message, Conversation.ConversationType.CHAT, Long.parseLong("168921"));
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_face:

				ll_options.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_map:
				Intent mapIntent = new Intent(TalkingPicsActivity.this,MapDemoActivity.class);
				startActivityForResult(mapIntent, 200);
				break;
			case R.id.btn_photo:
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 100);
			case R.id.btn_send:
				sendTextMsg();
				break;
		}
	}

	private void sendTextMsg() {

		Map<String,String> extension = new HashMap<>();
		extension.put("extension","extensionValue");
		String textContent = mEditEmojicon.getText().toString();

		message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(textContent+"~~~");
		message.updateExtension(extension);
		mTxtEmojicon.setText(textContent);
		mEditEmojicon.setText("");
		message.sendTo(currentConversation, new Callback<Message>() {
			@Override
			public void onSuccess(Message message) {

			}

			@Override
			public void onException(String s, String s1) {

			}

			@Override
			public void onProgress(Message message, int i) {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == RESULT_OK && null != data) {

			//String originalPicPath = getRealPathFromURI(data.getData());
			String realPath = null;
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
			if (cursor == null) return;
			if(cursor.moveToFirst()){
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				realPath = cursor.getString(column_index);
			}
			cursor.close();
            Log.e("originalPath", realPath);
			bitmap = BitmapFactory.decodeFile(realPath);
			String filename = String.valueOf(System.currentTimeMillis())+".png";
			try {
				saveFile(bitmap,filename);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String picturePath = fileUtils.getFilePath()+filename;

			Log.e("compressedPicPath", picturePath);
			File file = new File(picturePath);
			//创建消息
			message = IMEngine.getIMService(MessageBuilder.class).buildImageMessage(picturePath, file.length(), 0);
			sendPictureMsg(message);

		}else if(requestCode == 200 && resultCode == RESULT_OK && null!= data){

			String  filePath = (String) data.getExtras().get("map_snapshot_path");
			Log.e("TalkingPicsActivity",filePath);
			File file = new File(filePath);
			message = IMEngine.getIMService(MessageBuilder.class).buildImageMessage(filePath, file.length(), 0);
			sendPictureMsg(message);
		}



	}
   //保存压缩后的图片
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		String path = fileUtils.getFilePath();
		File dirFile = new File(path);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
		bos.flush();
		bos.close();
	}

	//根据返回的Uri获取真实路径
	public String getRealPathFromURI(Uri contentUri) {
		String realPath = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

		if(cursor.moveToFirst()){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			realPath = cursor.getString(column_index);
		}
		cursor.close();
		return realPath;
	}
	private void sendPictureMsg(Message message) {

		message.sendTo(currentConversation, new Callback<Message>() {

			@Override
			public void onSuccess(Message message) {
				String remoteUrl = ((MessageContent.ImageContent) message.messageContent()).url();
				Log.e("TalkingPicsActivity", "sendImageSuccess ---remoteUrl: " + remoteUrl);
			}

			@Override
			public void onException(String s, String s1) {
				Log.e("TalkingPicsActivity", s + "---" + s1);
			}

			@Override
			public void onProgress(Message message, int i) {

			}
		});
	}


	private void registerReceiver() {
		IMEngine.getIMService(MessageService.class).addMessageListener(mMessageListener);
	}


	private MessageListener mMessageListener = new MessageListener() {

			@Override
			public void onAdded(List<Message> list, DataType dataType) {
				Log.e("MessageListener", "onAdded");
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
					Glide.with(TalkingPicsActivity.this).load(remoteUrl).into(imageMsg);
				}*/
			}

	};


	public void initWkLogion( ) {
		long oldopenId = AuthService.getInstance().latestAuthInfo().getOpenId();
		if (oldopenId != 0) {
			IMEngine.getIMService(AuthService.class).autoLogin(oldopenId);

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

}
