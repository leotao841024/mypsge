package com.example.renrenstep;


import helper.BGHelper;
import helper.SPHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import service.SimpleStepService;
import tools.StepTool;
import view.HomeColumnar;
import view.HomeColumnar.OnItemClickListener;
import view.HomeDiagram;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.DayStep;
import bean.MinuteStep;
import bean.StepHisData;
import comm.CommHelper;
import db.DBbase;
import helper.DatabaseHelper;
import db.DbSqlLite;
import constant.Cons;

public class StepDataActivity extends ActionBarActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView iv_step, iv_fire, iv_person, iv_history;
	private RelativeLayout linear, layout_actionbar;
	private TextView tv_data, tv_step, tv_cal, tv_low_step, tv_high_step,
			tv_average_step, tv_distance, tv_time_length,total_step;
	private String sex;
	private LinearLayout ll_exit;
	private DatabaseHelper database;
	private DBbase base;
	private List<StepHisData> hisdatas;
	private List<MinuteStep> mlist,mflist;
	private int height,weight; 
	private int pwidth;  
	private HomeDiagram homeDiagram;
	//private StepService msgService;
	private SimpleStepService simpleStep;
	private DecimalFormat df=new DecimalFormat("#.##");
	private boolean istoday=true;
	ServiceConnection myconn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			simpleStep = ((SimpleStepService.StepBinder) service).getStepBinder();
			showBro(simpleStep.getNowTotalStep());
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub v
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_step_data); 
		database=new DatabaseHelper(this);
		String mid=SPHelper.getBaseMsg(this, "mid",""); 
		base=new DbSqlLite(this,database);
		initView();
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		pwidth= wm.getDefaultDisplay().getWidth()-20;
		Intent service = new Intent(this, SimpleStepService.class);
		//Intent service = new Intent("com.rabbit.run.service.SimpleStepService");     
		this.bindService(service, myconn, Context.BIND_AUTO_CREATE);
		mflist=new ArrayList<MinuteStep>();
		showPoint();
	}
	
	private void showPoint() {
		int point2 = SPHelper.getDetailMsg(this, "point2", 0);
		if(point2==0){
			final LinearLayout ll_point = (LinearLayout)findViewById(R.id.ll_point);
			ll_point.setVisibility(View.VISIBLE);
			ll_point.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ll_point.setVisibility(View.GONE);
					SPHelper.setDetailMsg(StepDataActivity.this, "point2", 1);
				}
			});
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unbindService(myconn);
	}
	
	private void setData(int totalstep,int faststep,int cal,int dis,int time,double maxspeed,double minspeed,double avespeed) {
		tv_step.setText(String.valueOf(faststep));
		total_step.setText(String.valueOf(totalstep));
		tv_cal.setText(String.valueOf(cal));
		tv_distance.setText(String.valueOf(dis));
		tv_time_length.setText(time+"");
		tv_low_step.setText(df.format(minspeed));
		tv_high_step.setText(df.format(maxspeed));
		tv_average_step.setText(df.format(avespeed));
	}

	public void showCol() {
		hisdatas=getDate();
		getHisData(hisdatas);
		HomeColumnar columnar = new HomeColumnar(StepDataActivity.this, hisdatas,
				this,pwidth); 
		linear.addView(columnar);
		StepHisData data= hisdatas.get(hisdatas.size()-1);
		mlist=getStepDataInDay(data.getYear(), data.getMonth(), data.getDay());
		initData(data.getSteps(),data.getFaststep());
		CommHelper.insert_visit(this,"hissteppg");
	} 
	void getHisData(List<StepHisData> hisdatas){
		for(StepHisData data:hisdatas){
			DayStep step= base.GetOneDay(data.getYear(),data.getMonth() ,data.getDay());
			data.setSteps(step.getTotal_step());
			data.setFaststep(step.getFast_step());
		}
	}
	
	List<StepHisData> getDate(){
		GregorianCalendar gc=new GregorianCalendar();
		List<StepHisData> hisdates=new ArrayList<StepHisData>();
		for(int i=-7;i<0;i++){	
			gc.setTime(new Date());
			gc.add(5, i);
			StepHisData hisdate=new StepHisData();
			hisdate.setYear(gc.get(Calendar.YEAR));
			hisdate.setMonth(gc.get(Calendar.MONTH)+1);
			hisdate.setDay(gc.get(Calendar.DAY_OF_MONTH));
			hisdates.add(hisdate);
		}
		return hisdates;
	}
	
	public void showBro(DayStep step) {
		Calendar rightNow = Calendar.getInstance(); 
		int year=rightNow.get(Calendar.YEAR);
		int month=rightNow.get(Calendar.MONTH)+1;
		int day=rightNow.get(Calendar.DAY_OF_MONTH);
		mlist=getStepDataInDay(year, month, day);
		List<Integer> lists = groupStepDatas(mlist);
		//List<Integer> lists = new ArrayList<Integer>(); 
		linear = (RelativeLayout) findViewById(R.id.linear);
		homeDiagram = new HomeDiagram(this, lists,pwidth/12); 
		homeDiagram.setClickable(false);
		initData(step.getTotal_step(),step.getFast_step());  
		sex = SPHelper.getDetailMsg(this, Cons.APP_SEX, 
				getString(R.string.appsex_man));
		if (sex.equals(getResources().getString(R.string.appsex_man))) {
			homeDiagram.setColor(
					0xff3D98FF,
					R.drawable.icon_point_blue,
					new int[] { Color.argb(100, 61, 152, 255),
							Color.argb(45, 61, 152, 255),
							Color.argb(10, 61, 152, 255) });
		}
		linear.addView(homeDiagram); 
		CommHelper.insert_visit(this,"todaysteppg");
	}
	
	void initData(int total,int fast){
		int cal=(int)StepTool.calc_calories(weight, fast, total-fast);
		int dis = StepTool.calStepDistance(height,total-fast,fast);
		int min=(int)StepTool.cal_total_time(fast, total-fast);
		filterList();
		int ave_step_num=mflist.size()==0?0:getFastSteps()/mflist.size();
		int max_step_num=mflist.size()==0?0:getMaxMinHisData(true).getSteps();
		int min_step_num=mflist.size()==0?0:getMaxMinHisData(false).getSteps();
		double max_speed = StepTool.calStepSpeed(max_step_num*60,height);
		double min_speed = StepTool.calStepSpeed(min_step_num*60,height);
		double ave_speed = StepTool.calStepSpeed(ave_step_num*60,height);
		setData(total,fast,cal,dis,min,max_speed,min_speed,ave_speed);  
	}
	
	int getFastSteps(){
		int pstep=0;
		for(MinuteStep item:mflist){
			pstep+=item.getSteps();
		}
		return pstep;
	}
	
	void filterList(){ 
		mflist.clear();
		for(MinuteStep item:mlist){
			if(item.getSteps()>=Cons.FIRSTSTEP){
				mflist.add(item);
			}
		}
	}
	
	HashMap filterMinSteps(List<MinuteStep> mlist){
		int total=0;
		int fast=0;
		for(MinuteStep step : mlist){
			if(StepTool.isFastStep(step.getSteps(), 1)){
				fast+=step.getSteps();
			}
			total+=step.getSteps();
		}
		HashMap map=new HashMap();
		map.put("total", total);
		map.put("fast", fast);
		return map;
	}
	
	List<Integer> groupStepDatas(List<MinuteStep> steps){
		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		for(int i=0;i<24;i+=2){
			int ehour=i+2;
			int total=0;
			for(MinuteStep item :steps){
				if(item.getHour()>=i&&item.getHour()<ehour){
					total+=item.getSteps();
				}
			}
			list.add(total==0?1:total);
		}
		return list;
	}
	
	private int getViewWidth(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		view.measure(w, h); 
		int width =view.getMeasuredWidth();
		return width;
	}
	
	List<MinuteStep> getStepDataInDay(int year,int month,int day){
		return  base.GetOneDaySteps(year, month, day);
	}

	public int getRandom(int min, int max) {
		return (int) Math.round(Math.random() * (max - min) + min);
	}
	
	private MinuteStep getMaxMinHisData(boolean ismax){
		int index=0;
		int initnum=ismax?0:mflist.get(0).getSteps();
		for(int i=0;i<mflist.size();i++){
			int item=mflist.get(i).getSteps();
			if(ismax? item>initnum:item<initnum){
				index=i;
				initnum=item;
			}
		}
		return mflist.get(index);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		layout_actionbar = (RelativeLayout) findViewById(R.id.layout_actionbar);
		layout_actionbar.setBackgroundResource(BGHelper.setBackground(this, SPHelper.getDetailMsg(this, Cons.APP_SEX,"M")));
		linear = (RelativeLayout) findViewById(R.id.linear);

		String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX,
				getString(R.string.appsex_man));
		iv_person = (ImageView)findViewById(R.id.iv_person);
		iv_person.setImageResource(BGHelper.setIconPerson(this, sex));
		iv_step = (ImageView) findViewById(R.id.iv_step);
		iv_step.setImageResource(BGHelper.setIconStep(this, sex));
		iv_fire = (ImageView) findViewById(R.id.iv_fire);
		iv_fire.setImageResource(BGHelper.setIconFire(this, sex));
		iv_history = (ImageView) findViewById(R.id.iv_history);
		iv_history.setOnClickListener(this);
		ll_exit = (LinearLayout) findViewById(R.id.ll_exit);
		ll_exit.setOnClickListener(this);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_step = (TextView) findViewById(R.id.tv_step);
		tv_cal = (TextView) findViewById(R.id.tv_cal);
		tv_low_step = (TextView) findViewById(R.id.tv_low_step);
		tv_high_step = (TextView) findViewById(R.id.tv_high_step);
		tv_average_step = (TextView) findViewById(R.id.tv_average_step);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_time_length = (TextView) findViewById(R.id.tv_time_length);
		total_step= (TextView) findViewById(R.id.total_step);
		height=SPHelper.getDetailMsg(this, "height", 170);// ConfigHelper1.getInt(this, "height");
		weight=SPHelper.getDetailMsg(this, "weight",160);
	}
	void initViewData(){
		if(istoday){
			iv_history.setVisibility(View.VISIBLE);
			linear.removeAllViews();
			showBro(simpleStep.getNowTotalStep());
			tv_data.setText(getString(R.string.step_data_today));			
		}else{
			iv_history.setVisibility(View.GONE);
			linear.removeAllViews();
			showCol();
			tv_data.setText(getString(R.string.step_data_histort));
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) { 
		//case R.id.exit:
		case R.id.ll_exit:
			if(istoday){ 
				this.finish();
			}else{
				istoday=true;
				initViewData();
			}
			break;
		case R.id.iv_history:
			istoday=false;
			initViewData();
			break;
		default:
			break;
		}
	} 
	@Override
	public void OnItemClick(int position) {
		// TODO Auto-generated method stub
		//setData();
		StepHisData hisdata= hisdatas.get(position);
		mlist=getStepDataInDay(hisdata.getYear(), hisdata.getMonth(),hisdata.getDay());
		initData(hisdata.getSteps(),hisdata.getFaststep());		
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(istoday){
			finish();
		}else{
			istoday=true;
			initViewData();
		}
		return false;
	}  
}
