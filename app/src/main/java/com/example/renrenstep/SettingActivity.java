package com.example.renrenstep;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.ToastManager;

import org.json.JSONObject;

import service.SimpleStepService;
import service.TalkingMessageService;
import bean.ServiceData;
import bean.ServiceReturnData;
import bean.ServiceStepData;

import com.alibaba.wukong.auth.AuthService;
import com.alibaba.wukong.im.IMEngine;
import com.google.gson.Gson;

import helper.SPHelper;
import helper.UpdateManager;
import helper.BaseAsyncTask;
import comm.CommHelper;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.SqlLiteManager;
import constant.Cons;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SettingActivity extends Activity implements OnClickListener { 
	private Button bt_exit;
	private LinearLayout layout_dev_updata,linear_about, set_recommed, devlayout,layout_updata,ll_back;
	private ImageView iv_toggle;
	private String gender; 
	private TextView bindtitle,bindstate,tv_updata;
	private EditText bindnumber;
	private Button bind_cancel, bind_confirm;
	private Dialog binddialog;
	private SqlLiteManager stepManger; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_menu);
		initView(); 
		initEvent();
	}
	
	void initView(){
		gender=SPHelper.getDetailMsg(this, Cons.APP_SEX, "M");
		iv_toggle=(ImageView)findViewById(R.id.iv_toggle);
		//int id= R.string.demodemo;
		((ImageView) findViewById(R.id.image1)).setImageResource(isMan()? R.drawable.setting_step_blue: R.drawable.setting_step_red);
		((ImageView) findViewById(R.id.image2)).setImageResource(isMan()? R.drawable.setting_recommend_blue: R.drawable.setting_recommend_red);
		((ImageView) findViewById(R.id.image3)).setImageResource(isMan()? R.drawable.setting_about_blue: R.drawable.setting_about_red);
		((ImageView) findViewById(R.id.image4)).setImageResource(isMan()? R.drawable.setting_update_blue: R.drawable.setting_update_red);
		((ImageView) findViewById(R.id.image5)).setImageResource(isMan()? R.drawable.blue_bind_ioc: R.drawable.red_bind_ioc);
		RelativeLayout layout_actionbar=(RelativeLayout)findViewById(R.id.layout_actionbar);
		layout_actionbar.setBackgroundResource(isMan()?R.color.appcolor_blue:R.color.appcolor_red);
		linear_about = (LinearLayout)findViewById(R.id.linear_about);
		set_recommed = (LinearLayout)findViewById(R.id.set_recommed);
		layout_dev_updata = (LinearLayout) findViewById(R.id.layout_dev_updata);
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		bindstate = (TextView)findViewById(R.id.tv_show_bind_msg);
		tv_updata= (TextView)findViewById(R.id.tv_updata);
		stepManger = new SqlLiteManager(new DbSqlLite(this,new DatabaseHelper(this)));
		layout_updata = (LinearLayout)findViewById(R.id.layout_updata);
		bt_exit=(Button)findViewById(R.id.bt_exit);
		initToggleView(); 
		initBindStateView();
		int versionStatus = SPHelper.getBaseMsg(this, "versionStatus",-1);
		if (versionStatus == 2 || versionStatus == 3) {
			tv_updata.setTextColor(0xffff0000);
			layout_updata.setOnClickListener(this);
		} else {
			tv_updata.setText(getResources().getString(R.string.noupdate));
		}
	}
	void initToggleView(){
		 boolean sel=SPHelper.getDetailMsg(this, "isrun", true);
		 iv_toggle.setImageResource(sel?isMan()?R.drawable.toggle_blue:R.drawable.toggle_red:R.drawable.toggle_normal);
	}
	
	void initBindStateView(){
		String bindtyp=SPHelper.getBaseMsg(this, "bindkey", "");
		if(bindtyp.equals("")||bindtyp.equals("3")){
			bindstate.setText(getResources().getString(R.string.no_bind));
		}else{
			bindstate.setText(getResources().getString(R.string.has_bind));
		}
	}
	
	boolean isMan(){
		return gender.equals("M");
	}
	
	void initEvent(){
		iv_toggle.setOnClickListener(this);
		set_recommed.setOnClickListener(this);
		linear_about.setOnClickListener(this);
		layout_dev_updata.setOnClickListener(this);//layout_dev_updata
		bt_exit.setOnClickListener(this);
		ll_back.setOnClickListener(this);
	}
	View initBindView() {
		View bindview = LayoutInflater.from(this).inflate(R.layout.bind_device, null);
		bindtitle = (TextView) bindview.findViewById(R.id.bindtitle);
		bindnumber = (EditText) bindview.findViewById(R.id.bindnumber);
		bind_cancel = (Button) bindview.findViewById(R.id.cancel);
		bind_confirm = (Button) bindview.findViewById(R.id.confirm);
		return bindview;
	}
	void initViewTheme(){
		String gender=SPHelper.getDetailMsg(this, Cons.APP_SEX, "M");
		bind_confirm.setBackgroundDrawable(getResources().getDrawable(gender.equals("M")?R.drawable.bg_bt_blue:R.drawable.bg_bt_red));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_toggle:
			boolean isrun=SPHelper.getDetailMsg(this, "isrun", true);
			SPHelper.setDetailMsg(this, "holdproc", isrun);
			SPHelper.setDetailMsg(this, "isrun", !isrun);
			initToggleView();
			break;
		case R.id.set_recommed:
			CommHelper.insert_visit(this, "recommentpg");
			Intent intent = new Intent(this, ReportActivity.class);
			intent.putExtra("pagenm",getResources().getString(R.string.setting_item2));
			intent.putExtra("gender",SPHelper.getDetailMsg(this, Cons.APP_SEX, "M"));
			intent.putExtra("url", Cons.RECOMMEN_URL);
			startActivity(intent);
			break;
		case R.id.linear_about:
			Intent abountitent = new Intent(this,AboutActivity.class);
			startActivity(abountitent);
			break;
		case R.id.layout_dev_updata:
			View bindview=initBindView();
			initViewTheme();
			initBindData();
			showBindDeviceDialog(bindview);
			break;
		case R.id.layout_updata:
			UpdateManager mUpdateManager = new UpdateManager(this);
			mUpdateManager.checkUpdateInfo();
			break;
		case R.id.ll_back:
			finish();
			break;
		case R.id.bt_exit:
			showAlertdialog();
//			HandlerThread thread=new HandlerThread("123");
//			thread.start();
//			Handler handler=new Handler(thread.getLooper());
//			handler.post(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(SettingActivity.this, "1111111", 2000).show();
//				}
//			});
			break;
		default:
			break;
		}
	}
	
	void initBindData() {
		final String devtyp = SPHelper.getBaseMsg(this, "bindkey", "");
		final String devcode = SPHelper.getBaseMsg(this, "bindval", "");
		if (!devtyp.equals("")&&!devtyp.equals("3")) {
			bindtitle.setText(getResources().getString(R.string.unbind_title));
			bind_confirm.setVisibility(View.GONE);
			bindnumber.setText(devcode);
			
			bind_cancel.setText(getResources().getString(R.string.btn_unbind));
			bindnumber.setEnabled(false);
			bind_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					oprationDev(devtyp, devcode, Cons.UNBIND_DEVICE,false);
				}
			});
			WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			int height = wm.getDefaultDisplay().getHeight();
		    int mywidth  = (width - 150)/2-40; // 设置宽度
		    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.width=mywidth; 
		    bind_cancel.setLayoutParams(params);
			
		} else {
			bind_confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String ncode = bindnumber.getText().toString();
					oprationDev("1", ncode, Cons.BIND_DEVICE,true);
				}
			});
			bind_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (binddialog != null) {
						binddialog.dismiss();
					}
				}
			});
		}
	} 
	
	void oprationDev(final String typ, final String code, String purl,
			final boolean isbind) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("devicetype_cd", typ);
		maps.put("device_no", code);
		new BaseAsyncTask(purl, maps, BaseAsyncTask.HttpType.Post, "", this) {
			@Override
			public void handler(String param) {
				// TODO Auto-generated method stub
				try {
					if (param != null && param.contains("status")) {
						JSONObject json = new JSONObject(param);
						if (json.getInt("status") == 0) {
							if (isbind) {
								SPHelper.setBaseMsg(SettingActivity.this, "bindkey",typ);
								SPHelper.setBaseMsg(SettingActivity.this, "bindval",code);
								down_data(); 
							} else {
								SPHelper.setBaseMsg(SettingActivity.this, "bindkey","3");
								SPHelper.setBaseMsg(SettingActivity.this, "bindval","");
							} 
							initBindStateView();
							binddialog.dismiss();
//							Intent intent=new Intent(SettingActivity.this,TalkingMessageService.class);
//							SettingActivity.this.stopService(intent);
						} else { 
							ToastManager.show(SettingActivity.this,json.getString("description"), 2000);
						}
					} else {
						ToastManager.show(SettingActivity.this, getResources().getString(R.string.wangluoyic), 2000);
					}
				} catch (Exception ex) {
					ToastManager.show(SettingActivity.this,getResources().getString(R.string.wangluoyic), 2000);
				}
			}
		}.execute("");
	}
	String getDate() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(5, -7);
		return gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-"+ gc.get(Calendar.DAY_OF_MONTH);
	}
	
	void down_data() {
		try {
			String storeLastDay=SPHelper.getDetailMsg(this , "downday","");
			String formart_date = storeLastDay.equals("")?getDate():storeLastDay;
			final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			final long pointer = format.parse(formart_date).getTime();
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("time", formart_date); 
			new BaseAsyncTask(Cons.DOWN_DATA, maps, BaseAsyncTask.HttpType.Get, "",this) {
				@Override
				public void handler(String param) {
					if (param != null && param.contains("status")) {
						ServiceReturnData data = new ServiceReturnData();
						Gson json = new Gson();
						data = json.fromJson(param, data.getClass());
						stepManger.DelStepHisData(pointer);
						storeData(data.getData());
						SPHelper.setDetailMsg(SettingActivity.this, "downday",format.format(new Date()));
						long storeval= CommHelper.getMaxValue(data.getData());
						SPHelper.getDetailMsg(SettingActivity.this, "lastval",storeval);
						//initMainPage();
					}
				}
			}.execute("");
		} catch (Exception ex) {
			
		}
	}
	
	void storeData(List<ServiceData> pdatas){
		for(ServiceData item: pdatas){
			List<ServiceStepData> stepdatas= item.getSmds();
			for(ServiceStepData pitem:stepdatas){
				insertServiceStepData(pitem);
			}
		}
	}
	
	void insertServiceStepData(ServiceStepData pdata){
		Date date=new Date(pdata.getCollect_time()*1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int myear= cal.get(Calendar.YEAR);
		int mth= cal.get(Calendar.MONTH)+1;
		int mday= cal.get(Calendar.DAY_OF_MONTH);
		int mhour= cal.get(Calendar.HOUR_OF_DAY);
		int mins= cal.get(Calendar.MINUTE);
		stepManger.Insert(myear,mth,mday,mhour,mins,pdata.getSteps(),pdata.getCollect_time()*1000, SPHelper.getBaseMsg(this, "mid", ""));
	}
	
	void showBindDeviceDialog(View view) {
		binddialog = new Dialog(this,R.style.DialogStyle);
		binddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		binddialog.setCanceledOnTouchOutside(true); 
		binddialog.setContentView(view);
		WindowManager.LayoutParams lp = binddialog.getWindow().getAttributes();
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		lp.width = width - 150; // 设置宽度
		lp.height = height / 3 + 26;
		binddialog.getWindow().setAttributes(lp);  
		binddialog.show();
	}
	
	void loginOut(){
		AuthService authService = IMEngine.getIMService(AuthService.class);
		authService.logout();
		Intent talkingService=new Intent(SettingActivity.this,TalkingMessageService.class);
		SettingActivity.this.stopService(talkingService);
	}
	
	protected void showAlertdialog() {
			Builder builder = new Builder(this);
			builder.setMessage(getResources().getString(R.string.out_msg));
			builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					SPHelper.setDetailMsg(SettingActivity.this, "ischecked", "");
					SPHelper.setBaseMsg(SettingActivity.this, "mtoken", ""); 
					if (!SPHelper.getDetailMsg(SettingActivity.this, "isrun", true)) {
						SPHelper.setDetailMsg(SettingActivity.this, "holdproc", false);
						Intent service = new Intent(SettingActivity.this,SimpleStepService.class); 
						SettingActivity.this.stopService(service);
					} else {
						SPHelper.setDetailMsg(SettingActivity.this, "holdproc", true);
					}
					MainActivity.mainpage.finish();
					loginOut();
					
					Intent intent = new Intent(SettingActivity.this, AppActivity.class);
					SettingActivity.this.startActivity(intent);
					SettingActivity.this.finish();
				}
			});
			
			builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			builder.show();
		}
	@Override
	protected void onResume() {
			// TODO Auto-generated method stub
			if(SPHelper.getDetailMsg(SettingActivity.this, "ischecked", "").equals("")){
				Intent intent = new Intent(SettingActivity.this, AppActivity.class);
				startActivity(intent);
			}
			super.onResume();
	}
}
