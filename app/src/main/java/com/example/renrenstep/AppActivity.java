package com.example.renrenstep;
 
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import comm.CommHelper;
import helper.SPHelper;
import tools.ToastManager;
public class AppActivity extends LoginActivity implements OnClickListener {
//	private static final int LOGIN_CODE = 0;
//	private static final int GET_INFO = 1;
//	private FrameLayout layout_app; 
	private Button bt_login;
	private LinearLayout ll_reset_mid, ll_reset_secret;
	private EditText et_mid, et_pwd;
	private Dialog dialog; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app);
		init();
		checkIsLogined();
	}
	void checkIsLogined() {
		int login = SPHelper.getBaseMsg(this, "login", 1);
		if (login == 1) {
			Intent itent = new Intent(this, LeaderActivity.class);
			startActivity(itent);
			finish();
		}
	}
	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	private void init() {
		et_mid = (EditText) findViewById(R.id.et_mid);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
//		layout_app = (FrameLayout) findViewById(R.id.layout_app);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_login.setOnClickListener(this);
		ll_reset_mid = (LinearLayout) findViewById(R.id.ll_reset_mid);
		ll_reset_mid.setOnClickListener(this);
		ll_reset_secret = (LinearLayout) findViewById(R.id.ll_reset_secret);
		ll_reset_secret.setOnClickListener(this);
		dialog = CommHelper.createLoadingDialog(this, "", "F");  
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_reset_mid:
			Intent regist = new Intent(this, RegisterActivity.class);
			startActivity(regist); 
			break;
		case R.id.ll_reset_secret:
			Intent reset = new Intent(this, ResetSecritActivity.class);
			startActivity(reset);
			break;
		case R.id.bt_login:
			dialog = CommHelper.createLoadingDialog(AppActivity.this, "", "F");
			dialog.show();
			String uid = et_mid.getText().toString();
			String pwd = et_pwd.getText().toString();
			if (uid.equals("") || pwd.equals("")) {
				dialog.dismiss();
				ToastManager.show(AppActivity.this,getResources().getString(R.string.userpwderror), 2000);
				return;
			}
			checkLoginMsg(uid, pwd);
			break;
		default:
			break;
		}
	} 
	
	@Override
	protected void login_Fail() {
		dialog.dismiss();
		ToastManager.show(AppActivity.this,getResources().getString(R.string.userpwderror), 2000);
	}
	 
	@Override
	protected void login_Success() {
		dialog.dismiss();
		Intent main = new Intent(AppActivity.this, MainActivity.class);
		startActivity(main);
		finish();
	}
	
	@Override
	protected void login_Exception() {
		exception_msg();
	}
	
	void exception_msg(){
		dialog.dismiss();
		ToastManager.show(AppActivity.this, getResources().getString(R.string.wangluoyic), 1000);	
	}
	
}
