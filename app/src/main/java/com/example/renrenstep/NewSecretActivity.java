package com.example.renrenstep;


import helper.BGHelper;
import helper.SPHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tools.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;

public class NewSecretActivity extends ActionBarActivity {
	private Button bt_next;
	private EditText et_set_secret, et_next_secret;
	private Dialog alertDialog; 
	private RelativeLayout layout_actionbar;
	private ImageView iv_newsecret1, iv_newsecret2; 
	private Dialog dialog;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			NewSecretActivity.this.finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_new_secret);
		
		initView();
	}
	
	private void initView() {
		String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX, getString(R.string.appsex_man));
		iv_newsecret1 = (ImageView)findViewById(R.id.iv_newsecret1);
		iv_newsecret2 = (ImageView)findViewById(R.id.iv_newsecret2);
		View logView = LayoutInflater.from(this).inflate(R.layout.dialog_reset_secret, null);
		TextView tv_reset_secret = (TextView)logView.findViewById(R.id.tv_reset_secret);
		tv_reset_secret.setTextColor(getResources().getColor(BGHelper.setBackground(this, sex)));
		alertDialog = new AlertDialog.Builder(this). 
                setView(logView). 
                create(); 
		bt_next = (Button)findViewById(R.id.bt_next);
		dialog=CommHelper.createLoadingDialog(this, "","F");
		bt_next.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String secret1 = et_set_secret.getText().toString().trim();
				String secret2 = et_next_secret.getText().toString().trim();
				if(secret1.equals("")||secret2.equals("")){
					ToastManager.show(NewSecretActivity.this,getResources().getString(R.string.scritisnull),2000);
					return;
				}
				
				if(secret1.equals(secret2)){
					if(secret1.length()<6){
						ToastManager.show(NewSecretActivity.this,getResources().getString(R.string.minpwdlen),2000);
						return;
					}
					Map<String,Object> checks = new HashMap<String, Object>();
					checks.put("pwd", secret1);
					dialog.show();
					new BaseAsyncTask(Cons.CHANGE_CODE, checks, BaseAsyncTask.HttpType.Post, "" ,NewSecretActivity.this) {
						@Override
						public void handler(String param) {
							Log.i("resetsecret", param+"    修改密码");
							if(param!=null){
								try {
									JSONObject jsonObject = new JSONObject(param);
									int status = jsonObject.getInt("status");
									if(status==0){
										dialog.dismiss();
										//发送成功
										alertDialog.show();
										TimerTask task = new TimerTask() { 
											@Override
											public void run() {
												// TODO Auto-generated method stub
												Message msg = new Message();
												handler.sendMessage(msg);
											}
										}; 
										Timer timer = new Timer();
										timer.schedule(task, 1500);
									}else{
										dialog.dismiss();
										ToastManager.show(NewSecretActivity.this, "失败",1000);
									}
								} catch (JSONException e) {
									dialog.dismiss();
									ToastManager.show(NewSecretActivity.this,getResources().getString(R.string.wangluoyic),2000);
								}
							}else{
								dialog.dismiss();
								ToastManager.show(NewSecretActivity.this,getResources().getString(R.string.wangluoyic),2000);
							}
						}
					}.execute("");
				}else{ 
					ToastManager.show(NewSecretActivity.this,getResources().getString(R.string.confirm_secret),2000);
				}
			}
		});
		et_set_secret =(EditText)findViewById(R.id.et_set_secret);
		et_next_secret =(EditText)findViewById(R.id.et_next_secret); 
	} 
}








