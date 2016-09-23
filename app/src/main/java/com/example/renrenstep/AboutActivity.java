package com.example.renrenstep;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tencent.mm.sdk.modelbiz.JumpToBizProfile;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import comm.CommHelper;
import constant.Cons;
import helper.BGHelper;
import helper.SPHelper;

public class AboutActivity extends Activity {
	
	private RelativeLayout layout_actionbar;
	private LinearLayout ll_back;
	private LinearLayout weixin,weibo;
	private TextView protocol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//sgetActionBar().hide();
		setContentView(R.layout.activity_about);
		initView();
		CommHelper.insert_visit(this,"aboutpg");
	}
	
	private void initView() {
		String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX,
				getString(R.string.appsex_man));
		layout_actionbar = (RelativeLayout) findViewById(R.id.layout_actionbar);
		layout_actionbar.setBackgroundResource(BGHelper
				.setBackground(this, sex));
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		protocol=(TextView)findViewById(R.id.protocol);
		protocol.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		protocol.getPaint().setAntiAlias(true);//抗锯齿 
		protocol.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommHelper.insert_visit(AboutActivity.this,"protocalpg");
				Intent intent=new Intent(AboutActivity.this,ReportActivity.class);
				intent.putExtra("pagenm", getResources().getString(R.string.app_name));
				intent.putExtra("gender", SPHelper.getDetailMsg(AboutActivity.this, Cons.APP_SEX, "M"));
				intent.putExtra("url", Cons.PROTOCOL); 
				AboutActivity.this.startActivity(intent);
			}
		});
		ll_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutActivity.this.finish();
			}
		});
		weixin = (LinearLayout) findViewById(R.id.weixin);
		weixin.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				CommHelper.insert_visit(AboutActivity.this,"wxcommnumpg");
				IWXAPI wxapi = WXAPIFactory.createWXAPI(AboutActivity.this,
						Cons.WEIXINGONGZHONGHAO, false);
				wxapi.registerApp(Cons.WEIXINGONGZHONGHAO); 
				JumpToBizProfile.Req req = new JumpToBizProfile.Req();
				req.toUserName = "renrenjiankang0"; //公众号原始ID
				req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE;
				req.extMsg = "extMsg";
				wxapi.sendReq(req);
			}
		});
		weibo = (LinearLayout) findViewById(R.id.weibo);
		weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AboutActivity.this,ReportActivity.class);
				intent.putExtra("pagenm",getResources().getString(R.string.selfdoctor_weibo));
				intent.putExtra("url", Cons.WEIBO);
				intent.putExtra("gender", SPHelper.getDetailMsg(AboutActivity.this, Cons.APP_SEX, "M"));
				startActivity(intent);
			}
		});

		TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		tv_phone.getPaint().setAntiAlias(true);//抗锯齿
		String[] split = tv_phone.getText().toString().split("-");
		final String phone = split[0]+split[1];
		tv_phone.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				Intent phoneIntent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phone)); 
				phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(phoneIntent);
			}
		});
		try
		{
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0); 
			TextView tv_version=(TextView)findViewById(R.id.tv_version);
			tv_version.setText(info.versionName);
		}catch(Exception ex){
			
		}
	}

}
