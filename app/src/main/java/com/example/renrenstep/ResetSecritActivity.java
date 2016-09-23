package com.example.renrenstep;

import helper.SPHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;

public class ResetSecritActivity extends ActionBarActivity implements
		OnClickListener {

	private Button bt_code, bt_next;
	private EditText et_userid, et_code;
	private RelativeLayout layout_actionbar;
	private int times;
	private Timer timer;
	private TimerTask task;
	public static final int SEND_CODE = 0;
	public static final int STOP_CODE = 1; 
	private Dialog dialog;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_CODE:
				bt_code.setText(String.valueOf(times)+getResources().getString(R.string.send_time));
				break;
			case STOP_CODE:
				timer.cancel();
				bt_code.setClickable(true);
				bt_code.setText(getResources().getString(R.string.send_code));
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_reset_secrit); 
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		bt_code = (Button) findViewById(R.id.bt_code);
		bt_code.setOnClickListener(this);
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		et_userid = (EditText) findViewById(R.id.et_userid);
		et_code = (EditText) findViewById(R.id.et_code);
		layout_actionbar = (RelativeLayout)findViewById(R.id.layout_actionbar); 
		//ImageView iv_secret_exit=(ImageView)findViewById(R.id.iv_secret_exit);
		//iv_secret_exit.setOnClickListener(this);
		dialog=CommHelper.createLoadingDialog(this, "","F"); 
		LinearLayout ll_secret_exit=(LinearLayout)findViewById(R.id.ll_secret_exit);
		ll_secret_exit.setOnClickListener(this); 
	}
	private void sendCode(){
		times = 60;
		bt_code.setClickable(false);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				--times;
				if (times > 0) {
					Message msg = new Message();
					msg.what = SEND_CODE;
					msg.arg1 = times;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = STOP_CODE;
					handler.sendMessage(msg);
				}
			}
		};
		timer.schedule(task, 1000, 1000);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_secret_exit:
			finish();
			break;
		case R.id.bt_code:
			if(!et_userid.getText().toString().equals("")){ 
				Map<String,Object> maps = new HashMap<String, Object>();
				maps.put("uid", et_userid.getText().toString().trim());
				dialog.show();
				new BaseAsyncTask(Cons.SEND_CODE, maps, BaseAsyncTask.HttpType.Post, "" ,this) {
					@Override
					public void handler(String param) {
						dialog.dismiss();
						Log.i("resetsecret", param+"   发送");
						if(param!=null){
							try {
								JSONObject jsonObject = new JSONObject(param);
								int status = jsonObject.getInt("status");
								if(status==0){
									//发送成功
									
									sendCode();
								}else{
									//发送失败 
									String description = jsonObject
											.getString("description");
									Toast.makeText(ResetSecritActivity.this,
											description + "", Toast.LENGTH_LONG)
											.show();
								}
								
							} catch (JSONException e) { 
								Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.getvalidloser), Toast.LENGTH_LONG)
										.show();
							}
						}else{ 
							Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.wangluoyic), Toast.LENGTH_LONG)
							.show();
						}
					}
				}.execute(""); 
			}else{
				Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.misspphone),2000).show();
			}
			break;
		case R.id.bt_next: 
			Map<String,Object> checks = new HashMap<String, Object>();
			checks.put("uid", et_userid.getText().toString().trim());
			checks.put("code", et_code.getText().toString().trim());
			if( et_userid.getText().toString().trim().equals("")){
				Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.misspphone),2000).show();
				return;
			}
			if(et_code.getText().toString().trim().equals("")){
				Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.validcode),2000).show();
				return;
			}
			if(et_code.getText().toString().trim().length()!=6){
				Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.validcderror),2000).show();
				//Toast.makeText(this, getResources().getString(R.string.validcderror), 1).show();
				return;
			}
			dialog.show();
			new BaseAsyncTask(Cons.CHECK_CODE, checks, BaseAsyncTask.HttpType.Post, "" ,this) {
				@Override
				public void handler(String param) {
					Log.i("resetsecret", param+"    找回");
					if(param!=null){
						try {
							dialog.dismiss();
							JSONObject jsonObject = new JSONObject(param);
							int status = jsonObject.getInt("status");
							if(status==0){
								//发送成功
								String mtoken = jsonObject.getString("mtoken");
								SPHelper.setBaseMsg(ResetSecritActivity.this,"mtoken", mtoken);
								Intent newSecret = new Intent(ResetSecritActivity.this, NewSecretActivity.class);
								startActivity(newSecret);
								ResetSecritActivity.this.finish();
							}else{
								String description = jsonObject.getString("description");
								Toast.makeText(ResetSecritActivity.this,description + "", 2000).show();
							}
						} catch (Exception e) { 
							dialog.dismiss();
							Toast.makeText(ResetSecritActivity.this,getResources().getString(R.string.wangluoyic), 2000).show();
						}
					}
				}
			}.execute("");  
			break;  
		default:
			break;
		}
	} 
}
